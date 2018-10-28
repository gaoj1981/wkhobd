package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.CarMotCopy;

@Repository
public interface CarMotCopyRepository extends JpaRepository<CarMotCopy, String>, ICarMotCopyRepository {
	CarMotCopy findByCid(String cid);
}
