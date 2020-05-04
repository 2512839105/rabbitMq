package PersistenceQueue;

import com.rabbitmq.client.Connection;

import util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

public class Send {
	private static final String QUEUE_NAME="test_persistence_quee";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		Connection connection=ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		channel.queueDeclare(QUEUE_NAME,true,false,false,null);
		//(队列字符串即使队列名称,是否持久，)
		String msg="hello simple!";
		for(int i=0;i<2;i++){
			msg=msg+"--"+i;
		    channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		}
		System.out.println("-------------send msg: "+msg);
		channel.close();
		connection.close();
		
	}
}
