package com.phcarvalho.model.configuration;

import com.phcarvalho.model.communication.protocol.vo.command.SendMessageCommand;
import com.phcarvalho.model.configuration.entity.User;
import com.phcarvalho.model.vo.OfflineMessageList;

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
    private Map<User, OfflineMessageList> offlineMessageListMap;

    private Configuration() {
        remoteUserList = new ArrayList<>();
        offlineMessageListMap = new HashMap<>();
    }

    public void addRemoteUser(User remoteUser){
        remoteUserList.add(remoteUser);
    }

    public void removeRemoteUser(User user) {
        remoteUserList.remove(user);
    }

    public Boolean isRemoteUserOnline(User remoteUser){
        return getRemoteUser(remoteUser)
                .getOnline();
    }

    private User getRemoteUser(User remoteUser) {
        return remoteUserList.stream()
                .filter(remoteUserElement -> remoteUserElement.equals(remoteUser))
                .findFirst()
                .get();
    }

    public void setRemoteUserAsOnline(User remoteUser){
        getRemoteUser(remoteUser)
                .setOnline(true);
    }

    public void setRemoteUserAsOffline(User remoteUser){
        getRemoteUser(remoteUser)
                .setOnline(false);
    }

    public List<User> getRemoteUserList(User remoteUser) {
        return remoteUserList.stream()
                .filter(remoteUserElement -> !remoteUserElement.equals(remoteUser))
                .collect(Collectors.toList());
    }

    public OfflineMessageList getOfflineMessageList(User user){
        OfflineMessageList offlineMessageList = offlineMessageListMap.get(user);

        if (offlineMessageList == null){
            offlineMessageList = new OfflineMessageList();
            offlineMessageListMap.put(user, offlineMessageList);
        }

        return offlineMessageList;
    }

    public void addOfflineMessage(SendMessageCommand sendMessageCommand){
        User targetUser = sendMessageCommand.getTargetUser();

        getOfflineMessageList(targetUser).add(sendMessageCommand);
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

    public void getOfflineMessageList() {
    }
}
