package com.hjy.mtpattern.chap10.tss.example;

import com.hjy.mtpattern.chap10.tss.ManagedThreadLocal;

/**
 * Created by hjy on 17-12-8.
 */
public class ManagedThreadLocalClient {


    private final static ManagedThreadLocal<Integer> mtl = ManagedThreadLocal.newInstance(
            new ManagedThreadLocal.InitialValueProvider<Integer>(){
        @Override
        protected Integer initialValue() {
            System.out.println(Thread.currentThread().getName());
            return Integer.valueOf((int)Thread.currentThread().getId());
        }
    });

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Helper().start();
        }
    }



    static class Helper extends Thread{
        @Override
        public void run() {
            System.out.println(mtl.get());
        }
    }


}
