package org.baosight.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.baosight.pojo.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class CommonUtil {
	private static Logger log = LoggerFactory.getLogger(CommonUtil.class);

	public static String PROJECT_ROOT = "http://wechatapplication1.sinaapp.com/images/weather/";
	/** 处理https GET/POST 请求 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建sslcontext
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			TrustManager[] tm = { new MyX509TrustManager() };
			// 初始化
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpsUrlConn = (HttpsURLConnection) url.openConnection();
			httpsUrlConn.setRequestMethod(requestMethod);
			// 设置当前实例使用的sslsocketfactory
			httpsUrlConn.setSSLSocketFactory(ssf);
			httpsUrlConn.setDoOutput(true);
			httpsUrlConn.setDoInput(true);
			httpsUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpsUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpsUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				buffer.append(line);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			httpsUrlConn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}

//	// 获取access_token的接口地址（GET） 限200（次/天）
//	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
//
//	/**
//	 * 获取access_token
//	 * 
//	 * @param appid
//	 *            凭证
//	 * @param appsecret
//	 *            密钥
//	 * @return
//	 */
//	public static AccessToken getAccessToken(String appid, String appsecret) {
//		AccessToken accessToken = null;
//
//		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
//		String jsonString = CommonUtil.httpsRequest(requestUrl, "GET", null);
//		try {
//			// 讲json字符串转换为java对象
//			JSONObject jsonObject = JSONObject.fromObject(jsonString);
//
//			accessToken = new AccessToken();
//			accessToken.setToken(jsonObject.getString("access_token"));
//			accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//
//		return accessToken;
//	}
	
	
	
	/** 处理http GET/POST 请求 */
	public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setRequestMethod(requestMethod);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				buffer.append(line);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}
	

	/**
	 * url的utf8编码
	 * 
	 * @param source
	 * @return
	 */
	public static String urlEncodeUTF8(String source) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

}
