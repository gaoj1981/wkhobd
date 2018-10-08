package com.wkhmedical.web.api;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.wkhmedical.util.BizUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "地理接口")
@RequestMapping("/api/geographic")
public class GeographicController {

	@ApiOperation(value = "根据城市ID获取区县列表（建议调用方留存，减少网络请求）")
	@GetMapping("/area/{cityId:[0-9]{0,6}}")
	public Object getAreas(@ApiParam(value = "城市ID", required = true) @PathVariable String cityId) throws IOException {
		// TODO 需要Guava Cache
		log.info("GeographicController->getAreas");
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("geogr/area.json");
		String content = BizUtil.getResourcesStr(inputStream);
		JSONObject jso = JSONObject.parseObject(content);
		return jso.get(cityId);
	}

	@ApiOperation(value = "根据省份ID获取城市列表")
	@GetMapping("/city/{provId:[0-9]{0,6}}")
	public Object getCity(@ApiParam(value = "省ID", required = true) @PathVariable String provId) throws IOException {
		// TODO 需要Guava Cache
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("geogr/city.json");
		String content = BizUtil.getResourcesStr(inputStream);
		JSONObject jso = JSONObject.parseObject(content);
		return jso.get(provId);
	}

	@ApiOperation(value = "获取省份列表")
	@GetMapping("/province")
	public Object getProvince() throws IOException {
		// TODO 需要Guava Cache
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("geogr/province.json");
		String content = BizUtil.getResourcesStr(inputStream);
		return JSONObject.parse(content);
	}

	// public Object TestTmp() throws IOException {
	// File file = ResourceUtils.getFile("classpath:geogr/areas.json");
	// String content = FileUtils.readFileToString(file, "UTF-8");
	// JSONArray jsa = JSONArray.parseArray(content);
	// String cityIdTmp;
	// JSONObject jsoProvTmp;
	// JSONObject jsoCityTmp;
	// JSONArray jsarrCityTmp;
	// JSONArray jsarrAreaTmp;
	// JSONObject jsoAreaTmp;
	// String provNameTmp;
	// String cityNameTmp;
	//
	// JSONObject rtnJso = new JSONObject();
	// JSONArray areaArr;
	// JSONObject areaJso;
	// for (int i = 0; i < jsa.size(); i++) {
	// jsoProvTmp = JSONObject.parseObject(jsa.getString(i));
	// provNameTmp = jsoProvTmp.getString("label");
	// cityIdTmp = jsoProvTmp.getString("value");
	// jsarrCityTmp = jsoProvTmp.getJSONArray("children");
	// areaArr = new JSONArray();
	// for (int j = 0; j < jsarrCityTmp.size(); j++) {
	// jsoCityTmp = jsarrCityTmp.getJSONObject(j);
	// cityNameTmp = jsoCityTmp.getString("label");
	// if (jsoCityTmp.containsKey("children")) {
	// cityIdTmp = jsoCityTmp.getString("value");
	// areaArr = new JSONArray();
	// jsarrAreaTmp = jsoCityTmp.getJSONArray("children");
	// for (int k = 0; k < jsarrAreaTmp.size(); k++) {
	// jsoAreaTmp = jsarrAreaTmp.getJSONObject(k);
	// areaJso = new JSONObject();
	// areaJso.put("province", provNameTmp);
	// areaJso.put("city", cityNameTmp);
	// areaJso.put("id", jsoAreaTmp.getString("value"));
	// areaJso.put("name", jsoAreaTmp.getString("label"));
	// areaArr.add(areaJso);
	// }
	// rtnJso.put(cityIdTmp, areaArr);
	// }
	// }
	// }
	//
	// for (int i = 0; i < jsa.size(); i++) {
	// jsoProvTmp = JSONObject.parseObject(jsa.getString(i));
	// provNameTmp = jsoProvTmp.getString("label");
	// cityIdTmp = jsoProvTmp.getString("value");
	// jsarrCityTmp = jsoProvTmp.getJSONArray("children");
	// areaArr = new JSONArray();
	// for (int j = 0; j < jsarrCityTmp.size(); j++) {
	// jsoCityTmp = jsarrCityTmp.getJSONObject(j);
	// cityNameTmp = jsoCityTmp.getString("label");
	// if (jsoCityTmp.containsKey("children")) {
	// continue;
	// }
	// areaJso = new JSONObject();
	// areaJso.put("province", provNameTmp);
	// areaJso.put("city", cityNameTmp);
	// areaJso.put("id", jsoCityTmp.getString("value"));
	// areaJso.put("name", jsoCityTmp.getString("label"));
	// areaArr.add(areaJso);
	// }
	// if (!areaArr.isEmpty()) {
	// rtnJso.put(cityIdTmp, areaArr);
	// }
	// }
	// System.out.println(rtnJso);
	// return null;
	// }

}
