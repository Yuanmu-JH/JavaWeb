package ServletDemo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class ServletDemo2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理表单提交的数据
        String firstName = req.getParameter("firstName");
        String secondName = req.getParameter("secondName");

        //构造相应页面
        resp.setContentType("text/html;charset=utf-8"); //这句一定要在Write的前面
        Writer writer = resp.getWriter();
        writer.write("<html>");
        writer.write("姓氏：" +firstName);
        writer.write("<br>");
        writer.write("名字："+secondName);
        writer.write("</html>");
    }
}
