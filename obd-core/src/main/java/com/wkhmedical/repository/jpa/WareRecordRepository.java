package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.WareRecord;

@Repository
public interface WareRecordRepository extends JpaRepository<WareRecord, Long>, IWareRecordRepository {
	
}
