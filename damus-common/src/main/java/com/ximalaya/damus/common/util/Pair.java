package com.ximalaya.damus.common.util;

import java.io.Serializable;

public class Pair<A, B> implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 5759774711782213911L;

	private A a;
	private B b;

	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A getA() {
		return a;
	}

	public B getB() {
		return b;
	}

	public void setA(A a) {
		this.a = a;
	}

	public void setB(B b) {
		this.b = b;
	}

	@Override
	public String toString() {
		return "(" + a + "," + b + ")";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Pair)) {
			return false;
		}
		Pair<A, B> otherPair = (Pair<A, B>) other;
		return ((a == null && otherPair.a == null) || a.equals(otherPair.a))
				&& ((b == null && otherPair.b == null) || b.equals(otherPair.b));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int hashCode() {
		if (a == null && b == null) {
			return 0;
		} else if (a == null) {
			return (b instanceof Enum) ? ((Enum) b).name().hashCode() : b.hashCode();
		} else if (b == null) {
			return (a instanceof Enum) ? ((Enum) a).name().hashCode() : a.hashCode();
		} else {
			int codeA = (a instanceof Enum) ? ((Enum) a).name().hashCode() : a.hashCode();
			int codeB = (b instanceof Enum) ? ((Enum) b).name().hashCode() : b.hashCode();
			return codeA * 31 + codeB;
		}
	}

}
