package com.cms.entity;

import java.util.List;

import com.cms.entity.base.BaseDiv;
import com.cms.util.DbUtils;
import com.jfinal.plugin.activerecord.Page;

/**
 * Entity - 自定义表
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Div extends BaseDiv<Div> {
	
    /**
     * 查找所有自定义表
     * 
     */
    public List<Div> findAll(){
        return find("select * from cms_div");
    }
	
	/**
	 * 查找自定义表分页
	 * 
	 * @param pageNumber
	 *            页码
	 * @param pageSize
	 *            每页记录数
	 * @return 表单分页
	 */
	public Page<Div> findPage(Integer pageNumber, Integer pageSize){
	    String filterSql = "";
		String orderBySql = DbUtils.getOrderBySql("createDate desc");
		return paginate(pageNumber, pageSize, "select *", "from cms_div where 1=1 "+filterSql+orderBySql);
	}
}
