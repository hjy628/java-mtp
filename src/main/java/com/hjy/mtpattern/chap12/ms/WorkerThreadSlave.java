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
         FutureTask<V> ft = new FutureTask<V>(new Callable<V>() {
            @Override
            public V call() throws Exception {
                V result;
                try {
                    result = doProcess(task);
                }catch (Exception e){
                        SubTaskFailureException stfe = newSubTaskFailureException(task,e);
                    throw stfe;
                }
                return result;
            }
        });
        taskQueue.put(ft);
        terminationToken.reservations.incrementAndGet();
        return ft;
    }

    private SubTaskFailureException newSubTaskFailureException(final T subTask, Exception cause) {
        RetryInfo<T,V> retryInfo = new RetryInfo<T, V>(subTask, new Callable<V>() {
            @Override
            public V call() throws Exception {
                V result;
                result = doProcess(subTask);
                return result;
            }
        });
        return new SubTaskFailureException(retryInfo,cause);
    }

    /**
     *   @Description      留给子类实现，用于实现子任务的处理逻辑
     *   @Param   task 子任务
     *   @return 子任务的处理结果
     *   @Date: 下午5:11 17-12-29
     */
    protected abstract V doProcess(T task) throws Exception;

    @Override
    protected void doRun() throws Exception {
        try {
            Runnable task = taskQueue.take();
            task.run();
        }finally {
            terminationToken.reservations.decrementAndGet();
        }
    }

    @Override
    public void init() {
        start();
    }

    @Override
    public void shutdown() {
        terminate(true);
    }
}
