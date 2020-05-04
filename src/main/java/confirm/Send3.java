package confirm;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

import javax.sound.midi.Sequence;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import util.ConnectionUtils;

/**
 * 
 * @author 
 * 异步
 *
 */
public class Send3 {
	private static final String QUEUE_NAME = "test_queue_confirm1";

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//生产者调用confirmSelect 将channel设置为confirm 模式 注意
		channel.confirmSelect();
		//未确认的消息标识
		final SortedSet<Long> confirmSet=Collections.synchronizedSortedSet(new TreeSet<Long>());
		//添加监听
		channel.addConfirmListener(new ConfirmListener() {
			//回执没有问题的
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				if(multiple){
					System.out.println("-------handleNack-----multiple");
					confirmSet.headSet(deliveryTag+1).clear();
				}else{
					System.out.println("-------handleNack--------multiple false");
					confirmSet.remove(deliveryTag);
				}
				
			}
			
			//回执有问题的
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				if(multiple){
					System.out.println("----handleAck----multiple");
					confirmSet.headSet(deliveryTag).clear();
				}else{
					System.out.println("----handleAck-----multiple false");
					confirmSet.remove(deliveryTag);
				}
				
			}
		});
 		channel.confirmSelect();
		String mString="sssss";
		while(true){
			long segNo=channel.getNextPublishSeqNo();
			channel.basicPublish("", QUEUE_NAME, null, mString.getBytes());
			confirmSet.add(segNo);
		}
		
 
 
	}
}
