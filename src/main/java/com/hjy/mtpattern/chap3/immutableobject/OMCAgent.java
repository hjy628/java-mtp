package com.hjy.mtpattern.chap3.immutableobject;

/**
 * Created by hjy on 17-12-5.
 * 处理彩信中心、路由表的变更
 * 与运维中心对接的类
 * 模式角色:ImmutableObject.Manipulator
 */
public class OMCAgent extends Thread{

    @Override
    public void run() {
        boolean isTableModificationMsg = false;
        String updatedTableName = null;
        while (true){
            //省略其他代码

            //从与OMC连接的Socket中读取消息并进行解析，解析到数据表更新信息后，重置MMSCRouter实例
            if (isTableModificationMsg){
                if ("MMSCInfo".equals(updatedTableName)){
                    MMSCRouter.setInstance(new MMSCRouter());
                }
            }

            //省略其他代码

        }
    }
}
