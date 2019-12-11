package com.phcarvalho.model.communication.webservice;

import com.phcarvalho.model.configuration.entity.User;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface IQueueHandler {

    @WebMethod
    void create(User user);
}
