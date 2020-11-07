import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeoutException;

public class RPCClient implements AutoCloseable {
    private final Connection connection;
    private final Channel channel;

    public RPCClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        this.connection = factory.newConnection();
        this.channel = this.connection.createChannel();
    }

    public static void main(String[] args) {
        try {
            RPCClient client = new RPCClient();
            for (int i = 0; i < 32; i++) {
                String index = Integer.toString(i);
                System.out.println("[x] Requesting fib(" + index + ")");
                String response = client.call(index);
                System.out.println("[.] Got '" + response + "'");
            }
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String call(String message) throws IOException, InterruptedException {
        final String correlationId = UUID.randomUUID().toString();
        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties properties = new AMQP.BasicProperties
                .Builder()
                .correlationId(correlationId)
                .replyTo(replyQueueName)
                .build();
        channel.basicPublish(
                "",
                Constants.RPC_QUEUE_NAME,
                properties,
                message.getBytes(StandardCharsets.UTF_8)
        );
        final BlockingDeque<String> response = (BlockingDeque<String>) new ArrayBlockingQueue<String>(1);
        String tag = channel.basicConsume(
                replyQueueName,
                true,
                (consumerTag, delivery) -> {
                    if (delivery.getProperties().getCorrelationId().equals(correlationId)) {
                        response.offer(new String(delivery.getBody(), "UTF-8"));
                    }
                }, consumerTag -> {

                }
        );
        String result = response.take();
        channel.basicCancel(tag);
        return result;
    }

    @Override
    public void close() throws Exception {
        this.connection.close();
    }
}
