/**
 * 
 */
package com.wkhmedical.util;

import java.io.File;
import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import com.wkhmedical.constant.BizConstant;
import com.wkhmedical.dto.UploadResult;

/**
 * @author Administrator
 */
public class FileUtil {

	/**
	 * 获取文件含单位大小
	 * 
	 * @param size
	 * @return
	 */
	public static String getSizeFormat(long size) {
		BigDecimal bdSize = new BigDecimal(size);
		BigDecimal bd1000 = new BigDecimal(1000);
		BigDecimal bd1000000 = new BigDecimal(1000000);
		if (size > 1000000000) {
			return bdSize.divide(bd1000000).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "G";
		}
		else if (size > 1000000) {
			return bdSize.divide(bd1000).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "M";
		}
		else {
			return bdSize.divide(bd1000).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "K";
		}
	}

	/**
	 * 获取文件名后缀
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileEnd(MultipartFile file) {
		if (file == null) return null;
		String fileOriginalName = file.getOriginalFilename().toLowerCase();
		return fileOriginalName.substring(fileOriginalName.lastIndexOf(".") + 1);
	}

	public static UploadResult uploadFile(UploadResult result, MultipartFile file) {
		try {
			String uploadPath = BizUtil.getUploadPath();
			String dtNow = DateUtil.getNowDateByFormat("yyyy/MM/dd/HH");
			String filePureName = DateUtil.getStrTimes() + BizUtil.genDbId();
			String fileName = filePureName + "." + getFileEnd(file);
			String fileDir = "/" + dtNow + "/";
			File localFile = new File(uploadPath + fileDir + fileName);
			//
			File fileParent = localFile.getParentFile();
			if (!fileParent.exists()) {
				fileParent.mkdirs();
			}
			// 将上传文件写到服务器上指定的文件
			file.transferTo(localFile);
			//
			result.setFid(filePureName);
			result.setFname(fileName);
			result.setFdir(fileDir);
			result.setOname(file.getOriginalFilename());
		}
		catch (Exception e) {
			result.setIsSuc(false);
			result.setErrMsg(BizConstant.ERR_UNKNOWN);
		}
		return result;
	}

	/**
	 * 校验上传文件的格式
	 * 
	 * @param result
	 * @param file
	 * @param endFormat
	 * @return
	 */
	public static UploadResult checkFileFormat(UploadResult result, MultipartFile file, String endFormat) {
		String fileOriginalName = file.getOriginalFilename().toLowerCase();
		String fileEnd = fileOriginalName.substring(fileOriginalName.lastIndexOf(".") + 1);
		if (("," + endFormat + ",").indexOf("," + fileEnd + ",") < 0) {
			result.setIsSuc(false);
			result.setErrMsg("上传文件格式不支持！");
		}
		return result;
	}

	/**
	 * 校验上传文件的大小
	 * 
	 * @param result
	 * @param file
	 * @param maxSize
	 * @return
	 */
	public static UploadResult checkFileSize(UploadResult result, MultipartFile file, long maxSize) {
		long size = file.getSize();
		if (size > maxSize) {
			result.setIsSuc(false);
			result.setErrMsg("上传文件过大（" + getSizeFormat(size) + "），不允许超过" + getSizeFormat(maxSize) + "！");
		}
		return result;
	}

}
