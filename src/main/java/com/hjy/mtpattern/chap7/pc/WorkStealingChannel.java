package com.hjy.mtpattern.chap7.pc;

import com.hjy.mtpattern.chap7.pc.WorkStealingEnabledChannel;

import java.util.concurrent.BlockingDeque;

/**
 * Created by hjy on 17-12-7.
 */
public class WorkStealingChannel<T> implements WorkStealingEnabledChannel<T> {
    //受管队列
    private final BlockingDeque<T>[] managedQueues;

    public WorkStealingChannel(BlockingDeque<T>[] managedQueues) {
        this.managedQueues = managedQueues;
    }

    @Override
    public T take(BlockingDeque<T> preferredQueue) throws InterruptedException {

        //优先从指定的受管队列中取“产品”
        BlockingDeque<T> targetQueue = preferredQueue;
        T product = null;

        //试图从指定的队列的队首取“产品”
        if (null!=targetQueue){
            product = targetQueue.poll();
        }

        int queueIndex = -1;

        while (null==product){
            queueIndex = (queueIndex+1)%managedQueues.length;
            targetQueue = managedQueues[queueIndex];
            //试图从其他受管队列的队尾“窃取”“产品”
            product = targetQueue.pollLast();
            System.out.println("----------------------------------------------------------stealed from "+queueIndex+":"+product);
            if (preferredQueue==targetQueue){
                break;
            }
        }

        if (null==product){
            //随机“窃取”其他受管队列的“产品”
            queueIndex = (int)(System.currentTimeMillis()%managedQueues.length);
            targetQueue = managedQueues[queueIndex];
            product = targetQueue.takeLast();
            System.out.println("----------------------------------------------stealed from "+queueIndex+":"+product);
        }
        return product;
    }

    @Override
    public T take() throws InterruptedException {
        return take(null);
    }

    @Override
    public void put(T product) throws InterruptedException {
        int targetIndex = (product.hashCode()%managedQueues.length);
        BlockingDeque<T> targetQueue = managedQueues[targetIndex];
        targetQueue.put(product);
    }
}
