package com.cms.util;

import com.jfinal.plugin.activerecord.Db;

public class FieldUtils {
    
    public static void add(String table,String fieldName){
        StringBuilder sql = new StringBuilder();
        sql.append("alter table "+table+" add "+fieldName+" "+"varchar(255)"+" DEFAULT NULL;");
        Db.update(sql.toString());
    }
    
    public static void update(String table,String oldFieldName,String newFieldName){
        StringBuilder sql = new StringBuilder();
        sql.append("alter table "+table+" change "+oldFieldName+" "+newFieldName+" "+"varchar(255)"+" DEFAULT NULL;");
        Db.update(sql.toString());
    }
    
    public static void delete(String table,String fieldName){
        StringBuilder sql = new StringBuilder();
        sql.append("alter table "+table+" drop column "+fieldName+";");
        Db.update(sql.toString());
    }

}
