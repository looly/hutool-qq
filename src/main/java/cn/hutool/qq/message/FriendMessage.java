package cn.hutool.qq.message;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.qq.entity.base.Font;

/**
 * 消息.
 *
 * @author Looly
 */
public class FriendMessage implements Message{

	private long time;
	private String content;
	private long userId;
	private Font font;
	
	/**
	 * 构造
	 * 
	 * @param json 返回的JSON数据
	 */
	public FriendMessage(JSONObject json) {
		this(json, "from_uin");
	}

	/**
	 * 构造
	 * 
	 * @param json 返回的JSON数据
	 * @param userIdFieldName 用户ID对应的字段名，用于适配不同类型消息
	 */
	protected FriendMessage(JSONObject json, String userIdFieldName) {
		JSONArray cont = json.getJSONArray("content");
		this.font = cont.getJSONArray(0).getBean(1, Font.class);

		final int size = cont.size();
		final StringBuilder contentBuilder = new StringBuilder();
		for (int i = 1; i < size; i++) {
			contentBuilder.append(cont.getStr(i));
		}
		this.content = contentBuilder.toString();

		this.time = json.getLong("time");
		this.userId = json.getLong(userIdFieldName);
	}

	@Override
	public long getTime() {
		return time;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public long getUserId() {
		return userId;
	}

	@Override
	public Font getFont() {
		return font;
	}

}
