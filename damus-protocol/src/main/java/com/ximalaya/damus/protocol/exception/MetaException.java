package com.ximalaya.damus.protocol.exception;

import com.ximalaya.damus.common.exception.DamusException;

public class MetaException extends DamusException {

    /**
     *  
     */
    private static final long serialVersionUID = -440852299950398724L;

    public MetaException() {
        super();
    }

    public MetaException(String msg) {
        super(msg);
    }

    public MetaException(String msg, Throwable e) {
        super(msg, e);
    }
}
