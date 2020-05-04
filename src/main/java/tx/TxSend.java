package tx;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import util.ConnectionUtils;
/**
 * 出现异常情况消息发送不成功
 * @author admin
 *
 */
public class TxSend {
	private static final String QUEUE_NAME = "test_queue_tx";

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		try {
			String msg = "hello tx message!";
			channel.txSelect();
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			int xx=1/0; //设置异常
			System.out.println("send "+msg);
			channel.txCommit();// 提交事务
			System.out.println("Send :" + msg);
		} catch (Exception e) {
			channel.txRollback();
			System.out.println("send message txRollbak");
		}
		channel.close();
		connection.close();

	}
}
