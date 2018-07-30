package cn.hutool.qq.test;

import java.util.Collection;

import cn.hutool.core.lang.Console;
import cn.hutool.qq.QQClient;
import cn.hutool.qq.entity.Category;
import cn.hutool.qq.entity.Friend;

public class QQClientTest2 {
	public static void main(String[] args) {
		QQClient client = new QQClient();
		Collection<Category> friendsByCategory = client.getFriendsByCategory();
		for (Category category : friendsByCategory) {
			Console.log(category);
			for (Friend friend : category.getFriends()) {
				Console.log("-- {}", friend.getNickname());
			}
		}
	}
}