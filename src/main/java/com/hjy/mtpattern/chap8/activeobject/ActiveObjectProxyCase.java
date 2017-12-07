package com.hjy.mtpattern.chap8.activeobject;

import com.hjy.mtpattern.chap8.activeobject.example.SampleActiveObject;
import com.hjy.mtpattern.chap8.activeobject.example.SampleActiveObjectImpl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by hjy on 17-12-7.
 * 基于可复用的API快速实现Active Object模式
 */
public class ActiveObjectProxyCase {

    public static void main(String[] args) throws InterruptedException,ExecutionException{
        SampleActiveObject sao = ActiveObjectProxy.newInstance(
                SampleActiveObject.class,new SampleActiveObjectImpl(), Executors.newCachedThreadPool());
        Future<String> ft = sao.process("Something",1);

        TimeUnit.MILLISECONDS.sleep(50);

        while (!ft.isDone()){
            TimeUnit.MILLISECONDS.sleep(50);
            System.out.println(ft.isCancelled());
        }
        System.out.println(ft.get());

    }


}
