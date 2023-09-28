package com.cms.controller.front;

import com.cms.Feedback;
import com.cms.entity.Guestbook;
import com.cms.entity.Site;
import com.cms.routes.RouteMapping;

import java.util.Date;
import java.util.HashMap;

/**
 * Controller - 留言
 *
 *
 *
 */
@RouteMapping(url = "/guestbook")
public class GuestbookController extends BaseController {


    /**
     * 留言
     */
    public void save(){
        if(!validateCaptcha("captcha")){
            renderJson(Feedback.error("验证码错误!"));
            return;
        }
        Site currentSite = getCurrentSite();
        Guestbook guestbook =  getModel(Guestbook.class,"",true);
        guestbook.setStatus(0);
        guestbook.setCreateDate(new Date());
        guestbook.setUpdateDate(new Date());
        guestbook.setSiteId(currentSite.getId());
        guestbook.save();
        renderJson(Feedback.success(new HashMap<>()));
    }

}
