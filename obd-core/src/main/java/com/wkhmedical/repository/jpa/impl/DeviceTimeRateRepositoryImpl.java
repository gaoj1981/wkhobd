package com.wkhmedical.repository.jpa.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.repository.jpa.BaseAreaRepository;
import com.wkhmedical.repository.jpa.IDeviceTimeRateRepository;

public class DeviceTimeRateRepositoryImpl implements IDeviceTimeRateRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	BaseAreaRepository baseAreaRepository;

	@Override
	public BigDecimal getRateSumByDate(Date dt1, Date dt2) {
		String sql = "SELECT SUM(rate) AS rateSum FROM device_time_rate WHERE dt>=? AND dt<=?";
		Object[] values = new Object[] { dt1, dt2 };
		List<BigDecimal> lstNum = hibernateSupport.findByNativeSql(BigDecimal.class, sql, values, 1);
		return lstNum.get(0);
	}

}
