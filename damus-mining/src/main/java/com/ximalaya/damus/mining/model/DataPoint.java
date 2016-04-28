package com.ximalaya.damus.mining.model;

import java.io.Serializable;

import scala.Tuple2;

public abstract class DataPoint implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4216710972515598802L;

    private final Tuple2<Integer, Integer> key;

    protected DataPoint(Tuple2<Integer, Integer> key) {
        this.key = key;
    }

    public abstract int getUser();

    public abstract int getProduct();

    public abstract double getRating();

    public Tuple2<Integer, Integer> getKey() {
        return key;
    }
}
