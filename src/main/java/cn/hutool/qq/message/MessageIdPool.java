package cn.hutool.qq.message;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 消息ID池，用于生成消息ID，单例存在
 * @author looly
 *
 */
public enum MessageIdPool {
	INSTANCE;
	
	private AtomicLong id = new AtomicLong(43690001);
	
	/**
	 * 获取下一个消息ID
	 * @return 消息ID
	 */
	public long getNext() {
		return id.incrementAndGet();
	}
}
