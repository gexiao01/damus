package com.ximalaya.damus.protocol.util;

import java.io.IOException;
import java.io.InputStream;

import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.Serializable;

public class SerializeUtil {

    public static long bytesToLong(byte[] bs) {
        long l = 0L;
        for (int i = bs.length - 1; i >= 0; i--) {
            l = (l <<= 8) + bs[i] + (bs[i] < 0 ? 0x100 : 0);
        }
        return l;
    }

    public static byte[] longToBytes(long l, int bytes) {
        byte[] bs = new byte[bytes];
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return bs;
    }

    public static <T extends Serializable> T deserializeFromStream(InputStream is, Class<T> clz)
            throws DamusException, IOException {
        try {
            T t = clz.newInstance();
            t.deserialize(is);
            return t;
        } catch (Exception e) {
            throw new DamusException(e);
        }

    }
}
