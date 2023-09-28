package com.cms;
/**
 * 
 * 数据库表属性
 *
 */
public class TableField {

	/** 名称 */
	private String name;

	/** 名称 */
	private String type;
	
	/** 描述 */
	private String comment;
	
	/** 是否必填 */
	private Boolean isRequired;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}
}
