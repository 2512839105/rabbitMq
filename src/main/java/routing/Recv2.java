package routing;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;

import util.ConnectionUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
 

public class Recv2 {
	private static final String QUEUE_NAME="test_queue_direct2";
	private static final String EXCHANGE_NAME="test_exchange_direct";//交换机名称
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection=ConnectionUtils.getConnection();
		final Channel channel=connection.createChannel();
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		//绑定队列到交换机 转发器

		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "info");
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "warning");

		channel.basicQos(1);//保证一次只分发一个
		//定义一个消费者
		Consumer consumer=new DefaultConsumer(channel){

			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg=new String(body,"utf-8");
				System.out.println("{2} recv msg:"+msg);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
					System.out.println("{2} done");
					channel.basicAck(envelope.getDeliveryTag(), false);
				}				
			}			
		};
		boolean autoAck=false;
		channel.basicConsume(QUEUE_NAME, autoAck,consumer);
		//监听队列
	}
}
