package org.baosight.message.req;

/**
 * 图片消息
 * 
 * @author tangbin
 * @date 2015-12-09
 */
public class ImageMessage extends BaseMessage {
	// 图片链接
	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
}
