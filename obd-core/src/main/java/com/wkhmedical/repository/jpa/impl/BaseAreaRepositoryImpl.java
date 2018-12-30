package com.wkhmedical.repository.jpa.impl;

import javax.annotation.Resource;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.repository.jpa.BaseAreaRepository;
import com.wkhmedical.repository.jpa.IBaseAreaRepository;

public class BaseAreaRepositoryImpl implements IBaseAreaRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	BaseAreaRepository baseAreaRepository;

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.repository.jpa.IBaseAreaRepository#findCountByPid(java.lang.Long)
	 */
	@Override
	public Long findCountByPid(Long pid) {
		return hibernateSupport.countByNativeSql("SELECT COUNT(1) FROM base_area ba WHERE ba.pid = " + pid, null);
	}

}
