package com.ajie.loginsendmail.utils;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMailUtil {

    public static void sendMail(String msg) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", "smtp.qq.com");// 主机名
        properties.put("mail.smtp.port", 465);// 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
        properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
        Session session = Session.getInstance(properties);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("1149023676@qq.com"));
        InternetAddress[] receipients = new InternetAddress[2];
        receipients[0] = new InternetAddress("1149023676@qq.com");
		receipients[1] = new InternetAddress("844824689@qq.com");
        message.setRecipients(Message.RecipientType.TO, receipients);
        message.setSubject("qy服务器登录提醒");
        message.setText(msg);
        Transport transport = session.getTransport();
        // 连接自己的邮箱账户
        transport.connect("1149023676@qq.com", "tgfivisrjlghjcdh");// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
        // 发送邮件
		System.out.println("开始发送邮件");
        transport.sendMessage(message, message.getAllRecipients());
		System.out.println("发送完毕");
        transport.close();
    }
}
