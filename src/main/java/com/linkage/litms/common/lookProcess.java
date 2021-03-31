/*
 * Created on 2004-7-2
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.linkage.litms.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Vector;

import com.linkage.commons.db.DBUtil;
import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import SysManager.Manager;
import SysManager.ManagerHelper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;

/**
 * @author yuht
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 * 
 * ---------------------------------------------------
 * auth: Alex(yanhj@lianchuang.com)
 * date: 20080521
 * desc: 修改OSS_PROCESS_LIST.
 */
public class lookProcess {
	private static Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);
    private String OSS_PROCESS_LIST = "select * from tab_oss_proc_conf order by gather_id,proc_level,proc_name";

    public lookProcess() {

    }

    /**
     * 获得所有进程信息
     * 
     * @return Cursor
     */
    public Cursor getAllProcess() {
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            OSS_PROCESS_LIST = "select gather_id, proc_name, java_proc_key, proc_desc, proc_level, reserved1, reserved2 from tab_oss_proc_conf order by gather_id,proc_level,proc_name";
        }
    	PrepareSQL psql = new PrepareSQL(OSS_PROCESS_LIST);
    	psql.getSQL();
        return DataSetBean.getCursor(OSS_PROCESS_LIST);
    }

    /**
     * 根据进程名称获得进程运行状态
     * 
     * @param pNames
     * @return Vector
     */
    public Vector getProcessStatus(String[] pNames) {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        String ls, path, cmdStr;
        Vector list = new Vector();
        for (int i = 0; i < pNames.length; i++) {
            path = LipossGlobals.getLipossHome() + File.separator + "WEB-INF" + File.separator + "classes" + File.separator;
            cmdStr = path + "findProcess.sh " + pNames[i];
            String[] cmd = { "sh", "-c", cmdStr };
            try {
                p = r.exec(cmd);
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String[] tmp = new String[2];
                tmp[0] = pNames[i];
                if ((ls = br.readLine()) != null) {
                    tmp[1] = ls;
                    while ((ls = br.readLine()) != null) {
                        tmp[1] += "," + ls;
                    }
                } else {
                    tmp[1] = "-1";
                }
                p.waitFor();
                br.close();
                br = null;
                list.add(tmp);
            } catch (Exception e) {
            	logger.error(e.getMessage());
            }
        }

        return list;
    }
    
    /**
     * 根据进程名称获得进程运行状态
     * 
     * @param pNames
     * @return Vector
     */
    public String[] getProcessStatusCorba(String[] pNames) {
    	String[] arr_tab = null;
		try {
			String[] args = null;
			ORB orb = ORB.init(args, null);

			String ior = "";
			String sql = "select ior from tab_ior where object_name='SysManager' and object_poa='SysManager_Poa'";
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			Map map = DataSetBean.getRecord(sql);
			if (null != map) {
				ior = (String) map.get("ior");
			}
			org.omg.CORBA.Object objRef = orb.string_to_object(ior);
			Manager manager = ManagerHelper.narrow(objRef);
			arr_tab = manager.ProcessManager(pNames);			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return arr_tab;
    }

    public void testProcessStatus() {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        String ls, path;
        path = "/opt/zhwgtest/tomcat.apache/adslsheet/WEB-INF/classes/my.sh";
        try {
            logger.debug("------------start----------------");
            p = r.exec(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((ls = br.readLine()) != null) {
                logger.debug(ls);
            }

            p.waitFor();
            br.close();
            br = null;
            logger.debug("------------end----------------");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static void main(String[] args) {
        String[] pNames = new String[2];
        pNames[0] = "SnmpConf";
        pNames[1] = "java";

        lookProcess lkProcess = new lookProcess();
        Vector list = lkProcess.getProcessStatus(pNames);

        String[] tmp = new String[2];
        for (int i = 0; i < list.size(); i++) {
            tmp = (String[]) list.get(i);
            logger.debug(i + ": " + tmp[0] + " process id is " + tmp[1]);
        }
    }
}
