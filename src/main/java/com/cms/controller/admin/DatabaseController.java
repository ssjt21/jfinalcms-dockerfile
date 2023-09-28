package com.cms.controller.admin;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.cms.Feedback;
import com.cms.routes.RouteMapping;
import com.cms.util.BackupUtils;
/**
 * Controller - 数据库
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/database")
public class DatabaseController extends BaseController {
	
    /**
     * 列表
     */
    public void index() {
        List<String> backups = BackupUtils.getFiles();
        Collections.reverse(backups);
        setAttr("backups", backups);
        render(getView("database/index"));
    }
    
    /**
     * 备份
     */
    public void backup(){
        BackupUtils.backup();
        renderJson(Feedback.success(new HashMap<>()));
    }
    
    /**
     * 还原
     */
    public void restore(){
        String name = getPara("name");
        BackupUtils.restore(name);
        renderJson(Feedback.success(new HashMap<>()));
    }

    
    /**
     * 删除
     */
    public void delete() {
        String name = getPara("name");
        BackupUtils.delete(name);
        renderJson(Feedback.success(new HashMap<>()));
    }
}
