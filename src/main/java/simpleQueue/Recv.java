package simpleQueue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;

import util.ConnectionUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
 

public class Recv {
	
	private static final String QUEUE_NAME="test_simple_quee";

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, TimeoutException {
		
		
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		DefaultConsumer consumer=new DefaultConsumer(channel){

			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg=new String(body,"utf-8");
				System.out.println("new api recv:"+msg);
				
			}
			
		};
		channel.basicConsume(QUEUE_NAME, true,consumer);
		
 

	}

 
}
