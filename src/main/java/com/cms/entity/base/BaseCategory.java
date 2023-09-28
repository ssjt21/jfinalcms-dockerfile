package com.cms.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseCategory<M extends BaseCategory<M>> extends Model<M> implements IBean {

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
	
	public M setStatus(Integer status) {
		set("status", status);
		return (M)this;
	}
	
	public Integer getStatus() {
		return getInt("status");
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
	
	public M setCat(String cat) {
		set("cat", cat);
		return (M)this;
	}
	
	public String getCat() {
		return getStr("cat");
	}
	
	public M setName(String name) {
		set("name", name);
		return (M)this;
	}
	
	public String getName() {
		return getStr("name");
	}
	
	public M setSubname(String subname) {
		set("subname", subname);
		return (M)this;
	}
	
	public String getSubname() {
		return getStr("subname");
	}
	
	public M setIco(String ico) {
		set("ico", ico);
		return (M)this;
	}
	
	public String getIco() {
		return getStr("ico");
	}
	
	public M setImage(String image) {
		set("image", image);
		return (M)this;
	}
	
	public String getImage() {
		return getStr("image");
	}
	
	public M setIntroduction(String introduction) {
		set("introduction", introduction);
		return (M)this;
	}
	
	public String getIntroduction() {
		return getStr("introduction");
	}
	
	public M setDescription(String description) {
		set("description", description);
		return (M)this;
	}
	
	public String getDescription() {
		return getStr("description");
	}
	
	public M setKeywords(String keywords) {
		set("keywords", keywords);
		return (M)this;
	}
	
	public String getKeywords() {
		return getStr("keywords");
	}
	
	public M setTitle(String title) {
		set("title", title);
		return (M)this;
	}
	
	public String getTitle() {
		return getStr("title");
	}
	
	public M setTreePath(String treePath) {
		set("treePath", treePath);
		return (M)this;
	}
	
	public String getTreePath() {
		return getStr("treePath");
	}
	
	public M setParentId(Integer parentId) {
		set("parentId", parentId);
		return (M)this;
	}
	
	public Integer getParentId() {
		return getInt("parentId");
	}
	
	public M setModelId(Integer modelId) {
		set("modelId", modelId);
		return (M)this;
	}
	
	public Integer getModelId() {
		return getInt("modelId");
	}
	
	public M setListTemplate(String listTemplate) {
		set("listTemplate", listTemplate);
		return (M)this;
	}
	
	public String getListTemplate() {
		return getStr("listTemplate");
	}
	
	public M setDetailTemplate(String detailTemplate) {
		set("detailTemplate", detailTemplate);
		return (M)this;
	}
	
	public String getDetailTemplate() {
		return getStr("detailTemplate");
	}
	
	public M setOutlink(String outlink) {
		set("outlink", outlink);
		return (M)this;
	}
	
	public String getOutlink() {
		return getStr("outlink");
	}
	
	public M setChunkValue(String chunkValue) {
		set("chunkValue", chunkValue);
		return (M)this;
	}
	
	public String getChunkValue() {
		return getStr("chunkValue");
	}
	
	public M setRemark(String remark) {
		set("remark", remark);
		return (M)this;
	}
	
	public String getRemark() {
		return getStr("remark");
	}
	
	public M setSiteId(Integer siteId) {
		set("siteId", siteId);
		return (M)this;
	}
	
	public Integer getSiteId() {
		return getInt("siteId");
	}
	
}
