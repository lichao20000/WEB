/**
 * @(#)Accounts.java 2006-2-9
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.liposs.aaa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public class Accounts {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(Accounts.class);
	/*
	 * read all the information from the database
	 * IMPORATANCE: we must make sure the data in the memery synchronized with that
	 * 				in the database manually!!!!!!
	 * 
	 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
     *    |     map1     |     map2     |     map3     |   ...    |     mapN    |
     *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
     * mapN records all of a certain accounts' information, the key of which is the same
     * as that in the database
	 */
	private static ArrayList acctInfo = new ArrayList();
	/*
	 * ArrayList is array list whose elements is an array list, too
	 * IMPORATANCE: we must make sure the data in the memery synchronized with that
	 * 				in the database manually!!!!!!
	 * 
	 *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
     *    |     arraylist1      |    arraylist2     |    arraylist3     |   ...
     *    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
     * the first element of arraylistN is user name of a certain accounts, the other 
     * elements is the ip which can be logined by the accouts
	 */
	private static ArrayList acctDevInfo = new ArrayList();
	
	/*
	 * preread the information of this accounts
	 */
	static {
		readAcctInfo();
	}
	
	private String 	userName;
	private String 	pwd;
	private String 	creator;
	private String 	createDate;
	private String 	effectDate;
	private String 	expireDate;
	private String 	city;
	private String 	memo;
	private ArrayList devices;

	/**
	 * 
	 * @param userName
	 * @param pwd
	 * @param devices
	 */
	public Accounts(String userName, 
					String pwd, 
					ArrayList devices) {
		this(userName, 
			pwd, 
			AcctUtil.DEFAULT_CREATOR, 
			//DEFAULT_CREATE_DATE, 
			AcctUtil.DEFAULT_EFFECT_DATE_STR, 
			AcctUtil.DEFAULT_EXPIRED_DATE_STR, 
			AcctUtil.DEFAULT_CITY, 
			AcctUtil.DEFAULT_MEMO, 
			devices);
	}
	
	/**
	 * 
	 * @param userName
	 * @param pwd
	 * @param creator
	 * @param effectDate
	 * @param expireDate
	 * @param city
	 * @param memo
	 * @param devices
	 */
	public Accounts(String userName, 
					String pwd, 
					String creator, 
					//Date createDate, 
					Date effectDate, 
					Date expireDate, 
					String city, 
					String memo, 
					ArrayList devices) {
		this(userName, 
			pwd, 
			creator, 
			String.valueOf(effectDate.getTime()),
			String.valueOf(expireDate.getTime()),
			city,
			memo,
			devices);
	}
	
	/**
	 * 
	 * @param userName
	 * @param pwd
	 * @param creator
	 * @param effectDate
	 * @param expireDate
	 * @param city
	 * @param memo
	 * @param devices
	 */
	public Accounts(String userName, 
					String pwd, 
					String creator, //web username
					//String createDate, 
					String effectDate, 
					String expireDate, 
					String city, 
					String memo, 
					ArrayList devices) {
		this(userName,
				pwd,
				creator,
				AcctUtil.DEFAULT_CREATE_DATE_STR,
				effectDate,
				expireDate,
				city,
				memo,
				devices);
	}
	
	/**
	 * This constructor should only invoked by UserManagerImpl.getAcctByUser(String userName)
	 * 
	 * @param userName
	 * @param pwd
	 * @param creator
	 * @param createDate
	 * @param effectDate
	 * @param expireDate
	 * @param city
	 * @param memo
	 * @param devices
	 */
	public Accounts(String userName, 
					String pwd, 
					String creator, //web username
					String createDate, 
					String effectDate, 
					String expireDate, 
					String city, 
					String memo, 
					ArrayList devices) {
		this.userName	= userName;
		this.pwd		= pwd;
		if(creator == null) {
			this.creator = AcctUtil.DEFAULT_CREATOR;
		} else {
			this.creator	= creator;
		}
		if(createDate == null) {
			this.createDate = AcctUtil.DEFAULT_CREATE_DATE_STR;
		} else {
			this.createDate	= createDate;
		}
		if(effectDate == null) {
			this.effectDate = AcctUtil.DEFAULT_EFFECT_DATE_STR;
		} else {
			this.effectDate	= effectDate;
		}
		if(expireDate == null) {
			this.expireDate	= AcctUtil.DEFAULT_EXPIRED_DATE_STR;
		} else if(expireDate.compareTo(AcctUtil.DEFAULT_CREATE_DATE_STR) <= 0) {
			this.expireDate	= AcctUtil.DEFAULT_EXPIRED_DATE_STR;
		} else {
			this.expireDate	= expireDate;
		}
		if(city == null) {
			this.city	= AcctUtil.DEFAULT_CITY;
		} else {
			this.city = city;
		}
		if(memo == null) {
			this.memo = AcctUtil.DEFAULT_MEMO;
		} else {
			this.memo = memo;
		}
		/*
		 * make sure devices is NOT null
		 */
		if(devices == null) {
			devices = new ArrayList(1);
			devices.add(AcctUtil.DEFAULT_NO_VALID_DEVICE);
		}
		this.devices = devices;
		
		/*
		 * preread the information of this accounts
		 */
		//readAcctInfo();
	}
	
	/**
	 * save the current accounts to database.
	 * @return
	 *		ACCOUNTS_CREATE_SUCCESS(=1) if successful, nagative value on error occures
	 */
	public int saveAcct() {
		for(int i=0; i<acctInfo.size(); i++) {
			Map acc = (Map)acctInfo.get(i);
			String user = (String)acc.get("user_name");
			if(user.compareTo(userName) == 0) {
//				AcctUtil.log("saveAcct: Code: " + AcctUtil.ACCOUNTS_EXISTED +
//									", User tried to insert an accounts: '" + userName +
//									"' that is already existed, blocked.");
				return AcctUtil.ACCOUNTS_EXISTED;
			}
		}
		
		/*
		* user existed in the database
		* let's process it further
		*/
		int verified = AcctUtil.verifyUser(userName, pwd);
		if(verified != AcctUtil.UTIL_USER_NAME_OK) {
			return verified;
		}
		/*
		* user name and password is legal,
		* insert it into the table: rad_accounts
		*/
		String sql = "insert into rad_accounts(user_name,user_pwd,creator,create_date," +
			"effect_date,expire_date,city,memo) values('" +
			userName + "','" + pwd + "','" + creator + "','" + createDate +
			"','" + effectDate + "','" + expireDate + "','" + city + "','" +
			memo + "')";
//		AcctUtil.log("saveAcct: " + sql);
		int flag;
		try {
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			flag = DataSetBean.executeUpdate(sql);
		} catch (Exception e) {
//			AcctUtil.log("saveAcct: insert operation failed! Username: " + userName);
			return AcctUtil.ACCOUNTS_DATEBASE_FAILED;
		}
		if(flag == -1){
//			AcctUtil.log("saveAcct: insert operation failed! Username: " + userName);
			return AcctUtil.ACCOUNTS_DATEBASE_FAILED;
		}
		
		acctInfo.ensureCapacity(acctInfo.size()+1);
		addAcct2AcctInfo();//save the new accounts into memery
//		AcctUtil.log("saveAcct: "  + userName + 
//							" successfully inserted into database!");
		
		/*
		 * insert data into table: rad_acc_device
		 * IMPORTATANCE: It seems long work to do to save device_id here,
		 * 				 we insert the column device_id the same as the 
		 * 				 device loopback ip.
		 */
		flag = -10;//any value NOT equals to -1
		int retry = 0;
		for(int i=0; i<devices.size(); i++) {
			/*
			 * as we are now creating a new accounts, there should be no same record
			 * in the table: rad_acc_device. Since that, we will NOT check that up for
			 * a second time.
			 */
			while(retry < 3) {
				sql = "insert into rad_acc_device(device_id,loopback_ip,user_name)" +
					" values('" + devices.get(i) +"','" + devices.get(i) +"','" +
					userName + "')";
//				AcctUtil.log("saveAcct: " + sql);
				PrepareSQL psql = new PrepareSQL(sql);
				psql.getSQL();
				flag = DataSetBean.executeUpdate(sql);
				if(flag >= 1) {
					break;
				} else {
					retry++;
					try{
						Thread.sleep(200);
					} catch(Exception e) {
						logger.warn(e.getMessage());
					}
				}
			}
			if(retry >= 3) {
//				AcctUtil.log("saveAcct: Error happened while triying " +
//						"to insert data into rad_acc_device! Username: " + userName);
				return AcctUtil.ACCOUNTS_DATEBASE_FAILED;
			}
		}
		addNewDevList2AcctDevInfo();//save the new accounts' device list into memery
//		AcctUtil.log("saveAcct: Accounts saved successfully! Username: " + userName);
		return AcctUtil.ACCOUNTS_CREATE_SUCCESS;
	}
	
	/**
	 * remove current accounts from database.
	 * @return
	 * 		ACCOUNTS_DEL_SUCCESS(=2) if successful, nagative value on error occures
	 */
	public int removeAcct() {
		boolean flag = false;
		int i;
		for(i=0; i<acctInfo.size(); i++) {
			Map acct = (Map)acctInfo.get(i);
			String user = (String)acct.get("user_name");
			if(user.compareTo(userName) == 0) {
				flag = true;
				break;
			}
		}
		if(flag) {
			String sql = "delete rad_accounts where user_name='" + userName + "'";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				sql = "delete from rad_accounts where user_name='" + userName + "'";
			}
//			AcctUtil.log("removeAcct: " + sql);
			int execDel;
			try {
				PrepareSQL psql = new PrepareSQL(sql);
				psql.getSQL();
				execDel = DataSetBean.executeUpdate(sql);
			} catch (Exception e) {
//				AcctUtil.log("removeAcct: delete operation failed! Username: " + userName);
				return AcctUtil.ACCOUNTS_DATEBASE_FAILED;
			}
			if(execDel == -1){
//				AcctUtil.log("removeAcct: delete operation failed! Username: " + userName);
				return AcctUtil.ACCOUNTS_DATEBASE_FAILED;
			}

			acctInfo.remove(i);//remove this accounts from memery, too
			/*
			 * we have to remove all the information from rad_acc_device, too
			 */
			sql = "delete rad_acc_device where user_name='" + userName + "'";
//			AcctUtil.log("removeAcct: " + sql);
			execDel = -10;//any value BUT -1
			try {
				PrepareSQL psql = new PrepareSQL(sql);
				psql.getSQL();
				execDel = DataSetBean.executeUpdate(sql);
			} catch (Exception e) {
//				AcctUtil.log("removeAcct: delete operation failed! Username: " + userName);
				return AcctUtil.ACCOUNTS_DATEBASE_FAILED;
			}
			if(execDel == -1){
//				AcctUtil.log("removeAcct: delete operation failed! Username: " + userName);
				return AcctUtil.ACCOUNTS_DATEBASE_FAILED;
			}
			acctDevInfo.remove(i);//remove this accounts from memery, too
			
			/*
			 * operation executed successfully, feed something back to the caller
			 */
//			AcctUtil.log("removeAcct: Accounts deleted successfully! Username: " + userName);
			return AcctUtil.ACCOUNTS_DEL_SUCCESS;
		} else {
//			AcctUtil.log("removeAcct: Code: " + AcctUtil.ACCOUNTS_NOT_EXISTED +
//								", User tried to delete an accounts: '" + userName +
//								"' that is NOT existed, blocked.");
			return AcctUtil.ACCOUNTS_NOT_EXISTED;
		}
	}
	
	/**
	 * modify devices of an accounts can login
	 * @return
	 * 		ACCOUNTS_MOD_SUCCESS(=3) if successful, negative value on error occures
	 */
	public int modDevOfAcct() {
		boolean flag = false;
		int pst_usr;
		/*
		 * check out weather the user is existed, 
		 * remember it's position in varible pst_usr
		 */
		for(pst_usr=0; pst_usr<acctInfo.size(); pst_usr++) {
			Map acct = (Map)acctInfo.get(pst_usr);
			String user = (String)acct.get("user_name");
			if(user.compareTo(userName) == 0) {
				flag = true;
				break;
			}
		}
		if(flag) {
			int pst_dev=0;
			for(pst_dev=0; pst_dev<acctDevInfo.size(); pst_dev++) {
				if( ( (String) ((ArrayList)acctDevInfo.get(pst_dev)).get(0) ).compareTo(userName) == 0 ) {
					break;
				}
			}
			if(pst_dev >= acctDevInfo.size()) {
//				AcctUtil.log("modDevOfAcct: Code: " + AcctUtil.ACCOUNTS_UNKOWN_ERROR +
//									", data in list acctInfo and acctDevInfo " +
//									"doesn't match! Username: " + userName);
				return AcctUtil.ACCOUNTS_UNKOWN_ERROR;
			}
			ArrayList src = (ArrayList)acctDevInfo.get(pst_dev);
			ArrayList dst = new ArrayList(devices.size());
			/*
			 * clone devices to dst
			 */
			for(int i=0; i<devices.size(); i++) {
				dst.add(devices.get(i));
			}

			/*
			 * find out devices that are present in both src and dst, remove them,
			 * at last, device(s) in the src are which we must remove from database,
			 * and, device(s) in the dst are which we must insert into database
			 */
			for(int i=1; i<src.size(); i++) {//the first element of src is the usrname, should NOT be removed
				for(int j=0; j<dst.size(); j++) {
					if(((String)src.get(i)).compareTo( (String)dst.get(j) ) == 0) {
						src.remove(i);
						dst.remove(j);
						i--;j--;//move the poiters ahead
						break;
					}
				}
			}


/*			
			/*
			 * remove device(s) in the list src from database
			 */
			for(int i=1; i<src.size(); i++) {
				String sql = "delete rad_acc_device where loopback_ip = '" +
							(String)src.get(i) + "'";
				// teledb
				if (DBUtil.GetDB() == Global.DB_MYSQL) {
					sql = "delete from rad_acc_device where loopback_ip = '" +
							(String)src.get(i) + "'";
				}
				AcctUtil.log("modDevOfAcct: " + sql);
				int execDel = -10;//any value BUT -1
				try {
					PrepareSQL psql = new PrepareSQL(sql);
					psql.getSQL();
					execDel = DataSetBean.executeUpdate(sql);
				} catch (Exception e) {
//					AcctUtil.log("modDevOfAcct: modify operation failed! " +
//							"Username: " + userName);
					return AcctUtil.ACCOUNTS_DATEBASE_FAILED;
				}
				if(execDel == -1){
//					AcctUtil.log("modDevOfAcct: modify operation failed! " +
//							"Username: " + userName);
					return AcctUtil.ACCOUNTS_DATEBASE_FAILED;
				}
			}
			/*
			 * insert new device(s) in the list into the database.
			 */
			for(int i=0; i<dst.size(); i++) {
				String sql = "insert into rad_acc_device(device_id,loopback_ip,user_name) values('" +
							(String)dst.get(i) + "','" + (String)dst.get(i) + "','" +
							userName +"')";
				AcctUtil.log("modDevOfAcct: " + sql);
				int execSql = -10;//any value BUT -1
				try {
					PrepareSQL psql = new PrepareSQL(sql);
					psql.getSQL();
					execSql = DataSetBean.executeUpdate(sql);
				} catch (Exception e) {
//					AcctUtil.log("modDevOfAcct: insert operation failed! " +
//							"Username: " + userName);
					return AcctUtil.ACCOUNTS_DATEBASE_FAILED;
				}
				if(execSql == -1){
//					AcctUtil.log("modDevOfAcct: insert operation failed! " +
//							"Username: " + userName);
					return AcctUtil.ACCOUNTS_DATEBASE_FAILED;
				}
			}
			/*
			 * since database delete operation successfully, update data in the memery
			 */
			dst = devices;
			dst.add(0,userName);
					
			acctDevInfo.remove(pst_dev);
			acctDevInfo.add(pst_dev, dst);
			
//			AcctUtil.log("modDevOfAcct: Accounts modified successfully! " +
//					"Username: " + userName);
			return AcctUtil.ACCOUNTS_MOD_SUCCESS;
		} else {
//			AcctUtil.log("modDevOfAcct: Code: " + AcctUtil.ACCOUNTS_NOT_EXISTED +
//								", User tried to modify an accounts '" + userName +
//								"' that is NOT existed, blocked.");
			return AcctUtil.ACCOUNTS_NOT_EXISTED;
		}
	}

