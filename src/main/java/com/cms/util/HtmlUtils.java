package com.cms.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cms.entity.*;
import com.jfinal.core.JFinal;
import com.jfinal.render.RenderManager;
import com.jfinal.template.Template;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.jfinal.kit.PathKit;

/**
 * Utils - 静态化
 *
 *
 *
 */
public class HtmlUtils {


	/**
	 * 类型
	 */
	public enum Type{
		ALL("全部"),
		INDEX("首页"),
		CATEGORY("栏目页"),
		CONTENT("内容页");
		public String text;
		Type(String text){
			this.text = text;
		}
		public String getText(){
			return this.text;
		}
		public static Map<Integer, HtmlUtils.Type> typeValueMap = new HashMap<>();
		static {
			HtmlUtils.Type[] values = HtmlUtils.Type.values();
			for (HtmlUtils.Type type : values) {
				typeValueMap.put(type.ordinal(), type);
			}
		}
	}

	/**
	 * 生成静态
	 *
	 * @param templatePath
	 *            模板文件路径
	 * @param staticPath
	 *            静态文件路径
	 * @param model
	 *            数据
	 * @return 生成数量
	 */
	public static int generate(String templatePath, String staticPath, Map<String, Object> model) {

		Writer writer = null;
		try {
			Template template = RenderManager.me().getEngine().getTemplate(templatePath);
			File staticFile = new File(staticPath);
			File staticDir = staticFile.getParentFile();
			if (staticDir != null) {
				staticDir.mkdirs();
			}
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(staticFile), "UTF-8"));
			template.render(model, writer);
			writer.flush();
			return 1;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	/**
	 * 生成静态
	 *
	 * @param content
	 *            内容
	 * @return 生成数量
	 */
	public static int generate(Content content) {
		if (content == null || StringUtils.isBlank(content.getCategory().getDetailTemplate())) {
			return 0;
		}
		delete(content);
		Map<String, Object> model = new HashMap<String, Object>();
		Site site = new Site().dao().findById(content.getSiteId());
		model.put("base", JFinal.me().getContextPath());
		model.put(Site.CURRENT_SITE, site);
		model.put(Web.CURRENT_WEB, new Web().dao().findBySiteId(site.getId()));
		model.put(Company.CURRENT_COMPANY, new Company().dao().findBySiteId(site.getId()));
		model.put("currentCategory", content.getCategory());
		model.put("currentContent", content);
		int generateCount = 0;
		if(StringUtils.isNotBlank(content.getPcTemplatePath())){
			generateCount+=generate(content.getPcTemplatePath(), PathKit.getWebRootPath()+content.getPath(), model);
		}
		if(StringUtils.isNotBlank(content.getMobileTemplatePath())){
			generateCount+=generate(content.getMobileTemplatePath(), PathKit.getWebRootPath()+"/mobile/"+content.getPath(), model);
		}
		return generateCount;
	}

