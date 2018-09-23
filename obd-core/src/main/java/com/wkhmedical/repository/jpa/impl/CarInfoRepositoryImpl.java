package com.wkhmedical.repository.jpa.impl;

import javax.annotation.Resource;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.repository.jpa.ICarInfoRepository;

public class CarInfoRepositoryImpl implements ICarInfoRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

}
