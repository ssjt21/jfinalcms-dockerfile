package com.cms.entity;

import com.cms.entity.base.BaseGuestbook;
import com.cms.util.DbUtils;
import com.jfinal.plugin.activerecord.Page;

/**
 * Entity - 留言
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Guestbook extends BaseGuestbook<Guestbook> {
	
	/**
	 * 查找留言分页
	 * 
	 * @param pageNumber
	 *            页码
	 * @param pageSize
	 *            每页记录数
	 * @param siteId
	 *            站点ID
	 * @return 留言分页
	 */
	public Page<Guestbook> findPage(Integer pageNumber,Integer pageSize,Integer siteId){
	    String filterSql = " and siteId="+siteId;
	    String orderBySql = DbUtils.getOrderBySql("createDate desc");
		return paginate(pageNumber, pageSize, "select *", "from cms_guestbook where 1=1 "+filterSql+orderBySql);
	}
}
