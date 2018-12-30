package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.BindUser;

@Repository
public interface BindUserRepository extends JpaRepository<BindUser, String>, IBindUserRepository {

	BindUser findByIdAndDelFlag(String id, Integer delFlag);

	BindUser findByUtypeAndAreaIdAndIsDefault(Integer utype, Long areaId, Integer isDefault);

	List<BindUser> findByAreaIdAndIsDefault(Long areaId, Integer isDefault);
}
