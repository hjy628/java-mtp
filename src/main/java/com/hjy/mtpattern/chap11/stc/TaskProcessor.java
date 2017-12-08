package com.hjy.mtpattern.chap11.stc;

/**
 * Created by hjy on 17-12-8.
 * 对任务处理的抽象
 * @param <T> 表示任务的类型
 * @param <V> 表示任务处理结果的类型
 */
public interface TaskProcessor<T,V> {

    /**
     *   @Description 对指定任务进行处理
     *   @Param   task 任务
     *   @return  任务处理结果
     *   @throws Exception
     *   @Date: 下午5:19 17-12-8
     */
    V doProcess(T task)throws Exception;
}
