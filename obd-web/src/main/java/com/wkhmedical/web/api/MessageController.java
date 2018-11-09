/*
 * Copyright (C) 2018 WKH-Medical. All rights reserved.
 */
package com.wkhmedical.web.api;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.wkhmedical.message.BodyData;
import com.wkhmedical.message.Message;
import com.wkhmedical.message.WaveData;
import com.wkhmedical.message.event.SendEvent;

import io.swagger.annotations.Api;

/**
 * The Class MessageController.
 *
 * @author Derek
 * @since 1.0, 2018-2-28
 */
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


}
