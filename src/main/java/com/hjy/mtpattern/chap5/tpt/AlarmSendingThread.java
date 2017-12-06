package com.hjy.mtpattern.chap5.tpt;


import com.hjy.mtpattern.chap5.tpt.example.AlarmAgent;
import com.hjy.mtpattern.chap5.tpt.example.AlarmInfo;
import com.hjy.mtpattern.chap5.tpt.example.AlarmType;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hjy on 17-12-6.
 *  模式角色:Two-phaseTermination.ConcreteTerminatableThread
 */
public class AlarmSendingThread extends AbstractTerminatableThread{

    private final AlarmAgent alarmAgent = new AlarmAgent();

    //告警队列
    private final BlockingQueue<AlarmInfo> alarmQueue;
    private final ConcurrentHashMap<String,AtomicInteger> submittedAlarmRegistry;

    public AlarmSendingThread() {
        alarmQueue = new ArrayBlockingQueue<AlarmInfo>(100);
        submittedAlarmRegistry = new ConcurrentHashMap<String,AtomicInteger>();
        alarmAgent.init();
    }

    @Override
    protected void doRun() throws Exception {
        AlarmInfo alarm;
        alarm = alarmQueue.take();
        terminationToken.reservations.decrementAndGet();

        try {
            //将告警信息发送至告警服务器
            alarmAgent.sendAlarm(alarm);
        }catch (Exception e){
            e.printStackTrace();
        }


        /**
         *   @Description   处理恢复告警：将相应的故障告警从注册表中删除，使得相应故障恢复后若再次出现相同故障，该故障信息能够上报到服务器
         *   @Date: 下午4:08 17-12-6
         */
        if (AlarmType.RESUME == alarm.type){
            String key = AlarmType.FAULT.toString() +":" +alarm.getId() +"@"+alarm.getExtraInfo();
            submittedAlarmRegistry.remove(key);

            key = AlarmType.RESUME.toString() +":"+alarm.getId()+"@"+alarm.getExtraInfo();
            submittedAlarmRegistry.remove(key);
        }
    }

    public int sendAlarm(final AlarmInfo alarmInfo){
        AlarmType type = alarmInfo.type;
        String id = alarmInfo.getId();
        String extraInfo = alarmInfo.getExtraInfo();

        if (terminationToken.isToShutdown()){
            //记录告警
            System.err.println("rejected alarm: "+id + ","+extraInfo);
            return -1;
        }

        int duplicateSubmissionCount = 0;
        try {
            AtomicInteger prevSubmittedCounter;

            prevSubmittedCounter = submittedAlarmRegistry.putIfAbsent(type.toString()+":"+id+"@"+extraInfo,new AtomicInteger(0));
            if (null==prevSubmittedCounter){
                terminationToken.reservations.incrementAndGet();
                alarmQueue.put(alarmInfo);
            }else {
                //故障未恢复，不用重复发送告警信息给服务器，故仅增加计数
                duplicateSubmissionCount = prevSubmittedCounter.incrementAndGet();
            }
        }catch (Throwable t){
            t.printStackTrace();
        }

        return duplicateSubmissionCount;
    }

    @Override
    protected void doCleanup(Exception cause) {
        if (null!=cause&&!(cause instanceof InterruptedException)){
            cause.printStackTrace();
        }
        alarmAgent.disconnect();
    }
}
