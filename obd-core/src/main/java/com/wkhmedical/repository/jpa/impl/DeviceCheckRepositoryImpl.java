package com.wkhmedical.repository.jpa.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
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

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.repository.jpa.IDeviceCheckRepository#getCheckSum(com.wkhmedical.dto.
	 * DeviceCheckSumBody)
	 */
	@Override
	public Long getCheckSum(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId, String inTypeStr) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder sqlBuf = new StringBuilder("");
		sqlBuf.append(" SELECT SUM(dc.number) AS sumNum");
		sqlBuf.append(" FROM device_check dc");
		sqlBuf.append(" WHERE 1 = 1");
		if (StringUtils.isNotBlank(eid)) {
			sqlBuf.append(" AND dc.eid = ?");
			paramList.add(eid);
		}
		if (provId != null) {
			sqlBuf.append(" AND dc.provId = ?");
			paramList.add(provId);
		}
		if (cityId != null) {
			sqlBuf.append(" AND dc.cityId = ?");
			paramList.add(cityId);
		}
		if (areaId != null) {
			sqlBuf.append(" AND dc.areaId = ?");
			paramList.add(areaId);
		}
		if (townId != null) {
			sqlBuf.append(" AND dc.townId = ?");
			paramList.add(townId);
		}
		if (villId != null) {
			sqlBuf.append(" AND dc.villId = ?");
			paramList.add(villId);
		}
		// 处理体检项IN查询
		if (StringUtils.isNotBlank(inTypeStr)) {
			sqlBuf.append(" AND dc.type IN (" + inTypeStr + ")");
		}
		@SuppressWarnings("rawtypes")
		List<Map> lstCount = hibernateSupport.findByNativeSql(Map.class, sqlBuf.toString(), paramList.toArray());
		BigDecimal resNum = (BigDecimal) lstCount.get(0).get("sumNum");
		if (resNum == null) return 0L;
		return resNum.longValue();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.wkhmedical.repository.jpa.IDeviceCheckRepository#getCheckSumByStatus(java.lang.Integer)
	 */
	@Override
	public BigDecimal getCheckSumByStatus(Integer status) {
		String sql = "SELECT SUM(number) AS sumNum FROM device_check";
		if (status != null) {
			sql = sql + " WHERE status=" + status;
		}
		@SuppressWarnings("rawtypes")
		List<Map> lstObj = hibernateSupport.findByNativeSql(Map.class, sql, null, 1);
		if (lstObj == null || lstObj.get(0).get("sumNum") == null) {
			return BigDecimal.ZERO;
		}
		return (BigDecimal) lstObj.get(0).get("sumNum");
	}

	@Override
	public BigDecimal getCheckSumByStatus(Integer status, String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId) {
		String sql = "SELECT SUM(number) AS sumNum FROM device_check WHERE 1=1";
		if (status != null) {
			sql = sql + " AND status=" + status;
		}
		if (provId != null) {
			sql = sql + " AND provId=" + provId;
		}
		if (cityId != null) {
			sql = sql + " AND cityId=" + cityId;
		}
		if (areaId != null) {
			sql = sql + " AND areaId=" + areaId;
		}
		if (townId != null) {
			sql = sql + " AND townId=" + townId;
		}
		if (villId != null) {
			sql = sql + " AND villId=" + villId;
		}

		@SuppressWarnings("rawtypes")
		List<Map> lstObj = hibernateSupport.findByNativeSql(Map.class, sql, null, 1);
		if (lstObj == null || lstObj.get(0).get("sumNum") == null) {
			return BigDecimal.ZERO;
		}
		return (BigDecimal) lstObj.get(0).get("sumNum");
	}

}
