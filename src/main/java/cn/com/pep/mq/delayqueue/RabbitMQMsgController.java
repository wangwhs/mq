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
 * @time:2023��1��2��-����9:40:40
 */
@RequestMapping("rabbitmq")
@RestController
public class RabbitMQMsgController {

	@Autowired
	private DelayMessageSender sender;
	
	@RequestMapping("/sendmsg")
	public void sendMsg(String msg, int delayType) {
		System.err.println("��ǰʱ��Ϊ����" + new Date().toString() + "��,�յ�����ϢΪ:��" + msg + "��,delayTypeΪ����" + delayType + "��");
		DelayTypeEnum[] values = DelayTypeEnum.values();
		sender.sendMsg(msg, values[delayType]);
	}
}
