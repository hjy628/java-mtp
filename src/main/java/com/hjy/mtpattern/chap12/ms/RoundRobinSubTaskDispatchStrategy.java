package com.hjy.mtpattern.chap12.ms;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by hjy on 17-12-11.
 * 简单的轮询(Round-robin)派发算法策略
 * @param <T> 原始任务类型
 * @param <V> 子任务处理结果类型
 */
public class RoundRobinSubTaskDispatchStrategy<T,V> implements SubTaskDispatchStrategy<T,V> {


    @SuppressWarnings("unchecked")
    @Override
    public Iterator<Future<V>> dispatch(Set<? extends SlaveSpec<T, V>> slaves,
                                        TaskDivideStrategy<T> taskDivideStrategy) throws InterruptedException {
        final List<Future<V>> subResults = new LinkedList<Future<V>>();
        T subTask;
        Object[] arrSlaves = slaves.toArray();
        int i = -1;
        final int slaveCount = arrSlaves.length;
        Future<V> subTaskResultPromise;

        while (null!=(subTask=taskDivideStrategy.nextChunk())){
            i = (i+1)%slaveCount;
            subTaskResultPromise = new FutureTask<V>(null);
            subResults.add(subTaskResultPromise);
        }
        return subResults.iterator();
    }
}
