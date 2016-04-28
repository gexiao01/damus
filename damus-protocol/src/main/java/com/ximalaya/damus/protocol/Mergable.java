package com.ximalaya.damus.protocol;

import com.ximalaya.damus.common.exception.DamusException;

public interface Mergable<T> {

    T merge(T other) throws DamusException;
}
