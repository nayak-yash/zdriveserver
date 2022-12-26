package controller;

import dao.UserDao;
import models.User;


public class RegisterActivity extends Thread {
    private UserDao mUserDao;
    public boolean result;
    public RegisterActivity(User user){
        mUserDao=new UserDao();
        result=mUserDao.addUser(user);
    }
}
