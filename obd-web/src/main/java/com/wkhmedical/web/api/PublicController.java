/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical.web.api;

import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.patchca.background.TransparentBackgroundFactory;
import org.patchca.color.RandomColorFactory;
import org.patchca.filter.library.CurvesImageOp;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.AdaptiveRandomWordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wkhmedical.exception.PublicException;
import com.wkhmedical.service.SmsService;
import com.wkhmedical.service.VcodeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * The Class PublicController.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@RestController
@Api(tags = "公共接口")
@RequestMapping("/public")
public class PublicController {

	@Autowired
	VcodeService vcodeService;
	@Autowired
	SmsService smsService;

	/**
	 * dateNow.
	 *
	 * @return the date
	 */
	@ApiOperation(value = "服务端系统时间")
	@GetMapping("/sys/date")
	public Date dateNow() {
		return new Date();
	}

	/**
	 * 获取图形验证码.
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the string
	 */
	@ApiOperation(value = "获取图形验证码")
	@GetMapping("captcha")
	public String captchaImg(HttpServletRequest request, HttpServletResponse response) {
		ConfigurableCaptchaService cs = new ConfigurableCaptchaService();

		AdaptiveRandomWordFactory wordFactory = new AdaptiveRandomWordFactory();
		wordFactory.setMaxLength(4);
		wordFactory.setMinLength(4);
		// wordFactory.setCharacters("2345678abcdefhijkmnpqrstuvwxyz");
		wordFactory.setCharacters("123456789");
		cs.setWordFactory(wordFactory);
		cs.setBackgroundFactory(new TransparentBackgroundFactory());
		cs.setColorFactory(new RandomColorFactory());

		CurvesRippleFilterFactory filterFactory = new CurvesRippleFilterFactory(new RandomColorFactory()) {
			@Override
			protected List<BufferedImageOp> getPostRippleFilters() {
				CurvesImageOp curves = new CurvesImageOp();
				curves.setColorFactory(new RandomColorFactory());
				List<BufferedImageOp> list = new ArrayList<>();
				list.add(curves);
				return list;
			}
		};

		cs.setFilterFactory(filterFactory);

		response.setContentType("image/png");

		// 直接写图片流
		try (OutputStream out = response.getOutputStream()) {
			String captcha = EncoderHelper.getChallangeAndWriteImage(cs, "png", out);
			String accessToken = request.getParameter("access_token");
			vcodeService.genImgCode(accessToken, captcha);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 发送手机验证码.
	 *
	 * @param userMobi
	 *            手机号
	 * @param valiType
	 *            验证码类型（1：注册用；2：密码用）
	 * @param imgCode
	 *            图形验证码
	 * @param request
	 *            the request
	 */
	@ApiOperation(value = "发送手机验证码")
	@PostMapping("send.mobimsg")
	public void sendMobiMsg(
			@ApiParam(name = "user_mobi", value = "手机号", required = true) @RequestParam(name = "user_mobi") String userMobi,
			@ApiParam(name = "vali_type", value = "验证码类型（1：注册用；2：密码用）", required = true) @RequestParam(name = "vali_type") int valiType,
			@ApiParam(name = "img_code", value = "图形验证码", required = true) @RequestParam(name = "img_code") String imgCode,
			HttpServletRequest request) {
		// 图形验证码校验
		String accessToken = request.getParameter("access_token");
		String imgVali = vcodeService.getImgCode(accessToken);
		if (!imgCode.equals(imgVali)) {
			throw new PublicException("public_captcha_error", "");
		}
		// 短信发送
		smsService.sendMesValiCode(userMobi, valiType, accessToken);
	}

}
