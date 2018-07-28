package cn.hutool.qq;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.qq.entity.Category;
import cn.hutool.qq.entity.Discuss;
import cn.hutool.qq.entity.DiscussInfo;
import cn.hutool.qq.entity.DiscussUser;
import cn.hutool.qq.entity.Friend;
import cn.hutool.qq.entity.FriendStatus;
import cn.hutool.qq.entity.Group;
import cn.hutool.qq.entity.GroupInfo;
import cn.hutool.qq.entity.GroupUser;
import cn.hutool.qq.entity.UserInfo;
import cn.hutool.qq.entity.base.Font;
import cn.hutool.qq.listener.MessageListener;
import cn.hutool.qq.message.MessageFactory;
import cn.hutool.qq.message.MessageIdPool;
import cn.hutool.qq.message.MessageType;
import cn.hutool.qq.util.QQHttpUtil;

/**
 * QQ客户端
 * 
 * @author looly
 *
 */
public class QQClient {
	private static Log log = LogFactory.get();

	private QQConfig config;
	private QQSession session;

	/**
	 * 构造，使用默认配置项
	 */
	public QQClient() {
		this(new QQConfig());
	}

	/**
	 * 构造
	 * 
	 * @param config QQ客户端配置项，如果为<code>null</code>使用默认配置
	 */
	public QQClient(QQConfig config) {
		if (null == config) {
			config = new QQConfig();
		}
		this.config = config;
		this.session = new QQSession(config);
	}

	/**
	 * 获得当前登录用户的详细信息
	 *
	 * @return 用户详细地址
	 */
	public UserInfo getAccountInfo() {
		log.debug("开始获取登录用户信息");

		HttpResponse response = QQHttpUtil.retryGet(config.getRetryCount(), ApiEnum.GET_ACCOUNT_INFO);
		JSONObject result = QQHttpUtil.getObjResult(response);
		return result.toBean(UserInfo.class);
	}

	/**
	 * 获取群列表
	 *
	 * @return 群列表
	 */
	public List<Group> getGroupList() {
		log.debug("开始获取群列表……");

		JSONObject r = new JSONObject();
		r.put("vfwebqq", session.vfwebqq);
		r.put("hash", session.getHashedUid());

		HttpResponse response = QQHttpUtil.retryPost(config.getRetryCount(), r, ApiEnum.GET_GROUP_LIST);
		JSONObject result = QQHttpUtil.getObjResult(response);
		return result.getJSONArray("gnamelist").toList(Group.class);
	}

	/**
	 * 获得讨论组列表
	 *
	 * @return 讨论组列表
	 */
	public List<Discuss> getDiscussList() {
		log.debug("开始获取讨论组列表……");

		HttpResponse response = QQHttpUtil.get(ApiEnum.GET_DISCUSS_LIST, session.psessionid, session.vfwebqq);
		JSONObject result = QQHttpUtil.getObjResult(response);
		return result.getJSONArray("dnamelist").toList(Discuss.class);
	}

	/**
	 * 获取好友列表
	 *
	 * @return 好友列表
	 */
	public Collection<Friend> getFriends() {
		return parseFriendMap(getFriendsJson()).values();
	}

	/**
	 * 获取分组后的好友列表
	 * 
	 * @return 分组列表，分组中包含好友列表
	 */
	public Collection<Category> getFriendsByCategory() {
		JSONObject result = getFriendsJson();

		// 获得好友信息
		Map<Long, Friend> friendMap = parseFriendMap(result);

		// 获得分组
		JSONArray categories = result.getJSONArray("categories");
		Map<Integer, Category> categoryMap = new LinkedHashMap<>();
		categoryMap.put(0, Category.defaultCategory());
		for (JSONObject item : categories.jsonIter()) {
			Category category = item.toBean(Category.class);
			categoryMap.put(category.getIndex(), category);
		}

		// 好友和分组关系
		JSONArray friends = result.getJSONArray("friends");
		for (JSONObject item : friends.jsonIter()) {
			Friend friend = friendMap.get(item.getLong("uin"));
			categoryMap.get(item.getInt("categories")).addFriend(friend);
		}
		return categoryMap.values();
	}

	/**
	 * 获得好友的详细信息
	 *
	 * @param 好友ID
	 * @return 好友信息（用户信息）
	 */
	public UserInfo getFriendInfo(long friendId) {
		log.debug("开始获取好友信息");

		HttpResponse response = QQHttpUtil.get(ApiEnum.GET_FRIEND_INFO, friendId, session.vfwebqq, session.psessionid);
		JSONObject result = QQHttpUtil.getObjResult(response);
		return result.toBean(UserInfo.class);
	}

