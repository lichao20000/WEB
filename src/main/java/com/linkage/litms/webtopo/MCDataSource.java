package com.linkage.litms.webtopo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.dataSource;

import com.linkage.litms.common.corba.WebCorbaInst;
import com.linkage.litms.common.util.Encoder;

public class MCDataSource extends DataManagerControl {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(MCDataSource.class);
  private static dataSource manager = null;

  private String account=null;

  private String passwd=null;

  public MCDataSource() {

  }




  public MCDataSource(String account, String passwd) {
    this.account=account;
    this.passwd=passwd;
    if(manager==null)
    {

      try {
        if(WebCorbaInst.passwordString==null)
        {
          WebCorbaInst.passwordString = dbInstance.ConnectToDb(this.account, this.passwd);
        }

        manager = dbInstance.createDataSourceServent(WebCorbaInst.passwordString);
      }
      catch (Exception e) {
        rebindManager();
        e.printStackTrace();
      }
    }

   }

   private void rebindManager()
   {
     rebind();
     try {
       String passwordString = dbInstance.ConnectToDb(this.account, this.passwd);
       logger.debug(passwordString);
       manager = dbInstance.createDataSourceServent(passwordString);
     }
     catch (Exception e1) {
       // TODO Auto-generated catch block
       e1.printStackTrace();
     }
   }


   /**
    * 通知后台删除告警
    * @param alarmList 告警列表
    * @param m_ObserverName 删除告警方的名称
    * @return
    * 失败返回-1
    * 成功返回1
    */

   public int RemoveAlarm(String[] alarmList,String m_ObserverName)
   {
     try {
       manager.RemoveAlarm(alarmList,Encoder.ChineseStringToAscii(m_ObserverName));
     }
     catch (Exception e) {
       try
       {
         rebindManager();
         manager.RemoveAlarm(alarmList,Encoder.ChineseStringToAscii(m_ObserverName));
       }
       catch(Exception ex)
       {
           return -1;
       }
     }

     return 1;
   }
   /**
    * 通知后台确认告警
    * @param alarmList 告警列表
    * @param m_ObserverName 告警方的名称
    * @return
    * 失败返回-1
    * 成功返回1
    */

   public int AckAlarm(String[] alarmList,String m_ObserverName)
   {
     try {
       manager.AckAlarm(alarmList,Encoder.ChineseStringToAscii(m_ObserverName));
     }
     catch (Exception e) {
       try
       {
         rebindManager();
         manager.AckAlarm(alarmList,Encoder.ChineseStringToAscii(m_ObserverName));
       }
       catch(Exception ex)
       {
           return -1;
       }
     }

     return 1;
   }
   /**
    * 通知后台确认告警(新增)
    * 
    * @param alarmList
    *                告警列表(new
    *                String[2]{"1111@1/dev/device_Coding;nanjing","2222@1/dev/device_Coding;nanjing"})
    * @author duangr 2007-11-15
    * @return 失败返回-1 成功返回1
    */
   public int AckAlarm(String[] alarmList) {
	try {
	    manager.AckAlarmOther(alarmList);
	} catch (Exception e) {
	    try {
		rebindManager();
		manager.AckAlarmOther(alarmList);
	    } catch (Exception ex) {
		return -1;
	    }
	}

	return 1;
   }
   /**
    * 通知后台删除告警(新增)
    * 
    * @param alarmList
    *                告警列表(new
    *                String[2]{"1111@1/dev/device_Coding;nanjing","2222@1/dev/device_Coding;nanjing"})
    * @author duangr 2007-11-15
    * @return 失败返回-1 成功返回1
    */
   public int RemoveAlarm(String[] alarmList) {
	try {
	    manager.RemoveAlarmByGather(alarmList);
	} catch (Exception e) {
	    try {
		rebindManager();
		manager.RemoveAlarmByGather(alarmList);
	    } catch (Exception ex) {
		return -1;
	    }
	}

	return 1;
   }
   /**
    * 取得序列号大于指定序列号的告警信息
    * 
    * @param serialno
    *                当前取告警前，获得的最大告警序列号
    * @param fetchCount
    *                需要获取告警的条数
    * @param maxSerialNo
    *                从MC获取告警后，得到的告警最大序列号
    * @param gatherId
    *                采集点
    * @param area_id
    *                域id
    * @return 返回数据
    */
   public RemoteDB.AlarmEvent[] getAlarmEventList(int serialno,
	    int fetchCount, org.omg.CORBA.IntHolder maxSerialHolder, String gatherId, String area_id) {
	try {
	    org.omg.CORBA.IntHolder count = new org.omg.CORBA.IntHolder();
	    count.value = fetchCount;
	    maxSerialHolder.value = 0;
	    RemoteDB.AlarmEvent[] result = manager.getAlarmEventList(serialno + 1,count, maxSerialHolder, gatherId, area_id);

	    return result;
	}catch (Exception e) {
	    try {
		rebindManager();
		 org.omg.CORBA.IntHolder count = new org.omg.CORBA.IntHolder();
		    count.value = fetchCount;
		    maxSerialHolder.value = 0;
		    RemoteDB.AlarmEvent[] result = manager.getAlarmEventList(serialno + 1,count, maxSerialHolder, gatherId, area_id);

		    return result;
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	return new RemoteDB.AlarmEvent[0];
   }
}
