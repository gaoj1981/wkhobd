/*
 * Copyright (C) 2018 WKH-Medical. All rights reserved.
 */
package com.wkhmedical.message.event;

import org.springframework.context.ApplicationEvent;

/**
 * The Class ArrivedEvent.
 *
 * @author Derek
 * @since 1.0, 2018-2-28
 */
@SuppressWarnings("serial")
public class ArrivedEvent extends ApplicationEvent {

	/**
	 * Instantiates a new arrived event.
	 *
	 * @param message the message
	 */
	public ArrivedEvent(byte[] message) {
		super(message);
	}
	
}
