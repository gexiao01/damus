package com.ximalaya.damus.protocol.reduced;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumMap;

import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.Serializable;
import com.ximalaya.damus.protocol.config.Constant;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.meta.DimDict;
import com.ximalaya.damus.protocol.util.SerializeUtil;

/**
 * Entity of reduced flow data, persisted on local disk of hdfs
 *
 * @author xiao.ge
 * @date 20151130
 *
 */
public class ReducedFlow implements Serializable {

    private static final long serialVersionUID = -1335991493877334143L;
    // the dimensions of this flow
    private final EnumMap<DimType, Long> dims;

    public ReducedFlow() {
        dims = new EnumMap<DimType, Long>(DimType.class);
        // pre-put all enums so that dist.size() is always constant
        for (DimType type : DimType.values()) {
            dims.put(type, Constant.UNKNOWN_ID);
        }
    }

    public EnumMap<DimType, Long> getDims() {
        return dims;
    }

    /**
     * @param id dimension value id
     * @param dict used to check if id exists in dictionary, if not, will put UNKNOWN_ID instead of
     *            id
     */
    public void put(DimType type, long id, DimDict dict) {
        dims.put(type, dict.getDimMeta(type).containsId(id) ? id : Constant.UNKNOWN_ID);
    }

    public void put(DimType type, String val, DimDict dict) {
        dims.put(type, dict.getDimMeta(type).getId(val));
    }

    public long get(DimType type) {
        return dims.get(type);
    }

    @Override
    public void serialize(OutputStream os) throws DamusException, IOException {
        for (DimType type : dims.keySet()) {
            os.write(SerializeUtil.longToBytes(dims.get(type), type.getIdBytes()));
        }
    }

    @Override
    public void deserialize(InputStream is) throws DamusException, IOException {
        for (DimType type : dims.keySet()) {
            byte[] bs = new byte[type.getIdBytes()];
            is.read(bs);
            dims.put(type, SerializeUtil.bytesToLong(bs));
        }
    }

    /**
     * util for testing & logging
     *
     * @param dict
     */
    public String toPrettyFormat(DimDict dict) {
        StringBuilder sb = new StringBuilder();
        for (DimType type : DimType.values()) {
            sb.append(type.name()).append("=").append(
                    type.isLiteral() ? dict.getDimMeta(type).getValue(dims.get(type)) : dims
                            .get(type)).append(",");
        }
        return sb.toString();
    }

    /**
     * util for testing
     *
     * @param dict
     */
    public static ReducedFlow fromPrettyFormat(String line, DimDict dict) {
        ReducedFlow flow = new ReducedFlow();
        for (String term : line.split(",")) {
            String[] items = term.split("=");
            try {
                DimType dimType = DimType.valueOf(items[0]);
                Long value = dict.getDimMeta(dimType).getId(items[1]);
                flow.dims.put(dimType, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flow;
    }

    @Override
    public String toString() {
        return dims.toString();
    }
}
