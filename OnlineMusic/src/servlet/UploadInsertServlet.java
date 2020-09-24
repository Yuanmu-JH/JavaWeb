package servlet;

import dao.MusicDao;
import entity.Music;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//将音乐信息写到数据库中
@WebServlet("/uploadsucess")
public class UploadInsertServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        String singer = req.getParameter("singer"); //获取歌手名
        //从session中取出文件名
        String fileName = (String)req.getSession().getAttribute("fileName");    //req返回的是Object，所以需要类型转换
        //这里的fileName是歌名.mp3，我们不需要所以要去掉.mp3
        String[] strings = fileName.split("\\.");  //分隔完成为数字
        String title =strings[0];    //0号下标对应的歌名
        //获取时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());   //拿到当前日期并进行格式化

        //获取UserId还是再session中获取
        User user = (User)req.getSession().getAttribute("user");
        int user_id = user.getId();

        String url = "music\\"+title;

        MusicDao musicDao = new MusicDao();
        int ret = musicDao.insert(title,singer,time,url,user_id);
        if(ret == 1){
            resp.sendRedirect("list.html"); //插入成功跳转页面
        }
    }

}
