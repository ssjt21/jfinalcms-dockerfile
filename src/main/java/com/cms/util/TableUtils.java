package com.cms.util;

import com.jfinal.plugin.activerecord.Db;

public class TableUtils {

    public static void create(String table){
    	Db.update("SET FOREIGN_KEY_CHECKS=0;");
    	Db.update("DROP TABLE IF EXISTS `"+table+"`;");
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE `"+table+"` (\r\n");
        sql.append("`id` int(11) NOT NULL AUTO_INCREMENT,\r\n");
        sql.append("`createDate` datetime NOT NULL,\r\n");
        sql.append("`updateDate` datetime NOT NULL,\r\n");
        sql.append("PRIMARY KEY (`id`)\r\n");
        sql.append(") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;\r\n");
        Db.update(sql.toString());
    }
    
    
    public static void delete(String table){
    	Db.update("SET FOREIGN_KEY_CHECKS=0;");
        StringBuilder sql = new StringBuilder();
        sql.append("DROP TABLE IF EXISTS `"+table+"`;\r\n");
        Db.update(sql.toString());
    }
}
