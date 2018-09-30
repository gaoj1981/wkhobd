package com.wkhmedical.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.wkhmedical.dto.MobiVcode;
import com.wkhmedical.dto.Vcode;
import com.wkhmedical.service.VcodeService;

@Service
public class VcodeServiceImpl implements VcodeService {

	@CachePut(cacheNames = "cachecode", key = "'vcode_'+#phone")
	public Vcode genVcode(String phone) {
		Vcode vcode = new Vcode();
		vcode.setPhone(phone);
		vcode.setCode(RandomStringUtils.randomAlphanumeric(4));
		return vcode;
	}

	@Cacheable(cacheNames = "cachecode", key = "'vcode_'+#phone")
	public Vcode getVcode(String phone) {
		return null;
	}

	/**
	 * 缓存设置手机验证码
	 */
	@CachePut(cacheNames = "cachecode", key = "'vcode_'+#vcode.userMobi+'_'+#vcode.valiType")
	public MobiVcode genMobiCode(MobiVcode vcode) {
		return vcode;
	}

	/**
	 * 获取缓存手机验证码
	 */
	@Cacheable(cacheNames = "cachecode", key = "'vcode_'+#vcode.userMobi+'_'+#vcode.valiType")
	public MobiVcode getMobiCode(MobiVcode vcode) {
		return null;
	}

	/**
	 * 删除缓存验证码
	 */
	@CacheEvict(cacheNames = "cachecode", key = "'vcode_'+#vcode.userMobi+'_'+#vcode.valiType")
	public MobiVcode delMobiCode(MobiVcode vcode) {
		return null;
	}

	/**
	 * 缓存设置图形验证码
	 */
	@CachePut(cacheNames = "cachecode", key = "'vcode_imgcaptcha_'+#accessToken")
	public String genImgCode(String accessToken, String imgCaptcha) {
		return imgCaptcha;
	}

	/**
	 * 获取缓存图形验证码
	 */
	@Cacheable(cacheNames = "cachecode", key = "'vcode_imgcaptcha_'+#accessToken")
	public String getImgCode(String accessToken) {
		return null;
	}

	/**
	 * 删除图形验证码
	 */
	@CacheEvict(cacheNames = "cachecode", key = "'vcode_imgcaptcha_'+#accessToken")
	public String delImgCode(String accessToken) {
		return null;
	}

}
