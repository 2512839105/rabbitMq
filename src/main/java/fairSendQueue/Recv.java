package fairSendQueue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.naming.ldap.BasicControl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;

import util.ConnectionUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
 

public class Recv {
	
	private static final String QUEUE_NAME="test_fairSend_quee";

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, TimeoutException {
		
		
		Connection connection=ConnectionUtils.getConnection();
		final Channel channel=connection.createChannel();
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    
	    channel.basicQos(1);//保证一次一个
	    Consumer consumer = new DefaultConsumer(channel) {
	        @Override
	        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
	                byte[] body) throws IOException {
	            String message = new String(body, "UTF-8");
	            System.out.println(" [1] Received '" + message + "'");
	            try {
	                Thread.sleep(200);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }

	            // 手动应答
	            channel.basicAck(envelope.getDeliveryTag(), false);
	        }
	    };
	    boolean autoAck = false;//自动应答
	    channel.basicConsume(QUEUE_NAME, autoAck, consumer);
	    
	}
}