package com.ximalaya.damus.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ximalaya.damus.common.exception.DamusException;

public interface Serializable extends java.io.Serializable {

    void serialize(OutputStream os) throws DamusException, IOException;

    void deserialize(InputStream is) throws DamusException, IOException;

}
