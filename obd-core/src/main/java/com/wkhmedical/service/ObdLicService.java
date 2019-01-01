package com.wkhmedical.service;

import java.math.BigDecimal;

import com.wkhmedical.constant.LicStatus;
import com.wkhmedical.dto.DeviceCheckSumBody;
import com.wkhmedical.dto.ObdLicDTO;

public interface ObdLicService {

	ObdLicDTO getObdLic(String urlEid, String rsaStr);

	Long[] getLicCountArr(LicStatus status);

	void updateEquipCheck(String sendStr);

	void updateEquipStuff(String sendStr);

	Long getCheckSum(DeviceCheckSumBody paramBody);

	BigDecimal getCheckExpRate();

	void qzCheckTime();

	void qzDeviceTimeRate();
	
}
