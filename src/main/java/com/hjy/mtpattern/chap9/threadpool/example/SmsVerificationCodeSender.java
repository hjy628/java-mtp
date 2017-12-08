package com.hjy.mtpattern.chap9.threadpool.example;

import java.text.DecimalFormat;
import java.util.concurrent.*;

/**
 * Created by hjy on 17-12-8.
 * 验证码短信下发源码
 */
public class SmsVerificationCodeSender {

    private static final ExecutorService EXECUTOR  = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors(),
            60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r,"VerfCodeSender");
            t.setDaemon(true);
            return t;
        }
    },new ThreadPoolExecutor.DiscardPolicy());


    public void sendVerificationSms(final String msisdn){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                //生成强随机数验证码
                int verificationCode = ThreadSpecificSecureRandom.INSTANCE.nextInt(999999);
                DecimalFormat df = new DecimalFormat("000000");
                String txtVerCode = df.format(verificationCode);

                //发送验证码短信
                sendSms(msisdn,txtVerCode);
            }
        };

        EXECUTOR.submit(task);
    }

    private void sendSms(String msisdn,String verificationCode){
        System.out.println("Sending verification code "+verificationCode+"to"+msisdn);
        //短信发送
    }

}
