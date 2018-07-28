package cn.hutool.qq.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 讨论组资料，包括成员信息等
 *
 * @author Looly
 */
public class DiscussInfo extends Discuss{

	private List<DiscussUser> users = new ArrayList<>();

	/**
	 * 增加用户
	 * @param user
	 */
	public void addUser(DiscussUser user) {
		this.users.add(user);
	}

	public List<DiscussUser> getUsers() {
		return users;
	}

	public void setUsers(List<DiscussUser> users) {
		this.users = users;
	}

}
