/**
 * 
 */
package com.wkhmedical.constant;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wkhmedical.util.BizUtil;

/**
 * @author Administrator
 */
public class Geographic {

	public final static Map<String, Object> GeographicMap = new HashMap<String, Object>();
	static {
		System.out.println(999999999);
		try {
			InputStream inputStream = Geographic.class.getClassLoader().getResourceAsStream("geogr/province.json");
			String content = BizUtil.getResourcesStr(inputStream);
			System.out.println(content);
			GeographicMap.put("province", JSONArray.parseArray(content));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static JSONArray getProvArr() {
		Object obj = GeographicMap.get("province");
		if (obj == null) {
			try {
				InputStream inputStream = Geographic.class.getClassLoader().getResourceAsStream("geogr/province.json");
				String content = BizUtil.getResourcesStr(inputStream);
				GeographicMap.put("province", JSONArray.parseArray(content));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (JSONArray) GeographicMap.get("province");
	}

	public static String getProvName(Integer provId) {
		String idStr = String.valueOf(provId);
		JSONArray provArr = getProvArr();
		if (provArr == null) {
			return null;
		}
		JSONObject jso;
		for (Object obj : provArr) {
			jso = (JSONObject) obj;
			if (idStr.equals(jso.getString("id"))) {
				return jso.getString("name");
			}
		}
		return null;
	}

}
