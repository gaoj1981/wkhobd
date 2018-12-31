package com.wkhmedical.repository.jpa;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.DeviceTimeRate;

@Repository
public interface DeviceTimeRateRepository extends JpaRepository<DeviceTimeRate, String>, IDeviceTimeRateRepository {

	DeviceTimeRate findByDt(Date dt);

}
