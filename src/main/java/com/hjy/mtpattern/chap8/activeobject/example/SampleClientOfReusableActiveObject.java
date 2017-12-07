package com.hjy.mtpattern.chap8.activeobject.example;

import com.hjy.mtpattern.chap8.activeobject.ActiveObjectProxy;
import com.hjy.util.Debug;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by hjy on 17-12-7.
 */
public class SampleClientOfReusableActiveObject {

    public static void main(String[] args) throws InterruptedException,
            ExecutionException {

        SampleActiveObject sao = ActiveObjectProxy.newInstance(
                SampleActiveObject.class, new SampleActiveObjectImpl(),
                Executors.newCachedThreadPool());
        Future<String> ft = null;

        Debug.info("Before calling active object");
        try {
            ft = sao.process("Something", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //模拟其它操作的时间消耗
        Thread.sleep(40);

        Debug.info(ft.get());
    }


}
