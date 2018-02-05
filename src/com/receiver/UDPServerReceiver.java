package com.receiver;

import com.constant.Constant;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by obo on 2018/2/4.
 * Email:obo1993@gmail.com
 * Git:https://github.com/OboBear
 * Blog:http://blog.csdn.net/leilba
 */
public class UDPServerReceiver extends BaseServerReceiver {
    public static final int MAX_FRAME_SIZE = 32768;

    @Override
    public void startReceive(ServerReceiverListener serverReceiverListener) {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket(Constant.DATA_RECEIVE_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        // 2、创建数据包
        byte[] buf = new byte[MAX_FRAME_SIZE];

        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
        while (true) {
            try {
                // 从客户端获取到视频数据
                datagramSocket.receive(datagramPacket);
                serverReceiverListener.receiveData(datagramPacket.getData(), datagramPacket.getLength());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
