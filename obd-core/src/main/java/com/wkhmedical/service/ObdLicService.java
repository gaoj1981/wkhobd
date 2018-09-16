package com.wkhmedical.service;

import com.wkhmedical.dto.ObdLicDTO;

public interface ObdLicService {

	ObdLicDTO getObdLic(String id, String rsaStr);
}
