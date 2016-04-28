package com.ximalaya.damus.protocol.config;

public enum DimType {

    POSITION(byte.class, 1, true),
    PROVINCE(short.class, 2, true),
    CITY(short.class, 2, true),
    VERSION(short.class, 2, true),
    RESOLUTION(short.class, 2, true),
    PACKAGE(short.class, 2, true),
    CARRIER(byte.class, 1, true),
    OS(short.class, 2, true),
    NETWORK(byte.class, 1, true),
    HOUR(byte.class, 1, false),
    APP(short.class, 2, false);

    // only used for minimizing size when persisting reduced data
    private final Class<?> idClass;
    private final int idBytes;
    private boolean literal; // only true to maintain dict

    private static int recordLength;
    static {
        for (DimType type : DimType.values()) {
            recordLength += type.getIdBytes();
        }
    }

    private DimType(Class<?> idClass, int idBytes, boolean literal) {
        this.idClass = idClass;
        this.idBytes = idBytes;
        this.literal = literal;
    }

    public Class<?> getIdClass() {
        return idClass;
    }

    public int getIdBytes() {
        return idBytes;
    }

    public boolean isLiteral() {
        return literal;
    }

    public static int getRecordLength() {
        return recordLength;
    }

}
