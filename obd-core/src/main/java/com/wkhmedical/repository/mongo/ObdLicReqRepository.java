package com.wkhmedical.repository.mongo;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.MongoRepository;
import com.wkhmedical.po.MgObdLicReq;

@Repository
public interface ObdLicReqRepository extends MongoRepository<MgObdLicReq, String>, IObdLicReqRepository {

	MgObdLicReq findByEid(String eid);

}
