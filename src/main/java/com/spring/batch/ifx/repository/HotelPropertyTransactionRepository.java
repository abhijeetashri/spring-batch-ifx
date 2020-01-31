package com.spring.batch.ifx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.batch.ifx.entities.HotelProperty;

@Repository
public interface HotelPropertyTransactionRepository
		extends JpaRepository<HotelProperty, HotelProperty.HotelPropertyIdentifier> {

}
