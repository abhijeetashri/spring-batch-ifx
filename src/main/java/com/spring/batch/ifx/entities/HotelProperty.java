package com.spring.batch.ifx.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "hotel_property")
@IdClass(HotelProperty.HotelPropertyIdentifier.class)
@Data
public class HotelProperty {

	@Column(name = "destination")
	@Id
	private String destination;

	@Column(name = "brand")
	@Id
	private String brand;

	@Column(name = "pid")
	@Id
	private String pid;

	@Column(name = "language")
	@Id
	private String languageCode;

	public static class HotelPropertyIdentifier implements Serializable {

		private static final long serialVersionUID = 2273454288695055575L;
		private String destination;
		private String brand;
		private String pid;
		private String languageCode;

		public String getDestination() {
			return destination;
		}

		public HotelPropertyIdentifier setDestination(String destination) {
			this.destination = destination;
			return this;
		}

		public String getBrand() {
			return brand;
		}

		public HotelPropertyIdentifier setBrand(String brand) {
			this.brand = brand;
			return this;
		}

		public String getPid() {
			return pid;
		}

		public HotelPropertyIdentifier setPid(String pid) {
			this.pid = pid;
			return this;
		}

		public String getLanguageCode() {
			return languageCode;
		}

		public HotelPropertyIdentifier setLanguageCode(String languageCode) {
			this.languageCode = languageCode;
			return this;
		}
	}
}
