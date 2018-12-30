package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.BaseArea;

@Repository
public interface BaseAreaRepository extends JpaRepository<BaseArea, String>, IBaseAreaRepository {

	List<BaseArea> findByPid(Long pid);

	List<BaseArea> findByLevel(Integer level);
}
