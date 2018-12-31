package com.wkhmedical.repository.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.DeviceTimeTemp;

@Repository
public interface DeviceTimeTempRepository extends JpaRepository<DeviceTimeTemp, String>, IDeviceTimeTempRepository {

	DeviceTimeTemp findByEidAndDt(String eid, Date dt);

	List<DeviceTimeTemp> findTop2000ByFlagAndDtLessThan(Integer flag, Date dt);

	DeviceTimeTemp findFirstByFlagAndDtLessThan(Integer flag, Date dt);

	Long countByFlagAndDt(Integer flag, Date dt);

}
