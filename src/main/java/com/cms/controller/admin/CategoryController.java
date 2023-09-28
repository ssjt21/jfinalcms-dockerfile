/*
 * 
 * 
 * 
 */
package com.cms.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cms.util.PinYinUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cms.Feedback;
import com.cms.entity.Category;
import com.cms.entity.Content;
import com.cms.entity.Model;
import com.cms.entity.ModelField;
import com.cms.routes.RouteMapping;
import org.apache.commons.lang.StringUtils;


/**
 * Controller - 分类
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/category")

public class CategoryController extends BaseController {
	
	/**
	 * 列表
	 */
	public void index() {
		setListQuery();
		setAttr("categoryTree", new Category().dao().findTree(null,getCurrentSite().getId()));
		render(getView("category/index"));
	}

	/**
	 * 添加
	 */
	public void add() {
		setAttr("categoryTree", new Category().dao().findTree(null,getCurrentSite().getId()));
		setAttr("models",new Model().dao().findNormalList());
		render(getView("category/add"));
	}

	/**
	 * 检查目录
	 */
	public void checkCat(){
		String cat = getPara("cat");
		if(StringUtils.isBlank(cat)){
			renderJson(true);
			return;
		}
		Integer id = getParaToInt("id");
		if(id!=null){
			Category category = new Category().dao().findById(id);
			if(category.getCat().equals(cat)){
				renderJson(true);
				return;
			}
		}
		Category category = new Category().findByCat(cat,getCurrentSite().getId());
		renderJson(category==null);
	}
	
   /**
     * 内容模型模板
     */
    public void modelTemplate(){
        Integer modelId = getParaToInt("modelId");
        Model model = new Model().dao().findById(modelId);
        Map<String,Object> data = new HashMap<>();
        data.put("listTemplate", model.getListTemplate());
        data.put("detailTemplate", model.getDetailTemplate());
        renderJson(data);
    }
    
	/**
	 * 保存
	 */
	public void save() {
		Category category = getModel(Category.class,"",true); 
		String[] chunkValues = getParaValues("chunkValues");
		Integer modelId = category.getModelId();
		List<ModelField> modelFields = new ModelField().dao().findList(modelId);
		Map<String,Object> model = new HashMap<String,Object>();
		for(ModelField modelField : modelFields){
            model.put(modelField.getName(), getPara(modelField.getName()));
		}
		if(ArrayUtils.isNotEmpty(chunkValues)){
			category.setChunkValue(JSONArray.toJSONString(chunkValues));
		}else{
			category.setChunkValue(JSONArray.toJSONString(new String[]{}));
		}
		if(StringUtils.isBlank(category.getCat())){
			category.setCat(PinYinUtils.convertLower(category.getName()));
		}
		category.setSiteId(getCurrentSite().getId());
		category.setValue();
		category.setCreateDate(new Date());
		category.setUpdateDate(new Date());
		category.save();
		redirect(getListQuery("/admin/category"));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Integer id = getParaToInt("id");
		Category category = new Category().dao().findById(id);
		setAttr("categoryTree", new Category().dao().findTree(null,getCurrentSite().getId()));
		setAttr("category", category);
		setAttr("models",new Model().dao().findNormalList());
		if(category.getModelId() != null){
            List<ModelField> modelFields = new ModelField().dao().findList(category.getModelId());
            setAttr("modelFields", modelFields);
        }else{
            setAttr("modelFields", new ArrayList<ModelField>());
        }
		render(getView("category/edit"));
	}

	/**
	 * 更新
	 */
	public void update() {
		Category category = getModel(Category.class,"",true);
		String[] chunkValues = getParaValues("chunkValues");
		Integer modelId = category.getModelId();
		List<ModelField> modelFields = new ModelField().dao().findList(modelId);
		Map<String,Object> model = new HashMap<String,Object>();
		for(ModelField modelField : modelFields){
            model.put(modelField.getName(), getPara(modelField.getName()));
		}
		if(ArrayUtils.isNotEmpty(chunkValues)){
			category.setChunkValue(JSONArray.toJSONString(chunkValues));
		}else{
			category.setChunkValue(JSONArray.toJSONString(new String[]{}));
		}
		if(StringUtils.isBlank(category.getCat())){
			category.setCat(PinYinUtils.convertLower(category.getName()));
		}
		category.setValue();
		category.setUpdateDate(new Date());
		category.update();
		redirect(getListQuery("/admin/category"));
	}
	
	/**
	 * 修改状态
	 */
	public void updateStatus(){
		Integer status = BooleanUtils.toInteger(getParaToBoolean("status"));
		Integer id = getParaToInt("id");
		Category category = new Category().dao().findById(id);
		category.setStatus(status);
		category.setUpdateDate(new Date());
		category.update();
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
            Integer id = jsonObject.getInteger("id");
            Integer sort = jsonObject.getInteger("sort");
            Category category = new Category().dao().findById(id);
            category.setSort(sort);
            category.update();
        }
        renderJson(Feedback.success(new HashMap<>()));
    }

	/**
	 * 删除
	 */
	public void delete() {
		Integer id = getParaToInt("id");
		Category category = new Category().dao().findById(id);
		if (category == null) {
			renderJson(Feedback.error("分类不存在"));
			return;
		}
		List<Category> children = category.getChildren();
		if (children != null && !children.isEmpty()) {
			renderJson(Feedback.error("存在下级分类，无法删除"));
			return;
		}
		List<Content> contents = category.getContents();
		if (contents != null && !contents.isEmpty()) {
			renderJson(Feedback.error("存在下级内容，无法删除"));
			return;
		}
		new Category().dao().deleteById(id);
		renderJson(Feedback.success(new HashMap<>()));
	}

}