package com.linkage.litms.webtopo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.AttrConf;
import RemoteDB.AttributeData;
import RemoteDB.HostObjectManager;
import RemoteDB.ObjectConf;
import RemoteDB.TrapConf;

import com.linkage.litms.common.corba.WebCorbaInst;

public class HostManager
    extends DataManagerControl {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(HostManager.class);
  private static HostObjectManager manager = null;

  private String account = null;

  private String passwd = null;

  public HostManager() {
  }

  public HostManager(String account, String passwd) {
    logger.debug(account+","+passwd);
    this.account = account;
    this.passwd = passwd;
    if (manager == null) {

      try {
        if(WebCorbaInst.passwordString==null)
        {
          WebCorbaInst.passwordString = dbInstance.ConnectToDb(this.account, this.passwd);
        }

        manager = dbInstance.createHostObjectManager(WebCorbaInst.passwordString);
      }
      catch (Exception e) {
        e.printStackTrace();
        rebindManager();

      }
    }

  }

  private void rebindManager() {
    rebind();
    try {
      String passwordString = dbInstance.ConnectToDb(this.account, this.passwd);
      logger.debug(passwordString);
      manager = dbInstance.createHostObjectManager(passwordString);
    }
    catch (Exception e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }



  /**
   * 设置采样属性
   * @param dxbh:对象编号
   * @return
   */
  public int SetObjectAttrList(int dxbh,AttrConf[] attrconflist)
  {
    int flag=-1;
    try
    {
      flag=manager.SetObjectAttrList(dxbh,attrconflist);
    }
    catch(Exception e)
    {
      rebind();
      flag=manager.SetObjectAttrList(dxbh,attrconflist);
    }
    return flag;
  }

  /**
   * 采集最新的属性数据 slbh=-1:采集所有实例数据
   * @param dxbh:对象编号
   * @return
   */
  public ArrayList GetRealAttributeData(String dxbh)
  {
    AttributeData[] attr;
    Map map = null;
    ArrayList list = new ArrayList();
    try
    {
        attr = manager.GetRealAttributeData(Integer.parseInt(dxbh), -1, -1);
        for (int i = 0; i < attr.length; i++)
        {
            map = new HashMap();
            map.put("dxbh", String.valueOf(attr[i].dxbh));
            map.put("lxsj", String.valueOf(attr[i].lxsj));
            map.put("cysj", String.valueOf(attr[i].cysj));
            map.put("sxbh", String.valueOf(attr[i].sxbh));
            map.put("slbh", String.valueOf(attr[i].slbh));
            map.put("sxlx", String.valueOf(attr[i].sxlx));
            map.put("slmc", String.valueOf(attr[i].slmc));
            map.put("csz", String.valueOf(attr[i].csz));
            list.add(map);
        }
    }
    catch(Exception e)
    {
      rebind();
      attr = manager.GetRealAttributeData(Integer.parseInt(dxbh), -1, -1);
      for (int i = 0; i < attr.length; i++)
      {
          map.put("dxbh", String.valueOf(attr[i].dxbh));
          map.put("lxsj", String.valueOf(attr[i].lxsj));
          map.put("cysj", String.valueOf(attr[i].cysj));
          map.put("sxbh", String.valueOf(attr[i].sxbh));
          map.put("slbh", String.valueOf(attr[i].slbh));
          map.put("sxlx", String.valueOf(attr[i].sxlx));
          map.put("slmc", String.valueOf(attr[i].slmc));
          map.put("csz", String.valueOf(attr[i].csz));
          list.add(map);
      }
    }
    return list;
  }

  public int DelObjects(String ids)
  {
    int flag=-1;
    String[] dxbhs=ids.split(",");
    for(int i=0;i<dxbhs.length;i++)
    {
      flag=DeleteObjectConf(Integer.parseInt(dxbhs[i]));
      if(flag==-1)
      {
        break;
      }
    }
    return flag;
  }

  /**
   * 设置告警的属性
   * @param dxbh:对象编号
   * @param trapList:告警对象列表
   * @return
   */
  public int SetObjectTrapList(int dxbh,TrapConf[] trapList)
  {
    int flag=-1;
    try
    {
      flag=manager.SetObjectTrapList(dxbh,trapList);
    }
    catch(Exception e)
    {
      rebind();
      flag=manager.SetObjectTrapList(dxbh,trapList);
    }
    return flag;
  }

  public int AddObjectConf(ObjectConf S_var) {
    int flag = -1;
    try {
      flag = manager.AddObjectConf(S_var);
      //logger.debug(flag);
    }
    catch (Exception e) {
      e.printStackTrace();
      rebind();
      flag = manager.AddObjectConf(S_var);
    }
    return flag;
  }

  public int EditObjectConf(int dxbh, ObjectConf S_var) {
    int flag = -1;
    try {
      flag = manager.EditObjectConf(dxbh, S_var);
      //logger.debug(flag);
    }
    catch (Exception e) {
      rebind();
      flag = manager.EditObjectConf(dxbh, S_var);
    }
    return flag;
  }

  public int DeleteObjectConf(int dxbh) {
    int flag = -1;
    try {
      flag = manager.DeleteObjectConf(dxbh);
    }
    catch (Exception e) {
      rebind();
      flag = manager.DeleteObjectConf(dxbh);
    }
    return flag;
  }

  public AttrConf[] GetObjectAttrList(int dxbh) {
    AttrConf[] retAttr = null;
    try {
      retAttr = manager.GetObjectAttrList(dxbh);
    }
    catch (Exception e) {
      rebind();
      retAttr = manager.GetObjectAttrList(dxbh);
    }
    return retAttr;

  }




}