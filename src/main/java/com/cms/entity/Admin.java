package com.cms.entity;

import java.util.List;

import com.cms.entity.base.BaseAdmin;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import com.cms.util.DbUtils;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * Entity - 管理员
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Admin extends BaseAdmin<Admin> {
	
	/** 管理员session名称 */
	public static final String SESSION_ADMIN="session_admin";
	
	/** 角色 */
	@JSONField(serialize=false)  
	private Role role;
	
    /**
     * 获取权限
     * 
     * @return 权限
     */
    public List<String> getPermissions() {
        return JSONArray.parseArray(getRole().getPermission(), String.class);
    }
	
    /**
     * 获取角色
     * 
     * @return 角色
     */
    public Role getRole(){
    	if(role == null){
    		role = new Role().dao().findFirst("select * from cms_role where id in (select roleId from cms_admin_role where adminId=?)",getId());
    	}
    	return role;
    }
    
	/**
	 * 获取角色ID
	 * @return
	 */
	public List<Integer> getRoleIds(){
		List<Integer> roleIds = Db.query("select roleId from cms_admin_role where adminId=?",getId());
		return roleIds;
	}
	
	/**
	 * 判断用户名是否存在
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	public boolean usernameExists(String username) {
		if (StrKit.isBlank(username)) {
			return false;
		}
		Long count = Db.queryLong("select count(1) from cms_admin where username = ?",username);
		return count > 0;
	}

	/**
	 * 根据用户名查找管理员
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 管理员，若不存在则返回null
	 */
	public Admin findByUsername(String username) {
		if (StrKit.isBlank(username)) {
			return null;
		}
		return findFirst("select * from cms_admin where username = ?",username);
	}
	
	/**
	 * 查找管理员分页
	 * 
	 * @param pageNumber
	 *            页码
	 * @param pageSize
	 *            每页记录数
	 * @return 管理员分页
	 */
	public Page<Admin> findPage(String name,String username,Integer pageNumber,Integer pageSize){
	    String filterSql = "";
        if(StringUtils.isNotBlank(name)){
            filterSql+= " and name like '%"+name+"%'";
        }
        if(StringUtils.isNotBlank(username)){
            filterSql+= " and username like '%"+username+"%'";
        }
	    String orderBySql = DbUtils.getOrderBySql("createDate desc");
		return paginate(pageNumber, pageSize, "select *", "from cms_admin where 1=1 "+filterSql+orderBySql);
	}
}
