/*
 * Copyright (C) 2018 WKH-Medical. All rights reserved.
 */
package com.wkhmedical.web.api;

import java.math.BigDecimal;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.wkhmedical.dto.ObdCarDTO;
import com.wkhmedical.message.BodyData;
import com.wkhmedical.message.Message;
import com.wkhmedical.message.WaveData;
import com.wkhmedical.message.event.SendEvent;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;

/**
 * The Class MessageController.
 *
 * @author Derek
 * @since 1.0, 2018-2-28
 */
@Log4j2
@RestController
@Api(tags = "数据消息接口")
public class MessageController {

	/** The messaging template. */
	@Resource
	SimpMessagingTemplate messagingTemplate;

	/** The application context. */
	@Resource
	ApplicationContext applicationContext;

	/**
	 * Send wave data.
	 *
	 * @param data the data
	 * @param userId the userId
	 */
	@MessageMapping("/queue/wave.data.{userId}")
	public void sendWaveData(WaveData data, @DestinationVariable String userId) {
		messagingTemplate.convertAndSend("/queue/wave.data." + userId, data);
		Message message = new Message(userId, WaveData.class.getSimpleName(), data);
		applicationContext.publishEvent(new SendEvent(message));
	}

	/**
	 * Send body data.
	 *
	 * @param data the data { "colTime":1532443076836, "bid":"aaaaa", "hr": "80", "bpStatus": "0",
	 *            "sys": "1", "dia": "2", "map": "3", "bp": "4", "spo2": "5", "pr": "6", "rr": "7",
	 *            "temp": "8" }
	 * @param userId the userId
	 */
	@MessageMapping("/queue/body.data.{userId}")
	public void sendBodyData(BodyData data, @DestinationVariable String userId) {
		messagingTemplate.convertAndSend("/queue/body.data." + userId, data);
		Message message = new Message(userId, BodyData.class.getSimpleName(), data);
		applicationContext.publishEvent(new SendEvent(message));
	}

	/**
	 * Send obd data test.
	 *
	 * @param data the data
	 * @param userId the userId
	 */
	@MessageMapping("/queue/obd.data.{userId}")
	public void sendObdData(WaveData data, @DestinationVariable String userId) {
		ObdCarDTO obdCar = new ObdCarDTO();
		Integer[] speedArr = new Integer[] { 20, 50, 77, 99, 188, 220, 110, 65, 11, 88 };
		Random random = new Random();
		for (int i = 0; i < 300; i++) {
			try {
				Thread.sleep(250);
				obdCar.setVehicleSpeed(speedArr[random.nextInt(10)]);
				obdCar.setLng(new BigDecimal("119.691967").add(new BigDecimal(i * 0.0001)).setScale(6, BigDecimal.ROUND_HALF_DOWN));
				obdCar.setLat(new BigDecimal("29.801359").add(new BigDecimal(i * 0.00001)).setScale(6, BigDecimal.ROUND_HALF_DOWN));
				obdCar.setRemainingGasValue(29 - i / 100);
				obdCar.setDashboardTotalMileage(
						new BigDecimal(3456.999).add(new BigDecimal(i * 299)).setScale(4, BigDecimal.ROUND_HALF_DOWN).doubleValue());
				obdCar.setEnvironmentTemperature(28 + random.nextInt(10));
				obdCar.setAirDoorTemperature(78 + random.nextInt(10));
				obdCar.setCoolWaterTemperature(90 + random.nextInt(10));
				obdCar.setEid(userId);
				messagingTemplate.convertAndSend("/queue/obd.data." + userId, obdCar);
				Message message = new Message("11111111", ObdCarDTO.class.getSimpleName(), obdCar);
				applicationContext.publishEvent(new SendEvent(message));
				log.info("queue/obd.data" + i);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
