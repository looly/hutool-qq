package cn.hutool.qq.listener;

import cn.hutool.qq.message.Message;

/**
 * 消息监听
 * 
 * @author looly
 *
 */
public interface MessageListener {
	
	/**
	 * 消息接收事件，当接收到消息时触发此方法
	 * @param message 消息对象，Message接口有不同实现，可以根据实现判断消息类型
	 */
	public void onRecived(Message message);
}
