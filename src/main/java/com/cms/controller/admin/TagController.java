package com.cms.controller.admin;

import com.cms.Feedback;
import com.cms.entity.Tag;
import com.cms.routes.RouteMapping;
import org.apache.commons.lang.ArrayUtils;

import java.util.Date;
import java.util.HashMap;


/**
 * Controller - 标签
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/tag")
public class TagController extends BaseController {
	
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
		setAttr("page", new Tag().dao().findPage(pageNumber,PAGE_SIZE,getCurrentSite().getId()));
		setAttr("name", name);
		render(getView("tag/index"));
	}


	/**
	 * 添加
	 */
	public void add() {
		render(getView("tag/add"));
	}

	/**
	 * 保存
	 */
	public void save() {
		Tag tag = getModel(Tag.class,"",true);
		tag.setSiteId(getCurrentSite().getId());
		tag.setCreateDate(new Date());
		tag.setUpdateDate(new Date());
		tag.save();
		redirect(getListQuery("/admin/tag"));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Integer id = getParaToInt("id");
		setAttr("tag", new Tag().dao().findById(id));
		render(getView("tag/edit"));
	}

	/**
	 * 更新
	 */
	public void update() {
		Tag tag = getModel(Tag.class,"",true);
		tag.setUpdateDate(new Date());
		tag.update();
		redirect(getListQuery("/admin/tag"));
	}
	
	/**
	 * 删除
	 */
	public void delete() {
		Integer ids[] = getParaValuesToInt("ids");
		if(ArrayUtils.isNotEmpty(ids)){
			for(Integer id:ids){
				new Tag().dao().deleteById(id);
			}
		}
		renderJson(Feedback.success(new HashMap<>()));
	}

}