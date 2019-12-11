package com.phcarvalho.model;

import com.phcarvalho.controller.ConnectionController;
import com.phcarvalho.dependencyfactory.DependencyFactory;
import com.phcarvalho.model.communication.jms.QueueService;
import com.phcarvalho.model.communication.strategy.IConnectionStrategy;
import com.phcarvalho.model.communication.webservice.WebServicePublisher;
import com.phcarvalho.model.configuration.Configuration;
import com.phcarvalho.model.configuration.entity.User;
import com.phcarvalho.view.util.DialogUtil;

import java.rmi.RemoteException;

public class ConnectionModel {

    public static final String SERVER_CREATION = "Server Creation";

    private ConnectionController controller;
    private IConnectionStrategy connectionStrategy;
    private WebServicePublisher webServicePublisher;
    private QueueService queueService;
    private DialogUtil dialogUtil;

    public ConnectionModel(ConnectionController controller) {
        this.controller = controller;
        connectionStrategy = DependencyFactory.getSingleton().get(IConnectionStrategy.class);
        webServicePublisher = DependencyFactory.getSingleton().get(WebServicePublisher.class);
        queueService = DependencyFactory.getSingleton().get(QueueService.class);
        dialogUtil = DependencyFactory.getSingleton().get(DialogUtil.class);
    }

    public void startServer(User localUser) throws RemoteException {
        Configuration.getSingleton().setLocalUser(localUser);
        connectionStrategy.startServer(localUser);
        dialogUtil.showInformation("The Socket/RMI server is up!", SERVER_CREATION);
        webServicePublisher.publishQueueHandler();
        dialogUtil.showInformation("The web service was published!", SERVER_CREATION);
        queueService.createConnection();
        dialogUtil.showInformation("The queue server is connected!", SERVER_CREATION);
    }
}
