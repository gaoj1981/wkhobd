package com.wkhmedical.repository.jpa;

import com.wkhmedical.dto.UserDTO;

public interface IYunUserRepository {
	UserDTO findUserInfo(String credId);
}
