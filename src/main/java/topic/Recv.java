package topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;

import util.ConnectionUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
 

public class Recv {
	private static final String QUEUE_NAME="test_queue_topic";
	private static final String EXCHANGE_NAME="test_exchange_topic";//交换机名称
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection=ConnectionUtils.getConnection();
		final Channel channel=connection.createChannel();
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		//绑定队列到交换机 转发器
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "goods.add");
		channel.basicQos(1);//保证一次只分发一个
		//定义一个消费者
		Consumer consumer=new DefaultConsumer(channel){

			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg=new String(body,"utf-8");
				System.out.println("{1} recv msg:"+msg);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
					System.out.println("{1} done");
					channel.basicAck(envelope.getDeliveryTag(), false);// // 确认消息

				}				
			}			
		};
		boolean autoAck=false;
		channel.basicConsume(QUEUE_NAME, autoAck,consumer);
		//监听队列
	}
}
/**
 * 需要注意的 basicAck 方法需要传递两个参数
deliveryTag（唯一标识 ID）：当一个消费者向 RabbitMQ 注册后，会建立起一个 Channel ，RabbitMQ 会用 basic.deliver 方法向消费者推送消息，
这个方法携带了一个 delivery tag， 它代表了 RabbitMQ 向该 Channel 投递的这条消息的唯一标识 ID，是一个单调递增的正整数，delivery tag 的范围仅限于 Channel
multiple：为了减少网络流量，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
 */

