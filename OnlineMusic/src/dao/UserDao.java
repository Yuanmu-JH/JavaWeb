package dao;

import entity.User;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//有关用户的数据库操作
public class UserDao {
    public  User login(User loginUser)  {
        User user = null;
        //获取连接
        Connection connection = null;
        //预编译sql语句
        PreparedStatement statement = null;
        //从数据库拿到的结果集合
        ResultSet resultSet = null;


        try {
            String sql = "select * from user where username=? and password=?";
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);   //sql语句的预编译
            statement.setString(1,loginUser.getUsername());
            statement.setString(2,loginUser.getPassword());

            //执行sql语句
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                //如果next为真则说明查询到了,并且查询到的只有一个所以是if
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setAge(resultSet.getInt("age"));
                user.setGender(resultSet.getString("gender"));
                user.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            //这里出错是sql语句的问题
            e.printStackTrace();
        }finally {
            DBUtils.getclose(connection,statement,resultSet);
        }
        return user;
    }

    //注册
    public void register(User user){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement("insert into user values(null,?,?,?,?,?)");
            statement.setString(1,user.getUsername());
            statement.setString(2,user.getPassword());
            statement.setString(3,user.getGender());
            statement.setInt(4,user.getAge());
            statement.setString(5,user.getEmail());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }finally {
            DBUtils.getclose(connection,statement,null);
        }
    }

}
