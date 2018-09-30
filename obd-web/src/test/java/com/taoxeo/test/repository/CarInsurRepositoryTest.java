package com.taoxeo.test.repository;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.taoxeo.boot.util.JsonUtils;
import com.taoxeo.test.BaseTest;
import com.wkhmedical.po.CarInsur;
import com.wkhmedical.repository.jpa.CarInsurRepository;

public class CarInsurRepositoryTest extends BaseTest {
	@Resource
	CarInsurRepository carInsurRepository;
	
	@Test
	public void find() {
		Pageable pageable = PageRequest.of(0, 10);
		Object[] params = new Object[] {};
		Page<CarInsur> page = carInsurRepository.findPageByNativeSql("SELECT * FROM car_insur", "SELECT COUNT(1) FROM car_insur",
				params, pageable);
		System.out.println(JsonUtils.toJsonString(page));
	}
}
