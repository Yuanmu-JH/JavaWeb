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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/loveMusicServlet")
public class LoveMusicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        //获得musicId
        String idStr = req.getParameter("id");
        int musicId = Integer.parseInt(idStr);
        //获取UserId还是再session中获取
        User user = (User)req.getSession().getAttribute("user");
        int user_id = user.getId();

        MusicDao musicDao = new MusicDao();
        Map <String,Object> return_map = new HashMap<>();
        //判断这首歌是否存在
        boolean effect = musicDao.findMusicByMusicId(user_id,musicId);
        if(effect){
            //已经存在于我喜欢中,不可重复添加
            return_map.put("msg",false);
        }else {
            boolean flag = musicDao.insertLoveMusic(user_id,musicId);
            if(flag){
                //如果为真则添加成功
                return_map.put("msg",true);
            }else {
                return_map.put("msg",false);
            }
        }
        //将结果return_map返回前端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);
    }
}
