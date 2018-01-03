package com.hjy.mtpattern.chap13.example;

import com.hjy.mtpattern.chap13.AbstractPipe;
import com.hjy.mtpattern.chap13.Pipe;
import com.hjy.mtpattern.chap13.PipeException;
import com.hjy.mtpattern.chap13.SimplePipeline;

import java.util.Random;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hjy on 18-1-3.
 * 使用基于线程池的Pipe示例代码
 */
public class ThreadPoolBasedPipeExample {

    public static void main(String[] args) {

        final ThreadPoolExecutor executorService;

        executorService = new ThreadPoolExecutor(1,Runtime.getRuntime().availableProcessors()*2,60, TimeUnit.MINUTES,
                new SynchronousQueue<Runnable>(),new ThreadPoolExecutor.CallerRunsPolicy());

        final SimplePipeline<String,String> pipeline = new SimplePipeline<String,String>();

        Pipe<String,String> pipe = new AbstractPipe<String, String>() {
            @Override
            protected String doProcess(String input) throws PipeException {
                String result = input +"->[pipe1,"+Thread.currentThread().getName()+"]";
                System.out.println(result);
                return result;
            }
        };

        pipeline.addAsThreadPoolBasedPipe(pipe,executorService);

        pipe = new AbstractPipe<String, String>() {
            @Override
            protected String doProcess(String input) throws PipeException {
                String result = input +"->[pipe2,"+Thread.currentThread().getName()+"]";
                System.out.println(result);
                try {
                    TimeUnit.MILLISECONDS.sleep(new Random().nextInt(100));
                }catch (InterruptedException e){
                    ;
                }
                return result;
            }
        };

        pipeline.addAsThreadPoolBasedPipe(pipe,executorService);


        pipe = new AbstractPipe<String, String>() {
            @Override
            protected String doProcess(String input) throws PipeException {
                String result = input +"->[pipe3,"+Thread.currentThread().getName()+"]";
                System.out.println(result);
                try {
                    TimeUnit.MILLISECONDS.sleep(new Random().nextInt(200));
                }catch (InterruptedException e){
                    ;
                }
                return result;
            }

            @Override
            public void shutdown(long timeout, TimeUnit unit) {
                //在最后一个Pipe中关闭线程池
                executorService.shutdown();
                try {
                    executorService.awaitTermination(timeout, unit);
                }catch (InterruptedException e){
                    ;
                }
            }
        };

        pipeline.addAsThreadPoolBasedPipe(pipe,executorService);

        pipeline.init(pipeline.newDefaultPipeContext());


        int N = 100;

        try {
            for (int i = 0; i < N; i++) {
                pipeline.process("Task-"+i);
            }
        }catch (IllegalStateException e){
            ;
        }catch (InterruptedException e){
            ;
        }

        pipeline.shutdown(10,TimeUnit.SECONDS);

    }

}
