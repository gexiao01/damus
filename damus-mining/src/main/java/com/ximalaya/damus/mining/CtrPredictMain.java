package com.ximalaya.damus.mining;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.evaluation.RegressionMetrics;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;

import scala.Tuple2;

import com.ximalaya.damus.mining.model.CtrDataPoint;
import com.ximalaya.damus.mining.model.DataPoint;
import com.ximalaya.damus.mining.spark.CtrDataPointReader;
import com.ximalaya.damus.mining.spark.DataPointMapper;

/**
 * @author xiao.ge
 * @date 20161221
 */
public class CtrPredictMain {

	static Logger logger = Logger.getLogger(CtrPredictMain.class);

	@SuppressWarnings("serial")
	public static void main(String[] args) {
		// args = new String[] { "./data/test/deviceId-adItemId.show",
		// "./data/test/deviceId-adItemId.click",
		// "file:/Users/xmly/git/damus/damus-mining/data/test/output" };
		// args = new String[] {
		// "/Users/xmly/deviceId-adItemId-2016011821.show",
		// "/Users/xmly/deviceId-adItemId-2016011821.click" };

		String currentDir = new File(".").getAbsolutePath();
		args = new String[] { "./data/province-ad-2015011821/province-adItemId-2016011821.show",
				"./data/province-ad-2015011821/province-adItemId-2016011821.click",
				"file:" + currentDir + "/data/province-ad-2015011821/output" };

		JavaSparkContext sc = new JavaSparkContext("local[2]", "CtrPredict");
		SparkContext rawSc = JavaSparkContext.toSparkContext(sc);

		// 加载点击和展现数据并merge到一起
		JavaPairRDD<Tuple2<Integer, Integer>, CtrDataPoint> showDps = sc.textFile(args[0], 6).mapToPair(
				new CtrDataPointReader(true));
		JavaPairRDD<Tuple2<Integer, Integer>, CtrDataPoint> clickDps = sc.textFile(args[1], 6).mapToPair(
				new CtrDataPointReader(false));

		JavaPairRDD<Tuple2<Integer, Integer>, DataPoint> ctrDps = showDps.union(clickDps)
				.reduceByKey(new Function2<CtrDataPoint, CtrDataPoint, CtrDataPoint>() {
					@Override
					public CtrDataPoint call(CtrDataPoint pt1, CtrDataPoint pt2) throws Exception {
						return pt1.merge(pt2);
					}
				}).mapToPair(new DataPointMapper<CtrDataPoint>());

		// 一半做训练，一半做校验
		JavaPairRDD<Tuple2<Integer, Integer>, DataPoint> trainDps = ctrDps.sample(false, 0.5D);
		JavaPairRDD<Tuple2<Integer, Integer>, DataPoint> checkDps = ctrDps.subtract(trainDps);

		// 构造Rating对象作为训练模型输入
		JavaRDD<Rating> trainRatings = trainDps.values().map(new Function<DataPoint, Rating>() {
			@Override
			public Rating call(DataPoint pt) throws Exception {
				return new Rating(pt.getUser(), pt.getProduct(), pt.getRating());
			}
		});

		// 训练模型
		MatrixFactorizationModel model = ALS.train(trainRatings.rdd(), 50, 10, 0.01);
		model.save(rawSc, args[2] + "/model");

		// 如果之前save过，可以直接load就不需要重新训练了
		// MatrixFactorizationModel.load(rawSc,args[3] +"/model");

		// 单个预测
		// double predictResult = model.predict(11509,
		// "ffffffff-ffff-0801-0205-376c0037d7ef"
		// .hashCode());
		// System.err.println(predictResult);

		// 校验集的原数据（期望值）
		JavaPairRDD<Tuple2<Integer, Integer>, Double> checkExpects = checkDps
				.mapToPair(new PairFunction<Tuple2<Tuple2<Integer, Integer>, DataPoint>, Tuple2<Integer, Integer>, Double>() {
					@Override
					public Tuple2<Tuple2<Integer, Integer>, Double> call(Tuple2<Tuple2<Integer, Integer>, DataPoint> t)
							throws Exception {
						return new Tuple2<Tuple2<Integer, Integer>, Double>(t._1(), t._2().getRating());
					}
				});

		// 对整个校验集进行预测
		JavaPairRDD<Tuple2<Integer, Integer>, Double> checkPredicts = model.predict(
				checkDps.values().mapToPair(new PairFunction<DataPoint, Integer, Integer>() {
					@Override
					public Tuple2<Integer, Integer> call(DataPoint pt) throws Exception {
						return new Tuple2<Integer, Integer>(pt.getUser(), pt.getProduct());
					}
				})).mapToPair(new PairFunction<Rating, Tuple2<Integer, Integer>, Double>() {
			@Override
			public Tuple2<Tuple2<Integer, Integer>, Double> call(Rating rating) throws Exception {
				return new Tuple2<Tuple2<Integer, Integer>, Double>(new Tuple2<Integer, Integer>(rating.user(), rating
						.product()), rating.rating());
			}
		});

		// 期望值和预测值join起来
		JavaPairRDD<Tuple2<Integer, Integer>, Tuple2<Double, Double>> predictionAndObservationsDouble = checkExpects
				.join(checkPredicts);

		// 保存结果供查看
		predictionAndObservationsDouble.repartition(1).saveAsTextFile(args[2] + "/predict");

		// 计算校验集上的均方误差（RMSE）
		JavaRDD<Tuple2<Object, Object>> predictionAndObservations = predictionAndObservationsDouble.values().map(
				new Function<Tuple2<Double, Double>, Tuple2<Object, Object>>() {
					@Override
					public Tuple2<Object, Object> call(Tuple2<Double, Double> tuple) throws Exception {
						return new Tuple2<Object, Object>(tuple._1(), tuple._2);
					}
				});

		RegressionMetrics regressionMetrics = new RegressionMetrics(predictionAndObservations.rdd());
		System.err.println("rootMeanSquaredError: " + regressionMetrics.rootMeanSquaredError());
		sc.close();
	}
}
