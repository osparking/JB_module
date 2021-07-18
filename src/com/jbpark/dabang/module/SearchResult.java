package com.jbpark.dabang.module;

public class SearchResult {
	int totalRow;
	private RoadAddress[] addresses;
	private int addrCount;
	
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
	public int getAddrCount() {
		return addrCount;
	}
	public void setAddressCount(int addressCount) {
		this.addrCount = addressCount;
	}
} 