	/**
	 * 获得qq号
	 *
	 * @param friendId 用户id
	 * @return
	 */
	public long getQQById(long friendId) {
		log.debug("开始获取QQ号……");

		HttpResponse response = QQHttpUtil.get(ApiEnum.GET_QQ_BY_ID, friendId, session.vfwebqq);
		JSONObject result = QQHttpUtil.getObjResult(response);
		return result.getLong("account");
	}

	/**
	 * 获得所有好友的在线状态及客户端类型信息
	 *
	 * @return 所有好友的在线状态及客户端类型信息
	 */
	public List<FriendStatus> getFriendStatus() {
		log.debug("开始获取好友状态");

		HttpResponse response = QQHttpUtil.get(ApiEnum.GET_FRIEND_STATUS, session.vfwebqq, session.psessionid);
		JSONArray result = QQHttpUtil.getArrayResult(response);
		return result.toList(FriendStatus.class);
	}

	/**
	 * 获得群的详细信息
	 *
	 * @param groupCode 群编号
	 * @return 群成员信息
	 */
	public GroupInfo getGroupInfo(long groupCode) {
		log.debug("开始获取群资料……");

		HttpResponse response = QQHttpUtil.get(ApiEnum.GET_GROUP_INFO, groupCode, session.vfwebqq);
		JSONObject result = QQHttpUtil.getObjResult(response);

		GroupInfo groupInfo = result.getBean("ginfo", GroupInfo.class);
		// 用于群成员查找
		Map<Long, GroupUser> groupUserMap = new LinkedHashMap<>();

		// 获得群成员信息
		JSONArray minfo = result.getJSONArray("minfo");
		for (JSONObject item : minfo.jsonIter()) {
			GroupUser groupUser = item.toBean(GroupUser.class);
			groupUserMap.put(groupUser.getUin(), groupUser);
			groupInfo.addUser(groupUser);
		}
		// 群成员客户端信息和在线状态
		JSONArray stats = result.getJSONArray("stats");
		for (JSONObject item : stats.jsonIter()) {
			GroupUser groupUser = groupUserMap.get(item.getLong("uin"));
			groupUser.setClientType(item.getInt("client_type"));
			groupUser.setStatus(item.getInt("stat"));
		}
		// cards
		JSONArray cards = result.getJSONArray("cards");
		for (JSONObject item : cards.jsonIter()) {
			groupUserMap.get(item.getLong("muin")).setCard(item.getStr("card"));
		}
		// VIP信息
		JSONArray vipinfo = result.getJSONArray("vipinfo");
		for (JSONObject item : vipinfo.jsonIter()) {
			GroupUser groupUser = groupUserMap.get(item.getLong("u"));
			groupUser.setVip(item.getInt("is_vip") == 1);
			groupUser.setVipLevel(item.getInt("vip_level"));
		}
		return groupInfo;
	}

	/**
	 * 获得讨论组的详细信息
	 *
	 * @param discussId 讨论组id
	 * @return
	 */
	public DiscussInfo getDiscussInfo(long discussId) {
		log.debug("开始获取讨论组资料");

		HttpResponse response = QQHttpUtil.get(ApiEnum.GET_DISCUSS_INFO, discussId, session.vfwebqq, session.psessionid);
		JSONObject result = QQHttpUtil.getObjResult(response);
		DiscussInfo discussInfo = result.getBean("info", DiscussInfo.class);
		// 获得讨论组成员信息
		Map<Long, DiscussUser> discussUserMap = new LinkedHashMap<>();

		// 讨论组成员
		JSONArray minfo = result.getJSONArray("mem_info");
		for (JSONObject item : minfo.jsonIter()) {
			DiscussUser discussUser = item.toBean(DiscussUser.class);
			discussUserMap.put(discussUser.getUin(), discussUser);
			discussInfo.addUser(discussUser);
		}
		// 成员状态信息
		JSONArray stats = result.getJSONArray("mem_status");
		for (JSONObject item : stats.jsonIter()) {
			DiscussUser discussUser = discussUserMap.get(item.getLong("uin"));
			discussUser.setClientType(item.getInt("client_type"));
			discussUser.setStatus(item.getInt("status"));
		}
		return discussInfo;
	}

