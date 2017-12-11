package com.hjy.mtpattern.chap12.ms.example;


/**
 * Created by hjy on 17-12-6.
 * AbstractTerminatableThread类
 * 可停止的抽象线程
 * 模式角色:Two-phaseTermination.AbstractTerminatableThread
 */
public abstract class AbstractTerminatableThread extends Thread implements Terminatable {

    //模式角色:Two-phaseTermination.TerminationToken
    public final TerminationToken terminationToken;

    public AbstractTerminatableThread() {
        this(new TerminationToken());
    }


    /**
     *   @Param  terminationToken 线程间共享的线程终止标志实例
     *   @Date: 下午3:46 17-12-6
     */
    public AbstractTerminatableThread(TerminationToken terminationToken) {
        super();
        this.terminationToken = terminationToken;
        terminationToken.register(this);
    }

    /**
     *   @Description   留给子类实现其线程处理逻辑
     *   @Date: 下午3:46 17-12-6
     */
    protected abstract void doRun() throws Exception;

    /**
     *   @Description 留给子类实现，用于实现线程停止后的一些清理动作
     *   @Param   cause
     *   @Date: 下午3:48 17-12-6
     */
    protected void doCleanup(Exception cause){
        //什么也不做
    }

    /**
     *   @Description 留给子类实现，用于执行线程停止所需的操作
     *   @Param   cause
     *   @Date: 下午3:48 17-12-6
     */
    protected void doTerminate( ){
        //什么也不做
    }

    @Override
    public void run() {
        Exception ex = null;
        try {
            for (;;){
                //在执行线程的处理逻辑前先判断线程停止的标志
                if (terminationToken.isToShutdown()&&terminationToken.reservations.get()<=0){
                    break;
                }
                doRun();
            }
        }catch (Exception e){
            //使得线程能够相应interrupt调用而退出
            ex = e;
        }finally {
            try {
                doCleanup(ex);
            }finally {
                terminationToken.notifyThreadTermination(this);
            }
        }
    }

    @Override
    public void interrupt() {
        terminate();
    }


    /**
     *   @Description   请求停止线程
     *   @Date: 下午3:56 17-12-6
     */
    @Override
    public void terminate() {
        terminationToken.setToShutdown(true);
        try {
            doTerminate();
        }finally {
            //若无待处理的任务，则试图强制终止线程
            if (terminationToken.reservations.get()<=0){
                super.interrupt();
            }
        }
    }

    public void terminate(boolean waitUtilThreadTerminated){
        terminate();
        if (waitUtilThreadTerminated){
            try {
                this.join();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }
}
