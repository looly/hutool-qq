package cn.hutool.qq.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.qq.QQClient;
import cn.hutool.qq.listener.SplitMessageListener;
import cn.hutool.qq.message.DiscussMessage;
import cn.hutool.qq.message.FriendMessage;
import cn.hutool.qq.message.GroupMessage;
import cn.hutool.qq.message.MessageType;

public class QQClientTest {
	public static void main(String[] args) {
		final QQClient client = new QQClient();
		client.loopPollMessage(new SplitMessageListener() {
			
			@Override
			public void onGroupMessage(GroupMessage message) {
				String content = message.getContent();
				Console.log("[{}][{}] {}", message.getUserId(), DateUtil.date(message.getTime()).toTimeStr(), message.getContent());
				long groupId = message.getGroupId();
				String answer = QADict.INSTANCE.getAnswerLike(content);
				if(null != answer) {
					client.sendMessage(MessageType.GROUP, groupId, answer);
				}
			}
			
			@Override
			public void onFriendMessage(FriendMessage message) {
				Console.log("[{}][{}] {}", message.getUserId(), DateUtil.date(message.getTime()).toTimeStr(), message.getContent());
				long id = message.getUserId();
				String answer = QADict.INSTANCE.getAnswerLike(message.getContent());
				if(null != answer) {
					client.sendMessage(MessageType.FRIEND, id, answer);
				}
			}
			
			@Override
			public void onDiscussMessage(DiscussMessage message) {
				Console.log("[{}][{}] {}", message.getUserId(), DateUtil.date(message.getTime()).toTimeStr(), message.getContent());
			}
		});
	}
}