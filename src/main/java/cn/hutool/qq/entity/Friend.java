package cn.hutool.qq.entity;

/**
 * 好友.
 *
 * @author Looly
 */
public class Friend {

	private long userId;
	private String markname = "";
	private String nickname;
	private boolean vip;
	private int vipLevel;
	
	/**
	 * 默认构造
	 */
	public Friend() {
	}
	
	/**
	 * 构造
	 * @param userId 用户ID
	 * @param nickname 昵称
	 */
	public Friend(long userId, String nickname) {
		this.userId = userId;
		this.nickname = nickname;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getMarkname() {
		return markname;
	}

	public void setMarkname(String markname) {
		this.markname = markname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	@Override
	public String toString() {
		return "Friend{" + "userId=" + userId + ", markname='" + markname + '\'' + ", nickname='" + nickname + '\'' + ", vip=" + vip + ", vipLevel=" + vipLevel + '}';
	}
}
