package com.spring.batch.ifx.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class HotelPropertyEntityDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String brand;
	private String pid;
	private String language;
	private String propertyName;
	private String city;
	private String countryCode;
}
