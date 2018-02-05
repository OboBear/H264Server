package com.receiver;

/**
 * Created by obo on 2018/2/5.
 * Email:obo1993@gmail.com
 * Git:https://github.com/OboBear
 * Blog:http://blog.csdn.net/leilba
 */
public interface ServerReceiverListener {
    void receiveData(byte[] data, int length);
}
