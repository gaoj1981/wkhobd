/*
 * Copyright (C) 2018 WKH-Medical. All rights reserved.
 */
package com.wkhmedical.message.event;

import java.security.Principal;

import org.springframework.context.ApplicationEvent;

/**
 * The Class ConnectEvent.
 *
 * @author Derek
 * @since 1.0, 2018-2-28
 */
public class ConnectEvent extends ApplicationEvent {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6469583249311068725L;

	/** The Enum TYPE. */
	public static enum TYPE {

		/** The open. */
		OPEN,
		/** The close. */
		CLOSE
	};

	/** The type. */
	private TYPE type;

	/** The principal. */
	private Principal principal;

	/**
	 * Instantiates a new connect event.
	 *
	 * @param principal the principal
	 */
	public ConnectEvent(Principal principal) {
		super(principal);
		this.principal = principal;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public Principal getPrincipal() {
		return principal;
	}

	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}

}
