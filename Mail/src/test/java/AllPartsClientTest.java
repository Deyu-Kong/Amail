import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import javax.mail.*;
public class AllPartsClientTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            String pop3Server = "pop.qq.com";
            String protocol = "pop3";
            String username = "1518579103@qq.com";
            String password = "gpnllxywxoxxjhbi"; // QQ邮箱的SMTP的授权码，什么是授权码，它又是如何设置？

            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", protocol); // 使用的协议（JavaMail规范要求）
            props.setProperty("mail.smtp.host", pop3Server); // 发件人的邮箱的 SMTP服务器地址

            // 获取连接
            Session session = Session.getDefaultInstance(props);
            session.setDebug(false);

            // 获取Store对象
            Store store = session.getStore(protocol);
            store.connect(pop3Server, username, password); // POP3服务器的登陆认证

            // 通过POP3协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
            Folder folder = store.getFolder("INBOX");// 获得用户的邮件帐户

            if(folder==null){
                System.out.println("Folder not found!");
                System.exit(1);
            }
            folder.open(Folder.READ_WRITE);
            //从服务器获取消息
            Message[] ms=folder.getMessages();
            for(int i=0;i<ms.length;i++){
                System.out.println("-----------Message"+i+"begin-----------");
                //显示消息首部
                Enumeration headers=ms[i].getAllHeaders();
                while(headers.hasMoreElements()){
                    Header header=(Header) headers.nextElement();
                    System.out.println(header.getName()+" : "+header.getValue());
                }
                System.out.println();
                //枚举各个部分
                Object obj=ms[i].getContent();
                if(obj instanceof Multipart){
                    processMultipart((Multipart)obj);
                }else{
                    processPart(ms[i]);
                }

            }
            folder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void processPart(Part part) {
        try {
            String fileName=part.getFileName();
            String disposition=part.getDisposition();
            String contentType=part.getContentType();
            if(contentType.toLowerCase().startsWith("multipart/*")){
                processMultipart((Multipart)part.getContent());
            }else if((fileName==null)&&(Part.ATTACHMENT.equalsIgnoreCase(disposition))
                    ||(!contentType.equalsIgnoreCase("text/plain"))){
                if(fileName!=null){
                    if(fileName.endsWith(".jpg")){
                        fileName=File.createTempFile("attachment", ".jpg").getName();
                    }else{
                        fileName=File.createTempFile("attachment", ".txt").getName();
                    }
                }

            }
            if(fileName==null){
                //可能是内部邮件
                part.writeTo(System.out);
            }else{
                File f=new File(fileName);
                for(int i=1;f.exists();i++){
                    String newName=fileName+"_"+i;
                    f=new File(newName);
                }
                OutputStream output=new BufferedOutputStream(new FileOutputStream(f));
                InputStream input=new BufferedInputStream(part.getInputStream());
                int b;
                while((b=input.read())!=-1){
                    output.write(b);
                }
                output.flush();
                output.close();
                input.close();
                System.out.println("附件"+f.getAbsolutePath()+"成功下载!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processMultipart(Multipart obj) throws MessagingException {
        for(int i=0;i<obj.getCount();i++){
            processPart(obj.getBodyPart(i));
        }
    }

}