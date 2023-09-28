package com.cms.entity;

import java.util.*;

import com.cms.Config;
import com.cms.entity.base.BaseContent;
import com.cms.util.SystemUtils;
import com.jfinal.kit.PathKit;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.cms.util.DbUtils;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;

/**
 * Entity - 内容
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Content extends BaseContent<Content> {

	/**
	 * 状态
	 */
	public enum Status{
		DISABLE("禁用"),
		NORMAL("启用");
		public String text;
		Status(String text){
			this.text = text;
		}
		public String getText(){
			return this.text;
		}
		public static Map<Integer, Content.Status> statusValueMap = new HashMap<>();
		static {
			Content.Status[] values = Content.Status.values();
			for (Content.Status status : values) {
				statusValueMap.put(status.ordinal(), status);
			}
		}
	}
    
    /**
     * 上一篇内容
     */
    @JSONField(serialize=false)  
    private Content lastContent;
    
    /**
     * 下一篇内容
     */
    @JSONField(serialize=false)  
    private Content nextContent;
    
    /**
     * 分类
     */
    @JSONField(serialize=false)  
    private Category category;

	/**
	 * 标签
	 */
	@JSONField(serialize=false)
	private List<Tag> tags;
    
    
	/**
	 * 查找内容分页
	 * 
	 * @param categoryId
	 *            栏目ID
	 * @param pageNumber
	 *            页码
	 * @param pageSize
	 *            每页记录数
	 * @return 内容分页
	 */
	public Page<Content> findPage(Integer categoryId,String title,Integer pageNumber,Integer pageSize,Integer siteId){
	    String filterSql = " and siteId="+siteId;
		if(categoryId!=null){
			filterSql+=" and (categoryId="+categoryId+" or categoryId in ( select id from cms_category where treePath  like '%"+Category.TREE_PATH_SEPARATOR+categoryId+Category.TREE_PATH_SEPARATOR+"%'))";
		}
        if(StringUtils.isNotBlank(title)){
            filterSql+= " and title like '%"+title+"%'";
        }
		String orderBySql = DbUtils.getOrderBySql("sort desc,createDate desc");
		return paginate(pageNumber, pageSize, "select *", "from cms_content where 1=1 "+filterSql+orderBySql);
	}
	
	
   /**
     * 查找内容分页
     * 
     * @param categoryId
     *            分类ID
     * @param pageNumber
     *            页码
     * @param pageSize
     *            每页记录数
     * @return 内容分页
     */
    public Page<Content> findPage(Integer categoryId,Integer tagId,String keyword,String condition,String orderBy,Integer pageNumber,Integer pageSize,Integer siteId){
        String filterSql = " and siteId="+siteId+" and status="+Status.NORMAL.ordinal();
        if(categoryId!=null){
            filterSql+=" and (categoryId="+categoryId+" or categoryId in ( select id from cms_category where treePath  like '%"+Category.TREE_PATH_SEPARATOR+categoryId+Category.TREE_PATH_SEPARATOR+"%'))";
        }
		if(tagId!=null){
			filterSql+= " and tagIdValue like '%,"+tagId+",%'";
		}
        if(StringUtils.isNotBlank(condition)){
            filterSql+=" and "+condition;
        }
        if(StringUtils.isNotBlank(keyword)){
            filterSql+= " and title like '%"+keyword+"%'";
        }
        String orderBySql = "";
        if(StringUtils.isBlank(orderBy)){
            orderBySql = DbUtils.getOrderBySql("sort desc,createDate desc");
        }else{
            orderBySql = DbUtils.getOrderBySql(orderBy+",sort desc");
        }
        return paginate(pageNumber, pageSize, "select *", "from cms_content where 1=1 "+filterSql+orderBySql);
    }
	
	/**
	 * 查找内容
	 * 
	 * @param categoryId
	 *            栏目ID
	 * @param orderBy
	 *            排序
	 * @param start
     *            起始位置
	 * @param count
	 *            数量
	 * @return 内容
	 */
	public List<Content> findList(Integer categoryId, String condition, Date startDate, Date endDate, String orderBy, Integer start, Integer count, Integer siteId){
	    String filterSql = " and siteId="+siteId+" and status="+Status.NORMAL.ordinal();
        if(categoryId!=null){
            filterSql+=" and (categoryId="+categoryId+" or categoryId in ( select id from cms_category where treePath  like '%"+Category.TREE_PATH_SEPARATOR+categoryId+Category.TREE_PATH_SEPARATOR+"%'))";
        }
        if(StringUtils.isNotBlank(condition)){
            filterSql+=" and "+condition;
        }
		if(startDate!=null){
			filterSql=" and createDate>='"+ DateFormatUtils.format(startDate, "yyyy-MM-dd HH:mm:ss")+"'";
		}
		if(endDate!=null){
			filterSql=" and createDate<='"+DateFormatUtils.format(endDate, "yyyy-MM-dd HH:mm:ss")+"'";
		}
        String orderBySql = "";
        if(StringUtils.isBlank(orderBy)){
            orderBySql = DbUtils.getOrderBySql("sort desc,createDate desc");
        }else{
            orderBySql = DbUtils.getOrderBySql(orderBy+",sort desc");
        }
        String countSql=DbUtils.getCountSql(start, count);
        return find("select * from cms_content where 1=1 "+filterSql+orderBySql+countSql);
    }
	
	/**
	 * 获取分类
	 * @return 分类
	 */
	public Category getCategory(){
	    if(category == null){
	        category = new Category().dao().findById(getCategoryId());
	    }
		return category;
	}

	/**
	 * 获取文本内容
	 * 
	 * @return 文本内容
	 */
	public String getText() {
		if (StringUtils.isEmpty(getIntroduction())) {
			return StringUtils.EMPTY;
		}
		return Jsoup.parse(getIntroduction()).text();
	}
	
   /**
     * 获取第一张图片
     * 
     * @return 第一张图片
     */
    public String getFirstImage(){
        Elements elements = Jsoup.parse(getIntroduction()).getElementsByTag("img");
        if(elements!=null && elements.size()>0){
            Element element = elements.get(0);
            return element.attr("src");
        }
        return null;
    }
    

	/**
	 * 获取上一篇内容
	 * 
	 * @return 上一篇内容
	 */
	public Content getLastContent(){
	    if(lastContent == null){
	        lastContent = findFirst("select * from cms_content where id < ? and categoryId=? and status=? order by id desc limit 1",getId(),getCategoryId(),Status.NORMAL.ordinal());
	    }
		return lastContent;
	}
	
	/**
	 * 获取下一篇内容
	 * 
	 * @return 下一篇内容
	 */
	public Content getNextContent(){
	    if(nextContent == null){
	        nextContent = findFirst("select * from cms_content where id > ? and categoryId=? and status=? order by id asc limit 1",getId(),getCategoryId(),Status.NORMAL.ordinal());
	    }
		return nextContent;
	}
	
	/**
	 * 获取属性
	 * 
	  @param name
	 * 			名称
	 * @return 属性
	 */
	public String getAttribute(String name){
		JSONObject jsonObject = JSONObject.parseObject(getModelFieldValue());
		return jsonObject.getString(name);
	}
	
   /**
     * 获取属性
     * 
      @param name
     *          名称
     * @return 属性
     */
    public List<String> getAttributes(String name){
        JSONObject jsonObject = JSONObject.parseObject(getModelFieldValue());
        JSONArray jsonArray = jsonObject.getJSONArray(name);
        if(jsonArray == null){
            return new ArrayList<>();
        }
        return JSONArray.parseArray(jsonArray.toJSONString(),String.class);
    }
    
    /**
     * 获取块数据
     * 
     * @return 块数据
     */
    public List<String> getChunkValues(){
    	if(StringUtils.isNotBlank(getChunkValue())){
			return JSONArray.parseArray(getChunkValue(), String.class);
		}
    	return new ArrayList<>();
    }

	/**
	 * 获取轮播多图
	 *
	 * @return
	 */
	public List<String> getPics(){
		if(StringUtils.isNotBlank(getPicValue())){
			return JSONArray.parseArray(getPicValue(), String.class);
		}
		return new ArrayList<>();
	}

	/**
	 * 获取标签
	 *
	 * @return
	 */
	public List<Tag> getTags(){
		String tagIdValue = getTagIdValue();
		if(StringUtils.isNotBlank(tagIdValue)){
			if(tags == null){
				String tagIdsSql = StringUtils.substring(tagIdValue,1,tagIdValue.length()-1);
				return new Tag().dao().find("select * from cms_tag where id in ("+tagIdsSql+") order by field(id,"+tagIdsSql+")");
			}
			return tags;
		}
		return new ArrayList<>();
	}

	/**
	 * 获取路径
	 *
	 * @return 路径
	 */
	public String getPath() {
		if(StringUtils.isNotBlank(getOutlink())){
			return getOutlink();
		}
		Config config = SystemUtils.getConfig();
		if(config.getSiteModel()==Config.SiteModel.DYNAMIC.ordinal()){
			return getDynamicPath();
		}
		return getHtmlPath();
	}

	/**
	 * 获取动态路径
	 *
	 * @return 动态路径
	 */
	public String getDynamicPath(){
		Site site = new Site().dao().findById(getSiteId());
		String siteUrl = site.getUrl();
		String categoryCat = getCategory().getCat();
		return siteUrl+"/"+categoryCat+"/"+getId();
	}

	/**
	 * 获取静态路径
	 *
	 * @return 静态路径
	 */
	public String getHtmlPath() {
		Site site = new Site().dao().findById(getSiteId());
		String siteUrl = site.getUrl();
		String categoryCat = getCategory().getCat();
		return siteUrl+"/"+categoryCat+"/"+getId()+".html";
	}

	/**
	 * 获取pc模板路径
	 *
	 * @return 模板路径
	 */
	public String getPcTemplatePath() {
		Site site = new Site().dao().findById(getSiteId());
		if(StringUtils.isBlank(site.getPcTemplate()) || StringUtils.isBlank(getCategory().getDetailTemplate())){
			return null;
		}
		return "templates/"+site.getPcTemplate()+"/"+getCategory().getDetailTemplate();
	}

	/**
	 * 获取mobile模板路径
	 *
	 * @return 模板路径
	 */
	public String getMobileTemplatePath() {
		Site site = new Site().dao().findById(getSiteId());
		if(StringUtils.isBlank(site.getMobileTemplate()) || StringUtils.isBlank(getCategory().getDetailTemplate())){
			return null;
		}
		return "templates/"+site.getMobileTemplate()+"/"+getCategory().getDetailTemplate();
	}
}
