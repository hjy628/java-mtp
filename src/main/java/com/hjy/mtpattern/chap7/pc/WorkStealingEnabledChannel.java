package com.hjy.mtpattern.chap7.pc;

import java.util.concurrent.BlockingDeque;

/**
 * Created by hjy on 17-12-7.
 */
public interface WorkStealingEnabledChannel<P> extends Channel<P> {

    P take(BlockingDeque<P> preferredQueue)throws InterruptedException;

}
