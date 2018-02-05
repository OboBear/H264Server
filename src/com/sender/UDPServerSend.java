package com.sender;

import com.constant.Constant;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by obo on 2018/2/4.
 * Email:obo1993@gmail.com
 * Git:https://github.com/OboBear
 * Blog:http://blog.csdn.net/leilba
 */
public class UDPServerSend extends BaseServerSend {

    DatagramSocket datagramSocket = null;

    public UDPServerSend() {
        try {
            datagramSocket = new DatagramSocket(Constant.DATA_SEND_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startSend(ServerSendListener sendListener) {
        // 开启线程获取远程客户端发送的连接请求
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    byte[] buf = new byte[64];
                    DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                    //receive
                    while (true) {
                        datagramSocket.receive(datagramPacket);
                        // 收到客户端请求后放入到连接池
                        connect(datagramPacket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();

        while (true) {
            // 从外部获取视频数据
            ServerSendListener.H264Data h264Data = sendListener.getH264Data();
            byte[] data = h264Data.data;
            int length = h264Data.length;
            h264Data.recycler();
            // 将视频数据发送给客户端
            send(data, length);
        }
    }




    static class ConnectData {
        InetAddress address;
        int port;
        public ConnectData(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }
        long timeStamp;
    }

    //维护链接池
    HashMap<String, ConnectData> connectManager = new HashMap<>();
    private static final long TIME_OUT = 5000;


    private void connect(DatagramPacket datagramPacket) {
        String key = datagramPacket.getAddress().toString();
        ConnectData connectData = connectManager.get(key);
        if (connectData == null) {
            // 该用户之前尚未连接过，所以加入到连接池
            connectData = new ConnectData(datagramPacket.getAddress(), datagramPacket.getPort());
            connectManager.put(key, connectData);
            System.out.println("connect " + connectData.address);
        }
        // 更新连接时间戳
        connectData.timeStamp = System.currentTimeMillis();
    }

    private void send( byte[] data, int length) {
        Set<String> address = connectManager.keySet();
        for (String key : address) { //遍历连接池，获取当前所有的客户端
            ConnectData connectData = connectManager.get(key);
            //连接超时，说明用户已经断开连接，则清除
            if (System.currentTimeMillis() - connectData.timeStamp > TIME_OUT) {
                connectManager.remove(key);
                System.out.println("disconnect " + connectData.address);
                continue;
            }
            DatagramPacket response = new DatagramPacket(data, length, connectData.address, connectData.port);
            try {
                // 发送数据给指定用户
                datagramSocket.send(response);
                System.out.println("send " + connectData.address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 测试入口
    public static void main(String[] args) {
        UDPServerSend udpServerSend = new UDPServerSend();
        udpServerSend.startSend(new ServerSendListener() {
            @Override
            public H264Data getH264Data() {
                return null;
            }
        });
    }
}
