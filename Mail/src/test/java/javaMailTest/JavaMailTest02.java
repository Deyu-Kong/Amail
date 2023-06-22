package javaMailTest;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;

/**
 * @author 孔德昱
 * @date 2022/11/15 14:21 星期二
 */
public class JavaMailTest02 {
    public static void main(String[] args) {
        //装载服务器属性，并与服务器建立连接
        String smtpHost=null,sendAddress=null,userName=null,password=null;
        Properties p = new Properties();
        Session session=null;
        try {
            //从文件中读入相关的服务器属性设置
            FileInputStream fileIn = new FileInputStream("test01/src/main/resources/smtp.properties");
            p.load(fileIn);
            smtpHost=p.getProperty("smtp.host");
            sendAddress=p.getProperty("smtp.address");
            userName=p.getProperty("smtp.username");
            password=p.getProperty("smtp.password");
            fileIn.close();
            //创建与服务器的对话
            p=new Properties();
            p.put("mail.smtp,host",smtpHost);
            p.put("mail.smtp.auth","true");//设置身份验证为真,如果发邮件时需要身份验证必须设为真
            session = Session.getInstance(p, null);
            session.setDebug(true);
        }
        catch (Exception ex) {
            System.out.println("装载服务器属性出错！");
            ex.printStackTrace();
        }
        MimeMessage msg=null;
        //建构邮件信息
        try{
            msg = new MimeMessage(session);
            //处理邮件头部
            String to="kdy1518579103@sina.com";
            String cc="";
            if(!to.equals(""))
                msg.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            if(!cc.equals(""))
                msg.addRecipient(Message.RecipientType.BCC,new InternetAddress(cc));
            String subject="这是一个测试";
            msg.setSubject(subject);//设置邮件主题
            assert sendAddress != null;
            msg.setFrom(new InternetAddress(sendAddress));
            msg.setSentDate(new Date());//设置邮件发送日期

            Multipart mp=new MimeMultipart();//创建用于封装邮件的Multipart对象
            //处理邮件正文
            MimeBodyPart mbp1=new MimeBodyPart();
            String text="这是测试的正文";
            mbp1.setText(text);
//            mbp1.setText(contentArea.getText());
            mp.addBodyPart(mbp1);


            //处理邮件附件：先暂时不要附件
//            MimeBodyPart mbpAttatch;
//            FileDataSource fds;
//            BASE64Encoder enco=new BASE64Encoder();
//            String sendFileName="";
//            if(attatchFiles.size()!=0){
//                for (int i = 0; i < attatchFiles.size(); i++) {
//                    mbpAttatch = new MimeBodyPart();
//                    fds = new FileDataSource(attatchFiles.get(i).toString());
//                    mbpAttatch.setDataHandler(new DataHandler(fds));
//                    //将文件名进行BASE64编码
//                    sendFileName="=?GB2312?B?"+enco.encode(new String(fds.getName().getBytes(),"gb2312").getBytes("gb2312"))+"?=";
//                    mbpAttatch.setFileName(sendFileName);
//                    mp.addBodyPart(mbpAttatch);
//                }
//            }
//            //封装并保存邮件信息
//            msg.setContent(mp);
//            msg.saveChanges();
        }catch(Exception ex){
            System.out.println("构建邮件出错！");
            ex.printStackTrace();
        }
        try{
            assert session != null;
            Transport transport=session.getTransport("smtp");
            transport.connect(smtpHost,userName,password);
            assert msg != null;
            transport.sendMessage(msg,msg.getAllRecipients());
            transport.close();
            System.out.println("发送邮件成功!");
        }catch(Exception ex)
        {
            System.out.println("发送邮件失败!");
        }
    }
}
