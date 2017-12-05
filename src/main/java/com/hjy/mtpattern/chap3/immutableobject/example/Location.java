package com.hjy.mtpattern.chap3.immutableobject.example;

/**
 * Created by hjy on 17-12-5.
 * 状态可变的位置信息模型(非线程安全)
 */
public class Location {

    private double x;
    private double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setXY(double x,double y) {
        this.x = x;
        this.y = y;
    }
}
