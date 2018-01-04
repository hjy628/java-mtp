package com.hjy.mtpattern.chap14.hsha;

import java.util.concurrent.*;

/**
 * Created by hjy on 18-1-4.
 * Half-sync/Half-async模式的可复用实现
 * @param <V> 同步任务的处理结果类型
 */
public abstract class AsyncTask<V> {

    //相当于Half-sync/Half-async模式的同步层:用于执行异步层提交的任务
    private volatile Executor executor;
    private final static ExecutorService DEFAULT_EXECUTOR;

    static {
        DEFAULT_EXECUTOR = new ThreadPoolExecutor(1, 1, 8, TimeUnit.HOURS,
                new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "AsyncTaskDefaultWorker");

                //使该线程在JVM关闭时自动停止
                thread.setDaemon(true);
                return thread;
            }
        }, new RejectedExecutionHandler() {

            /**
             * 该RejectedExecutionHandler支持重试
             * 当任务被ThreadPoolExecutor拒绝时，
             * 该RejectedExecutionHandler支持重新将任务放入ThreadPoolExecutor
             * 的工作对列(这意味着，此时客户端代码需要等待ThreadPoolExecutor的队列非满)
             */
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                if (!executor.isShutdown()){
                    try {
                        executor.getQueue().put(r);
                    }catch (InterruptedException e){
                        ;
                    }
                }
            }
        });
    }

    public AsyncTask(Executor executor) {
        this.executor = executor;
    }

    public AsyncTask() {
        this(DEFAULT_EXECUTOR);
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }


    /**
     *   @Description   留给子类实现耗时较短的任务，默认实现什么也不做
     *   @Param    params 客户端代码调用dispatch方法时所传递的参数列表
     *   @Date: 上午10:10 18-1-4
     */
    protected void onPreExecute(Object... params){
        //什么也不做
    }

    /**
     *   @Description   留给子类实现,用于实现同步任务执行结束后所需执行的操作，默认实现什么也不做
     *   @Param    V 同步任务的处理结果
     *   @Date: 上午10:10 18-1-4
     */
    protected void onPostExecute(V result){
        //什么也不做
    }

    protected void onExecutionError(Exception e){
        e.printStackTrace();
    }

    /**
     *   @Description   留给子类实现耗时较长的任务(同步任务)，由后台线程负责调用
     *   @Param    params 客户端代码调用dispatch方法时所传递的参数列表
     *   @return 同步任务的处理结果
     *   @Date: 上午10:10 18-1-4
     */
    protected abstract V doInBackground(Object... params);

    protected Future<V> dispatch(final Object... params){
        FutureTask<V> ft = null;

        //进行异步层初步处理
        onPreExecute(params);

        Callable<V> callable = new Callable<V>() {
            @Override
            public V call() throws Exception {
                V result;
                result = doInBackground(params);
                return result;
            }
        };

        ft = new FutureTask<V>(callable){
            @Override
            protected void done() {
                try {
                    onPostExecute(this.get());
                }catch (InterruptedException e){
                    onExecutionError(e);
                }catch (ExecutionException e){
                    onExecutionError(e);
                }
            }
        };

        //提交任务到同步层处理
        executor.execute(ft);
        return ft;
    }


}
