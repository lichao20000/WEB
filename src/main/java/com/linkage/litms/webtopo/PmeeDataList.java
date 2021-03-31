package com.linkage.litms.webtopo;

import java.util.Map;

import org.omg.CORBA.StringHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.DB;
import RemoteDB.DataManager;
import RemoteDB.PmeeData;
import RemoteDB.SQLException;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.corba.CorbaInstFactory;
import com.linkage.litms.common.corba.WebCorbaInst;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;


public class PmeeDataList {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(PmeeDataList.class);
	// private String

	public PmeeDataList() {
		strPm_expressionInfo = "select a.expressionid, a.name,a.descr from "
				+ "pm_expression a ,pm_map b where a.expressionid =b.expressionid "
				+ "and b.device_id=? and b.isok= 1";

		strPm_instanceID = "select id from pm_map_instance where expressionid = ? and device_id = ?";
	}

	public Cursor getPm_expressionInfo(String device_id) {
		PrepareSQL pSQL = null;
		pSQL = new PrepareSQL(strPm_expressionInfo);
		pSQL.setStringExt(1, device_id, true);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		

		return cursor;
	}

	public PmeeData[] getDevicePmee(String expressionid, String device_id,
			String account, String passwd) {
		DataManager manager = null;
		if (dbInstance == null)
			dbInstance = (DB) CorbaInstFactory.factory("db");
		if (dbInstance == null)
			rebind();
		else
			logger.debug(dbInstance.getClass().toString());
		try {
                        if(WebCorbaInst.passwordString==null)
                        {
                          WebCorbaInst.passwordString = dbInstance.ConnectToDb(account, passwd);
                        }

			manager = dbInstance.createDataManager(WebCorbaInst.passwordString);
		} catch (Exception e) {
                  rebind();
                   try {
                          String passwordString = dbInstance.ConnectToDb(account, passwd);
                          logger.debug(passwordString);
                          manager = dbInstance.createDataManager(passwordString);
                  } catch (SQLException e1) {
                          // TODO Auto-generated catch block
                          e1.printStackTrace();
                  }

			e.printStackTrace();
		}
		if (manager == null) {
			logger.debug("无法获取设备实例");
			return null;
		}
		StringHolder collecttime = new StringHolder();
		logger.debug("开始获取设备" + device_id + "的性能");
		PmeeData datas[] = manager.GetDataList(expressionid, device_id,
				collecttime);
		if (datas != null) {
			logger.debug(""+datas.length);
		}
		return datas;
	}
	
	
	

	public boolean rebind() {
               dbInstance=null;
		try {
			dbInstance = (DB) CorbaInstFactory.factory("db");

                        if(dbInstance==null)
                        {
                          dbInstance = (DB) CorbaInstFactory.factory("db");
                        }
			return dbInstance != null;
		} catch (Exception e) {
			return false;
		}
	}

	protected static DB dbInstance = null;

        public DataManager getDataManager(String account,String passwd)
        {
          DataManager manager = null;

          try {
            if(WebCorbaInst.passwordString==null)
            {
              WebCorbaInst.passwordString = dbInstance.ConnectToDb(account, passwd);
            }
            //logger.debug(passwordString);
            manager = dbInstance.createDataManager(WebCorbaInst.passwordString);
          }
          catch (Exception e) {
            rebind();
            try {
              if(WebCorbaInst.passwordString==null)
              {
                WebCorbaInst.passwordString = dbInstance.ConnectToDb(account, passwd);
              }
              //logger.debug(passwordString);
              manager = dbInstance.createDataManager(WebCorbaInst.passwordString);
            }
            catch (Exception e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }

            e.printStackTrace();
          }

          return manager;

        }

	
	public PmeeData[] getDevicePmeeVpn(String expressionid, String device_id,
			String account, String passwd) 
	{
		DataManager manager = null;
		VpnScheduler  scheduler = new VpnScheduler();
		manager = scheduler.getDataManager(account,passwd);	
		if (manager == null) {
			logger.debug("无法获取设备实例");
			return null;
		}
		StringHolder collecttime = new StringHolder();
		logger.debug("开始获取设备" + device_id + "的性能");
		PmeeData datas[] = manager.GetDataList(expressionid, device_id,
				collecttime);
		if (datas != null) {
			logger.debug(""+datas.length);
		}
		return datas;
	}

	public String[] getPM_instance_ID(String expressionID,String deviceID) {
		String[] idArray;
		PrepareSQL pSQL = new PrepareSQL(strPm_instanceID);
        pSQL.setStringExt(1, expressionID, false);
		pSQL.setStringExt(2, deviceID,true);
        
        Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
        int size = cursor.getRecordSize();
        Map fields = cursor.getNext();
        idArray = new String[size];
        int i=0;
        while(fields!=null)
        {
        	idArray[i] = (String)fields.get("id");
        	i++;
        	fields=cursor.getNext();
        }

		return idArray;
	}

	private String strPm_expressionInfo;
	private String strPm_instanceID;
}
