package com.cms.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseAdmin<M extends BaseAdmin<M>> extends Model<M> implements IBean {

	public M setId(Integer id) {
		set("id", id);
		return (M)this;
	}
	
	public Integer getId() {
		return getInt("id");
	}
	
	public M setCreateDate(java.util.Date createDate) {
		set("createDate", createDate);
		return (M)this;
	}
	
	public java.util.Date getCreateDate() {
		return getDate("createDate");
	}
	
	public M setUpdateDate(java.util.Date updateDate) {
		set("updateDate", updateDate);
		return (M)this;
	}
	
	public java.util.Date getUpdateDate() {
		return getDate("updateDate");
	}
	
	public M setName(String name) {
		set("name", name);
		return (M)this;
	}
	
	public String getName() {
		return getStr("name");
	}
	
	public M setPassword(String password) {
		set("password", password);
		return (M)this;
	}
	
	public String getPassword() {
		return getStr("password");
	}
	
	public M setUsername(String username) {
		set("username", username);
		return (M)this;
	}
	
	public String getUsername() {
		return getStr("username");
	}
	
}