	/**
	 * 生成静态
	 *
	 * @param category
	 *            栏目
	 * @return 生成数量
	 */
	public static int generate(Category category) {
		if (category == null || StringUtils.isNotBlank(category.getOutlink())){
			return 0;
		}
		Map<String, Object> model = new HashMap<String, Object>();
		Site site = new Site().dao().findById(category.getSiteId());
		model.put("base", JFinal.me().getContextPath());
		model.put(Site.CURRENT_SITE, site);
		model.put(Web.CURRENT_WEB, new Web().dao().findBySiteId(site.getId()));
		model.put(Company.CURRENT_COMPANY, new Company().dao().findBySiteId(site.getId()));
		model.put("currentCategory", category);
		model.put("pageNumber",1);
		int generateCount = 0;
		if(Model.Type.PAGE.ordinal() == category.getModel().getType()){
			delete(category);
			if(StringUtils.isNotBlank(category.getPcTemplatePath())){
				generateCount+=generate(category.getPcTemplatePath(), PathKit.getWebRootPath()+category.getPath(), model);
			}
			if(StringUtils.isNotBlank(category.getMobileTemplatePath())){
				generateCount+=generate(category.getMobileTemplatePath(), PathKit.getWebRootPath()+"/mobile/"+category.getPath(), model);
			}
		}else{
			if(StringUtils.isNotBlank(category.getPcTemplatePath())){
				generateCount+=generate(category.getPcTemplatePath(), PathKit.getWebRootPath()+category.getPath(), model);
			}
			if(StringUtils.isNotBlank(category.getMobileTemplatePath())){
				generateCount+=generate(category.getMobileTemplatePath(), PathKit.getWebRootPath()+"/mobile/"+category.getPath(), model);
			}
			//获取总页数
			Integer pcTotalPage = 0;
			Integer mobileTotalPage = 0;
			try {
				File pcHtmlFile = new File(PathKit.getWebRootPath()+category.getPath());
				if(pcHtmlFile.exists()){
					String pcHtml = FileUtils.readFileToString(pcHtmlFile,"UTF-8");
					Pattern pcPattern = Pattern.compile("^<!--totalPageStart\\((.*)\\)totalPageEnd-->$");
					Matcher pcMatcher = pcPattern.matcher(pcHtml);
					if (pcMatcher.find()) {
						pcTotalPage = Integer.valueOf(pcMatcher.group(1));
					}
				}
				File mobileHtmlFile = new File(PathKit.getWebRootPath()+"/mobile/"+category.getPath());
				if(mobileHtmlFile.exists()){
					String mobileHtml = FileUtils.readFileToString(mobileHtmlFile,"UTF-8");
					Pattern mobilePattern = Pattern.compile("^<!--totalPageStart\\((.*)\\)totalPageEnd-->$");
					Matcher mobileMatcher = mobilePattern.matcher(mobileHtml);
					if (mobileMatcher.find()) {
						mobileTotalPage = Integer.valueOf(mobileMatcher.group(1));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(StringUtils.isNotBlank(category.getPcTemplatePath())){
				for(int pageNumber=2;pageNumber<=pcTotalPage;pageNumber++){
					Map<String, Object> pcModel = new HashMap<String, Object>();
					pcModel.putAll(model);
					pcModel.put("pageNumber",pageNumber);
					generateCount+=generate(category.getPcTemplatePath(), PathKit.getWebRootPath()+category.getPagePath(pageNumber), pcModel);
				}
			}
			if(StringUtils.isNotBlank(category.getMobileTemplatePath())){
				for(int pageNumber=2;pageNumber<=mobileTotalPage;pageNumber++){
					Map<String, Object> mobileModel = new HashMap<String, Object>();
					mobileModel.putAll(model);
					mobileModel.put("pageNumber",pageNumber);
					generateCount+=generate(category.getMobileTemplatePath(), PathKit.getWebRootPath()+"/mobile/"+category.getPagePath(pageNumber), mobileModel);
				}
			}
		}
		return generateCount;
	}

	/**
	 * 生成内容静态
	 *
	 * @param siteId
	 *            站点ID
	 * @return 生成数量
	 */
	public static int generateCategory(Integer siteId) {
		int generateCount = 0;
		List<Category> categoryTree = new Category().dao().findTree(null,siteId);
		for(Category category : categoryTree){
			generateCount += generate(category);
		}
		return generateCount;
	}

	/**
	 * 生成内容静态
	 *
	 * @param categoryId
	 *            分类ID
	 * @param startDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @return 生成数量
	 */
	public static int generateContent(Integer categoryId, Date startDate, Date endDate,Integer siteId) {
		int generateCount = 0;
		for (int i = 0;; i += 100) {
			List<Content> contents = new Content().dao().findList(categoryId, null,startDate, endDate,null, i, 100,siteId);
			if (CollectionUtils.isNotEmpty(contents)) {
				for (Content content : contents) {
					generateCount += generate(content);
				}
			}
			if (contents.size() < 100) {
				break;
			}
		}
		return generateCount;
	}

	/**
	 * 生成首页静态
	 *
	 * @return 生成数量
	 */
	public static int generateIndex(Integer siteId) {
		Site site = new Site().dao().findById(siteId);
		int generateCount = 0;
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("base", JFinal.me().getContextPath());
		model.put(Site.CURRENT_SITE, site);
		model.put(Web.CURRENT_WEB, new Web().dao().findBySiteId(site.getId()));
		model.put(Company.CURRENT_COMPANY, new Company().dao().findBySiteId(site.getId()));
		if(StringUtils.isNotBlank(site.getPcTemplate())){
			generateCount += generate("templates/"+site.getPcTemplate()+"/index.html", PathKit.getWebRootPath()+"/index.html", model);
		}
		if(StringUtils.isNotBlank(site.getMobileTemplate())){
			generateCount += generate("templates/"+site.getMobileTemplate()+"/index.html", PathKit.getWebRootPath()+"/mobile/"+"/index.html", model);
		}
		return generateCount;
	}

	/**
	 * 生成所有静态
	 *
	 * @return 生成数量
	 */
	public static int generateAll(Integer siteId) {
		int generateCount = 0;
		generateCount += generateCategory(siteId);
		generateCount += generateContent(null, null, null,siteId);
		generateCount += generateIndex(siteId);
//		generateCount += generateSitemap();
		return generateCount;
	}

	/**
	 * 删除静态
	 *
	 * @param staticPath
	 *            静态文件路径
	 * @return 删除数量
	 */
	public static int delete(String staticPath) {
		if (StringUtils.isEmpty(staticPath)) {
			return 0;
		}
		File staticFile = new File(PathKit.getWebRootPath()+staticPath);
		return FileUtils.deleteQuietly(staticFile) ? 1 : 0;
	}


	/**
	 * 删除静态
	 *
	 * @param content
	 *            内容
	 * @return 删除数量
	 */
	public static int delete(Content content) {
		if (content == null || StringUtils.isEmpty(content.getPath())) {
			return 0;
		}
		return delete(content.getPath());
	}

	/**
	 * 删除静态
	 *
	 * @param category
	 *            栏目
	 * @return 删除数量
	 */
	public static int delete(Category category) {
		if (category == null || StringUtils.isEmpty(category.getPath())) {
			return 0;
		}

		return delete(category.getPath());
	}

	/**
	 * 删除静态
	 *
	 * @param category
	 *            栏目
	 * @param pageNumber
	 *            栏目
	 * @return 删除数量
	 */
	public static int delete(Category category,Integer pageNumber) {
		if (category == null || pageNumber == null || StringUtils.isEmpty(category.getPagePath(pageNumber))) {
			return 0;
		}
		return delete(category.getPagePath(pageNumber));
	}

	/**
	 * 删除首页静态
	 *
	 * @return 删除数量
	 */
	public static int deleteIndex() {
		return delete("index.html");
	}

//	/**
//	 * 生成Sitemap
//	 *
//	 * @return 生成数量
//	 */
//	public static int generateSitemap() {
//		int generateCount = 0;
//		List<SitemapUrl> sitemapUrls = new ArrayList<SitemapUrl>();
//		for (int i = 0;; i += 100) {
//			List<Content> contents = Content.dao.findList(i, 100, null);
//			if (CollectionUtils.isNotEmpty(contents)) {
//				for (Content content : contents) {
//					SitemapUrl contentSitemapUrl = new SitemapUrl();
//					contentSitemapUrl.setLoc(content.getUrl());
//					contentSitemapUrl.setLastmod(content.getModifyDate());
//					contentSitemapUrl.setChangefreq(SitemapUrl.Changefreq.daily);
//					contentSitemapUrl.setPriority(0.6F);
//					sitemapUrls.add(contentSitemapUrl);
//				}
//			}
//			if (contents.size() < 100) {
//				break;
//			}
//		}
//		Map<String, Object> model = new HashMap<String, Object>();
//		model.put("sitemapUrls", sitemapUrls);
//		String sitemapPath = TemplateUtils.getSitemapStaticPath();
//		generateCount += generate(TemplateUtils.getSitemapTemplatePath(), sitemapPath, model);
//		return generateCount;
//	}
//
//	/**
//	 * SitemapUrl
//	 *
//	 *
//	 *
//	 */
//	public static class SitemapUrl {
//
//		/**
//		 * 更新频率
//		 */
//		public enum Changefreq {
//
//			/** 经常 */
//			always,
//
//			/** 每小时 */
//			hourly,
//
//			/** 每天 */
//			daily,
//
//			/** 每周 */
//			weekly,
//
//			/** 每月 */
//			monthly,
//
//			/** 每年 */
//			yearly,
//
//			/** 从不 */
//			never
//		}
//
//		/** 链接地址 */
//		private String loc;
//
//		/** 最后修改日期 */
//		private Date lastmod;
//
//		/** 更新频率 */
//		private Changefreq changefreq;
//
//		/** 权重 */
//		private float priority;
//
//		/**
//		 * 获取链接地址
//		 *
//		 * @return 链接地址
//		 */
//		public String getLoc() {
//			return loc;
//		}
//
//		/**
//		 * 设置链接地址
//		 *
//		 * @param loc
//		 *            链接地址
//		 */
//		public void setLoc(String loc) {
//			this.loc = loc;
//		}
//
//		/**
//		 * 获取最后修改日期
//		 *
//		 * @return 最后修改日期
//		 */
//		public Date getLastmod() {
//			return lastmod;
//		}
//
//		/**
//		 * 设置最后修改日期
//		 *
//		 * @param lastmod
//		 *            最后修改日期
//		 */
//		public void setLastmod(Date lastmod) {
//			this.lastmod = lastmod;
//		}
//
//		/**
//		 * 获取更新频率
//		 *
//		 * @return 更新频率
//		 */
//		public Changefreq getChangefreq() {
//			return changefreq;
//		}
//
//		/**
//		 * 设置更新频率
//		 *
//		 * @param changefreq
//		 *            更新频率
//		 */
//		public void setChangefreq(Changefreq changefreq) {
//			this.changefreq = changefreq;
//		}
//
//		/**
//		 * 获取权重
//		 *
//		 * @return 权重
//		 */
//		public float getPriority() {
//			return priority;
//		}
//
//		/**
//		 * 设置权重
//		 *
//		 * @param priority
//		 *            权重
//		 */
//		public void setPriority(float priority) {
//			this.priority = priority;
//		}
//
//	}

}
