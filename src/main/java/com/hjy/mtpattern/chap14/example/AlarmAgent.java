package com.hjy.mtpattern.chap14.example;

import com.hjy.mtpattern.chap4.gs.Blocker;
import com.hjy.mtpattern.chap4.gs.ConditionVarBlocker;
import com.hjy.mtpattern.chap4.gs.GuardedAction;
import com.hjy.mtpattern.chap4.gs.Predicate;
import com.hjy.util.Debug;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by hjy on 17-12-5.
 * 负责连接告警服务器，并发送告警信息至告警服务器
 */
public class AlarmAgent {

    //用于记录AlarmAgent是否连接上告警服务器
    private volatile boolean connectedToServer = false;

    //模式角色：GuardedSuspension.Predicate
    private final Predicate agentConnected = new Predicate() {
        @Override
        public boolean evaluate() {
            return connectedToServer;
        }
    };

    //模式角色：GuardedSuspension.Blocker
    private final Blocker blocker = new ConditionVarBlocker();

    //心跳定时器
    private final Timer heartbeatTimer = new Timer(true);

    //省略其他代码


    /**
     *   @Author
     *   @Description   发送告警信息
     *   @Param   alarm告警信息
     *   @Date: 下午5:57 17-12-5
     */
    public void sendAlarm(final AlarmInfo alarm)throws Exception{
        //可能需要等待，知道AlarmAgent连接上告警服务器(或者连接中断后重新连上服务器)
        //模式角色：GuardedSuspension.GuardedAction
        GuardedAction<Void> guardedAction = new GuardedAction<Void>(agentConnected) {
            @Override
            public Void call() throws Exception {
                doSendAlarm(alarm);
                return null;
            }
        };
        blocker.callWithGuard(guardedAction);
    }

    //通过网络连接将告警信息发送给告警服务器
    private void doSendAlarm(AlarmInfo alarm){
        //省略其他代码
        Debug.info("sending alarm "+alarm);

        //模拟发送告警至服务器的耗时
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        }catch (Exception e){

        }
    }

    public void init(){
        //省略其他代码

        //告警连接线程
        Thread connectingThread = new Thread(new ConnectionTask());

        connectingThread.start();
        heartbeatTimer.schedule(new HeartbeatTask(),60000,2000);
    }


    public void disconnect(){
        //省略其他代码
        Debug.info("disconnected from alarm server.");
        connectedToServer = false;
    }

    protected void onConnected(){
        try {
            blocker.signalAfter(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    connectedToServer = true;
                    Debug.info("connected to server");
                    return Boolean.TRUE;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void onDisconnected(){
        connectedToServer = false;
    }

    //负责与告警服务器建立网络连接
    private class ConnectionTask implements Runnable{
        @Override
        public void run() {
            //省略其他代码
            //模拟连接操作耗时
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            }catch (InterruptedException e){
                ;
            }

            onConnected();
        }
    }

    //心跳定时任务：定时检查与告警服务器的连接是否正常，发现连接异常后自动重新连接
    private class HeartbeatTask extends TimerTask{
        //省略其他代码

        @Override
        public void run() {
            //省略其他代码

            if (!testConnection()){
                onConnected();
                reconnect();
            }
        }




        private boolean testConnection(){
            //省略其他代码
            return true;
        }


        private void reconnect(){
            ConnectionTask connectionThread = new ConnectionTask();

            //直接在心跳定时器线程中执行
            connectionThread.run();
        }
    }





}
