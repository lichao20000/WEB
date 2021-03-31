/**
 * 
 */
package com.linkage.module.gtms.stb.resource.serv;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.resource.dao.StbBindProtectDAO;

/**
 * @author songxq
 * @version 1.0
 * @since 2019-9-16 下午3:20:28
 * @category 
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */

public class StbBindProtectBIO
{
	private StbBindProtectDAO dao ;
	
	private static Logger logger = LoggerFactory
			.getLogger(StbBindProtectBIO.class);
	/**
	 * @param userName
	 * @param mac
	 * @param acc_oid
	 * @param remark
	 * @return
	 * 2019-9-16
	
	 */
	public String add(String userName, String mac, String acc_oid, String remark)
	{
		// TODO Auto-generated method stub
		return dao.add(userName,mac,acc_oid,remark);
	}

	/**
	 * @param userName
	 * @param mac
	 * @param num_splitPage 
	 * @param curPage_splitPage 
	 * @return
	 * 2019-9-16
	
	 */
	public List<HashMap<String, String>> query(String userName, String mac, int curPage_splitPage, int num_splitPage)
	{
		// TODO Auto-generated method stub
		return dao.query(userName,mac,curPage_splitPage,num_splitPage);
	}

	/**
	 * @param userName
	 * @param mac
	 * @return
	 * 2019-9-16
	
	 */
	public int hasData(String userName, String mac)
	{
		// TODO Auto-generated method stub
		return dao.hasData(userName,mac);
	}

	
	public StbBindProtectDAO getDao()
	{
		return dao;
	}

	
	public void setDao(StbBindProtectDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * @param userName
	 * @param mac
	 * 2019-9-17
	 * @return 
	
	 */
	public int delete(String userName, String mac)
	{
		// TODO Auto-generated method stub
		return  dao.delete(userName,mac);
	}

	/**
	 * @param userName
	 * @param mac
	 * @param acc_oid
	 * @param remark
	 * @param remarkEdit 
	 * @return
	 * 2019-9-17
	
	 */
	public int update(String userName, String mac, String userNameEdit, String macEdit, String remarkEdit)
	{
		// TODO Auto-generated method stub
		return dao.update(userName,mac,userNameEdit,macEdit,remarkEdit);
	}

	/**
	 * @param ip
	 * @param id
	 * @param ajax
	 * 2019-9-18
	
	 */
	public void insertOperLog(String ip, long id, String ajax,String menuName,String operationContent)
	{
		// TODO Auto-generated method stub
		dao.insertOperLog(ip,id,ajax, menuName, operationContent);
	}

	/**
	 * @param userName
	 * @param mac
	 * @return
	 * 2019-9-25
	
	 */
	public List<HashMap<String, String>> query(String userName, String mac)
	{
		// TODO Auto-generated method stub
		return dao.query(userName, mac);
	}

	/**
	 * @param userName
	 * @param mac
	 * @return
	 * 2019-9-25
	
	 */
	public int queryNum(String userName, String mac)
	{
		// TODO Auto-generated method stub
		return dao.queryNum(userName, mac);
	}

	

	
	

	
}

