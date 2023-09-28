/*
 * 
 * 
 * 
 */
package com.cms.controller.admin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;

import com.cms.CommonAttribute;
import com.cms.Config;
import com.cms.routes.RouteMapping;
import com.cms.util.ImageUtils;
import com.cms.util.SystemUtils;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

/**
 * Controller - 文件
 * 
 * 
 * 
 */
@RouteMapping(url = "/admin/file")
public class FileController extends BaseController {

	/**
	 * 上传
	 */
    public void upload() {
        UploadFile uploadFile = getFile();
        Map<String, Object> data = new HashMap<String, Object>();
        if (uploadFile == null || uploadFile.getFile().length()==0) {
            data.put("message", "操作错误");
            data.put("state", "ERROR");
            renderJson(data);
            return;
        }
        String suffix = FilenameUtils.getExtension(uploadFile.getOriginalFileName());
        String newFileBaseName = UUID.randomUUID().toString();
        String newFileName = newFileBaseName+"."+suffix;
        String url = "/"+CommonAttribute.UPLOAD_PATH+"/"+newFileName;
        File newFile = new File(PathKit.getWebRootPath()+url);
        uploadFile.getFile().renameTo(newFile);
        String [] imageSuffixs = new String[]{"jpg","png"};
        Config config = SystemUtils.getConfig();
        if(config.getIsWatermarkEnabled() && ArrayUtils.contains(imageSuffixs, suffix.toLowerCase())){
            String watermarkImage = config.getWatermarkImage();
            String watermarkPosition = config.getWatermarkPosition();
            File watermarkImageFile = new File(PathKit.getWebRootPath()+watermarkImage);
            String sourceFileName = newFileBaseName+"_source."+suffix;
            File sourceFile = new File(PathKit.getWebRootPath()+"/"+CommonAttribute.UPLOAD_PATH+"/"+sourceFileName);
            newFile.renameTo(sourceFile);
            ImageUtils.addWatermark(sourceFile, newFile, watermarkImageFile, watermarkPosition);
            FileUtils.deleteQuietly(sourceFile);
        }
        data.put("message", "成功");
        data.put("state", "SUCCESS");
        data.put("url", url);
        data.put("name",FilenameUtils.getBaseName(url));
        FileUtils.deleteQuietly(uploadFile.getFile());
        renderJson(data);
    }
}