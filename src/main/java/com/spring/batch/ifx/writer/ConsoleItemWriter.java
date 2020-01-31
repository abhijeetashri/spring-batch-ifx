package com.spring.batch.ifx.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.spring.batch.ifx.dto.HotelPropertyDetails;

public class ConsoleItemWriter implements ItemWriter<HotelPropertyDetails> {

	@Override
	public void write(List<? extends HotelPropertyDetails> items) throws Exception {
		for (HotelPropertyDetails hpDetails : items) {
			System.out.println(hpDetails);
		}
	}
}
