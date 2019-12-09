package com.phcarvalho.model;

import com.phcarvalho.controller.ConnectedUserController;
import com.phcarvalho.dependencyfactory.DependencyFactory;
import com.phcarvalho.model.communication.commandtemplate.remote.adapter.ConnectionRemoteCommandTemplateAdapter;
import com.phcarvalho.model.communication.protocol.vo.command.ConnectCommand;
import com.phcarvalho.model.communication.protocol.vo.command.DisconnectCommand;
import com.phcarvalho.model.configuration.Configuration;
import com.phcarvalho.model.configuration.entity.User;

import javax.swing.*;
import java.util.List;

public class ConnectedUserModel {

    private ConnectedUserController controller;
    private ConnectionRemoteCommandTemplateAdapter connectionRemoteCommandTemplateAdapter;
    private DefaultListModel<User> list;

    public ConnectedUserModel(ConnectedUserController controller) {
        this.controller = controller;
        connectionRemoteCommandTemplateAdapter = DependencyFactory.getSingleton()
                .get(ConnectionRemoteCommandTemplateAdapter.class);
        list = new DefaultListModel();
    }

    public void add(ConnectCommand connectCommand) {
        User sourceUser = connectCommand.getSourceUser();

        Configuration.getSingleton().addRemoteUser(sourceUser);
        controller.add(sourceUser);
        list.addElement(sourceUser);

        List<User> remoteUserList = Configuration.getSingleton().getRemoteUserList();

        connectCommand.setConnectedUserList(remoteUserList);
        remoteUserList.forEach(remoteUser -> connectionRemoteCommandTemplateAdapter
                .connect(connectCommand, remoteUser));
    }

    public void remove(DisconnectCommand disconnectCommand) {
        User sourceUser = disconnectCommand.getSourceUser();

        remove(sourceUser);
        connectionRemoteCommandTemplateAdapter.disconnect(disconnectCommand, sourceUser);
    }

    private void remove(User sourceUser) {
        Configuration.getSingleton().removeRemoteUser(sourceUser);
        controller.remove(sourceUser);
        list.removeElement(sourceUser);
    }

    public void remove() {
//        User sourceUser = addUserCommand.getSourceUser();
//
//        remove(sourceUser);
//
////        Integer gameId = addUserCommand.getGame().getId();
//
////        Configuration.getSingleton().getGame(gameId).removeUser(sourceUser);
//        Configuration.getSingleton().getRemoteUserList()
//                .forEach(remoteUser -> mainRemoteCommandTemplateAdapter.addPlayer(addUserCommand, remoteUser));
    }

    public DefaultListModel<User> getList() {
        return list;
    }
}
