package com.hjy.mtpattern.chap9.threadpool.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hjy on 17-12-8.
 */
public class ThreadPoolSize4IOIntensiveTask {


    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                //核心线程池大小为1
                1,
                //最大线程池大小为2*Ncpu
                Runtime.getRuntime().availableProcessors()*2,
                60, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(200));
        threadPool.submit(new IOIntensiveTask());
    }



    //某个I/O密集型任务
    private static class IOIntensiveTask implements Runnable{

        @Override
        public void run() {
            //执行大量的I/O操作
            System.out.println("doing I/O");
            try {
                TimeUnit.SECONDS.sleep(2);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
