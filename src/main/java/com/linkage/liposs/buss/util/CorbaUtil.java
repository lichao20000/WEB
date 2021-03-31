package com.linkage.liposs.buss.util;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;

/**
 * 该类主要提供corba的总线访问功能
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-09-17
 * @category Corba
 * 
 * @version 2.0 增加对单进程和多进程的支持 modify by 陈仲民（5243）
 */
public class CorbaUtil
{
	private JdbcTemplate jt;// spring的jdbc模版
	private static Logger log = LoggerFactory.getLogger(CorbaUtil.class);
	private NamingContext namingContext = null;
	private int default_port = 20000;// 大客户默认corba接口
	/**
	 * orb初始化类，该对象全局保证只有一个。否则会将系统的socket连接占满
	 * 
	 * @return true：
	 */
	private boolean selfInit()
	{
		Properties props = new Properties();
		props.put("org.omg.CORBA.ORBInitialPort", default_port);
		String[] args = null;
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
		PrepareSQL psql = new PrepareSQL("select ior from tab_ior where object_name='NameService' and object_poa='/rootPoa' and object_port=0");
		List<Map> res = jt.queryForList(psql.getSQL());
		if (res.size() == 0)
			{
				log.error("获取corba总线出错,从数据库中查取ior总数为零");
				return false;
			}
		String names = res.get(0).get("ior").toString().trim();
		org.omg.CORBA.Object objRef = orb.string_to_object(names);
		namingContext = NamingContextHelper.narrow(objRef);
		return true;
	}
	/**
	 * 根据采集点和程序的名称获取对应的对象引用（单进程使用）
	 * 
	 * @param gather_id
	 *            采集点
	 * @param ProcessName
	 *            进程的名称
	 * @return corba中的引用对象
	 * @throws Exception
	 *             数据库和corba异常
	 */
	public org.omg.CORBA.Object getProcessObject(String gather_id, String ProcessName)
			throws Exception
	{
		return getProcessObject(gather_id, ProcessName, 0);
	}
	/**
	 * 根据采集点和程序的名称获取对应的对象引用（支持多进程，单进程情况下processNum为0）
	 * 
	 * @param gather_id
	 *            采集点
	 * @param ProcessName
	 *            进程的名称
	 * @param processNum
	 *            进程编号
	 * @return corba中的引用对象
	 * 
	 * @author 陈仲民（5243）
	 * @throws Exception
	 */
	public org.omg.CORBA.Object getProcessObject(String gather_id, String ProcessName,
			int processIndex) throws Exception
	{
		// namingContext为空时初始化
		if (namingContext == null)
			{
				selfInit();
			}
		// 查询对应接口的配置信息
		String poa_name = getPoaName(gather_id, ProcessName, processIndex);
		String object_name = getObjName(gather_id, ProcessName, processIndex);
		// 表中没有配置信息则直接返回空
		if (poa_name == null || object_name == null)
			{
				return null;
			}
		NameComponent path[] =
			{ new NameComponent(poa_name, "PoaName"),
					new NameComponent(object_name, "ObjectName") };
		org.omg.CORBA.Object envObj = namingContext.resolve(path);
		return envObj;
	}
	/**
	 * 根据指定的poa_name和object_name名称取得对应的对象引用（适用于表里没有poa信息的接口）
	 * 
	 * @param poa_name
	 * @param object_name
	 * @return corba中的引用对象
	 * 
	 * @author 陈仲民（5243）
	 * @throws Exception
	 */
	public org.omg.CORBA.Object getProcess_byName(String poa_name, String object_name)
			throws Exception
	{
		// namingContext为空时初始化
		if (namingContext == null)
			{
				selfInit();
			}
		NameComponent path[] =
			{ new NameComponent(poa_name, "PoaName"),
					new NameComponent(object_name, "ObjectName") };
		org.omg.CORBA.Object envObj = namingContext.resolve(path);
		return envObj;
	}
	/**
	 * 查询接口的poa_name
	 * 
	 * @param gather_id
	 *            采集点
	 * @param ProcessName
	 *            进程名
	 * @return 接口的poa_name
	 * 
	 * @author 陈仲民（5243）
	 */
	private String getPoaName(String gather_id, String ProcessName, int processIndex)
	{
		// 初始化
		String poaName = null;
		String getPoa = "select para_context from tab_process_config where gather_id='"
				+ gather_id + "' and process_name='" + ProcessName + "' and location='"
				+ processIndex + "' and para_item='LocalPoaName'";
		log.debug("getPoaName:" + getPoa);
		// 查询进程配置
		PrepareSQL psql = new PrepareSQL(getPoa);
		List<Map> poas = jt.queryForList(psql.getSQL());
		if (poas.size() > 0)
			{
				poaName = poas.get(0).get("para_context").toString();
			}
		return poaName;
	}
	/**
	 * 查询设备的object_name
	 * 
	 * @param gather_id
	 *            采集点
	 * @param ProcessName
	 *            进程名
	 * @return 接口的object_name
	 * 
	 * @author 陈仲民（5243）
	 */
	private String getObjName(String gather_id, String ProcessName, int processIndex)
	{
		// 初始化
		String objName = null;
		String getObjects = "select para_context from tab_process_config where gather_id='"
				+ gather_id
				+ "' and process_name='"
				+ ProcessName
				+ "' and location='"
				+ processIndex + "' and para_item='LocalName'";
		log.debug("getObjName:" + getObjects);
		// 查询进程配置
		PrepareSQL psql = new PrepareSQL(getObjects);
		List<Map> objects = jt.queryForList(psql.getSQL());
		if (objects.size() > 0)
			{
				objName = objects.get(0).get("para_context").toString();
			}
		return objName;
	}
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
}
