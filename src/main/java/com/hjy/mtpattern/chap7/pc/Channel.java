package com.hjy.mtpattern.chap7.pc;

/**
 * Created by hjy on 17-12-7.
 * 对通道参与者进行抽象
 *  P “产品”类型
 *
 */
public interface Channel<P> {

    /**
     *   @Description 从通道中取出一个“产品”
     *   @return "产品"
     *   @throws InterruptedException
     *   @Date: 上午9:35 17-12-7
     */
    P take() throws InterruptedException;

    /**
     *   @Description   往通道中存储一个"产品"
     *   @Param   product 产品
     *   @throws InterruptedException
     *   @Date: 上午9:37 17-12-7
     */
    void put(P product) throws InterruptedException;

}
