package com.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cms.Feedback;
import com.cms.entity.Menu;
import com.cms.routes.RouteMapping;

/**
 * Controller - 菜单
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/menu")
public class MenuController extends BaseController{
	
	/**
	 * 列表
	 */
	public void index() {
		setListQuery();
		setAttr("menuTree", new Menu().dao().findTree());
		render(getView("menu/index"));
	}
	
	
	/**
	 * 添加
	 */
	public void add() {
		setAttr("menuTree", new Menu().dao().findTree());
		render(getView("menu/add"));
	}
	
    
	/**
	 * 保存
	 */
	public void save() {
		Menu menu = getModel(Menu.class,"",true); 
		if(menu.getIsShow()==null){
			menu.setIsShow(false);
        }
		menu.setValue();
		menu.setCreateDate(new Date());
		menu.setUpdateDate(new Date());
		menu.save();
		redirect(getListQuery("/admin/menu"));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		Menu menu = new Menu().dao().findById(id);
		setAttr("menuTree", new Menu().dao().findTree());
		setAttr("menu", menu);
		render(getView("menu/edit"));
	}

	/**
	 * 更新
	 */
	public void update() {
		Menu menu = getModel(Menu.class,"",true); 
		if(menu.getIsShow()==null){
			menu.setIsShow(false);
        }
		menu.setValue();
		menu.setUpdateDate(new Date());
		menu.update();
		redirect(getListQuery("/admin/menu"));
	}
	
	/**
	 * 修改是否显示
	 */
	public void updateIsShow(){
		Boolean isShow = getParaToBoolean("value");
		Integer id = getParaToInt("id");
		Menu menu = new Menu().dao().findById(id);
		menu.setIsShow(isShow);
		menu.setUpdateDate(new Date());
		menu.update();
		renderJson(Feedback.success(new HashMap<>()));
	}
	
   /**
     * 修改排序
     */
    public void updateSort(){
        String sortArray = getPara("sortArray");
        JSONArray jsonArray = JSONArray.parseArray(sortArray);
        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Long id = jsonObject.getLong("id");
            Integer sort = jsonObject.getInteger("sort");
            Menu menu = new Menu().dao().findById(id);
            menu.setSort(sort);
            menu.update();
        }
        renderJson(Feedback.success(new HashMap<>()));
    }

	/**
	 * 删除
	 */
	public void delete() {
		Long id = getParaToLong("id");
		Menu menu = new Menu().dao().findById(id);
		if (menu == null) {
			renderJson(Feedback.error("菜单不存在"));
			return;
		}
		List<Menu> children = menu.getChildren();
		if (children != null && !children.isEmpty()) {
			renderJson(Feedback.error("存在下级菜单，无法删除"));
			return;
		}
		new Menu().dao().deleteById(id);
		renderJson(Feedback.success(new HashMap<>()));
	}

}
