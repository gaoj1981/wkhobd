package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.YunUser;

@Repository
public interface YunUserRepository extends JpaRepository<YunUser, Long>, IYunUserRepository {
	YunUser findByUserIdCardOrUserMobi(String idCard, String userMobi);

	YunUser findByUserMobi(String userMobi);

	YunUser findByUserIdCard(String idCard);
}
