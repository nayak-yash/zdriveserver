package controller;

import dao.UserDao;

public class RegisterActivity extends Thread {
    private UserDao mUserDao;
    public boolean result;
    public RegisterActivity(String name, String username, String email, String password){
        mUserDao=new UserDao();
        result=mUserDao.addUser(name,username,email,password);
    }
}
