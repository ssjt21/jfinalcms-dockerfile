package com.cms.controller.front;

import com.cms.routes.RouteMapping;

/**
 * Controller - 搜索
 *
 *
 *
 */
@RouteMapping(url = "/search")
public class SearchController extends BaseController {


    /**
     * 搜索
     */
    public void index() {
        String keyword = getPara("keyword");
        Integer pageNumber = getParaToInt("pageNumber");
        if(pageNumber==null){
            pageNumber = 1;
        }
        setAttr("pageNumber", pageNumber);
        setAttr("keyword", keyword);
        render("/templates/"+getCurrentTemplate()+"/search.html");
    }
}
