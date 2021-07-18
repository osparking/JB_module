package com.jbpark.dabang.module;

public class RoadAddress {
	private String mgmtNumber;
	private String newZipcode;
	private String roadName;
	
	public RoadAddress(String mgmtNumber, String newZipcode, String roadName) {
		super();
		this.mgmtNumber = mgmtNumber;
		this.newZipcode = newZipcode;
		this.roadName = roadName;
	}
	
	public String getMgmtNumber() {
		return mgmtNumber;
	}

	public void setMgmtNumber(String mgmtNumber) {
		this.mgmtNumber = mgmtNumber;
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
		return ". (우편번호)" + newZipcode + " " + roadName;
	}
	/**
	 * 자동 생성된 것임.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mgmtNumber == null) ? 0 : mgmtNumber.hashCode());
		return result;
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
		if (mgmtNumber == null) {
			if (other.mgmtNumber != null)
				return false;
		} else if (!mgmtNumber.equals(other.mgmtNumber))
			return false;
		return true;
	}

}
