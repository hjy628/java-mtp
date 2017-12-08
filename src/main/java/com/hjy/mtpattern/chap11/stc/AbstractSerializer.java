package com.hjy.mtpattern.chap11.stc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

/**
 * Created by hjy on 17-12-8.
 * Serial Thread Confiment模式Serializer参与者可复用实现
 * @param <T> Serializer 向WorkerThread所提交的任务对应的类型
 * @param <V> service方法的返回值类型
 */
public abstract class AbstractSerializer<T,V> {
    private final TerminatableWorkerThread<T,V> workerThread;

    public AbstractSerializer(BlockingQueue<Runnable> workQueue,TaskProcessor<T,V> taskProcessor) {
        this.workerThread = new TerminatableWorkerThread<T, V>(workQueue, taskProcessor);
    }

    /**
     *   @Description   留给子类实现，用于根据指定参数生成相应的任务实例
     *   @Param   params 参数列表
     *   @return 任务实例，用于提交给WorkerThread
     *   @Date: 下午5:35 17-12-8
     */
    protected abstract T makeTask(Object... params);




    /**
     *   @Description   该类对外暴露的服务方法，该类的子类需要定义一个命名含义比该方法更为具体的方法（如downloadFile）
     *   含义具体的服务方法(如downloadFile)可直接调用该方法
     *   @Param   params 客户端代码调用该方法时所传递的参数列表
     *   @return 可借以获取任务处理结果的Promise实例
     *   @throws InterruptedException
     *   @Date: 下午5:39 17-12-8
     */
    protected Future<V> service(Object... params)throws InterruptedException{
        T task = makeTask(params);
        Future<V> resultPromise = workerThread.submit(task);
        return resultPromise;
    }


    /**
    *   初始化该类对外暴露的服务
    */
    public void init(){
        workerThread.start();
    }


    /**
    *   停止该类对外暴露的服务
    */
    public void shutdown(){
        workerThread.terminate();
    }

}
