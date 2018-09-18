package com.wkhmedical.repository.mongo;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.MongoRepository;
import com.wkhmedical.po.MgObdLicSum;

@Repository
public interface ObdLicSumRepository extends MongoRepository<MgObdLicSum, String>, IObdLicSumRepository {

	MgObdLicSum findByEid(String eid);

}
