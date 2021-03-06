package com.wkhmedical.repository.mongo.impl;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.taoxeo.repository.MongoSupport;
import com.wkhmedical.constant.LicStatus;
import com.wkhmedical.po.MgObdLic;
import com.wkhmedical.repository.mongo.IObdLicRepository;
import com.wkhmedical.util.DateUtil;

public class ObdLicRepositoryImpl implements IObdLicRepository {
	@Resource
	MongoSupport mongoSupport;

	@Override
	public MgObdLic getLicInfoGtNow(String eid, LicStatus status, Integer type) {
		Criteria criteria = new Criteria();
		criteria.and("exp").gt(DateUtil.getTimestamp());
		if (eid != null) {
			criteria.and("eid").is(eid);
		}
		if (status != null) {
			criteria.and("status").is(status);
		}
		if (type != null) {
			criteria.and("type").is(type);
		}
		Sort sort = new Sort(Direction.ASC, "exp");
		Query query = new Query();
		query.addCriteria(criteria);
		query.with(sort);
		return mongoSupport.findOne(query, MgObdLic.class);
	}

	@Override
	public Long getCountByLicInfo(String eid, LicStatus status, Integer type) {
		Criteria criteria = new Criteria();
		if (eid != null) {
			criteria.and("eid").is(eid);
		}
		if (status != null) {
			criteria.and("status").is(status);
		}
		if (type != null) {
			criteria.and("type").is(type);
		}
		Query query = new Query();
		query.addCriteria(criteria);

		return mongoSupport.count(query, MgObdLic.class);
	}

}
