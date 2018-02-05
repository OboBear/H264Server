package com.constant;

/**
 * Created by obo on 2018/2/5.
 * Email:obo1993@gmail.com
 * Git:https://github.com/OboBear
 * Blog:http://blog.csdn.net/leilba
 */
public class Constant {
    // 接收数据所使用的端口
    public static final int DATA_RECEIVE_PORT = 10086;
    // 接收到数据后是否写入到本地文件, 否的话则发送到网络环境
    public static final boolean WRITE_TO_FILE = false;

    // 发送数据所使用的端口
    public static final int DATA_SEND_PORT = 10087;
    // 是否从本地文件中读取数据, 否的话则从网络环境读取
    public static final boolean READ_FROM_FILE = false;

    // 本地的视频文件
    public static final String LOCAL_FILE_NAME = "./h264.mp4";
}
