package com.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cms.Feedback;
import com.cms.entity.FriendLink;
import com.cms.entity.Slide;
import com.cms.routes.RouteMapping;


/**
 * Controller - 友情链接
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/friend_link")
public class FriendLinkController extends BaseController {
	
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
		setAttr("page", new FriendLink().dao().findPage(pageNumber,PAGE_SIZE,getCurrentSite().getId()));
		setAttr("gids", new FriendLink().dao().findGids(getCurrentSite().getId()));
		setAttr("name", name);
		render(getView("friend_link/index"));
	}


	/**
	 * 添加
	 */
	public void add() {
		setAttr("gids", new FriendLink().dao().findGids(getCurrentSite().getId()));
		render(getView("friend_link/add"));
	}

	/**
	 * 保存
	 */
	public void save() {
		FriendLink friendLink = getModel(FriendLink.class,"",true);  
		if(friendLink.getGid()==null){
			Integer maxGid = new FriendLink().dao().findMaxGid(getCurrentSite().getId());
			if(maxGid==null){
				maxGid=0;
			}
			maxGid = maxGid+1;
			friendLink.setGid(maxGid);
		}
		friendLink.setSiteId(getCurrentSite().getId());
		friendLink.setCreateDate(new Date());
		friendLink.setUpdateDate(new Date());
		friendLink.save();
		redirect(getListQuery("/admin/friend_link"));
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Integer id = getParaToInt("id");
		setAttr("friendLink", new FriendLink().dao().findById(id));
		setAttr("gids", new FriendLink().dao().findGids(getCurrentSite().getId()));
		render(getView("friend_link/edit"));
	}

	/**
	 * 更新
	 */
	public void update() {
		FriendLink friendLink = getModel(FriendLink.class,"",true);   
		friendLink.setUpdateDate(new Date());
		friendLink.update();
		redirect(getListQuery("/admin/friend_link"));
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
            FriendLink friendLink = new FriendLink().dao().findById(id);
            friendLink.setSort(sort);
            friendLink.update();
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
				new FriendLink().dao().deleteById(id);
			}
		}
		renderJson(Feedback.success(new HashMap<>()));
	}

}