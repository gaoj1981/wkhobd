package com.wkhmedical.repository.mongo.impl;

import javax.annotation.Resource;

import com.taoxeo.repository.MongoSupport;
import com.wkhmedical.repository.mongo.IObdLicSumRepository;

public class ObdLicSumRepositoryImpl implements IObdLicSumRepository {
	@Resource
	MongoSupport mongoSupport;

}
