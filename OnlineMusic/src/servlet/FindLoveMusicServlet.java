package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import entity.Music;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/findLoveMusic")
public class FindLoveMusicServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        String loveMusicName = req.getParameter("loveMusicName");

        MusicDao musicDao = new MusicDao();
        //从session中获取该用户
        User user = (User)req.getSession().getAttribute("user");
        int user_id = user.getId();

        List<Music> musicList = new ArrayList<>();

        if(loveMusicName != null){
            //模糊查询
            musicList = musicDao.ifMusicLove(loveMusicName,user_id);
        }else {
            //所有喜欢的音乐
            musicList = musicDao.findLoveMusic(user_id);
        }
        //将结果返回前端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),musicList);
    }
}
