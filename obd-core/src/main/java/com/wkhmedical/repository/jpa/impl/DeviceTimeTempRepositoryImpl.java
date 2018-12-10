package com.wkhmedical.repository.jpa.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.repository.jpa.CarMotCopyRepository;
import com.wkhmedical.repository.jpa.IDeviceTimeTempRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DeviceTimeTempRepositoryImpl implements IDeviceTimeTempRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	CarMotCopyRepository carMotCopyRepository;

	@Value("#{query.findCount}")
	private String findCount;

}
