package com.cms.entity;

import com.cms.entity.base.BaseSetup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entity - 配置
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Setup extends BaseSetup<Setup> {
	
	/**
     * 获取配置
     */
    public Map<String,String> getSetupMap(){
    	List<Setup> setups = findAll();
    	Map<String,String> setupMap = new HashMap<String, String>();
    	for(Setup setup : setups){
    		setupMap.put(setup.getName(), setup.getValue());
    	}
    	return setupMap;
    }
    
    /**
     * 查找全部配置
     */
    public List<Setup> findAll(){
    	return new Setup().dao().find("select * from cms_setup order by sort desc");
    }
    
    /**
     * 根据名称查找配置
     * 
     * @return 配置
     */
    public Setup findByName(String name){
    	return new Setup().dao().findFirst("select * from cms_setup where name = ?",name);
    }
}
