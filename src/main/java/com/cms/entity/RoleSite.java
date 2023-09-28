package com.cms.entity;

import com.cms.entity.base.BaseRoleSite;
import com.jfinal.plugin.activerecord.Db;

/**
 * Entity - 角色站点
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class RoleSite extends BaseRoleSite<RoleSite> {
	
	/**
	 * 根据角色ID删除角色站点
	 * 
	 * @param roleId
	 */
	public void deleteByRoleId(Integer roleId){
		Db.update("delete from cms_role_site where roleId=?",roleId);
	}
}
