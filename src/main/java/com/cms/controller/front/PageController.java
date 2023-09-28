package com.cms.controller.front;

import com.cms.routes.RouteMapping;

/**
 * Controller - 页面
 *
 *
 *
 */
@RouteMapping(url = "/page")
public class PageController extends BaseController {


    /**
     * 页面
     */
    public void index(){
        keepPara();
        String html = getPara(0);
        render("/templates/"+getCurrentTemplate()+"/"+html+".html");
    }
}
