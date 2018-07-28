package cn.hutool.qq.util;

import java.util.regex.Pattern;

import cn.hutool.core.util.ReUtil;

/**
 * QQ正则工具
 * 
 * @author looly
 *
 */
public class QQReUtil {
	private static Pattern VERIFY_QRCODE_RESP_PATTERN = Pattern.compile("ptuiCB\\('(\\d+)','(\\d+)','(.*?)','(\\d+)','(.*?)', '(.*?)'\\)");

	/**
	 * 获取ptwebqq的URL
	 * 
	 * @param verifyQrCodeResponse 验证QQ二维码的接口返回值
	 * @return URL
	 */
	public static String getPtwebqqUrl(String verifyQrCodeResponse) {
		return ReUtil.get(VERIFY_QRCODE_RESP_PATTERN, verifyQrCodeResponse, 3);
	}
}
