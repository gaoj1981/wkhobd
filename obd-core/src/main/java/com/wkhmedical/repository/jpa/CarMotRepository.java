package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.CarMot;

@Repository
public interface CarMotRepository extends JpaRepository<CarMot, Long>, ICarMotRepository {
	
}
