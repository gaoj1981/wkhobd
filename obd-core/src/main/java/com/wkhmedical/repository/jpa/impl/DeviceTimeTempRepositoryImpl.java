package com.wkhmedical.repository.jpa.impl;

import javax.annotation.Resource;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.repository.jpa.IDeviceTimeTempRepository;

public class DeviceTimeTempRepositoryImpl implements IDeviceTimeTempRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

}
