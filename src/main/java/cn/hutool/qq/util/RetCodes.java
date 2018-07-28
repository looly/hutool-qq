package cn.hutool.qq.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * QQ接口状态码消息映射
 * @author looly
 *
 */
public class RetCodes {
	private static Map<Integer, String> retCodeMap = new ConcurrentHashMap<>();
	
	static {
		retCodeMap.put(103, "请求失败，错误代码[103]。你需要进入http://w.qq.com，检查是否能正常接收消息。如果可以的话点击[设置]->[退出登录]后查看是否恢复正常");
	}
	
	/**
	 * 获取状态码对应消息，无对应关系返回<code>null</code>
	 * @param code 状态码
	 * @return 消息
	 */
	public static String getMsg(int code) {
		return retCodeMap.get(code);
	}
}
