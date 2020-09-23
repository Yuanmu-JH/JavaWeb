package util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.*;

//进行数据库操作,固定格式
public class DBUtils {

    private static String url = "jdbc:mysql://127.0.0.1:3306/" +
            "musicserver?useSSL=false";
    private static String password = "0123";
    private static String username = "root";

    private  static volatile DataSource DATASOURCE;

    //获取连接
    private static DataSource getDataSource(){
        //双重校验锁
        if(DATASOURCE == null){
            synchronized (DBUtils.class){
                if(DATASOURCE == null){
                    DATASOURCE = new MysqlDataSource();
                    ((MysqlDataSource)DATASOURCE).setURL(url);
                    ((MysqlDataSource)DATASOURCE).setUser(username);
                    ((MysqlDataSource)DATASOURCE).setPassword(password);
                }
            }
        }
        return DATASOURCE;
    }

    public static Connection getConnection() {
        //从池子里获取连接
        try {
            Connection connection = getDataSource().getConnection();
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("数据库连接失败");
        }
    }

    public static void getclose(Connection connection,PreparedStatement statement,ResultSet resultSet){
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    }
