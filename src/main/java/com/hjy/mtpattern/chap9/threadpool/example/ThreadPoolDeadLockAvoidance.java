package com.hjy.mtpattern.chap9.threadpool.example;

import java.util.concurrent.*;

/**
 * Created by hjy on 17-12-8.
 *
 *  要执行彼此有依赖关系的任务可以考虑将不同类型的任务交给不同的线程池实例执行，或者对负责任务执行的线程池实例进行如下配置
 *  配置1:设置线程池的最大大小为一个有限值，而不是默认值Integer.MAX_VALUE
 *  配置2:使用SynchronousQueue做为工作队列
 *  配置2:使用ThreadPoolExecutor.CallerRunsPolicy作为线程池饱和处理策略
 *
 * 避免线程死锁的一种方法
 */
public class ThreadPoolDeadLockAvoidance {

    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
        1,
            //最大线程池大小为1(有限数值):
         1,60, TimeUnit.SECONDS,
            //工作队列为SynchroonousQueue
            new SynchronousQueue<Runnable>(),
            //线程池饱和处理策略为CallerRunsPolicy
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        ThreadPoolDeadLockAvoidance me = new ThreadPoolDeadLockAvoidance();
        me.test("<This will NOT deadlock>");
    }

    public void test(final String message){
        Runnable taskA = new Runnable() {
            @Override
            public void run() {
                System.out.println("Executing TaskA...");
                Runnable taskB = new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("TaskB processes "+message);
                    }
                };
                Future<?> result = threadPool.submit(taskB);
                try {
                    //等待TaskB执行结束才能继续执行TaskA,使TaskA和TaskB成为有依赖关系的两个任务
                    result.get();
                }catch (InterruptedException e){
                    ;
                }catch (ExecutionException e){
                    e.printStackTrace();
                }

                System.out.println("TaskA Done.");
            }
        };

        threadPool.submit(taskA);
    }

}
