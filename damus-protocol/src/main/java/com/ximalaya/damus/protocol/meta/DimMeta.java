package com.ximalaya.damus.protocol.meta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ximalaya.damus.protocol.Mergable;
import com.ximalaya.damus.protocol.config.Constant;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.exception.MergeException;
import com.ximalaya.damus.protocol.exception.MetaException;

/**
 * meta info for a single dimension
 *
 * TODO make it thread safe
 *
 * @author xiao.ge
 * @date 20151130
 *
 */
public class DimMeta implements Mergable<DimMeta>, Serializable {

	private static final long serialVersionUID = -5481059669016809168L;

	private final DimType type;

	// following fields should be readonly except during merging
	private Map<String, Long> valueMap = new HashMap<String, Long>();
	private Map<Long, String> idMap = new HashMap<Long, String>();
	private long maxId;

	public DimMeta(DimType type) {
		this.type = type;
		// XXX by xiao.ge: not thread safe
		// change to ConcurrentHashMap if concurrency is needed
		try {
			// Default mapping for unknown
			this.put(Constant.UNKNOWN_ID, Constant.UNKNOWN_VALUE);
		} catch (MetaException e) {
			// this is safe, will not throw
		}
	}

	public DimType getType() {
		return type;
	}

	public void put(long id, String value) throws MetaException {
		if (!type.isLiteral()) {
			return;
		}
		if (containsDifferentValue(idMap, id, value)) {
			throw new MetaException("id " + id + " already exists");
		} else if (containsDifferentValue(valueMap, value, id)) {
			throw new MetaException("value " + value + " already exists");
		}
		idMap.put(id, value);
		valueMap.put(value, id);

		if (id > maxId) {
			maxId = id;
		}
	}

	private <K, V> boolean containsDifferentValue(Map<K, V> map, K key, V newValue) {
		V value = map.get(key);
		return value != null && !value.equals(newValue);
	}

	/**
	 * Only used for recording unrecognized value
	 *
	 * @param value
	 * @throws MetaException
	 */
	public void putNew(String value) throws MetaException {
		if (!type.isLiteral()) {
			return;
		}
		put(maxId++, value);
	}

	public String getValue(long id) {
		if (!type.isLiteral()) {
			return id + "";
		}
		String value = idMap.get(id);
		return value != null ? value : Constant.UNKNOWN_VALUE;
	}

	public boolean containsId(long id) {
		if (!type.isLiteral()) {
			return true;
		}
		return idMap.containsKey(id);
	}

	public Map<Long, String> getIdMap() {
		return idMap;
	}

	public Map<String, Long> getValueMap() {
		return valueMap;
	}

	public long getId(String value) {
		if (!type.isLiteral()) {
			return Long.valueOf(value);
		}
		Long id = valueMap.get(value);
		if (id == null) {
			return Constant.UNKNOWN_ID;
		}

		return id.longValue();
	}

	public long getMaxId() {
		return maxId;
	}

	@Override
	public DimMeta merge(DimMeta other) throws MergeException, MetaException {
		if (other == null) {
			return this;
		} else if (other.type != this.type) {
			throw new MergeException("type do not equal");
		}

		for (Entry<Long, String> entry : other.idMap.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}

		return this;
	}

	@Override
	public String toString() {
		return valueMap.toString();
	}
}
