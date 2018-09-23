package com.wkhmedical.web.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wkhmedical.dto.CarSendDTO;
import com.wkhmedical.service.ObdCarService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "OBD接口")
@RequestMapping("/obd")
public class ObdController {

	@Autowired
	ObdCarService obdCarService;

	@ApiOperation(value = "OBD推送实时车辆信息")
	@PostMapping("/receiveData.json")
	public void receiveData(@RequestBody @Valid String carStr) {
		log.info("============BEGIN==============");
		log.info("OBD请求数据：" + carStr);
		carStr = carStr.replaceAll("=", ":'").replaceAll("&", "',");
		CarSendDTO carInfo = JSON.parseObject("{" + carStr + "'}", CarSendDTO.class);
		obdCarService.saveObdCar(carInfo);
		log.info("============END==============");
	}

}
