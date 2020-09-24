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

//多选删除
@WebServlet("/deleteSelMusicServlet")
public class DeleteSelMusicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        //list.html前端返回的是一个id数组,values数组中存放的是所有需要删除歌曲的id
        String[] values = req.getParameterValues("id[]");
        MusicDao musicDao = new MusicDao();
        int sum = 0;
        Map<String,Object> return_map = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            int id = Integer.parseInt(values[i]);
            //先找有没有这首歌
            Music music = musicDao.findMusicById(id);

            int del = musicDao.deleteMusicById(id);
            if(del == 1){
                //File file = new File("E:\\JavaWeb\\OnlineMusic\\web\\"+music.getUrl()+".mp3");    //本地URL
                File file = new File("/root/apache-tomcat-8.5.58/webapps/OnlineMusic/"+music.getUrl()+".mp3");   //云服务器Url

                if (file.delete()){
                    //成功删除一个则加一个，计数，依据这个判断是否删除完毕
                    sum += del;

                }else {
                  return_map.put("msg",false);
                  System.out.println("服务器删除失败");
                }
            }else {
                return_map.put("msg",false);
                System.out.println("数据库删除失败");
            }
        }
        if(sum == values.length){
            //说明数组中的每个数据都被删除
            return_map.put("msg",true);
        }else {
            return_map.put("msg",false);
        }
        //将结果返回前端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);
    }
}
