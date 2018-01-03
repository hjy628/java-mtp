package com.hjy.mtpattern.chap13;

/**
 * Created by hjy on 18-1-3.
 * 对复合Pipe的抽象，一个Pipeline实例可包含多个Pipe实例
 * @param <IN> 输入类型
 * @param <OUT> 输出类型
 */
public interface Pipeline<IN,OUT> extends Pipe<IN,OUT> {

    /**
     *   @Description 往该Pipeline实例中添加一个Pipe实例
     *   @param   pipe Pipe实例
     *   @Date: 下午3:57 18-1-3
     */
    void addPipe(Pipe<?,?> pipe);
}
