package com.linkage.litms.resource;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.QueryString;

public class UsernameByIP {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(UsernameByIP.class);
    private String url = "http://132.228.36.241:7001/ipquery";
    /** 错误信息 */
    private String errMsg = "";
    /** 错误编号 */
    private int errID = 0;
    private final boolean DEBUG = true;
    /** 当前连接数 */
    private static int SOCKET_NUMBER = 0;
    /** 最大连接数 */
    private int maxSocketNumber = 40;
    private Object o = new Object();
    private String realname = "";
    
    public UsernameByIP() {
        String sMaxSocket = LipossGlobals.getLipossProperty("socket.UserInst.maxSocket");
        if (sMaxSocket != null){
            try{
                this.maxSocketNumber = Integer.parseInt(sMaxSocket);
            }
            catch(Exception e){
            }
        }
    }
    
    public UsernameByIP(String host,int port) {
        this();
        this.url = "http://" + host + ":"+ port +"/ipquery";
    }
    
    public UsernameByIP(String host) {
        this(host,7001);
    }
    /**
     * 根据IP地址获取用户名
     * @param dynamicip IP地址
     * @return 用户名
     */
    public String getUsername(String dynamicip) {
        if (dynamicip == null)
            return "";

        String result = "";
        URLConnection uc = null;
        URL u = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:S'+00:00'");
            //当前时间
            Date dt = new Date();
            long lms = dt.getTime();
            //当前时间后15分钟
            dt = new Date(lms + 15 * 60 * 1000);
            //当前时间前15分钟
            Date dt2 = new Date(lms - 15 * 60 * 1000);
            synchronized (o) {
                SOCKET_NUMBER++;
            }
            
            if (SOCKET_NUMBER > maxSocketNumber) {
                errID = -2;
                errMsg = "服务器忙，请稍候再试!";
                return result;
            }
            u = new URL(url);
            QueryString query = new QueryString();
            String queryXML = "<?xml version=\"1.0\" encoding=\"gb2312\"?>"
                            + "<QueryCommand>"
                            + "<ID>1</ID>"
                            + "<IP>" + dynamicip + "</IP>"
                            + "<startTime>" + sdf.format(dt2) + "</startTime>"
                            + "<endTime>" + sdf.format(dt) + "</endTime>"
                            + "</QueryCommand>";
            query.add("inCommand", queryXML);
            
            uc = u.openConnection();
            uc.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream());
            //logger.debug("Query="+queryXML);
            out.write(query.getQuery());
            out.flush();
            
            InputStream in = uc.getInputStream();
            result = parseUsername(in);
            int pos = result.indexOf("|");
            if (pos != -1) {
                result = result.substring(0, pos);
            }
            
            out.close();
            out = null;
        } catch (MalformedURLException e) {
            errMsg = "查询Radius认证接口失败";
        } catch (IOException e) {
            errMsg = "查询Radius认证接口失败";
        } finally {
            try {
                uc = null;
                u = null;
            } catch (Exception e) {
                
            }
            synchronized (o) {
                SOCKET_NUMBER--;
                if (DEBUG)
                    logger.debug("current socket number in UsernameByIP.java: " + SOCKET_NUMBER);
            }
        }
        return result;
    }
    
    /**
     * 从输入流中提取用户名
     * @param in 输入流
     * @return 用户名
     */
    public String parseUsername(InputStream in) {
        String account = "";
        try {
            BufferedInputStream buffer = new BufferedInputStream(in);
            InputStreamReader r = new InputStreamReader(buffer);
            int c;
            StringBuffer sb = new StringBuffer(200);
            while ((c = r.read()) != -1) {
                sb.append((char) c);
            }
            r.close();
            r = null;
            String s = sb.toString();
            sb = null;
            logger.debug("获取账户串：" + s);
            int begin = s.indexOf("<account>");
            if (begin == -1) {
                errMsg = "解析账号出错";
                begin = s.indexOf("<errorID>");
                int end = s.indexOf("</errorID>");
                begin = begin + "<errorID>".length();
                String tmpID = s.substring(begin,end);
                if(tmpID.equals("1") || tmpID.equals("2") || tmpID.equals("3"))
                    errID = Integer.parseInt(tmpID);
            } else {
                int end = s.indexOf("</account>");
                begin = begin + "<account>".length();
                account = s.substring(begin, end);
                
                begin = s.indexOf("<userName>");
                end = s.indexOf("</userName>");
                begin = begin + "<userName>".length();
                realname = s.substring(begin,end);
            }
        } catch (IOException ioe) {
            errMsg = "解析账号出错";
        } finally {
        }
        
        return account;
    }
    
    public String getRealName(){
        return realname;
    }
    public String getErrMsg() {
        return errMsg;
    }
    
    public static void main(String[] args) {
        String s = "";
        String host = "202.102.13.112";
        int port = 8001;
        if(args.length==3){
            host = args[1];
            port = Integer.parseInt(args[2]);
        }
        UsernameByIP test = new UsernameByIP();
        s = test.getUsername(args[0]);
        if (!s.equals(""))
            logger.debug("username=" + s);
        else
            logger.warn(
                    "error: " + test.getErrMsg() + " errID: " + test.getErrID());
        
        System.exit(0);
    }
    /**
     * 获取错误编号
     * @return 错误编号
     */
    public int getErrID() {
        return errID;
    }
    
}
