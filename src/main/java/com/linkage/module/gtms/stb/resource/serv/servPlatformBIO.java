package com.linkage.module.gtms.stb.resource.serv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.resource.dao.servPlatformDAO;


/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-1-5
 * @category com.linkage.module.gtms.stb.resource.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class servPlatformBIO
{
	private static Logger logger = LoggerFactory.getLogger(servPlatformBIO.class);
	private servPlatformDAO dao;
	/**
	 * 查询基本信息
	 * @param platformname
	 * @return
	 */
	public List<Map<String,String>> querylist(String platformname)
	{
		return dao.querylist(platformname);
	}
	/**
	 * 添加信息
	 * @param platformid
	 * @param platformname
	 * @param remark
	 * @param operator
	 * @return
	 */
	public String addservPlatform(String platformid,String platformname,String remark,String operator)
	{
		String infor="";
		int str=dao.addservPlatform(platformid, platformname, remark, operator);
		if(!StringUtil.IsEmpty(String.valueOf(str))&&str>0)
		{
			infor="1";
		}else
		{
			infor="-1";
		}
		return infor;
	}
	/**
	 * 修改信息
	 * @param platformid
	 * @param platformname
	 * @param remark
	 * @param operator
	 * @return
	 */
	public String updateservPlatform(String platformid ,String platformname,String remark,String operator)
	{
		String infor="";
		int str=dao.updateservPlatform(platformid, platformname, remark, operator);
		if(!StringUtil.IsEmpty(String.valueOf(str))&&str>0)
		{
			infor="1";
		}else
		{
			infor="-1";
		}
		return infor;
	}
	/**
	 * 查询id是否存在
	 * @param platformid
	 * @return
	 */
	public String queryplatformid(String platformid)
	{
		String infor="";
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		list= dao.queryplatformid(platformid);
		if(list!=null&&list.size()>0)
		{
			infor="-1";
		}else
		{
			infor="1";
		}
		return infor;
	}
	/**
	 * 删除信息
	 * @param platformid
	 * @return
	 */
	public String  deleteservPlatform(String platformid)
	{
		String infor="";
		int str=dao.deleteservPlatform(platformid);
		if(!StringUtil.IsEmpty(String.valueOf(str))&&str>0)
		{
			infor="1";
		}else
		{
			infor="-1";
		}
		return infor;
	}
	
	public List<Map<String,String>> queryplatformname(String platformid)
	{
		return dao.queryplatformname(platformid);
	}
	public servPlatformDAO getDao()
	{
		return dao;
	}
	
	public void setDao(servPlatformDAO dao)
	{
		this.dao = dao;
	}
	
}
