
package com.miot.orm;

import java.util.ArrayList;
import java.util.List;

public class Room extends Object {
    private int floor;

    private int type;

    private List<Pu> pus = new ArrayList<Pu>();

    private String homeId;

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public List<Pu> getPus() {
        return pus;
    }

    public void setPus(List<Pu> pus) {
        this.pus = pus;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
