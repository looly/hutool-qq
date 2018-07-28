package cn.hutool.qq.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.qq.QQClient;
import cn.hutool.qq.listener.MessageListener;
import cn.hutool.qq.message.Message;

public class QQClientTest {
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		QQClient client  = new QQClient();
		client.loopPollMessage(new MessageListener() {
			
			@Override
			public void onRecived(Message message) {
				Console.log("[{}][{}] {}", message.getUserId(), DateUtil.date(message.getTime()).toTimeStr(), message.getContent());
			}
		});
	}
}