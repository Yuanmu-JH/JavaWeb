package ServletDemo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;

public class ServletDemo9 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1. 获取session ，若用户没有访问过则创建新的session，若访问过了则获取到之前的session
        //新用户访问，该操作会自动生成一个SessionId，同时创建一个httpSession对象，把这个键值对
        //放在Hash表中，同时把sessionId写回浏览器的cookie中
        //老用户访问，根据请求中Cookie里的SessionId，在hash表中查，找到对应的session对象
        HttpSession httpSession = req.getSession(true);
        //2. 判断是是不是新用户
        Integer count = 1;  //访问次数
        if(httpSession.isNew()){
            //新用户
            //把count值写到session对象中
            httpSession.setAttribute("count",count);
        }else {
            //老用户
            //从httpSession中读取到count值
            count = (Integer)httpSession.getAttribute("count");
            count = count +1;
            //count自增完成后要重新写入Session
            httpSession.setAttribute("count",count);
        }
        //3.返回响应页面
        resp.setContentType("text/html; charset=utf-8");
        Writer writer = resp.getWriter();
        writer.write("<html>");
        writer.write("count: " + count);
        writer.write("</html>");

    }
}
