package com.Amail.service;

import com.Amail.utils.Pop3Util;
import com.Amail.pojo.Mail;
import org.springframework.stereotype.Service;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author 孔德昱
 * @date 2022/11/16 17:07 星期三
 */
@Service
public class MailListService {

    public List<Mail> getMailList(HttpServletRequest request,String host,String username,String password){
        long startTime1=System.currentTimeMillis();   //获取开始时间
        HttpSession session = request.getSession();

        String from = "";
        String subject = "";
        Folder folder = Pop3Util.getFolder(host,username,password);
        List<Mail> mails= new ArrayList<>();
        session.setAttribute("folder",folder);
        assert folder != null;
        try {
            Message[] messages = folder.getMessages();
            long startTime2=System.currentTimeMillis();   //获取开始时间

            for(int i=0;i<messages.length;i++)
            {
                //这一步耗时最长
                InternetAddress[] address = (InternetAddress[]) messages[i].getFrom();
                from = address[0].getAddress();
                if (from == null)
                    from = "";
                String personal = address[0].getPersonal();
                if (personal == null)
                    personal = "";
                String fromaddr = personal + "<" + from + ">";
                subject = messages[i].getSubject();
                Date sentDate = messages[i].getSentDate();
                String sendDateStr=null;
                if(sentDate!=null){
                    sendDateStr = DateFormat.getInstance().format(sentDate);
                }
                mails.add(new Mail(subject,fromaddr,i+1,sendDateStr,sentDate));
                Collections.sort(mails);
            }
            long endTime2=System.currentTimeMillis(); //获取结束时间
            System.out.println("for循环运行时间： "+(endTime2-startTime2)+"ms");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        long endTime1=System.currentTimeMillis(); //获取结束时间
        System.out.println("getMails运行时间： "+(endTime1-startTime1)+"ms");
        return mails;
    }
}
