package com.Amail.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class HeadService{
    public Map<String,Object> getHeader(HttpServletRequest request,int msgnum) throws IOException {
        HttpSession session = request.getSession();
        Folder folder = (Folder) session.getAttribute("folder");

        StringBuilder sb = null;
        Map<String,Object>map=new HashMap<>();
        try {
            Message msg = folder.getMessage(msgnum);
            InternetAddress address[] = (InternetAddress[]) msg.getFrom();
            String from = address[0].getAddress();
            if (from == null)
                from = "";
            String personal = address[0].getPersonal();
            if (personal == null)
                personal = "";
            String fromAddr = personal + "<" + from + ">";
            String subject = msg.getSubject();
            String sendDate=null;
            if(msg.getSentDate()!=null){
                sendDate = DateFormat.getInstance().format(msg.getSentDate());
            }

            sb = new StringBuilder();
            sb.append("邮件主题：").append(subject).append("<br/>");
            sb.append("发件人:").append(fromAddr).append("<br/>");
            sb.append("发送日期：").append(sendDate).append("<br/><br/>");

            map.put("header",sb.toString());
            // 如果该邮件是组合型"multipart/*"则可能包含附件等
            if (msg.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) msg.getContent();

                for (int i = 0; i < mp.getCount(); i++) {
                    BodyPart bp = mp.getBodyPart(i);

                    // 如果该BodyPart对象包含附件，则应该解析出来
                    if (bp.getDisposition() != null) {
                        map.put("hasAppendix",true);
                        String filename = bp.getFileName();
//                        System.out.println("filename：" + filename);

                        if (filename.startsWith("=?")) {
                            // 把文件名编码成符合RFC822规范
                            filename = MimeUtility.decodeText(filename);
                        }
                        map.put("msgNum",msgnum);
                        map.put("bodyNum",i);
                        map.put("filename",filename);
                        // 生成打开附件的超链接
//                        sb.append("附件：");
//                        sb.append("<a th:href=\"@{HandleAttach/").append(msgnum).append("/").append(i).
//                                append("/").append(filename).append("}\">").append(filename).append("</a><br/>");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}