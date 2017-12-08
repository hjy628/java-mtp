package com.hjy.mtpattern.chap9.threadpool.example;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by hjy on 17-12-8.
 * 该线程池饱和处理策略支持将提交失败的任务重新放入线程池工作队列
 */
public class ReEnqueueRejectedExecutionHandler implements RejectedExecutionHandler{
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (executor.isShutdown()){
            return;
        }
        try {
            executor.getQueue().put(r);
        }catch (InterruptedException e){
            ;
        }

    }
}
