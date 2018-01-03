package com.hjy.mtpattern.chap13;

/**
 * Created by hjy on 18-1-3.
 */
public class PipeException extends Exception{
    private static final long serialVersionUID = -4128988617937348822L;

    /*
    * 抛出异常的Pipe实例
    */
    public final Pipe<?,?> sourcePipe;


    /*
    * 抛出异常的Pipe实例在抛出异常时所处理的输入元素
    */
    public final Object input;

    public PipeException(Pipe<?, ?> sourcePipe, Object input,String message) {
        super(message);
        this.sourcePipe = sourcePipe;
        this.input = input;
    }

    public PipeException(String message, Throwable cause, Pipe<?, ?> sourcePipe, Object input) {
        super(message, cause);
        this.sourcePipe = sourcePipe;
        this.input = input;
    }
}
