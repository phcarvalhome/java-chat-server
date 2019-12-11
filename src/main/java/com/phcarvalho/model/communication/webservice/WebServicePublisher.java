package com.phcarvalho.model.communication.webservice;

import javax.xml.ws.Endpoint;

public class WebServicePublisher {

    public void publishQueueHandler(){
        QueueHandler queueHandler = new QueueHandler();

        Endpoint.publish("http://localhost:9990/chat-server/queue-handler", queueHandler);
    }
}
