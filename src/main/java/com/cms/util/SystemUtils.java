/*
 * 
 * 
 * 
 */
package com.cms.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.cms.Config;
import com.cms.entity.Setup;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * Utils - 系统
 * 
 * 
 * 
 */
public final class SystemUtils {

	/**
	 * 不可实例化
	 */
	private SystemUtils() {
	}

	/**
	 * 获取系统设置
	 * 
	 * @return 系统设置
	 */
	public static Config getConfig() {
		String cacheKey = "config";
		Config config = CacheKit.get(Config.CACHE_NAME,cacheKey);
		if (config == null) {
			Map<String,String> setupMap = new Setup().dao().getSetupMap();
			config = new Config();
			for(String key : setupMap.keySet()){
				try {
					BeanUtils.setProperty(config, key, setupMap.get(key));
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			CacheKit.put(Config.CACHE_NAME,cacheKey,config);
		}
		return config;
	}
}