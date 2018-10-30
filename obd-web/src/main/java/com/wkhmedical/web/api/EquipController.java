/**
 * 
 */
package com.wkhmedical.web.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.taoxeo.boot.security.CurrentUser;
import com.wkhmedical.constant.BizConstant;
import com.wkhmedical.dto.EquipExcelDTO;
import com.wkhmedical.dto.UploadResult;
import com.wkhmedical.security.TUserDetails;
import com.wkhmedical.util.FileUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "设备接口")
@RequestMapping("/api/equip")
public class EquipController {

	@ApiOperation(value = "上传设备Excel")
	@PostMapping("upload")
	public UploadResult uploadExcel(MultipartHttpServletRequest multiRequest, HttpServletRequest request, @CurrentUser TUserDetails user) {
		UploadResult rtnRes = new UploadResult();
		try {
			Map<String, MultipartFile> files = multiRequest.getFileMap();
			MultipartFile file = files.get("file");
			//
			rtnRes = FileUtil.checkFileFormat(rtnRes, file, "xls,xlsx");
			if (!rtnRes.getIsSuc()) {
				return rtnRes;
			}
			//
			rtnRes = FileUtil.checkFileSize(rtnRes, file, 100000);
			if (!rtnRes.getIsSuc()) {
				return rtnRes;
			}
			// 执行上传
			rtnRes = FileUtil.uploadFile(rtnRes, file);
		}
		catch (Exception e) {
			log.error("上传设备Excel报错");
			e.printStackTrace();
			rtnRes.setIsSuc(false);
			rtnRes.setErrMsg(BizConstant.ERR_UNKNOWN);
		}
		return rtnRes;
	}

	@ApiOperation(value = "解析和检验设备Excel")
	@GetMapping("/excel.analysis")
	public EquipExcelDTO excelAnalysis(@ApiParam(value = "excel文件路径", required = true) @RequestParam String excelPath,
			@ApiParam(value = "区县ID", required = true) @RequestParam Integer areaId) {
		return null;
	}

}
