/*
 * 
 * 
 * 
 */
package com.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;

import com.cms.Feedback;
import com.cms.entity.Model;
import com.cms.entity.ModelField;
import com.cms.routes.RouteMapping;


/**
 * Controller - 模型
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/model")

public class ModelController extends BaseController {
	
	/**
	 * 列表
	 */
	public void index() {
		setListQuery();
	    String name = getPara("name");
		Integer pageNumber = getParaToInt("pageNumber");
		if(pageNumber==null){
			pageNumber = 1;
		}
		setAttr("page", new Model().dao().findPage(pageNumber,PAGE_SIZE));
		setAttr("name", name);
		render(getView("model/index"));
	}

	/**
	 * 添加
	 */
	public void add() {
	    render(getView("model/add"));
	}

	/**
	 * 保存
	 */
	public void save() {
		Model model = getModel(Model.class,"",true);
		model.setCreateDate(new Date());
		model.setUpdateDate(new Date());
		model.save();
		redirect(getListQuery("/admin/model"));
	}
	
	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("model", new Model().dao().findById(id));
		render(getView("model/edit"));
	}

	/**
	 * 更新
	 */
	public void update() {
		Model model = getModel(Model.class,"",true); 
		model.setUpdateDate(new Date());
		model.update();
		redirect(getListQuery("/admin/model"));
	}

	/**
	 * 删除
	 */
	public void delete() {
		Integer ids[] = getParaValuesToInt("ids");
		if(ArrayUtils.isNotEmpty(ids)){
			for(Integer id:ids){
			    new ModelField().dao().deleteByModelId(id);
				Model model = new Model().dao().findById(id);
				model.delete();
			}
		}
		renderJson(Feedback.success(new HashMap<>()));
	}

}