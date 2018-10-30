package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.EquipInfo;

@Repository
public interface EquipInfoRepository extends JpaRepository<EquipInfo, String>, IEquipInfoRepository {
	
}
