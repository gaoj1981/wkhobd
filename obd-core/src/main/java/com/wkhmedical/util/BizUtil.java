package com.wkhmedical.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.wkhmedical.config.ConfigProperties;
import com.wkhmedical.constant.BizConstant;
import com.wkhmedical.constant.Gender;
import com.wkhmedical.dto.IdCardValidator;

@Component
public class BizUtil {

	private static ConfigProperties configProps;

	@Autowired
	public void setConfigProps(ConfigProperties configProps) {
		BizUtil.configProps = configProps;
	}

	public static String getUploadPath() {
		return configProps.getUploadPath();
	}

	public static Long getDbWorkerId() {
		return configProps.getDbWorkerId();
	}

	public static Long getDbDatacenterId() {
		return configProps.getDbDatacenterId();
	}

	public static Long genDbId(SnowflakeIdWorker idWorker) {
		return idWorker.nextId();
	}

	public static String genDbIdStr(SnowflakeIdWorker idWorker) {
		return idWorker.nextId() + "";
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

	public static void setSqlJoin(Object obj, String fname, StringBuffer sqlBuf, List<Object> paramList, String sqlStr) {
		Object value = getFieldValueByName(fname, obj);
		if (value != null) {
			sqlBuf.append(sqlStr);
			if (sqlStr.indexOf("LIKE") > 0) {
				paramList.add("%" + value + "%");
			}
			else {
				paramList.add(value);
			}
		}
	}

	public static void setSqlWhere(Object obj, String fnames, StringBuffer sqlWhere, List<Object> objList, List<String> sqlStrList) {
		String[] fnArr = fnames.split(",");
		String fname, sqlStr;
		for (int i = 0; i < fnArr.length; i++) {
			fname = fnArr[i];
			sqlStr = sqlStrList.get(i);
			Object value = getFieldValueByName(fname, obj);
			if (value != null) {
				sqlWhere.append(sqlStr);
				if (sqlStr.indexOf("LIKE") > 0) {
					objList.add("%" + value + "%");
				}
				else {
					objList.add(value);
				}
			}
		}
	}

	public static String getSqlOrder(Sort sort, String[] fnamesArr, String[] onamesArr, String initOrders) {
		if (sort == null || fnamesArr == null || onamesArr == null || fnamesArr.length != onamesArr.length) {
			if (initOrders != null) {
				return initOrders;
			}
			return "";
		}
		//
		StringBuffer sqlOrder = new StringBuffer(" ORDER BY");
		for (int i = 0; i < fnamesArr.length; i++) {
			Order order = sort.getOrderFor(fnamesArr[i]);
			if (order == null) {
				continue;
			}
			if (i > 0) {
				sqlOrder.append(",");
			}
			sqlOrder.append(" " + onamesArr[i] + " " + order.getDirection().name());
		}
		//
		if (sqlOrder.length() > 10) {
			return sqlOrder.toString();
		}
		else {
			if (initOrders != null) {
				return initOrders;
			}
			return "";
		}
	}

	/** * 根据属性名获取属性值 * */
	public static Object getFieldValueByName(String fname, Object obj) {
		try {
			String firstLetter = fname.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fname.substring(1);
			Method method = obj.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(obj, new Object[] {});
			return value;
		}
		catch (Exception e) {
			return null;
		}
	}

	public static Map<String, Object> getFieldInfo(Object obj, String fname) {
		try {
			Field field = obj.getClass().getDeclaredField(fname);
			Map<String, Object> infoMap = new HashMap<String, Object>();
			infoMap.put("type", field.getType().toString());
			infoMap.put("value", getFieldValueByName(fname, obj));
			return infoMap;
		}
		catch (Exception e) {
			return null;
		}
	}

	public static Integer getProvId(int areaId) {
		return areaId / 10000 * 10000;
	}

	public static Integer getCityId(int areaId) {
		int provId = getProvId(areaId);
		if (provId == 110000 || provId == 310000 || provId == 120000 || provId == 500000) {
			// 直辖市，省ID和市ID返回同一值
			return provId;
		}
		return areaId / 100 * 100;
	}

	public static String getDelBackupVal(String delFieldVal) {
		if (StringUtils.isEmpty(delFieldVal)) {
			return null;
		}
		else {
			return delFieldVal + "_del_" + System.currentTimeMillis();
		}
	}

	public static String getResourcesStr(InputStream inputStream) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		String content = result.toString(StandardCharsets.UTF_8.name());
		inputStream.close();
		result.close();
		return content;
	}

	public static Integer[] getPgArr(Integer page, Integer size) {
		Integer[] rtnArr = new Integer[2];
		if (page == null) {
			page = 0;
		}
		else {
			page = page - 1;
			if (page < 0) page = 0;
		}
		if (size == null) {
			size = BizConstant.FIND_PAGE_NUM;
		}
		else if (size > BizConstant.FIND_PAGE_MAXNUM) {
			size = BizConstant.FIND_PAGE_MAXNUM;
		}
		rtnArr[0] = page;
		rtnArr[1] = size;
		return rtnArr;
	}

}
