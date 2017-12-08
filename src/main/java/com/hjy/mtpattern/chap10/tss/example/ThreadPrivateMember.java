package com.hjy.mtpattern.chap10.tss.example;

import java.util.concurrent.TimeUnit;

/**
 * Created by hjy on 17-12-8.
 * 用线程对象的私有实例变量实现线程间不共享变量
 */
public class ThreadPrivateMember {

    public static void main(String[] args) throws InterruptedException{
        XThread thread;
        for (int i = 0; i < 3; i++) {
            thread = new XThread("message-"+i);
            thread.start();
        }

        TimeUnit.SECONDS.sleep(1);

    }



    private static class XThread extends Thread{
        private final String message;

        public XThread(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                System.out.println(message);
            }
        }
    }


}
