package com.hjy.mtpattern.chap12.ms;

import java.util.concurrent.Future;

/**
 * Created by hjy on 17-12-11.
 * 对Master-Slave模式Slave参与者的抽象
 * @param <T> 子任务类型
 * @param <V> 子任务处理结果类型
 */
public interface SlaveSpec<T,V> {

    /**
     *   @Description 用于Master向其提交一个子任务
     *   @Param   task 子任务
     *   @return 可借以获取子任务处理结果的Promise实例
     *   @throws InterruptedException
     *   @Date: 下午7:56 17-12-11
     */
    Future<V> submit(final T task)throws InterruptedException;

    /**
    *  初始化Slave实例提供的服务
    */
    void init();

    /**
    *  停止Slave实例对外提供的服务
    */
    void shutdown();


}
