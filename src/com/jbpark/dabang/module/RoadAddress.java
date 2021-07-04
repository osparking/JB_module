package com.jbpark.dabang.module;

public class RoadAddress {
	private String newZipcode;
	private String roadName;
	
	public RoadAddress(String newZipcode, String roadName) {
		super();
		this.newZipcode = newZipcode;
		this.roadName = roadName;
	}
	public String getNewZipcode() {
		return newZipcode;
	}
	public void setNewZipcode(String newZipcode) {
		this.newZipcode = newZipcode;
	}
	public String getRoadName() {
		return roadName;
	}
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	@Override
	public String toString() {
		return "(우편번호)" + newZipcode + " " + roadName;
	}
	
	
}
