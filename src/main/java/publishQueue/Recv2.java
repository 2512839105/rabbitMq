package publishQueue;

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
	
	private static final String QUEUE_NAME="test_queue_fanout_sms";
	private static final String EXCHANGE_NAME="test_exchange_fanout";

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, TimeoutException {
		
		
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		
		//绑定队列到交换机 转发器
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					System.out.println("{2} done");
				}
				
			}
			
		};
		boolean autoAck=false;
		channel.basicConsume(QUEUE_NAME, autoAck,consumer);
		//监听队列
 

	}

 
}
