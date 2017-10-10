package com.miot.orm;

import java.util.ArrayList;
import java.util.List;

public class Cu extends Object {

	private String password = "";
	private String nickname = "";
	private String mobile = "";

	private List<Home> homes = new ArrayList<Home>();

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<Home> getHomes() {
		return homes;
	}

	public void setHomes(List<Home> homes) {
		this.homes = homes;
	}
}
