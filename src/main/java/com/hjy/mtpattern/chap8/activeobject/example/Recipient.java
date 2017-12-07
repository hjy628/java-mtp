package com.hjy.mtpattern.chap8.activeobject.example;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hjy on 17-12-7.
 */
public class Recipient implements Serializable{
    private static final long serialVersionUID = -241108575776728946L;

    private Set<String> to = new HashSet<String>();

    public void addTo(String msisdn){
        to.add(msisdn);
    }

    public Set<String> getToList(){
        return (Set<String>) Collections.unmodifiableCollection(to);
    }


}
