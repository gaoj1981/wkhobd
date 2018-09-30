package com.wkhmedical.service;

import com.wkhmedical.dto.MobiVcode;
import com.wkhmedical.dto.Vcode;

public interface VcodeService {

	MobiVcode genMobiCode(MobiVcode vcode);

	MobiVcode getMobiCode(MobiVcode vcode);

	MobiVcode delMobiCode(MobiVcode vcode);

	String genImgCode(String accessToken, String imgCaptcha);

	String getImgCode(String accessToken);

	String delImgCode(String accessToken);

	Vcode genVcode(String phone);

	Vcode getVcode(String phone);
}
