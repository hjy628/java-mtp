package com.hjy.mtpattern.chap4.gs;

import java.util.concurrent.Callable;

/**
 * Created by hjy on 17-12-5.
 */
public interface Blocker {

    /**
     *   @Author
     *   @Description   在保护条件成立时执行目标动作，否则阻塞当前线程，直到保护条件成立
     *   @Param  guardedAction 带保护条件的目标动作
     *   @Date: 下午5:35 17-12-5
     */
    <V> V callWithGuard(GuardedAction<V> guardedAction)throws Exception;



    /**
     *   @Author
     *   @Description   执行stateOperation所指定的操作后，决定是否唤醒本Blocker所暂挂的所有线程中的一个线程
     *   @Param   stateOperation 更改状态的操作，其call方法的返回值为true时，该方法才会唤醒被暂挂的线程
     *   @Date: 下午5:37 17-12-5
     */
    void signalAfter(Callable<Boolean> stateOperation)throws Exception;

    void signal() throws InterruptedException;

    /**
     *   @Author
     *   @Description 执行stateOperation所指定的操作后，决定是否唤醒本Blocker所暂挂的所有线程中的所有线程
     *   @Param   stateOperation 更改状态的操作，其call方法的返回值为true时，该方法才会唤醒被暂挂的线程
     *   @Date: 下午5:40 17-12-5
     */
    void broadcastAfter(Callable<Boolean> stateOperation) throws Exception;
}
