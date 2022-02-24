package com.disebud.puninar_absensi.model.getData;

public class ResultItem{
	private String img;
	private String address;
	private String dateAbsen;
	private String latitude;
	private String id;
	private String longitude;
	private String status;
	private String current_datetime;

	public void setImg(String img){
		this.img = img;
	}

	public String getImg(){
		return img;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setDateAbsen(String dateAbsen){
		this.dateAbsen = dateAbsen;
	}

	public String getDateAbsen(){
		return dateAbsen;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setCurrentDatetime(String currentDatetime){
		this.current_datetime = currentDatetime;
	}

	public String getCurrentDatetime(){
		return current_datetime;
	}
}
