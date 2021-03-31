package com.linkage.litms.webtopo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.DB;
import RemoteDB.DataManager;

import com.linkage.litms.common.corba.CorbaInstFactory;
import com.linkage.litms.common.corba.WebCorbaInst;

public class DataManagerControl {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(DataManagerControl.class);
	public DataManagerControl() {
		if (dbInstance == null)
			dbInstance = (DB) CorbaInstFactory.factory("db");
		if (dbInstance == null)
			rebind();
		else
			logger.debug(dbInstance.getClass().toString());
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

}