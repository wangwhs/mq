/**
 * 
 */
package cn.com.pep.mq.delayqueue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:TODO
 * @author:Administrator
 * @time:2023年1月2日-下午9:32:09
 */
@Component
public class DelayMessageSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void sendMsg(String msg, DelayTypeEnum type) {
		switch (type) {
		case DELAY_6S:
			rabbitTemplate.convertAndSend(RabbitMQConfig.BUSINESS_EXCHANGE,
					RabbitMQConfig.BUSINESS_DELAY_QUEUE_6_ROUTING_KEY, msg);
			break;
		case DELAY_60S:
			rabbitTemplate.convertAndSend(RabbitMQConfig.BUSINESS_EXCHANGE,
					RabbitMQConfig.BUSINESS_DELAY_QUEUE_60_ROUTING_KEY, msg);
			break;
		}
	}

}
