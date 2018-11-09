/*
 * Copyright (C) 2018 WKH-Medical. All rights reserved.
 */
package com.wkhmedical.message;

import java.util.List;

/**
 * The Interface MessagePublish.
 *
 * @author Derek
 * @since 1.0, 2018-2-28
 */
public interface MessagePublish {
	
	/**
	 * 批量发布消息.
	 *
	 * @param destServer 目标服务器
	 * @param payload 消息负载
	 */
	void publish(String destServer, List<Message> payload);
}
