package com.spring.batch.ifx.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "hotel_prop_address")
@IdClass(HotelPropertyAddress.PropertyAddressIdentifier.class)
@Data
public class HotelPropertyAddress implements Serializable {

	private static final long serialVersionUID = -7455821689035685531L;

	@Id
	@Column(name = "brand")
	private String brand;

	@Id
	@Column(name = "pid")
	private String pid;

	/**
	 * If address is of head office, physical location of property
	 */
	@Id
	@Column(name = "use_type")
	private int addressType;

	@Column(name = "state")
	private String state;

	@Column(name = "country")
	private String country;

	public static class PropertyAddressIdentifier implements Serializable {

		private static final long serialVersionUID = 3693099629925156773L;
		private String brand;
		private String pid;
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

		public int getAddressType() {
			return addressType;
		}

		public void setAddressType(int addressType) {
			this.addressType = addressType;
		}
	}
}
