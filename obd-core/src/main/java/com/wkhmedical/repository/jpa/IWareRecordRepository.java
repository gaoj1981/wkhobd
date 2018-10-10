package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.WareRecordDTO;
import com.wkhmedical.dto.WareRecordBody;
import com.wkhmedical.po.WareRecord;

public interface IWareRecordRepository {
	WareRecord findByKey(Long id);

	List<WareRecordDTO> findWareRecordList(WareRecordBody paramBody, Integer page, Integer size);

	Page<WareRecord> findPgWareRecord(WareRecordBody paramBody, Integer page, Integer size);

	Integer findCount(Long id);
}
