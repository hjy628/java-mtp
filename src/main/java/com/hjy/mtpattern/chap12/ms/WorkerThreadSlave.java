package com.hjy.mtpattern.chap12.ms;

import com.hjy.mtpattern.chap12.ms.example.AbstractTerminatableThread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by hjy on 17-12-11.
 * 基于工作者线程的Slave参与者通用实现
 * @param <T> 子任务类型
 * @param <V> 子任务处理结果类型
 */
public abstract class WorkerThreadSlave<T,V> extends AbstractTerminatableThread implements SlaveSpec<T,V> {
    private final BlockingQueue<Runnable> taskQueue;

    public WorkerThreadSlave(BlockingQueue<Runnable> taskQueue) {
        this.taskQueue = taskQueue;
    }

    public Future<V> submit(final T task)throws InterruptedException{
         Future<V> ft = new FutureTask<V>(new Callable<V>() {
            @Override
            public V call() throws Exception {
                V result;
                try {
                    result = null;
                }catch (Exception e){

                }
                return result;
            }
        });
        taskQueue.put(ft);
        terminationToken.reservations.incrementAndGet();
        return ft;
    }




}
