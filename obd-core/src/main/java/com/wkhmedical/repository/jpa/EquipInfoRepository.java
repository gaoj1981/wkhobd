package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.EquipInfo;

@Repository
public interface EquipInfoRepository extends JpaRepository<EquipInfo, String>, IEquipInfoRepository {
	List<EquipInfo> findByEid(String eid);

	List<EquipInfo> findByEidAndTypeOrderByIdAsc(String eid, Integer type);
}
