package com.cms.controller.front;

import com.cms.routes.RouteMapping;

/**
 * Controller - Ajax
 *
 *
 *
 */
@RouteMapping(url = "/ajax")
public class AjaxController extends BaseController {


    /**
     * HTML
     */
    public void html(){
        keepPara();
        String html = getPara("html");
        render("/templates/"+getCurrentTemplate()+"/"+html);
    }
}
