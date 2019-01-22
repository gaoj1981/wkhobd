package com.wkhmedical.repository.jpa;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.DeviceCheckTime;

@Repository
public interface DeviceCheckTimeRepository extends JpaRepository<DeviceCheckTime, String>, IDeviceCheckTimeRepository {

	DeviceCheckTime findByEidAndTypeAndDt(String eid, String type, Date dt);

	Long countByEidAndDt(String eid, Date dt);

	Long countByEidAndDtAndNumberGreaterThan(String eid, Date dt, Integer number);
}
