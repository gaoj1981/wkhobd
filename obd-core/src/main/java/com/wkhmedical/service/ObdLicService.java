package com.wkhmedical.service;

import java.math.BigDecimal;
import java.util.List;

import com.wkhmedical.constant.LicStatus;
import com.wkhmedical.dto.AreaCarBody;
import com.wkhmedical.dto.CheckItemTotal;
import com.wkhmedical.dto.CheckPeopleTotal;
import com.wkhmedical.dto.DeviceCheckSumBody;
import com.wkhmedical.dto.MonthAvgCarDTO;
import com.wkhmedical.dto.MonthAvgExamDTO;
import com.wkhmedical.dto.MonthAvgTimeDTO;
import com.wkhmedical.dto.ObdLicDTO;

public interface ObdLicService {

	ObdLicDTO getObdLic(String urlEid, String rsaStr);

	Long[] getLicCountArr(LicStatus status);

	void updateEquipCheck(String sendStr);

	void updateEquipStuff(String sendStr);

	Long getCheckSum(DeviceCheckSumBody paramBody);

	CheckItemTotal getCheckItemSum(DeviceCheckSumBody paramBody);

	BigDecimal getCheckExpRate();

	BigDecimal getCheckExpRate(AreaCarBody paramBody);

	void qzCheckTime();

	void qzDeviceTimeRate();

	void qzMonthSum();

	List<MonthAvgExamDTO> getCheckMonthAvg(AreaCarBody paramBody);

	List<MonthAvgTimeDTO> getTimeMonthAvg(AreaCarBody paramBody);

	List<MonthAvgCarDTO> getCarMonthAvg(AreaCarBody paramBody);

	Long getTimeTotal(AreaCarBody paramBody);

	CheckPeopleTotal getCheckPeopleTotal(AreaCarBody paramBody);

}
