package com.hjy.mtpattern.chap8.activeobject.example;

import com.hjy.util.Debug;

import java.util.concurrent.Future;

/**
 * Created by hjy on 17-12-7.
 */
public class SampleActiveObjectImpl{
    public String doProcess(String arg, int i) {
        Debug.info("doProcess start");
        try {
            // 模拟一个比较耗时的操作
            Thread.sleep(50);
        } catch (InterruptedException e) {
            ;
        }
        return arg + "-" + i;
    }


}
