package com.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;

import com.cms.Feedback;
import com.cms.entity.Div;
import com.cms.routes.RouteMapping;
import com.cms.util.TableUtils;


/**
 * Controller - 自定义表
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/div")
public class DivController extends BaseController {
	
	/**
     * 列表
     */
    public void index(){
    	setListQuery();
    	Integer pageNumber = getParaToInt("pageNumber");
		if(pageNumber==null){
			pageNumber = 1;
		}
    	setAttr("page", new Div().dao().findPage(pageNumber,PAGE_SIZE));
        render(getView("div/index"));
    }
    
	/**
	 * 添加
	 */
	public void add() {
		render(getView("div/add"));
	}
	
	/**
	 * 保存
	 */
	public void save() {
		Div div =  getModel(Div.class,"",true);
		div.setCreateDate(new Date());
		div.setUpdateDate(new Date());
		div.save();
		//创建数据库表
		TableUtils.create(div.getTableName());
		redirect(getListQuery("/admin/div"));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Integer id = getParaToInt("id");
		Div div = new Div().dao().findById(id);
		setAttr("div", div);
		render(getView("div/edit"));
	}

	/**
	 * 修改
	 */
	public void update() {
		Div div =  getModel(Div.class,"",true);
		div.setUpdateDate(new Date());
		div.update();
		redirect(getListQuery("/admin/div"));
	}

	/**
	 * 删除
	 */
	public void delete() {
		Integer ids[] = getParaValuesToInt("ids");
		if(ArrayUtils.isNotEmpty(ids)){
			for(Integer id:ids){
				Div div = new Div().dao().findById(id);
				TableUtils.delete(div.getTableName());
				div.delete();
			}
		}
		renderJson(Feedback.success(new HashMap<>()));
	}

}