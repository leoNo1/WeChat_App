package org.baosight.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.baosight.message.resp.Article;
import org.baosight.message.resp.Music;
import org.baosight.message.resp.MusicMessage;
import org.baosight.message.resp.NewsMessage;
import org.baosight.message.resp.TextMessage;
import org.baosight.util.MessageUtil;
import org.baosight.util.WeChatUtil;
import org.baosight.util.WeatherUtil;

/**
 * 核心服务类
 * 
 * @author tangbin
 * @date 2015-12-09
 */
public class CoreService {
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String process(HashMap<String, String> requestMap) {
		String respXML = null;
		TextMessage textMessage = new TextMessage();
		try {
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);

			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				String content = requestMap.get("Content");
				if (content.startsWith("歌曲")) {
					// 如果以“歌曲”2个字开头
					// 将歌曲2个字及歌曲后面的+、空格、-等特殊符号去掉
					String keyWord = content.replaceAll("^歌曲[\\+ ~!@#%^-_=]?", "");
					// 如果歌曲名称为空
					if ("".equals(keyWord)) {
						String respContent = WeChatUtil.getUsage();

						textMessage.setContent(respContent);
						respXML = MessageUtil.textMessageToXml(textMessage);
					} else {
						String[] kwArr = keyWord.split("@");
						// 歌曲名称
						String musicTitle = kwArr[0];
						// 演唱者默认为空
						String musicAuthor = "";
						if (2 == kwArr.length)
							musicAuthor = kwArr[1];

						// 搜索音乐
						Music music = BaiduMusicService.searchMusic(musicTitle, musicAuthor);
						// 未搜索到音乐
						if (null == music) {
							String respContent = "对不起，没有找到你想听的歌曲<" + musicTitle + ">";

							textMessage.setContent(respContent);
							respXML = MessageUtil.textMessageToXml(textMessage);
						} else {
							// 音乐消息
							MusicMessage musicMessage = new MusicMessage();
							musicMessage.setToUserName(fromUserName);
							musicMessage.setFromUserName(toUserName);
							musicMessage.setCreateTime(new Date().getTime());
							musicMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_MUSIC);
							musicMessage.setMusic(music);
							respXML = MessageUtil.musicMessageToXml(musicMessage);
						}
					}

				}



			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				String respContent = "您发送的是图片消息！";
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				String respContent = "您发送的是地理位置消息！";
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				String respContent = "您发送的是链接消息！";
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				String respContent = "您发送的是音频消息！";
			} else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {
				String respContent = "您发送的是shi频消息！";
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {

					String respContent = "\ue40a本公众号正在持续开发并更新中，感谢您的关注!" + "[憨笑]" + "\n"
							+ "<a href=\"http://toutiao.com/\">今日新闻</a>";

					textMessage.setContent(respContent);
					respXML = MessageUtil.textMessageToXml(textMessage);

				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");

					if (eventKey.equals("11")) {
						String respContent = "天气预报菜单项被点击！";
					} else if (eventKey.equals("12")) {
						String respContent = "公交查询菜单项被点击！";
					} else if (eventKey.equals("13")) {
						NewsMessage newsMessage = new NewsMessage();
						newsMessage.setToUserName(fromUserName);
						newsMessage.setFromUserName(toUserName);
						newsMessage.setCreateTime(new Date().getTime());
						newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						newsMessage.setFuncFlag(0);
	
						List<Article> articleList = WeatherUtil.queryWeatherList("上海");
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respXML = MessageUtil.newsMessageToXml(newsMessage);
						
						
											
					} else if (eventKey.equals("14")) {
						String respContent = "历史上的今天菜单项被点击！";
					} else if (eventKey.equals("21")) {

						String respContent = WeChatUtil.getUsage();

						textMessage.setContent(respContent);
						respXML = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("22")) {
						String respContent = "经典游戏菜单项被点击！";
					} else if (eventKey.equals("23")) {
						String respContent = "美女电台菜单项被点击！";
					} else if (eventKey.equals("24")) {
						String respContent = "人脸识别菜单项被点击！";
					} else if (eventKey.equals("25")) {
						String respContent = "聊天唠嗑菜单项被点击！";
					} else if (eventKey.equals("31")) {
						String respContent = "Q友圈菜单项被点击！";
					} else if (eventKey.equals("32")) {
						String respContent = "电影排行榜菜单项被点击！";
					} else if (eventKey.equals("33")) {
						String respContent = "幽默笑话菜单项被点击！";
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return respXML;
	}
}
