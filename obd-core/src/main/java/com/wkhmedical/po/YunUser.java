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

@Entity
@Table(name = "yun_user")
@DynamicInsert
@DynamicUpdate
public class YunUser implements Serializable {
	private static final long serialVersionUID = 1427381057257374891L;

	@Id
	private Long id;

	private String userName;

	private String userIdCard;

	private String userPwd;

	private String userPwdSalt;

	private String userMobi;

	private Date userBirth;

	private Integer state;

	@Enumerated(EnumType.STRING)
	private Gender userSex;

	@Enumerated(EnumType.STRING)
	private Ethnic userEthnic;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserIdCard() {
		return userIdCard;
	}

	public void setUserIdCard(String userIdCard) {
		this.userIdCard = userIdCard;
	}

	public String getUserPwdSalt() {
		return userPwdSalt;
	}

	public void setUserPwdSalt(String userPwdSalt) {
		this.userPwdSalt = userPwdSalt;
	}

	public String getUserMobi() {
		return userMobi;
	}

	public void setUserMobi(String userMobi) {
		this.userMobi = userMobi;
	}

	public Date getUserBirth() {
		return userBirth;
	}

	public void setUserBirth(Date userBirth) {
		this.userBirth = userBirth;
	}

	public Gender getUserSex() {
		return userSex;
	}

	public void setUserSex(Gender userSex) {
		this.userSex = userSex;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public Ethnic getUserEthnic() {
		return userEthnic;
	}

	public void setUserEthnic(Ethnic userEthnic) {
		this.userEthnic = userEthnic;
	}

}
