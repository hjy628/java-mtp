package com.hjy.mtpattern.chap7.pc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * Created by hjy on 17-12-7.
 *  基于Semaphore的支持流量控制的通道实现
 *  @param   <P> 产品类型
 */
public class SemaphoreBasedChannel<P> implements Channel<P>{
    private final BlockingQueue<P> queue;
    private final Semaphore semaphore;

    /**
     *   @Param  queue 阻塞队列，通常是一个无界阻塞队列
     *   @Param  flowLimit  流量限制数
     *   @Date: 上午10:08 17-12-7
     */
    public SemaphoreBasedChannel(BlockingQueue<P> queue,int flowLimit) {
        this.queue = queue;
        this.semaphore = new Semaphore(flowLimit);
    }

    @Override
    public P take() throws InterruptedException {
        return queue.take();
    }

    @Override
    public void put(P product) throws InterruptedException {
            semaphore.acquire();
        try {
            queue.put(product);
        }finally {
            semaphore.release();
        }
    }
}
