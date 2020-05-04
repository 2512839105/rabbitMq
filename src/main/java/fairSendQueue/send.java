package fairSendQueue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.omg.IOP.Encoding;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import util.ConnectionUtils;

public class send {
	private static final String QUEUE_NAME="test_fairSend_quee";//公平发送
	/**
	 * 现象 消费者1 比2 处理的多 原因  消费者1 sleep时间比2 短，能者多劳
	 * @param args
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel=connection.createChannel();
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		/**
		 * 每个消费者发送确认消息之前，消息队列不发送下一个消息到消费者，一次只处理一个请求
		 * 限制发送给同一个消费者不得超过一条消息
		 */
		 int prefecthCount=1;
		channel.basicQos(prefecthCount);
          for (int i = 0; i < 50; i++)
          {
              String msg = "hello world " + i;
              //发送消息
              channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
              System.out.println("fairSend: "+msg);
          }
          channel.close();
          connection.close();

	}
}