	/**
	 * 拉取消息，此方法为一次性拉取消息
	 *
	 * @param listener 消息监听
	 */
	public void pollMessage(MessageListener listener) {
		log.debug("开始接收消息……");

		JSONObject r = JSONUtil.createObj()//
				.put("ptwebqq", session.ptwebqq)//
				.put("clientid", config.getClientId())//
				.put("psessionid", session.psessionid)//
				.put("key", "");

		HttpResponse response = QQHttpUtil.get(ApiEnum.POLL_MESSAGE, r);
		JSONArray array = QQHttpUtil.getArrayResult(response);
		for (JSONObject item : array.jsonIter()) {
			listener.onRecived(MessageFactory.createMessage(item.getStr("poll_type"), item.getJSONObject("value")));
		}
	}

	/**
	 * 发送消息<br>
	 * 目标ID因目标不同而意义不同规则如下：
	 * 
	 * <pre>
	 * 1、目标为好友，targetId表示好友ID
	 * 2、目标为群，targetId表示群ID
	 * 3、目标为讨论组，targetId表示讨论组ID
	 * </pre>
	 *
	 * @param messageType 消息类型枚举，决定发送消息到好友、群还是讨论组
	 * @param targetId 发送到的目标ID，依据消息类型不同此ID表示意义不同
	 * @param msg 消息内容
	 */
	public void sendMessage(MessageType messageType, long targetId, String msg) {
		JSONObject r = JSONUtil.createObj()//
				// 附带字体的消息内容，以JSON字符串表示
				.put("content", JSONUtil.toJsonStr(Arrays.asList(msg, Arrays.asList("font", Font.DEFAULT_FONT))))//
				.put("face", 573)//
				.put("clientid", config.getClientId())//
				.put("msg_id", MessageIdPool.INSTANCE.getNext())//
				.put("psessionid", session.psessionid);

		String urlParam = null;
		// 判断消息类型
		switch (messageType) {
		case FRIEND:
			log.debug("开始发送消息……");
			r.put("to", targetId);
			urlParam = "buddy";
			break;
		case GROUP:
			log.debug("开始发送群消息……");
			r.put("group_uin", targetId);
			urlParam = "qun";
			break;
		case DISCUSS:
			log.debug("开始发送讨论组消息……");
			r.put("did", targetId);
			urlParam = "discu";
			break;
		}

		HttpResponse response = QQHttpUtil.retryPost(config.getRetryCount(), r, ApiEnum.SEND_MESSAGE, urlParam);
		checkSendMsgResult(response);
	}

	// --------------------------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 调用获取用户列表接口，返回JSON
	 * 
	 * @return 用户列表信息JSON
	 */
	private JSONObject getFriendsJson() {
		log.debug("开始获取好友列表……");

		JSONObject r = new JSONObject();
		r.put("vfwebqq", session.vfwebqq);
		r.put("hash", session.getHashedUid());

		HttpResponse response = QQHttpUtil.post(r, ApiEnum.GET_FRIEND_LIST);
		return QQHttpUtil.getObjResult(response);
	}

	/**
	 * 将JSON返回值解析为好友ID和好友对象对应Map
	 * 
	 * @param result 接口返回JSON值
	 * @return 好友ID和好友对象对应Map
	 */
	private static Map<Long, Friend> parseFriendMap(JSONObject result) {
		Map<Long, Friend> friendMap = new LinkedHashMap<>();

		// 好友信息
		final JSONArray info = result.getJSONArray("info");
		for (JSONObject item : info.jsonIter()) {
			Friend friend = new Friend(item.getLong("uin"), item.getStr("nick"));
			friendMap.put(friend.getUserId(), friend);
		}

		// 备注名
		final JSONArray marknames = result.getJSONArray("marknames");
		for (JSONObject item : marknames.jsonIter()) {
			friendMap.get(item.getLong("uin")).setMarkname(item.getStr("markname"));
		}

		// VIP会员信息
		final JSONArray vipinfo = result.getJSONArray("vipinfo");
		Friend friend;
		for (JSONObject item : vipinfo.jsonIter()) {
			friend = friendMap.get(item.getLong("u"));
			friend.setVip(item.getInt("is_vip") == 1);
			friend.setVipLevel(item.getInt("vip_level"));
		}
		return friendMap;
	}

	// 检查消息是否发送成功
	private static void checkSendMsgResult(HttpResponse response) {
		final int status = response.getStatus();
		if (200 != status) {
			log.error("消息发送失败，Http返回码[{}]", status);
		}
		final JSONObject json = JSONUtil.parseObj(response.body());
		Integer retcode = json.getInt("retcode");
		if (null != retcode && 0 == retcode) {
			log.debug("消息发送成功。");
		} else {
			log.debug("消息发送失败，错误代码：[{}]", retcode);
		}
	}
	// --------------------------------------------------------------------------------------------------------------------- Private method end
}
