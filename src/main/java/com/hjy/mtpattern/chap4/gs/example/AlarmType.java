package com.hjy.mtpattern.chap4.gs.example;

/**
 * Created by hjy on 17-12-5.
 */
public enum  AlarmType {
    FAULT("fault"),
    RESUME("resume");

    private String name;

    AlarmType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
