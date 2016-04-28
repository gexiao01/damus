package com.ximalaya.damus.protocol.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.Mergable;
import com.ximalaya.damus.protocol.Serializable;
import com.ximalaya.damus.protocol.config.Constant;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.exception.MergeException;
import com.ximalaya.damus.protocol.exception.MetaException;

/**
 * dictionary of all dimension meta datas, should be persisted but always cached
 * in memory. this class should be used as singleton except during merging
 * process
 *
 * Note:. if the dimension is numberic by itself, it will not be enumerated in
 * meta (DimMetas.get(idMap) is empty)
 *
 * @author xiao.ge
 * @date 20151130
 */
public class DimDict implements Mergable<DimDict>, Serializable {

	private static final long serialVersionUID = 826294588470087396L;
	private EnumMap<DimType, DimMeta> dimMetas;

	public DimDict() {
		dimMetas = new EnumMap<DimType, DimMeta>(DimType.class);
		for (DimType type : DimType.values()) {
			dimMetas.put(type, new DimMeta(type));
		}
		// updateRecordLength();
	}

	@Override
	public DimDict merge(DimDict other) throws MergeException, MetaException {
		if (other == null) {
			return this;
		}

		for (Entry<DimType, DimMeta> dimEntry : other.dimMetas.entrySet()) {
			DimMeta meta = this.dimMetas.get(dimEntry.getKey()); // cannot be
																	// null
			meta.merge(dimEntry.getValue());
		}
		return this;
	}

	public DimMeta getDimMeta(DimType dimType) {
		return dimMetas.get(dimType);
	}

	@Override
	public void serialize(OutputStream os) throws DamusException, IOException {

		List<String> lines = new ArrayList<String>();

		for (Entry<DimType, DimMeta> dimEntry : dimMetas.entrySet()) {
			for (Entry<Long, String> entry : dimEntry.getValue().getIdMap().entrySet()) {
				// type id value
				// 初始化自带的UNKNOW_ID就不用反序列化了..
				if (entry.getKey() != Constant.UNKNOWN_ID) {
					String line = StringUtils.join(
							Arrays.asList(dimEntry.getKey().name(), String.valueOf(entry.getKey()), entry.getValue()),
							Constant.DICT_ITEM_SEPERATOR);
					lines.add(line);
				}
			}
		}
		IOUtils.writeLines(lines, "\n", os, Constant.ENCODING);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserialize(InputStream is) throws DamusException, IOException {

		List<String> lines = IOUtils.readLines(is);

		for (String line : lines) {
			try {
				String[] items = line.split(Constant.DICT_ITEM_SEPERATOR);
				DimType type = DimType.valueOf(items[0]);
				long id = Long.parseLong(items[1]);
				String value = items[2];

				dimMetas.get(type).put(id, value);
			} catch (Exception e) {
				throw new MetaException("Corrupted dict " + line);
			}
		}

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (DimType type : DimType.values()) {
			sb.append(type.name()).append("=")
					.append(this.getDimMeta(type) == null ? "" : this.getDimMeta(type).getIdMap()).append(" ");
		}
		return sb.toString();
	}
}
