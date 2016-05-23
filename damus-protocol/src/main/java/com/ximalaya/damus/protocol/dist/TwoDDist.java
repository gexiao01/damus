package com.ximalaya.damus.protocol.dist;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.Mergable;
import com.ximalaya.damus.protocol.Serializable;
import com.ximalaya.damus.protocol.config.Constant;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.exception.DistException;
import com.ximalaya.damus.protocol.exception.MetaException;

/**
 * entity of two dimensitonal flow distribution result
 *
 * @author xiao.ge
 * @date 20160521
 */
public class TwoDDist implements Serializable, Mergable<TwoDDist> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9175639979457640626L;
	// type id flow
	private final Table<DimType, DimType, Table<Long, Long, Long>> dists;
	private transient long total;

	public TwoDDist() {
		dists = HashBasedTable.<DimType, DimType, Table<Long, Long, Long>> create();
		// pre-put all enums so that dist.size() is always constant
		for (DimType type1 : DimType.values()) {
			for (DimType type2 : DimType.values()) {
				dists.put(type1, type2, HashBasedTable.<Long, Long, Long> create());
			}
		}
	}

	public void put(DimType type1, DimType type2, long id1, long id2, long flow) {
		dists.get(type1, type2).put(id1, id2, flow);
	}

	public void add(DimType type1, DimType type2, long id1, long id2, long flow) {
		Long sourceSum = dists.get(type1, type2).get(id1, id2);
		if (sourceSum == null) {
			sourceSum = 0L;
		}
		put(type1, type2, id1, id2, sourceSum + flow);
	}

	public long get(DimType type1, DimType type2, long id1, long id2) {
		Long number = dists.get(type1, type2).get(id1, id2);
		if (number == null) {
			// xiao.ge: in general queryed id should be in dict (thus in dist)
			// if you do request an unexisting id, then there is not
			// corresponding flow exist, so
			// return 0
			return 0;
		}
		return number.longValue();
	}

	public Table<Long, Long, Long> getDimTypeTable(DimType type1, DimType type2) {
		return dists.get(type1, type2);
	}

	@Override
	public void serialize(OutputStream os) throws DamusException, IOException {
		List<String> lines = new ArrayList<String>();

		for (Cell<DimType, DimType, Table<Long, Long, Long>> dist : dists.cellSet()) {
			for (Cell<Long, Long, Long> cell : dist.getValue().cellSet()) {
				// type id value
				String line = StringUtils.join(
						Arrays.asList(dist.getRowKey().name(), cell.getRowKey() + "", dist.getColumnKey().name(),
								cell.getColumnKey() + "", cell.getValue() + ""), Constant.DICT_ITEM_SEPERATOR);
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
				DimType type1 = DimType.valueOf(items[0]);
				DimType type2 = DimType.valueOf(items[1]);
				long id1 = Long.parseLong(items[2]);
				long id2 = Long.parseLong(items[3]);
				long flow = Long.parseLong(items[4]);

				this.put(type1, type2, id1, id2, flow);
			} catch (Exception e) {
				throw new MetaException("Corrupted dict " + line);
			}
		}

		calcTotalFlow();
	}

	private void calcTotalFlow() throws DistException {
		total = -1L;

		for (Cell<DimType, DimType, Table<Long, Long, Long>> dist : dists.cellSet()) {
			long total0 = 0L;
			for (long flow : dist.getValue().values()) {
				total0 += flow;
			}

			if (total == -1L) {
				total = total0;
			} else if (total != total0) {
				throw new DistException("Total Flow not consistent " + dist.getRowKey() + " " + dist.getColumnKey()
						+ " " + total + "/" + total0);
			}
		}
	}

	public long getTotal() {
		return total;
	}

	@Override
	public TwoDDist merge(TwoDDist other) throws DamusException {
		for (DimType type1 : DimType.values()) {
			for (DimType type2 : DimType.values()) {
				for (Cell<Long, Long, Long> cell : other.getDimTypeTable(type1, type2).cellSet()) {
					add(type1, type2, cell.getRowKey(), cell.getColumnKey(), cell.getValue());
				}
			}
		}
		return this;
	}
}
