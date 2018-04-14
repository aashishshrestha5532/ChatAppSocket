package com.alchemist.evlivechat;

/**
 * Created by Ashish Alton on 3/6/2018.
 */

public class Data {
    private String message;
    private String name;


    public Data(String message,String name){
        this.message=message;
        this.name=name;
    }
    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}
