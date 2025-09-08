package com.miniproject.qr_generator.dto;

public class QrRequest {
	private String name;
    public QrRequest(String name, String bikeNumber, String contactNumber) {
		super();
		this.name = name;
		this.bikeNumber = bikeNumber;
		this.contactNumber = contactNumber;
//		this.logo = logo;
	}
    public QrRequest() {}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBikeNumber() {
		return bikeNumber;
	}
	public void setBikeNumber(String bikeNumber) {
		this.bikeNumber = bikeNumber;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	private String bikeNumber;
    private String contactNumber;
    //private String logo;
}
