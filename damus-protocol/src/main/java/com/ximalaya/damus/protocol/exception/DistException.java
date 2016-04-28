package com.ximalaya.damus.protocol.exception;

import com.ximalaya.damus.common.exception.DamusException;

public class DistException extends DamusException {

    /**
     *  
     */
    private static final long serialVersionUID = -440852299950398724L;

    public DistException() {
        super();
    }

    public DistException(String msg) {
        super(msg);
    }

    public DistException(String msg, Throwable e) {
        super(msg, e);
    }
}
