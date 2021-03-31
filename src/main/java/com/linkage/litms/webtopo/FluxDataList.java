package com.linkage.litms.webtopo;

import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.StringHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.DB;
import RemoteDB.DataManager;
import RemoteDB.FluxData;
import RemoteDB.Link;
import RemoteDB.SQLException;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.corba.CorbaInstFactory;
import com.linkage.litms.common.corba.WebCorbaInst;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

// Referenced classes of package com.linkage.litms.webtopo:
//            DataManagerControl, DevicePortInfo

public class FluxDataList {
	
	/** log */
	private static Logger logger = LoggerFactory.getLogger(FluxDataList.class);
	public FluxDataList() {
		strInterfaceDevicePort = "select ifindex,ifdescr,ifnamedefined from flux_interfacedeviceport where device_id =? and gatherflag=1";
		
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


	public HashMap getInterfaceDevicePort(String device_id) {
		HashMap portMap = new HashMap();
		PrepareSQL pSQL = null;
		pSQL = new PrepareSQL(strInterfaceDevicePort);
		pSQL.setStringExt(1, device_id, true);
		Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
		Map map = cursor.getNext();
		int ifindex = 0;
		String ifdescr = null;
		String ifnamedefined = null;
		for (; map != null; map = cursor.getNext()) {
			ifdescr = (String) map.get("ifdescr");
			ifnamedefined = (String) map.get("ifnamedefined");
			portMap.put(map.get("ifindex"), new DevicePortInfo(ifindex,
					ifdescr, ifnamedefined));
		}

		return portMap;
	}

	public FluxData[] getDeviceFlux(String device_id, String account,
			String passwd) {
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
			//logger.debug(passwordString);
			manager = dbInstance.createDataManager(WebCorbaInst.passwordString);
		} catch (Exception e) {
			 rebind();
			 try {
                                if(WebCorbaInst.passwordString==null)
                                {
                                  WebCorbaInst.passwordString = dbInstance.ConnectToDb(account, passwd);
                                }
				//logger.debug(passwordString);
				manager = dbInstance.createDataManager(WebCorbaInst.passwordString);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		}
		if (manager == null) {
			logger.warn("无法获取设备实例");
			return null;
		}
		StringHolder collecttime = new StringHolder();
		logger.debug("开始获取设备" + device_id + "的流量");

		FluxData datas[] = manager.getPortData(device_id, collecttime);
		logger.debug("获得采集时间为" + collecttime.value );
		if (datas != null) {
			//logger.debug(datas.length);
		}
		return datas;
	}
	
	/**
	 * VPN的获取流量信息
	 * @param device_id
	 * @param account
	 * @param passwd
	 * @return
	 */
	public FluxData[] getDeviceFluxVpn(String device_id, String account,String passwd) 
	{
		DataManager manager = null;
		VpnScheduler  scheduler = new VpnScheduler();
		manager = scheduler.getDataManager(account,passwd);		
		if (manager == null) {
			logger.warn("无法获取设备实例");
			return null;
		}
		StringHolder collecttime = new StringHolder();
		logger.debug("开始获取设备" + device_id + "的流量");

		FluxData datas[] = manager.getPortData(device_id, collecttime);
		logger.debug("获得采集时间为" + collecttime.value );
		if (datas != null) {
			//logger.debug(datas.length);
		}
		return datas;
	}
	
	
	
	public Link[] getLinks(String linkid,String account,
			String passwd) {
		DataManager manager = null;
		try {
               if(WebCorbaInst.passwordString==null)
               {
            	  logger.debug("here....");
            	  WebCorbaInst.passwordString = dbInstance.ConnectToDb(account, passwd);
               }
			manager = dbInstance.createDataManager(WebCorbaInst.passwordString);
		} catch (Exception e) { 
			 rebind();
			 try {
                    if(WebCorbaInst.passwordString==null)
                    {
                      WebCorbaInst.passwordString = dbInstance.ConnectToDb(account, passwd);
                    }
				//logger.debug(passwordString);
				manager = dbInstance.createDataManager(WebCorbaInst.passwordString);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
		}
		if (manager == null) {
			logger.warn("无法获取设备实例");
			return null;
		}
        if(linkid != null && linkid.substring(0,2).compareTo("4/") == 0)
            return manager.GetRealLinkListUserView(linkid);
		return manager.GetRealLinkList(linkid);
		
	}
	

	private String strInterfaceDevicePort;
}
