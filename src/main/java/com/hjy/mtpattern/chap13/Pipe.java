package com.hjy.mtpattern.chap13;

import java.util.concurrent.TimeUnit;

/**
 * Created by hjy on 18-1-3.
 * 对处理阶段的抽象
 * 负责对输入进行处理并将输出做为下一个处理阶段的输入
 *
 * @param <IN> 输入类型
 * @param <OUT> 输出类型
 *
 */
public interface Pipe<IN,OUT> {

    /**
     *   @Description   设置当前Pipe实例的下一个Pipe实例
     *   @param   nextPipe 下一个Pipe实例
     *   @Date: 下午1:58 18-1-3
     */
    void setNextPipe(Pipe<?,?> nextPipe);


    /**
     *   @Description   初始化当前Pipe实例对外提供的服务
     *   @param   pipeCtx　
     *   @Date: 下午1:58 18-1-3
     */
    void init(PipeContext pipeCtx);

    /**
     *   @Description   停止当前Pipe实例对外提供的服务
     *   @param   timeout
     *   @param   unit
     *   @Date: 下午1:59 18-1-3
     */
    void shutdown(long timeout, TimeUnit unit);

    /**
     *   @Description   对输入元素进行处理，并将处理结果做为下一个Pipe实例的输入
     *   @Date: 下午2:00 18-1-3
     */
    void process(IN input) throws InterruptedException;


}
