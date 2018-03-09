package com.md.studio.domain;
import java.io.Serializable;


public class SiteUserRate implements Serializable {
	private static final long serialVersionUID = -6361783325007486763L;

	private int rate;
	private long count;

	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
}
