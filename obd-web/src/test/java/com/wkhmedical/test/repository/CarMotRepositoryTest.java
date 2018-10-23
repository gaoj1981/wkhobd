package com.wkhmedical.test.repository;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.wkhmedical.dto.CarMotBody;
import com.wkhmedical.repository.jpa.CarMotRepository;
import com.wkhmedical.test.BaseTest;

public class CarMotRepositoryTest extends BaseTest{
	@Resource
	CarMotRepository carMotRepository;
	
	
	@Test
	public void findPgCarMotDTO() {
		CarMotBody paramBody = new CarMotBody();
		Pageable pageable = PageRequest.of(0, 2);
		carMotRepository.findPgCarMotDTO(paramBody, pageable);
	}
}
