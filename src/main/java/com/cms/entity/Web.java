package com.cms.entity;

import com.cms.entity.base.BaseWeb;

/**
 * Entity - 网站信息
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Web extends BaseWeb<Web> {
	
    /** 前台网站信息 */
    public static final String CURRENT_WEB="currentWeb";
	
    /**
     * 根据站点ID查询网站信息
     * 
     */
    public Web findBySiteId(Integer siteId){
        return findFirst("select * from cms_web where siteId=?",siteId);
    }
}
