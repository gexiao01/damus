package com.ximalaya.damus.protocol.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.Serializable;
import com.ximalaya.damus.protocol.util.SerializeUtil;

public class HdfsResource<T extends Serializable> extends Resource<T> {

	private static final Configuration conf = new Configuration();
	private static FileSystem fs;
	static {
		conf.addResource(HdfsResource.class.getResource("/core-site.xml"));
		conf.addResource(HdfsResource.class.getResource("/hdfs-site.xml"));
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			logger.error("Fail to load FileSystem: " + conf, e);
			System.exit(1);
		}
	}

	private InputStream is = null;
	private OutputStream os = null;
	private boolean overwrite = true;

	public HdfsResource(String path, Class<T> clz) {
		super(path, clz);
	}

	@Override
	public void load() throws DamusException {
		try {
			InputStream is = getInputStream();
			T newContent = SerializeUtil.deserializeFromStream(is, clz);
			this.content = newContent;
		} catch (IOException e) {
			logger.error("Fail to load resource: " + path, e);
			throw new DamusException(e);
		} finally {
			closeInputStream();
		}
	}

	@Override
	public void save() throws DamusException {
		try {
			OutputStream os = getOutputStream();
			content.serialize(os);
		} catch (IOException e) {
			logger.error("Fail to save resource: " + getPath(), e);
			throw new DamusException(e);
		} finally {
			closeOutputStream();
		}
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public InputStream getInputStream() throws IOException {

		if (is == null) {
			String path = getPath();
			is = fs.open(new Path(path));
		}
		return is;
	}

	@Override
	public void closeInputStream() {
		IOUtils.closeStream(is);
		// IOUtils.closeWithLog(fs);
	}

	@Override
	public OutputStream getOutputStream() throws IOException {

		if (os == null) {
			String path = getPath();
			os = fs.create(new Path(path), overwrite);
		}
		return os;
	}

	@Override
	public void remove() throws IOException {
		String path = getPath();
		fs.delete(new Path(path), true);
	}

	@Override
	public boolean exists() throws IOException {
		String path = getPath();
		return fs.exists(new Path(path));
	}

	@Override
	public void closeOutputStream() {
		IOUtils.closeStream(os);
		// IOUtils.closeWithLog(fs);
	}

	@Override
	public void close() throws IOException {
		closeInputStream();
		closeOutputStream();
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

}
