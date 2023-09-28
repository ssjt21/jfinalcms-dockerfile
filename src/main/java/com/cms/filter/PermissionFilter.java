package com.cms.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;

import com.alibaba.fastjson.JSONObject;
import com.cms.Feedback;
import com.cms.entity.Admin;
import com.cms.util.WebUtils;

public class PermissionFilter implements Filter{
    
    /** 不包含 */
    private List<String> adminExcludes = new ArrayList<String>(){{
        add("/admin/login");
        add("/admin/error");
        add("/admin/static");
    }};
    
    /** 不包含 */
    private List<String> permissionExcludes = new ArrayList<String>(){{
        add("/admin/logout");
        add("/admin/index");
        add("/admin/file");
        add("/admin/cache");
        add("/admin/profile");
    }};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String url = request.getRequestURI().toString();
        String contextPath = request.getContextPath();
        url = url.substring(contextPath.length());
        //匹配admin
        for(String key : adminExcludes){
            if(url.startsWith(key)){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        Admin currentAdmin = (Admin) request.getSession().getAttribute(Admin.SESSION_ADMIN);
        if(currentAdmin!=null){
        	//演示程序不允许修改、新增、删除开始
        	if("read".equals(currentAdmin.getUsername())){
        		String lowerUrl = url.toLowerCase();
        		if(lowerUrl.contains("save") 
        				|| lowerUrl.contains("update") 
        				|| lowerUrl.contains("delete")
        				|| lowerUrl.contains("backup")
        				|| lowerUrl.contains("restore")
                        || lowerUrl.contains("setDefault".toLowerCase())
                        || lowerUrl.contains("generate")
        				){
        			if(WebUtils.isAjaxRequest(request)){
        				//是ajax操作
        				response.setContentType("application/json;charset=UTF-8");  
        				PrintWriter writer = response.getWriter();  
        				writer.write(JSONObject.toJSONString(Feedback.error("演示账号不允许操作!")));
        				writer.flush();
        				writer.close();  
        				return;
        			}else{
        				//是url操作
        				response.setContentType("text/html;charset=utf-8");  
        				PrintWriter writer = response.getWriter();  
        				writer.write("<script>alert('演示账号不允许操作!');history.back();</script>");
        				writer.flush();
        				writer.close(); 
        				return;
        			}
        		}
        	}
        	//演示程序不允许修改、新增、删除结束
        	//系统内置角色
        	if(currentAdmin.getRole()!=null && BooleanUtils.isTrue(currentAdmin.getRole().getIsSystem())){
        		filterChain.doFilter(servletRequest, servletResponse);
        		return;
        	}
            for(String key : permissionExcludes){
                if(url.startsWith(key)){
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }
            List<String> permissions = currentAdmin.getPermissions();
            for(String key : permissions){
                if(url.startsWith(key)){
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }
            response.sendRedirect(contextPath+"/admin/error/unauthorized");
            return;
        }
    	response.sendRedirect(contextPath+"/admin/login");
    	return;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }

}
