package com.cms.controller.admin.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cms.Feedback;
import com.cms.controller.admin.BaseController;
import com.cms.entity.Model;
import com.cms.entity.ModelField;
import com.cms.routes.RouteMapping;


/**
 * Controller - 模型字段
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/model/model_field")
public class ModelFieldController extends BaseController {
	
	/**
	 * 列表
	 */
	public void index() {
		setListQuery();
		Integer modelId = getParaToInt("modelId");
		List<ModelField> modelFields = new ModelField().dao().findList(modelId);
		setAttr("modelFields", modelFields);
		setAttr("modelId", modelId);
		render(getView("model/model_field/index"));
	}
    
    /**
     * 检查名称是否存在
     */
    public void checkName() {
        Integer modelId = getParaToInt("modelId");
        String name = getPara("name");
        if (StringUtils.isEmpty(name)) {
            renderJson(false);
            return;
        }
        renderJson(!new ModelField().dao().nameExists(modelId,name));
    }


	/**
	 * 添加
	 */
	public void add() {
		Integer modelId = getParaToInt("modelId");
		setAttr("model", new Model().dao().findById(modelId));
		render(getView("model/model_field/add"));
	}

	/**
	 * 保存
	 */
	public void save() {
		ModelField modelField = getModel(ModelField.class,"",true); 
		modelField.setCreateDate(new Date());
		modelField.setUpdateDate(new Date());
		modelField.save();
		redirect(getListQuery("/admin/model/model_field"));
	}

	
	/**
	 * 编辑
	 */
	public void edit() {
		Integer id = getParaToInt("id");
		setAttr("modelField", new ModelField().dao().findById(id));
		render(getView("model/model_field/edit"));
	}

	/**
	 * 更新
	 */
	public void update() {
		ModelField modelField = getModel(ModelField.class,"",true); 
		modelField.setUpdateDate(new Date());
		modelField.update();
		redirect(getListQuery("/admin/model/model_field"));
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
            ModelField modelField = new ModelField().dao().findById(id);
            modelField.setSort(sort);
            modelField.update();
        }
        renderJson(Feedback.success(new HashMap<>()));
    }

	/**
	 * 删除
	 */
	public void delete() {
		Integer ids[] = getParaValuesToInt("ids");
		if(ArrayUtils.isNotEmpty(ids)){
			for(Integer id:ids){
				ModelField modelField = new ModelField().dao().findById(id);
		        modelField.delete();
			}
		}
		renderJson(Feedback.success(new HashMap<>()));
	}

}