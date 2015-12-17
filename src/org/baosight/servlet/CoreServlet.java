package org.baosight.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.baosight.service.CoreService;
import org.baosight.util.MessageUtil;
import org.baosight.util.SignUtil;

/**
 * 核心请求处理类
 * 
 * @author tangbin
 * @date 2015-12-09
 */
public class CoreServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;

	/**
	 * 确认请求来自微信服务器
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		PrintWriter out = response.getWriter();
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
		out.close();
		out = null;
	}

	/**
	 * 处理微信服务器发来的消息
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 加解密类型
		String encryptType = request.getParameter("encrypt_type");

		try {
			// 响应消息
			PrintWriter out = response.getWriter();
			if (SignUtil.checkSignature(signature, timestamp, nonce)) {
				HashMap<String, String> requestMap = null;

				// 加解密模式
				if ("aes".equals(encryptType)) {

					requestMap = MessageUtil.parseXmlCrypt(request);

					String respXML = CoreService.process(requestMap);
					// 返回消息加密
					respXML = MessageUtil.getWXBizMsgCrypt().encryptMsg(respXML, timestamp, nonce);
					out.write(respXML);
				}
				// 明文模式
				else {
					requestMap = MessageUtil.parseXml(request);

					// 调用核心业务类接收消息、处理消息
					String respXML = CoreService.process(requestMap);
					out.write(respXML);
				}

			}

			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
