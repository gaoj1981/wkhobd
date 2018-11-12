package com.wkhmedical.constant;

import java.util.HashMap;
import java.util.Map;

public class BizConstant {
	// 异常
	public final static String ERR_UNKNOWN = "发生未知异常";

	// 每页查询出的数据库记录条数
	public final static int FIND_PAGE_NUM = 20;
	public final static int FIND_PAGE_MAXNUM = 50;

	// 手机验证码类型；1：注册用；2：密码用
	public final static Integer MOBI_VALITYPE_1 = 1;
	public final static Integer MOBI_VALITYPE_2 = 2;

	// 云账户状态；0：未启动；1：正常；2：锁定
	public final static Integer USER_STATE_0 = 0;
	public final static Integer USER_STATE_1 = 1;
	public final static Integer USER_STATE_2 = 2;

	// 省份简称
	public final static Map<Integer, String> MAP_PROV_ELLIPSIS = new HashMap<Integer, String>();
	static {
		MAP_PROV_ELLIPSIS.put(110000, "京");
		MAP_PROV_ELLIPSIS.put(120000, "津");
		MAP_PROV_ELLIPSIS.put(130000, "冀");
		MAP_PROV_ELLIPSIS.put(140000, "晋");
		MAP_PROV_ELLIPSIS.put(150000, "蒙");
		MAP_PROV_ELLIPSIS.put(210000, "辽");
		MAP_PROV_ELLIPSIS.put(220000, "吉");
		MAP_PROV_ELLIPSIS.put(230000, "黑");
		MAP_PROV_ELLIPSIS.put(310000, "沪");
		MAP_PROV_ELLIPSIS.put(320000, "苏");
		MAP_PROV_ELLIPSIS.put(330000, "浙");
		MAP_PROV_ELLIPSIS.put(340000, "皖");
		MAP_PROV_ELLIPSIS.put(350000, "闽");
		MAP_PROV_ELLIPSIS.put(360000, "赣");
		MAP_PROV_ELLIPSIS.put(370000, "鲁");
		MAP_PROV_ELLIPSIS.put(410000, "豫");
		MAP_PROV_ELLIPSIS.put(420000, "鄂");
		MAP_PROV_ELLIPSIS.put(430000, "湘");
		MAP_PROV_ELLIPSIS.put(440000, "粤");
		MAP_PROV_ELLIPSIS.put(450000, "桂");
		MAP_PROV_ELLIPSIS.put(460000, "琼");
		MAP_PROV_ELLIPSIS.put(500000, "渝");
		MAP_PROV_ELLIPSIS.put(510000, "川");
		MAP_PROV_ELLIPSIS.put(520000, "贵");
		MAP_PROV_ELLIPSIS.put(530000, "云");
		MAP_PROV_ELLIPSIS.put(540000, "藏");
		MAP_PROV_ELLIPSIS.put(610000, "陕");
		MAP_PROV_ELLIPSIS.put(620000, "甘");
		MAP_PROV_ELLIPSIS.put(630000, "青");
		MAP_PROV_ELLIPSIS.put(640000, "宁");
		MAP_PROV_ELLIPSIS.put(650000, "新");
		MAP_PROV_ELLIPSIS.put(710000, "台");
		MAP_PROV_ELLIPSIS.put(810000, "港");
		MAP_PROV_ELLIPSIS.put(820000, "澳");
	}
}
