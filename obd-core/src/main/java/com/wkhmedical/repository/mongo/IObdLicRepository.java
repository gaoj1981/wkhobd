package com.wkhmedical.repository.mongo;

import com.wkhmedical.constant.LicStatus;
import com.wkhmedical.po.MgObdLic;

public interface IObdLicRepository {

	/**
	 * 获取有效的授权信息
	 * 
	 * @param did 设备ID
	 * @param status 授权状态
	 * @param type 类型
	 * @return
	 */
	MgObdLic getLicInfoGtNow(String eid, LicStatus status, Integer type);

	/**
	 * 根据授权状态获取对应数量
	 * 
	 * @param did 设备ID
	 * @param status 授权状态
	 * @param type 类型
	 * @return
	 */
	Long getCountByLicInfo(String eid, LicStatus status, Integer type);
}
