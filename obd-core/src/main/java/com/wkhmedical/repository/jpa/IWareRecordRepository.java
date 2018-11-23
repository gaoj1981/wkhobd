package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wkhmedical.dto.WareRecordBody;
import com.wkhmedical.dto.WareRecordDTO;
import com.wkhmedical.po.WareRecord;

public interface IWareRecordRepository {
	WareRecord findByKey(String id);

	List<WareRecordDTO> findWareRecordList(WareRecordBody paramBody, Pageable pageable);

	Page<WareRecord> findPgWareRecord(WareRecordBody paramBody, Pageable pageable);

	Page<WareRecordDTO> findPgWareRecordDTO(WareRecordBody paramBody, Pageable pageable);

	Integer findCount(String id);
}
