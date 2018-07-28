package cn.hutool.qq;

import cn.hutool.core.util.StrUtil;

/**
 * QQ异常
 * 
 * @author looly
 *
 */
public class QQException extends RuntimeException {
	private static final long serialVersionUID = 6057602589533840889L;

	// 异常状态码
	private int status;

	public QQException() {
	}

	public QQException(String msg) {
		super(msg);
	}

	public QQException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public QQException(Throwable throwable) {
		super(throwable);
	}

	public QQException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public QQException(int status, String msg) {
		super(msg);
		this.status = status;
	}

	public QQException(int status, Throwable throwable) {
		super(throwable);
		this.status = status;
	}

	public QQException(int status, String msg, Throwable throwable) {
		super(msg, throwable);
		this.status = status;
	}

	/**
	 * @return 获得异常状态码
	 */
	public int getStatus() {
		return status;
	}
}
