
package com.linkage.module.gwms.resource.act;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.bio.ImportLoidBIO;

/**
 * @author wuchao(工号) Tel:1
 * @version 1.0
 * @since 2011-9-21 下午02:21:47
 * @category com.linkage.module.gwms.resource.act
 * @copyright 南京联创科技 网管科技部
 */
public class ImportLoidACT implements ServletRequestAware, SessionAware
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(ImportLoidACT.class);
	// request取登陆帐号使用
	private HttpServletRequest request;
	private ImportLoidBIO importLoidBIO;
	// 导入文件,根据此文件解析用户账号
	private File file;
	// 导入的文件中，查询异常数据返回deviceId
	private List deviceExceptionList = new ArrayList();
	// 导入的文件中，查询异常数据loid的字符串
	private String deviceExceptionStr = "";
	// 导入的文件中，查询正常数据返回的deviceId
	private List device_idNormalList = new ArrayList();
	// 导入的文件中，查询正常device_id组成的字符串
	private String device_idNormalStr;
	private List<String> mapIdList = new ArrayList();
	private String map_id;
	private List<String> deviceIdList = new ArrayList();
	private List<String> device_id_exNormalList = new ArrayList();
	// 判断是否能下发 1表示下发
	private String isFword = "0";
	// 导入的文件中,loid总个数
	private int allLoid;
	// 导入的文件中,loid正常的个数
	private int normalLoid;
	private Map session;
	private String msg;
	private List<String> mapName = new ArrayList();
	private List<Map<String, String>> digitMapList = new ArrayList<Map<String, String>>();
	// 配置下发任务的名字
	private String task_name;
	private String ajax;

	public String getTask_name()
	{
		return task_name;
	}

	public void setTask_name(String taskName)
	{
		try
		{
			this.task_name = java.net.URLDecoder.decode(taskName, "UTF-8");
		}
		catch (Exception e)
		{
			this.task_name = taskName;
		}
	}

	public String execute()
	{
		return "success";
	}

	/**
	 * @category
	 
	 
	 
	 
	 */
	public String forward()
	{
		logger.warn("map_id为" + map_id);
		logger.warn("device_idNormalStr为" + device_idNormalStr);
		logger.warn("task_name为" + task_name);
		if (importLoidBIO.forward(map_id, device_idNormalStr, task_name))
		{
			ajax = "成功";
		}
		else
		{
			ajax = "失败";
		}
		return "ajax";
	}

	/**
	 * @category 批量导入用户账号查询设备,查询的设备主要分为两种， 一：异常设备，例如库中不存在的数据； 二：正常设备；
	 */
	public String getDeviceByImportLoid()
	{
		String queryCity = null;
		isFword = "1";
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		queryCity = curUser.getCityId();
		// 导入的文件中，前100条数据
		List<String> loidList;
		if (Global.NXDX.equals(Global.instAreaShortName))
		{
			loidList = importLoidBIO.getDeviceByImportLoid4Nx(this.file);
			logger.warn("需要下发数图的loid：({})", loidList);
		}
		else
		{
			loidList = importLoidBIO.getDeviceByImportLoid(this.file);
		}
		allLoid = loidList.size();
		if (loidList == null || loidList.size() == 0)
		{
			return "deviceByImport";
		}
		List<Map<String, String>> list = importLoidBIO.getResult(queryCity, loidList);
		logger.warn("查询到的结果" + list.size());
		if (list.size() == 0)
		{
			deviceExceptionStr = StringUtils.weave(loidList);
		}
		else
		{
			for (int i = 0; i < list.size(); i++)
			{
				String temp = StringUtil.getStringValue(((Map) (list.get(i)))
						.get("device_id"));
				String device_id_ex = StringUtil.getStringValue(((Map) (list.get(i)))
						.get("device_id_ex"));
				device_id_exNormalList.add(device_id_ex);
				device_idNormalList.add(temp);
				device_idNormalStr = StringUtils.weave(device_idNormalList);
			}
			for (String temp : loidList)
			{
				if (!device_id_exNormalList.contains(temp))
				{
					deviceExceptionList.add(temp);
				}
			}
			// 异常的loid
			deviceExceptionStr = StringUtils.weave(deviceExceptionList);
			digitMapList = importLoidBIO.getDigitMap(queryCity, list);
			logger.warn("异常的loid：" + deviceExceptionStr);
			normalLoid = list.size();
			logger.warn("正常的：" + normalLoid);
			logger.warn("所有的：" + allLoid);
		}
		return "deviceByImport";
	}

	public List<String> getMapIdList()
	{
		return mapIdList;
	}

	public void setMapIdList(List<String> mapIdList)
	{
		this.mapIdList = mapIdList;
	}

	public String getMap_id()
	{
		return map_id;
	}

	public void setMap_id(String mapId)
	{
		map_id = mapId;
	}

	public List<String> getDeviceIdList()
	{
		return deviceIdList;
	}

	public void setDeviceIdList(List<String> deviceIdList)
	{
		this.deviceIdList = deviceIdList;
	}

	public List<String> getMapName()
	{
		return mapName;
	}

	public void setMapName(List<String> mapName)
	{
		this.mapName = mapName;
	}

	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}

	public ImportLoidBIO getImportLoidBIO()
	{
		return importLoidBIO;
	}

	public void setImportLoidBIO(ImportLoidBIO importLoidBIO)
	{
		this.importLoidBIO = importLoidBIO;
	}

	public File getFile()
	{
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
	}

	public List getDeviceExceptionList()
	{
		return deviceExceptionList;
	}

	public void setDeviceExceptionList(List deviceExceptionList)
	{
		this.deviceExceptionList = deviceExceptionList;
	}

	public int getAllLoid()
	{
		return allLoid;
	}

	public void setAllLoid(int allLoid)
	{
		this.allLoid = allLoid;
	}

	public int getNormalLoid()
	{
		return normalLoid;
	}

	public void setNormalLoid(int normalLoid)
	{
		this.normalLoid = normalLoid;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public String getDeviceExceptionStr()
	{
		return deviceExceptionStr;
	}

	public void setDeviceExceptionStr(String deviceExceptionStr)
	{
		this.deviceExceptionStr = deviceExceptionStr;
	}

	public List getDevice_idNormalList()
	{
		return device_idNormalList;
	}

	public void setDevice_idNormalList(List deviceIdNormalList)
	{
		device_idNormalList = deviceIdNormalList;
	}

	public String getDevice_idNormalStr()
	{
		return device_idNormalStr;
	}

	public void setDevice_idNormalStr(String deviceIdNormalStr)
	{
		device_idNormalStr = deviceIdNormalStr;
	}

	public String getIsFword()
	{
		return isFword;
	}

	public void setIsFword(String isFword)
	{
		this.isFword = isFword;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public Map getSession()
	{
		return session;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public List<Map<String, String>> getDigitMapList()
	{
		return digitMapList;
	}

	public void setDigitMapList(List<Map<String, String>> digitMapList)
	{
		this.digitMapList = digitMapList;
	}

	public List<String> getDevice_id_exNormalList()
	{
		return device_id_exNormalList;
	}

	public void setDevice_id_exNormalList(List<String> deviceIdExNormalList)
	{
		device_id_exNormalList = deviceIdExNormalList;
	}
}
