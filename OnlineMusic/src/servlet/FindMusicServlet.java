package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import entity.Music;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/findMusic")
public class FindMusicServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        String musicName = req.getParameter("musicName");

        MusicDao musicDao = new MusicDao();
        List<Music> musicList = new ArrayList<>();

        if(musicName != null){
            //模糊查询
           musicList = musicDao.ifMusic(musicName);
        }else {
            //查询全部
            musicList = musicDao.findMusic();
            }
        //将MusicList的数据返回给前端

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),musicList);

    }
}
