/**
 * @(#)UserManager.java 2006-2-9
 *
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.liposs.aaa;

import java.util.ArrayList;

/**
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public interface UserManager {
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
	public int addAccounts(String userName,
							String pwd,
							String creator,
							String effectDate,
							String expireDate,
							String city,
							String demo,
							ArrayList devices);
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
	public int addAccounts(Accounts acct);

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
	public int delAccounts(String username);
	
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
	public int delAccounts(Accounts acct);
	
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
	public int modDevOfAcct(String username, ArrayList newDevList);
	
	/**
	 * 修改一个设备允许登录的帐号
	 * @param acct
	 * 			用户列表
	 * @return
	 * 		未实现
	 */
	public int modAcctOfDev(String devAddr, ArrayList newAcctList);
	
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
	public int modAccounts(Accounts acctOld, Accounts acctNew);	
	
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
	public int modAccounts(String usernameOld, Accounts acctNew);
	
	/**
	 * 获取一个帐号
	 * @param userName
	 * @return 包含用户名的帐号
	 */
	public Accounts getAcctByUser(String userName);	
	
	/**
	 * 获取所有的帐号，不包含账号可以登陆的设备链表
	 * @return 
	 * 		帐号列表，链表中每个元素都是一个Map类型的数据结构，key跟表：rad_accounts中的列名一样。
	 */
	public ArrayList getAllAcct();	
	
	/**
	 * 返回一个设备允许登录的帐号
	 * @param devIp
	 * 			设备的loopback ip
	 * @return 允许登录deviceId所指设备的帐号列表
	 */
	public ArrayList getAcctByDev(String devIp);
	
	/**
	 * 获取一个帐号可以登陆的设备列表
	 * @param username
	 * 			用户名
	 * @return
	 * 		设备列表，找不到时返回null
	 */
	public ArrayList getDevOfAcct(String username);
	
	/**
	 * 获取一个accounts可以登陆的设备列表
	 * @param acct
	 * 			帐号
	 * @return
	 * 		设备列表，找不到时返回null
	 */
	public ArrayList getDevOfAcct(Accounts acct);
	
	/**
	 * fetch the records according to the <code>queryStr</code>
	 * @param queryStr
	 * 		sql statement
	 * @return
	 * 		Array list of records, whose elements are <code>Map</code>
	 * 		keys of element are the same column name as which in the datebase
	 */
	public ArrayList getRecord(String queryStr);
}
