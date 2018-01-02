package com.hjy.mtpattern.chap12.ms;

/**
 * Created by hjy on 17-12-4.
 */
public class PackageInfo {


    /*
    * 要使用可复用代码实现Master-Slave模式，应用代码需要完成以下几件事情
    * 1.[必需] 创建TaskDivideStrategy接口的实现类，在该类中实现原始任务分解算法.
    * 2.[必需] 创建AbstractMaster的子类，该类除了实现其父类定义的几个抽象方法外，还要定义服务方法，该服务方法的名字比其父类的service方法含义更为具体
    * 3.[必需] 创建WorkerThreadSlave的子类，在该子类中实现其父类的doProcess方法。当然，我们也可以自己编写SlaveSpec接口的实现类
    * 4.[可选] 创建SubTaskDispatchStrategy的实现类，在该类中实现子任务派发算法. AbstractMaster默认使用RoundRobinTaskDispatchStrategy
    */


}
