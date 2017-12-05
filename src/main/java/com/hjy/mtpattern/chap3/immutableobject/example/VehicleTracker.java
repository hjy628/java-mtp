package com.hjy.mtpattern.chap3.immutableobject.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hjy on 17-12-5.
 * 使用不可变对象的情况下更新车辆的位置信息
 */
public class VehicleTracker {

    private Map<String,ImmutableLocation> locMap = new ConcurrentHashMap<String,ImmutableLocation>();

    public void updateLocation(String vehicleId,ImmutableLocation newLocation){
        locMap.put(vehicleId, newLocation);
    }


}
