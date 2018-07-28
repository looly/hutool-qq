## 简介

基于Hutool和WebQQ-API实现的QQ API封装，功能包括：

- 登录QQ（二维码登录）
- 接收消息（好友消息、群消息、讨论组消息）
- 发送消息（好友消息、群消息、讨论组消息）
- 获取当前登录用户信息
- 获取所有好友信息，包括分组信息
- 获取群信息（包括群成员列表）
- 获取讨论组信息（包括讨论组成员信息）
- 根据好友ID获取好友的QQ号码

-------------------------------------------------------------------------------

## 运行

```java
final QQClient client = new QQClient();
client.loopPollMessage(new SplitMessageListener() {
	
	@Override
	public void onGroupMessage(GroupMessage message) {
		//接收群消息
		String content = message.getContent();
		Console.log("[{}][{}] {}", message.getUserId(), DateUtil.date(message.getTime()).toTimeStr(), message.getContent());
	}
	
	@Override
	public void onFriendMessage(FriendMessage message) {
		//接收好友消息
		Console.log("[{}][{}] {}", message.getUserId(), DateUtil.date(message.getTime()).toTimeStr(), message.getContent());
		
	}
	
	@Override
	public void onDiscussMessage(DiscussMessage message) {
		//接收讨论组消息
		Console.log("[{}][{}] {}", message.getUserId(), DateUtil.date(message.getTime()).toTimeStr(), message.getContent());
	}
});
```

运行后提示：

二维码保存于：[XXX\hutool-qq\target\test-classes\qrcode.png]，请打开手机QQ扫描登录。

打开地址对应的图片，使用手机QQ扫描即可。

## 感谢

WebQQ API全部来自[Smart QQ Java](https://github.com/ScienJus/smartqq)项目，部分逻辑思想有参考。