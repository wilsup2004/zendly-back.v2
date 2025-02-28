package com.weight.services;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Pagination {

	@JsonProperty("limit")
	private int limit;
	
	@JsonProperty("offset")
	private int offset;
	
	@JsonProperty("count")
	private int count;
	
	@JsonProperty("total")
	private int total;
	
	
	
	public Pagination() {
		super();
	}
	
	
	public Pagination(int limit, int offset, int count, int total) {
		super();
		this.limit = limit;
		this.offset = offset;
		this.count = count;
		this.total = total;
	}


	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	
}
