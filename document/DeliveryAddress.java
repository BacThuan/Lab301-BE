package com.application.backend.document;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "deliveryAddress")
public class DeliveryAddress {
	@Id
	private String id;
	@Field("city")
	private String city;

	@Field("fastDeliveryCodeCity")
	private String fastDeliveryCodeCity;

	@Field("district")
	private String district;

	@Field("fastDeliveryCodeDistrict")
	private String fastDeliveryCodeDistrict;

	@Field("ward")
	private String ward;

	@Field("fastDeliveryCodeWard")
	private String fastDeliveryCodeWard;

	@Field("address")
	private String address;


	public DeliveryAddress() {
		super();
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFastDeliveryCodeCity() {
		return fastDeliveryCodeCity;
	}

	public void setFastDeliveryCodeCity(String fastDeliveryCodeCity) {
		this.fastDeliveryCodeCity = fastDeliveryCodeCity;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getFastDeliveryCodeDistrict() {
		return fastDeliveryCodeDistrict;
	}

	public void setFastDeliveryCodeDistrict(String fastDeliveryCodeDistrict) {
		this.fastDeliveryCodeDistrict = fastDeliveryCodeDistrict;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getFastDeliveryCodeWard() {
		return fastDeliveryCodeWard;
	}

	public void setFastDeliveryCodeWard(String fastDeliveryCodeWard) {
		this.fastDeliveryCodeWard = fastDeliveryCodeWard;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
