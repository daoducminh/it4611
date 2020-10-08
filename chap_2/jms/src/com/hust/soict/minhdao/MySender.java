package com.hust.soict.minhdao;

import javax.jms.*;
import javax.naming.InitialContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MySender {
    public static void main(String[] args) {
//        Properties properties = new Properties();
//        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
//        properties.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
//        properties.put(Context.PROVIDER_URL, "http://localhost:4848/");
        try {
            InitialContext context = new InitialContext();
            TopicConnectionFactory factory = (TopicConnectionFactory) context.lookup("myTopicConnectionFactory");
            TopicConnection connection = factory.createTopicConnection();
            connection.start();

            TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = (Topic) context.lookup("myTopic");
            TopicPublisher publisher = session.createPublisher(topic);

            TextMessage message = session.createTextMessage();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.println("Enter message, end to terminate: ");
                String s = reader.readLine();

                if (s.equals("end")) {
                    break;
                }
                message.setText(s);
                publisher.publish(message);
                System.out.println("Message successfully sent.");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
