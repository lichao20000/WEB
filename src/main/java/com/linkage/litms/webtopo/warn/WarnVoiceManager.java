package com.linkage.litms.webtopo.warn;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.FileViewer;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;

/**
 * voicetype 『loop：0 只播放一次 1 一直播放 相当于false和true』 warnvoice 告警声音文件名 warnlevel
 * 告警级别 inid 内部编号
 * 
 * @author
 * 
 */
public class WarnVoiceManager {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(WarnVoiceManager.class);
    private HttpServletRequest request = null;

    private String insertSQL = "insert into tab_warn_config(inid,warnlevel,warnvoice,voicetype,acc_oid) values(?,?,?,?,?)";

    private String updateSQL = "update tab_warn_config set warnvoice=?,voicetype=? where inid=?";

    private String deleteSQL = "delete from tab_warn_config where inid=?";

    private String getMaxIDSQL = "select nvl(max(inid),0)+1 as inid from tab_warn_config";

    private String mysql = "select count(1) as num from tab_warn_config where warnlevel=? and acc_oid=?";

    private String selSQL = "select inid,warnlevel,warnvoice,voicetype from tab_warn_config where acc_oid=?";

    PrepareSQL pSQL = null;

    public WarnVoiceManager() {
        if (pSQL == null) {
            pSQL = new PrepareSQL();
        }
    }

    public WarnVoiceManager(HttpServletRequest request) {
        this.request = request;
        if (pSQL == null) {
            pSQL = new PrepareSQL();
        }
    }

    public HashMap getWarnVoice() {
        HashMap retMap = new HashMap();
        Cursor cursor = getWarnVoiceCur();
        if (cursor != null) {
            Map map = cursor.getNext();
            while (map != null) {
                retMap.put(map.get("warnlevel"), map.get("warnvoice") + "|||"
                        + map.get("voicetype"));
                map = cursor.getNext();
            }
        }

        return retMap;

    }
    /**
     * getWarnVoice得到到Hashmap后解析数据
     * @param maxid 得到的告警级别
     * @return
     */
    public String[] getWarnVoiceNameAndType(String maxid){
        HashMap voiceMap = getWarnVoice();
        logger.debug("maxid: " + maxid + " " + voiceMap);
        String voice=(String)voiceMap.get(maxid);
        logger.debug("voice is:" + voice);
        String[] voices = null;
        if(voice!=null){
            voices = voice.split("\\|\\|\\|");
        }
        //voices = voices == null ? new String[]{"",""} : voices;
        return voices;
    }
        

    public FileViewer getSoundFile() {
        FileViewer f = null;
        if (request.getSession().getAttribute("WarnVoice_FileViewer") == null) {
            f = new FileViewer();
            String soundFile = LipossGlobals.getLipossHome();
            soundFile = soundFile + File.separator + "webtopo" + File.separator
                    + "sound";
            f.setPath(soundFile);
            f.refreshList();
            request.getSession().setAttribute("WarnVoice_FileViewer", f);
        } else {
            f = (FileViewer) request.getSession().getAttribute(
                    "WarnVoice_FileViewer");
        }
        f.refreshView();
        return f;
    }

    public Map getWarnLevel() {
        HashMap map = new HashMap();
        map.put("0", "清除告警");
        map.put("1", "事件告警");
        map.put("2", "警告告警");
        map.put("3", "次要告警");
        map.put("4", "主要告警");
        map.put("5", "严重告警");
        return map;

    }

    public Cursor getWarnVoiceCur() {
        UserRes userRes = (UserRes) this.request.getSession().getAttribute(
                "curUser");
        long acc_oid = userRes.getUser().getId();
        pSQL.setSQL(selSQL);
        pSQL.setLong(1, acc_oid);
        Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
        return cursor;
    }

    /**
     * 对用户的操作进行保存
     * 
     * @return -1:操作失败；-100:该用户在相同告警级别已经配置了记录；1:配置成功
     */
    public int WarnVoiceSave() {
        int flag = 1;
        String action = request.getParameter("action");
        String inid = request.getParameter("inid");
        if (action.compareTo("delete") == 0) {
            pSQL.setSQL(deleteSQL);
            pSQL.setStringExt(1, inid, false);
        } else {
            String warnlevel = request.getParameter("warnlevel");
            String warnvoice = request.getParameter("warnvoice");
            String voicetype = request.getParameter("voicetype");
            UserRes userRes = (UserRes) this.request.getSession().getAttribute(
                    "curUser");
            long acc_oid = userRes.getUser().getId();
            if (action.compareTo("edit") == 0) {
                pSQL.setSQL(updateSQL);
                pSQL.setStringExt(1, warnvoice, true);
                pSQL.setStringExt(2, voicetype, false);
                pSQL.setStringExt(3, inid, false);
            } else {
                // 首先判断该用户的该告警级别的是否判断
                if (isExit(warnlevel, acc_oid)) {
                    return -100;
                }
                //判断当前数据是sybase/oracle
               // if(DbUtil.getDbType().indexOf("sybase") != -1){
                
              //add by zhangcong@ 2011-06-21
                if(!LipossGlobals.isOracle())
                {
                    getMaxIDSQL = getMaxIDSQL.replaceAll("nvl","isnull");
                }

                // teledb
                if (DBUtil.GetDB() == Global.DB_MYSQL) {
                    getMaxIDSQL = "select ifnull(max(inid),0)+1 as inid from tab_warn_config";
                }

                com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(getMaxIDSQL);
                psql.getSQL();
                //}
                Map map = DataSetBean.getRecord(getMaxIDSQL);
                logger.debug(getMaxIDSQL);
                String maxid = "0";
                if (map != null) {
                    maxid = (String) map.get("inid");
                }
                pSQL.setSQL(insertSQL);
                pSQL.setStringExt(1, maxid, false);
                pSQL.setStringExt(2, warnlevel, false);
                pSQL.setStringExt(3, warnvoice, true);
                pSQL.setStringExt(4, voicetype, false);
                pSQL.setLong(5, acc_oid);

            }

        }
        flag = DataSetBean.executeUpdate(pSQL.getSQL());

        return flag;
    }

    /**
     * 判断同一个登录账号配置告警级别的记录是否存在
     * 
     * @param warnlevel
     * @param acc_oid
     * @return
     */
    public boolean isExit(String warnlevel, long acc_oid) {
        boolean flag = false;
        pSQL.setSQL(mysql);
        pSQL.setStringExt(1, warnlevel, false);
        pSQL.setLong(2, acc_oid);
        Map map = DataSetBean.getRecord(pSQL.getSQL());
        if (map != null && Integer.parseInt((String) map.get("num")) > 0) {
            flag = true;
        }
        return flag;
    }

}