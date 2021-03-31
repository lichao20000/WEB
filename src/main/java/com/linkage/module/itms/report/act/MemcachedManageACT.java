
package com.linkage.module.itms.report.act;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.report.bio.MemcachedManageBIO;

/**
 * @author chenzhangjian (72724)
 * @version 1.0
 * @since 2015-8-13
 * @category com.linkage.module.itms.report.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class MemcachedManageACT
{

	private static Logger logger = LoggerFactory.getLogger(MemcachedManageACT.class);
	private MemcachedManageBIO memcachedManageBIO = null;
	private static final String RETURN_INIT = "init";
	private static final String RETURN_AJAX = "ajax";
	private ArrayList<HashMap<String, String>> resultList = null;
	private String key = "";
	private String value = "";
	private String ipaddress = "";
	private String port = "";
	private String ajax = "";

	/**
	 * 功能入口，查询缓存库信息
	 * 
	 * @return
	 */
	public String init()
	{
		logger.debug("MemcachedManageACT==>init()");
		resultList = memcachedManageBIO.queryMemcachedInfos();
		return RETURN_INIT;
	}

	/**
	 * 从缓存库中查询记录
	 * 
	 * @return
	 */
	public String query()
	{
		logger.debug("MemcachedManageACT==>query()");
		ajax = memcachedManageBIO.queryFromMemcached(ipaddress, port, key);
		return RETURN_AJAX;
	}

	/**
	 * 从缓存库中删除记录
	 * 
	 * @return
	 */
	public String del()
	{
		logger.debug("MemcachedManageACT==>del()");
		ajax = memcachedManageBIO.delFromMemcached(ipaddress, port, key);
		return RETURN_AJAX;
	}

	/**
	 * 从缓存库中增加记录
	 * 
	 * @return
	 */
	public String add()
	{
		logger.debug("MemcachedManageACT==>add()");
		ajax = memcachedManageBIO.updToMemcached(ipaddress, port, key, value);
		return RETURN_AJAX;
	}

	/**
	 * 从缓存库中修改记录
	 * 
	 * @return
	 */
	public String upd()
	{
		logger.debug("MemcachedManageACT==>upd()");
		ajax = memcachedManageBIO.updToMemcached(ipaddress, port, key, value);
		return RETURN_AJAX;
	}

	/**
	 * 查询缓存库运行状态数据
	 * 
	 * @return
	 */
	public String stats()
	{
		logger.debug("MemcachedManageACT==>stats()");
		ajax = memcachedManageBIO.getMemcachedStats(ipaddress, port);
		ajax = ajax.replaceAll("\n", "<br/>");
		return RETURN_AJAX;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getIpaddress()
	{
		return ipaddress;
	}

	public void setIpaddress(String ipaddress)
	{
		this.ipaddress = ipaddress;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

	public ArrayList<HashMap<String, String>> getResultList()
	{
		return resultList;
	}

	public void setResultList(ArrayList<HashMap<String, String>> resultList)
	{
		this.resultList = resultList;
	}

	public MemcachedManageBIO getMemcachedManageBIO()
	{
		return memcachedManageBIO;
	}

	public void setMemcachedManageBIO(MemcachedManageBIO memcachedManageBIO)
	{
		this.memcachedManageBIO = memcachedManageBIO;
	}
}
