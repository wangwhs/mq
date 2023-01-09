/**
 * 
 */
package cn.com.pep.mq.delayqueue;

import java.io.IOException;
import java.util.Date;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

/**
 * @Description:TODO
 * @author:Administrator
 * @time:2023年1月2日-下午9:22:40
 */
@Component
public class DeadLetterConsumer {

	@RabbitListener(queues = RabbitMQConfig.DEAD_LETTER_QUEUE_A)
	public void consumeDeadLetterQueueA(Message message, Channel channel) throws IOException {
		System.err.println("当前时间为:【" + new Date().toString() + "】,收到的死信消息为：【" + new String(message.getBody()) + "】");
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}

	@RabbitListener(queues = RabbitMQConfig.DEAD_LETTER_QUEUE_B)
	public void consumeDeadLetterQueueB(Message message, Channel channel) throws IOException {
		System.err.println("当前时间为:【" + new Date().toString() + "】,收到的死信消息为：【" + new String(message.getBody()) + "】");
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}

}
