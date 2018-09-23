package com.wkhmedical.repository.mongo.impl;

import javax.annotation.Resource;

import com.taoxeo.repository.MongoSupport;
import com.wkhmedical.repository.mongo.IObdCarRepository;

public class ObdCarRepositoryImpl implements IObdCarRepository {
	@Resource
	MongoSupport mongoSupport;

}
