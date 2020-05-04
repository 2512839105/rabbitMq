package spring;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StringMain {

	/*@Autowired
	RabbitTemplate template;*/
	public static void main(String[] args) throws InterruptedException {
		AbstractApplicationContext context=new  ClassPathXmlApplicationContext("classpath:context.xml");
		//RabbitMq 模板
		RabbitTemplate template=context.getBean(RabbitTemplate.class);
		//发送消息
		template.convertAndSend("hello world");
		Thread.sleep(1000);
		context.destroy();//容器销毁
	}
}
