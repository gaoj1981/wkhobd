package com.wkhmedical.repository.jpa;

import java.math.BigDecimal;
import java.util.Date;

public interface IDeviceTimeRepository {

	Long findCarDayCount(Date dt, String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId);

	Long getTimeSum(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId, Date dtStart, Date dtEnd);

	BigDecimal getDisSum(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId, Date dtStart, Date dtEnd);

	Long getCarSum(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId, Date dtStart, Date dtEnd);

}
