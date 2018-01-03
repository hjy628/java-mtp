package com.hjy.mtpattern.chap13;

/**
 * Created by hjy on 18-1-3.
 * 对各个处理阶段的计算环境进行抽象，主要用于异常处理
 */
public interface PipeContext {

    /**
     *   @Description   用于对处理阶段抛出的异常进行处理
     *   @param   * @param null
     *   @Date: 下午2:03 18-1-3
     */
    void handleError(PipeException exp);

}
