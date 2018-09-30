package com.wkhmedical.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.wkhmedical.config.AliSmsProperties;
import com.wkhmedical.exception.AliException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class AliSMSUtil {

	private static AliSmsProperties aliSmsProperties;

	@Autowired
	public void setConfigProps(AliSmsProperties aliSmsProperties) {
		AliSMSUtil.aliSmsProperties = aliSmsProperties;
	}

	/**
	 * 
	 * @param mapParam
	 *            模板消息参数
	 * @param mobis
	 *            手机号（多个手机“,”分隔）
	 * @param smsSign
	 *            短信签名
	 * @param tplCode
	 *            模板编号
	 * @param exStr
	 *            扩展参数（暂无用）
	 * @return
	 */
	public static boolean sendMnsValiCode(Map<String, String> mapParam, String mobis, String smsSign, String tplCode,
			String exStr) {
		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliSmsProperties.getAccessKeyId(),
				aliSmsProperties.getAccessKeySecret());
		try {
			DefaultProfile.addEndpoint(aliSmsProperties.getEndpointName(), aliSmsProperties.getEndpointRegion(),
					aliSmsProperties.getProduct(), aliSmsProperties.getDomain());
		} catch (ClientException e) {
			e.printStackTrace();
		}
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(mobis);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(smsSign);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(tplCode);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		request.setTemplateParam(JSONObject.toJSONString(mapParam));

		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		request.setOutId(StringUtils.isEmpty(exStr) ? "NONE" : exStr);

		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse = null;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		log.info("短信：" + mobis + " 内容：" + mapParam + " Message: " + sendSmsResponse.getMessage());
		if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
			return true;
		} else {
			throw new AliException("ali_sendmsg_error", sendSmsResponse.getMessage());
		}
	}

}
