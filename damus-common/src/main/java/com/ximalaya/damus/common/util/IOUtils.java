package com.ximalaya.damus.common.util;

import java.io.Closeable;
import java.io.IOException;

import org.apache.log4j.Logger;

public class IOUtils {

    static Logger logger = Logger.getLogger(IOUtils.class);

    public static void closeWithLog(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            logger.error("fail to close " + closeable, ioe);
        }
    }
}
