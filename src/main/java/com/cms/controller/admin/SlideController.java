package com.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cms.Feedback;
import com.cms.entity.Slide;
import com.cms.routes.RouteMapping;


/**
 * Controller - 幻灯片
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/slide")
public class SlideController extends BaseController {
	
	/**
	 * 列表
	 */
	public void index(){
		setListQuery();
		Integer pageNumber = getParaToInt("pageNumber");
		if(pageNumber==null){
			pageNumber = 1;
		}
		setAttr("page", new Slide().dao().findPage(pageNumber,PAGE_SIZE,getCurrentSite().getId()));
		setAttr("gids", new Slide().dao().findGids(getCurrentSite().getId()));
		render(getView("slide/index"));
	}


	/**
	 * 添加
	 */
	public void add() {
		setAttr("gids", new Slide().dao().findGids(getCurrentSite().getId()));
		render(getView("slide/add"));
	}

	/**
	 * 保存
	 */
	public void save() {
		Slide slide = getModel(Slide.class,"",true); 
		if(slide.getGid()==null){
			Integer maxGid = new Slide().dao().findMaxGid(getCurrentSite().getId());
			if(maxGid==null){
				maxGid=0;
			}
			maxGid = maxGid+1;
			slide.setGid(maxGid);
		}
		slide.setSiteId(getCurrentSite().getId());
		slide.setCreateDate(new Date());
		slide.setUpdateDate(new Date());
		slide.save();
		redirect(getListQuery("/admin/slide"));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Integer id = getParaToInt("id");
		setAttr("slide", new Slide().dao().findById(id));
		setAttr("gids", new Slide().dao().findGids(getCurrentSite().getId()));
		render(getView("slide/edit"));
	}

	/**
	 * 修改
	 */
	public void update() {
		Slide slide = getModel(Slide.class,"",true); 
		slide.setUpdateDate(new Date());
		slide.update();
		redirect(getListQuery("/admin/slide"));
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
	        Slide slide = new Slide().dao().findById(id);
	        slide.setSort(sort);
	        slide.update();
	    }
	    renderJson(Feedback.success(new HashMap<>()));
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long ids[] = getParaValuesToLong("ids");
		if(ArrayUtils.isNotEmpty(ids)){
			for(Long id:ids){
				new Slide().dao().deleteById(id);
			}
		}
		renderJson(Feedback.success(new HashMap<>()));
	}

}