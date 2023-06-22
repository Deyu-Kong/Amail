package com.Amail.controller;

import com.Amail.pojo.Mail;
import com.Amail.service.AppendixService;
import com.Amail.service.ContentService;
import com.Amail.service.HeadService;
import com.Amail.service.MailListService;
import com.Amail.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 孔德昱
 * @date 2022/11/16 17:04 星期三
 */
@Controller
public class MailController {

    @Autowired
    private MailListService mailListService;

    @Autowired
    private HeadService headService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private AppendixService appendixService;

    /**
     * @Description 获取用户信息，登录！
     * @Author Kong Deyu
     * @Date 9:40 2022/11/18
     * @Param [request]
     * @return java.lang.String
     **/
    @RequestMapping("/login")
    public String login(HttpServletRequest request){
        String host = request.getParameter("host");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        session.setAttribute("host",host);
        session.setAttribute("username",username);
        session.setAttribute("password",password);
        return "redirect:/page/all";
//        return "redirect:/getMails";
    }

    /**
     * @Description 用于查看收件箱所有邮件的列表
     * @Author Kong Deyu
     * @Date 20:10 2022/11/16
     * @Param [request]
     * @return java.lang.String
     **/
    @RequestMapping("/getMails")
    public String getMailList(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String host = (String)session.getAttribute("host");
        String username = (String)session.getAttribute("username");
        String password = (String)session.getAttribute("password");
        List<Mail> mails = mailListService.getMailList(request,host,username,password);

        request.setAttribute("mails", mails);
        return "mailList";
    }

    /**
     * @Description 用于查看邮件正文
     * @Author Kong Deyu
     * @Date 20:10 2022/11/16
     * @Param [mailId]
     * @return java.lang.String
     **/
    @RequestMapping("/mailDetail/{mailId}")
    public String getMailDetail(HttpServletRequest request,@PathVariable("mailId")Integer mailId) throws IOException {
        Map<String,Object> map = headService.getHeader(request, mailId);
//        request.setAttribute("");
        request.setAttribute("header",map.get("header"));
        request.setAttribute("hasAppendix",map.getOrDefault("hasAppendix",false));
        request.setAttribute("msgNum",map.getOrDefault("msgNum",null));
        request.setAttribute("bodyNum",map.getOrDefault("bodyNum",null));
        request.setAttribute("filename",map.getOrDefault("filename",null));

        String content=contentService.getContent(request,mailId);
        request.setAttribute("content",content);
        return "message";
    }

    @RequestMapping("/appendix/{msgNum}/{bodyNum}/{filename}")
    public String getAppendix(HttpServletRequest request,
                              HttpServletResponse response,
                              @PathVariable("msgNum") Integer msgNum,
                              @PathVariable("bodyNum") Integer bodyNum,
                              @PathVariable("filename") String filename
    ) throws IOException {
        appendixService.getAppendix(request,response,msgNum,bodyNum,filename);
        return "redirect:mailDetail/"+request.getParameter("msgnum");
    }

    @RequestMapping("/write")
    public String write(){
        return "write";
    }

    @RequestMapping("send")
    public String send(MultipartFile attachment, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        String host = (String)session.getAttribute("host");
        String username = (String)session.getAttribute("username");
        String password = (String)session.getAttribute("password");
        String to = request.getParameter("to");
        String cc = request.getParameter("cc");
        String subject = request.getParameter("subject");
        String body = request.getParameter("body");
//        List<String>attachments=new ArrayList<>();

        SendService emailUtils = SendService.entity(host, username, password, to, cc, subject, body, attachment);

        emailUtils.send(); // 发送！
        //这里不能直接重定向回去到getMailList，因为request这次将不会再带有那些参数
        return "success";
    }
}
