package com.cms.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseMenu<M extends BaseMenu<M>> extends Model<M> implements IBean {

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
	
	public M setSort(Integer sort) {
		set("sort", sort);
		return (M)this;
	}
	
	public Integer getSort() {
		return getInt("sort");
	}
	
	public M setGrade(Integer grade) {
		set("grade", grade);
		return (M)this;
	}
	
	public Integer getGrade() {
		return getInt("grade");
	}
	
	public M setTreePath(String treePath) {
		set("treePath", treePath);
		return (M)this;
	}
	
	public String getTreePath() {
		return getStr("treePath");
	}
	
	public M setCode(String code) {
		set("code", code);
		return (M)this;
	}
	
	public String getCode() {
		return getStr("code");
	}
	
	public M setName(String name) {
		set("name", name);
		return (M)this;
	}
	
	public String getName() {
		return getStr("name");
	}
	
	public M setType(Integer type) {
		set("type", type);
		return (M)this;
	}
	
	public Integer getType() {
		return getInt("type");
	}
	
	public M setParentId(Long parentId) {
		set("parentId", parentId);
		return (M)this;
	}
	
	public Long getParentId() {
		return getLong("parentId");
	}
	
	public M setUrl(String url) {
		set("url", url);
		return (M)this;
	}
	
	public String getUrl() {
		return getStr("url");
	}
	
	public M setIcon(String icon) {
		set("icon", icon);
		return (M)this;
	}
	
	public String getIcon() {
		return getStr("icon");
	}
	
	public M setIsShow(Boolean isShow) {
		set("isShow", isShow);
		return (M)this;
	}
	
	public Boolean getIsShow() {
		return get("isShow");
	}
	
}
