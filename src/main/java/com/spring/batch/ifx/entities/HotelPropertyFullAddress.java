package com.spring.batch.ifx.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "hotel_prop_full_address")
@IdClass(HotelPropertyFullAddress.HotelPropertyFullAddressIdentifier.class)
@Data
public class HotelPropertyFullAddress implements Serializable {

	private static final long serialVersionUID = -5260728161460534191L;

	@Id
	@Column(name = "brand")
	private String brand;

	@Id
	@Column(name = "pid")
	private String pid;

	@Id
	@Column(name = "lang")
	private String language;

	@Id
	@Column(name = "use_type")
	private int addressType;

	@Column(name = "address1")
	private String address1;

	@Column(name = "address2")
	private String address2;

	@Column(name = "address3")
	private String address3;

	@Column(name = "address4")
	private String address4;

	@Column(name = "address5")
	private String address5;

	@Column(name = "city")
	private String city;

	@Column(name = "county")
	private String county;

	@Column(name = "postal")
	private String postalCode;

	public static class HotelPropertyFullAddressIdentifier implements Serializable {

		private static final long serialVersionUID = -6158887864755913049L;
		private String brand;
		private String pid;
		private String language;
		private int addressType;

		public String getBrand() {
			return brand;
		}

		public void setBrand(String brand) {
			this.brand = brand;
		}

		public String getPid() {
			return pid;
		}

		public void setPid(String pid) {
			this.pid = pid;
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public int getAddressType() {
			return addressType;
		}

		public void setAddressType(int addressType) {
			this.addressType = addressType;
		}
	}
}
