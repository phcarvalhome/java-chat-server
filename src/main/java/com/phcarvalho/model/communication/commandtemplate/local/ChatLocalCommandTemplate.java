package com.phcarvalho.model.communication.commandtemplate.local;

import com.phcarvalho.dependencyfactory.DependencyFactory;
import com.phcarvalho.model.MainModel;
import com.phcarvalho.model.communication.commandtemplate.IChatCommandTemplate;
import com.phcarvalho.model.communication.protocol.vo.command.BecomeOfflineCommand;
import com.phcarvalho.model.communication.protocol.vo.command.BecomeOnlineCommand;
import com.phcarvalho.model.communication.protocol.vo.command.SendMessageCommand;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatLocalCommandTemplate extends UnicastRemoteObject implements IChatCommandTemplate {

    private MainModel mainModel;

    public ChatLocalCommandTemplate() throws RemoteException {
        super();
        mainModel = DependencyFactory.getSingleton().get(MainModel.class);
    }

    public void sendMessage(SendMessageCommand sendMessageCommand){
        mainModel.sendMessage(sendMessageCommand);
    }

    @Override
    public void becomeOnline(BecomeOnlineCommand becomeOnlineCommand) throws RemoteException {
        mainModel.becomeOnline(becomeOnlineCommand);
    }

    @Override
    public void becomeOffline(BecomeOfflineCommand becomeOfflineCommand) throws RemoteException {
        mainModel.becomeOffline(becomeOfflineCommand);
    }
}
