package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    //1. 注册（新增用户）
    //把一个User对象插入到数据库中
    public void add(User user){
        //1. 获取到数据库连接
        Connection connection = DBUtil.getConnection();
        //2. 拼装SQL语句
        //用户名和密码要动态拼接所以先？
        String sql = "insert into user values (null,?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            //填写？
            statement.setString(1,user.getName());
            statement.setString(2,user.getPassword());
            //3. 执行SQL语句
            int ret = statement.executeUpdate();    //执行更新，给服务器发送请求
            if(ret != 1){
                System.out.println("插入新用户失败");
                return;
            }
            System.out.println("插入新用户成功");

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //4. 释放数据库连接
            DBUtil.close(connection,statement,null);
        }


    }

    //2. 登录（按照名字查找用户）
    public User selectByName(String name){
        // 1.和数据库建立连接
         Connection connection = DBUtil.getConnection();
        //2. 拼装sql语句
        String sql = "select * from user where name = ?";
        PreparedStatement statement = null;

        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1,name);
            //3. 执行sql
            resultSet = statement.executeQuery();
            //4. 遍历结果集合,因为预期name在数据库中不能重复，所以在此处查找最多只能查出一条记录所以是if
            if(resultSet.next()){
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //5. 释放连接
            DBUtil.close(connection,statement,resultSet);
        }
        return null;
    }

    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        //1.先测试add方法
//        User user = new User();
//        user.setName("ym");
//        user.setPassword("0123");
//        userDao.add(user);
        //2. 测试 selectByName
        User user = userDao.selectByName("ym");
        System.out.println(user);
    }

    //根据用户id找到信息
    public User selectById(int userId) {
        // 1.和数据库建立连接
        Connection connection = DBUtil.getConnection();
        //2. 拼装sql语句
        String sql = "select * from user where userId = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1,userId);
            //3. 执行sql
            resultSet = statement.executeQuery();
            //4. 遍历结果集合,因为预期name在数据库中不能重复，所以在此处查找最多只能查出一条记录所以是if
            if(resultSet.next()){
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //5. 释放连接
            DBUtil.close(connection,statement,resultSet);
        }
        return null;
    }
}
