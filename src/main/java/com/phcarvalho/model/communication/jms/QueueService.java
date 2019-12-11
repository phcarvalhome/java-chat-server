package com.phcarvalho.model.communication.jms;

import com.phcarvalho.model.communication.protocol.vo.command.SendMessageCommand;
import com.phcarvalho.model.configuration.Configuration;
import com.phcarvalho.model.configuration.entity.User;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;
import java.util.concurrent.Executors;

public class QueueService {

    private Context context;
    private QueueConnectionFactory queueConnectionFactory;
    private QueueConnection queueConnection;
//    private QueueSession queueSession;

    public void createConnection(){
        Hashtable properties = new Hashtable();

        properties.put(Context.INITIAL_CONTEXT_FACTORY,"org.exolab.jms.jndi.InitialContextFactory");
        properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");

        try {
            context = new InitialContext(properties);
            queueConnectionFactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");

            try {
                queueConnection = queueConnectionFactory.createQueueConnection();
//                queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void createQueue(User user){
        String queueName = user.getName();

        try {
            QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = queueSession.createQueue(queueName);

            try {
                context.bind(queueName, queue);
            } catch (NamingException e) {
                e.printStackTrace();
            }

            queueSession.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        createReceive(user);
    }

    public void createReceive(User user){
        Queue queue = getQueue(user);

        Executors.newSingleThreadExecutor().execute(() -> {

            try {
                //FIXME Here is the correct place for it?
                queueConnection.start();

                QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                QueueReceiver queueReceiver = queueSession.createReceiver(queue);

                while(true){
                    ObjectMessage objectMessage = (ObjectMessage) queueReceiver.receive();
                    SendMessageCommand sendMessageCommand = (SendMessageCommand) objectMessage.getObject();

                    Configuration.getSingleton().addOfflineMessage(sendMessageCommand);
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    public Queue getQueue(User user) {
        String queueName = user.getName();

        try {
            return (Queue) context.lookup(queueName);
        } catch (NamingException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("The queue was not found! User: " + user);
    }

    public void sendMessage(SendMessageCommand sendMessageCommand){
        User targetUser = sendMessageCommand.getTargetUser();
        Queue queue = getQueue(targetUser);

        try {
            QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            ObjectMessage objectMessage = queueSession.createObjectMessage();
            QueueSender queueSender = queueSession.createSender(queue);

            objectMessage.setObject(sendMessageCommand);
            queueSender.send(objectMessage);
            queueSession.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
