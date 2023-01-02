package controller;

import models.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
                    oos.flush();
                }else if(REQUEST_CODE==6){
                    ObjectInputStream ois =new ObjectInputStream(is);
                    ObjectOutputStream oos=new ObjectOutputStream(os);
                    File dir=(File)ois.readObject();
                    File item=(File)ois.readObject();
                    if(item.isDirectory()){
                        File startDir=new File(dir,item.getName());
                        startDir.mkdir();
                        copyDirectory(item,startDir, 0);
                    }
                    else {
                        copyFileUsingStream(item, dir, 0);
                    }
                    File renewed=new File(dir.getAbsolutePath());
                    oos.writeObject(renewed);
                    oos.flush();
                }else if(REQUEST_CODE==7){
                    ObjectInputStream ois =new ObjectInputStream(is);
                    ObjectOutputStream oos=new ObjectOutputStream(os);
                    File dir=(File)ois.readObject();
                    File item=(File)ois.readObject();
                    if(item.isDirectory()){
                        File startDir=new File(dir,item.getName());
                        startDir.mkdir();
                        copyDirectory(item,startDir, 1);       // copystatus = 1 for move and 0 for copy
                    }
                    else {
                        copyFileUsingStream(item, dir, 1);
                    }
                    File renewed=new File(dir.getAbsolutePath());
                    oos.writeObject(renewed);
                    oos.flush();
                }
                else if(REQUEST_CODE==8){
                    ObjectInputStream ois =new ObjectInputStream(is);
                    ObjectOutputStream oos=new ObjectOutputStream(os);
                    File curDir=(File)ois.readObject();
                    File item=(File)ois.readObject();
                    if(item.isDirectory()){
                        deleteDirectory(item);
                    }
                    else{
                        item.delete();
                    }
                    File renewed=new File(curDir.getAbsolutePath());
                    oos.writeObject(renewed);
                    oos.flush();
                }else if(REQUEST_CODE==9){
                    ObjectInputStream ois =new ObjectInputStream(is);
                    ObjectOutputStream oos=new ObjectOutputStream(os);
                    File item=(File)ois.readObject();
                    String newName=dataInputStream.readUTF();
                    File renamed=new File(item.getParentFile(),newName);
                    item.renameTo(renamed);
                    oos.writeObject(renamed);
                    oos.flush();
                }
                dataOutputStream.close();
                dataInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyDirectory(File item, File dir, int copyStatus) throws IOException{
        File[] child=item.listFiles();
        for(File f:child){
            if(f.isDirectory()){
                File newDir=new File(dir,f.getName());
                newDir.mkdir();
                if(copyStatus == 1){
                    f.delete();
                }
                copyDirectory(f,newDir, copyStatus);
            }
            else {
                copyFileUsingStream(f, dir, copyStatus);
            }
        }
        if(copyStatus == 1){
            item.delete();
        }
    }


    private static void deleteDirectory(File item) {
        File[] child=item.listFiles();
        for(File f:child){
            if(f.isDirectory()){
                deleteDirectory(f);
            }else {
                f.delete();
            }
        }
        item.delete();
    }

    private static void copyFileUsingStream(File source, File dest, int copyStatus) throws IOException {
        try {
            dest=new File(dest.getAbsolutePath()+"\\");
            InputStream is = new FileInputStream(source);
            OutputStream os = new FileOutputStream(dest+"\\"+source.getName());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            is.close();
            os.close();
            if(copyStatus == 1){
                source.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
