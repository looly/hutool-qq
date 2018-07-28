package cn.hutool.qq.message;

import cn.hutool.json.JSONObject;

/**
 * 群消息.
 *
 * @author Looly
 */
public class GroupMessage extends FriendMessage {

	private long groupId;

	public GroupMessage(JSONObject json) {
		super(json, "send_uin");
		this.groupId = json.getLong("group_code");
	}

	/**
	 * 获取群ID
	 * 
	 * @return 群ID
	 */
	public long getGroupId() {
		return groupId;
	}
}
