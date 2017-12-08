package com.hjy.mtpattern.chap10.tss.example;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hjy on 17-12-8.
 * 使用Thread Specific Storage模式实现参数传递的效果
 */
public class ImplicitParameterPassing {

    public static void main(String[] args) throws InterruptedException{
        ClientThread thread;
        BusinessService bs = new BusinessService();
        for (int i = 0; i < Integer.valueOf(args[0]); i++) {
            thread = new ClientThread("test",bs);
            thread.start();
            thread.join();
        }
    }



    static class ClientThread extends Thread{
        private final String message;
        private final BusinessService bs;
        private static final AtomicInteger SEQ = new AtomicInteger(0);

        public ClientThread(String message, BusinessService bs) {
            this.message = message;
            this.bs = bs;
        }

        @Override
        public void run() {
            Context.INSTANCE.setTransactionId(SEQ.getAndIncrement());
            bs.service(message);
        }
    }


    static class Context{

        private static final ThreadLocal<Integer> TS_OBJECT_PROXY = new ThreadLocal<Integer>();

        public static final Context INSTANCE = new Context();

        //私有构造器
        private Context(){

        }

        public Integer getTransactionId(){
            return TS_OBJECT_PROXY.get();
        }

        public void setTransactionId(Integer transactionId){
            TS_OBJECT_PROXY.set(transactionId);
        }

        public void reset(){
            TS_OBJECT_PROXY.remove();;
        }


    }

    static class BusinessService{
        public void service(String message){
            int transactionId = Context.INSTANCE.getTransactionId();
            System.out.println("processing transaction "+transactionId + " 's message:"+message);
        }
    }

}
