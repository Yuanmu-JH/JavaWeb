import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestMySQL {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/blogdemo?characterEncoding=utf-8&useSSL=true";
        String username = "root";
        String password = "0123";
        // 1. 创建 DataSource 实例. 并设置数据库的相关参数.
        DataSource dataSource = new MysqlDataSource();
        ((MysqlDataSource)dataSource).setURL(url);
        ((MysqlDataSource)dataSource).setUser(username);
        ((MysqlDataSource)dataSource).setPassword(password);
        // 2. 和数据库建立连接
        Connection connection = dataSource.getConnection();
        //3. 访问数据库需要先拼装一个sql语句
        String sql = "select * from user";
        PreparedStatement statement = connection.prepareStatement(sql);
        //4.执行sql
        ResultSet resultSet = statement.executeQuery();
        //5. 遍历结果集
        while (resultSet.next()){
            System.out.println(resultSet.getInt("id"));
            System.out.println(resultSet.getString("name"));
            System.out.println(resultSet.getTime("create_time"));
        }
        //6. 关闭连接 关闭顺序与创建顺序正好相反
        resultSet.close();
        statement.close();
        connection.close();
    }
}
