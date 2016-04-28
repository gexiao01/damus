package com.ximalaya.damus.offline.spark.io;

import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordWriter;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Progressable;

public class ReducedFlowOutputFormat extends FileOutputFormat<NullWritable, BytesWritable> {

	protected static class LineRecordWriter implements RecordWriter<NullWritable, BytesWritable> {

		protected DataOutputStream out;

		public LineRecordWriter(DataOutputStream out) {
			this.out = out;
		}

		@Override
		public synchronized void write(NullWritable key, BytesWritable value) throws IOException {
			out.write(value.getBytes());
			// out.write(newline);
		}

		@Override
		public synchronized void close(Reporter reporter) throws IOException {
			out.close();

		}
	}

	@Override
	public RecordWriter<NullWritable, BytesWritable> getRecordWriter(FileSystem ignored, JobConf job, String name,
			Progressable progress) throws IOException {
		Path file = FileOutputFormat.getTaskOutputPath(job, name);
		FileSystem fs = file.getFileSystem(job);
		FSDataOutputStream fileOut = fs.create(file, progress);
		return new LineRecordWriter(fileOut);
	}

}
