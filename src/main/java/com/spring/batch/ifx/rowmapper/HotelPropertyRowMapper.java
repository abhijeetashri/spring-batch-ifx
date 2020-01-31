package com.spring.batch.ifx.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.spring.batch.ifx.entities.HotelProperty;

public class HotelPropertyRowMapper implements RowMapper<HotelProperty> {

	@Override
	public HotelProperty mapRow(ResultSet rs, int rowNum) throws SQLException {
		HotelProperty caTxn = new HotelProperty();
		caTxn.setBrand(rs.getString("brand"));
		caTxn.setPid(rs.getString("pid"));
		caTxn.setDestination(rs.getString("destination"));
		caTxn.setLanguageCode(rs.getString("language"));
		return caTxn;
	}
}
