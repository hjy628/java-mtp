package com.hjy.mtpattern.chap8.activeobject.example;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by hjy on 17-12-7.
 * 自己动手实现Scheduler的错误隔离示例代码
 */
public class CustomScheduler implements Runnable{
    private LinkedBlockingQueue<Runnable> activationQueue = new LinkedBlockingQueue<Runnable>();


    @Override
    public void run() {
        dispatch();
    }

    public void dispatch(){
        while (true){
            Runnable methodRequest;
            try {
                methodRequest = activationQueue.take();

                //防止个别任务执行失败导致线程终止的代码在run方法中
                methodRequest.run();
            }catch (InterruptedException e){
                //处理该异常
            }
        }
    }

}
