package com.love;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSONObject;

@Slf4j
public class LeaveMessageServlet extends HttpServlet {
	
	public static File file = null;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");
		
		String Content = name + "|" + email + "|" + subject + "|" + message + "\n";
		IOUtils.write(Content, new FileOutputStream(file, true), "utf-8");
		log.error("【留言】：" + name + "|" + email + "|" + subject + "|" + message);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			file = new File(request.getRealPath("/WEB-INF/classes") + "/message.txt");
			
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter writer = response.getWriter();
			
			List<String> listString = IOUtils.readLines(new FileInputStream(file), "utf-8");
			List<Message> messageList = new ArrayList<Message>();
			
			for (String contentStr : listString) {
				if (null != contentStr && !"".equals(contentStr)) {
					String[] contents = contentStr.replaceAll("\r\n", "").split("\\|");
					if (contents.length < 3) {
						continue;
					} else {
						Message message = new Message();
						message.setName(contents[0]);
						message.setMessage(contents[3]);
						message.setSubject(contents[2]);
						messageList.add(message);
					}
				}
			}
			writer.write(JSONObject.toJSONString(messageList));
		} catch (Exception e) {
			log.error("获取留言板信息失败：" + e);
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> listString = IOUtils.readLines(new FileInputStream(file));
		List<Message> messageList = new ArrayList<Message>();
		
		for (String contentStr : listString) {
			if (null != contentStr && !"".equals(contentStr)) {
				String[] contents = contentStr.replaceAll("\r\n", "").split("\\|");
				if (contents.length < 3) {
					continue;
				} else {
					Message message = new Message();
					message.setName(contents[0]);
					message.setMessage(contents[2]);
					message.setSubject(contents[3]);
					messageList.add(message);
				}
			}
		}
		System.out.println(JSONObject.toJSONString(messageList));
	}
}
