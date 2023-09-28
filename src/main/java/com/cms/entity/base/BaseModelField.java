package com.cms.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseModelField<M extends BaseModelField<M>> extends Model<M> implements IBean {

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
	
	public M setSort(Integer sort) {
		set("sort", sort);
		return (M)this;
	}
	
	public Integer getSort() {
		return getInt("sort");
	}
	
	public M setType(String type) {
		set("type", type);
		return (M)this;
	}
	
	public String getType() {
		return getStr("type");
	}
	
	public M setAlias(String alias) {
		set("alias", alias);
		return (M)this;
	}
	
	public String getAlias() {
		return getStr("alias");
	}
	
	public M setValue(String value) {
		set("value", value);
		return (M)this;
	}
	
	public String getValue() {
		return getStr("value");
	}
	
	public M setModelId(Long modelId) {
		set("modelId", modelId);
		return (M)this;
	}
	
	public Long getModelId() {
		return getLong("modelId");
	}
	
}
