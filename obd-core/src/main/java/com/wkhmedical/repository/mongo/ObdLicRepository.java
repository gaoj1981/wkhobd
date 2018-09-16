package com.wkhmedical.repository.mongo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.MongoRepository;
import com.wkhmedical.constant.LicStatus;
import com.wkhmedical.po.MgObdLic;

@Repository
public interface ObdLicRepository extends MongoRepository<MgObdLic, String>, IObdLicRepository {

	List<MgObdLic> findByDid(String did);

	List<MgObdLic> findByDidAndStatus(String did, LicStatus status);

	MgObdLic findByDidAndSn(String id, String sn);

}
