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
 * @Description:延时队列的配置
 * @author:Administrator
 * @time:2023年1月2日-下午7:54:43
 */
@Configuration
public class RabbitMQConfig {
	// 业务交换机
	public static final String BUSINESS_EXCHANGE = "delay.queue.business_exchange";
	// 延时队列A,延迟6秒
	public static final String BUSINESS_DELAY_QUEUE_6 = "delay.business.queue_6";
	// 延迟队列B，延迟60秒
	public static final String BUSINESS_DELAY_QUEUE_60 = "delay.business.queue_60";
	// 延迟队列A的路由Key
	public static final String BUSINESS_DELAY_QUEUE_6_ROUTING_KEY = "delay.queue.routing_key_6";
	// 延迟队列B的路由key
	public static final String BUSINESS_DELAY_QUEUE_60_ROUTING_KEY = "delay.queue.routing_key_60";
	// 死信交换机
	public static final String DEAD_LETTER_EXCHANGE = "dead.letter.exchange";
	// 死信队列A的路由key
	public static final String DEAD_LETTER_ROUTING_KEY_A = "dead.letter.queue.routing_key_a";
	// 死信队列B的路由key
	public static final String DEAD_LETTER_ROUTING_KEY_B = "dead.letter.queue.routing_key.b";
	// 死信队列A的名称
	public static final String DEAD_LETTER_QUEUE_A = "dead.letter.queue_a";
	// 死信队列B的名称
	public static final String DEAD_LETTER_QUEUE_B = "dead.letter.queue_b";

	/**
	 * @Description:创建延迟队列的交换机
	 * @return
	 * @author: Administrator
	 * @time:2023年1月2日-下午8:04:28
	 */
	@Bean("delayExchange")
	public Exchange setDelayExchange() {
		return ExchangeBuilder.directExchange(BUSINESS_EXCHANGE).durable(true).delayed().build();
	}

	/**
	 * @Description:创建死信交换机
	 * @return
	 * @author: Administrator
	 * @time:2023年1月2日-下午8:06:26
	 */
	@Bean("deadLetterExchange")
	public Exchange setDeadLetterExchange() {
		return ExchangeBuilder.directExchange(DEAD_LETTER_EXCHANGE).build();
	}

	/**
	 * @Description:创建延迟队列A，延迟6秒
	 * @return
	 * @author: Administrator
	 * @time:2023年1月2日-下午8:08:01
	 */
	@Bean("delayQueueA")
	public Queue setDelayQueueA() {
		Map<String, Object> args = new HashMap<String, Object>();
		/* 给当前延迟队列声明一个死信交换器 */
		args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
		/* 给当前延迟队列的死信消息声明一个路由key */
		args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY_A);
		/* 设置当前延迟队列的延迟时间 */
		args.put("x-message-ttl", 6000);
		return QueueBuilder.durable(BUSINESS_DELAY_QUEUE_6).withArguments(args).build();
	}

	/**
	 * @Description:创建延迟队列延迟60s
	 * @return
	 * @author: Administrator
	 * @time:2023年1月2日-下午8:16:42
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
	 * @Description:声明延迟队列A的绑定关系
	 * @param queue
	 * @param exchange
	 * @return
	 * @author: Administrator
	 * @time:2023年1月2日-下午8:19:25
	 */
	@Bean
	public Binding setDelayQueueABinding(@Qualifier("delayQueueA") Queue queue,
			@Qualifier("delayExchange") DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(BUSINESS_DELAY_QUEUE_6_ROUTING_KEY);
	}

	/**
	 * @Description:声明延迟队列B的绑定关系
	 * @param queue
	 * @param exchange
	 * @return
	 * @author: Administrator
	 * @time:2023年1月2日-下午8:31:55
	 */
	@Bean
	public Binding setDelayQueueBBinding(@Qualifier("delayQueueB") Queue queue,
			@Qualifier("delayExchange") DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(BUSINESS_DELAY_QUEUE_60_ROUTING_KEY);
	}

	/**
	 * @Description:声明死信队列A
	 * @return
	 * @author: Administrator
	 * @time:2023年1月2日-下午8:33:00
	 */
	@Bean("deadQueueA")
	public Queue setDeadQueueA() {
		return QueueBuilder.durable(DEAD_LETTER_QUEUE_A).build();
	}

	/**
	 * @Description:声明死信队列B
	 * @return
	 * @author: Administrator
	 * @time:2023年1月2日-下午8:34:41
	 */
	@Bean("deadQueueB")
	public Queue setDeadQueueB() {
		return QueueBuilder.durable(DEAD_LETTER_QUEUE_B).build();
	}

	/**
	 * @Description:声明死信队列A的绑定关系
	 * @return
	 * @author: Administrator
	 * @time:2023年1月2日-下午8:36:32
	 */
	@Bean
	public Binding setDeadLetterQueueABinding(@Qualifier("deadLetterExchange") DirectExchange exchange,
			@Qualifier("deadQueueA") Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_ROUTING_KEY_A);
	}
	
	/**
	 * @Description:声明死信队列B的绑定关系
	 * @param exchange
	 * @param queue
	 * @return
	 * @author: Administrator
	 * @time:2023年1月2日-下午8:41:14
	 */
	@Bean
	public Binding setDeadLetterQueueBBinding(@Qualifier("deadLetterExchange") DirectExchange exchange,
			@Qualifier("deadQueueB") Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_ROUTING_KEY_B);
	}

}
