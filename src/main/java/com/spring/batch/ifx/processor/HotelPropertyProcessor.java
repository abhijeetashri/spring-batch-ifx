package com.spring.batch.ifx.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.spring.batch.ifx.dto.HotelPropertyEntityDto;
import com.spring.batch.ifx.dto.HotelPropertyDetails;
import com.spring.batch.ifx.entities.HotelProperty;
import com.spring.batch.ifx.repository.HotelPropertyRepository;

public class HotelPropertyProcessor implements ItemProcessor<HotelProperty, HotelPropertyDetails> {

	private static final Logger LOGGER = LoggerFactory.getLogger(HotelPropertyProcessor.class);

	@Autowired
	private HotelPropertyRepository hotelPropertyRepository;

	@Cacheable(value = "hotelPropertyByBrandAndPid", key = "{#brand, #pid}", unless = "#result==null")
	public HotelPropertyEntityDto loadHotelPropertyBy(String brand, String pid) {
		return hotelPropertyRepository.findByBrandAndPid(brand, pid);
	}

	@Override
	public HotelPropertyDetails process(HotelProperty aHotel) throws Exception {
		LOGGER.info("Processing hotel: {}-{} ", aHotel.getBrand(), aHotel.getPid());
		HotelPropertyDetails property = new HotelPropertyDetails();
		property.setBrand(aHotel.getBrand());
		property.setPid(aHotel.getPid());
		HotelPropertyEntityDto hotel = loadHotelPropertyBy(aHotel.getBrand(), aHotel.getPid());
		if (hotel != null) {
			property.setPropertyName(hotel.getPropertyName());
			property.setCity(hotel.getCity());
			property.setCountry(hotel.getCountryCode());
		}
		return property;
	}
}
