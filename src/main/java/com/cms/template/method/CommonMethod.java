package com.cms.template.method;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.jfinal.kit.StrKit;

/**
 * 模板方法 - 公共方法
 * 
 * 
 * 
 */
public class CommonMethod {

    public static String abbreviate(String str,Integer width,String ellipsis){
        if (StrKit.isBlank(str) || width == null) {
            return str;
        }
        if(str.length()<=width){
            return str;
        }
        return StringUtils.substring(str, 0, width)+ellipsis;
    }
    
    /**
     * 交集
     * @param list1
     * @param list2
     * @return
     */
    public static Boolean intersection(List<String> list1,List<String> list2){
    	if(CollectionUtils.isNotEmpty(list1) && CollectionUtils.isNotEmpty(list2)){
    		Collection list = CollectionUtils.intersection(list1, list2);
    		if(CollectionUtils.isNotEmpty(list)){
    			return true;
    		}
    	}
    	return false;
    }
}
