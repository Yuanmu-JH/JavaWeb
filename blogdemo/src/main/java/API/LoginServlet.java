package API;

import View.HtmlGenerator;
import model.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        //1. 获取到用户名和密码，进行校验
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        if(name == null || "".equals(name) || password == null || "".equals(password)){
            String html = HtmlGenerator.getMessagePage("用户名或密码为空",
                    "login.html");
            resp.getWriter().write(html);
            return;
        }
        //2. 数据库中查找看用户是否存在
        UserDao userDao = new UserDao();
        User user = userDao.selectByName(name);
        //用户不存在或密码不匹配
        if(user == null || !password.equals(user.getPassword())){
            String html = HtmlGenerator.getMessagePage("用户名不存在或密码错误",
                    "login.html");
            resp.getWriter().write(html);
            return;
        }
        //4. 匹配成功则登陆成功，自动创建一个session
        HttpSession httpSession = req.getSession(true);
        //后续再访问服务器时就知道客户端是谁
        httpSession.setAttribute("user",user);
        //5. 返回一个登陆成功的提示页面
        String html = HtmlGenerator.getMessagePage("登陆成功","article");
        resp.getWriter().write(html);
    }
}
