import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class MailSendTests {

    @Test
    public void sendEmail() throws Exception {
        //QQ邮箱测试
        String userName = "1518579103@qq.com"; // 发件人邮箱
        String password = "gpnllxywxoxxjhbi"; // 发件人密码，其实不一定是邮箱的登录密码，对于QQ邮箱来说是SMTP服务的授权文本
        String smtpHost = "smtp.qq.com"; // 邮件服务器

        //163邮箱测试
        // String userName = "gblfy02@163.com"; // 发件人邮箱
        // String password = "TBFJUSKCUOPEYOYU"; // 发件人密码，其实不一定是邮箱的登录密码，对于QQ邮箱来说是SMTP服务的授权文本
        // String smtpHost = "smtp.163.com"; // 邮件服务器

//        String to = "275700993@qq.com"; // 收件人，多个收件人以半角逗号分隔
//        String cc = "1679077554@qq.com"; // 抄送，多个抄送以半角逗号分隔
        String to="yanshannanqwq@gmail.com";
        String cc=null;
//        String cc="2020302191852@whu.edu.cn";
        String subject = "这是邮件的主题"; // 主题
        String body = "这是邮件的正文"; // 正文，可以用html格式的哟
        List<String> attachments = Collections.singletonList(
                "D:\\学习\\课程学习\\大三上\\周1 操作系统\\课件\\第7章 死锁.pptx"
//                "D:\\学习\\课程学习\\大三上\\周1 操作系统\\课件\\第6章 进程同步.pptx",
//                "D:\\学习\\课程学习\\大三上\\周1 操作系统\\课件\\第9章 虚拟内存.pptx"
        ); // 附件的路径，多个附件也不怕

//        SendUtil emailUtils = SendUtil.entity(smtpHost, userName, password, to, cc, subject, body, attachments);

//        emailUtils.send(); // 发送！
    }

}

