package com.wkhmedical.service;

import java.math.BigDecimal;

import com.wkhmedical.constant.LicStatus;
import com.wkhmedical.dto.AreaCarBody;
import com.wkhmedical.dto.DeviceCheckSumBody;
import com.wkhmedical.dto.ObdLicDTO;

public interface ObdLicService {

	ObdLicDTO getObdLic(String urlEid, String rsaStr);

	Long[] getLicCountArr(LicStatus status);

	void updateEquipCheck(String sendStr);

	void updateEquipStuff(String sendStr);

	Long getCheckSum(DeviceCheckSumBody paramBody);

	BigDecimal getCheckExpRate();

	BigDecimal getCheckExpRate(AreaCarBody paramBody);

	void qzCheckTime();

	void qzDeviceTimeRate();

	void qzMonthSum();
}
