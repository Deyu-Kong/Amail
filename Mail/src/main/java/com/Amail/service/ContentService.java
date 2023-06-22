package com.Amail.service;

import com.Amail.utils.ReceiveUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class ContentService{
    public String getContent(HttpServletRequest request, int msgnum) throws IOException {
        HttpSession session = request.getSession();
        Folder folder = (Folder) session.getAttribute("folder");
        String content=null;
        try {
            Message msg = folder.getMessage(msgnum);
            ReceiveUtil process=new ReceiveUtil((MimeMessage)msg);
            process.getMailContent((Part) msg);
            content=process.getBodyText().replace("%", "%%");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}