package com.linkage.litms.webtopo.warn;

import java.util.ArrayList;
import java.util.HashMap;

import RemoteDB.GatherIDEvent;

public class BaseWarnInfo {
  //当前告警的最低级别的编号,默认为最高级别
  private Integer min_level = new Integer(5);

  //存放当前告警级别的列表
  private ArrayList levelList = new ArrayList();

  //存放每个级别告警所对应的告警hash表
  private HashMap level_warn_map = new HashMap();

  //存放每个采集点对应的告警的最大id
  private GatherIDEvent[] gather_arrays=null;

  //存放每个采集点对应的最新更新告警时间
  private long updateTime=0;

  public void setUpdateTime(long updateTime)
  {
    this.updateTime=updateTime;
  }

  public long getUpdateTime()
  {
    return updateTime;
  }

  public void setGatherArrays(GatherIDEvent[]  gather_arrays)
  {
    this.gather_arrays=gather_arrays;
  }

  public GatherIDEvent[] getGatherArrays()
  {
    return this.gather_arrays;
  }

  public BaseWarnInfo() {

  }

  public Integer getMinLevel()
  {
    return min_level;
  }

  public ArrayList getLevelList()
  {
    return levelList;
  }

  public HashMap getWarnMap()
  {
    return level_warn_map;
  }

  public void setMinLevel(Integer level)
  {
    this.min_level=level;
  }

  public void setLevelList(ArrayList list)
  {
    this.levelList=list;
  }

  public void setLevel_warn_map(HashMap map)
  {
    this.level_warn_map=map;
  }

}