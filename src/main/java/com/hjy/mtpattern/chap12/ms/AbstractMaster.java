package com.hjy.mtpattern.chap12.ms;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * Created by hjy on 18-1-2.
 * Master-Slave模式Master参与者的可复用实现
 * @param <T> 子任务对象类型
 * @param <V> 子任务的处理结果类型
 * @param <R> 原始任务处理结果类型
 */
public abstract class AbstractMaster<T,V,R> {

    protected volatile Set<? extends SlaveSpec<T,V>> slaves;

    //子任务派发算法策略
    private volatile SubTaskDispatchStrategy<T,V> dispatchStrategy;

    public AbstractMaster() {
    }

    protected void init(){
        slaves = createSlaves();
        dispatchStrategy = newSubTaskDispatchStrategy();
        for (SlaveSpec<T, V> slave: slaves){
            slave.init();
        }
    }

    /**
     *   @Description   对子类暴露的服务方法，该类的子类需要定义一个比该方法命名更为具体的服务方法
     *   (如downloadFileService)
     *   该命名含义具体的服务方法(如downloadFileService)调用该方法
     *   该方法使用了template（模板）模式、Strategy(策略)模式
     *   @Param   params 客户端代码传递的参数列表
     *   @Date: 下午3:56 18-1-2
     */
    protected R service(Object... params)throws Exception{
        final TaskDivideStrategy<T> taskDivideStrategy = newTaskDivideStrategy(params);

        /*
        * 对原始任务进行分解，并将分解得来的子任务派发给Slave参与者实例,这里使用了Strategy
        * (策略)模式：原始任务分解和子任务派发
        * 这两个具体的计算是通过调用需要的算法策略(对象)实现的
        */
        Iterator<Future<V>> subResults = dispatchStrategy.dispatch(slaves,taskDivideStrategy);

        //等待Slave实例处理结束
        for (SlaveSpec<T,V> slave:slaves) {
            slave.shutdown();
        }

        //合并子任务的处理结果
        R result = combineResults(subResults);
        return result;
    }



    /**
     *   @Description   留给子类实现，用于创建原始任务分解算法策略
     *   @param params 客户端代码调用service方法时传递的参数列表
     *   @Date: 下午3:42 18-1-2
     */
    protected abstract TaskDivideStrategy<T> newTaskDivideStrategy(Object... params);

    /**
     *   @Description   用于创建子任务派发算法策略，默认使用轮询(Round-Robin)派发算法.
     *   @return 子任务派发算法策略实例
     *   @Date: 下午3:47 18-1-2
     */
    protected  SubTaskDispatchStrategy<T,V> newSubTaskDispatchStrategy(){
        return new RoundRobinSubTaskDispatchStrategy<T, V>();
    }

    /**
     *   @Description   留给子类实现，用于创建Slave参与者实例
     *   @return 一组Slave参与者实例
     *   @Date: 下午3:42 18-1-2
     */
    protected abstract Set<? extends SlaveSpec<T,V>> createSlaves();

    /**
     *   @Description   留给子类实现，用于合并子任务的处理结果
     *   @param subResults 各个子任务处理结果
     *   @return  原始任务的处理结果
     *   @Date: 下午3:45 18-1-2
     */
    protected abstract R combineResults(Iterator<Future<V>> subResults);

}
