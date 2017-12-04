package com.hjy.mtpattern.chap1;

/**
 * Created by hjy on 17-12-4.
 * 线程安全的计数器
 */
public class ThreadSafeCounter {

    private int counter;

    public void increment(){
        synchronized (this){
            counter++;
        }
    }

    public int get(){
        synchronized (this){
            return counter;
        }
    }


}
