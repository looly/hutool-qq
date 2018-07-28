package cn.hutool.qq.message;

import cn.hutool.json.JSONObject;
import cn.hutool.log.StaticLog;

/**
 * 消息工厂
 * 
 * @author looly
 *
 */
public class MessageFactory {
	
	/**
	 * 创建消息
	 * @param type 消息类型
	 * @param messageJSON 消息内容JSON
	 * @return {@link Message}
	 * @see FriendMessage
	 * @see GroupMessage
	 * @see DiscussMessage
	 */
	public static Message createMessage(String type, JSONObject messageJSON) {
		switch (type) {
		case "message":
			return new FriendMessage(messageJSON);
		case "group_message":
			return new GroupMessage(messageJSON);
		case "discu_message":
			return new DiscussMessage(messageJSON);
		default:
			StaticLog.error("无法识别的消息类型：{}", type);
			return new FriendMessage(messageJSON);
		}
	}
}
