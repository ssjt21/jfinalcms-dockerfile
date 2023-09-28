/*
 * 
 * 
 * 
 */
package com.cms.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cms.entity.Site;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.cms.TemplateFile;
import com.jfinal.kit.PathKit;

/**
 * Utils - 模板
 * 
 * 
 * 
 */
public class TemplateUtils {
    
    /**
     * 获取所有模板
     * 
     * @return 所有模板
     */
    public static List<String> getTemplates(){
        File[] files = new File(PathKit.getWebRootPath()+File.separator+"templates").listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.exists() && file.isDirectory();
            }
        });
        List<String> templates = new ArrayList<String>();
        for (File file : files) {
            templates.add(file.getName());
        }
        return templates;
    }


	/**
	 * 获取站点模板
	 * @return
	 */
	public static List<TemplateFile> getSiteTemplates(Site site){
		List<TemplateFile> templateFiles = new ArrayList<>();
		String pcTemplate = site.getPcTemplate();
		String pcTemplatePath = PathKit.getWebRootPath()+"/templates/"+pcTemplate;
		File pcFile = new File(pcTemplatePath);
		TemplateFile pcTemplateFile=new TemplateFile();
		pcTemplateFile.setName(pcTemplate);
		pcTemplateFile.setModifyDate(DateFormatUtils.format(pcFile.lastModified(), "yyyy-MM-dd HH:mm:ss"));
		pcTemplateFile.setIsDirectory(true);
		templateFiles.add(pcTemplateFile);
		TemplateFile mobileTemplateFile=new TemplateFile();
		String mobileTemplate = site.getMobileTemplate();
		if(StringUtils.isNotBlank(mobileTemplate)){
			String mobileTemplatePath = PathKit.getWebRootPath()+"/templates/"+mobileTemplate;
			File mobileFile = new File(mobileTemplatePath);
			mobileTemplateFile.setName(mobileTemplate);
			mobileTemplateFile.setModifyDate(DateFormatUtils.format(mobileFile.lastModified(), "yyyy-MM-dd HH:mm:ss"));
			mobileTemplateFile.setIsDirectory(true);
			templateFiles.add(mobileTemplateFile);
		}
		return templateFiles;
	}
	
	/**
	 * 获取模板文件
	 * 
	 * @return 模板文件
	 */
	public static List<TemplateFile> getTemplateFiles(String directory){
		String templatePath = PathKit.getWebRootPath()+"/templates/"+directory;
		List<TemplateFile> templateFiles=new ArrayList<TemplateFile>();
		File file =new File(templatePath);
		if(!file.exists()){
			file.mkdirs();
		}
		File [] files=file.listFiles();
		for(int i=0;i<files.length;i++){
			TemplateFile templateFile=new TemplateFile();
			templateFile.setName(files[i].getName());
			templateFile.setModifyDate(DateFormatUtils.format(files[i].lastModified(), "yyyy-MM-dd HH:mm:ss"));
			if(files[i].isDirectory()){
				templateFile.setIsDirectory(true);
			}else{
			    long fileLength = files[i].length();
			    String size = "";
			    if(fileLength%1024 == 0){
			        size = fileLength/1024 +"KB";
			    }else{
			        size = (fileLength/1024+1)+"KB";
			    }
			    templateFile.setSize(size);
				templateFile.setIsDirectory(false);
				templateFile.setType(FilenameUtils.getExtension(files[i].getName()));
			}
			templateFiles.add(templateFile);
		}
		return templateFiles;
	}
	
	/**
	 * 读取模板文件内容
	 * 
	 * @param templatePath
	 *            模板路径
	 * @return 模板文件内容
	 */
	public static String read(String templatePath) {
		try {
			String path = PathKit.getWebRootPath()+"/templates/"+templatePath;
			File templateFile = new File(path);
			return FileUtils.readFileToString(templateFile, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} 
	}

	/**
	 * 写入模板文件内容
	 * 
	 * @param templatePath
	 *            模板路径
	 * @param content
	 *            模板文件内容
	 */
	public static void write(String templatePath, String content) {
		try {
			String path = PathKit.getWebRootPath()+"/templates/"+templatePath;
			File file = new File(path);
			FileUtils.writeStringToFile(file, content, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} 
	}

	/**
	 * 删除模板文件
	 *
	 * @param templatePath
	 *            模板路径
	 */
	public static void delete(String templatePath){
		String path = PathKit.getWebRootPath()+"/templates/"+templatePath;
		File file = new File(path);
		FileUtils.deleteQuietly(file);
	}
}