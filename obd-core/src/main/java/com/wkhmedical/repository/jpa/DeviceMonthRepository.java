package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.DeviceMonth;

@Repository
public interface DeviceMonthRepository extends JpaRepository<DeviceMonth, String>, IDeviceMonthRepository {
	DeviceMonth findByYmMonth(String ymMonth);
}
