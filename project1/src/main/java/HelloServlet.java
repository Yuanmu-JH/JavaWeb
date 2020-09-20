import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet extends HttpServlet {
    @Override
    //参数对应http请求与http响应
    /*
    服务器处理请求的典型过程：①读取请求并解析（反序列化） ②根据请求计算响应 ③把响应写回给客户端（序列化）
    其中①和③由servlet处理好了
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //请求无论是啥响应固定返回hello world
        resp.getWriter().write("<h1>hello servlet</h1>");  //将一个字符串写入HTTP响应的body中
    }
}
