package com;

import com.constant.Constant;
import com.receiver.BaseServerReceiver;
import com.receiver.FileServerReceiver;
import com.receiver.ServerReceiverListener;
import com.receiver.UDPServerReceiver;
import com.sender.*;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by obo on 2018/2/3.
 * Email:obo1993@gmail.com
 * Git:https://github.com/OboBear
 * Blog:http://blog.csdn.net/leilba
 */
public class H264Forward {

    // 队列大小
    private static final int MAX_QUEUE_SIZE = 10;
    // 发送数据，可以发送数据到本地文件或者远程client
    private BaseServerSend serverSend;
    // 接收数据，可以是从本地文件或者远程client中获取数据
    private BaseServerReceiver serverReceiver;
    // 阻塞队列，用于暂存视频数据
    private volatile LinkedBlockingQueue<ServerSendListener.H264Data> linkedBlockingQueue = new LinkedBlockingQueue<>();

    public H264Forward() {
        // 初始化 发送数据 和 接收数据 对象
        if (Constant.WRITE_TO_FILE) {
            serverSend = new FileServerSender();
        } else {
            serverSend = new UDPServerSend();
        }
        if (Constant.READ_FROM_FILE) {
            serverReceiver = new FileServerReceiver();
        } else {
            serverReceiver = new UDPServerReceiver();
        }
    }

    public void start() {
        // 开启线程发送h264数据
        new Thread(){
            @Override
            public void run() {
                super.run();
                serverSend.startSend(new ServerSendListener() {
                    @Override
                    public H264Data getH264Data() {
                        try {
                            System.out.println("linkedBlockingQueue size = " + linkedBlockingQueue.size());
                            return linkedBlockingQueue.take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
            }
        }.start();

        // 开启线程接收视频数据
        serverReceiver.startReceive(new ServerReceiverListener() {
            @Override
            public void receiveData(byte[] data, int length) {
                try {
                    if (linkedBlockingQueue.size() > MAX_QUEUE_SIZE) {
                        linkedBlockingQueue.take().recycler();
                    }
                    linkedBlockingQueue.put(ServerSendListener.H264Data.obtain().set(data, length));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //入口
    public static void main(String[] args) {
        H264Forward h264Forward = new H264Forward();
        h264Forward.start();
    }

}
