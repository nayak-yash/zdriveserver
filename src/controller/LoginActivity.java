package controller;

import dao.UserDao;

import java.io.File;


public class LoginActivity {
    private UserDao mUserDao;
    public Long userId;

    public LoginActivity(String userName, String password) {
        mUserDao = new UserDao();
        userId = mUserDao.getID(userName, password);
    }
}
