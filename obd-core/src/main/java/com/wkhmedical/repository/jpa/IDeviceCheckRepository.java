package com.wkhmedical.repository.jpa;

import com.wkhmedical.po.DeviceCheck;

public interface IDeviceCheckRepository {
	DeviceCheck findByKey(String id);
}
