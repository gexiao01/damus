package com.ximalaya.damus.common.exception;

public class DamusException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 7812415381586383619L;

    public DamusException() {
        super();
    }

    public DamusException(String msg) {
        super(msg);
    }

    public DamusException(Throwable e) {
        super(e);
    }

    public DamusException(String msg, Throwable e) {
        super(msg, e);
    }
}
