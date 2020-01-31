package com.spring.batch.ifx.repository;

import com.spring.batch.ifx.dto.HotelPropertyEntityDto;

public interface HotelPropertyRepository {

	HotelPropertyEntityDto findByBrandAndPid(String brand, String pid);
}
