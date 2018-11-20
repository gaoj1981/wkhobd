package com.wkhmedical.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wkhmedical.constant.Ethnic;
import com.wkhmedical.constant.Gender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "yun_user")
@DynamicInsert
@DynamicUpdate
public class YunUser implements Serializable {
	private static final long serialVersionUID = 1427381057257374891L;

	@Id
	private String id;

	private String userName;

	private String userIdCard;

	private String userPwd;

	private String userPwdSalt;

	private String userMobi;

	private Date userBirth;

	private String roleId;

	private Integer state;

	@Enumerated(EnumType.STRING)
	private Gender userSex;

	@Enumerated(EnumType.STRING)
	private Ethnic userEthnic;

}
