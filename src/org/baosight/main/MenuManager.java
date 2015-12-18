package org.baosight.main;

import org.baosight.pojo.AccessToken;
import org.baosight.pojo.Button;
import org.baosight.pojo.CommonButton;
import org.baosight.pojo.ComplexButton;
import org.baosight.pojo.Menu;
import org.baosight.pojo.ViewButton;
import org.baosight.util.CommonUtil;
import org.baosight.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 菜单管理器类
 * 
 * @author tangbin
 * @date 2015-12-10
 */
public class MenuManager {
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);

	public static void main(String[] args) {
		// 第三方用户唯一凭证
		String appId = "wxc57af3aca3de0e4e";
		// 第三方用户唯一凭证密钥
		String appSecret = "3c50403d77e8a3774015a6da587674bf";

		// 调用接口获取access_token
		AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

		if (null != at) {
			// 调用接口创建菜单
			int result = WeixinUtil.createMenu(getMenu(), at.getToken());

			// 判断菜单创建结果
			if (0 == result)
				log.info("菜单创建成功！");
			else
				log.info("菜单创建失败，错误码：" + result);
		}
	}

	/**
	 * 组装菜单数据
	 * 
	 * @return
	 */
	private static Menu getMenu() {
		ViewButton btn11 = new ViewButton();
		btn11.setName("便民服务");
		btn11.setType("view");
		btn11.setUrl("http://wechatapplication1.sinaapp.com/index2.html");

		ViewButton btn12 = new ViewButton();
		btn12.setName("交通指数");
		btn12.setType("view");
		btn12.setUrl("http://60.12.175.34:8080/TrafficIndexWeb/indexroad.jsp");

		CommonButton btn13 = new CommonButton();
		btn13.setName("天气预报");
		btn13.setType("click");
		btn13.setKey("13");

		CommonButton btn21 = new CommonButton();
		btn21.setName("歌曲点播");
		btn21.setType("click");
		btn21.setKey("21");

		CommonButton btn22 = new CommonButton();
		btn22.setName("经典游戏");
		btn22.setType("click");
		btn22.setKey("22");


		CommonButton btn31 = new CommonButton();
		btn31.setName("朋友圈");
		btn31.setType("click");
		btn31.setKey("31");

	

		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("业务大厅");
		mainBtn1.setSub_button(new Button[] { btn11, btn12, btn13 });

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("休闲娱乐");
		mainBtn2.setSub_button(new Button[] { btn21, btn22 });

//		ComplexButton mainBtn3 = new ComplexButton();
//		mainBtn3.setName("更多体验");
//		mainBtn3.setSub_button(new Button[] { btn31});

		/**
		 * 每个一级菜单都有二级菜单项<br>
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
		 * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br>
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn1, mainBtn2, btn31 });

		return menu;
	}
}