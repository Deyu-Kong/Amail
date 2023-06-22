package com.Amail.utils;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;

public class Pop3Util {
    public static Folder getFolder(String host, String username, String password) {
        Properties prop = new Properties();
        prop.setProperty("mail.store.protocol", "pop3");
        prop.setProperty("mail.pop3.host", host);

        Session mailSession = Session.getDefaultInstance(prop, null);
        mailSession.setDebug(false);

        try {
            Store store = mailSession.getStore("pop3");
            store.connect(host, username, password);
            Folder folder = store.getFolder("inbox");
            folder.open(Folder.READ_WRITE);
            return folder;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}