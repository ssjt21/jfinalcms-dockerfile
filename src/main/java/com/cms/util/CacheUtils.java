package com.cms.util;

import com.cms.Config;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.ehcache.CacheKit;
/**
 * Utils - 缓存
 * 
 * 
 * 
 */
public class CacheUtils {

	/**
	 * 清除所有缓存
	 */
	public static void clearAll() {
	    CacheKit.getCacheManager().clearAll();
		PropKit.clear();
	}
	
	/**
	 * 清除config缓存
	 */
	public static void clearConfig(){
		CacheKit.removeAll(Config.CACHE_NAME);
	}
}
