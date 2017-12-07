package com.hjy.mtpattern.chap7.pc.example;

import com.hjy.mtpattern.chap7.pc.AbstractTerminatableThread;
import com.hjy.mtpattern.chap7.pc.TerminationToken;
import com.hjy.mtpattern.chap7.pc.WorkStealingChannel;
import com.hjy.mtpattern.chap7.pc.WorkStealingEnabledChannel;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by hjy on 17-12-7.
 * 工作窃取算法实例，该类使用two-phase Termination 两阶段终止模式
 */
public class WorkStealingExample {
    private final WorkStealingEnabledChannel<String> channel;
    private final TerminationToken token = new TerminationToken();

    public WorkStealingExample() {
        int nCPU = Runtime.getRuntime().availableProcessors();
        int consumerCount = nCPU/2 +1;

        @SuppressWarnings("unchecked")
        BlockingDeque<String>[] managedQueues = new LinkedBlockingDeque[consumerCount];

        //该通道实例对应了多个队列实例managedQueues
        channel = new WorkStealingChannel<String>(managedQueues);

        Consumer[] consumers = new Consumer[consumerCount];
        for (int i = 0; i < consumerCount; i++) {
            managedQueues[i] = new LinkedBlockingDeque<String>();
            consumers[i] = new Consumer(token,managedQueues[i]);
        }

        for (int i = 0; i < nCPU; i++) {
            new Producer().start();
        }

        for (int i = 0; i < consumerCount; i++) {
            consumers[i].start();
        }
    }

    public void doSomething(){

    }

    public static void main(String[] args) throws InterruptedException{
        WorkStealingExample wse;
        wse = new WorkStealingExample();

        wse.doSomething();
        TimeUnit.MILLISECONDS.sleep(3500);


    }

    private class Producer extends AbstractTerminatableThread {
        private int i = 0;

        @Override
        protected void doRun() throws Exception {
            TimeUnit.MILLISECONDS.sleep((int)(Math.random()*15)+2);
            channel.put(String.valueOf(i++));
            token.reservations.incrementAndGet();
        }
    }

    private class Consumer extends AbstractTerminatableThread{
        private final BlockingDeque<String> workQueue;

        public Consumer(TerminationToken terminationToken, BlockingDeque<String> workQueue) {
            super(terminationToken);
            this.workQueue = workQueue;
        }

        @Override
        protected void doRun() throws Exception {
            /*
            * WorkStealingEnabledChannel接口的take(BlockingDeque<P> preferredQueue)方法
            * 实现了工作窃取算法
            */
            String product = channel.take(workQueue);


            System.out.println("Processing product:"+product);

            //模拟执行真正操作的时间消耗
            try {
                System.out.println(Thread.currentThread().getName());
                if (!"Thread-0".equals(Thread.currentThread().getName())){
                    System.out.println("------------"+Thread.currentThread().getName());
                    TimeUnit.MILLISECONDS.sleep((int)(Math.random()*1500)+880);
                }else {
                    TimeUnit.MILLISECONDS.sleep((int)(Math.random()*150)+2);
                }
            }catch (InterruptedException e){
                ;
            }finally {
                token.reservations.decrementAndGet();
            }

        }
    }
}
