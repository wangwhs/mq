/**
 * 
 */
package cn.com.pep.mq.delayqueue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:��ʱ���е�����
 * @author:Administrator
 * @time:2023��1��2��-����7:54:43
 */
@Configuration
public class RabbitMQConfig {
	// ҵ�񽻻���
	public static final String BUSINESS_EXCHANGE = "delay.queue.business_exchange";
	// ��ʱ����A,�ӳ�6��
	public static final String BUSINESS_DELAY_QUEUE_6 = "delay.business.queue_6";
	// �ӳٶ���B���ӳ�60��
	public static final String BUSINESS_DELAY_QUEUE_60 = "delay.business.queue_60";
	// �ӳٶ���A��·��Key
	public static final String BUSINESS_DELAY_QUEUE_6_ROUTING_KEY = "delay.queue.routing_key_6";
	// �ӳٶ���B��·��key
	public static final String BUSINESS_DELAY_QUEUE_60_ROUTING_KEY = "delay.queue.routing_key_60";
	// ���Ž�����
	public static final String DEAD_LETTER_EXCHANGE = "dead.letter.exchange";
	// ���Ŷ���A��·��key
	public static final String DEAD_LETTER_ROUTING_KEY_A = "dead.letter.queue.routing_key_a";
	// ���Ŷ���B��·��key
	public static final String DEAD_LETTER_ROUTING_KEY_B = "dead.letter.queue.routing_key.b";
	// ���Ŷ���A������
	public static final String DEAD_LETTER_QUEUE_A = "dead.letter.queue_a";
	// ���Ŷ���B������
	public static final String DEAD_LETTER_QUEUE_B = "dead.letter.queue_b";

	/**
	 * @Description:�����ӳٶ��еĽ�����
	 * @return
	 * @author: Administrator
	 * @time:2023��1��2��-����8:04:28
	 */
	@Bean("delayExchange")
	public Exchange setDelayExchange() {
		return ExchangeBuilder.directExchange(BUSINESS_EXCHANGE).durable(true).delayed().build();
	}

	/**
	 * @Description:�������Ž�����
	 * @return
	 * @author: Administrator
	 * @time:2023��1��2��-����8:06:26
	 */
	@Bean("deadLetterExchange")
	public Exchange setDeadLetterExchange() {
		return ExchangeBuilder.directExchange(DEAD_LETTER_EXCHANGE).build();
	}

	/**
	 * @Description:�����ӳٶ���A���ӳ�6��
	 * @return
	 * @author: Administrator
	 * @time:2023��1��2��-����8:08:01
	 */
	@Bean("delayQueueA")
	public Queue setDelayQueueA() {
		Map<String, Object> args = new HashMap<String, Object>();
		/* ����ǰ�ӳٶ�������һ�����Ž����� */
		args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
		/* ����ǰ�ӳٶ��е�������Ϣ����һ��·��key */
		args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY_A);
		/* ���õ�ǰ�ӳٶ��е��ӳ�ʱ�� */
		args.put("x-message-ttl", 6000);
		return QueueBuilder.durable(BUSINESS_DELAY_QUEUE_6).withArguments(args).build();
	}

	/**
	 * @Description:�����ӳٶ����ӳ�60s
	 * @return
	 * @author: Administrator
	 * @time:2023��1��2��-����8:16:42
	 */
	@Bean("delayQueueB")
	public Queue setDelayQueueB() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
		args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY_B);
		args.put("x-message-ttl", 60000);
		return QueueBuilder.durable(BUSINESS_DELAY_QUEUE_60).withArguments(args).build();
	}

	/**
	 * @Description:�����ӳٶ���A�İ󶨹�ϵ
	 * @param queue
	 * @param exchange
	 * @return
	 * @author: Administrator
	 * @time:2023��1��2��-����8:19:25
	 */
	@Bean
	public Binding setDelayQueueABinding(@Qualifier("delayQueueA") Queue queue,
			@Qualifier("delayExchange") DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(BUSINESS_DELAY_QUEUE_6_ROUTING_KEY);
	}

	/**
	 * @Description:�����ӳٶ���B�İ󶨹�ϵ
	 * @param queue
	 * @param exchange
	 * @return
	 * @author: Administrator
	 * @time:2023��1��2��-����8:31:55
	 */
	@Bean
	public Binding setDelayQueueBBinding(@Qualifier("delayQueueB") Queue queue,
			@Qualifier("delayExchange") DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(BUSINESS_DELAY_QUEUE_60_ROUTING_KEY);
	}

	/**
	 * @Description:�������Ŷ���A
	 * @return
	 * @author: Administrator
	 * @time:2023��1��2��-����8:33:00
	 */
	@Bean("deadQueueA")
	public Queue setDeadQueueA() {
		return QueueBuilder.durable(DEAD_LETTER_QUEUE_A).build();
	}

	/**
	 * @Description:�������Ŷ���B
	 * @return
	 * @author: Administrator
	 * @time:2023��1��2��-����8:34:41
	 */
	@Bean("deadQueueB")
	public Queue setDeadQueueB() {
		return QueueBuilder.durable(DEAD_LETTER_QUEUE_B).build();
	}

	/**
	 * @Description:�������Ŷ���A�İ󶨹�ϵ
	 * @return
	 * @author: Administrator
	 * @time:2023��1��2��-����8:36:32
	 */
	@Bean
	public Binding setDeadLetterQueueABinding(@Qualifier("deadLetterExchange") DirectExchange exchange,
			@Qualifier("deadQueueA") Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_ROUTING_KEY_A);
	}
	
	/**
	 * @Description:�������Ŷ���B�İ󶨹�ϵ
	 * @param exchange
	 * @param queue
	 * @return
	 * @author: Administrator
	 * @time:2023��1��2��-����8:41:14
	 */
	@Bean
	public Binding setDeadLetterQueueBBinding(@Qualifier("deadLetterExchange") DirectExchange exchange,
			@Qualifier("deadQueueB") Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_ROUTING_KEY_B);
	}

}
