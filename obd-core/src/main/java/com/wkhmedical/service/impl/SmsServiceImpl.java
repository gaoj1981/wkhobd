package com.wkhmedical.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wkhmedical.constant.AliSmsSign;
import com.wkhmedical.constant.AliSmsTpl;
import com.wkhmedical.dto.MobiVcode;
import com.wkhmedical.exception.AliException;
import com.wkhmedical.service.SmsService;
import com.wkhmedical.service.VcodeService;
import com.wkhmedical.util.AliSMSUtil;
import com.wkhmedical.util.DateUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class SmsServiceImpl implements SmsService {

	@Autowired
	VcodeService vcodeService;

	@Override
	public void sendMesValiCode(String userMobi, int valiType, String accessToken) {
		// 随机验证码
		String valiCode = RandomStringUtils.randomNumeric(4);
		int sendTimes = 0;
		Date dtNow = new Date();
		Date sendDate = dtNow;
		// 判断验证码合法性
		MobiVcode vcode = new MobiVcode();
		vcode.setUserMobi(userMobi);
		vcode.setValiType(valiType);
		vcode = vcodeService.getMobiCode(vcode);
		if (vcode != null) {
			valiCode = vcode.getValiCode();
			sendDate = vcode.getSendDate();
			int minutes = DateUtil.getDiffMinutes(dtNow, sendDate);
			sendTimes = vcode.getSendTimes();
			if (minutes < 5) {
				if (sendTimes > 3) {
					// 5分钟内发送短信超过3次
					throw new AliException("ali_sendmsg_overtimes", userMobi);
				}
			} else {
				// 距离上次短信超过5分钟，初始化短信发送情况
				vcodeService.delMobiCode(vcode);
				sendDate = dtNow;
				sendTimes = 0;
				valiCode = RandomStringUtils.randomNumeric(4);
			}
		}
		// 发送短信至手机
		Map<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("code", valiCode);
		log.info(AliSMSUtil.class + " " + valiCode);
		String tplCode = null;
		if (valiType == 1) {
			tplCode = AliSmsTpl.SMS_147105260.name();
		} else if (valiType == 2) {
			tplCode = AliSmsTpl.SMS_147105259.name();
		} else {
			throw new AliException("public_apiparam_blank", "valiType");
		}
		// 放开此注释，真实发送短信至手机
		if (!AliSMSUtil.sendMnsValiCode(mapParam, userMobi, AliSmsSign.上海码诺.name(), tplCode, null)) {
			throw new AliException("ali_sendmsg_error", "");
		}
		vcodeService.delImgCode(accessToken);

		// 将现有短信发送情况更新到缓存
		vcode = new MobiVcode();
		vcode.setSendDate(sendDate);
		sendTimes++;
		vcode.setSendTimes(sendTimes);
		vcode.setUserMobi(userMobi);
		vcode.setValiCode(valiCode);
		vcode.setValiType(valiType);
		vcodeService.genMobiCode(vcode);
	}

}
