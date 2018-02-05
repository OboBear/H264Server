package com.sender;

import java.util.LinkedList;

/**
 * Created by obo on 2018/2/5.
 * Email:obo1993@gmail.com
 * Git:https://github.com/OboBear
 * Blog:http://blog.csdn.net/leilba
 */
public interface ServerSendListener {
    H264Data getH264Data();

    class H264Data {
        public byte[] data;
        public int length;

        private static volatile LinkedList<H264Data> linkedList = new LinkedList<>();

        public static H264Data obtain() {
            if (linkedList.size() > 0) {
                return linkedList.remove(0);
            }
            return new H264Data();
        }

        public void recycler() {
            linkedList.add(this);
        }

        public H264Data set(byte[] data, int length) {
            this.data = data.clone();
            this.length = length;
            return this;
        }
    }
}
