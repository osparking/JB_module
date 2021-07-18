package com.jbpark.dabang.module;

public class SearchResult {
	int totalRow;
	private RoadAddress[] addresses;
	private int addressCount;
	
	public int getTotalRow() {
		return totalRow;
	}
	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}
	public RoadAddress[] getAddresses() {
		return addresses;
	}
	public void setAddresses(RoadAddress[] addresses) {
		this.addresses = addresses;
	}
	public int getAddressCount() {
		return addressCount;
	}
	public void setAddressCount(int addressCount) {
		this.addressCount = addressCount;
	}
} 