package com.cms.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cms.entity.base.BaseSite;
import com.jfinal.core.JFinal;

import com.cms.util.DbUtils;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;

/**
 * Entity -站点
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Site extends BaseSite<Site> {
    
    /** 前台站点 */
    public static final String CURRENT_SITE="currentSite";
    
    /** 后台站点 */
    public static final String ADMIN_SESSION_SITE="admin_session_site";



    /**
     * 类型
     */
    public enum Type{
        CAT("目录"),
        DOMAIN("域名");
        public String text;
        Type(String text){
            this.text = text;
        }
        public String getText(){
            return this.text;
        }
        public static Map<Integer, Type> typeValueMap = new HashMap<>();
        static {
            Type[] values = Type.values();
            for (Type type : values) {
                typeValueMap.put(type.ordinal(), type);
            }
        }
    }

	
    /**
     * 查找站点分页
     * 
     * @param pageNumber
     *            页码
     * @param pageSize
     *            每页记录数
     * @return 站点分页
     */
    public Page<Site> findPage(Integer pageNumber,Integer pageSize){
        String filterSql = "";
        String orderBySql = DbUtils.getOrderBySql("createDate desc");
        return paginate(pageNumber, pageSize, "select *", "from cms_site where 1=1 "+filterSql+orderBySql);
    }
    
    /**
     * 查找默认站点
     * 
     */
    public Site findDefault(){
        return findFirst("select * from cms_site where isDefault=? ", true);
    }
    
    /**
     * 查找所有站点
     * 
     */
    public List<Site> findAll(){
    	String orderBySql = DbUtils.getOrderBySql("createDate desc");
        return find("select * from cms_site"+orderBySql);
    }

    
    /**
     * 获取地址
     */
    public String getUrl(){
        String contextPath = JFinal.me().getContextPath();
        if(Type.CAT.ordinal()==getType()){
            String cat = getCat(); //目录
            if(StringUtils.isBlank(cat)){
                return contextPath;
            }
            return contextPath+"/"+cat;
        }else if(Type.DOMAIN.ordinal()==getType()){
            String protocol = getProtocol(); //协议
            if(StringUtils.isBlank(protocol)){
                protocol = "";
            }
            String domain = getDomain(); //域名
            if(StringUtils.isBlank(domain)){
                domain = "";
            }
            return protocol+domain+contextPath;
        }
        return contextPath;
    }
}
