package com.hjy.mtpattern.chap3.immutableobject.example;

/**
 * Created by hjy on 17-12-5.
 * 状态不可变的位置信息模型
 */
public class ImmutableLocation {

    private final double x;
    private final double y;

    public ImmutableLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }


}
