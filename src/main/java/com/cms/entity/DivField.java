package com.cms.entity;

import java.util.List;

import com.cms.entity.base.BaseDivField;
import com.cms.util.DbUtils;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

/**
 * Entity - 自定义表字段
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class DivField extends BaseDivField<DivField> {
	
    /**
     * 自定义表
     */
    private Div div;
    
    
	/**
	 * 获取自定义表
	 * 
	 * @return 自定义表
	 */
	public Div getDiv(){
	    if(div == null){
			div = new Div().dao().findById(getDivId());
	    }
		return div;
	}
	
   /**
     * 判断名称是否存在
     * 
     * @param name
     *            名称
     * @return 名称是否存在
     */
    public boolean nameExists(Integer formId,String name) {
        if (StrKit.isBlank(name)) {
            return false;
        }
        Integer count = Db.queryInt("select count(1) from cms_div_field where divId = ? and name = ?",formId,name);
        return count > 0;
    }
	
	/**
	 * 查找自定义表字段
	 * 
	 * @param divId
	 * 				自定义表ID
	 * @return 自定义表字段
	 */
	public List<DivField> findList(Integer divId){
	    String filterSql = "";
		if(divId != null){
		    filterSql+= " and divId = "+divId;
		}
		String orderBySql = DbUtils.getOrderBySql("sort desc,createDate desc");
		return find("select * from cms_div_field where 1=1 "+filterSql+orderBySql);
	}
	
	/**
	 * 根据自定义表ID删除自定义表字段
	 * 
	 * @param divId
	 */
	public void deleteByFormId(Integer divId){
	    Db.update("delete from cms_div_field where divId = ?",divId);
	}
}
