package com.spring.batch.ifx.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.spring.batch.ifx.dto.HotelPropertyEntityDto;

@Component
public class HotelPropertyRepositoryImpl implements HotelPropertyRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(HotelPropertyRepositoryImpl.class);

	@Autowired
	@Qualifier("ifxEntityManager")
	private EntityManager em;

	/**
	 * addressType for physical location of property = 5.
	 */
	private static final String HOTEL_BY_BRAND_PID = "SELECT hp.brand, hp.pid, hp.propertyName, full_addr.city, addr.country FROM HotelProperty hp, HotelPropertyFullAddress full_addr, HotelPropertyAddress addr ";
	private static final String WHERE_CLAUSE = "WHERE addr.brand=hp.brand AND addr.pid=hp.pid AND full_addr.brand=hp.brand AND full_addr.pid=hp.pid AND hp.language='en' AND addr.addressType=5 AND full_addr.addressType=5 AND full_addr.language='en' ";

	@SuppressWarnings("unchecked")
	public HotelPropertyEntityDto findByBrandAndPid(String brand, String pid) {
		LOGGER.info(brand + " and " + pid);
		List<Object[]> result = new ArrayList<>();
		long start = System.currentTimeMillis();
		try {
			result = em.createQuery(
					HOTEL_BY_BRAND_PID + WHERE_CLAUSE + "AND hp.brand = '" + brand + "' AND hp.pid = '" + pid + "'")
					.getResultList();
		} catch (Exception e) {
			LOGGER.error("Error getting data from IFX database.", e);
			return null;
		} finally {
			LOGGER.info("Time to IFX (seconds) : " + (System.currentTimeMillis() - start) / 1000);
		}

		if (!result.isEmpty()) {
			Object[] hotelDetails = result.get(0);
			HotelPropertyEntityDto hotelProperty = new HotelPropertyEntityDto();
			hotelProperty.setBrand(brand);
			hotelProperty.setPid(pid);
			hotelProperty.setPropertyName(String.valueOf(hotelDetails[2]).trim());
			hotelProperty.setCity(String.valueOf(hotelDetails[3]).trim());
			hotelProperty.setCountryCode(String.valueOf(hotelDetails[4]).trim());
			return hotelProperty;
		}
		return null;
	}
}
