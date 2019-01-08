package com.wkhmedical.repository.jpa;

import java.util.Date;

public interface IDeviceCheckTimeRepository {

	Long getCheckSum(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId, String type, Date dtStart, Date dtEnd);

}
