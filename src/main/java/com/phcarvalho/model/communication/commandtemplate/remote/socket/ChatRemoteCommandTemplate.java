package com.phcarvalho.model.communication.commandtemplate.remote.socket;

import com.phcarvalho.dependencyfactory.DependencyFactory;
import com.phcarvalho.model.communication.commandtemplate.IChatCommandTemplate;
import com.phcarvalho.model.communication.protocol.vo.command.SendMessageCommand;
import com.phcarvalho.model.communication.strategy.socket.SocketConnectionStrategy;
import com.phcarvalho.model.configuration.entity.User;

import java.rmi.RemoteException;
import java.util.Objects;

public class ChatRemoteCommandTemplate implements IChatCommandTemplate {

    private User remoteUser;
    private SocketConnectionStrategy socketConnectionStrategy;

    public ChatRemoteCommandTemplate(User remoteUser) {
        this.remoteUser = remoteUser;
        socketConnectionStrategy = DependencyFactory.getSingleton().get(SocketConnectionStrategy.class);
    }

    @Override
    public void sendMessage(SendMessageCommand sendMessageCommand) throws RemoteException {
        socketConnectionStrategy.send(sendMessageCommand, remoteUser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRemoteCommandTemplate that = (ChatRemoteCommandTemplate) o;
        return Objects.equals(remoteUser, that.remoteUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(remoteUser);
    }
}
