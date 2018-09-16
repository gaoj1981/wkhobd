package com.wkhmedical.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoxeo.boot.security.PasswordUtils;
import com.taoxeo.boot.security.PasswordUtils.Password;
import com.taoxeo.lang.exception.BizRuntimeException;
import com.wkhmedical.constant.BizConstant;
import com.wkhmedical.dto.IdCardValidator;
import com.wkhmedical.dto.RegisterBody;
import com.wkhmedical.dto.UserDTO;
import com.wkhmedical.dto.YunuserBody;
import com.wkhmedical.po.YunUser;
import com.wkhmedical.repository.jpa.YunUserRepository;
import com.wkhmedical.service.YunUserService;
import com.wkhmedical.util.BizUtil;

@Service
public class YunUserServiceImpl implements YunUserService {

	@Resource
	YunUserRepository userRepository;

	@Override
	@Transactional
	public void registerUser(RegisterBody registerInfo) {
		// 根据身份证号获取用户相关信息
		String userIdCard = registerInfo.getIdCard();
		IdCardValidator idCardInfo = BizUtil.getCertNoInfo(userIdCard);
		if (!idCardInfo.isVali()) {
			throw new BizRuntimeException("user_idcard_error", idCardInfo.getErrMsg());
		}
		// 判断是否已注册
		int isUpdOrIns = 0;
		String userMobi = registerInfo.getUserMobi();
		YunUser dbUser = userRepository.findByUserMobi(userMobi);
		if (dbUser == null) {
			dbUser = userRepository.findByUserIdCard(userIdCard);
		}
		if (dbUser != null) {
			// 防止当前手机号与身份证号分别注册云账号
			if (!userMobi.equals(dbUser.getUserMobi()) || !userIdCard.equals(dbUser.getUserIdCard())) {
				throw new BizRuntimeException("user_already_exists", userIdCard, userMobi);
			}
			if (dbUser.getState().equals(BizConstant.USER_STATE_0)) {
				// 未启动状态，需更新激活状态
				isUpdOrIns = 1;
			}
			else {
				throw new BizRuntimeException("user_already_exists", userIdCard, userMobi);
			}
		}
		// 生成用户密码信息
		String userPwd = registerInfo.getUserPwd();
		if (StringUtils.isBlank(userPwd)) {
			throw new BizRuntimeException("user_pwd_not_exists");
		}
		Password password = PasswordUtils.encodePwd(userPwd);
		// 表中存在用户且处于未启动状态
		if (isUpdOrIns == 1) {
			// 激活状态并设置密码
			dbUser.setUserPwd(password.getPwd());
			dbUser.setUserPwdSalt(password.getPwdSalt());
			dbUser.setState(BizConstant.USER_STATE_1);
			userRepository.update(dbUser);
		}
		else {
			// 入库
			YunUser userInfo = new YunUser();
			userInfo.setId(BizUtil.genDbId());
			userInfo.setUserName(registerInfo.getUserName());
			userInfo.setUserPwd(password.getPwd());
			userInfo.setUserPwdSalt(password.getPwdSalt());
			userInfo.setUserIdCard(userIdCard);
			userInfo.setUserMobi(userMobi);
			userInfo.setUserSex(idCardInfo.getSex());
			userInfo.setUserBirth(idCardInfo.getBirthDay());
			userInfo.setState(BizConstant.USER_STATE_1);
			userRepository.save(userInfo);
		}
	}

	@Override
	public UserDTO findUserInfo(String credId) {
		UserDTO userInfo = userRepository.findUserInfo(credId);
		if (userInfo == null) {
			// 正常业务，但无真实数据，说明存在非法获取可能，因此按返回异常提示
			throw new BizRuntimeException("user_not_exists", credId);
		}
		return userInfo;
	}

	@Override
	public void updateUserPwd(String userMobi, String userPwd, int pwdFlag, String oldPwd, YunuserBody userBody) {
		// check
		if (StringUtils.isBlank(userPwd)) {
			throw new BizRuntimeException("user_pwd_not_exists");
		}
		// 当前会话用户的手机号与参数中的手机号是否一致
		if (userBody != null) {
			String sessMobi = userBody.getUserMobi();
			if (!sessMobi.equals(userMobi)) {
				throw new BizRuntimeException("user_pwd_mobi_not_vali", userMobi);
			}
		}
		// check userMobi isExist
		YunUser user = userRepository.findByUserMobi(userMobi);
		if (user == null) {
			throw new BizRuntimeException("user_not_exists", userMobi);
		}
		if (pwdFlag == 1) {
			if (!PasswordUtils.matchPwd(user.getUserPwd(), oldPwd)) {
				throw new BizRuntimeException("user_pwd_old_error");
			}
		}
		// update
		Password password = PasswordUtils.encodePwd(userPwd);
		user.setUserPwd(password.getPwd());
		user.setUserPwdSalt(password.getPwdSalt());
		userRepository.update(user);
	}

}
