package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.DeviceTimeTemp;

@Repository
public interface DeviceTimeTempRepository extends JpaRepository<DeviceTimeTemp, String>, IDeviceTimeTempRepository {
}
