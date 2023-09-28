package com.cms.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.cms.CommonAttribute;
import com.cms.TableField;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
/**
 * 备份工具类
 *
 */
public class BackupUtils {
    
    /**
     * 获取备份文件
     * 
     */
    public static List<String> getFiles(){
        List<String> backups = new ArrayList<>();
        File backupFile = new File(PathKit.getWebRootPath()+"/"+CommonAttribute.BACK_PATH);
        File[] files = backupFile.listFiles();
        if(files!=null && files.length>0){
            for(File file: files){
                backups.add(file.getName());
            }
        }
        return backups;
    }
    
    /**
     * 删除备份文件
     */
    public static void delete(String backupName){
        File backupFile = new File(PathKit.getWebRootPath()+"/"+CommonAttribute.BACK_PATH+"/"+backupName);
        if(!backupFile.exists()){
            return;
        }
        FileUtils.deleteQuietly(backupFile);
    }

    /**
     * 还原
     */
    public static void restore(String backupName){
        File backupFile = new File(PathKit.getWebRootPath()+"/"+CommonAttribute.BACK_PATH+"/"+backupName);
        if(!backupFile.exists()){
            return;
        }
        try {
            List<String> lines = FileUtils.readLines(backupFile,"UTF-8");
            StringBuilder sql = new StringBuilder();
            for(String line : lines){
                if(line.startsWith("--")){
                    continue;
                }
                sql.append(line);
                if(line.endsWith(";")){
                    Db.update(sql.toString());
                    sql.delete(0, sql.length());
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * 备份
     */
    public static void backup(){
        String backupName = DateFormatUtils.format(new Date(), "yyyy年MM月dd日HH时mm分ss秒_"+RandomStringUtils.randomNumeric(6))+".sql";
        File backupFile = new File(PathKit.getWebRootPath()+"/"+CommonAttribute.BACK_PATH+"/"+backupName);
        StringBuilder sql = new StringBuilder();
        sql.append("SET FOREIGN_KEY_CHECKS=0;\r\n");
        List<String> tables = DbUtils.getTables();
        for(String table : tables){
            sql.append("\r\n-- ----------------------------\r\n");
            sql.append("-- Table structure for `"+table+"`\r\n");
            sql.append("-- ----------------------------\r\n");
            sql.append("DROP TABLE IF EXISTS `"+table+"`;\r\n");
            Object[] tableData = Db.queryFirst("show create table "+table);
            String createTable = String.valueOf(tableData[1]);
            sql.append(createTable+";\r\n");
            sql.append("\r\n-- ----------------------------\r\n");
            sql.append("-- Records of "+table+"\r\n");
            sql.append("-- ----------------------------\r\n");
            List<TableField> tableFields = DbUtils.getTableFields(table);
            StringBuilder insertColumn = new StringBuilder();
            StringBuilder selectColumn = new StringBuilder();
            Map<Integer,String> columnMap = new HashMap<>();
            for(int i=0;i<tableFields.size();i++){
            	TableField tableField = tableFields.get(i);
                String field = String.valueOf(tableField.getName());
                insertColumn.append(",`"+field+"`");
                selectColumn.append(","+field);
                columnMap.put(i, String.valueOf(tableField.getType()));
            }
            StringBuilder insertSql = new StringBuilder();
            String insertBeforeSql = "INSERT INTO `"+table+"` ("+insertColumn.substring(1).toString()+")";
            List<Object[]> resultDatas = Db.query("select "+selectColumn.substring(1).toString()+" from "+table);
            for(Object[] resultData : resultDatas){
                StringBuilder resultSql = new StringBuilder();
                for(int i=0;i<resultData.length;i++){
                    Object result = resultData[i];
                    if(result!=null){
                        String columnType = columnMap.get(i);
                        if("date".equals(columnType)){
                            resultSql.append(",'"+DateFormatUtils.format((Date)result, "yyyy-MM-dd")+"'");
                        }else if("datetime".equals(columnType)){
                            resultSql.append(",'"+DateFormatUtils.format((Date)result, "yyyy-MM-dd HH:mm:ss")+"'");
                        }else if("bit".equals(columnType)){
                            resultSql.append(","+BooleanUtils.toInteger((Boolean)result));
                        }else{
                            resultSql.append(",'"+String.valueOf(result).replace("\r", "\\r").replace("\n", "\\n").replace("\"", "\\\"").replace("\'", "\\\'")+"'");
                        }
                    }else{
                        resultSql.append(",null");
                    }
                }
                insertSql.append(insertBeforeSql+" values("+resultSql.substring(1).toString()+");\r\n");
            }
            sql.append(insertSql.toString());
        }
        try {
            FileUtils.write(backupFile, sql.toString(),"UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
