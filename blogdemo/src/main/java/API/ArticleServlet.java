package API;

import View.HtmlGenerator;
import model.Article;
import model.ArticleDao;
import model.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ArticleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        //1. 验证用户是否登录，没登录就不能查看要先登录
        //参数为false，则返回null
        HttpSession httpSession = req.getSession(false);
        if(httpSession == null){
            //当前未登录
            String html = HtmlGenerator.getMessagePage("请先登录",
                    "login.html");
            resp.getWriter().write(html);
            return;
        }
        //判断如果当前登陆成功就获取User对象
        User user = (User) httpSession.getAttribute("user");
        //2. 用户已经登录则判断当前请求中是否存在articleId参数
        String articleIdstr = req.getParameter("articleId");
        //1）没有参数则执行获取文章列表的操作
        if(articleIdstr == null){
            //未获取到
            getAllArticle(user,resp);
        }else {
            //2）有参数则执行获取文章详情的操作
            getOneArticle(Integer.parseInt(articleIdstr),user,resp);
        }
    }



    private void getAllArticle(User user, HttpServletResponse resp) throws IOException {
        //1. 查找数据库
        ArticleDao articleDao = new ArticleDao();
        List<Article> articles = articleDao.selectAll();
        //2. 构造页面
        String html = HtmlGenerator.getArticleListPage(articles,user);
        resp.getWriter().write(html);
    }

    private void getOneArticle(int articleId, User user, HttpServletResponse resp) throws IOException {
        //1.查找数据库
        ArticleDao articleDao = new ArticleDao();
        Article article = articleDao.selectById(articleId);
        if(article == null){
            //文章未找到
            String html = HtmlGenerator.getMessagePage("文章不存在","article");
            resp.getWriter().write(html);
            return;
        }
        //2. 根据作者id找到信息并得到其名字
        UserDao userDao = new UserDao();
        User author = userDao.selectById(article.getUserId());
        //3. 构造页面
        String html = HtmlGenerator.getArticleDetailPage(article,user,author);
        resp.getWriter().write(html);
    }

    //实现新增文章
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html; charset=utf-8");
        //1. 判断用户的登陆状态，若尚未登录则提醒登录
        HttpSession httpSession = req.getSession(false);
        if(httpSession == null){
            String html = HtmlGenerator.getMessagePage("尚未登陆","login.html");
            resp.getWriter().write(html);
            return;
        }
        User user = (User) httpSession.getAttribute("user");
        //2. 先从请求中读取浏览器提交的数据（title、content）并进行简单校验
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        if(title == null ||"".equals(title) || content == null || "".equals(content)){
            String html = HtmlGenerator.getMessagePage("您提交的标题或正文为空","article");
            resp.getWriter().write(html);
            return;
        }
        //3. 把数据插入到数据库中
        ArticleDao articleDao = new ArticleDao();
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setUserId(user.getUserId());
        articleDao.add(article);
        //4. 返回一个插入成功的页面
        String html = HtmlGenerator.getMessagePage("发布成功","article");
        resp.getWriter().write(html);
        return;
    }
}
