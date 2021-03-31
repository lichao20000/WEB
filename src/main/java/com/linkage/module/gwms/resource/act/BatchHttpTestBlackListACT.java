
package com.linkage.module.gwms.resource.act;

import java.util.List;
import java.util.Map;


import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.resource.bio.BatchHttpTestBlackListBIO;
import com.linkage.module.gwms.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zszhao6(Ailk No.78987)
 * @version 1.0
 * @since 2018-7-26
 * @category com.linkage.module.gwms.resource.act
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchHttpTestBlackListACT extends ActionSupport implements SessionAware
{

	/** 序列号 */
	private static final long serialVersionUID = 1L;
	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(BatchHttpTestBlackListACT.class);
	/** 简单查询类型过滤 */
	private String gwShare_queryField = "";
	/** 简单询查询字段 */
	private String gwShare_queryParam = "";
	
	/** 黑名单查询结果 */
	private List taskResultList = null;
	
	/**查询类型：1（简单查询）2（高级查询定制）*/
	private String gwShare_queryType = null;
	
	private String deviceIds;
	
	private String param;
	
	private String gw_type;
	
	private String fileName_st ;
	
	private String gwShare_fileName = null;
	// session
	private Map session;
	
	private String ajax;
	
	private String task_desc;
	
	private String importQueryField;
	
	private List<String> rm_deviceIds;
	
	private BatchHttpTestBlackListBIO bio;
	
	

	/**
	 * 简单查询，根据设备序列号或是宽带账号，查询设备信息（包括是否在黑名单内）
	 * 高级查询定制，根据属地厂商等信息，查询出具体设备信息，用户操作是否添加进黑名单
	 * @return 设备信息
	 */
	public String getDeviceIfo()
	{
		logger.debug("BatchHttpTestBlackListACT--->getDeviceIfo");
			taskResultList = bio.queryDevcieDetailWithBlackList(gwShare_queryField,
					gwShare_queryParam);
			return "deviceDetailWihtBlackLsit";
		
	}

	/**
	 * 添加设备进入黑名单（根据设备id和高级设置）
	 * @return
	 */
	public String setBlackList()
	{
		logger.debug("setBlackList begin()");
		try
		{
			UserRes curUser = (UserRes) session.get("curUser");
			if (true == StringUtil.IsEmpty(deviceIds))
			{
				logger.debug("任务中没有设备");
			}
			long accoid = curUser.getUser().getId();
			insertTask(param, curUser.getAreaId(), accoid, gw_type, task_desc);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception="+e.getMessage());
			return "result";
		}
		return "result";	
	}
	
	/**
	 * 插入黑名单表 文件名不为空则来源为导入，deviceIds不为0来源为直接radio查询，deviceIds也为空则来源为高级查询。
	 * @param param  参数
	 * @param areaId 操作员域id
	 * @param accoid 操作员acc
	 * @param gw_type 系统类型
	 * @param task_desc 任务描述
	 */
	public void insertTask(String param,long areaId,long accoid, String gw_type, String task_desc)
	{
		//本方法要入库三个表（1：黑名单任务表，2：黑名单表，3：黑名单任务详细表）
		logger.warn("insertTask4BlackList开始");
		if(!"0".equals(deviceIds))
		{
			//简单查询，直接传deviceIds
			logger.warn("入黑名单表-设备id列表(来源为直接radio查询,简单查询)");
			String[] deviceId_array = deviceIds.split(",");
			bio.creatHttpTaskBlackListTaskSql(accoid, null, deviceId_array, null, null, task_desc);
		}else if (!StringUtil.IsEmpty(fileName_st))
		{
			//文件导入
			logger.warn("文件导入批量设置黑名单");
			bio.creatHttpTaskBlackListTaskSql(accoid, null, null, fileName_st, null, task_desc);				
		}
		else
		{
			//高级设置
			logger.warn("高级设置添加黑名单");
			if(StringUtil.IsEmpty(param))
			{
				logger.warn("param为空");
			}
			String[] _param = param.split("\\|");
			bio.upDataBlackListByAdvanCedSetting(accoid,_param,task_desc);				
		}
	}
	
	/**
	 * 上传文件ajax
	 * @return "ajax"
	 */
	public String saveUpFile(){
		ajax = bio.saveUpFile(gwShare_fileName);
		return "ajax";
	}
	
	/**
	 * 根据传入的devcieids删除黑名单
	 */
	public void removeBlackList()
	{
		bio.removeBlackListByDeviceId(rm_deviceIds);
	}
	/**
	 * @return the taskResultList
	 */
	public List getTaskResultList()
	{
		return taskResultList;
	}

	/**
	 * @param taskResultList
	 *            the taskResultList to set
	 */
	public void setTaskResultList(List taskResultList)
	{
		this.taskResultList = taskResultList;
	}

	/**
	 * @return the gwShare_queryField
	 */
	public String getGwShare_queryField()
	{
		return gwShare_queryField;
	}

	/**
	 * @param gwShare_queryField
	 *            the gwShare_queryField to set
	 */
	public void setGwShare_queryField(String gwShare_queryField)
	{
		this.gwShare_queryField = gwShare_queryField;
	}

	/**
	 * @return the gwShare_queryParam
	 */
	public String getGwShare_queryParam()
	{
		return gwShare_queryParam;
	}

	/**
	 * @param gwShare_queryParam
	 *            the gwShare_queryParam to set
	 */
	public void setGwShare_queryParam(String gwShare_queryParam)
	{
		this.gwShare_queryParam = gwShare_queryParam;
	}

	
	/**
	 * @return the bio
	 */
	public BatchHttpTestBlackListBIO getBio()
	{
		return bio;
	}

	
	/**
	 * @param bio the bio to set
	 */
	public void setBio(BatchHttpTestBlackListBIO bio)
	{
		this.bio = bio;
	}
	
	/**
	 * @return the gw_type
	 */
	public String getGw_type()
	{
		return gw_type;
	}

	
	/**
	 * @param gw_type the gw_type to set
	 */
	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	
	/**
	 * @return the gwShare_queryType
	 */
	public String getGwShare_queryType()
	{
		return gwShare_queryType;
	}

	
	/**
	 * @param gwShare_queryType the gwShare_queryType to set
	 */
	public void setGwShare_queryType(String gwShare_queryType)
	{
		this.gwShare_queryType = gwShare_queryType;
	}
	
	/**
	 * @return the deviceIds
	 */
	public String getDeviceIds()
	{
		return deviceIds;
	}

	
	/**
	 * @param deviceIds the deviceIds to set
	 */
	public void setDeviceIds(String deviceIds)
	{
		this.deviceIds = deviceIds;
	}

	
	/**
	 * @return the param
	 */
	public String getParam()
	{
		return param;
	}

	
	/**
	 * @param param the param to set
	 */
	public void setParam(String param)
	{
		this.param = param;
	}

	
	/**
	 * @return the fileName_st
	 */
	public String getFileName_st()
	{
		return fileName_st;
	}

	
	/**
	 * @param fileName_st the fileName_st to set
	 */
	public void setFileName_st(String fileName_st)
	{
		this.fileName_st = fileName_st;
	}

	
	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	
	/**
	 * @param session the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	
	/**
	 * @return the task_desc
	 */
	public String getTask_desc()
	{
		return task_desc;
	}

	
	/**
	 * @param task_desc the task_desc to set
	 */
	public void setTask_desc(String task_desc)
	{
		this.task_desc = task_desc;
	}

	
	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		return ajax;
	}

	
	/**
	 * @param ajax the ajax to set
	 */
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	
	/**
	 * @return the gwShare_fileName
	 */
	public String getGwShare_fileName()
	{
		return gwShare_fileName;
	}

	
	/**
	 * @param gwShare_fileName the gwShare_fileName to set
	 */
	public void setGwShare_fileName(String gwShare_fileName)
	{
		this.gwShare_fileName = gwShare_fileName;
	}

	
	/**
	 * @return the importQueryField
	 */
	public String getImportQueryField()
	{
		return importQueryField;
	}

	
	/**
	 * @param importQueryField the importQueryField to set
	 */
	public void setImportQueryField(String importQueryField)
	{
		this.importQueryField = importQueryField;
	}

	
	/**
	 * @return the rm_deviceIds
	 */
	public List getRm_deviceIds()
	{
		return rm_deviceIds;
	}

	
	/**
	 * @param rm_deviceIds the rm_deviceIds to set
	 */
	public void setRm_deviceIds(List rm_deviceIds)
	{
		this.rm_deviceIds = rm_deviceIds;
	}
	
	
}
