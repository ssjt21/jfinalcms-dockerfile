package cms;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.jfinal.plugin.druid.DruidPlugin;

/**
 * @description : 
 * @author : heyewei
 * @create : 2020年9月14日
 **/
public class SqlTrans {

	public static DataSource getDataSource() {
		DruidPlugin druidPlugin = new DruidPlugin(
				"jdbc:mysql://localhost:3308/moban8?useUnicode=true&characterEncoding=UTF-8", 
				"root", 
				"root",
				"com.mysql.jdbc.Driver");
		druidPlugin.start();
		return druidPlugin.getDataSource();
	}
	
	
	public static void main(String[] args) throws SQLException {
		DataSource dataSource = getDataSource();
		Connection connection = dataSource.getConnection();
		Statement statement = connection.createStatement();
		statement.execute("select catid,catid from qing_category order by catid asc");
	}
}
