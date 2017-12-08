package com.hjy.mtpattern.chap10.tss.example.memoryleak;

/**
 * Created by hjy on 17-12-8.
 */
public class Counter {
    private int i=0;

    public int getAndIncrement(){
        return (i++);
    }
}
