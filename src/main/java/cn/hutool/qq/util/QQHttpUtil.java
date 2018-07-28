package cn.hutool.qq.util;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.qq.ApiEnum;
import cn.hutool.qq.QQException;

/**
 * QQ的Http客户端请求工具
 * 
 * @author looly
 *
 */
public class QQHttpUtil {

	/**
	 * 发送HTTP Get请求
	 * 
	 * @param url 地址枚举
	 * @param urlParams 参数
	 * @return 响应结果
	 */
	public static String getForStr(ApiEnum url, Object... urlParams) {
		return get(url, urlParams).body();
	}

	/**
	 * 多次重试发送GET请求
	 * 
	 * @param retryCount 重试次数
	 * @param url API地址枚举{@link ApiEnum}
	 * @param urlParams 参数
	 * @return {@link HttpResponse}
	 */
	public static HttpResponse retryGet(int retryCount, ApiEnum url, Object... urlParams) {
		HttpResponse response = get(url, urlParams);
		while ((response.getStatus() < 400) && (--retryCount > 0)) {
			response.close();
			response = get(url, urlParams);
		}
		return response;
	}

	/**
	 * 发送HTTP Get请求
	 * 
	 * @param url 地址枚举
	 * @param urlParams URL参数
	 * @return 响应结果
	 */
	public static HttpResponse get(ApiEnum url, Object... urlParams) {
		String urlStr = url.buildUrl(urlParams);
		final HttpRequest request = HttpRequest.get(urlStr);
		final String referer = url.getReferer();
		if (null != referer) {
			request.header(Header.REFERER, referer);
		}
		return request.execute();
	}
	
	/**
	 * 多次重试发送POST请求
	 * 
	 * @param retryCount 重试次数
	 * @param arguments 参数
	 * @param url API地址枚举{@link ApiEnum}
	 * @param urlParams URL参数替换
	 * @return {@link HttpResponse}
	 */
	public static HttpResponse retryPost(int retryCount, JSONObject arguments, ApiEnum url, Object... urlParams) {
		HttpResponse response = post(arguments, url, urlParams);
		while ((response.getStatus() < 400) && (--retryCount > 0)) {
			response.close();
			response = post(arguments, url, urlParams);
		}
		return response;
	}

	/**
	 * 发送HTTP POST请求
	 * 
	 * @param arguments 参数
	 * @param url 地址枚举
	 * @param urlParams URL参数替换
	 * @return 响应结果
	 */
	public static HttpResponse post(JSONObject arguments, ApiEnum url, Object... urlParams) {
		final HttpRequest request = HttpRequest.post(url.buildUrl(urlParams)).form("r", arguments);
		final String referer = url.getReferer();
		if (null != referer) {
			request.header(Header.REFERER, referer);
		}
		final String origin = url.getOrigin();
		if (null != origin) {
			request.header("Origin", origin);
		}
		return request.execute();
	}

	/**
	 * 获取响应中的JSON数据中的"result"对应内容，并检查返回的retcode
	 * 
	 * @param response 响应
	 * @return 返回的JSON
	 */
	public static JSONObject getObjResult(HttpResponse response) {
		return getJson(response).getJSONObject("result");
	}

	/**
	 * 获取响应中的JSON数据中的"result"对应内容，并检查返回的retcode
	 * 
	 * @param response 响应
	 * @return 返回的JSON
	 */
	public static JSONArray getArrayResult(HttpResponse response) {
		return getJson(response).getJSONArray("result");
	}

	/**
	 * 获取响应中的JSON数据，并检查返回的retcode
	 * 
	 * @param response 响应
	 * @return 返回的JSON
	 */
	public static JSONObject getJson(HttpResponse response) {
		final int status = response.getStatus();
		if (status >= 400) {
			throw new QQException("请求失败，HTTP状态码：[{}]", status);
		}

		final JSONObject json = JSONUtil.parseObj(response.body());
		final Integer retCode = json.getInt("retcode");
		if (retCode == null) {
			throw new QQException("请求失败，API返回：\n{}", json.toStringPretty());
		}

		if(0 != retCode) {
			final String msg = RetCodes.getMsg(retCode);
			if (null == msg) {
				throw new QQException("请求失败，错误代码：[{}]", retCode);
			} else {
				throw new QQException("请求失败，错误：[{}] {}", retCode, msg);
			}
		}

		return json;
	}
}
