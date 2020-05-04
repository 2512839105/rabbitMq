package confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import util.ConnectionUtils;

/**
 * 
 * @author 
 * 单条发送
 *
 */
public class Send1 {
	private static final String QUEUE_NAME = "test_queue_confirm1";

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//生产者调用confirmSelect 将channel设置为confirm 模式 注意
		channel.confirmSelect();
		String mString="hello confirm message!";
		for(int i=0;i<10;i++){
			channel.basicPublish("", QUEUE_NAME, null, mString.getBytes());

		}
		
		if(!channel.waitForConfirms()){
			System.out.println("message send failed");
		}else{
			System.out.println("message send succ");
		}
		channel.close();
		connection.close();
	}
}
