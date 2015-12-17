package org.baosight.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.baosight.message.resp.Article;
import org.baosight.message.resp.MusicMessage;
import org.baosight.message.resp.NewsMessage;
import org.baosight.message.resp.TextMessage;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 消息工具类
 * 
 * @author tangbin
 * @date 2015-12-09
 */
public class MessageUtil {

	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	public static final String RESP_MESSAGE_TYPE_IMAGE = "image";
	public static final String RESP_MESSAGE_TYPE_VOICE = "voice";
	public static final String RESP_MESSAGE_TYPE_VIDEO = "video";
	/**
	 * 请求消息类型：文本
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";
	public static final String REQ_MESSAGE_TYPE_VIDEO = "video";

	/**
	 * 请求消息类型：推送
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";

	
	
	/**
	 * 获取微信加解密类的实例
	 */
	public static WXBizMsgCrypt getWXBizMsgCrypt(){
	WXBizMsgCrypt pc = null;
	try {
		pc = new WXBizMsgCrypt(SignUtil.token, "GTdhtg2RQy9VXOQaGpTqA85jkm9VvLJBzarMv6Tp5Oh", "wxc57af3aca3de0e4e");
	} catch (AesException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return pc;
	
	}
	
	/**
	 * 明文模式 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */

	public static HashMap<String, String> parseXml(HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
		HashMap<String, String> map = new HashMap<String, String>();

		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		recursiveParseXML(root, map);

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}

	/**
	 * 密文模式 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	
	public static HashMap<String, String> parseXmlCrypt(HttpServletRequest request) throws Exception {

		// 将解析结果存储在HashMap中
		HashMap<String, String> map = new HashMap<String, String>();

		/**
		 * 1.从inputstream中读取xml文本
		 */
		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		// 每次读取的内容
		String line = null;
		// 最终读取的内容
		StringBuffer buffer = new StringBuffer();
		while((line = br.readLine())!=null){
			buffer.append(line);
			
		}
		br.close();
		inputStream.close();
		/**
		 * 2.解密
		 * 
		 */
		
		String msgSignature = request.getParameter("msg_signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");

		WXBizMsgCrypt wxCrypt=  MessageUtil.getWXBizMsgCrypt();
		String fromXML = wxCrypt.decryptMsg(msgSignature, timestamp, nonce, buffer.toString());
		
		/**
		 * 3.解析xml，获取请求参数
		 */
		Document document = DocumentHelper.parseText(fromXML);
		// 得到xml根元素
		Element root = document.getRootElement();
		recursiveParseXML(root, map);

		return map;
	}

	// 级联解析xml
	public static void recursiveParseXML(Element root, HashMap<String, String> map) {

		List<Element> elementList = root.elements();
		if (elementList.size() == 0) {
			map.put(root.getName(), root.getTextTrim());

		} else {

			for (Element e : elementList) {

				recursiveParseXML(e, map);

			}
		}

	}

	/**
	 * 文本消息对象转换成xml
	 * 
	 * @param textMessage
	 *            文本消息对象
	 * @return xml
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 音乐消息对象转换成xml
	 * 
	 * @param musicMessage
	 *            音乐消息对象
	 * @return xml
	 */
	public static String musicMessageToXml(MusicMessage musicMessage) {
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}

	/**
	 * 图文消息对象转换成xml
	 * 
	 * @param newsMessage
	 *            图文消息对象
	 * @return xml
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}

	/**
	 * 扩展xstream，使其支持CDATA块
	 * 
	 * 
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@SuppressWarnings("unchecked")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}
