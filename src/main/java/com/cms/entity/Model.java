package com.cms.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cms.entity.base.BaseModel;
import com.cms.util.DbUtils;
import com.jfinal.plugin.activerecord.Page;

/**
 * Entity - 模型
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Model extends BaseModel<Model> {
    
	/**
	 * 状态
	 */
	public enum Status{
		DISABLE("禁用"),
		NORMAL("启用");
		public String text;
		Status(String text){
			this.text = text;
		}
		public String getText(){
			return this.text;
		}
		public static Map<Integer, Model.Status> statusValueMap = new HashMap<>();
		static {
			Model.Status[] values = Model.Status.values();
			for (Model.Status status : values) {
				statusValueMap.put(status.ordinal(), status);
			}
		}
	}

	/**
	 * 类型
	 */
	public enum Type{
		PAGE("单页"),
		LIST("列表");
		public String text;
		Type(String text){
			this.text = text;
		}
		public String getText(){
			return this.text;
		}
		public static Map<Integer, Model.Type> typeValueMap = new HashMap<>();
		static {
			Model.Type[] values = Model.Type.values();
			for (Model.Type type : values) {
				typeValueMap.put(type.ordinal(), type);
			}
		}
	}


	public String getTypeName(){
		return Model.Type.typeValueMap.get(getType()).getText();
	}
    
	
	/**
	 * 查找模型分页
	 * 
	 * @param pageNumber
	 *            页码
	 * @param pageSize
	 *            每页记录数
	 * @return 模型分页
	 */
	public Page<Model> findPage(Integer pageNumber,Integer pageSize){
		String filterSql = "";
	    String orderBySql = DbUtils.getOrderBySql("createDate desc");
		return paginate(pageNumber, pageSize, "select *", "from cms_model where 1=1 "+filterSql+orderBySql);
	}
	
	/**
	 * 查找启用模型列表
	 * 
	 * @return 启用模型列表
	 */
	public List<Model> findNormalList(){
	    String filterSql = " and status="+Status.NORMAL.ordinal();
	    String orderBySql = DbUtils.getOrderBySql("createDate desc");
		return find("select * from cms_model where 1=1 "+filterSql+orderBySql);
	}
}
