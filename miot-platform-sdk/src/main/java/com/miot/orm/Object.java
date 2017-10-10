package com.miot.orm;

import java.util.ArrayList;
import java.util.List;

public abstract class Object {
    private static List<String> keyList = null;

    /**
     * ��ȡ��������йؼ���
     * 
     * @return
     */
    public static List<String> getKeys() {
        if (keyList == null) {
            keyList = new ArrayList<String>(4);
            keyList.add("id");
            keyList.add("name");
            keyList.add("userData");
            keyList.add("resourceUrl");
        }
        return keyList;
    }

    private String id = "";
    private String name = "";
    private String userData = "";
    private String resourceUrl = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

}
