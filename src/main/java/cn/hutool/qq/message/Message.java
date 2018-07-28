package cn.hutool.qq.message;

import cn.hutool.qq.entity.base.Font;

/**
 * 消息接口
 *
 * @author Looly
 */
public interface Message {
	
	/**
	 * 获取消息发送用户
	 * @return 消息发送用户
	 */
	long getUserId();

	/**
	 * 获取消息发送时间
	 * @return 消息发送时间
	 */
	long getTime();
	
	/**
	 * 获取消息内容
	 * @return 消息内容
	 */
	String getContent();
	
	/**
	 * 获取字体
	 * @return 字体
	 */
	Font getFont();
}
