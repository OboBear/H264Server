package com.sender;

import com.constant.Constant;

import java.io.*;

/**
 * Created by obo on 2018/2/5.
 * Email:obo1993@gmail.com
 * Git:https://github.com/OboBear
 * Blog:http://blog.csdn.net/leilba
 */
public class FileServerSender extends BaseServerSend {
    private OutputStream mOutputStream = null;
    public FileServerSender() {
        File file = new File(Constant.LOCAL_FILE_NAME);
        try {
            mOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startSend(ServerSendListener sendListener) {
        while (true) {
            ServerSendListener.H264Data h264Data = sendListener.getH264Data();
            try {
                mOutputStream.write(h264Data.data, 0, h264Data.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
