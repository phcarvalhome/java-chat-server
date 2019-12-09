package com.phcarvalho.model.configuration;

import com.phcarvalho.model.configuration.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Configuration {

    private static Configuration singleton;

    public static Configuration getSingleton(){

        if(singleton == null)
            singleton = new Configuration();

        return singleton;
    }

    private User localUser;
    private List<User> remoteUserList;
    private Integer gameCounter;

    private Configuration() {
        remoteUserList = new ArrayList<>();
        gameCounter = 0;
    }

    public void addRemoteUser(User remoteUser){
        remoteUserList.add(remoteUser);
    }

    public void removeRemoteUser(User user) {
        remoteUserList.remove(user);
    }

    public List<User> getRemoteUserList(User remoteUser) {
        return remoteUserList.stream()
                .filter(remoteUserElement -> !remoteUserElement.equals(remoteUser))
                .collect(Collectors.toList());
    }

    public User getLocalUser() {

        if(localUser == null)
            throw new RuntimeException("The localUser field is null!");

        return localUser;
    }

    public void setLocalUser(User localUser) {
        this.localUser = localUser;
    }

    public List<User> getRemoteUserList() {
        return remoteUserList;
    }
}
