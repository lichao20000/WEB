// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   MailerBean.java

package com.linkage.litms.mail;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.Encoder;

public class MailerBean
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(MailerBean.class);
    class SendThread extends Thread
    {

        String address[];
        String Title;
        String Body;
        String attatchPath[];
        boolean IsRun;

        public void run()
        {
            IsRun = true;
            try
            {
                if(session == null)
                {
                    Properties props = System.getProperties();
                    props.put("mail.smtp.host", MailerBean.smtpServer);
                    logger.debug("mail.smtp.host"+MailerBean.smtpServer);
                    props.put("mail.smtp.auth", MailerBean.auth);
                    session = Session.getDefaultInstance(props, null);
                    transport = session.getTransport("smtp");
                    transport.connect(MailerBean.smtpServer, MailerBean.user, MailerBean.password);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                log(e, " Create Mail Session Fail");
                IsRun = false;
                return;
            }
            try
            {
                MimeMessage msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(MailerBean.from));
                LogMessage = "Mail  from:" + MailerBean.from;
                LogMessage = LogMessage + " to: ";
                javax.mail.Address to[] = new InternetAddress[address.length];
                for(int i = 0; i < address.length; i++)
                {
                    to[i] = new InternetAddress(address[i]);
                    LogMessage = LogMessage + address[i] + ",";
                }

                msg.setRecipients(javax.mail.Message.RecipientType.TO, to);
                LogMessage = LogMessage + "  Subject:" + Title;
                Title = Encoder.AsciiToChineseString(Title);
                msg.setSubject(Title);
                msg.setSentDate(new Date());
                MimeMultipart multipart = new MimeMultipart();
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                LogMessage = LogMessage + "  Body:" + Body;
                Body = Encoder.AsciiToChineseString(Body);
                messageBodyPart.setText(Body);
                multipart.addBodyPart(messageBodyPart);
                if(attatchPath != null)
                {
                    LogMessage = LogMessage + "  attatchPath:";
                    for(int i = 0; i < attatchPath.length; i++)
                    {
                        messageBodyPart = new MimeBodyPart();
                        LogMessage = LogMessage + attatchPath[i] + ",";
                        attatchPath[i] = Encoder.AsciiToChineseString(attatchPath[i]);
                        javax.activation.DataSource source = new FileDataSource(attatchPath[i]);
                        messageBodyPart.setDataHandler(new DataHandler(source));
                        int pos = attatchPath[i].lastIndexOf("/");
                        if(pos == -1)
                            pos = attatchPath[i].lastIndexOf("\\");
                        if(pos != -1)
                            attatchPath[i] = attatchPath[i].substring(pos + 1, attatchPath[i].length());
                        messageBodyPart.setFileName(attatchPath[i]);
                        multipart.addBodyPart(messageBodyPart);
                    }

                }
                msg.setContent(multipart);
                logger.debug("connect OK.");
                transport.sendMessage(msg, to);
                logger.debug("send message OK.");
                LogMessage = LogMessage + " message OK.";
                log(LogMessage);
                IsRun = false;
            }
            catch(Exception e)
            {
                LogMessage = LogMessage + " message Fail.";
                e.printStackTrace();
                log(e, LogMessage);
                IsRun = false;
                return;
            }
        }

        SendThread()
        {
            IsRun = false;
        }
    }


    private static MailerBean Instance = null;
    private Session session;
    private Transport transport;
    private static String smtpServer = "218.94.62.106";
    private static String from = "zouyx@lianchuang.com";
    private static String user = "liulj";
    private static String password = "gameov";
    private static String auth = "false";
    private static String maillog = ".";
    private static PrintWriter Log = null;
    String LogMessage;
    private ArrayList Threadpool;

    private MailerBean()
    {
        session = null;
        transport = null;
        LogMessage = "";
        Threadpool = null;
        Threadpool = new ArrayList();
        try
        {
            //java.io.InputStream is = getClass().getResourceAsStream("/mail.properties");
            //Properties Props = new Properties();
            //Props.load(is);
            //smtpServer = Props.getProperty("mail.smtp.host");
            //from = Props.getProperty("mail.from");
            //user = Props.getProperty("User");
            //password = Props.getProperty("Password");
            //auth = Props.getProperty("mail.smtp.auth");
            maillog = LipossGlobals.getLipossHome();
            maillog = maillog + "/logs/mailog.log";
            if(!(new File(maillog)).exists())
                (new File(maillog)).createNewFile();
            try
            {
                Log = new PrintWriter(new FileWriter(maillog, true), true);
            }
            catch(Exception e)
            {
                logger.error("无法打开日志文件:" + maillog);
                Log = new PrintWriter(System.err);
            }
        }
        catch(Exception e)
        {
            logger.error("Cann't Read Property file,");
        }
    }

    public static MailerBean getInstance()
    {
        if(Instance == null)
            Instance = new MailerBean();
        return Instance;
    }

    private void log(String msg)
    {
        try
        {
            msg = new String(msg.getBytes(), "iso8859-1");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        Log.println(new Date() + ":" + msg);
    }

    private void log(Throwable e, String msg)
    {
        try
        {
            msg = new String(msg.getBytes("GBK"), "iso8859-1");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        Log.println(new Date() + ":" + msg);
        e.printStackTrace(Log);
    }

    public synchronized short sendMail(String address[], String Title, String Body, String attatchPath[])
    {
        if(address == null)
            return -1;
        if(Title == null && Body == null)
            return -1;
        if(address.length <= 0)
            return -1;
        if(Title == null)
            Title = "";
        if(Body == null)
            Body = "";
        Iterator I = Threadpool.iterator();
        if(!I.hasNext())
        {
            SendThread sendth = new SendThread();
            sendth.address = address;
            sendth.Title = Title;
            sendth.Body = Body;
            sendth.attatchPath = attatchPath;
            sendth.IsRun = false;
            Threadpool.add(sendth);
            sendth.run();
        } else
        {
            SendThread sendth;
            for(sendth = null; I.hasNext(); sendth = null)
            {
                sendth = (SendThread)I.next();
                if(!sendth.IsRun)
                    break;
            }

            if(sendth != null)
            {
                sendth.address = address;
                sendth.Title = Title;
                sendth.Body = Body;
                sendth.attatchPath = attatchPath;
                sendth.IsRun = false;
                sendth.run();
            } else
            if(Threadpool.size() < 5)
            {
                sendth = new SendThread();
                sendth.address = address;
                sendth.Title = Title;
                sendth.Body = Body;
                sendth.attatchPath = attatchPath;
                sendth.IsRun = false;
                Threadpool.add(sendth);
                sendth.run();
            } else
            {
                log("Thread pool is full max 5");
            }
        }
        return 0;
    }

    public static void main(String args[])
    {
        MailerBean client = new MailerBean();
        String address[] = {
            "zouyx@lianchuang.com"
        };
        String attatchPath[] = {};
        String Title = "IP地址管理";
        String Body = "inetnum: 220.167.166.196 - 220.167.166.199";
        client.sendMail(address, Title, Body, attatchPath);
    }












}
