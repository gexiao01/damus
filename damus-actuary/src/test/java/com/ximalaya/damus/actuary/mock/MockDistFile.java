package com.ximalaya.damus.actuary.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.meta.DimDict;
import com.ximalaya.damus.protocol.reduced.ReducedFlow;
import com.ximalaya.damus.protocol.resource.FileResource;
import com.ximalaya.damus.protocol.resource.Resource;
import com.ximalaya.damus.protocol.util.SerializeUtil;

/**
 * @author xiao.ge
 * @date 20151221
 */
public class MockDistFile {

	@SuppressWarnings("unchecked")
	public static void lateralToBinary(String fromPath, String toPath, DimDict dict) throws IOException, DamusException {
		List<String> lines = FileUtils.readLines(new File(fromPath));
		OutputStream os = new FileOutputStream(toPath);
		for (String line : lines) {
			ReducedFlow.fromPrettyFormat(line, dict).serialize(os);
		}
		os.close();
	}

	public static void binaryToLateral(String fromPath, String toPath, DimDict dict) throws IOException, DamusException {

		InputStream is = new FileInputStream(fromPath);
		List<String> lines = new ArrayList<String>();
		while (is.available() > 0) {
			ReducedFlow flow = SerializeUtil.deserializeFromStream(is, ReducedFlow.class);
			lines.add(flow.toPrettyFormat(dict));
		}
		is.close();
		FileUtils.writeLines(new File(toPath), lines);
	}

	public static void main(String[] args) throws DamusException, IOException {

		// mock("./data/dist/dimdict", "./data/dist/reducedFlow.literal",
		// "./data/dist/reducedFlow",
		// false);
		// mock("./data/dist/dimdict", "./data/dist/reducedFlow",
		// "./data/dist/reducedFlow.check",
		// true);

		mock("./data/quasi/damus.meta", "./data/quasi/-damus-reduced-flow..temp-part-00000",
				"./data/quasi/reduceFlow.literal", true);
	}

	static void mock(String dictPath, String fromPath, String toPath, boolean binaryToLateral) throws IOException,
			DamusException {
		Resource<DimDict> dimDictResource = new FileResource<DimDict>(dictPath, DimDict.class);
		DimDict dict = dimDictResource.lazyGet();

		if (binaryToLateral) {
			binaryToLateral(fromPath, toPath, dict);
		} else {
			lateralToBinary(fromPath, toPath, dict);
		}
	}
}
