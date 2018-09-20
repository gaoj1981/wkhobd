package com.wkhmedical.web.api;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "设备接口")
@RequestMapping("/obd")
public class ObdController {

	@PostMapping("/receiveData.json")
	public void getWare(@RequestBody @Valid String licStr) {
		log.info("============BEGIN==============");
		log.info("OBD请求数据：" + licStr);
		log.info("============END==============");
	}

}
