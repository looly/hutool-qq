package cn.hutool.qq;

/**
 * QQ配置
 * 
 * @author looly
 *
 */
public class QQConfig {

	/** 默认客户端id */
	public static final long CLIENT_ID_DEFAULT = 53999199;

	/** 客户端ID，固定值 */
	private long clientId = CLIENT_ID_DEFAULT;
	/** 请求接口失败后重试的次数，默认3次 */
	private int retryCount = 3;
	
	public long getClientId() {
		return clientId;
	}
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
}
