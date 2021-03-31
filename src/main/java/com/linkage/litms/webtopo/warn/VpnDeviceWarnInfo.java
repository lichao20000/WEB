package com.linkage.litms.webtopo.warn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.AlarmEvent;
import RemoteDB.AlarmNum;
import RemoteDB.GatherIDEvent;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.WebTopoCorbaInst;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.webtopo.VpnScheduler;

/**
 * 关于客户端告警操作的类
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class VpnDeviceWarnInfo {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(VpnDeviceWarnInfo.class);
    // 存放缓存中告警的列表
    private BaseWarnInfo bwi = null;

    private int warn_level = 4;

    private int warn_num = 2000;

    private int warn_view_num = 200;

    private VpnScheduler scheduler = null;

    private String area_id = null;

    private List gatherList = null;

    // 是否是新告警
    private boolean isNewWarn = false;

    // Add by Hemc 2006-12-13 新告警级别
    private short newWarnLevel = -1;

    // 存放设备类型编号和设备类型之间的对应关系
    public static HashMap type_name_map = null;

    public static HashMap status_name_map = null;

    // 临时的变量
    private ArrayList tempList = null;

    private HashMap tempMap = null;

    private Integer tempLevel = null;

    private GatherIDEvent[] temp_arr_id = null;

    private long tempUpdateTime = 0;
    
    private HttpSession session = null;
    
    //存放AlarmNum的map
    HashMap alarmNumMap = new HashMap();

    public VpnDeviceWarnInfo() {

        if (LipossGlobals.getLipossProperty("webtopo.WARN_ALL_NUM") != null) {
            warn_num = Integer.parseInt(LipossGlobals
                    .getLipossProperty("webtopo.WARN_ALL_NUM"));
        }

        if (LipossGlobals.getLipossProperty("webtopo.WARN_VIEW_LEVEL") != null) {
            warn_level = Integer.parseInt(LipossGlobals
                    .getLipossProperty("webtopo.WARN_VIEW_LEVEL"));
        }

        if (LipossGlobals.getLipossProperty("webtopo.WARN_VIEW_NUM") != null) {
            warn_view_num = Integer.parseInt(LipossGlobals
                    .getLipossProperty("webtopo.WARN_VIEW_NUM"));
        }

        if (type_name_map == null) {
            type_name_map = new HashMap();
            String mysql = "select serial,device_name from tab_devicetype_info";
            PrepareSQL psql = new PrepareSQL(mysql);
    		psql.getSQL();
            Cursor cursor = DataSetBean.getCursor(mysql);
            // logger.debug("------------------------");
            Map map = cursor.getNext();
            while (map != null) {
                // logger.debug(map.get("serial")+","+map.get("device_name"));
                type_name_map.put((String) map.get("serial"), (String) map
                        .get("device_name"));
                map = cursor.getNext();
            }
        }

        if (status_name_map == null) {
            status_name_map = new HashMap();
            status_name_map.put("1", "原始告警");
            status_name_map.put("2", "引擎处理");
            status_name_map.put("3", "已显示");
            status_name_map.put("4", "已确认");
            status_name_map.put("5", "已处理");
        }

        scheduler = new VpnScheduler();
    }

    public void setWarnPara(String area_id, List gatherList) {
        this.area_id = area_id;
        this.gatherList = gatherList;
    }

    public void setOnlyWarnInfo(HttpSession session) {
        this.session = session;
        bwi = (BaseWarnInfo) session.getAttribute("webtopo_warn");
    }

    public void setDeviceWarnInfo(HttpSession session) {
        this.session = session;
        synchronized (WebTopoCorbaInst.isReBind) {
            // 如果已经重新绑定了
            if (WebTopoCorbaInst.isReBind.booleanValue()) {
                logger.debug("将客户端的缓存session清空!!");
                session.setAttribute("webtopo_warn", null);
                WebTopoCorbaInst.isReBind = new Boolean("false");
            }
        }

        // 如果为空的话，则获取所有的新告警
        if (session.getAttribute("webtopo_warn") == null) {
            if (scheduler == null) {
                return;
            }
            if (gatherList == null || gatherList.size() == 0) {
                return;
            }
            // 从ＭＣ那边获取所有告警的列表，逐一加入
            // logger.debug("获取区域编码"+area_id+"的告警");
            temp_arr_id = new GatherIDEvent[gatherList.size()];
            for (int i = 0; i < gatherList.size(); i++) {
                temp_arr_id[i] = new GatherIDEvent();
                temp_arr_id[i].gather_id = (String) gatherList.get(i);
                temp_arr_id[i].max_event_id = "0";
            }

            logger.debug("area_id is:" + area_id);
            for (int i = 0; i < temp_arr_id.length; i++) {
                logger.debug("---------------------------");
                logger.debug(temp_arr_id[i].gather_id);
                logger.debug(temp_arr_id[i].max_event_id);
            }

            AlarmEvent[] tempAlarmList = scheduler.getAllAlarm(area_id,
                    temp_arr_id);
            logger.debug("getAllAlarm:获取到所有的告警的数量是:"
                    + tempAlarmList.length);
            // 设置临时变量
            tempList = new ArrayList();
            tempMap = new HashMap();
            tempLevel = new Integer(5);
            int alarmnum = tempAlarmList.length;
            for (int i = 0; i < alarmnum; i++) {
                addAlarm(tempAlarmList[i]);
            }

            if (alarmnum > 0) {
                tempUpdateTime = (new Date()).getTime() / 1000;
            }

            bwi = new BaseWarnInfo();
            isNewWarn = false;
        }
        // 如果不为空的话，则获取新的告警
        else {
            bwi = (BaseWarnInfo) session.getAttribute("webtopo_warn");
            tempMap = bwi.getWarnMap();
            tempList = bwi.getLevelList();
            tempLevel = bwi.getMinLevel();
            temp_arr_id = bwi.getGatherArrays();
            tempUpdateTime = bwi.getUpdateTime();
            // logger.debug("获取区域编码"+area_id+"的告警");
            // 针对采集点进行处理一遍，如果在原先的采集点编号中没有的话，则需要添加，如果有的话则引用
            GatherIDEvent[] temp_gather = new GatherIDEvent[gatherList.size()];
            for (int i = 0; i < gatherList.size(); i++) {
                boolean flag = true;
                for (int j = 0; j < temp_arr_id.length; j++) {
                    // 如果有的话
                    if (((String) gatherList.get(i))
                            .compareTo(temp_arr_id[j].gather_id) == 0) {
                        temp_gather[i] = temp_arr_id[j];
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    temp_gather[i] = new GatherIDEvent();
                    temp_gather[i].gather_id = (String) gatherList.get(i);
                    temp_gather[i].max_event_id = "0";
                }
            }
            // 进行赋值
            temp_arr_id = temp_gather;
            /*
             * logger.debug("area_id is:"+area_id); for (int i = 0; i <
             * temp_arr_id.length; i++) {
             * logger.debug("---------------------------");
             * logger.debug(temp_arr_id[i].gather_id);
             * logger.debug(temp_arr_id[i].max_event_id); }
             */
            AlarmEvent[] tempAlarmList = scheduler.getNewAlarm(area_id, 0,temp_arr_id);
            int alarmnum = tempAlarmList.length;
            logger.debug("getNewAlarm:获取到所有的新告警的数量是:" + alarmnum);
            short tempNewLevel = 0;
            for (int i = 0; i < alarmnum; i++) {
                if (addAlarm(tempAlarmList[i])) {
                    tempNewLevel = tempAlarmList[i].m_Severity;
                    if (newWarnLevel < tempNewLevel)
                        newWarnLevel = tempNewLevel;
                }
            }
            logger.debug("新告警最高级别为：" + getNewWarnLevel());
            if (alarmnum > 0) {
                tempUpdateTime = (new Date()).getTime() / 1000;
            }
            isNewWarn = true;
        }
        // logger.debug("before tempList size is:"+tempList.size());
        // 将告警的等级稍微排个小序，有高到低进行排序
        int[] tempi = new int[tempList.size()];

        for (int i = 0; i < tempList.size(); i++) {
            tempi[i] = ((Integer) tempList.get(i)).intValue();
        }

        Arrays.sort(tempi);
        tempList.clear();
        for (int i = (tempi.length - 1); i >= 0; i--) {
            // logger.debug(tempi[i]);
            tempList.add(new Integer(tempi[i]));
        }
        // logger.debug("after tempList size is:"+tempList.size());

        // 打印出这次获取告警完之后的采集点所对应的最大id
        /*
         * for (int i = 0; i < temp_arr_id.length; i++) {
         * logger.debug("+++++++++++++++++++++++++++++");
         * logger.debug(temp_arr_id[i].gather_id);
         * logger.debug(temp_arr_id[i].max_event_id); }
         */

        // 所有的告警处理结束后，进行赋值
        bwi.setLevel_warn_map(tempMap);
        bwi.setLevelList(tempList);
        bwi.setMinLevel(tempLevel);
        bwi.setGatherArrays(temp_arr_id);
        bwi.setUpdateTime(tempUpdateTime);
        session.setAttribute("webtopo_warn", bwi);
    }

    private int getAllAlarmSize() {
        int size = 0;
        ArrayList temp = null;
        for (int i = 0; i < tempList.size(); i++) {
            temp = (ArrayList) tempMap.get((Integer) tempList.get(i));
            if (temp != null) {
                size += temp.size();
            }
        }
        return size;

    }

    /**
     * 将经过过滤后的告警加入告警列表进行处理
     * 
     * @param e
     */
    private void addNewAlarm(AlarmEvent e) {

        Integer severity = new Integer(e.m_Severity);
        ArrayList temp2 = null;
        if (tempList.indexOf(severity) == -1) {
            // logger.debug("加入告警等级"+severity);
            tempList.add(severity);
            temp2 = new ArrayList();
            temp2.add(e);
            tempMap.put(severity, temp2);
        } else {
            if (tempMap.get(severity) != null) {
                temp2 = (ArrayList) tempMap.get(severity);
                temp2.add(e);
            } else {
                temp2 = new ArrayList();
                temp2.add(e);
                tempMap.put(severity, temp2);
            }
        }

        // 如果现有的告警等级比当前最低的告警等级还低，则将该告警的级别赋予最低告警级别
        if (severity.intValue() < tempLevel.intValue()) {
            tempLevel = severity;
        }

    }
   /*
    * 设置告警数据对象AlarmNum的值,并保存在map中.
    * 
    * @return 返回AlarmNum结构数组
    */
   public void setAllAlarmNum(AlarmEvent e) {
       try {
           // 133521@5/dev/1337
           String tmpID = e.m_AlarmId;
           // 安全级别 0 1 2 3 4 5
           int serverity = e.m_Severity;
           int index = tmpID.indexOf("@");
           String key = null;
           AlarmNum alarmNum = null;
           if (index != -1) {
               tmpID = tmpID.substring(index + 1);
           }
           // 以tmpID+$+serverity
           key = tmpID + "$" + serverity;
           // 如果map存在AlarmNum,则直接对AlarmNum的对象的num进行++操作
           if (alarmNumMap.containsKey(key)) {
               alarmNum = (AlarmNum) alarmNumMap.get(key);
               /*logger.debug("@" + tmpID + "," + alarmNum.number + ","
                       + alarmNum.level);*/
               alarmNum.number++;
           } else {
               alarmNum = new AlarmNum();
               alarmNum.id = tmpID;
               alarmNum.level = serverity;
               alarmNum.number = 1;
               alarmNumMap.put(key, alarmNum);
           }
       } catch (Exception ex) {
           logger.warn(ex.getMessage());
       }
   }

   /**
    * 根据alarmNumMap中存放的数据,转换成AlarmNum数组
    * 
    * @return 返回转换后AlarmNum数组
    */
   public AlarmNum[] getAlarmNum() {
       logger.debug("size:" + this.alarmNumMap.size());
       AlarmNum[] arr = new AlarmNum[this.alarmNumMap.size()];
       java.util.Iterator keyValuePairs = alarmNumMap.entrySet().iterator();
       for (int j = 0; j < alarmNumMap.size(); j++) {
           Map.Entry entry = (Map.Entry) keyValuePairs.next();
           arr[j] = (AlarmNum) entry.getValue();
       }
       return arr;
   }
   
    /**
     * 将告警增加到告警源中
     * 
     * @param e
     */
    private boolean addAlarm(AlarmEvent e) {
        // 将告警的最大ｉｄ放入列表中
        String gather_id = e.m_GatherID;
        String alarmid = e.m_Number;
        for (int i = 0; i < temp_arr_id.length; i++) {
            if (temp_arr_id[i].gather_id.compareTo(gather_id) == 0) {
                if (Long.parseLong(alarmid) > Long
                        .parseLong(temp_arr_id[i].max_event_id)) {
                    temp_arr_id[i].max_event_id = alarmid;
                }
                break;
            }
        }
        // 判断如果当时告警的长度尚小于指定的长度，则直接加入即可
        if (getAllAlarmSize() < warn_num) {
            addNewAlarm(e);
        }
        // 如果指定的长度已经超出了指定的长度，则需要删除相关的告警后再进行操作
        /**
         * 删除告警的规则如下： 对于新增的告警，判断在告警列表中是否存在比自己低(包括自己同级别的)级别的告警；
         * 如果存在的话，则找到最低级别告警中时间最长的告警进行删除； 如果不存在，则将自己扔弃掉，保留原来的告警
         */
        else {
            short severity = e.m_Severity;
            // 告警列表中是否存在比自己低,找出告警最老的告警，记录序列号，进行删除
            if (tempLevel.intValue() <= severity) {
                ArrayList list = (ArrayList) tempMap.get(tempLevel);
                int del_temp = -1;
                long del_date = 0;
                AlarmEvent temp_e = null;
                for (int i = 0; i < list.size(); i++) {
                    temp_e = (AlarmEvent) list.get(i);
                    if (i == 0) {
                        del_date = temp_e.m_CreateTime;
                        del_temp = 0;
                    } else {
                        if (del_date > temp_e.m_CreateTime) {
                            del_date = temp_e.m_CreateTime;
                            del_temp = i;
                        }
                    }
                }
                // logger.debug("从列表"+tempLevel+"中删除告警序列号"+del_temp);
                list.remove(del_temp);
                // 删除完毕后，需要判断一下该列表是否有告警长度，如果为0的话，则需要负责更新最低级别的告警
                if (list.size() == 0) {
                    // 从hash表中删除该告警级别的告警和从list列表中删除该告警等级
                    tempMap.remove(tempLevel);
                    tempList.remove(tempList.indexOf(tempLevel));
                    // 从列表中获取最新的告警等级作为最低级别的告警等级
                    tempLevel = new Integer(5);
                    for (int j = 0; j < tempList.size(); j++) {
                        if (tempLevel.intValue() > ((Integer) tempList.get(j))
                                .intValue()) {
                            tempLevel = (Integer) tempList.get(j);
                        }
                    }
                }

                // 加入告警列表
                addNewAlarm(e);
            }
            // 如果在现有的系统中所在的告警都比现有的告警级别高，则该告警不处理，直接扔弃
            else {

            }
        }
        return true;
    }

    public boolean removeAlarmList(String[] alarmID, HttpSession session) {
        boolean flag = false;
        ArrayList comList = new ArrayList();
        for (int i = 0; i < alarmID.length; i++) {
            comList.add(alarmID[i]);
        }

        if (bwi != null) {
            tempList = bwi.getLevelList();
            tempMap = bwi.getWarnMap();
            tempLevel = bwi.getMinLevel();

            AlarmEvent e = null;
            //ArrayList List1 = new ArrayList();
            ArrayList list = null;
            Integer key = null;
            for (int j = 0; j < tempList.size(); j++) {
                //List1 = new ArrayList();
                key = (Integer) tempList.get(j);
                list = (ArrayList) tempMap.get(key);

                for (int i = 0; i < list.size(); i++) {
                    e = (AlarmEvent) list.get(i);
                    if (comList.indexOf(e.m_AlarmId) != -1) {
                        //List1.add(e);
                        //comList中有该条告警,则充list中移除.
                        list.remove(i);
                        logger.debug("成功删除告警:" + e.m_AlarmId);
                        removeAlarmObject(e);
                        flag = true;
                    }
                }
                //如果告警级别为key的告警list被全部移除,则充tempMap中删除key
                if(list.size() == 0){
                    tempMap.remove(key);
                    tempList.remove(key);
                }
                /*
                if (List1.size() == 0) {
                    tempList.remove(key);
                    tempMap.remove(key);
                } else {
                    tempMap.put(key, List1);
                }
                */
            }

            for (int j = 0; j < tempList.size(); j++) {
                if (((Integer) tempList.get(j)).intValue() < tempLevel
                        .intValue()) {
                    tempLevel = (Integer) tempList.get(j);
                }
            }

            // 所有的告警处理结束后，进行赋值
            bwi.setLevel_warn_map(tempMap);
            bwi.setLevelList(tempList);
            bwi.setMinLevel(tempLevel);
            bwi.setGatherArrays(bwi.getGatherArrays());
            session.setAttribute("webtopo_warn", bwi);
        }
        return flag;
    }

    /**
     * session.setAttribute("device_warn", retList1);中删除告警
     * @param e 告警对象
     * @param session
     */
    public void removeAlarmObject(AlarmEvent e){
        if(session != null && session.getAttribute("device_warn") != null){
            ArrayList listWarn = (ArrayList)session.getAttribute("device_warn");
            if(listWarn != null){
                listWarn.remove(e);
                session.setAttribute("device_warn", listWarn);
            }
        }
    }
    public long getUpdateTime() {
        if (bwi != null) {
            return bwi.getUpdateTime();
        }

        return 0;

    }

    /**
     * 返回所用ｗｅｂ缓存列表中的告警
     * 
     * @return
     */
    public ArrayList getALLAlarmList() {
        ArrayList retList = new ArrayList();
        if (bwi != null) {
            // logger.debug("testtt"+bwi.getLevelList().size());
            for (int i = 0; i < bwi.getLevelList().size(); i++) {
                // logger.debug("test"+bwi.getLevelList().get(i));
                retList.addAll((ArrayList) bwi.getWarnMap().get(
                        (Integer) bwi.getLevelList().get(i)));
            }

        } else {
            logger.debug("bwi is not in session!!!");
        }
        return retList;
    }

    /**
     * 返回配置文件中配置要显示的告警，在拓扑图的下面的图中显示
     * 
     * @return
     */
    public ArrayList getConfigAlarmList(String updateTime) {
        // logger.debug("系统配置的显示告警数目是:"+warn_view_num+";配置要显示的最低告警级别是:"+warn_level);
        ArrayList retList1 = new ArrayList();

        if (bwi != null) {
            logger.debug("getConfigAlarmList:" + updateTime + "==" + bwi.getUpdateTime());
            if (Long.parseLong(updateTime) == bwi.getUpdateTime()) {
                return retList1;
            }

            int tempj = 0;
            ArrayList tempList2 = null;
            for (int i = 0; i < bwi.getLevelList().size(); i++) {
                // 只有告警等级大于配置文件中告警等级的告警才显示在上面
                logger.debug("告警级别:" + ((Integer) bwi.getLevelList().get(i)).intValue() + "配置告警级别:" + warn_level);
                if (((Integer) bwi.getLevelList().get(i)).intValue() >= warn_level) {
                    tempList2 = (ArrayList) bwi.getWarnMap().get(
                            (Integer) bwi.getLevelList().get(i));
                    if (tempList2 != null) {
                        // logger.debug("在"+bwi.getLevelList().get(i)+"级别告警中，有告警:"+tempList2.size());
                        for (int j = 0; j < tempList2.size(); j++) {
                            if (tempj >= warn_view_num) {
                                break;
                            }
                            setAllAlarmNum((AlarmEvent) tempList2.get(j));
                            retList1.add(tempList2.get(j));
                            tempj++;
                        }
                    }
                }
            }
            // logger.debug("获取到的能显示的告警数量:"+tempj);
        }
//      将retList1保存在session中,便于getAlarmListByDeviceID方法从list中获取设备告警
        session.setAttribute("device_warn", retList1);
        return retList1;
    }

    public String getMaxWarnLevel() {
        String maxLevel = null;
        if (bwi != null) {
            int maxid = -1;
            for (int i = 0; i < bwi.getLevelList().size(); i++) {
                if (((Integer) bwi.getLevelList().get(i)).intValue() > maxid) {
                    maxid = ((Integer) bwi.getLevelList().get(i)).intValue();
                }
            }

            if (maxid > -1) {
                maxLevel = String.valueOf(maxid);
            }

        }

        return maxLevel;

    }

    /**
     * 根据设备ｉｄ和告警等级来获取当前选中设备的告警信息
     * 
     * @param device_id
     * @param level
     * @return
     */
    public ArrayList getAlarmByDeviceID(String device_id, String level) {
        // logger.debug("获取设备:"+device_id+","+level+"的告警");
        ArrayList retList1 = new ArrayList();
        if (bwi != null) {
            ArrayList levelList = (ArrayList) bwi.getWarnMap().get(
                    new Integer(Integer.parseInt(level)));
            if (levelList != null) {
                AlarmEvent e = null;
                for (int i = 0; i < levelList.size(); i++) {
                    e = (AlarmEvent) levelList.get(i);
                    // logger.debug(e.m_DeviceCoding);
                    if (e.m_DeviceCoding.compareTo(device_id) == 0) {
                        retList1.add(e);
                    }
                }

            } else {
                logger.debug("没有获取到此告警级别的告警列表！");
            }
        }
        return retList1;
    }
   /**
    * 根据设备id和告警级别得到设备告警列表(getConfigAlarmList函数得到list时,放在session中,此处取出)
    * Add By HMC
    * @param device_id
    *            设备ID
    * @param level
    *            告警级别 (修改2006-12-12 By HMC 将列表中某设备告警全部取出)
    * @return
    */
   public ArrayList getAlarmListByDeviceID(String device_id, String level) {
       ArrayList listWarn = null;
       //short short_Level = 0;
       //if(!level.equals(""))
           //short_Level = Short.parseShort(level);
       if (session.getAttribute("device_warn") == null) {
           return null;
       } else {
           listWarn = (ArrayList) session.getAttribute("device_warn");
       }
       device_id = "5/dev/" + device_id;
       logger.debug("获取设备:" + device_id + "," + level + "的告警");
       ArrayList retList1 = new ArrayList();
       AlarmEvent e = null;
       String tmpID = null;
       int index = -1;
       for (int i = 0; i < listWarn.size(); i++) {
           e = (AlarmEvent) listWarn.get(i);
           tmpID = e.m_AlarmId;
           index = tmpID.indexOf("@");
           if (index != -1) {
               tmpID = tmpID.substring(index + 1);
           }
           //将该设备的所有告警都取出
           // && short_Level == e.m_Severity
           if (tmpID.equals(device_id)) {
               retList1.add(e);
           }
       }

       logger.debug("获取到设备告警列表size:" + retList1.size());
       return retList1;
   }
    /**
     * 获取新告警最高级别
     * 
     * @return short
     */
    public String getNewWarnLevel() {
        return Short.toString(newWarnLevel);
    }
     
    /**
     * 得到新告警属性。
     * 
     * @return
     */
    public boolean isNewWarn() {
        return isNewWarn;
    }
    public VpnScheduler getVpnScheduler(){
        return this.scheduler;
    }
}