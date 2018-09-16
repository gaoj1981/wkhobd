package com.wkhmedical.repository.mongo.impl;

import javax.annotation.Resource;

import com.taoxeo.repository.MongoSupport;
import com.wkhmedical.repository.mongo.IObdLicReqRepository;

public class ObdLicReqRepositoryImpl implements IObdLicReqRepository {
	@Resource
	MongoSupport mongoSupport;

}
