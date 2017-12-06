package com.hjy.mtpattern.chap4.gs.example;

import com.hjy.mtpattern.chap4.gs.Blocker;
import com.hjy.mtpattern.chap4.gs.ConditionVarBlocker;
import com.hjy.mtpattern.chap4.gs.GuardedAction;
import com.hjy.mtpattern.chap4.gs.Predicate;
import com.hjy.util.Debug;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

/**
 * Created by hjy on 17-12-6.
 * 　嵌套监视器锁死锁示例代码
 */
public class NestedMonitorLockoutExample {

    public static void main(String[] args) {
        final Helper helper = new Helper();
        Debug.info("Before calling guaredMethod.");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String result;
                result = helper.xGuardedMethod("test");
                Debug.info(result);
            }
        });
        t.start();

        final Timer timer = new Timer();

        //延迟50ms调用helper.stateChanged方法
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                helper.xStateChanged();
                timer.cancel();
            }
        },5,10);

    }


    private static class Helper{
        private volatile boolean isStateOK = false;
        private final Predicate stateBeOK = new Predicate() {
            @Override
            public boolean evaluate() {
                return isStateOK;
            }
        };

        private final Blocker blocker = new ConditionVarBlocker();

        public synchronized String xGuardedMethod(final String message){
            GuardedAction<String> ga = new GuardedAction<String>(stateBeOK) {
                @Override
                public String call() throws Exception {
                    return message+"->received.";
                }
            };
            String result = null;
            try {
                result = blocker.callWithGuard(ga);
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }

        public synchronized void xStateChanged(){
            try {
                blocker.signalAfter(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        isStateOK = true;
                        Debug.info("state ok.");
                        return Boolean.TRUE;
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


}
