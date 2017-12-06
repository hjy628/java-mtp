package com.hjy.mtpattern.chap5.tpt.example;

import com.hjy.mtpattern.chap5.tpt.AbstractTerminatableThread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by hjy on 17-12-6.
 * 停止生产者-消费者问题中的线程的例子
 */
public class SomeService {
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);

    private final Producer producer= new Producer();
    private final Consumer consumer = new Consumer();

    private class Producer extends AbstractTerminatableThread{
        private int i = 0;

        @Override
        protected void doRun() throws Exception {
            queue.put(String.valueOf(i++));
            consumer.terminationToken.reservations.incrementAndGet();
        }
    }


    private class Consumer extends AbstractTerminatableThread{
        @Override
        protected void doRun() throws Exception {
            String product = queue.take();

            System.out.println("Processing product: "+product);

            try {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(100));
            }catch (InterruptedException e){
                ;
            }finally {
                terminationToken.reservations.decrementAndGet();
            }
        }
    }

    public void shutdown(){
        //生产者线程停止后再停止消费者线程
        producer.terminate(true);
        consumer.terminate();
    }

    public void init(){
        producer.start();
        consumer.start();
    }

    public static void main(String[] args) throws InterruptedException{
        SomeService ss = new SomeService();
        ss.init();
        TimeUnit.SECONDS.sleep(1);
        ss.shutdown();
    }



}
