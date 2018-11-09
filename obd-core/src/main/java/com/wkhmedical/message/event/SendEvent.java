/*
 * Copyright (C) 2018 WKH-Medical. All rights reserved.
 */
package com.wkhmedical.message.event;

import org.springframework.context.ApplicationEvent;

import com.wkhmedical.message.Message;

/**
 * The Class SendEvent.
 *
 * @author Derek
 * @since 1.0, 2018-2-28
 */
@SuppressWarnings("serial")
public class SendEvent extends ApplicationEvent {
	
	/** The payload. */
	private Message payload;

	/**
	 * Instantiates a new send event.
	 *
	 * @param payload the payload
	 */
	public SendEvent(Message payload) {
		super(payload);
		this.payload = payload;
	}

	public Message getPayload() {
		return payload;
	}
}
