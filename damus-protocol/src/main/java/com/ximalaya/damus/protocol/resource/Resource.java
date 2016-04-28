package com.ximalaya.damus.protocol.resource;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.Serializable;
import com.ximalaya.damus.protocol.util.SerializeUtil;

/**
 * General interface for persisted resources
 *
 * @author xiao.ge
 * @date 20151204
 *
 * @param <T>
 */
public abstract class Resource<T extends Serializable> implements Closeable {

	protected T content;
	protected final Class<T> clz;
	protected final String path;

	protected static Logger logger = Logger.getLogger(Resource.class);

	private boolean initialized = false;

	public Resource(String path, Class<T> clz) {
		this.path = path;
		this.clz = clz;
	}

	/**
	 * @return the resource stuff, which should be singleton in most cases
	 */
	public T get() {
		return content;
	}

	/**
	 * just try a initialization, may throw Exception
	 *
	 * if you are sure that {@code #load()} is invoked, just use {@code #get()}
	 *
	 * @throws DamusException
	 */
	public T lazyGet() throws DamusException {
		if (!initialized) {
			load();
		}
		return content;
	}

	public void set(T content) {
		this.content = content;
	}

	/**
	 * load stuff from {@link #getPath()}
	 *
	 * @throws IOException
	 * @throws DamusException
	 */
	public void load() throws DamusException {
		if (initialized) {
			logger.warn("resource already initialized: " + getPath());
			return;
		}
		try {
			InputStream is = getInputStream();
			T newContent = SerializeUtil.deserializeFromStream(is, clz);
			this.content = newContent;
			this.initialized = true;
		} catch (IOException e) {
			logger.error("Fail to load resource: " + getPath());
			throw new DamusException(e);
		} finally {
			closeInputStream();
		}

	}

	/**
	 * persist stuff to {@link #getPath()}
	 *
	 * @throws IOException
	 * @throws DamusException
	 */
	public void save() throws DamusException {
		try {
			OutputStream os = getOutputStream();
			content.serialize(os);
		} catch (IOException e) {
			logger.error("Fail to load resource: " + getPath());
			throw new DamusException(e);
		} finally {
			closeOutputStream();
		}
	}

	// flowing stream apis are stateful, we recomend use the stateless apis as
	// above

	public abstract InputStream getInputStream() throws IOException;

	public abstract OutputStream getOutputStream() throws IOException;

	public abstract void remove() throws IOException;

	public abstract boolean exists() throws IOException;

	public abstract void closeInputStream();

	public abstract void closeOutputStream();

	/**
	 * @return path of the resource, will not be constant if in dailyRolling
	 *         mode
	 */
	public abstract String getPath();

	@Override
	public String toString() {
		return getPath();
	}
}
