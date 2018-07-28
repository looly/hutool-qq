package cn.hutool.qq;

import java.io.File;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.qq.util.QQHashUtil;
import cn.hutool.qq.util.QQHttpUtil;
import cn.hutool.qq.util.QQReUtil;

/**
 * QQ会话，每次登录后维护一个会话
 * 
 * @author looly
 *
 */
public class QQSession {
	private static Log log = LogFactory.get();
	
	private QQConfig config;
	
	/** 二维码令牌 */
	private String qrsig;
	/** 鉴权参数 */
	protected String ptwebqq;
	/** 验证参数 */
	protected String vfwebqq;
	/** 用户ID */
	protected long uin;
	/** 会话ID */
	protected String psessionid;

	/**
	 * 构造，新建会话的同时登录QQ
	 * @param config QQ配置项
	 */
	public QQSession(QQConfig config) {
		this.config = config;
		login();
	}
	
	/**
	 * 获取用户ID的hash值
	 * @return 用户ID的hash值
	 */
	protected String getHashedUid() {
		return QQHashUtil.hashUid(this.uin, this.vfwebqq);
	}

	/**
	 * 登录
	 */
	private void login() {
		getQrCode();
		String url = verifyQRCode();
		getPtwebqq(url);
		getVfwebqq();
		getUinAndPsessionid();
	}

	/**
	 * 1、获取登录验证码
	 */
	private void getQrCode() {
		log.debug("获取登录二维码……");
		final HttpResponse response = QQHttpUtil.get(ApiEnum.GET_QR_CODE);
		final File destFile = FileUtil.file("qrcode.png");
		response.writeBody(destFile);
		this.qrsig = response.getCookieValue("qrsig");
		log.info("二维码保存于：[{}]，请打开手机QQ扫描登录。", FileUtil.getCanonicalPath(destFile));
	}

	/**
	 * 2、等待用户验证二维码
	 * 
	 * @return ptwebqq的URL
	 */
	private String verifyQRCode() {
		HttpResponse response;
		while (true) {
			ThreadUtil.sleep(5000);
			response = QQHttpUtil.get(ApiEnum.VERIFY_QR_CODE, QQHashUtil.hash33(this.qrsig));
			String result = response.body();
			log.debug(result);
			if (result.contains("成功")) {
				log.info("正在登录，请稍后……");
				return QQReUtil.getPtwebqqUrl(result);
			} else if (result.contains("失败")) {
				log.info("二维码已失效，正在尝试重新获取二维码……");
				getQrCode();
			}
		}
	}

	/**
	 * 获取鉴权码ptwebqq
	 * 
	 * @param url 用户验证二维码后返回的链接
	 */
	private void getPtwebqq(String url) {
		log.debug("开始获取ptwebqq……");
		final HttpResponse response = QQHttpUtil.get(ApiEnum.GET_PTWEBQQ, url);
		String ptwebqq = response.getCookieValue("ptwebqq");
		if (null == ptwebqq) {
			log.error("Response Cookies: {}", response.getCookies());
			throw new QQException("[ptwebqq] from cookie not exist!");
		}
	}

	/**
	 * 获取验证参数vfwebqq
	 */
	private void getVfwebqq() {
		log.debug("开始获取vfwebqq……");
		final HttpResponse response = QQHttpUtil.retryGet(config.getRetryCount(), ApiEnum.GET_VFWEBQQ, ptwebqq);
		final String vfwebqq = QQHttpUtil.getObjResult(response).getStr("vfwebqq");
		if (null == vfwebqq) {
			throw new QQException("[vfwebqq] 获取失败，接口返回：{}", response.body());
		}
		this.vfwebqq = vfwebqq;
	}

	// 登录流程5：获取uin和psessionid
	private void getUinAndPsessionid() {
		log.debug("开始获取uin和psessionid……");

		JSONObject r = JSONUtil.createObj()//
				.put("ptwebqq", ptwebqq)//
				.put("clientid", config.getClientId())//
				.put("psessionid", "")//
				.put("status", "online");

		final HttpResponse response = QQHttpUtil.post(r, ApiEnum.GET_UIN_AND_PSESSIONID);
		JSONObject result = QQHttpUtil.getObjResult(response);
		this.psessionid = result.getStr("psessionid");
		this.uin = result.getLong("uin");
	}
}
