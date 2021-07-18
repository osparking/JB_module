package com.jbpark.dabang.module;

import java.util.Objects;

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
		return "\t- (우편번호)" + newZipcode + " " + roadName;
	}
	/**
	 * 자동 생성된 것임.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(newZipcode, roadName);
	}
	/**
	 * 자동 생성된 것임.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		RoadAddress other = (RoadAddress) obj;
		return Objects.equals(newZipcode, other.newZipcode)
				&& Objects.equals(roadName, other.roadName);
	}	
}
