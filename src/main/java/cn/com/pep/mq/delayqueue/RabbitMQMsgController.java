/**
 * 
 */
package cn.com.pep.mq.delayqueue;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:TODO
 * @author:Administrator
 * @time:2023年1月2日-下午9:40:40
 */
@RequestMapping("rabbitmq")
@RestController
public class RabbitMQMsgController {

	@Autowired
	private DelayMessageSender sender;
	
	@RequestMapping("/sendmsg")
	public void sendMsg(String msg, int delayType) {
		System.err.println("当前时间为：【" + new Date().toString() + "】,收到的消息为:【" + msg + "】,delayType为：【" + delayType + "】");
		DelayTypeEnum[] values = DelayTypeEnum.values();
		sender.sendMsg(msg, values[delayType]);
	}
}
