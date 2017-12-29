package com.hjy.mtpattern.chap12.ms;

import java.util.concurrent.Callable;

/**
 * Created by hjy on 17-12-29.
 * @param <T> 子任务类型
 * @param <V>子任务处理结果类型
 */
public class RetryInfo<T,V> {

    public final T subTask;
    public final Callable<V> redoCommand;

    public RetryInfo(T subTask, Callable<V> redoCommand) {
        this.subTask = subTask;
        this.redoCommand = redoCommand;
    }
}
