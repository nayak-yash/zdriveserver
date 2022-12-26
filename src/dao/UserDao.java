package dao;

import controller.GetConnection;
import models.User;

import javax.swing.*;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao{
    private Connection connection;
    public boolean addUser(User user){
        connection=GetConnection.getConnection();
        if(connection!=null) {
            String query = "insert into userinfo (name,username,email,password) values (?,?,?,?)";
            try {
                PreparedStatement ps =GetConnection.getConnection().prepareStatement(query);
                ps.setString(1,user.getName());
                ps.setString(2,user.getUserName());
                ps.setString(3,user.getEmail());
                ps.setString(4,doHashing(user.getPassword()));
                ps.executeUpdate();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public Long getID(String userName,String password){
        connection=GetConnection.getConnection();
        if(connection!=null) {
            try {
                String statement="select * from userinfo where username=? and password=?";
                PreparedStatement ps =(PreparedStatement) GetConnection.getConnection().prepareStatement(statement);
                ps.setString(1,userName);
                ps.setString(2,doHashing(password));
                ResultSet rs=ps.executeQuery();
                if(rs.next()){
                    return rs.getLong("id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    String doHashing(String password){
        StringBuilder sb=new StringBuilder();
        try {
            MessageDigest md=MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte res[]=md.digest();
            for(byte b:res){
                sb.append(String.format("%02x",b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
