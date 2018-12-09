package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.DeviceCheck;

@Repository
public interface DeviceCheckRepository extends JpaRepository<DeviceCheck, String>, IDeviceCheckRepository {

	DeviceCheck findByEidAndType(String eid, String type);

}
