package com.cms.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.cms.entity.base.BaseNav;
import com.cms.util.DbUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;

import java.util.*;

/**
 * Entity - 导航
 *
 *
 *
 */
@SuppressWarnings("serial")
public class Nav extends BaseNav<Nav> {

    /** 树路径分隔符 */
    public static final String TREE_PATH_SEPARATOR = ",";

    /**
     * 下级导航
     */
    @JSONField(serialize=false)
    private List<Nav> children;

    /**
     * 上级导航
     */
    @JSONField(serialize=false)
    private Nav parent;


    /**
     * 获取下级导航
     *
     * @return 下级导航
     */
    public List<Nav> getChildren() {
        if(children == null){
            children = find("select * from cms_nav where parentId=? order by sort desc",getId());
        }
        return children;
    }

    /**
     * 获取上级导航
     * @return	上级导航
     */
    public Nav getParent(){
        if(parent == null){
            parent = findById(getParentId());
        }
        return parent;
    }



    /**
     * 查找顶级导航
     *
     * @param start
     *            起始位置
     * @param count
     *            数量
     * @return 顶级导航
     */
    public List<Nav> findRoots(Integer start, Integer count,Integer siteId){
        String filterSql = " and siteId="+siteId;
        String countSql= DbUtils.getCountSql(start, count);
        String orderBySql = DbUtils.getOrderBySql("sort desc");
        return find("select * from cms_nav where parentId is null"+filterSql+orderBySql+countSql);
    }


    /**
     * 查找上级导航
     *
     * @param navId
     *            导航Id
     * @param recursive
     *            是否递归
     * @param start
     *            起始位置
     * @param count
     *            数量
     * @return 上级导航
     */
    public List<Nav> findParents(Integer navId,Boolean recursive,Integer start, Integer count,Integer siteId){
        Nav nav = findById(navId);
        if(navId == null || nav.getParentId() == null){
            return Collections.emptyList();
        }
        String filterSql = " and siteId="+siteId;
        if(recursive){
            String countSql=DbUtils.getCountSql(start, count);
            String orderBySql = DbUtils.getOrderBySql("grade asc");
            return find("select * from cms_nav where id in ("+ StringUtils.join(nav.getParentIds(), ",")+") "+filterSql+orderBySql+countSql);
        }else{
            return find("select * from cms_nav where id = ? "+filterSql,findById(navId).getParentId());
        }
    }


    /**
     * 查找下级导航
     *
     * @param navId
     *            导航Id
     * @param recursive
     *            是否递归
     * @param start
     *            起始位置
     * @param count
     *            数量
     * @return 下级导航
     */
    public List<Nav> findChildren(Integer navId,Boolean recursive,Integer start,Integer count,Integer siteId){
        String filterSql = " and siteId="+siteId;
        if(recursive){
            String countSql=DbUtils.getCountSql(start, count);
            String orderBySql = DbUtils.getOrderBySql("grade asc,sort desc");
            List<Nav> navs;
            if(navId!=null){
                navs = find("select * from cms_nav where 1=1 and treePath like ? "+filterSql+orderBySql+countSql,"%,"+navId+",%");
            }else{
                navs = find("select * from cms_nav where 1=1 "+filterSql+orderBySql+countSql);
            }
            sort(navs);
            return navs;
        }else{
            String orderBySql = DbUtils.getOrderBySql("sort desc");
            return find("select * from cms_nav where parentId = ? "+filterSql+orderBySql,navId);
        }
    }

    /**
     * 查找导航树
     *
     * @return 导航树
     */
    public List<Nav> findTree(Integer siteId){
        return findChildren(null,true,null,null,siteId);
    }


    /**
     * 获取所有上级导航ID
     *
     * @return 所有上级导航ID
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
     * 排序导航
     *
     * @param navs
     *            分类
     */
    private void sort(List<Nav> navs) {
        if(navs == null || navs.size()==0) {
            return;
        }
        final Map<Integer, Integer> sortMap = new HashMap<Integer, Integer>();
        for (Nav nav : navs) {
            sortMap.put(nav.getId(), nav.getSort());
        }
        Collections.sort(navs, new Comparator<Nav>() {
            @Override
            public int compare(Nav nav1, Nav nav2) {
                Integer[] ids1 = (Integer[]) ArrayUtils.add(nav1.getParentIds(), nav1.getId());
                Integer[] ids2 = (Integer[]) ArrayUtils.add(nav2.getParentIds(), nav2.getId());
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
                        compareToBuilder.append(nav1.getGrade(), nav2.getGrade());
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
}
