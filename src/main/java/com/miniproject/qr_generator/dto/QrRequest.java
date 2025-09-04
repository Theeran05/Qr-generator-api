package com.miniproject.qr_generator.dto;

public class QrRequest {
	private String name;
    public QrRequest(String name, String bikeNumber, String contactNumber, String logo) {
		super();
		this.name = name;
		this.bikeNumber = bikeNumber;
		this.contactNumber = contactNumber;
		this.logo = logo;
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
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	private String bikeNumber;
    private String contactNumber;
    private String logo;
}
