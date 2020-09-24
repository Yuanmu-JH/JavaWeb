package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import entity.Music;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/deleteServlet")
public class DeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        //获得id并转为整型
        String idStr = req.getParameter("id");
        int id = Integer.parseInt(idStr);
        //删除音乐操作
        MusicDao musicDao = new MusicDao();
        //①先判断这首是否存在
        Music music = musicDao.findMusicById(id);
        if(music == null){
            //该音乐不存在
            return;
        }
        //存在，执行删除，返回值为影响的行数
        int del = musicDao.deleteMusicById(id);
        Map<String,Object> return_map = new HashMap<>();
        if(del == 1){
            //仅代表删除了数据库成功，但是不能代表删除了服务器的音乐
           //File file = new File("E:\\JavaWeb\\OnlineMusic\\web\\"+music.getUrl()+".mp3");    //本地url
            File file = new File("/root/apache-tomcat-8.5.58/webapps/OnlineMusic/"+music.getUrl()+".mp3");   //云服务器Url

            if (file.delete()){
                //删除成功返回msg
                return_map.put("msg",true);
                System.out.println("服务器删除成功");
            }else {
                return_map.put("msg",false);
                System.out.println("服务器删除失败");
            }
        }else {
            //删除失败
            return_map.put("msg",false);
            System.out.println("数据库删除失败");
        }
        //写回给前端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);

    }
}
