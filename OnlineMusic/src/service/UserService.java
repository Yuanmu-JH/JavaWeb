package service;

import dao.UserDao;
import entity.User;

//业务实现层
public class UserService {
    //实现登录
    public User login(User loginUser){
        UserDao userDao = new UserDao();
        User user = userDao.login(loginUser);
        return user;
    }


}
