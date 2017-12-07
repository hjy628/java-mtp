package com.hjy.mtpattern.chap8.activeobject.example;

import java.util.concurrent.Future;

/**
 * Created by hjy on 17-12-7.
 */
public interface SampleActiveObject {

    public Future<String> process(String arg, int i);

}
