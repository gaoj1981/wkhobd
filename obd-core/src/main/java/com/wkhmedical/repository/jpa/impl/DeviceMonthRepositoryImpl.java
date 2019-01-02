package com.wkhmedical.repository.jpa.impl;

import javax.annotation.Resource;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.repository.jpa.BaseAreaRepository;
import com.wkhmedical.repository.jpa.IDeviceMonthRepository;

public class DeviceMonthRepositoryImpl implements IDeviceMonthRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	BaseAreaRepository baseAreaRepository;

}
