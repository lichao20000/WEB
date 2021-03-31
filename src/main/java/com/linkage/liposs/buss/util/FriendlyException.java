package com.linkage.liposs.buss.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: 从“全球眼视频监控系统”移植而来</p>
 *
 * <b>Usage:</b>
 * <p>in catch block, add code as such:</p>
 * <code>
 *             FriendlyException fe = new FriendlyException(ex);
 *             log.error(fe.getFriendlyLocateInfo());
 * </code>
 *
 *<p> or: </p>
 * <code>
 *     FriendlyException.printFriendlyLocateInfo(ex);
 * </code>
 *
 * <p> or: </p>
 * <code>
 *     FriendlyException.printFriendlyLocateInfo(ex,"com.linkage.globalview.puserver");
 * </code>
 *
 */

public class FriendlyException extends Throwable {
    private String localPackageName = "com.linkage";
    Throwable sourceObj = null;
    private static Logger log = LoggerFactory.getLogger(FriendlyException.class);

    public FriendlyException() {
        super();
    }

    public FriendlyException(String message) {
        super(message);
    }

    public FriendlyException(String message, Throwable cause) {
        super(message, cause);
        sourceObj = cause;
    }

    public FriendlyException(Throwable cause) {
        super(cause);
        sourceObj = cause;
    }

    public FriendlyException(Throwable cause, String packgeName) {
        super(cause);
        sourceObj = cause;
        this.setPackageName(packgeName);
    }

    public void setPackageName(String packgeName) {
        this.localPackageName = packgeName;
    }

    public String getFriendlyLocateInfo() {
        int printNum = 0;
        StringBuffer msgbuf = new StringBuffer("");
        msgbuf.append("【异常信息】" + this.getLocalizedMessage());
        msgbuf.append("\n");

        StackTraceElement[] st;
        if (sourceObj == null) {
            st = getStackTrace();
        } else {
            st = sourceObj.getStackTrace();
        }

        for (int i = 0; i < st.length; i++) {
            String fullClsName = st[i].getClassName();
            if (fullClsName.startsWith(this.localPackageName)) { //输出前1后2行定位信息
                if (i > 0) {
                    msgbuf.append(st[i - 1].toString());
                    msgbuf.append("\n");
                    printNum++;
                }
                msgbuf.append(st[i].toString());
                msgbuf.append("\n");
                printNum++;
                if (i < st.length - 1) {
                    msgbuf.append(st[i + 1].toString());
                    msgbuf.append("\n");
                    printNum++;
                    if (i < st.length - 2) {
                        msgbuf.append(st[i + 2].toString());
                        msgbuf.append("\n");
                        printNum++;
                    }
                }
                break;
            }
        }

        if (printNum == 0) {
            for (int i = 0; i < st.length; i++) {
                String s = st[i].toString();
                msgbuf.append("\t" + s);
                msgbuf.append("\n");
            }
        }

        return msgbuf.toString();

    }

    public static StackTraceElement getCurCodeLocation() {
        FriendlyException xx = new FriendlyException();
        return xx.getStackTrace()[1];
    }

    public static void printCurCodeLocation() {
        FriendlyException xx = new FriendlyException();
        StackTraceElement se = xx.getStackTrace()[1];
        String msg = "[Code Location]" + se.toString();
        log.error(msg);
    }

    public static void printFriendlyLocateInfo(Throwable ex) {
        FriendlyException fe = new FriendlyException(ex);
        log.error(fe.getFriendlyLocateInfo());
    }

    public static void printFriendlyLocateInfo(Throwable ex, String packgeName) {
        FriendlyException fe = new FriendlyException(ex, packgeName);
        log.error(fe.getFriendlyLocateInfo());
    }

}
