package com.cms.entity;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.cms.entity.base.BaseRole;
import com.cms.util.DbUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * Entity - 角色
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Role extends BaseRole<Role> {
	
	
    /**
     * 获取权限
     * 
     * @return 权限
     */
    public List<String> getPermissions() {
        return JSONArray.parseArray(getPermission(), String.class);
    }
	
	/**
	 * 获取站点ID
	 * @return
	 */
	public List<Integer> getSiteIds(){
		String orderBySql = DbUtils.getOrderBySql("id desc");
		List<Integer> siteIds = Db.query("select siteId from cms_role_site where roleId=?"+orderBySql,getId());
		return siteIds;
	}
	
	
	
    /**
     * 查找角色分页
     * 
     * @param pageNumber
     *            页码
     * @param pageSize
     *            每页记录数
     * @return 角色分页
     */
    public Page<Role> findPage(Integer pageNumber,Integer pageSize){
        String filterSql = "";
        String orderBySql = DbUtils.getOrderBySql("createDate desc");
        return paginate(pageNumber, pageSize, "select *", "from cms_role where 1=1 "+filterSql+orderBySql);
    }
}
