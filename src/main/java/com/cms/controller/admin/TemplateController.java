/*
 * 
 * 
 * 
 */
package com.cms.controller.admin;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.cms.CommonAttribute;
import com.cms.routes.RouteMapping;
import com.cms.util.TemplateUtils;
import com.jfinal.kit.PathKit;

/**
 * Controller - 模板
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/template")

public class TemplateController extends BaseController {
	
	/**
	 * 列表
	 */
	public void index() {
		setListQuery();
		String directory = getPara("directory");
		if(StringUtils.isNotBlank(directory)){
			setAttr("templateFiles", TemplateUtils.getTemplateFiles(directory.replaceAll(",", "/")));
			setAttr("directory", directory);
		}else{
			setAttr("templateFiles", TemplateUtils.getSiteTemplates(getCurrentSite()));
		}
		render(getView("template/index"));
	}
	
	/**
	 * 添加
	 */
	public void add() {
		String directory = getPara("directory");
		setAttr("directory", directory);
		render(getView("template/add"));
	}

	/**
	 * 保存
	 */
	public void save() {
		String fileName = getPara("fileName");
		String content = getPara("content");
		String directory = getPara("directory");
		String filePath = "";
		if(StringUtils.isNotBlank(directory)){
			filePath = "/"+directory.replaceAll(",", "/")+"/"+fileName;
		}else{
			filePath = "/"+fileName;
		}
		try {
			FileUtils.write(new File(PathKit.getWebRootPath()+"/templates/"+filePath), content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		redirect(getListQuery("/admin/template"));
	}

	/**
	 * 查看
	 */
	public void view() {
		String fileName = getPara("fileName");
		String directory = getPara("directory");
		if (StringUtils.isBlank(fileName)) {
			render(CommonAttribute.ADMIN_ERROR_VIEW);
			return;
		}
		String filePath = "";
		if(StringUtils.isNotBlank(directory)){
			filePath = "/"+directory.replaceAll(",", "/")+"/"+fileName;
		}else{
			filePath = "/"+fileName;
		}
		redirect("/templates/"+filePath);
	}
	
	/**
	 * 编辑
	 */
	public void edit() {
		String fileName = getPara("fileName");
		String directory = getPara("directory");
		if (StringUtils.isBlank(fileName)) {
			render(CommonAttribute.ADMIN_ERROR_VIEW);
			return;
		}
		setAttr("directory", directory);
		setAttr("fileName", fileName);
		String filePath = "";
		if(StringUtils.isNotBlank(directory)){
			filePath = "/"+directory.replaceAll(",", "/")+"/"+fileName;
		}else{
			filePath = "/"+fileName;
		}
		setAttr("content", StringEscapeUtils.escapeHtml(TemplateUtils.read(filePath)));
		render(getView("template/edit"));
	}

	/**
	 * 更新
	 */
	public void update() {
		String fileName = getPara("fileName");
		String directory = getPara("directory");
		String content = getPara("content");
		if (StringUtils.isBlank(fileName) || content == null) {
			render(CommonAttribute.ADMIN_ERROR_VIEW);
			return;
		}
		String filePath = "";
		if(StringUtils.isNotBlank(directory)){
			filePath = "/"+directory.replaceAll(",", "/")+"/"+fileName;
		}else{
			filePath = "/"+fileName;
		}
		TemplateUtils.write(filePath, content);
		redirect(getListQuery("/admin/template"));
	}

	/**
	 * 删除
	 */
	public void delete() {
		String fileName = getPara("fileName");
		String directory = getPara("directory");
		String filePath = "";
		if(StringUtils.isNotBlank(directory)){
			filePath = "/"+directory.replaceAll(",", "/")+"/"+fileName;
		}else{
			filePath = "/"+fileName;
		}
		TemplateUtils.delete(filePath);
		redirect(getListQuery("/admin/template"));
	}

}