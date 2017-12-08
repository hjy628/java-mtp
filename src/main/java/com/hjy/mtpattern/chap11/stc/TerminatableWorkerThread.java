package com.hjy.mtpattern.chap11.stc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by hjy on 17-12-8.
 * Serial Thread Confiment模式WorkerThread参与者可复用实现
 * 该类使用了Two-phase Termination模式
 * @param <T> Serializer向WorkerThread所提交的任务对应的类型
 * @param <V> 表示任务执行结果的类型
 */
public class TerminatableWorkerThread<T,V> extends AbstractTerminatableThread {

    private final BlockingQueue<Runnable> workQueue;

    //负责真正执行任务的对象
    private final TaskProcessor<T,V> taskProcessor;

    public TerminatableWorkerThread(BlockingQueue<Runnable> workQueue, TaskProcessor<T, V> taskProcessor) {
        this.workQueue = workQueue;
        this.taskProcessor = taskProcessor;
    }


    /**
     *   @Description 接收并行任务，并将其串行化
     *   @Param  task 任务
     *   @return 可借以获取任务处理结果的Promise实例
     *   @throws InterruptedException
     *   @Date: 下午5:25 17-12-8
     */
    public Future<V> submit(final T task)throws InterruptedException{
        Callable<V> callable = new Callable<V>() {
            @Override
            public V call() throws Exception {
                return taskProcessor.doProcess(task);
            }
        };
        FutureTask<V> ft = new FutureTask<V>(callable);
        workQueue.put(ft);

        terminationToken.reservations.incrementAndGet();
        return ft;
    }

    /**
     *   @Description 执行任务的处理逻辑
     *   @Date: 下午5:27 17-12-8
     */
    @Override
    protected void doRun() throws Exception {
        Runnable ft = workQueue.take();
        try {
            ft.run();
        }finally {
            terminationToken.reservations.decrementAndGet();
        }
    }
}
