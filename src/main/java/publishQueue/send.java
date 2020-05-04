package publishQueue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import util.ConnectionUtils;

public class send {
/**
 * publish 订阅模式
 * 与简单模式的区别，不直接发送消息给队列，而是先发送到交换机
 * 消费者--声明队列，再将队绑定到交换机
 * 
 * 
 */
	private static final String EXCHANGE_NAME="test_exchange_fanout";//交换机名称
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		//声明交换机
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");//分发
		
		String msg="hello publish";
		channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
		//(队列字符串即使队列名称,是否持久，)
		System.out.println("Send :"+msg);
	 
 		channel.close();
		connection.close();

	}
}
