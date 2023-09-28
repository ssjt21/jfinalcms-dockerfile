/*
 * 
 * 
 * 
 */
package com.cms.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cms.Feedback;
import com.cms.entity.Nav;
import com.cms.routes.RouteMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Controller - 导航
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/nav")

public class NavController extends BaseController {
	
	/**
	 * 列表
	 */
	public void index() {
		setListQuery();
		setAttr("navTree", new Nav().dao().findTree(getCurrentSite().getId()));
		render(getView("nav/index"));
	}

	/**
	 * 添加
	 */
	public void add() {
		setAttr("navTree", new Nav().dao().findTree(getCurrentSite().getId()));
		render(getView("nav/add"));
	}
	
	/**
	 * 保存
	 */
	public void save() {
		Nav nav = getModel(Nav.class,"",true);
		nav.setSiteId(getCurrentSite().getId());
		nav.setValue();
		nav.setCreateDate(new Date());
		nav.setUpdateDate(new Date());
		nav.save();
		redirect(getListQuery("/admin/nav"));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Integer id = getParaToInt("id");
		Nav nav = new Nav().dao().findById(id);
		setAttr("navTree", new Nav().dao().findTree(getCurrentSite().getId()));
		setAttr("nav", nav);
		render(getView("nav/edit"));
	}

	/**
	 * 更新
	 */
	public void update() {
		Nav nav = getModel(Nav.class,"",true);
		nav.setValue();
		nav.setUpdateDate(new Date());
		nav.update();
		redirect(getListQuery("/admin/nav"));
	}
	
   /**
     * 修改排序
     */
    public void updateSort(){
        String sortArray = getPara("sortArray");
        JSONArray jsonArray = JSONArray.parseArray(sortArray);
        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Integer id = jsonObject.getInteger("id");
            Integer sort = jsonObject.getInteger("sort");
            Nav nav = new Nav().dao().findById(id);
			nav.setSort(sort);
			nav.update();
        }
        renderJson(Feedback.success(new HashMap<>()));
    }

	/**
	 * 删除
	 */
	public void delete() {
		Integer id = getParaToInt("id");
		Nav nav = new Nav().dao().findById(id);
		if (nav == null) {
			renderJson(Feedback.error("导航不存在"));
			return;
		}
		List<Nav> children = nav.getChildren();
		if (children != null && !children.isEmpty()) {
			renderJson(Feedback.error("存在下级导航，无法删除"));
			return;
		}
		new Nav().dao().deleteById(id);
		renderJson(Feedback.success(new HashMap<>()));
	}

}