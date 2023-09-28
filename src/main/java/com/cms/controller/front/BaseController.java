package com.cms.controller.front;

import java.math.BigDecimal;

import com.cms.util.DeviceUtils;
import org.apache.commons.lang.StringUtils;

import com.cms.entity.Site;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;

/**
 * Controller - 基类
 * 
 * 
 * 
 */
public class BaseController extends Controller{
    
    /**
     * 获取当前站点
     * 
     * @return 当前站点
     */
    @NotAction
    protected Site getCurrentSite() {
    	Site currentSite = getAttr(Site.CURRENT_SITE);
        return currentSite;
    }

	/**
	 * 获取当前模板
	 *
	 * @return 当前模板
	 */
	@NotAction
	protected String getCurrentTemplate() {
		Site currentSite = getCurrentSite();
		if(DeviceUtils.isMobile(getRequest())){
			if(StringUtils.isNotBlank(currentSite.getMobileTemplate())){
				return currentSite.getMobileTemplate();
			}
		}
		return currentSite.getPcTemplate();
	}
    
	/**
	 * 获取BigDecimal数据
	 * 
	 * @param name
	 * 			名称
	 * @return BigDecimal数据
	 */
    @NotAction
	public BigDecimal getParaToBigDecimal(String name){
		String value = getPara(name);
		if(StringUtils.isNotBlank(value)){
			return new BigDecimal(value);
		}
		return null;
	}

}