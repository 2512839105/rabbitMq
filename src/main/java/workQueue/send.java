package workQueue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import util.ConnectionUtils;

public class send {
	private static final String QUEUE_NAME="test_work_queue";

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		//(队列字符串即使队列名称,是否持久，)
	 
		for(int i=0;i<50;i++){
			String msg="hello work"+i;
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());	
			Thread.sleep(i*20);
			System.out.println(msg);
		}
 		channel.close();
		connection.close();

	}
}
