package com.wkhmedical.service;

public interface SmsService {
	void sendMesValiCode(String userMobi, int valiType, String accessToken);
}
