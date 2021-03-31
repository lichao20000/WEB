/**
 * @(#)Tester.java 2006-2-9
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.liposs.aaa;

import java.util.ArrayList;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;

/**
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public class Tester {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(Tester.class);
    public static void main(String [] args) {
    	UserManager userManager = new UserManagerImpl();
    	ArrayList dev = new ArrayList();
		dev.add("192.168.28.194");
    	Accounts acct = new Accounts("cisco","cisco","admin","1146105610","1246105610","nj","null",dev);
    	userManager.delAccounts(acct);
    	userManager.addAccounts(acct);

    	
    	String sql = "select * from rad_record where why_failed = 'CHECKED_USER_NOT_EXIST'";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select record_id from rad_record where why_failed = 'CHECKED_USER_NOT_EXIST'";
		}
    	PrepareSQL psql = new PrepareSQL(sql);
    	ArrayList record = userManager.getRecord(psql.getSQL());
    	if(record == null ) {
    		logger.debug("record == null");
    		return;
    	}
    	for(int i=0; i<record.size(); i++) {
    		Map map = (Map)record.get(i);
    		logger.debug("record id: " + (String)map.get("record_id"));
    	}
    	//logger.debug(AcctUtil.isBlocked("user0"));
/*    	logger.debug(acct.removeAcct());
    	logger.debug(acct.saveAcct());
    	ArrayList newDevList = new ArrayList();
    	newDevList.add("192.168.28.191");
    	newDevList.add("192.168.28.192");
    	newDevList.add("192.168.28.193");
    	newDevList.add("192.168.28.197");
    	newDevList.add("192.168.28.198");
    	newDevList.add("192.168.28.199");
    	Accounts newAcct = new Accounts("user9_mod","user9_mod_pwd",newDevList);
    	int result;
    	//result = newAcct.modDevOfAcct();
    	result = userManager.modAccounts(acct, newAcct);
    	logger.debug("result="+result);
    	

    	 //show all the information in the memery

    	ArrayList acctInfo = Accounts.getAcctInfo();
    	ArrayList acctDevInfo = Accounts.getAcctDevInfo();
    	for(int i=0; i<acctInfo.size(); i++) {
    		Map map = (Map)acctInfo.get(i);
    		ArrayList devList = (ArrayList)acctDevInfo.get(i);
    		logger.debug("**************************************");
    		logger.debug("user_name:\t" + map.get("user_name"));
    		logger.debug("user_pwd:\t" + map.get("user_pwd"));
    		logger.debug("creator:\t" + map.get("creator"));
    		logger.debug("create_date:\t" + map.get("create_date"));
    		logger.debug("effect_date:\t" + map.get("effect_date"));
    		logger.debug("expire_date:\t" + map.get("expire_date"));
    		logger.debug("city:\t\t" + map.get("city"));
    		logger.debug("demo:\t\t" + map.get("demo"));
    		logger.debug("Devices:");
    		logger.debug("devList.size\t="+devList.size());
    		for(int j=0; j<devList.size(); j++) {
    			logger.debug("\t\t"+devList.get(j));
    		}
    	}
*.
    	userManager.getRecord(null);
/*    	ArrayList src = new ArrayList(8);
    	ArrayList dst = new ArrayList(9);
    	src.add("uu");//1
    	src.add("AA");//2
    	src.add("20");//3
    	src.add("BB");//4
    	src.add("CC");//5
    	src.add("DD");//6
    	src.add("30");//7
    	src.add("EE");//8
    	
    	dst.add("AA");//1
    	dst.add("11");//2
    	dst.add("CC");//3
    	dst.add("22");//4
    	dst.add("ff");//5
    	dst.add("44");//6
    	dst.add("BB");//7
    	dst.add("EE");//8
    	dst.add("DD");//9
   	
    	logger.debug("Original src:");
    	for(int i=0; i<src.size(); i++) {
    		
    	}
    	logger.debug();
    	logger.debug("Original dst:");
    	for(int i=0; i<dst.size(); i++) {
    	
    	}
    	logger.debug();
    	//dst.remove(0);
    	dst.add(0,"222222"); */
/*		for(int i=1; i<src.size(); i++) {
			for(int j=0; j<dst.size(); j++) {
				if(((String)src.get(i)).compareTo( (String)dst.get(j) ) == 0) {
					logger.debug(src.get(i));
					src.remove(i);
					logger.debug(dst.get(j));
					dst.remove(j);
					i--;j--;
					break;
				}
			}
		}
    	logger.debug("new src:");
    	for(int i=0; i<src.size(); i++) {
    	
    	}
    	logger.debug();
    	logger.debug("new dst:");
    	for(int i=0; i<dst.size(); i++) {
    		
    	}
    	logger.debug();*/
    	
    	
/*		String sql = "select user_name from rad_accounts where user_name = 'test19'";
    	Cursor csr = DataSetBean.getCursor(sql);
		if(csr.getRecordSize() != 0) {
			logger.debug("User already existed!");
		} else {
			logger.debug("User NOT existed!");
		}
    	String l = "92461826567164";
    	logger.debug(l.compareTo("92461826567165"));

    	String sql="select * from rad_accounts";
    	Cursor csr = DataSetBean.getCursor(sql);
    	logger.debug("row in the rad_accounts is: "+csr.getRecordSize());
    	Map record = csr.getRecord(0);
    	Set set = record.keySet();
    	Object [] key = set.toArray();
		record = csr.getRecord(0);
		String user = (String)record.get("user_name");
    	logger.debug("user_name=" + user);
    	int column=key.length;
    	for(int i=0;i<column;i++){
    		logger.debug(key[i].toString()+" = "+record.get(key[i].toString()));
    	}
*/

    	System.exit(0);
    }
}
