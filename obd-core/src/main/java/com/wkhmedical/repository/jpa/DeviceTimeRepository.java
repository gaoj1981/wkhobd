package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.DeviceTime;

@Repository
public interface DeviceTimeRepository extends JpaRepository<DeviceTime, String>, IDeviceTimeRepository {
	
}
