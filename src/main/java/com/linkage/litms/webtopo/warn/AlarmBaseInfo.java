package com.linkage.litms.webtopo.warn;

import java.util.HashMap;

import com.linkage.module.gwms.dao.gw.EventLevelLefDAO;

public class AlarmBaseInfo {
  public AlarmBaseInfo() {
  }

  public static HashMap getWarnLevel(){
    EventLevelLefDAO eventLevelLefDAO=new EventLevelLefDAO();
    HashMap warnMap = (HashMap)eventLevelLefDAO.getWarnLevel();
//    map.put("0", "自动清除");
//    map.put("1", "正常日志");
//    map.put("2", "提示告警");
//    map.put("3", "一般告警");
//    map.put("4", "严重告警");
//    map.put("5", "紧急告警");
    return warnMap;

  }


}