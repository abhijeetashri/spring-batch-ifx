package com.spring.batch.ifx.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HotelPropertyDetails {

	private String brand;
	private String pid;
	private String propertyName;
	private String city;
	private String country;
}
