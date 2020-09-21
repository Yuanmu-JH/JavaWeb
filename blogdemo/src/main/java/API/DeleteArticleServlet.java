package API;

import View.HtmlGenerator;
import model.Article;
import model.ArticleDao;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DeleteArticleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        //1. 验证用户登陆状态，未登录则不可删除
        HttpSession httpSession = req.getSession(false);
        if(httpSession == null){
            String html = HtmlGenerator.getMessagePage("尚未登录","login.html");
            resp.getWriter().write(html);
            return;
        }
        User user = (User) httpSession.getAttribute("user");
        //2. 读取请求内容，获取到要删除的文章id
        String artcleIdStr = req.getParameter("articleId");
        if(artcleIdStr == null || "".equals(artcleIdStr)){
            String html = HtmlGenerator.getMessagePage("要删除的文章id有误","article");
            resp.getWriter().write(html);
            return;
        }
        //3. 根据文章id 查找到该文章的作者，若当前用户是作者才能删除，否则不可删除
        ArticleDao articleDao = new ArticleDao();
        Article article = articleDao.selectById(Integer.parseInt(artcleIdStr));
        if(article.getUserId() != user.getUserId()){
            String html = HtmlGenerator.getMessagePage("你不可以删除别人的文章哦！",
                    "article");
            resp.getWriter().write(html);
            return;
        }
        //4. 执行数据库删除操作
        articleDao.delete(Integer.parseInt(artcleIdStr));
        //5. 返回一个“删除成功”的页面
        String html = HtmlGenerator.getMessagePage("删除成功！",
                "article");
        resp.getWriter().write(html);

    }
}
