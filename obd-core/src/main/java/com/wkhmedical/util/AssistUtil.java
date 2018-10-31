package com.wkhmedical.util;

import java.lang.reflect.Field;

import com.alibaba.fastjson.JSON;

public class AssistUtil {

	public static <T> T coverBean(Object obj, Class<T> clazz) {
		String json = JSON.toJSONString(obj);
		return JSON.parseObject(json, clazz);
	}

	public static Object combineSydwCore(Object sourceBean, Object targetBean) {
		Class<?> sourceBeanClass = sourceBean.getClass();
		Field[] sourceFields = sourceBeanClass.getDeclaredFields();
		Field[] targetFields = sourceBeanClass.getDeclaredFields();
		for (int i = 0; i < sourceFields.length; i++) {
			Field sourceField = sourceFields[i];
			Field targetField = targetFields[i];
			sourceField.setAccessible(true);
			targetField.setAccessible(true);
			try {
				if (!(sourceField.get(sourceBean) == null)) {
					targetField.set(targetBean, sourceField.get(sourceBean));
				}
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return targetBean;
	}

	public static boolean isArrContains(String[] arr, String value) {
		for (String s : arr) {
			if (s.equals(value)) return true;
		}
		return false;
	}

	public static int getArrContainsIndex(String[] arr, String value) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(value)) return i;
		}
		return -1;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
