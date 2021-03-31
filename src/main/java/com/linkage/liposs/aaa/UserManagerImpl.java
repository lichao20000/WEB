/**
 * @(#)UserManagerImpl.java 2006-2-9
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.liposs.aaa;

import java.util.ArrayList;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public class UserManagerImpl implements UserManager {
	private static final String DO_NOT_DEED_THIS_STR = "DO_NOT_DEED_THIS";
	private static final ArrayList DO_NOT_DEED_THIS_LIST = new ArrayList(0);
	
	
	/**
	 * 增加一个帐号
	 * @param acct
	 * 			new Accounts
	 * @return 1 as accounts successfully created, negative values on error occured:
	 * 			ACCOUNTS_CREATE_SUCCESS		= 1;
	 *			ACCOUNTS_EXISTED			= -1;
	 *			ACCOUNTS_INVALID_USERNAME	= -4;
	 *			ACCOUNTS_INVALID_PASSWORD	= -5;
	 *			ACCOUNTS_DATEBASE_FAILED	= -10;
	 *			ACCOUNTS_UNKOWN_ERROR		= -100;
	 */
	public int addAccounts(Accounts acct) {
		return acct.saveAcct();
	}
	
	/**
	 * 增加一个帐号
	 * @param userName
	 * 			user name
	 * @param pwd
	 * 			password
	 * @param creator
	 * 			web user who created this accounts
	 * @param effectDate
	 * 			date this accounts effectes
	 * @param expireDate
	 * 			date this accounts expires
	 * @param city
	 * 			which city will this accounts used
	 * @param demo
	 * 			record something that the web user think is important, less than 255 words
	 * @param devices
	 * 			devices this accounts can logon
	 * @return 1 as accounts successfully created, negative values on error occured:
	 * 			ACCOUNTS_CREATE_SUCCESS		= 1;
	 *			ACCOUNTS_EXISTED			= -1;
	 *			ACCOUNTS_INVALID_USERNAME	= -4;
	 *			ACCOUNTS_INVALID_PASSWORD	= -5;
	 *			ACCOUNTS_DATEBASE_FAILED	= -10;
	 *			ACCOUNTS_UNKOWN_ERROR		= -100;
	 */
	public int addAccounts(final String userName,
							final String pwd,
							final String creator,
							final String effectDate,
							final String expireDate,
							final String city,
							final String demo,
							final ArrayList devices) {
		
		Accounts acct = new Accounts(userName,
									 pwd,
									 creator,
									 effectDate,
									 expireDate,
									 city,
									 demo,
									 devices);
		return addAccounts(acct);
	}
	
	/**
	 * 删除一个帐号
	 * @param username
	 * 			用户名
	 * @return
	 *		删除结果：
	 *			ACCOUNTS_CREATE_SUCCESS		= 1
	 *			ACCOUNTS_DEL_SUCCESS		= 2
	 *			ACCOUNTS_MOD_SUCCESS		= 3
	 *			ACCOUNTS_EXISTED			= -1
	 *			ACCOUNTS_NOT_EXISTED		= -2
	 *			ACCOUNTS_INVALID_USERNAME	= -3
	 *			ACCOUNTS_INVALID_PASSWORD	= -4
	 *			ACCOUNTS_DEL_FAILED			= -5
	 *			ACCOUNTS_MOD_FAILED			= -6
	 *			ACCOUNTS_DATEBASE_FAILED	= -10
	 *			ACCOUNTS_UNKOWN_ERROR		= -100
	 */
	public int delAccounts(String username) {
		/*
		 * We can get the accounts with username, 
		 * so, when we want to remove it, only a username is needed
		 */
		Accounts acct = getAcctByUser(username);
		return delAccounts(acct);
	}
	
	/**
	 * 删除一个帐号
	 * @param acct
	 * 			待删除的帐号
	 * @return
	 * 		删除结果
	 *			ACCOUNTS_CREATE_SUCCESS		= 1
	 *			ACCOUNTS_DEL_SUCCESS		= 2
	 *			ACCOUNTS_MOD_SUCCESS		= 3
	 *			ACCOUNTS_EXISTED			= -1
	 *			ACCOUNTS_NOT_EXISTED		= -2
	 *			ACCOUNTS_INVALID_USERNAME	= -3
	 *			ACCOUNTS_INVALID_PASSWORD	= -4
	 *			ACCOUNTS_DEL_FAILED			= -5
	 *			ACCOUNTS_MOD_FAILED			= -6
	 *			ACCOUNTS_DATEBASE_FAILED	= -10
	 *			ACCOUNTS_UNKOWN_ERROR		= -100
	 */
	public int delAccounts(Accounts acct) {
		return acct.removeAcct();
	}
	
	/**
	 * 修改一个帐号可以登录的设备
	 * @param username
	 * 			目标用户
	 * @param newDevList
	 * 			新设备列表
	 * @return
	 * 		修改结果
	 *			ACCOUNTS_CREATE_SUCCESS		= 1
	 *			ACCOUNTS_DEL_SUCCESS		= 2
	 *			ACCOUNTS_MOD_SUCCESS		= 3
	 *			ACCOUNTS_EXISTED			= -1
	 *			ACCOUNTS_NOT_EXISTED		= -2
	 *			ACCOUNTS_INVALID_USERNAME	= -3
	 *			ACCOUNTS_INVALID_PASSWORD	= -4
	 *			ACCOUNTS_DEL_FAILED			= -5
	 *			ACCOUNTS_MOD_FAILED			= -6
	 *			ACCOUNTS_DATEBASE_FAILED	= -10
	 *			ACCOUNTS_UNKOWN_ERROR		= -100
	 */
	public int modDevOfAcct(String username, ArrayList newDevList) {
		Accounts acct = getAcctByUser(username);
		if(acct == null) {
//			AcctUtil.log("modDevOfAcct: error occured while modifying devices of accounts.");
			return AcctUtil.ACCOUNTS_MOD_FAILED;
		}
		acct.setDevice(newDevList);
		return modDevOfAcct(acct);
	}
	
	/**
	 * 修改一个帐号可以登录的设备
	 * @param acctWithNewDevList
	 * 			包含新列表的帐号
	 * @return
	 *			ACCOUNTS_CREATE_SUCCESS		= 1
	 *			ACCOUNTS_DEL_SUCCESS		= 2
	 *			ACCOUNTS_MOD_SUCCESS		= 3
	 *			ACCOUNTS_EXISTED			= -1
	 *			ACCOUNTS_NOT_EXISTED		= -2
	 *			ACCOUNTS_INVALID_USERNAME	= -3
	 *			ACCOUNTS_INVALID_PASSWORD	= -4
	 *			ACCOUNTS_DEL_FAILED			= -5
	 *			ACCOUNTS_MOD_FAILED			= -6
	 *			ACCOUNTS_DATEBASE_FAILED	= -10
	 *			ACCOUNTS_UNKOWN_ERROR		= -100
	 */
	public int modDevOfAcct(Accounts acctWithNewDevList) {
		return acctWithNewDevList.modDevOfAcct();
	}
	
	
	/**
	 * 修改一个设备允许登录的帐号
	 * @param acct
	 * 			用户列表
	 * @return
	 * 		未实现
	 */
	public int modAcctOfDev(String devAddr, ArrayList newAcctList) {
		//TODO
		ArrayList oldAcctList = getAcctByDev(devAddr);
		
		ArrayList src = (ArrayList)oldAcctList.clone();
		ArrayList dst = (ArrayList)newAcctList.clone();
		
		/*
		 * remove the same accounts in the two device lists
		 */
		for(int i=0; i<src.size(); i++) {
			for(int j=0; j<dst.size(); j++) {
				if(((String)src.get(i)).compareTo( (String)dst.get(j) ) == 0) {
					src.remove(i);
					dst.remove(j);
					i--;j--;//move the poiters ahead
					break;
				}
			}
		}
		
		for(int i=0; i<src.size(); i++) {
			String sql = "delete rad_acc_device where user_name='" + 
				((Accounts)src.get(i)).getUserName() +
				"' and loopback_ip='" + devAddr + "'";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sql = "delete from rad_acc_device where user_name='" +
						((Accounts)src.get(i)).getUserName() +
						"' and loopback_ip='" + devAddr + "'";
			}
//			AcctUtil.log("modAcctOfDev: " + sql);			
		}
		return 0;
	}
	
	/**
	 * 修改帐号的用户名、密码等信息
	 * @param acctOld
	 * 			老帐号
	 * @param acctNew
	 * 			新帐号
	 * @return 修改结果
	 *			ACCOUNTS_CREATE_SUCCESS		= 1
	 *			ACCOUNTS_DEL_SUCCESS		= 2
	 *			ACCOUNTS_MOD_SUCCESS		= 3
	 *			ACCOUNTS_EXISTED			= -1
	 *			ACCOUNTS_NOT_EXISTED		= -2
	 *			ACCOUNTS_INVALID_USERNAME	= -3
	 *			ACCOUNTS_INVALID_PASSWORD	= -4
	 *			ACCOUNTS_DEL_FAILED			= -5
	 *			ACCOUNTS_MOD_FAILED			= -6
	 *			ACCOUNTS_DATEBASE_FAILED	= -10
	 *			ACCOUNTS_UNKOWN_ERROR		= -100
	 */
	public int modAccounts(Accounts acctOld, Accounts acctNew) {
		String oldUserName = acctOld.getUserName();
		
		int flag = AcctUtil.ACCOUNTS_UNKOWN_ERROR;
		flag = acctOld.removeAcct();
		if (flag != AcctUtil.ACCOUNTS_DEL_SUCCESS) {
			AcctUtil.log("modAccounts: Code: " + AcctUtil.ACCOUNTS_DEL_FAILED +
					", accounts: " + oldUserName + " delete failed!");
			return AcctUtil.ACCOUNTS_MOD_FAILED;
		}
		
		flag = AcctUtil.ACCOUNTS_UNKOWN_ERROR;
		flag = acctNew.saveAcct();
		if (flag != AcctUtil.ACCOUNTS_CREATE_SUCCESS) {
//			AcctUtil.log("modAccounts: Code: " + AcctUtil.ACCOUNTS_DEL_FAILED +
//					", accounts: " + acctNew.getUserName() + " create failed!");
			return AcctUtil.ACCOUNTS_MOD_FAILED;
		}
		
//		AcctUtil.log("modAccounts: modify Accounts successfully, old username:" + oldUserName);
		return AcctUtil.ACCOUNTS_MOD_SUCCESS;
	}
	
	/**
	 * 修改帐号的用户名、密码等信息
	 * @param usernameOld
	 * 			老用户名
	 * @param acctNew
	 * 			新帐号
	 * @return
	 *			ACCOUNTS_CREATE_SUCCESS		= 1
	 *			ACCOUNTS_DEL_SUCCESS		= 2
	 *			ACCOUNTS_MOD_SUCCESS		= 3
	 *			ACCOUNTS_EXISTED			= -1
	 *			ACCOUNTS_NOT_EXISTED		= -2
	 *			ACCOUNTS_INVALID_USERNAME	= -3
	 *			ACCOUNTS_INVALID_PASSWORD	= -4
	 *			ACCOUNTS_DEL_FAILED			= -5
	 *			ACCOUNTS_MOD_FAILED			= -6
	 *			ACCOUNTS_DATEBASE_FAILED	= -10
	 *			ACCOUNTS_UNKOWN_ERROR		= -100
	 */
	public int modAccounts(String usernameOld, Accounts acctNew) {
		Accounts acctOld = getAcctByUser(usernameOld);
		return modAccounts(acctOld, acctNew);
	}
	
	/**
	 * 获取一个帐号
	 * @param userName
	 * @return 包含用户名的帐号
	 */
	public Accounts getAcctByUser(String userName) {
		ArrayList acctDevInfo = Accounts.getAcctDevInfo();
		ArrayList acctInfo = Accounts.getAcctInfo();
		ArrayList devList = new ArrayList();
		if(userName.length() == 0) {
//			AcctUtil.log("getAcctByUser: invalid user name, null will be returned");
			return null;
		}
		Accounts acctNew = new Accounts(userName, DO_NOT_DEED_THIS_STR, DO_NOT_DEED_THIS_LIST); 
		for(int i=0; i<acctDevInfo.size(); i++) {
			devList = (ArrayList)acctDevInfo.get(i);
			if(((String)devList.get(0)).compareTo(userName) == 0) {
				break;
			}
		}
		if(devList.isEmpty()) {
//			AcctUtil.log("getAcctByUser: accouts: '" + userName +"' NOT found, a useless one will be returned.");
			return acctNew;
		}
		for(int i=0; i<acctInfo.size(); i++) {
			Map acct = (Map)acctInfo.get(i);
			if(((String)acct.get("user_name")).compareTo(userName) == 0) {
				String user			= (String)acct.get("user_name");
				String pwd			= (String)acct.get("user_pwd");
				String creator		= (String)acct.get("creator");
				String createDate	= (String)acct.get("create_date");
				String effectDate	= (String)acct.get("effect_date");
				String expireDate	= (String)acct.get("expire_date");
				String city			= (String)acct.get("city");
				String memo			= (String)acct.get("memo");
				acctNew =  new Accounts(user, 
										pwd, 
										creator, 
										createDate,
										effectDate, 
										expireDate, 
										city, 
										memo,
										devList);
				return acctNew;
			}
		}
//		AcctUtil.log("getAcctByUser: accouts: " + userName +" NOT found, a useless one will be returned.");
		return acctNew;
	}
	
	/**
	 * 获取所有的帐号，不包含账号可以登陆的设备链表
	 * @return 
	 * 		帐号列表，链表中每个元素都是一个Map类型的数据结构，key跟表：rad_accounts中的列名一样。
	 */
	public ArrayList getAllAcct() {
		ArrayList acctDevInfo = Accounts.getAcctDevInfo();
		return acctDevInfo;
	}
	
	/**
	 * 返回一个设备允许登录的帐号
	 * @param devIp
	 * 			设备的loopback ip
	 * @return 允许登录deviceId所指设备的帐号列表
	 */
	public ArrayList getAcctByDev(String devIp) {
		ArrayList acctDevInfo = Accounts.getAcctDevInfo();
		ArrayList acctByDev = new ArrayList(acctDevInfo.size());
		if(devIp.length() == 0) {
//			AcctUtil.log("getAcctByDev: invalid device ip, null will be returned");
			return null;
		}
		for(int i=0; i<acctDevInfo.size(); i++) {
			ArrayList tmp = (ArrayList)acctDevInfo.get(i);
			for(int j=1; j<tmp.size(); j++) {
				if(((String)tmp.get(j)).compareTo(devIp) == 0) {
					acctByDev.add(tmp.get(0));
					break;
				}
			}
		}
		if(acctByDev.size() == 0) {
//			AcctUtil.log("getAcctByDev: no valid accounts found, a empty list will be returned");
		}
		acctByDev.trimToSize();
		return acctByDev;
	}
	
	/**
	 * 获取一个帐号可以登陆的设备列表
	 * @param username
	 * 			用户名
	 * @return
	 * 		设备列表，找不到时返回null
	 */
	public ArrayList getDevOfAcct(String username) {
		if(username.length() == 0) {
//			AcctUtil.log("getDevOfAcct: invalid username, null will be returned");
			return null;
		}
		ArrayList acctDevInfo = Accounts.getAcctDevInfo();
		for(int i=0; i<acctDevInfo.size(); i++) {
			ArrayList devOfAcct = (ArrayList)acctDevInfo.get(i);
			if(((String)devOfAcct.get(0)).compareTo(username) == 0) {
				devOfAcct.remove(0);//the first element of this list is the username,
									//BUT an ip of a device, invoker will not be interesting
									//with this, so remove it.
				return devOfAcct;
			}
		}
		//no matched list found
//		AcctUtil.log("getDevOfAcct: no matched list found, null will be returned");
		return null;
	}
	
	/**
	 * 获取一个accounts可以登陆的设备列表
	 * @param acct
	 * 			帐号
	 * @return
	 * 		设备列表，找不到时返回null
	 */
	public ArrayList getDevOfAcct(Accounts acct) {
		return getDevOfAcct(acct.getUserName());
	}
    
	/**
	 * fetch the records according to the <code>queryStr</code>
	 * @param queryStr
	 * 		sql statement
	 * @return
	 * 		Array list of records, whose elements are <code>Map</code>
	 * 		keys of element are the same column name as which in the datebase
	 */
	public ArrayList getRecord(String queryStr) {
		if(queryStr == null) {
//			AcctUtil.log("getRecord: Invalid sql query statment, null will be returned");
			return null;
		}
		if(!queryStr.trim().toLowerCase().startsWith("select")) {
//			AcctUtil.log("getRecord: the sql statement should start with 'select', case NOT senstivity, null will be returned");
			return null;
		}
		
//		AcctUtil.log("getRecord: " + queryStr);
		Cursor csr;
		try {
			csr = DataSetBean.getCursor(queryStr);
		} catch (Exception e) {
//			AcctUtil.log("getRecord: Error occured while fetching info from rad_record! null will be returned");
			return null;
		}
		
		int rows = csr.getRecordSize();
		ArrayList recordList = new ArrayList(rows);
		for(int i=0; i<rows; i++) {
			recordList.add(csr.getRecord(i));
		}
		return recordList;
	}
}
