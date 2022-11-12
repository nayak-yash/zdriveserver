package controller;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class GetConnection {
    public static Connection connection;
    public static Connection getConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/zdrive","root","");
            return connection;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error Occurred!!","Alert",JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }
}
