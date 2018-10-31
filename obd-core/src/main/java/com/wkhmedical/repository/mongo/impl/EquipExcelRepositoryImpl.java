/**
 * 
 */
package com.wkhmedical.repository.mongo.impl;

import javax.annotation.Resource;

import com.taoxeo.repository.MongoSupport;
import com.wkhmedical.repository.mongo.IEquipExcelRepository;

/**
 * @author Administrator
 */
public class EquipExcelRepositoryImpl implements IEquipExcelRepository {
	@Resource
	MongoSupport mongoSupport;

}
