package com.cms.controller.admin.div;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.cms.entity.Div;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cms.Feedback;
import com.cms.controller.admin.BaseController;
import com.cms.entity.DivField;
import com.cms.routes.RouteMapping;
import com.cms.util.FieldUtils;


/**
 * Controller - 自定义表字段
 *
 *
 *
 */
@RouteMapping(url = "/admin/div/div_field")
public class DivFieldController extends BaseController {

	/**
	 * 列表
	 */
	public void index() {
		setListQuery();
		Integer divId = getParaToInt("divId");
		List<DivField> divFields = new DivField().dao().findList(divId);
		setAttr("divFields", divFields);
		setAttr("divId", divId);
		render(getView("div/div_field/index"));
	}

    /**
     * 检查名称是否存在
     */
    public void checkName() {
        Integer divId = getParaToInt("divId");
        String name = getPara("name");
        if (StringUtils.isEmpty(name)) {
            renderJson(false);
            return;
        }
        renderJson(!new DivField().dao().nameExists(divId,name));
    }


	/**
	 * 添加
	 */
	public void add() {
		Integer divId = getParaToInt("divId");
		setAttr("div", new Div().dao().findById(divId));
		render(getView("div/div_field/add"));
	}

	/**
	 * 保存
	 */
	public void save() {
		DivField divField = getModel(DivField.class,"",true);
		divField.setCreateDate(new Date());
		divField.setUpdateDate(new Date());
		divField.save();
		//数据库表
		Div div = divField.getDiv();
	    FieldUtils.add(div.getTableName(), divField.getName());
		redirect(getListQuery("/admin/div/div_field"));
	}


	/**
	 * 编辑
	 */
	public void edit() {
		Integer id = getParaToInt("id");
		setAttr("divField", new DivField().dao().findById(id));
		render(getView("div/div_field/edit"));
	}

	/**
	 * 修改
	 */
	public void update() {
		DivField divField = getModel(DivField.class,"",true);
		divField.setUpdateDate(new Date());
		divField.update();
		redirect(getListQuery("/admin/div/div_field"));
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
            DivField divField = new DivField().dao().findById(id);
            divField.setSort(sort);
            divField.update();
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
				DivField divField = new DivField().dao().findById(id);
				Div div = divField.getDiv();
				//数据库表
	            FieldUtils.delete(div.getTableName(), divField.getName());
	            divField.delete();
			}
		}
		renderJson(Feedback.success(new HashMap<>()));
	}

}