package controller;

import models.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Blob;

public class ZDriveServer {
    public static void main(String[] args) {
        System.out.println("Waiting for Clients...");
        try {
            ServerSocket serverSocket = new ServerSocket(6969);
            System.out.println("Connection established");
            while (true) {
                Socket socket = serverSocket.accept();
                InputStream is=socket.getInputStream();
                OutputStream os=socket.getOutputStream();
                DataInputStream dataInputStream = new DataInputStream(is);
                DataOutputStream dataOutputStream = new DataOutputStream(os);
                int REQUEST_CODE = dataInputStream.readInt();
                if (REQUEST_CODE == 1) {
                    String username = dataInputStream.readUTF();
                    String password = dataInputStream.readUTF();
                    LoginActivity loginActivity = new LoginActivity(username, password);
                    if(loginActivity.userId==null){
                        dataOutputStream.writeBoolean(false);
                    }
                    else {
                        dataOutputStream.writeBoolean(true);
                        File f = new File("D:\\zdrive\\" + loginActivity.userId + "\\");
                        f.mkdir();
                        ObjectOutputStream oos = new ObjectOutputStream(os);
                        oos.writeObject(f);
                        oos.close();
                    }
                } else if (REQUEST_CODE == 2) {
                    ObjectInputStream ois=new ObjectInputStream(is);
                    Object o=ois.readObject();
                    User user=(User)o;
                    RegisterActivity registerActivity = new RegisterActivity(user);
                    dataOutputStream.writeBoolean(registerActivity.result);
                } else if (REQUEST_CODE == 3) {
                    String fileName = dataInputStream.readUTF();
                    int size = dataInputStream.readInt();
                    byte data[]=new byte[size];
                    dataInputStream.read(data, 0, size);
                    ObjectInputStream ois=new ObjectInputStream(is);
                    File curDir=(File)ois.readObject();
                    File newFile=new File(curDir.getAbsolutePath()+"\\"+fileName);
                    FileOutputStream fos=new FileOutputStream(newFile);
                    fos.write(data);
                    if(newFile.length()==size){
                        dataOutputStream.writeBoolean(true);
                    }
                    else{
                        dataOutputStream.writeBoolean(false);
                    }
                    fos.close();
                }
                else if(REQUEST_CODE==4){
                    ObjectInputStream ois =new ObjectInputStream(is);
                    File f=(File)ois.readObject();
                    int len= (int) f.length();
                    FileInputStream fis=new FileInputStream(f);
                    byte data[]=new byte[len];
                    fis.read(data,0,len);
                    dataOutputStream.write(data,0,len);
                    fis.close();
                }else if(REQUEST_CODE==5){
                    ObjectInputStream ois =new ObjectInputStream(is);
                    ObjectOutputStream oos=new ObjectOutputStream(os);
                    File dir=(File)ois.readObject();
                    String name=dataInputStream.readUTF();
                    File renewed=new File(dir,name);
                    renewed.mkdir();
                    oos.writeObject(renewed);
                }
                dataOutputStream.close();
                dataInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
