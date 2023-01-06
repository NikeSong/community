package com.example.community.util;

import com.example.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 工具专门储存用户信息，用于代替session对象
 * 储存ticket查到的用户信息，同时做到不同线程之间的隔离
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();//实现线程隔离，一个set一个get
    public void setUser(User user)
    {
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
