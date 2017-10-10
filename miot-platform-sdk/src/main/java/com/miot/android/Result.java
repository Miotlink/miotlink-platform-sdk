package com.miot.android;

import java.util.logging.Logger;

public class Result {
    public final static int CODE_FAIL = 0;
    public final static int CODE_SUCCESS = 1;
    public final static String MSG_FAIL = "fail";
    public final static String MSG_SUCCESS = "success";

    @Override
    public String toString(){
        return "Result:{code:"+code+",msg:'"+(msg==null?"null":msg)+"',data:"+(data==null?"null":data)+"}";
    }

    public Result success(Object data){
        setCode(CODE_SUCCESS);
        setMsg(MSG_SUCCESS);
        setData(data);
        return this;
    }

    public Result fail(String msg){
        setCode(CODE_FAIL);
        String s =(msg==null || msg.equals(""))?MSG_FAIL:msg;
        setMsg(s);
       
        return this;
    }

    private int code = CODE_FAIL;
    private String msg = "UNDO";

    public int getCode() {
        return code;
    }
    public Result setCode(int code) {
        this.code = code;
        return this;
    }
    public String getMsg() {
        return msg;
    }
    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    public Object getData() {
        return data;
    }
    public Result setData(Object data) {
        this.data = data;
        return this;
    }
    private Object data = null;
}
