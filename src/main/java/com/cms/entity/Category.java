package com.cms.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cms.Config;
import com.cms.entity.base.BaseCategory;
import com.cms.util.SystemUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.annotation.JSONField;
import com.cms.util.DbUtils;

/**
 * Entity - 内容栏目
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Category extends BaseCategory<Category> {
	
	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";

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
		public static Map<Integer, Category.Status> statusValueMap = new HashMap<>();
		static {
			Category.Status[] values = Category.Status.values();
			for (Category.Status status : values) {
				statusValueMap.put(status.ordinal(), status);
			}
		}
	}
	
	/**
	 * 模型
	 */
	@JSONField(serialize=false)  
	private Model model;
	
	/**
	 * 内容
	 */
	@JSONField(serialize=false)  
	private List<Content> contents;
	
	/**
	 * 下级分类
	 */
	@JSONField(serialize=false)  
	private List<Category> children;
	
	/**
	 * 上级分类
	 */
	@JSONField(serialize=false)  
	private Category parent;
	
	/**
	 * 获取模型
	 * @return	模型
	 */
	public Model getModel(){
		if(model == null){
			model = new Model().dao().findById(getModelId());
	    }
		return model;
	}
	
	/**
	 * 根据URL目录获取分类
	 * @return	分类
	 */
	public Category findByCat(String cat,Integer siteId){
		return new Category().findFirst("select * from cms_category where cat = ? and siteId=?",cat,siteId);
	}

	/**
	 * 根据名称获取分类
	 * @return	分类
	 */
	public Category findByName(String name,Integer siteId){
		return new Category().findFirst("select * from cms_category where name = ? and siteId=?",name,siteId);
	}
	
	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public List<Content> getContents(){
	    if(contents == null){
	        contents = new Content().dao().find("select * from cms_content where categoryId = ?",getId());
	    }
		return contents;
	}
	
	/**
	 * 获取下级分类
	 * 
	 * @return 下级分类
	 */
	public List<Category> getChildren() {
	    if(children == null){
	        children = find("select * from cms_category where parentId=? order by sort desc",getId());
	    }
		return children;
	}
	
	/**
	 * 获取上级分类
	 * @return	上级分类
	 */
	public Category getParent(){
	    if(parent == null){
	        parent = findById(getParentId());
	    }
		return parent;
	}
	
	/**
	 * 根据模型ID查询分类
	 * 
	 * @param modelId
     *            模型ID
	 * @param siteId
	 *            站点ID
	 * @return 顶级分类
	 */
	public List<Category> findByModelId(Integer modelId,Integer siteId){
	    String filterSql = " and siteId="+siteId;
	    if(modelId != null){
	    	filterSql+= " and modelId="+modelId;
	    }
		String orderBySql = DbUtils.getOrderBySql("grade asc,sort desc");
		List<Category> categorys = find("select * from cms_category where 1=1 "+filterSql+orderBySql);
		sort(categorys);
		return categorys;
	}
	
	/**
	 * 查找顶级分类
	 * 
	 * @param start
     *            起始位置
	 * @param count
	 *            数量
	 * @return 顶级分类
	 */
	public List<Category> findRoots(Integer start,Integer count,Integer siteId){
	    String filterSql = " and siteId="+siteId+" and status="+Status.NORMAL.ordinal();
		String countSql=DbUtils.getCountSql(start, count);
		String orderBySql = DbUtils.getOrderBySql("sort desc");
		return find("select * from cms_category where parentId is null"+filterSql+orderBySql+countSql);
	}
	
	
	/**
	 * 查找上级分类
	 * 
	 * @param categoryId
	 *            分类Id
	 * @param recursive
	 *            是否递归
	 * @param start
     *            起始位置
	 * @param count
	 *            数量
	 * @return 上级分类
	 */
	public List<Category> findParents(Integer categoryId,Boolean recursive,Integer start, Integer count,Integer siteId){
		Category category = findById(categoryId);
		if(categoryId == null || category.getParentId() == null){
			return Collections.emptyList();
		}
		String filterSql = " and siteId="+siteId+" and status="+Status.NORMAL.ordinal();
		if(recursive){
			String countSql=DbUtils.getCountSql(start, count);
			String orderBySql = DbUtils.getOrderBySql("grade asc");
			return find("select * from cms_category where id in ("+StringUtils.join(category.getParentIds(), ",")+") "+filterSql+orderBySql+countSql);
		}else{
			return find("select * from cms_category where id = ? "+filterSql,findById(categoryId).getParentId());
		}
	}
	
	
	/**
	 * 查找下级分类
	 * 
	 * @param categoryId
	 *            分类Id
	 * @param recursive
	 *            是否递归
	 * @param start
     *            起始位置
	 * @param count
	 *            数量
	 * @return 下级分类
	 */
	public List<Category> findChildren(Integer categoryId,Integer status,Boolean recursive,Integer start,Integer count,Integer siteId){
	    String filterSql = " and siteId="+siteId;
	    if(status!=null){
	    	filterSql += " and status="+status;
	    }
	    if(recursive){
			String countSql=DbUtils.getCountSql(start, count);
			String orderBySql = DbUtils.getOrderBySql("grade asc,sort desc");
			List<Category> categorys;
			if(categoryId!=null){
			    categorys = find("select * from cms_category where 1=1 and treePath like ? "+filterSql+orderBySql+countSql,"%,"+categoryId+",%");
			}else{
			    categorys = find("select * from cms_category where 1=1 "+filterSql+orderBySql+countSql);
			}
			sort(categorys);
			return categorys;
		}else{
		    String orderBySql = DbUtils.getOrderBySql("sort desc");
			return find("select * from cms_category where parentId = ? "+filterSql+orderBySql,categoryId);
		}
	}
	
	/**
	 * 查找分类树
	 * 
	 * @return 分类树
	 */
	public List<Category> findTree(Integer status,Integer siteId){
		return findChildren(null,status,true,null,null,siteId);
	}
	
	
	/**
	 * 获取所有上级分类ID
	 * 
	 * @return 所有上级分类ID
	 */
	public Integer[] getParentIds() {
		String[] treePaths = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
		Integer[] result = new Integer[treePaths.length];
		for (int i = 0; i < treePaths.length; i++) {
			result[i] = Integer.valueOf(treePaths[i]);
		}
		return result;
	}
	
	/**
	 * 排序分类
	 * 
	 * @param categorys
	 *            分类
	 */
	private void sort(List<Category> categorys) {
		if(categorys == null || categorys.size()==0) {
			return;
		}
		final Map<Integer, Integer> sortMap = new HashMap<Integer, Integer>();
		for (Category category : categorys) {
		    sortMap.put(category.getId(), category.getSort());
		}
		Collections.sort(categorys, new Comparator<Category>() {
			@Override
			public int compare(Category category1, Category category2) {
				Integer[] ids1 = (Integer[]) ArrayUtils.add(category1.getParentIds(), category1.getId());
				Integer[] ids2 = (Integer[]) ArrayUtils.add(category2.getParentIds(), category2.getId());
				Iterator<Integer> iterator1 = Arrays.asList(ids1).iterator();
				Iterator<Integer> iterator2 = Arrays.asList(ids2).iterator();
				CompareToBuilder compareToBuilder = new CompareToBuilder();
				while (iterator1.hasNext() && iterator2.hasNext()) {
					Integer id1 = iterator1.next();
					Integer id2 = iterator2.next();
					Integer sort1 = sortMap.get(id1);
					Integer sort2 = sortMap.get(id2);
					compareToBuilder.append(sort2,sort1).append(id1, id2);
					if (!iterator1.hasNext() || !iterator2.hasNext()) {
						compareToBuilder.append(category1.getGrade(), category2.getGrade());
					}
				}
				return compareToBuilder.toComparison();
			}
		});
	}
	
	
	/**
	 * 设置值
	 * 
	 */
	public void setValue() {
		if (getParentId() != null) {
			setTreePath(getParent().getTreePath() + getParent().getId() + TREE_PATH_SEPARATOR);
		} else {
			setTreePath(TREE_PATH_SEPARATOR);
		}
		setGrade(getParentIds().length);
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
	    	return getDynamicPath(null);
		}
	    return getHtmlPath(null);
	}

	/**
	 * 获取路径
	 *
	 * @return 路径
	 */
	public String getPagePath(Integer pageNumber) {
		Config config = SystemUtils.getConfig();
		if(config.getSiteModel()==Config.SiteModel.DYNAMIC.ordinal()){
			return getDynamicPath(pageNumber);
		}
		return getHtmlPath(pageNumber);
	}

	/**
	 * 获取动态路径
	 *
	 * @return 动态路径
	 */
	public String getDynamicPath(Integer pageNumber){
		Site site = new Site().dao().findById(getSiteId());
		String siteUrl = site.getUrl();
		String categoryCat = getCat();
		String pageNumberUrl = "";
		if(pageNumber !=null){
			pageNumberUrl = "_"+pageNumber;
		}
		return siteUrl+"/"+categoryCat+pageNumberUrl;
	}

	/**
	 * 获取静态路径
	 *
	 * @return 静态路径
	 */
	public String getHtmlPath(Integer pageNumber) {
		Site site = new Site().dao().findById(getSiteId());
		String siteUrl = site.getUrl();
		String categoryCat = getCat();
		String pageNumberUrl = "";
		if(pageNumber !=null){
			pageNumberUrl = "_"+pageNumber;
		}
		return siteUrl+"/"+categoryCat+pageNumberUrl+".html";
	}

	/**
	 * 获取pc模板路径
	 *
	 * @return 模板路径
	 */
	public String getPcTemplatePath() {
		Site site = new Site().dao().findById(getSiteId());
		if(StringUtils.isBlank(site.getPcTemplate())){
			return null;
		}
		if(getModel().getType()==Model.Type.LIST.ordinal()){
			if(StringUtils.isBlank(getListTemplate())){
				return null;
			}
			return "templates/"+site.getPcTemplate()+"/"+getListTemplate();
		}else if(getModel().getType()==Model.Type.PAGE.ordinal()){
			if(StringUtils.isBlank(getDetailTemplate())){
				return null;
			}
			return "templates/"+site.getPcTemplate()+"/"+getDetailTemplate();
		}else{
			return null;
		}
	}

	/**
	 * 获取mobile模板路径
	 *
	 * @return 模板路径
	 */
	public String getMobileTemplatePath() {
		Site site = new Site().dao().findById(getSiteId());
		if(StringUtils.isBlank(site.getMobileTemplate())){
			return null;
		}
		if(getModel().getType()==Model.Type.LIST.ordinal()){
			if(StringUtils.isBlank(getListTemplate())){
				return null;
			}
			return "templates/"+site.getMobileTemplate()+"/"+getListTemplate();
		}else if(getModel().getType()==Model.Type.PAGE.ordinal()){
			if(StringUtils.isBlank(getDetailTemplate())){
				return null;
			}
			return "templates/"+site.getMobileTemplate()+"/"+getDetailTemplate();
		}else{
			return null;
		}
	}
}
