package com.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;

import com.cms.util.PinYinUtils;
import org.apache.commons.lang.ArrayUtils;

import com.cms.Feedback;
import com.cms.entity.Company;
import com.cms.entity.Site;
import com.cms.entity.Web;
import com.cms.routes.RouteMapping;
import com.cms.util.TemplateUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Controller - 站点
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/site")
public class SiteController extends BaseController {
	
	/**
	 * 列表
	 */
	public void index() {
		setListQuery();
		Integer pageNumber = getParaToInt("pageNumber");
		if(pageNumber==null){
			pageNumber = 1;
		}
		setAttr("page", new Site().dao().findPage(pageNumber,PAGE_SIZE));
		render(getView("site/index"));
	}

	/**
	 * 添加
	 */
	public void add() {
		setAttr("templates", TemplateUtils.getTemplates());
	    render(getView("site/add"));
	}

	/**
	 * 保存
	 */
	public void save() {
		Site site = getModel(Site.class,"",true);
		if(StringUtils.isBlank(site.getPcTemplate())){
			site.setPcTemplate(PinYinUtils.convertLower(site.getName()));
		}
		if(site.getIsMobile() && StringUtils.isBlank(site.getMobileTemplate())){
			site.setMobileTemplate(PinYinUtils.convertLower(site.getName())+"_mobile");
		}
		site.setCreateDate(new Date());
		site.setUpdateDate(new Date());
		site.save();
		//网站信息
		Web web = new Web();
		web.setSiteId(site.getId());
		web.save();
		//公司信息
		Company company = new Company();
		company.setSiteId(site.getId());
		company.save();
		redirect(getListQuery("/admin/site"));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("site", new Site().dao().findById(id));
		setAttr("templates", TemplateUtils.getTemplates());
		render(getView("site/edit"));
	}

	/**
	 * 更新
	 */
	public void update() {
		Site site = getModel(Site.class,"",true); 
		site.setUpdateDate(new Date());
		site.update();
		redirect(getListQuery("/admin/site"));
	}

	/**
	 * 删除
	 */
	public void delete() {
		Integer ids[] = getParaValuesToInt("ids");
		if(ArrayUtils.isNotEmpty(ids)){
			for(Integer id:ids){
				new Web().dao().findBySiteId(id).delete();
				new Company().dao().findBySiteId(id).delete();
				new Site().dao().deleteById(id);
			}
		}
		renderJson(Feedback.success(new HashMap<>()));
	}

	/**
	 * 设置为默认站点
	 */
	public void setDefault(){
		Long id = getParaToLong("id");
		Site defaultSite = new Site().dao().findDefault();
		defaultSite.setIsDefault(false);
		if(StringUtils.isNotBlank(defaultSite.getPcTemplate())){
			defaultSite.setCat(defaultSite.getPcTemplate());
		}else if(StringUtils.isNotBlank(defaultSite.getMobileTemplate())){
			defaultSite.setCat(defaultSite.getMobileTemplate());
		}
		defaultSite.setUpdateDate(new Date());
		defaultSite.update();
		Site site = new Site().dao().findById(id);
		site.setIsDefault(true);
		site.setUpdateDate(new Date());
		site.setCat("");
		site.update();
		redirect(getListQuery("/admin/site"));
	}

}