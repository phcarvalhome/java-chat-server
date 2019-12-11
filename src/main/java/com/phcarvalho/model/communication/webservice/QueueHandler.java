package com.phcarvalho.model.communication.webservice;

import com.phcarvalho.dependencyfactory.DependencyFactory;
import com.phcarvalho.model.communication.jms.QueueService;
import com.phcarvalho.model.configuration.entity.User;

import javax.jws.WebService;

@WebService(endpointInterface = "com.phcarvalho.model.communication.webservice.IQueueHandler")
public class QueueHandler implements IQueueHandler {

    private QueueService queueService;

    public QueueHandler() {
        queueService = DependencyFactory.getSingleton().get(QueueService.class);
    }

    @Override
    public void create(User user) {
        queueService.createQueue(user);
    }
}
