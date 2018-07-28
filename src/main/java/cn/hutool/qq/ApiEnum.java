package cn.hutool.qq;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Api的请求地址和Referer<br>
 * 来自smartQQ项目
 *
 * @author ScienJus
 */
public enum ApiEnum {
	/** 登录二维码接口 */
	GET_QR_CODE(//
			"https://ssl.ptlogin2.qq.com/ptqrshow?appid=501004106&e=0&l=M&s=5&d=72&v=4&t=0.1", //
			null//
	),

	/** 验证二维码接口 */
	VERIFY_QR_CODE(//
			"https://ssl.ptlogin2.qq.com/ptqrlogin?" + //
					"ptqrtoken={0}&webqq_type=10&remember_uin=1&login2qq=1&aid=501004106&" + //
					"u1=https%3A%2F%2Fw.qq.com%2Fproxy.html%3Flogin2qq%3D1%26webqq_type%3D10&" + //
					"ptredirect=0&ptlang=2052&daid=164&from_ui=1&pttype=1&dumy=&fp=loginerroralert&0-0-157510&" + //
					"mibao_css=m_webqq&t=undefined&g=1&js_type=0&js_ver=10184&login_sig=&pt_randsalt=3", //
			QQReferer.REFERER_VERIFY//
	),

	/** 获取PTWEBQQ接口 */
	GET_PTWEBQQ(//
			"{0}", //
			null//
	),

	/** 获取VFWEBQQ接口 */
	GET_VFWEBQQ(//
			"https://s.web2.qq.com/api/getvfwebqq?ptwebqq={0}&clientid=53999199&psessionid=&t=0.1", //
			QQReferer.REFERER_S//
	),
	/** 获取UID和PSessionId接口 */
	GET_UIN_AND_PSESSIONID(//
			"https://d1.web2.qq.com/channel/login2", //
			QQReferer.REFERER_D1//
	),
	/** 获取QQ群接口 */
	GET_GROUP_LIST(//
			"https://s.web2.qq.com/api/get_group_name_list_mask2", //
			QQReferer.REFERER_D1//
	),

	/** 接收消息接口 */
	POLL_MESSAGE(//
			"https://d1.web2.qq.com/channel/poll2", //
			QQReferer.REFERER_D1//
	),
	
	/** 发送消息接口 */
	SEND_MESSAGE(//
			"https://d1.web2.qq.com/channel/send_{0}_msg2", //
			QQReferer.REFERER_D1//
	),

	/** 发送群消息接口 */
	SEND_MESSAGE_TO_GROUP(//
			"https://d1.web2.qq.com/channel/send_qun_msg2", //
			QQReferer.REFERER_D1//
	),

	/** 发消息接口 */
	SEND_MESSAGE_TO_FRIEND(//
			"https://d1.web2.qq.com/channel/send_buddy_msg2", //
			QQReferer.REFERER_D1//
	),

	/** 发送消息到讨论组 */
	SEND_MESSAGE_TO_DISCUSS(//
			"https://d1.web2.qq.com/channel/send_discu_msg2", //
			QQReferer.REFERER_D1//
	),

	/** 获取所有好友列表接口 */
	GET_FRIEND_LIST(//
			"https://s.web2.qq.com/api/get_user_friends2", //
			QQReferer.REFERER_S//
	),

	/** 获取会话列表接口 */
	GET_DISCUSS_LIST(//
			"https://s.web2.qq.com/api/get_discus_list?clientid=53999199&psessionid={0}&vfwebqq={1}&t=0.1", //
			QQReferer.REFERER_S//
	),

	/** 获取帐户信息 */
	GET_ACCOUNT_INFO(//
			"https://s.web2.qq.com/api/get_self_info2?t=0.1", //
			QQReferer.REFERER_S//
	),

	/** 获取当前列表信息 */
	GET_RECENT_LIST(//
			"https://d1.web2.qq.com/channel/get_recent_list2", //
			QQReferer.REFERER_D1//
	),

	/** 获取好友状态 */
	GET_FRIEND_STATUS(//
			"https://d1.web2.qq.com/channel/get_online_buddies2?vfwebqq={0}&clientid=53999199&psessionid={1}&t=0.1", //
			QQReferer.REFERER_D1//
	),

	/** 获取群信息 */
	GET_GROUP_INFO(//
			"https://s.web2.qq.com/api/get_group_info_ext2?gcode={0}&vfwebqq={1}&t=0.1", //
			QQReferer.REFERER_S//
	),

	/** 通过ID获取QQ */
	GET_QQ_BY_ID(//
			"https://s.web2.qq.com/api/get_friend_uin2?tuin={0}&type=1&vfwebqq={1}&t=0.1", //
			QQReferer.REFERER_S//
	),
	/** 获取讨论组信息 */
	GET_DISCUSS_INFO(//
			"https://d1.web2.qq.com/channel/get_discu_info?did={0}&vfwebqq={1}&clientid=53999199&psessionid={3}&t=0.1", //
			QQReferer.REFERER_D1//
	),
	/** 获取好友信息 */
	GET_FRIEND_INFO(//
			"https://s.web2.qq.com/api/get_friend_info2?tuin={1}&vfwebqq={2}&clientid=53999199&psessionid={3}&t=0.1", //
			QQReferer.REFERER_S//
	);

	/** API接口URL */
	private String url;
	/** API接口URL */
	private String referer;

	/**
	 * 构造
	 * 
	 * @param url URL
	 * @param referer Referer
	 */
	ApiEnum(String url, String referer) {
		this.url = url;
		this.referer = referer;
	}

	/**
	 * 获取URL
	 * 
	 * @return URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 获取Referer
	 * 
	 * @return Referer
	 */
	public String getReferer() {
		return referer;
	}

	/**
	 * 构建URL
	 * 
	 * @param urlParams URL参数值列表
	 * @return URL
	 */
	public String buildUrl(Object... urlParams) {
		if(ArrayUtil.isEmpty(urlParams)) {
			return this.url;
		}
		final Object[] args = Convert.toStrArray(urlParams);
		return StrUtil.indexedFormat(this.url, args);
	}

	/**
	 * 获取Origin
	 * 
	 * @return Origin
	 */
	public String getOrigin() {
		return this.url.substring(0, url.lastIndexOf("/"));
	}

	/**
	 * QQ的Referer定义
	 * 
	 * @author looly
	 *
	 */
	private static class QQReferer {
		/** d1子域名Referer */
		private static final String REFERER_D1 = "https://d1.web2.qq.com/proxy.html?v=20151105001&callback=1&id=2";
		/** s子域名Referer */
		private static final String REFERER_S = "https://s.web2.qq.com/proxy.html?v=20130916001&callback=1&id=1";
		/** 验证二维码Referer */
		private static final String REFERER_VERIFY = "https://ui.ptlogin2.qq.com/cgi-bin/login?" + //
				"daid=164&target=self&style=16&mibao_css=m_webqq&appid=501004106&enable_qlogin=0&no_verifyimg=1&" + //
				"s_url=https%3A%2F%2Fw.qq.com%2Fproxy.html&f_url=loginerroralert&strong_login=1&login_state=10&t=20131024001";//
	}
}