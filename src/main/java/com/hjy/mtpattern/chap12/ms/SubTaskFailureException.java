package com.hjy.mtpattern.chap12.ms;

/**
 * Created by hjy on 17-12-29.
 * 表示子任务处理失败的异常
 */
public class SubTaskFailureException extends Exception{

    public final RetryInfo retryInfo;

    /*
    * 对处理失败的子任务进行重试所需的信息
    */
    public SubTaskFailureException(RetryInfo retryInfo,Exception cause) {
        super(cause);
        this.retryInfo = retryInfo;
    }
}
