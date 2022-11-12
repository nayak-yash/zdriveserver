package dao;

import controller.GetConnection;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FileDao {
    private Connection connection;
    public boolean addFile(long userId, String name,String type,long createdAt,byte[] data){
        boolean result = false;
        connection= GetConnection.getConnection();
        if(connection!=null) {

            String query = "insert into file (data,name,type,createdAt,userId) values (?,?,?,?,?)";
            try {
                PreparedStatement ps =(PreparedStatement) GetConnection.getConnection().prepareStatement(query);
               ps.setBinaryStream(1,new ByteArrayInputStream(data),data.length);
              ps.setString(2,name);
               ps.setString(3,type);
               ps.setLong(4,createdAt);
              ps.setLong(5,userId);
             ps.executeUpdate();
             result=true;
            } catch (Exception e) {
                e.printStackTrace();
            }
       }
        return result;
   }
}
