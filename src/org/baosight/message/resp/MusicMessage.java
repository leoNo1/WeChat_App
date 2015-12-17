package org.baosight.message.resp;

/**
 * 音乐消息
 * 
 * @author tangbin
 * @date 2015-12-09
 */
public class MusicMessage extends BaseMessage {
	// 音乐
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
}