package ServletDemo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

//返回一个页面 每秒自动刷新一次
public class ServletDemo4 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setIntHeader("Refresh",1);
        //返回的页面中填写当前时间
        /*
        获取当前时间：
        System.currentTimeMillis()
        new Date()
        new Calender
         */
        Date date = new Date();
        Writer writer = resp.getWriter();
        writer.write("<html>");
        writer.write(date.toString());  //格式化时间日期
        writer.write("</html>");
    }
}
