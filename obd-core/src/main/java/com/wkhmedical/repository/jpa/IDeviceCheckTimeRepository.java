package com.wkhmedical.repository.jpa;

import java.math.BigDecimal;
import java.util.Date;

public interface IDeviceCheckTimeRepository {

	Long getCheckSum(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId, String type, Date dtStart, Date dtEnd);

	Long getCheckItemCount(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId);

	BigDecimal getCheckSumByStatus(Integer status, String eid, Date dt);

}
