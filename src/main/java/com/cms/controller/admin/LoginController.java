/*
 * 
 * 
 * 
 */
package com.cms.controller.admin;

import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;

import com.cms.Feedback;
import com.cms.entity.Admin;
import com.cms.routes.RouteMapping;

/**
 * Controller - 管理员登录
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/login")
public class LoginController extends BaseController {

    /**
     * 登录
     */
    public void index() {
    	String username = getPara("username");
        String password = getPara("password");
        setAttr("username", username);
        setAttr("password", password);
        render(getView("login/index"));
    }
    
    /**
     * 登录
     */
    public void login(){
    	String username = getPara("username");
        String password = getPara("password");
        Admin admin = new Admin().dao().findByUsername(username);
        if(admin==null){
            renderJson(Feedback.error("用户名不存在!"));
            return;
        }
    	if(!admin.getPassword().equals(DigestUtils.md5Hex(password))){
    		renderJson(Feedback.error("用户名密码错误!"));
    		return;
        }
        getSession().setAttribute(Admin.SESSION_ADMIN, admin);
        renderJson(Feedback.success(new HashMap<>()));
    }
	

}