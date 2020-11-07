import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class RPCServer {
    private static int fib(int n) {
        if (n <= 1)
            return n;
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(
                    Constants.RPC_QUEUE_NAME,
                    false,
                    false,
                    false,
                    null
            );
            channel.queuePurge(Constants.RPC_QUEUE_NAME);

            channel.basicQos(1);

            System.out.println("[x] Awaiting RPC requests");

            Object monitor = new Object();
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();
                StringBuilder response = new StringBuilder();
                try {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    int n = Integer.parseInt(message);
                    System.out.println("[.] fib(" + message + ")");
                    response.append(fib(n));
                } catch (RuntimeException e) {
                    System.out.println("[.] " + e.toString());
                } finally {
                    channel.basicPublish(
                            "",
                            delivery.getProperties().getReplyTo(),
                            replyProps,
                            response.toString().getBytes(StandardCharsets.UTF_8)
                    );
                    channel.basicAck(
                            delivery.getEnvelope().getDeliveryTag(),
                            false
                    );
// RabbitMq consumer worker thread notifies the RPC server owner thread
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };
            channel.basicConsume(
                    Constants.RPC_QUEUE_NAME,
                    false, deliverCallback,
                    consumerTag -> {

                    }
            );

            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
