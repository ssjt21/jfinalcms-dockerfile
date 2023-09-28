package com.cms.controller.admin;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.cms.Feedback;
import com.cms.entity.Div;
import com.cms.entity.DivField;
import com.cms.routes.RouteMapping;
import com.cms.util.DbUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;


/**
 * Controller - 自定义表数据
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/div_data")
public class DivDataController extends BaseController {
	
	/**
	 * 列表
	 */
	public void index(){
		setListQuery();
	    setAttr("divs", new Div().dao().findAll());
	    render(getView("div_data/index"));
	}

	/**
	 * 添加
	 */
	public void add(){
		Integer divId = getParaToInt("divId");
		Div div = new Div().dao().findById(divId);
		List<DivField> divFields = new DivField().dao().findList(divId);
		setAttr("divFields",divFields);
		setAttr("div", div);
		setAttr("divId", divId);
		render(getView("div_data/add"));
	}
	
	/**
	 * 保存
	 */
	public void save(){
		Integer divId = getParaToInt("divId");
		Div div = new Div().dao().findById(divId);
		List<DivField> divFields = new DivField().dao().findList(divId);
		Record divData = new Record();
		for(DivField divField : divFields){
			String value = getPara(divField.getName());
			divData.set(divField.getName(), value);
		}
		divData.set("createDate", new Date());
		divData.set("updateDate", new Date());
		Db.save(div.getTableName(), divData);
		renderJson(Feedback.success(new HashMap<>()));
	}
	
	/**
	 * 编辑
	 */
	public void edit() {
		Integer id = getParaToInt("id");
		Integer divId = getParaToInt("divId");
		Div div = new Div().dao().findById(divId);
		List<DivField> divFields = new DivField().dao().findList(divId);
		Record divData = Db.findById(div.getTableName(), id);
		setAttr("divFields",divFields);
		setAttr("div", div);
		setAttr("divData", divData);
		setAttr("divId", divId);
		render(getView("div_data/edit"));
	}

	/**
	 * 更新
	 */
	public void update() {
		Integer id = getParaToInt("id");
		Integer divId = getParaToInt("divId");
		Div div = new Div().dao().findById(divId);
		List<DivField> divFields = new DivField().dao().findList(divId);
		Record divData = new Record();
		for(DivField divField : divFields){
			String value = getPara(divField.getName());
			divData.set(divField.getName(), value);
		}
		divData.set("id", id);
		divData.set("updateDate", new Date());
		Db.update(div.getTableName(), divData);
		renderJson(Feedback.success(new HashMap<>()));
	}
	
	/**
	 * 数据
	 */
	public void data() {
		setListQuery();
		Integer divId = getParaToInt("divId");
		Integer pageNumber = getParaToInt("pageNumber");
		if(pageNumber==null){
			pageNumber = 1;
		}
		Div div = new Div().dao().findById(divId);
		List<DivField> divFields = new DivField().dao().findList(divId);
		setAttr("divFields", divFields);
		String orderBySql = DbUtils.getOrderBySql("createDate desc");
		Page<Record> page = Db.paginate(pageNumber, PAGE_SIZE, "select *", "from "+div.getTableName()+" where 1=1 "+orderBySql);
		setAttr("page", page);
		setAttr("divId", divId);
		render(getView("div_data/data"));
	}

	/**
	 * 删除
	 */
	public void delete() {
		Integer divId = getParaToInt("divId");
		Div div = new Div().dao().findById(divId);
		Integer ids[] = getParaValuesToInt("ids");
		if(ArrayUtils.isNotEmpty(ids)){
			Db.update("delete from "+div.getTableName()+" where id in("+StringUtils.join(ids, ",")+")");
		}
		renderJson(Feedback.success(new HashMap<>()));
	}

}