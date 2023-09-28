package com.cms.entity;

import com.cms.entity.base.BaseCompany;

/**
 * Entity - 公司
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Company extends BaseCompany<Company> {
	
    /** 前台公司信息 */
    public static final String CURRENT_COMPANY="currentCompany";
	
    /**
     * 根据站点ID查询公司信息
     * 
     */
    public Company findBySiteId(Integer siteId){
        return findFirst("select * from cms_company where siteId=?",siteId);
    }
}
