package com.ximalaya.damus.protocol.dist;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.Mergable;
import com.ximalaya.damus.protocol.Serializable;
import com.ximalaya.damus.protocol.config.Constant;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.exception.DistException;
import com.ximalaya.damus.protocol.exception.MetaException;

/**
 * entity of one dimensitonal flow distribution result
 *
 * @author xiao.ge
 * @date 20151130
 */
public class OneDDist implements Serializable, Mergable<OneDDist> {

	private static final long serialVersionUID = -1530982244499220614L;
	// type id flow
	private final EnumMap<DimType, Map<Long, Long>> dists;
	private transient long total;

	public OneDDist() {
		dists = new EnumMap<DimType, Map<Long, Long>>(DimType.class);
		// pre-put all enums so that dist.size() is always constant
		for (DimType type : DimType.values()) {
			dists.put(type, new HashMap<Long, Long>());
		}
	}

	public void put(DimType type, long id, long flow) {
		dists.get(type).put(id, flow);
	}

	public void add(DimType type, long id, long flow) {
		Long sourceSum = dists.get(type).get(id);
		if (sourceSum == null) {
			sourceSum = 0L;
		}
		put(type, id, sourceSum + flow);
	}

	public long get(DimType type, long id) {
		Long number = dists.get(type).get(id);
		if (number == null) {
			// xiao.ge: in general queryed id should be in dict (thus in dist)
			// if you do request an unexisting id, then there is not
			// corresponding flow exist, so
			// return 0
			return 0;
		}
		return number.longValue();
	}

	public Map<Long, Long> getDimTypeMap(DimType type) {
		return dists.get(type);
	}

	@Override
	public void serialize(OutputStream os) throws DamusException, IOException {

		List<String> lines = new ArrayList<String>();

		for (DimType type : dists.keySet()) {
			Map<Long, Long> dist = dists.get(type);
			for (Entry<Long, Long> entry : dist.entrySet()) {
				// type id value
				String line = StringUtils.join(Arrays.asList(type.name(), entry.getKey() + "", entry.getValue() + ""),
						Constant.DICT_ITEM_SEPERATOR);
				lines.add(line);
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
				long flow = Long.parseLong(items[2]);

				this.put(type, id, flow);
			} catch (Exception e) {
				throw new MetaException("Corrupted dict " + line);
			}
		}

		calcTotalFlow();
	}

	private void calcTotalFlow() throws DistException {
		total = 0L;

		for (DimType type : dists.keySet()) {
			long total0 = 0L;
			for (long flow : dists.get(type).values()) {
				total0 += flow;
			}

			if (total == 0) {
				total = total0;
			} else if (total != total0) {
				throw new DistException("Total Flow not consistent " + type + " " + total + "/" + total0);
			}
		}
	}

	public long getTotal() {
		return total;
	}

	@Override
	public OneDDist merge(OneDDist other) throws DamusException {
		for (DimType type : DimType.values()) {
			for (Entry<Long, Long> entry : other.getDimTypeMap(type).entrySet()) {
				add(type, entry.getKey(), entry.getValue());
			}
		}
		return this;
	}
}
