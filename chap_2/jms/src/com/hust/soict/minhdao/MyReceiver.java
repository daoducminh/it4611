package com.hust.soict.minhdao;

import javax.jms.*;
import javax.naming.InitialContext;

public class MyReceiver {
    public static void main(String[] args) {
//        Properties properties = new Properties();
//        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
//        properties.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
//        properties.put(Context.PROVIDER_URL, "http://localhost:4848/");
        try {
            InitialContext context = new InitialContext();
            TopicConnectionFactory factory = (TopicConnectionFactory) context.lookup("myTopicConnectionFactory");
            TopicConnection connection = factory.createTopicConnection();
            connection.start();

            TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = (Topic) context.lookup("myTopic");
            TopicSubscriber subscriber = session.createSubscriber(topic);
            MyListener listener = new MyListener();

            subscriber.setMessageListener(listener);
            System.out.println("Subscriber is ready, waiting for messages ...");

            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
