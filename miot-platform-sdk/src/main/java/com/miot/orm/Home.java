package com.miot.orm;

import java.util.ArrayList;
import java.util.List;

public class Home extends Object{
    private int masterId;
    private List<Room> rooms = new ArrayList<Room>();
    private List<Scence> scences = new ArrayList<Scence>();

    public List<Room> getRooms() {
        return rooms;
    }
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
    public List<Scence> getScences() {
        return scences;
    }
    public void setScences(List<Scence> scences) {
        this.scences = scences;
    }
    public int getMasterId() {
        return masterId;
    }
    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }
}
