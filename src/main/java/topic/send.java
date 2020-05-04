package topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import util.ConnectionUtils;

public class send {
/**
 * 将消息发布到交换机，并指定路由
 * 
 */
	
	private static final String EXCHANGE_NAME="test_exchange_topic";//交换机名称
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		//声明交换机
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");//分发
		
		String msg="商品。。。。"; //路由key
		String routingKey="goods.delete";
		channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
		//(队列字符串即使队列名称,是否持久，)
		System.out.println("Send :"+msg);
	 
 		channel.close();
		connection.close();

	}
}
