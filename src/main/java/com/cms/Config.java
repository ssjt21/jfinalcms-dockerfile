package com.cms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Config implements Serializable {

	private static final long serialVersionUID = 1012463140749529805L;

	/** 缓存名称 */
	public static final String CACHE_NAME = "config";

	/**
	 * 网站模式
	 */
	public enum SiteModel{
		DYNAMIC("动态"),
		REWRITE("伪静态"),
		HTML("静态");
		public String text;
		SiteModel(String text){
			this.text = text;
		}
		public String getText(){
			return this.text;
		}
		public static Map<Integer, Config.SiteModel> siteModelValueMap = new HashMap<>();
		static {
			Config.SiteModel[] values = Config.SiteModel.values();
			for (Config.SiteModel siteModel : values) {
				siteModelValueMap.put(siteModel.ordinal(), siteModel);
			}
		}
	}

	/** 是否水印开启 */
	private Boolean isWatermarkEnabled;
	
	/** 水印图片 */
	private String watermarkImage;
	
	/** 水印位置 */
	private String watermarkPosition;
	
	/** 是否开启缓存 */
	private Boolean isCacheEnabled;

	/** 网站模式 */
	private Integer siteModel;
	

	public Boolean getIsWatermarkEnabled() {
		return isWatermarkEnabled;
	}

	public void setIsWatermarkEnabled(Boolean isWatermarkEnabled) {
		this.isWatermarkEnabled = isWatermarkEnabled;
	}

	public String getWatermarkImage() {
		return watermarkImage;
	}

	public void setWatermarkImage(String watermarkImage) {
		this.watermarkImage = watermarkImage;
	}

	public String getWatermarkPosition() {
		return watermarkPosition;
	}

	public void setWatermarkPosition(String watermarkPosition) {
		this.watermarkPosition = watermarkPosition;
	}

	public Boolean getIsCacheEnabled() {
		return isCacheEnabled;
	}

	public void setIsCacheEnabled(Boolean isCacheEnabled) {
		this.isCacheEnabled = isCacheEnabled;
	}

	public Integer getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(Integer siteModel) {
		this.siteModel = siteModel;
	}
}
