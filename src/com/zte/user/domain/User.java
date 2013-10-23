package com.zte.user.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import sun.misc.Sort;

@Component
public class User {
	private int id;
	private List<String> listStr;
	private List<Integer> listInt;
	private String username;
	private String password;
	private String name;
	private String photo;
	private int age;
	private Integer a0;
	private boolean a1;
	private Boolean a2;
	private Date a3;
	private double a4;
	private Double a5;
	private Short a6;
	private short a8;
	private BigDecimal a7;
	
	
	@Override
	public String toString(){
		return  "id:"+id+"\n"+
				"username:"+username+"\n"+
				"password:"+password+"\n"+
				"name:"+name+"\n"+
				"age:"+age+"\n"+
				"photo:"+photo;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<String> getListStr() {
		return listStr;
	}

	public void setListStr(List<String> listStr) {
		this.listStr = listStr;
	}

	public List<Integer> getListInt() {
		return listInt;
	}

	public void setListInt(List<Integer> listInt) {
		this.listInt = listInt;
	}

	public boolean isA1() {
		return a1;
	}

	public void setA1(boolean a1) {
		this.a1 = a1;
	}

	public Boolean getA2() {
		return a2;
	}

	public void setA2(Boolean a2) {
		this.a2 = a2;
	}

	public Date getA3() {
		return a3;
	}

	public void setA3(Date a3) {
		this.a3 = a3;
	}

	public double getA4() {
		return a4;
	}

	public void setA4(double a4) {
		this.a4 = a4;
	}

	public Double getA5() {
		return a5;
	}

	public void setA5(Double a5) {
		this.a5 = a5;
	}

	public Short getA6() {
		return a6;
	}

	public void setA6(Short a6) {
		this.a6 = a6;
	}

	public BigDecimal getA7() {
		return a7;
	}

	public void setA7(BigDecimal a7) {
		this.a7 = a7;
	}

	public short getA8() {
		return a8;
	}

	public void setA8(short a8) {
		this.a8 = a8;
	}

	public Integer getA0() {
		return a0;
	}

	public void setA0(Integer a0) {
		this.a0 = a0;
	}
	
}
