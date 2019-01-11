package com.wkhmedical.repository.jpa;

import java.math.BigDecimal;
import java.util.List;

import com.wkhmedical.po.DeviceCheck;

public interface IDeviceCheckRepository {
	DeviceCheck findByKey(String id);

	BigDecimal getCheckSumByStatus(Integer status);

	Long getCheckSum(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId, String inTypeStr);

	BigDecimal getCheckSumByStatus(Integer status, String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId);

	List<DeviceCheck> getCheckItemSum(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId, String inTypeStr);

}
