package com.wkhmedical.repository.jpa.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
		@SuppressWarnings("rawtypes")
		List<Map> lstNum = hibernateSupport.findByNativeSql(Map.class, sql, values, 1);
		if (lstNum == null || lstNum.get(0).get("rateSum") == null) {
			return BigDecimal.ZERO;
		}
		else {
			return (BigDecimal) lstNum.get(0).get("rateSum");
		}
	}

}
