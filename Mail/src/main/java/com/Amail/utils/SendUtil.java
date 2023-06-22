package com.Amail.utils;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSocket;

public class SendUtil
{
    public static String fromname="xxx@xxx.com",
            password = "xxxxxxxxxxx",toname="xxxx@xxxx.com";
    public static String server="smtp.xxxx.com";
    private static DataOutputStream dataout;
    public static BufferedReader bufferedReader=null;
    public static void main(String[] args) throws Exception
    {
        String fromname_base64=
                Base64.getEncoder().encodeToString
                        (fromname.getBytes(StandardCharsets.UTF_8));
        String password_base64=
                Base64.getEncoder().encodeToString
                        (password.getBytes(StandardCharsets.UTF_8));
        SSLSocketFactory ssf=
                (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sskt=
                (SSLSocket) ssf.createSocket(server, 465);
        bufferedReader=new BufferedReader
                (new InputStreamReader(sskt.getInputStream()));
        dataout=new DataOutputStream(sskt.getOutputStream());

        dos("HELO "+server+"\r\n",9);
        dos("AUTH LOGIN\r\n",1);
        dos(fromname_base64+"\r\n",1);
        dos(password_base64+"\r\n",1);
        dos("MAIL FROM:<"+fromname+">\r\n",1);
        dos("RCPT TO:<"+toname+">\r\n",1);
        dos("DATA\r\n",1);
        dos("Subject: Email test\r\n",0);
        dos("Email Body\r\n",0);
        dos(".\r\n",0);
        dos("QUIT\r\n",1);
    }
    private static void dos(String s, int lines) throws Exception
    {
        dataout.writeBytes(s);
        System.out.println("CLIENT: "+s);
        Thread.sleep(1000);
        for (int i=0;i<lines;i++)
            System.out.println
                    ("SERVER : "+bufferedReader.readLine());
    }
}
