package com.receiver;

import com.constant.Constant;

import java.io.*;

/**
 * Created by obo on 2018/2/5.
 * Email:obo1993@gmail.com
 * Git:https://github.com/OboBear
 * Blog:http://blog.csdn.net/leilba
 */
public class FileServerReceiver extends BaseServerReceiver {

    private InputStream mInputStream;

    public FileServerReceiver() {
        File file = new File(Constant.LOCAL_FILE_NAME);
        try {
            mInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static final int MAX_FRAME_SIZE = 32768;
    @Override
    public void startReceive(ServerReceiverListener serverReceiverListener) {
        byte[] receiveData = new byte[MAX_FRAME_SIZE];
        int length;
        try {
            // 从文件中读取视频数据
            while ((length = mInputStream.read(receiveData)) != -1) {
                serverReceiverListener.receiveData(receiveData, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
