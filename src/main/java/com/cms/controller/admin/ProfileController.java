package com.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.cms.Feedback;
import com.cms.entity.Admin;
import com.cms.routes.RouteMapping;


/**
 * Controller - 个人资料
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/profile")
public class ProfileController extends BaseController {


	/**
	 * 编辑
	 */
	public void edit() {
		setAttr("admin", new Admin().dao().findById(getCurrentAdmin().getId()));
		render(getView("profile/edit"));
	}

	/**
	 * 更新
	 */
	public void update() {
		String currentPassword = getPara("currentPassword");
		String password = getPara("password");
		Admin admin = getCurrentAdmin();
		Admin pAdmin = new  Admin().dao().findById(admin.getId());
		if (StringUtils.isEmpty(currentPassword) || !StringUtils.equals(DigestUtils.md5Hex(currentPassword), pAdmin.getPassword())) {
			renderJson(Feedback.error("密码错误"));
			return;
		}
		pAdmin.setPassword(DigestUtils.md5Hex(password));
		pAdmin.setUpdateDate(new Date());
		pAdmin.update();
		renderJson(Feedback.success(new HashMap<>()));
	}

}