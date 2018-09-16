package com.wkhmedical.util;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wkhmedical.config.ConfigProperties;
import com.wkhmedical.constant.Gender;
import com.wkhmedical.dto.IdCardValidator;

@Component
public class BizUtil {

	private static ConfigProperties configProps;

	@Autowired
	public void setConfigProps(ConfigProperties configProps) {
		BizUtil.configProps = configProps;
	}

	public static Long genDbId() {
		SnowflakeIdWorker idWorker = new SnowflakeIdWorker(configProps.getDbWorkerId(), configProps.getDbDatacenterId());
		return idWorker.nextId();
	}

	public static IdCardValidator getCertNoInfo(String certificateNo) {
		IdCardValidator idCardValiInfo = new IdCardValidator();
		idCardValiInfo.setIdCard(certificateNo);
		if (certificateNo.length() == 15) {
			idCardValiInfo.setVali(false);
			idCardValiInfo.setErrMsg("身份证号不支持15位");
			return idCardValiInfo;
		}
		else if (certificateNo.length() == 18) {
			String address = certificateNo.substring(0, 6);// 6位，地区代码
			String birthday = certificateNo.substring(6, 14);// 8位，出生日期
			String[] provinceArray = { "11:北京", "12:天津", "13:河北", "14:山西", "15:内蒙古", "21:辽宁", "22:吉林", "23:黑龙江", "31:上海", "32:江苏", "33:浙江", "34:安徽",
					"35:福建", "36:江西", "37:山东", "41:河南", "42:湖北 ", "43:湖南", "44:广东", "45:广西", "46:海南", "50:重庆", "51:四川", "52:贵州", "53:云南", "54:西藏 ",
					"61:陕西", "62:甘肃", "63:青海", "64:宁夏", "65:新疆", "71:台湾", "81:香港", "82:澳门", "91:国外" };
			boolean valideAddress = false;
			for (int i = 0; i < provinceArray.length; i++) {
				String provinceKey = provinceArray[i].split(":")[0];
				if (provinceKey.equals(address.substring(0, 2))) {
					valideAddress = true;
					idCardValiInfo.setProvinceCode(provinceKey);
					break;
				}
			}
			if (valideAddress) {
				try {
					String year = birthday.substring(0, 4);
					String month = birthday.substring(4, 6);
					String day = birthday.substring(6);
					Calendar cld = Calendar.getInstance();
					Date birthDate = DateUtil.parseToDate(birthday, "yyyyMMdd");
					cld.setTime(birthDate);
					if ((cld.get(Calendar.YEAR) != Integer.parseInt(year) || cld.get(Calendar.MONTH) != Integer.parseInt(month) - 1
							|| cld.get(Calendar.DAY_OF_MONTH) != Integer.parseInt(day))) {
						idCardValiInfo.setVali(false);
						idCardValiInfo.setErrMsg("出生年月日错误");
						return idCardValiInfo;
					}
					idCardValiInfo.setBirthDay(birthDate);
				}
				catch (Exception e) {
					idCardValiInfo.setVali(false);
					idCardValiInfo.setErrMsg("出生年月日错误");
					return idCardValiInfo;
				}
				String[] certificateNoArray = new String[certificateNo.length()];
				for (int i = 0; i < certificateNo.length(); i++) {
					certificateNoArray[i] = String.valueOf(certificateNo.charAt(i));
				}
				int sexFlg = Integer.valueOf(certificateNoArray[16]);
				if (sexFlg % 2 == 0) {
					idCardValiInfo.setSex(Gender.F);
				}
				else {
					idCardValiInfo.setSex(Gender.M);
				}
				//
				int[] weightedFactors = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };// 加权因子
				int[] valideCode = { 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 };// 身份证验证位值，其中10代表X
				int sum = 0;// 声明加权求和变量
				if ("x".equals(certificateNoArray[17].toLowerCase())) {
					certificateNoArray[17] = "10";// 将最后位为x的验证码替换为10
				}
				for (int i = 0; i < 17; i++) {
					sum += weightedFactors[i] * Integer.parseInt(certificateNoArray[i]);// 加权求和
				}
				int valCodePosition = sum % 11;// 得到验证码所在位置
				if (Integer.parseInt(certificateNoArray[17]) == valideCode[valCodePosition]) {
					idCardValiInfo.setVali(true);
					return idCardValiInfo;
				}
				else {
					idCardValiInfo.setVali(false);
					idCardValiInfo.setErrMsg("验证码所在位置错误");
					return idCardValiInfo;
				}
			}
			else {
				idCardValiInfo.setVali(false);
				idCardValiInfo.setErrMsg("身份证号所属地区错误");
				return idCardValiInfo;
			}
		}
		else {
			idCardValiInfo.setVali(false);
			idCardValiInfo.setErrMsg("身份证号位数错误");
			return idCardValiInfo;
		}
	}

}