/*	
	public int modAcctOfDev(String devAddr, ArrayList newAcctList, ArrayList oldAcctList) {	
		ArrayList src = (ArrayList)oldAcctList.clone();
		ArrayList dst = (ArrayList)newAcctList.clone();
		
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
			String sql = "delete rad_acc_device where user_name='" + userName +
				"' and loopback_ip='" + devAddr + "'";
			AcctUtil.log("modAcctOfDev: " + sql);
			
			AcctUtil.log("modAcctOfDev: " + sql);
			int execDel = -10;//any value BUT -1
			try {
				execDel = DataSetBean.executeUpdate(sql);
			} catch (Exception e) {
				AcctUtil.log("modAcctOfDev: delete operation failed! " +
						"Username: " + userName);
				return AcctUtil.ACCOUNTS_DATEBASE_FAILED;
			}
			if(execDel == -1){
				AcctUtil.log("modAcctOfDev: delete operation failed! " +
						"Username: " + userName);
				return AcctUtil.ACCOUNTS_DATEBASE_FAILED;
			}
		}
		return 0;
	}
*/	
	
	
	/**
	 * preread all the information in the table: rad_accounts and rad_acc_device
	 * into memery to gain faster access to it. if <code>acctInfo</code> is empty, 
	 * then read it, if <code>acctInfo</code> is not empty, do nothing.
	 */
	private static int readAcctInfo() {
		if(acctInfo.isEmpty()) {
			String sql = "select * from rad_accounts";
//			AcctUtil.log("readAcctInfo: "+sql);
			PrepareSQL psql = new PrepareSQL(sql);
			Cursor csr;
			try {
				csr = DataSetBean.getCursor(psql.getSQL());
			} catch (Exception e) {
//				AcctUtil.log("readAcctInfo: Error occured " +
//						"while fetching info from rad_accounts!");
				return -1;
			}
			int rows = csr.getRecordSize();
			acctInfo.ensureCapacity(rows);
			acctDevInfo.ensureCapacity(rows);
			
			for(int i=0; i<rows; i++) {
				acctInfo.add(csr.getRecord(i));
				
				Map record = csr.getRecord(i);
				String user = (String)record.get("user_name");
				String sql_dev = "select distinct loopback_ip from rad_acc_device " +
							"where user_name='" + user +"'";
//				AcctUtil.log("readAcctInfo: " + sql_dev);
				PrepareSQL psql2 = new PrepareSQL(sql_dev);
				Cursor csr_dev;
				try {
					csr_dev = DataSetBean.getCursor(psql2.getSQL());
				} catch (Exception e) {
//					AcctUtil.log("readAcctInfo: Error occured when fetch info from rad_acc_device!");
					return -1;
				}
				int rows_dev = csr_dev.getRecordSize();
				
				ArrayList tmpDevList = new ArrayList(rows_dev+1);
				tmpDevList.add(user);
				for(int j=1; j<=rows_dev; j++) {
					Map dev = csr_dev.getRecord(j-1);
					tmpDevList.add((String)dev.get("loopback_ip"));
				}
				
				acctDevInfo.add(tmpDevList);
			}
		} else {
//			AcctUtil.log("readAcctInfo: The acctInfo is not empty, do nothing.");
		}
		acctInfo.trimToSize();
		acctDevInfo.trimToSize();
		return acctInfo.size();
	}
	
	/**
	 * add a new accounts into <code>acctInfo</code>, this action make sure accounts
	 * information in the memery synchronized with that in the database.
	 * @return
	 * 		always return 0
	 */
	private synchronized int addAcct2AcctInfo() {
		acctInfo.ensureCapacity(acctInfo.size()+1);
		HashMap newAcct = new HashMap(9);
		newAcct.put("user_name",	userName);
		newAcct.put("user_pwd",		pwd);
		newAcct.put("creator",		creator);
		newAcct.put("create_date",	createDate);
		newAcct.put("effect_date",	effectDate);
		newAcct.put("expire_date",	expireDate);
		newAcct.put("city",			city);
		newAcct.put("memo",			memo);
		acctInfo.add(newAcct);
		return 0;
	}
	
	/**
	 * add a new device list into <code>acctDevInfo</code>, this action make sure accounts
	 * information in the memery synchronized with that in the database.
	 * @return
	 * 		always return 0
	 */
	private synchronized int addNewDevList2AcctDevInfo() {
		acctDevInfo.ensureCapacity(acctDevInfo.size()+1);
		ArrayList newDevList = new ArrayList(devices.size()+1);
		newDevList.add(userName);
		for(int i=0; i<devices.size(); i++) {
			newDevList.add(devices.get(i));
		}
		acctDevInfo.add(newDevList);
		return 0;
	}
	
	
	/**
	 * acctInfo
	 * @return
	 * 		ArrayList <code>acctInfo</code>
	 */
	public static ArrayList getAcctInfo() {
		return acctInfo;
	}
	
	/**
	 * acctDevInfo
	 * @return
	 * 		ArrayList <code>acctDevInfo</code>
	 */
	public static ArrayList getAcctDevInfo() {
		return acctDevInfo;
	}
	
	/**
	 * get the devices which the user authorized to logon
	 * @return ArrayList getDevices
	 */
	public ArrayList getDevices() {
		return devices;
	}
	
	/**
	 * set the devices which the user authorized to logon
	 * @param devices
	 * @return always return 0
	 */
	public int setDevice(ArrayList devices) {
		this.devices = devices;
		return 0;
	}
	
	public String getUserName() {
		return userName;
	}
	public int setUserName(String userName) {
		this.userName = userName;
		return 0;
	}

	public String getPwd() {
		return pwd;
	}
	public int setPwd(String pwd) {
		this.pwd = pwd;
		return 0;
	}	
	public String getCreator() {
		return creator;
	}

	public int setCreator(String creator) {
		this.creator = creator;
		return 0;
	}	
	
	public String getCreateDate() {
		return createDate;
	}
	
	/*
	 * User should not modify the createdate
	 */
//	public int setCreateDate(Date createDate) {
//		this.createDate = createDate;
//		return 0;
//	}
	
	public String getEffectDate() {
		return effectDate;
	}
	public int setEffectDate(String effectDate) {
		this.effectDate = effectDate;
		return 0;
	}	
	
	public String getExpireDate() {
		return expireDate;
	}
	public int setExpireDate(String expireDate) {
		this.expireDate = expireDate;
		return 0;
	}	
	
	public String getCity() {
		return city;
	}
	public int setCity(String city) {
		this.city = city;
		return 0;
	}	
	
	public String getMemo() {
		return memo;
	}
	public int setMemo(String memo) {
		this.memo = memo;
		return 0;
	}
}
