package com.cms.controller.front;

import com.cms.Feedback;
import com.cms.entity.Div;
import com.cms.entity.DivField;
import com.cms.entity.Guestbook;
import com.cms.entity.Site;
import com.cms.routes.RouteMapping;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Controller - 自定义表
 *
 *
 *
 */
@RouteMapping(url = "/div")
public class DivController extends BaseController {


    /**
     * 保存
     */
    public void save(){
        Integer id = getParaToInt("id");
        Div div = new Div().dao().findById(id);
        List<DivField> formFields = new DivField().dao().findList(id);
        Record divData = new Record();
        for(DivField divField : formFields){
            String value = getPara(divField.getName());
            divData.set(divField.getName(), value);
        }
        divData.set("createDate", new Date());
        divData.set("updateDate", new Date());
        Db.save(div.getTableName(), divData);
        renderJson(Feedback.success(new HashMap<>()));
    }

}
