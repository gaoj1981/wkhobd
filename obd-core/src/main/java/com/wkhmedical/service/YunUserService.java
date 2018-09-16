package com.wkhmedical.service;

import com.wkhmedical.dto.RegisterBody;
import com.wkhmedical.dto.UserDTO;
import com.wkhmedical.dto.YunuserBody;

public interface YunUserService {

	void registerUser(RegisterBody registerBody);

	UserDTO findUserInfo(String credId);

	void updateUserPwd(String userMobi, String userPwd, int pwdFlag, String oldPwd, YunuserBody userBody);

}
