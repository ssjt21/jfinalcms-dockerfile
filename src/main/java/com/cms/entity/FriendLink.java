package com.cms.entity;

import java.util.List;

import com.cms.entity.base.BaseFriendLink;
import org.apache.commons.lang.StringUtils;

import com.cms.util.DbUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * Entity - 友情链接
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class FriendLink extends BaseFriendLink<FriendLink> {
    
    
	/**
	 * 查找友情链接分页
	 * 
	 * @param pageNumber
	 *            页码
	 * @param pageSize
	 *            每页记录数
	 * @param siteId
	 *            站点ID
	 * @return 友情链接分页
	 */
	public Page<FriendLink> findPage(Integer pageNumber,Integer pageSize,Integer siteId){
	    String filterSql = " and siteId="+siteId;
	    String orderBySql = DbUtils.getOrderBySql("sort desc,createDate desc");
		return paginate(pageNumber, pageSize, "select *", "from cms_friend_link where 1=1 "+filterSql+orderBySql);
	}
	
	/**
	 * 查找分组ID列表
	 * 
	 * @param siteId
	 *            站点ID
	 * @return 分组ID列表
	 */
	public List<String> findGids(Integer siteId){
		String filterSql = " and siteId="+siteId;
		return Db.query("select gid from cms_friend_link where 1=1 "+filterSql+" group by gid order by gid asc");
	}
	
	/**
	 * 查找最大的分组ID
	 * 
	 * @param siteId
	 * 			站点ID
	 * @return
	 */
	public Integer findMaxGid(Integer siteId){
		String filterSql = " and siteId="+siteId;
		return Db.queryInt("select max(gid) from cms_friend_link where 1=1 "+filterSql);
	}
	
	/**
	 * 查找友情链接
	 * 
	 * @param orderBy
	 *            排序
	 * @param start
     *            起始位置
     * @param count
     *            数量
     * @param siteId
	 *            站点ID
	 * @return 友情链接
	 */
	public List<FriendLink> findList(Integer gid,String orderBy,Integer start,Integer count,Integer siteId) {
	    String filterSql = " and siteId="+siteId;
	    if(gid != null){
	    	filterSql += " and gid="+gid;
	    }
		String orderBySql = "";
		if(StringUtils.isBlank(orderBy)){
		    orderBySql = DbUtils.getOrderBySql("sort desc,createDate desc");
		}else{
		    orderBySql = DbUtils.getOrderBySql(orderBy+",sort desc");
		}
		String countSql=DbUtils.getCountSql(start, count);
		return find("select * from cms_friend_link where 1=1 "+filterSql+orderBySql+countSql);
	}
}
