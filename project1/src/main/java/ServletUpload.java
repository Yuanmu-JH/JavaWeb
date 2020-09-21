import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

//实现上传
@MultipartConfig
public class ServletUpload  extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        //收到图片就把图片保存到指定目录中
        String basePath = "E:/JavaWeb/project1/images/";
        //类似于getParameter，参数里传入一个key值，来获取part对象，即上传的文件内容
        Part image = req.getPart("image");
        //这个方法得到上传的文件名
        String path = basePath + image.getSubmittedFileName();
        //写入磁盘路径
        image.write(path);
        //存完返回一个页面
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().write("图片上传成功");
    }
}
