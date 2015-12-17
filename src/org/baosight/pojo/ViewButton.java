package org.baosight.pojo;

/**
 * view类型的菜单
 * 
 * @author tangbin
 * @date 2015-12-10
 */
public class ViewButton extends Button {
	private String type;
	private String url;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}