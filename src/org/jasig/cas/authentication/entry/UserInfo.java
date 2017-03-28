package org.jasig.cas.authentication.entry;

public class UserInfo {

	private int id;//自增主键
	private String name;//用户名称
	private String account;//用户账号
	private String password;//密码
	private String digitalID;//数字电子证书编号
	private String email;//电子邮箱
	private String areaCode;//归属地
	private String regTime;//注册时间
	
	public UserInfo() {
	}

	public UserInfo(int id, String name, String account, String password, String digitalID, String email,
			String areaCode, String regTime) {
		super();
		this.id = id;
		this.name = name;
		this.account = account;
		this.password = password;
		this.digitalID = digitalID;
		this.email = email;
		this.areaCode = areaCode;
		this.regTime = regTime;
	}

	public UserInfo(String name, String account, String password, String digitalID, String email, String areaCode,
			String regTime) {
		super();
		this.name = name;
		this.account = account;
		this.password = password;
		this.digitalID = digitalID;
		this.email = email;
		this.areaCode = areaCode;
		this.regTime = regTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDigitalID() {
		return digitalID;
	}

	public void setDigitalID(String digitalID) {
		this.digitalID = digitalID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", name=" + name + ", account=" + account + ", password=" + password
				+ ", digitalID=" + digitalID + ", email=" + email + ", areaCode=" + areaCode + ", regTime=" + regTime
				+ "]";
	}

	
}
