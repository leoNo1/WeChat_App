package org.baosight.message.req;

/**
 * 文本消息
 * 
 * @author tangbin
 * @date 2015-12-09
 */
public class TextMessage extends BaseMessage {
	// 消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}