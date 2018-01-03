package com.hjy.mtpattern.chap13;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by hjy on 18-1-3.
 * 基于线程池的Pipe实现类
 *
 * @param <IN> 输入类型
 * @param <OUT> 输出类型
 */
public class ThreadPoolPipeDecorator<IN,OUT> implements Pipe<IN,OUT> {

    private final Pipe<IN,OUT> delegate;
    private final ExecutorService executorService;

    //线程池停止标志
    private final TerminationToken terminationToken;
    private final CountDownLatch stageProcessDoneLatch = new CountDownLatch(1);

    public ThreadPoolPipeDecorator(Pipe<IN, OUT> delegate, ExecutorService executorService) {
        this.delegate = delegate;
        this.executorService = executorService;
        this.terminationToken = TerminationToken.newInstance(executorService);
    }

    @Override
    public void setNextPipe(Pipe<?, ?> nextPipe) {
        delegate.setNextPipe(nextPipe);
    }

    @Override
    public void init(PipeContext pipeCtx) {
            delegate.init(pipeCtx);
    }

    @Override
    public void shutdown(long timeout, TimeUnit unit) {
        terminationToken.setIsToShutdown();

        if (terminationToken.reservations.get()>0){
            try {
                if (stageProcessDoneLatch.getCount()>0){
                    stageProcessDoneLatch.await(timeout, unit);
                }
            }catch (InterruptedException e){
                ;
            }
        }
        delegate.shutdown(timeout, unit);
    }

    @Override
    public void process(IN input) throws InterruptedException {

        Runnable task = new Runnable() {
            @Override
            public void run() {
                int remainingReservations = -1;
                try {
                    delegate.process(input);
                }catch (InterruptedException e){
                    ;
                }finally {
                    remainingReservations = terminationToken.reservations.decrementAndGet();
                }

                if (terminationToken.isToShutdown()&&0==remainingReservations){
                    stageProcessDoneLatch.countDown();
                }

            }
        };
        executorService.submit(task);

        terminationToken.reservations.incrementAndGet();

    }


    /**
    *  线程池停止标志
    *  每个ExecutorService实例对应唯一的一个TerminationToken实例
    * 　这里使用了Two-phase Termination模式的思想来停止多个Pipe实例所共用的线程池实例
    */
    private static class TerminationToken extends com.hjy.mtpattern.chap13.TerminationToken{
        private final static ConcurrentHashMap<ExecutorService, TerminationToken> INSTANCES_MAP =
                new ConcurrentHashMap<ExecutorService, TerminationToken>();

        //私有构造器
        private TerminationToken(){

        }

        void setIsToShutdown(){
            this.toShutdown = true;
        }

        static TerminationToken newInstance(ExecutorService executorService){
            TerminationToken token = INSTANCES_MAP.get(executorService);
            if (null==token){
                token = new TerminationToken();
                TerminationToken existingToken = INSTANCES_MAP.putIfAbsent(executorService,token);
                if (null!=existingToken){
                    token = existingToken;
                }
            }
            return token;
        }

    }





}
