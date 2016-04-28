package com.ximalaya.damus.protocol.config;

public class Constant {

    // do not use -1 to avoid serializing problems
    public static final long UNKNOWN_ID = 0L;
    // stands for a totally unrecognized target. this id should not be in dict
    public static final long NOT_FOUND_ID = Long.MIN_VALUE;
    public static final String UNKNOWN_VALUE = "<UNKNOWN>";
    public static final String DICT_ITEM_SEPERATOR = "\t";
    public static final String ENCODING = "utf-8";
}
