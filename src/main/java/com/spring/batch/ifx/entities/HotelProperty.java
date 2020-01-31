package com.spring.batch.ifx.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.spring.batch.ifx.entities.HotelProperty.HotelPropertyIdentifier;

import lombok.Data;

@Entity
@Table(name = "hotel_property")
@IdClass(HotelPropertyIdentifier.class)
@Data
public class HotelProperty implements Serializable {

	private static final long serialVersionUID = -6924543009280686755L;

	@Id
	@Column(name = "brand")
	private String brand;

	@Id
	@Column(name = "pid")
	private String pid;

	@Id
	@Column(name = "lang")
	private String language;

	@Column(name = "prop_name")
	private String propertyName;

	@Column(name = "city")
	private String city;

	public static class HotelPropertyIdentifier implements Serializable {

		private static final long serialVersionUID = -7335177661573147673L;
		private String brand;
		private String pid;
		private String language;

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
	}
}