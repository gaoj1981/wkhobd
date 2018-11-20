package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.UserExinfo;

@Repository
public interface UserExinfoRepository extends JpaRepository<UserExinfo, String>, IUserExinfoRepository {
	
}
