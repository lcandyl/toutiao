package com.toutiao.model;

import org.springframework.stereotype.Component;

/**
 * Created by admin on 16-7-13.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();
    public User getUser(){
        return  users.get();
    }
    //面试时候肯定会考！！！！
    public void setUser(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();
    }
}
