package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/removeLoveServlet")
public class RemoveLoveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        //获得musicId
        String idStr = req.getParameter("id");
        int musicId = Integer.parseInt(idStr);
        //获得user_id
        User user = (User)req.getSession().getAttribute("user");
        int user_id = user.getId();

        MusicDao musicDao = new MusicDao();
        int ret = musicDao.removeLoveMusic(user_id,musicId);

        Map<String,Object> return_map = new HashMap<>();

        if(ret == 1){
            //删除成功
            return_map.put("msg",true);
        }else{
            return_map.put("msg",false);
        }
        //写回给前端
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);
    }
}
