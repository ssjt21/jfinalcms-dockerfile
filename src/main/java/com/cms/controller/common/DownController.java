package com.cms.controller.common;

import java.io.File;

import com.cms.routes.RouteMapping;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;

@RouteMapping(url = "/common/down")
public class DownController extends Controller{

	public void file(){
		String fileKey = getPara("fileKey");
		renderFile(new File(PathKit.getWebRootPath()+fileKey));
	}
}
