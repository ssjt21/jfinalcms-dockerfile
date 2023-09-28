package com.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;

import com.cms.Feedback;
import com.cms.entity.Guestbook;
import com.cms.routes.RouteMapping;


/**
 * Controller - 留言
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/guestbook")
public class GuestbookController extends BaseController {
	
	/**
	 * 列表
	 */
	public void index() {
		setListQuery();
	    String name = getPara("name");
		Integer pageNumber = getParaToInt("pageNumber");
		if(pageNumber==null){
			pageNumber = 1;
		}
		setAttr("page", new Guestbook().dao().findPage(pageNumber,PAGE_SIZE,getCurrentSite().getId()));
		setAttr("name", name);
		render(getView("guestbook/index"));
	}
	
	/**
	 * 回复
	 */
	public void reply(){
		Integer guestbookId = getParaToInt("guestbookId");
		Guestbook guestbook = new Guestbook().dao().findById(guestbookId);
		setAttr("guestbook", guestbook);
		render(getView("guestbook/reply"));
	}
	
	/**
	 * 修改回复
	 */
	public void updateReply(){
		String replyContent = getPara("replyContent");
		Integer guestbookId = getParaToInt("guestbookId");
		Guestbook guestbook = new Guestbook().dao().findById(guestbookId);
		guestbook.setReplyContent(replyContent);
		guestbook.update();
		renderJson(Feedback.success(new HashMap<>()));
	}
	
	/**
	 * 修改状态
	 */
	public void updateStatus(){
		Integer status = BooleanUtils.toInteger(getParaToBoolean("status"));
		Integer id = getParaToInt("id");
		Guestbook guestbook = new Guestbook().dao().findById(id);
		guestbook.setStatus(status);
		guestbook.setUpdateDate(new Date());
		guestbook.update();
		renderJson(Feedback.success(new HashMap<>()));
	}


	/**
	 * 删除
	 */
	public void delete() {
		Integer ids[] = getParaValuesToInt("ids");
		if(ArrayUtils.isNotEmpty(ids)){
			for(Integer id:ids){
				new Guestbook().dao().deleteById(id);
			}
		}
		renderJson(Feedback.success(new HashMap<>()));
	}

}