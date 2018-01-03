package com.hjy.mtpattern.chap13;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by hjy on 18-1-3.
 */
public class SimplePipeline<T,OUT> extends AbstractPipe<T,OUT> implements Pipeline<T,OUT> {

    private final Queue<Pipe<?,?>> pipes = new LinkedList<Pipe<?,?>>();

    private final ExecutorService helperExecutor;

    public SimplePipeline() {
        this(Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r,"SimplePipeline-Helper");
                t.setDaemon(true);
                return t;
            }
        }));

    }

    public SimplePipeline(final ExecutorService helperExecutor) {
        super();
        this.helperExecutor = helperExecutor;
    }

    @Override
    public void shutdown(long timeout, TimeUnit unit) {
        Pipe<?,?> pipe;
        while (null!=(pipe=pipes.poll())){
            pipe.shutdown(timeout, unit);
        }
        helperExecutor.shutdown();
    }

    @Override
    public void addPipe(Pipe pipe) {
        //Pipe间的关联关系在init方法中建立
        pipes.add(pipe);
    }

    public <INPUT,OUTPUT> void addAsWorkerThreadBasedPipe(Pipe<INPUT,OUTPUT> delegate,int workerCount ){
        addPipe(new WorkerThreadPipeDecorator<INPUT,OUTPUT>(delegate,workerCount));
    }

    public <INPUT,OUTPUT> void addAsThreadPoolBasedPipe(Pipe<INPUT,OUTPUT> delegate,ExecutorService executorService){
        addPipe(new ThreadPoolPipeDecorator<INPUT,OUTPUT>(delegate,executorService));
    }

    @Override
    public void process(T input) throws InterruptedException {
        @SuppressWarnings("unchecked")
        Pipe<T, ?> firstPipe = (Pipe<T, ?>) pipes.peek();
        firstPipe.process(input);
    }

    @Override
    protected OUT doProcess(T input) throws PipeException {
        //什么也不做
        return null;
    }

    @Override
    public void init(final PipeContext pipeCtx) {
        LinkedList<Pipe<?,?>> pipesList = (LinkedList<Pipe<?,?>>) pipes;
        Pipe<?,?> prevPipe = this;
        for (Pipe<?, ?> pipe :
                pipesList) {
            prevPipe.setNextPipe(pipe);
            prevPipe = pipe;
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (Pipe<?, ?> pipe :   pipes) {
                    pipe.init(pipeCtx);
                }
            }
        };
        helperExecutor.submit(task);
    }

    public PipeContext newDefaultPipeContext(){
        return new PipeContext() {
            @Override
            public void handleError(PipeException exp) {
                helperExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        exp.printStackTrace();
                    }
                });
            }
        };
    }
}
