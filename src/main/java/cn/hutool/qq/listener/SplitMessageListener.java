package cn.hutool.qq.listener;

import cn.hutool.log.StaticLog;
import cn.hutool.qq.message.DiscussMessage;
import cn.hutool.qq.message.FriendMessage;
import cn.hutool.qq.message.GroupMessage;
import cn.hutool.qq.message.Message;

/**
 * 针对好友消息、群消息、讨论组消息分别监听处理
 * 
 * @author looly
 *
 */
public abstract class SplitMessageListener implements MessageListener {

	@Override
	public void onRecived(Message message) {
		if (message instanceof GroupMessage) {
			onGroupMessage((GroupMessage) message);
		} else if (message instanceof DiscussMessage) {
			onDiscussMessage((DiscussMessage) message);
		} else if (message instanceof FriendMessage) {
			onFriendMessage((FriendMessage) message);
		}else {
			StaticLog.warn("无法识别的消息类型：[{}]", message.getClass().getName());
		}
	}

	/**
	 * 处理好友消息
	 * @param message 好友消息
	 */
	public abstract void onFriendMessage(FriendMessage message);

	/**
	 * 处理群消息
	 * @param message 群消息
	 */
	public abstract void onGroupMessage(GroupMessage message);

	/**
	 * 处理讨论组消息
	 * @param message 讨论组消息
	 */
	public abstract void onDiscussMessage(DiscussMessage message);

}
