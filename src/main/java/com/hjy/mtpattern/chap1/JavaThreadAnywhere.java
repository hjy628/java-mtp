package com.hjy.mtpattern.chap1;

/**
 * Created by hjy on 17-12-4.
 * java代码的执行线程
 */
public class JavaThreadAnywhere {

    public static void main(String[] args) {
        System.out.println("The main method was executed by thread:"+Thread.currentThread().getName());
        Helper helper = new Helper("Java Thread Anywhere");
        helper.run();

        TestThread testThread = new TestThread();
        testThread.start();
        testThread.start();

    }



    static class Helper implements Runnable{
        private final String message;

        public Helper(String message) {
            this.message = message;
        }

        private void doSomething(String message){
            System.out.println("The doSomething method was executed by thread:"+Thread.currentThread().getName());
            System.out.println("Do something with " + message);
        }

        @Override
        public void run() {
            doSomething(message);
        }
    }

    static class TestThread extends Thread{
        @Override
        public void run() {
            System.out.println("run-------"+Thread.currentThread().getName());
        }
    }


}
