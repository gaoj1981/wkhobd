package com.wkhmedical.repository.jpa.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.po.DeviceCheck;
import com.wkhmedical.repository.jpa.DeviceCheckRepository;
import com.wkhmedical.repository.jpa.IDeviceCheckRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DeviceCheckRepositoryImpl implements IDeviceCheckRepository {
	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	DeviceCheckRepository deviceCheckRepository;

	@Value("#{query.findCount}")
	private String findCount;

	@Override
	public DeviceCheck findByKey(String id) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM car_insur");
		sqlBuf.append(" WHERE id = ?");
		paramList.add(id);

		List<DeviceCheck> lstObj = hibernateSupport.findByNativeSql(DeviceCheck.class, sqlBuf.toString(), paramList.toArray());
		if (lstObj != null && lstObj.size() > 0) {
			return lstObj.get(0);
		}
		log.info("获取设备体检对象为NULL");
		return null;
	}

}
