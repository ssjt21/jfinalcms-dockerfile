package com.cms.controller.admin;
import java.util.*;

import com.cms.entity.*;
import com.cms.util.PinYinUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cms.Feedback;
import com.cms.routes.RouteMapping;
import org.apache.commons.lang.StringUtils;


/**
 * Controller - 内容
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/content")
public class ContentController extends BaseController {
	
	/**
	 * 列表
	 */
	public void index(){
		setAttr("categoryTree", new Category().dao().findTree(null,getCurrentSite().getId()));
	    render(getView("content/index"));
	}

	/**
	 * 标签
	 */
	public void tag(){
		setAttr("tags",new Tag().findAll());
		render(getView("content/tag"));
	}

	/**
	 * 添加
	 */
	public void add() {
		Category category = new Category().dao().findById(getParaToInt("categoryId"));
	    List<ModelField> modelFields = new ModelField().dao().findList(category.getModelId());
		setAttr("categoryTree", new Category().dao().findTree(null,getCurrentSite().getId()));
		setAttr("modelFields", modelFields);
		setAttr("category", category);
		render(getView("content/add"));
	}
	
	/**
	 * 保存
	 */
	public void save() {
		Content content = getModel(Content.class,"",true);  
		String[] chunkValues = getParaValues("chunkValues");
		Integer modelId = content.getCategory().getModelId();
		List<ModelField> modelFields = new ModelField().dao().findList(modelId);
		Map<String,Object> model = new HashMap<String,Object>();
		for(ModelField modelField : modelFields){
		    if("multipleFile".equals(modelField.getType())){
                model.put(modelField.getName(), getParaValues(modelField.getName()));
            }else{
                model.put(modelField.getName(), getPara(modelField.getName()));
            }
		}
		if(ArrayUtils.isNotEmpty(chunkValues)){
			content.setChunkValue(JSONArray.toJSONString(chunkValues));
		}else{
			content.setChunkValue(JSONArray.toJSONString(new String[]{}));
		}
		//轮播多图
		String[] picValues = getParaValues("picValues");
		if(ArrayUtils.isNotEmpty(picValues)){
			content.setPicValue(JSONArray.toJSONString(picValues));
		}else{
			content.setPicValue("");
		}
		//标签
		String[] tagNames = getParaValues("tagNames");
		if(ArrayUtils.isNotEmpty(tagNames)){
			List<Integer> tagIds = new ArrayList<>();
			for(String tagName : tagNames){
				Tag tag = new Tag().dao().findByName(tagName);
				if(tag==null){
					tag = new Tag();
					tag.setName(tagName);
					tag.setCat(PinYinUtils.convertLower(tagName));
					tag.setCreateDate(new Date());
					tag.setUpdateDate(new Date());
					tag.save();
				}
				tagIds.add(tag.getId());
			}
			content.setTagIdValue(","+ StringUtils.join(tagIds,",")+",");
		}else{
			content.setTagIdValue("");
		}
		content.setModelFieldValue(JSONObject.toJSONString(model));
		content.setSiteId(getCurrentSite().getId());
		content.setCreateDate(new Date());
		content.setUpdateDate(new Date());
		content.save();
		renderJson(Feedback.success(new HashMap<>()));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Integer id = getParaToInt("id");
		Content content = new Content().dao().findById(id);
		Category category = new Category().dao().findById(content.getCategoryId());
        List<ModelField> modelFields = new ModelField().dao().findList(category.getModelId());
		setAttr("categoryTree", new Category().dao().findTree(null,getCurrentSite().getId()));
		setAttr("category", category);
        setAttr("modelFields", modelFields);
		setAttr("content", content);
		render(getView("content/edit"));
	}

	/**
	 * 更新
	 */
	public void update() {
		Content content = getModel(Content.class,"",true);  
		String[] chunkValues = getParaValues("chunkValues");
		Integer modelId = content.getCategory().getModelId();
		List<ModelField> modelFields = new ModelField().dao().findList(modelId);
		Map<String,Object> model = new HashMap<String,Object>();
		for(ModelField modelField : modelFields){
		    if("multipleFile".equals(modelField.getType())){
		        model.put(modelField.getName(), getParaValues(modelField.getName()));
		    }else{
		        model.put(modelField.getName(), getPara(modelField.getName()));
		    }
		}
		if(ArrayUtils.isNotEmpty(chunkValues)){
			content.setChunkValue(JSONArray.toJSONString(chunkValues));
		}else{
			content.setChunkValue(JSONArray.toJSONString(new String[]{}));
		}
		//轮播多图
		String[] picValues = getParaValues("picValues");
		if(ArrayUtils.isNotEmpty(picValues)){
			content.setPicValue(JSONArray.toJSONString(picValues));
		}else{
			content.setPicValue("");
		}
		//标签
		String[] tagNames = getParaValues("tagNames");
		if(ArrayUtils.isNotEmpty(tagNames)){
			List<Integer> tagIds = new ArrayList<>();
			for(String tagName : tagNames){
				Tag tag = new Tag().dao().findByName(tagName);
				if(tag==null){
					tag = new Tag();
					tag.setName(tagName);
					tag.setCat(PinYinUtils.convertLower(tagName));
					tag.setCreateDate(new Date());
					tag.setUpdateDate(new Date());
					tag.save();
				}
				tagIds.add(tag.getId());
			}
			content.setTagIdValue(","+ StringUtils.join(tagIds,",")+",");
		}else{
			content.setTagIdValue("");
		}
		content.setModelFieldValue(JSONObject.toJSONString(model));
		content.setUpdateDate(new Date());
		content.update();
		renderJson(Feedback.success(new HashMap<>()));
	}
	
	/**
	 * 修改状态
	 */
	public void updateStatus(){
		Integer status = BooleanUtils.toInteger(getParaToBoolean("value"));
		Integer id = getParaToInt("id");
		Content content = new Content().dao().findById(id);
		content.setStatus(status);
		content.setUpdateDate(new Date());
		content.update();
		renderJson(Feedback.success(new HashMap<>()));
	}
	
	/**
	 * 修改是否置顶
	 */
	public void updateIsTop(){
		Boolean isTop = getParaToBoolean("value");
		Integer id = getParaToInt("id");
		Content content = new Content().dao().findById(id);
		content.setIsTop(isTop);
		content.setUpdateDate(new Date());
		content.update();
		renderJson(Feedback.success(new HashMap<>()));
	}
	
	/**
	 * 修改是否推荐
	 */
	public void updateIsRecommend(){
		Boolean isRecommend = getParaToBoolean("value");
		Integer id = getParaToInt("id");
		Content content = new Content().dao().findById(id);
		content.setIsRecommend(isRecommend);
		content.setUpdateDate(new Date());
		content.update();
		renderJson(Feedback.success(new HashMap<>()));
	}
	
	/**
	 * 修改是否头条
	 */
	public void updateIsHeadline(){
		Boolean isHeadline = getParaToBoolean("value");
		Integer id = getParaToInt("id");
		Content content = new Content().dao().findById(id);
		content.setIsHeadline(isHeadline);
		content.setUpdateDate(new Date());
		content.update();
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
            Content content = new Content().dao().findById(id);
            content.setSort(sort);
            content.update();
        }
        renderJson(Feedback.success(new HashMap<>()));
    }
	
	/**
	 * 数据
	 */
	public void data() {
		setListQuery();
	    String title = getPara("title");
	    Integer categoryId = getParaToInt("categoryId");
		Integer pageNumber = getParaToInt("pageNumber");
		if(pageNumber==null){
			pageNumber = 1;
		}
		setAttr("categoryId",categoryId);
		setAttr("page", new Content().dao().findPage(categoryId,title,pageNumber,PAGE_SIZE,getCurrentSite().getId()));
		setAttr("title", title);
		render(getView("content/data"));
	}

	/**
	 * 删除
	 */
	public void delete() {
		Integer ids[] = getParaValuesToInt("ids");
		if(ArrayUtils.isNotEmpty(ids)){
			for(Integer id:ids){
				new Content().dao().deleteById(id);
			}
		}
		renderJson(Feedback.success(new HashMap<>()));
	}

}