package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

//上传音乐到服务器目录
@WebServlet("/upload")
public class UploadMusicServlet extends HttpServlet {
    //定义路径
   //private final String SAVEPATH="E:\\JavaWeb\\OnlineMusic\\web\\music"; //本地存储路径
    private final String SAVEPATH="/root/apache-tomcat-8.5.58/webapps/OnlineMusic/music/";  //云服务器路径

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        //resp.setContentType("application/json;charset=utf-8");    //没有响应到前端数据，所以可以不写

        //获取用户信息，这里的user对应Login中Session的user
        User user =  (User) req.getSession().getAttribute("user");
        if(user == null){
            System.out.println("请登陆后再上传音乐");
            resp.getWriter().write("<h2>请登陆后再上传音乐</h2>");
            return;
        }else {
            //上传到服务器(固定格式)
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            List<FileItem> fileItems = null;

            try {
                fileItems = upload.parseRequest(req);   //上传的东西从请求体拿过来，现在fileItems中是你要上传的全部内容
            } catch (FileUploadException e) {
                e.printStackTrace();
                return;
            }

            System.out.println("fileItems:"+fileItems);
            FileItem fileItem = fileItems.get(0);
            System.out.println("fileItem:" + fileItem);

            String fileName = fileItem.getName();   //当前上传的文件名
            //这里已经获取了歌手名，可以直接拿到写入session中供后面数据库插入数据使用
            req.getSession().setAttribute("fileName",fileName);

            try {
                fileItem.write(new File(SAVEPATH,fileName));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            //2.上传到数据库
            resp.sendRedirect("uploadsucess.html"); //跳转页面,再从页面找到实现数据库插入
        }

    }
}
