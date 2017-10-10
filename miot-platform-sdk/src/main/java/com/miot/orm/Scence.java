
package com.miot.orm;

import java.util.ArrayList;
import java.util.List;


public class Scence extends Object {
    private String homeId;

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }
    List<ScencePu> scencePus = new ArrayList<ScencePu>();

    public List<ScencePu> getScencePus() {
        return scencePus;
    }

    public void setScencePus(List<ScencePu> scencePus) {
        this.scencePus = scencePus;
    }

}
