package com.hjy.mtpattern.chap13;

import java.util.concurrent.TimeUnit;

/**
 * Created by hjy on 18-1-3.
 *  Pipe的抽象实现类
 * 该类会调用子类实现的doProcess方法对输入元素进行处理，并将相应的输出做为下一个Pipe实例的输入
 *
 * @param <IN> 输入类型
 * @param <OUT> 输出类型
 */
public abstract class AbstractPipe<IN,OUT> implements Pipe<IN,OUT> {
    protected volatile Pipe<?,?> nextPipe = null;
    private volatile PipeContext pipeCtx;

    @Override
    public void init(PipeContext pipeCtx) {
        this.pipeCtx = pipeCtx;
    }

    @Override
    public void setNextPipe(Pipe<?, ?> nextPipe) {
        this.nextPipe = nextPipe;
    }

    @Override
    public void shutdown(long timeout, TimeUnit unit) {
        //什么也不做
    }

 /**
  *   @Description  留给子类实现，用于子类实现其任务处理逻辑
  *   @param input 输入元素(任务)
  *   @return 任务的处理结果
  *   @throws PipeException
  *   @Date: 下午2:13 18-1-3
  */
    protected abstract OUT doProcess(IN input) throws PipeException;

    public void process(IN input) throws InterruptedException{

        try {
            OUT out = doProcess(input);
            if (null!=nextPipe){
                if (null!=out){
                    ((Pipe<OUT,?>)nextPipe).process(out);
                }
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }catch (PipeException e){
            pipeCtx.handleError(e);
        }

    }

}
