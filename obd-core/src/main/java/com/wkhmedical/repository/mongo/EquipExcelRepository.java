/**
 * 
 */
package com.wkhmedical.repository.mongo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.MongoRepository;
import com.wkhmedical.po.MgEquipExcel;

/**
 * @author Administrator
 */
@Repository
public interface EquipExcelRepository extends MongoRepository<MgEquipExcel, String>, IEquipExcelRepository {
	List<MgEquipExcel> findByExcelPath(String excelPath);

	List<MgEquipExcel> findByExcelPathAndDelFlag(String excelPath, int delFlag);
}
