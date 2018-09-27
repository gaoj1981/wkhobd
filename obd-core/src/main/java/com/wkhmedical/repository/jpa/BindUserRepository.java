package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.BindUser;

@Repository
public interface BindUserRepository extends JpaRepository<BindUser, Long>, IBindUserRepository {
	
}
