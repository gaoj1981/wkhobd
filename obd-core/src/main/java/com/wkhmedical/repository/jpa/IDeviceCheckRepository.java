package com.wkhmedical.repository.jpa;

import com.wkhmedical.po.DeviceCheck;

public interface IDeviceCheckRepository {
	DeviceCheck findByKey(String id);

	Long getCheckSum(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId, String inTypeStr);
}
