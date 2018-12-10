package com.wkhmedical.repository.jpa.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.repository.jpa.CarMotRepository;
import com.wkhmedical.repository.jpa.IDeviceTimeRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DeviceTimeRepositoryImpl implements IDeviceTimeRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	CarMotRepository carMotRepository;

	@Value("#{query.findCount}")
	private String findCount;

}
