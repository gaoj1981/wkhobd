package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.CarInsurCopy;

@Repository
public interface CarInsurCopyRepository extends JpaRepository<CarInsurCopy, String>, ICarInsurCopyRepository {
	
}
