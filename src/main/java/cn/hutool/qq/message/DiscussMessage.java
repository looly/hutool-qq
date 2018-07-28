package cn.hutool.qq.message;

import cn.hutool.json.JSONObject;

/**
 * 讨论组消息.
 *
 * @author Looly
 */
public class DiscussMessage extends FriendMessage{

	private long discussId;

	public DiscussMessage(JSONObject json) {
		super(json, "send_uin");
		this.discussId = json.getLong("did");
	}

	/**
	 * 获取讨论组ID
	 * @return 讨论组ID
	 */
	public long getDiscussId() {
		return discussId;
	}
}
