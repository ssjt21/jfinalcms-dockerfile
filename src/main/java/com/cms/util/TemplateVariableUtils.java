package com.cms.util;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.cms.ClassScaner;
import com.cms.TemplateVariable;
import com.cms.template.directive.BaseDirective;
import com.cms.template.method.CommonMethod;
import com.jfinal.kit.StrKit;
import com.jfinal.render.RenderManager;

/**
 * Utils - 模板变量
 * 
 * 
 * 
 */
public class TemplateVariableUtils {
	
	/**
	 * 设置基础变量
	 */
	public static void setBaseVariable(){
	}

	/**
	 * 设置标签变量
	 */
	public static void setDirectiveVariable(){
	    /** 自定义标签 */
        List<Class<BaseDirective>> directiveList = ClassScaner.scanSubClass(BaseDirective.class,true,false);
        if (CollectionUtils.isNotEmpty(directiveList)) {
            for (Class<BaseDirective> clazz : directiveList) {
                TemplateVariable templateVariable = clazz.getAnnotation(TemplateVariable.class);
                if (null != templateVariable && StrKit.notBlank(templateVariable.name())) {
                    RenderManager.me().getEngine().addDirective(templateVariable.name(),clazz);
                }
            }
        }
	}
	
	/**
	 * 设置方法变量
	 */
	public static void setMethodVariable(){
	    /** 自定义方法 */
	    RenderManager.me().getEngine().addSharedStaticMethod(CommonMethod.class);
	}
}
