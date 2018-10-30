package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wkhmedical.dto.EquipInfoDTO;
import com.wkhmedical.dto.EquipInfoBody;
import com.wkhmedical.po.EquipInfo;

public interface IEquipInfoRepository {
	EquipInfo findByKey(String id);
	
	List<EquipInfoDTO> findEquipInfoList(EquipInfoBody paramBody, Pageable pageable);
	
	Page<EquipInfoDTO> findPgEquipInfoDTO(EquipInfoBody paramBody, Pageable pageable);
	
	Page<EquipInfo> findPgEquipInfo(EquipInfoBody paramBody, Pageable pageable);

	Integer findCount(String id);
}
