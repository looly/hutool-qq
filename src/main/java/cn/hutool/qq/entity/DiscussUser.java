package cn.hutool.qq.entity;

import cn.hutool.qq.entity.base.Status;

/**
 * 讨论组成员.
 *
 * @author Looly
 */
public class DiscussUser extends Status{

	private long uin;
	private String nick;

	public long getUin() {
		return uin;
	}

	public void setUin(long uin) {
		this.uin = uin;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	@Override
	public String toString() {
		return "DiscussUser{" + "uin=" + uin + ", nick='" + nick + '\'' + ", clientType='" + getClientType() + '\'' + ", status='" + getStatus() + '\'' + '}';
	}
}
