package ServletDemo;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//服务器给浏览器写回cookie
public class ServletDemo7 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.先构造cookie对象，每个cookie对象就是一个键值对
        Cookie userName = new Cookie("userName","ym");
        Cookie age = new Cookie("age",18+""); //要把int18转为字符串
        //2. 将cookie写入响应中
        resp.addCookie(userName);
        resp.addCookie(age);
        //3. 创建一个响应报文
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().write("返回cookie成功");
    }
}
