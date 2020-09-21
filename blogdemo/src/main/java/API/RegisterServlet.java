package API;

import View.HtmlGenerator;
import model.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//实现注册

public class RegisterServlet extends HttpServlet {
    //浏览器是通过POST的方法提交注册信息给服务器，所以重写doPose
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        //1. 获取前端提交的数据（用户名、密码），并校验是否合法
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        if(name == null || "".equals(name) || password == null || "".equals(password)){
            //用户提交的数据有误，返回错误页面（html构成的字符串）
            String html = HtmlGenerator.getMessagePage("用户名或者密码为空",
                    "register.html");
            resp.getWriter().write(html);
            return;
        }
        //2. 在数据库中查找看当前用户是否存在，若存在则注册失败
        UserDao userDao = new UserDao();
        User existUser = userDao.selectByName(name);
        if (existUser != null){
            //如果existUser非空则说明该用户存在，提示注册失败
            String html = HtmlGenerator.getMessagePage("用户名重复啦！重新想一个吧~",
                    "register.html");
            resp.getWriter().write(html);
            return;
        }
        //3. 根据前端提交的数据构造User对象并插入到数据库中
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        userDao.add(user);
        //4. 返回一个结果页面，提示当前注册成功
        String html = HtmlGenerator.getMessagePage("注册成功！点击登录","login.html");
        resp.getWriter().write(html);
    }
}
