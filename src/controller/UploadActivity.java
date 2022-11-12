package controller;

import dao.FileDao;

public class UploadActivity {
    public boolean result;
    private FileDao mFileDao;
    public UploadActivity(long userId,String name, String type, long createdAt,byte data[]) {
        mFileDao=new FileDao();
        result = mFileDao.addFile(userId,name,type,createdAt,data);
    }
}
