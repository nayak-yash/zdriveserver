package controller;

import dao.UserDao;


public class LoginActivity {
    private UserDao mUserDao;
    public Long userId;

    public LoginActivity(String userName, String password) {
        mUserDao = new UserDao();
        userId = mUserDao.query(userName, password);
    }
}
