package com.phcarvalho.model;

import com.phcarvalho.controller.MainController;
import com.phcarvalho.dependencyfactory.DependencyFactory;
import com.phcarvalho.model.communication.commandtemplate.remote.adapter.ChatRemoteCommandTemplateAdapter;
import com.phcarvalho.model.communication.jms.QueueService;
import com.phcarvalho.model.communication.protocol.vo.command.BecomeOfflineCommand;
import com.phcarvalho.model.communication.protocol.vo.command.BecomeOnlineCommand;
import com.phcarvalho.model.communication.protocol.vo.command.SendMessageCommand;
import com.phcarvalho.model.configuration.Configuration;
import com.phcarvalho.model.configuration.entity.User;
import com.phcarvalho.model.vo.OfflineMessageList;

public class MainModel {

    private MainController controller;
    private ConnectionModel connectionModel;
    private ChatRemoteCommandTemplateAdapter chatRemoteCommandTemplateAdapter;
    private QueueService queueService;

    public MainModel(MainController controller) {
        this.controller = controller;
        connectionModel = DependencyFactory.getSingleton().get(ConnectionModel.class);
        chatRemoteCommandTemplateAdapter = DependencyFactory.getSingleton().get(ChatRemoteCommandTemplateAdapter.class);
        queueService = DependencyFactory.getSingleton().get(QueueService.class);
    }

    public void sendMessage(SendMessageCommand sendMessageCommand) {
        User targetUser = sendMessageCommand.getTargetUser();
        Boolean remoteUserOnline = Configuration.getSingleton().isRemoteUserOnline(targetUser);

        if(remoteUserOnline)
            chatRemoteCommandTemplateAdapter.sendMessage(sendMessageCommand, targetUser);
        else
            queueService.sendMessage(sendMessageCommand);
    }

    public void becomeOnline(BecomeOnlineCommand becomeOnlineCommand) {
        User sourceUser = becomeOnlineCommand.getSourceUser();
        OfflineMessageList offlineMessageList = Configuration.getSingleton().getOfflineMessageList(sourceUser);

        becomeOnlineCommand.setOfflineMessageList(offlineMessageList);
        Configuration.getSingleton().setRemoteUserAsOnline(sourceUser);
        Configuration.getSingleton().getRemoteUserList()
                .forEach(remoteUser -> chatRemoteCommandTemplateAdapter
                        .becomeOnline(becomeOnlineCommand, remoteUser));
        offlineMessageList.clear();
    }

    public void becomeOffline(BecomeOfflineCommand becomeOfflineCommand) {
        User sourceUser = becomeOfflineCommand.getSourceUser();

        Configuration.getSingleton().setRemoteUserAsOffline(sourceUser);
        Configuration.getSingleton().getRemoteUserList(sourceUser)
                .forEach(remoteUser -> chatRemoteCommandTemplateAdapter
                        .becomeOffline(becomeOfflineCommand, remoteUser));
    }
}
