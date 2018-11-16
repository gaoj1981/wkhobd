package com.wkhmedical.web.api;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.taoxeo.lang.BeanUtils;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.dto.CarSendDTO;
import com.wkhmedical.dto.ObdCarDTO;
import com.wkhmedical.message.Message;
import com.wkhmedical.message.event.SendEvent;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.service.CarInfoService;
import com.wkhmedical.service.ObdCarService;
import com.wkhmedical.util.MapGPSUtil;

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

	@Autowired
	CarInfoService carInfoService;

	/** The messaging template. */
	@Resource
	SimpMessagingTemplate messagingTemplate;

	/** The application context. */
	@Resource
	ApplicationContext applicationContext;

	@ApiOperation(value = "OBD推送实时车辆信息")
	@PostMapping("/receiveData.json")
	public void receiveData(@RequestBody @Valid String carStr) {
		log.info("============BEGIN==============");
		log.info("OBD请求数据：" + carStr);
		carStr = carStr.replaceAll("=", ":'").replaceAll("&", "',");
		CarSendDTO obdSend = JSON.parseObject("{" + carStr + "'}", CarSendDTO.class);
		obdCarService.saveObdCar(obdSend);
		log.info("============END==============");
		// 执行ws推送
		try {
			CarInfoParam paramBody = new CarInfoParam();
			paramBody.setDeviceNumber(obdSend.getDeviceNumber());
			CarInfo carInfo = carInfoService.getCarInfo(paramBody);
			// 是否推送数据到前端
			if (carInfo != null) {
				// 推送
				ObdCarDTO obdCar = new ObdCarDTO();
				BeanUtils.merageProperty(obdCar, carInfo);
				BeanUtils.merageProperty(obdCar, obdSend);
				String eid = carInfo.getEid();
				obdSend.setEid(carInfo.getEid());
				BigDecimal lat = obdCar.getLat();
				BigDecimal lng = obdCar.getLng();
				BigDecimal[] bdArr = MapGPSUtil.Transform(lat.doubleValue(), lng.doubleValue());
				if (bdArr != null && bdArr.length == 2) {
					obdCar.setLat(bdArr[0]);
					obdCar.setLng(bdArr[1]);
				}
				messagingTemplate.convertAndSend("/queue/obd.data." + eid, obdCar);
				Message message = new Message(eid, ObdCarDTO.class.getSimpleName(), obdCar);
				applicationContext.publishEvent(new SendEvent(message));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
