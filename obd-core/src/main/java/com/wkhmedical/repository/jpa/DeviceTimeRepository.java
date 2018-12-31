package com.wkhmedical.repository.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.DeviceTime;

@Repository
public interface DeviceTimeRepository extends JpaRepository<DeviceTime, String>, IDeviceTimeRepository {

	List<DeviceTime> findByDt(Date dt);

	Long countByDt(Date dt);
}
