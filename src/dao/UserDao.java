package dao;

import controller.GetConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao{
    private Connection connection;
    public boolean addUser(String name,String username,String email,String password){
        connection=GetConnection.getConnection();
        if(connection!=null) {
            String query = "insert into userinfo (name,username,email,password) values (?,?,?,?)";
            try {
                PreparedStatement ps =(PreparedStatement) GetConnection.getConnection().prepareStatement(query);
                ps.setString(1,name);
                ps.setString(2,username);
                ps.setString(3,email);
                ps.setString(4,password);
                ps.executeUpdate();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public Long query(String userName,String password){
        connection=GetConnection.getConnection();
        if(connection!=null) {
            try {
                String statement="select * from userinfo where username=? and password=?";
                PreparedStatement ps =(PreparedStatement) GetConnection.getConnection().prepareStatement(statement);
                ps.setString(1,userName);
                ps.setString(2,password);
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
}
