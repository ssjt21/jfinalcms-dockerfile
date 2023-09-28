package com.cms.util;

import java.util.ArrayList;
import java.util.List;

import com.cms.TableField;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

public class DbUtils {
	
	/**
	 * 获取限量Sql
	 * 
	 * @param start
	 *            起始位置
	 * @param count
	 *            数量
	 * @return	限量Sql
	 */
	public static String getCountSql(Integer start,Integer count){
		String countSql="";
		if(count != null){
			if(start !=null){
				countSql=" limit "+start+","+count;
			}else{
				countSql=" limit "+count;
			}
		}
		return countSql;
	}
	
	/**
     * 获取排序Sql
     * 
     * @param orderBy
     *          排序
     * @return  排序Sql
     */
    public static String getOrderBySql(String orderBy){
        String orderBySql="";
        if(StrKit.notBlank(orderBy)){
            orderBySql = " order by "+orderBy;
        }
        return orderBySql;
    }
    
    /**
     * 获取数据库表
     * 
     * @return  数据库表
     */
    public static List<String> getTables(){
    	List<String> tables = Db.query("show tables");
    	return tables;
    }
    
    /**
     * 获取数据库表属性
     * 
     * @return  数据库表属性
     */
    public static List<TableField> getTableFields(String table){
    	List<TableField> tableFields = new ArrayList<TableField>();
    	List<Object[]> columnDatas = Db.query("show full columns from "+table);
    	 for(int i=0;i<columnDatas.size();i++){
    		 TableField tableField = new TableField();
             Object[] columnData = columnDatas.get(i);
             String name = String.valueOf(columnData[0]);
             String type = String.valueOf(columnData[1]);
             String isRequired = String.valueOf(columnData[3]);
             String comment = String.valueOf(columnData[8]);
             
             tableField.setName(name);
             tableField.setComment(comment);
             if(type.equals("date")){
            	 tableField.setType("date");
             }else if(type.equals("datetime")){
            	 tableField.setType("datetime");
             }else if(type.equals("text")){
            	 tableField.setType("text");
             }else if(type.equals("longtext")){
            	 tableField.setType("longtext");
             }else if(type.startsWith("int(")){
            	 tableField.setType("int");
             }else if(type.startsWith("bigint(")){
            	 tableField.setType("bigint");
             }else if(type.startsWith("varchar(")){
            	 tableField.setType("varchar");
             }else if(type.startsWith("bit(")){
            	 tableField.setType("bit");
             }
             if("NO".equals(isRequired)){
            	 tableField.setIsRequired(true);
             }else if("YES".equals(isRequired)){
            	 tableField.setIsRequired(false);
             }
             tableFields.add(tableField);
         }
    	 return tableFields;
    }
}
