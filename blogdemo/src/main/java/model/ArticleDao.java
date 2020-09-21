package model;

import com.sun.org.apache.bcel.internal.generic.DUP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleDao {
    //1. 新增文章
    public void add(Article article){
        //1. 获取数据库连接
        Connection connection = DBUtil.getConnection();
        //2. 拼接sql
        String sql = "insert into article values (null, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            //这里失败则说明是sql语句构造有错
            statement = connection.prepareStatement(sql);
            statement.setString(1,article.getTitle());
            statement.setString(2,article.getContent());
            statement.setInt(3,article.getUserId());
            //3.执行sql
            int ret = statement.executeUpdate();
            if(ret != 1){
                System.out.println("执行插入文章操作失败");
                return;
            }
            System.out.println("执行插入文章操作成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //4.释放数据库连接
            DBUtil.close(connection,statement,null);
        }
    }

    //2. 查看文章列表（把所有文章的信息都查出来）
    public List<Article> selectAll(){
        List<Article> articles = new ArrayList<>();
        //1. 建立连接
        Connection connection = DBUtil.getConnection();
        //2. 拼装sql
        String sql = "select articleId,title,userId from article";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            //3. 执行sql
            resultSet = statement.executeQuery();
            //4. 遍历结果集
            while(resultSet.next()){
                //针对每个结果记录都构造一个article对象
                //没有从数据库中读取content字段，所以该字段暂时先不设置
                Article article = new Article();
                article.setArticleId(resultSet.getInt("articleId"));
                article.setTitle(resultSet.getString("title"));
                article.setUserId(resultSet.getInt("userId"));
                articles.add(article);
            }
            return articles;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //5. 释放连接
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    //3. 查看指定文章详情（需要查正文）
    public Article selectById(int articleId){
        //1. 建立数据库连接
        Connection connection = DBUtil.getConnection();
        //2. 构造SQL
        String sql = "select * from article where articleId = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1,articleId);
            //3.执行sql语句
            resultSet = statement.executeQuery();
            //4. 遍历结果集
            //因为id是主键不会重复，预期最多只能查出一条记录,所以用if
            if(resultSet.next()){
                Article article = new Article();
                article.setArticleId(resultSet.getInt("articleId"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setUserId(resultSet.getInt("userId"));
                return article;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //5.释放连接
            DBUtil.close(connection,statement,resultSet);
        }
        return null;
    }

    //4. 删除指定文章（根据文章id来删除）
    public void delete(int articleId) {
        // 1. 获取连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装 SQL
        String sql = "delete from article where articleId = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, articleId);
            // 3. 执行 SQL
            int ret = statement.executeUpdate();
            if (ret != 1) {
                System.out.println("删除文章失败");
                return;
            }
            System.out.println("删除文章成功");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 4. 释放连接.
            DBUtil.close(connection, statement, null);
        }
    }

    //单元测试模块
    public static void main(String[] args) {
        ArticleDao articleDao = new ArticleDao();
        //1. 测试新增文章
        Article article = new Article();
        article.setTitle("这是标题");
        article.setContent("这里是正文哦但是我又不知道要写点啥那就希望这个程序可以运行成功然后顺利顺利顺利顺利顺利顺利顺利顺利！");
        article.setUserId(1);
        articleDao.add(article);
        //2. 查看文章列表
//        List<Article> articles = articleDao.selectAll();
//        System.out.println(articles);
        //3. 查看指定文章内容
//        Article article = articleDao.selectById(1);
//        System.out.println(article);
        //4. 删除文章
//        articleDao.delete(1);
    }
}
