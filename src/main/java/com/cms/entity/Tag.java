package com.cms.entity;

import com.cms.entity.base.BaseTag;
import com.cms.util.DbUtils;
import com.jfinal.core.JFinal;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Entity - 标签
 *
 *
 *
 */
@SuppressWarnings("serial")
public class Tag extends BaseTag<Tag> {

	/**
	 * 根据URL目录查找标签
	 *
	 * @param cat
	 *            URL目录
	 * @return 标签，若不存在则返回null
	 */
	public Tag findByCat(String cat) {
		if (StrKit.isBlank(cat)) {
			return null;
		}
		return findFirst("select * from cms_tag where cat = ?",cat);
	}


	/**
	 * 根据名称查找标签
	 *
	 * @param name
	 *            标签名称
	 * @return 标签，若不存在则返回null
	 */
	public Tag findByName(String name) {
		if (StrKit.isBlank(name)) {
			return null;
		}
		return findFirst("select * from cms_tag where name = ?",name);
	}

	/**
	 * 查找标签分页
	 *
	 * @param pageNumber
	 *            页码
	 * @param pageSize
	 *            每页记录数
	 * @param siteId
	 *            站点ID
	 * @return 友情链接分页
	 */
	public Page<Tag> findPage(Integer pageNumber, Integer pageSize, Integer siteId){
		String filterSql = " and siteId="+siteId;
		String orderBySql = DbUtils.getOrderBySql("createDate desc");
		return paginate(pageNumber, pageSize, "select *", "from cms_tag where 1=1 "+filterSql+orderBySql);
	}

	/**
	 * 查找标签
	 *
	 * @param orderBy
	 *            排序
	 * @param start
	 *            起始位置
	 * @param count
	 *            数量
	 * @return 标签
	 */
	public List<Tag> findList(String condition, String orderBy, Integer start, Integer count,Integer siteId){
		String filterSql = " and siteId="+siteId;
		if(StringUtils.isNotBlank(condition)){
			filterSql+=" and "+condition;
		}
		String orderBySql = "";
		if(StringUtils.isNotBlank(orderBy)){
			orderBySql = DbUtils.getOrderBySql(orderBy);
		}else{
			orderBySql = DbUtils.getOrderBySql("createDate desc");
		}
		String countSql= DbUtils.getCountSql(start, count);
		return find("select * from cms_tag where 1=1 "+filterSql+orderBySql+countSql);
	}

	/**
	 * 获取路径
	 *
	 * @return 路径
	 */
	public String getPath() {
		String url = JFinal.me().getContextPath();
		String param = "";
		Site site = new Site().dao().findById(getSiteId());
		String siteCat = site.getCat();
		if(StringUtils.isNotBlank(siteCat)){
			url+="/"+siteCat;
		}
		String cat = getCat();
		if(StringUtils.isNotBlank(cat)){
			url+="/tag/"+cat;
		}else{
			url+="/tag";
			param+="&id="+getId();
		}
		if(StringUtils.isNotBlank(param)){
			param = "?" +param.substring(1);
		}
		return url+param;
	}

}
