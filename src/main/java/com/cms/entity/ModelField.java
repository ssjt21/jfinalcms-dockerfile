package com.cms.entity;

import java.util.List;

import com.cms.entity.base.BaseModelField;
import com.cms.util.DbUtils;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

/**
 * Entity - 模型字段
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class ModelField extends BaseModelField<ModelField> {
	
    /**
     * 模型
     */
    private Model model;
    
    
	/**
	 * 获取模型
	 * 
	 * @return 模型
	 */
	public Model getModel(){
	    if(model == null){
	        model = new Model().dao().findById(getModelId());
	    }
		return model;
	}
	
   /**
     * 判断名称是否存在
     * 
     * @param name
     *            名称
     * @return 名称是否存在
     */
    public boolean nameExists(Integer modelId,String name) {
        if (StrKit.isBlank(name)) {
            return false;
        }
        Integer count = Db.queryInt("select count(1) from cms_model_field where modelId = ? and name = ?",modelId,name);
        return count > 0;
    }
	
	/**
	 * 查找模型字段
	 * 
	 * @param modelId
	 * 				模型ID
	 * @return 模型字段
	 */
	public List<ModelField> findList(Integer modelId){
	    String filterSql = "";
		if(modelId != null){
		    filterSql+= " and modelId = "+modelId;
		}
		String orderBySql = DbUtils.getOrderBySql("sort desc,createDate desc");
		return find("select * from cms_model_field where 1=1 "+filterSql+orderBySql);
	}
	
	/**
	 * 根据模型ID删除模型属性
	 * 
	 * @param modelId
	 */
	public void deleteByModelId(Integer modelId){
	    Db.update("delete from cms_model_field where modelId = ?",modelId);
	}
	
}
