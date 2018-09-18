package com.wkhmedical.repository.mongo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.MongoRepository;
import com.wkhmedical.constant.LicStatus;
import com.wkhmedical.po.MgObdLic;

@Repository
public interface ObdLicRepository extends MongoRepository<MgObdLic, String>, IObdLicRepository {

	List<MgObdLic> findByEid(String eid);

	List<MgObdLic> findByEidAndStatus(String eid, LicStatus status);

	MgObdLic findByEidAndSn(String eid, String sn);

}
