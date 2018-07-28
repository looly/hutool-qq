package cn.hutool.qq.entity;

import cn.hutool.qq.entity.base.Status;

/**
 * 好友状态.
 *
 * @author Looly
 */
public class FriendStatus extends Status {

	private long uin;

	public long getUin() {
		return uin;
	}
	public void setUin(long uin) {
		this.uin = uin;
	}
}
