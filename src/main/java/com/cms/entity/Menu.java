package com.cms.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cms.entity.base.BaseMenu;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;

import com.alibaba.fastjson.annotation.JSONField;
import com.cms.util.DbUtils;

/**
 * Entity - 菜单
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Menu extends BaseMenu<Menu> {
	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";
	
	/**
	 * 类型
	 */
	public enum Type{
		MENU("菜单"),
		PAGE("页面");
		public String text;
		Type(String text){
			this.text = text;
		}
		public String getText(){
			return this.text;
		}
		public static Map<Integer, Menu.Type> typeValueMap = new HashMap<>();
		static {
			Menu.Type[] values = Menu.Type.values();
			for (Menu.Type type : values) {
				typeValueMap.put(type.ordinal(), type);
			}
		}
	}


	public String getTypeName(){
		return Type.typeValueMap.get(getType()).getText();
	}
    
    
	/**
	 * 下级菜单
	 */
	@JSONField(serialize=false)  
	private List<Menu> children;
	
	/**
	 * 上级菜单
	 */
	@JSONField(serialize=false)  
	private Menu parent;
	
	/**
	 * 获取下级菜单地址
	 * 
	 * @return 下级菜单地址
	 */
	public List<String> getChildrenUrls(){
		List<String> urls = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(getChildren())){
			for(Menu menu : getChildren()){
				urls.add(menu.getUrl());
			}
		}
		return urls;
	}
	
	/**
	 * 获取下级菜单
	 * 
	 * @return 下级菜单
	 */
	public List<Menu> getChildren() {
	    if(children == null){
	        children = find("select * from cms_menu where parentId=? order by sort desc",getId());
	    }
		return children;
	}
	
	/**
	 * 获取上级菜单
	 * @return	上级菜单
	 */
	public Menu getParent(){
	    if(parent == null){
	        parent = findById(getParentId());
	    }
		return parent;
	}
	
	
	/**
	 * 查找顶级菜单
	 * 
	 * @return 顶级菜单
	 */
	public List<Menu> findRoots(){
		String orderBySql = DbUtils.getOrderBySql("sort desc");
		return find("select * from cms_menu where parentId is null and isShow=1 "+orderBySql);
	}
	
	
	/**
	 * 查找下级菜单
	 * 
	 * @param menuId
	 *            菜单Id
	 * @param isShow
	 *            是否显示
	 * @param recursive
	 *            是否递归
	 *            
	 * @return 下级菜单
	 */
	public List<Menu> findChildren(Integer menuId,Boolean isShow,Boolean recursive){
	    String filterSql = "";
		if(isShow!=null){
		    filterSql+= " and isShow="+BooleanUtils.toInteger(isShow);
		}
		if(recursive){
			String orderBySql = DbUtils.getOrderBySql("grade asc,sort desc");
			List<Menu> menus;
			if(menuId!=null){
				menus = find("select * from cms_menu where 1=1 and treePath like ? "+filterSql+orderBySql,"%,"+menuId+",%");
			}else{
				menus = find("select * from cms_menu where 1=1 "+filterSql+orderBySql);
			}
			sort(menus);
			return menus;
		}else{
		    String orderBySql = DbUtils.getOrderBySql("sort desc");
			return find("select * from cms_menu where parentId = ? "+filterSql+orderBySql,menuId);
		}
	}
	
	/**
	 * 查找菜单树
	 * 
	 * @return 菜单树
	 */
	public List<Menu> findTree(){
		return findChildren(null,null,true);
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
	 * 排序菜单
	 * 
	 * @param menus
	 *           菜单
	 */
	private void sort(List<Menu> menus) {
		if(menus == null || menus.size()==0) {
			return;
		}
		final Map<Integer, Integer> sortMap = new HashMap<Integer, Integer>();
		for (Menu menu : menus) {
		    sortMap.put(menu.getId(), menu.getSort());
		}
		Collections.sort(menus, new Comparator<Menu>() {
			@Override
			public int compare(Menu menu1, Menu menu2) {
				Integer[] ids1 = (Integer[]) ArrayUtils.add(menu1.getParentIds(), menu1.getId());
				Integer[] ids2 = (Integer[]) ArrayUtils.add(menu2.getParentIds(), menu2.getId());
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
						compareToBuilder.append(menu1.getGrade(), menu2.getGrade());
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
			setTreePath(getParent().getTreePath() + getParent().getId() + Category.TREE_PATH_SEPARATOR);
		} else {
			setTreePath(Category.TREE_PATH_SEPARATOR);
		}
		setGrade(getParentIds().length);
	}
	
}
