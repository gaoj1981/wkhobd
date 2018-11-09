/*
 * Copyright (C) 2018 WKH-Medical. All rights reserved.
 */
package com.wkhmedical.message;

import java.io.Serializable;

/**
 * The Class Message.
 *
 * @author Derek
 * @since 1.0, 2018-2-28
 */
public class Message implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6713911412182097837L;
	
	/** The uid. */
	private String uid;
	
	/** The type. */
	private String type;
	
	/** The data. */
	private Object data;
	
	/**
	 * Instantiates a new message.
	 */
	public Message() {
	}

	/**
	 * Instantiates a new message.
	 *
	 * @param uid the uid
	 * @param type the type
	 * @param data the data
	 */
	public Message(String uid, String type, Object data) {
		this.type = type;
		this.data = data;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
