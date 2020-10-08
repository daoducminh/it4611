package com.hust.soict.minhdao;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("Following message is received: " + textMessage.getText());
        }catch (JMSException e){
            e.printStackTrace();
        }
    }
}
