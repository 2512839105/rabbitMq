package util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtils {

	/**
	 * ��ȡMq������
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	
	public static Connection getConnection() throws IOException, TimeoutException{
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost("127.0.0.1");
		factory.setPort(5672);
		factory.setUsername("user");
		//vhost
		factory.setVirtualHost("/virtual_mmr");
		factory.setPassword("123");
		return factory.newConnection();
	}
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionUtils. getConnection();
	}
	
}
