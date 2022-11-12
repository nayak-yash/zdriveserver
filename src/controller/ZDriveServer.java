package controller;

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
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                int REQUEST_CODE = dataInputStream.readInt();
                if (REQUEST_CODE == 1) {
                    String username = dataInputStream.readUTF();
                    String password = dataInputStream.readUTF();
                    LoginActivity loginActivity = new LoginActivity(username, password);
                    dataOutputStream.writeLong(loginActivity.userId);
                    dataOutputStream.flush();
                } else if (REQUEST_CODE == 2) {

                    String name = dataInputStream.readUTF();
                    String username = dataInputStream.readUTF();
                    String email = dataInputStream.readUTF();
                    String password = dataInputStream.readUTF();
                    RegisterActivity registerActivity = new RegisterActivity(name, username, email, password);
                    dataOutputStream.writeBoolean(registerActivity.result);
                    dataOutputStream.flush();
                } else if (REQUEST_CODE == 3) {
                    long userId = dataInputStream.readLong();
                    String name = dataInputStream.readUTF();
                    String type = dataInputStream.readUTF();
                    long createdAt = dataInputStream.readLong();
                    int size = dataInputStream.readInt();
                    byte data[] = new byte[size];
                    dataInputStream.read(data, 0, size);
                    UploadActivity uploadActivity = new UploadActivity(userId, name, type, createdAt, data);
                    dataOutputStream.writeBoolean(uploadActivity.result);
                    dataOutputStream.flush();
                }
                dataOutputStream.close();
                dataInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
