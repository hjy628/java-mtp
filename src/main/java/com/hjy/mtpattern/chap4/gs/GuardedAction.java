package com.hjy.mtpattern.chap4.gs;

import java.util.concurrent.Callable;

/**
 * Created by hjy on 17-12-5.
 */
public abstract class GuardedAction<V> implements Callable<V> {
    protected  final Predicate guard;

    public GuardedAction(Predicate guard) {
        this.guard = guard;
    }
}
