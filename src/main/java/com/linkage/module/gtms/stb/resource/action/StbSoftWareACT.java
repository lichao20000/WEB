
package com.linkage.module.gtms.stb.resource.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.StbSoftWareBIO;
import com.linkage.module.gtms.stb.share.serv.ShareDeviceQueryBIO;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.gwms.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zzs (Ailk No.78987)
 * @version 1.0
 * @since 2018-10-18
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept. 机顶盒批量软件升级
 */
public class StbSoftWareACT extends ActionSupport implements SessionAware
{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 5112590616842029627L;
	/** log */
	private static Logger logger = LoggerFactory.getLogger(StbSoftWareACT.class);
	
	private String deviceIds;
	private String ajax;
	private StbSoftWareBIO bio;
	// session
	private Map session;
	private String gwShare_queryType;
	private String softStrategy_type;
	private String taskId;
	private String param;
	// 任务描述
	private String taskDesc;
	private ShareDeviceQueryBIO shareDeviceQueryBIO;

	
	/**
	 * 设备批量升级
	 * 
	 * @author zzs
	 * @date 2018-10-18
	 * @return String
	 */
	public String batchUp()
	{
		logger.debug("StbbatchUp()");
		try
		{
			UserRes curUser = (UserRes) session.get("curUser");
			String isOpenSoftUpModule = LipossGlobals
					.getLipossProperty("stbSoftUpModule");
			/**
			 * isOpenSoftUpModule为机顶盒软件升级执行方式 0：web直接入策略 1：单台配置模块入策略 2：拆分配置模块入策略
			 * 目前机顶盒只实现了2，拆分配置模块入策略，其他暂不支持
			 */
			if ("2".equals(isOpenSoftUpModule))
			{
				batchUp4fen(param, curUser, "4", taskDesc, softStrategy_type);
			}
			else
			{
				logger.warn("机顶盒软件升级暂不支持单台配置模块入策略！，请配置为其它项");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("Exception=" + e.getMessage());
			return "result";
		}
		return "result";
	}

	public void batchUp4fen(String param, UserRes curUser, String gw_type,
			String taskDesc, String strategyType)
	{
		logger.warn("支持分配置模块的批量软件升级batchUp4fen({},{},{})",
				new Object[] { param.toString(), gw_type, taskDesc });
		PreProcessInterface softUpgradeCorba = CreateObjectFactory
				.createPreProcess(gw_type);
		// 用户id
		long accoid = curUser.getUser().getId();
		long taskid = Math.round(Math.random() * 100000L);
		taskId = StringUtil.getStringValue(taskid);
		/** 入任务表 **/
		// 属地id
		long city_Id = curUser.getAreaId();
		if (param.isEmpty())
		{
			int iec = bio.addToTask(taskid, "-1", city_Id, "-1", strategyType, accoid,
					"-1", "0", "0", taskDesc, "");
			if (iec < 0)
			{
				logger.warn("入任务表失败，请检查");
				return;
			}
		}
		else
		{
			String[] _param = param.split("\\|");
			String matchSQL = _param[10];
			int iec1 = bio.addToTask(taskid, "-1", city_Id, "-1", strategyType, accoid,
					"-1", "0", "0", taskDesc, matchSQL);
			if (iec1 < 0)
			{
				logger.warn("入任务表失败，请检查");
				return;
			}
		}
		if (!"0".equals(deviceIds))
		{
			String[] deviceId_array = deviceIds.split(",");
			String[] paramArr = new String[3];
			paramArr[0] = "softup";
			paramArr[1] = softStrategy_type;
			paramArr[2] = taskId;
			logger.warn("deviceIds={},调用后台软件升级corba接口", deviceIds);
			softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr);
		}
		// 调用后台corba接口时，小于100的情况，传deviceID数组，否则传SQL
		else
		{
			if (StringUtil.IsEmpty(param))
			{
				logger.warn("param为空");
			}
			String[] _param = param.split("\\|");
			String matchSQL = _param[10];
			long total = StringUtil.getLongValue(_param[11]);
			if (total < 100)
			{
				logger.warn("调用后台软件升级corba接口：数量小于100，传deviceID数组");
				logger.warn("total"+total);
				PrepareSQL pSQL = new PrepareSQL();
				pSQL.setSQL(matchSQL.replace("[", "\'"));
				List list = DBOperation.getRecords(pSQL.getSQL());
				String[] deviceId_array = new String[list.size()];
				String[] paramArr = new String[3];
				for (int i = 0; i < list.size(); i++)
				{
					Map map = (Map) list.get(i);
					deviceId_array[i] = StringUtil.getStringValue(map.get("device_id"));
				}
				paramArr[0] = "softup";
				paramArr[1] = softStrategy_type;
				paramArr[2] = taskId;
				softUpgradeCorba.processDeviceStrategy(deviceId_array, "5", paramArr);
				logger.warn("调用配置模块corba完成！设备id:" + deviceId_array
						+ " softStrategy_type:" + softStrategy_type + " 任务ID" + taskId);
			}
			else
			{
				logger.warn("调用后台软件升级corba接口：数量大于100，传sql数组");
				String[] paramArr = new String[3];
				paramArr[0] = "softup";
				paramArr[1] = softStrategy_type;
				paramArr[2] = taskId;
				softUpgradeCorba.processDeviceStrategy(
						new String[] { matchSQL.replace("[", "\'") }, "5", paramArr);
			}
		}
	}

	/**
	 * @return the deviceIds
	 */
	public String getDeviceIds()
	{
		return deviceIds;
	}

	/**
	 * @param deviceIds
	 *            the deviceIds to set
	 */
	public void setDeviceIds(String deviceIds)
	{
		this.deviceIds = deviceIds;
	}

	/**
	 * @return the ajax
	 */
	public String getAjax()
	{
		return ajax;
	}

	/**
	 * @param ajax
	 *            the ajax to set
	 */
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	/**
	 * @return the bio
	 */
	public StbSoftWareBIO getBio()
	{
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(StbSoftWareBIO bio)
	{
		this.bio = bio;
	}

	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * @return the gwShare_queryType
	 */
	public String getGwShare_queryType()
	{
		return gwShare_queryType;
	}

	/**
	 * @param gwShare_queryType
	 *            the gwShare_queryType to set
	 */
	public void setGwShare_queryType(String gwShare_queryType)
	{
		this.gwShare_queryType = gwShare_queryType;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId()
	{
		return taskId;
	}

	/**
	 * @param taskId
	 *            the taskId to set
	 */
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	/**
	 * @return the param
	 */
	public String getParam()
	{
		return param;
	}

	/**
	 * @param param
	 *            the param to set
	 */
	public void setParam(String param)
	{
		this.param = param;
	}

	/**
	 * @return the shareDeviceQueryBIO
	 */
	public ShareDeviceQueryBIO getShareDeviceQueryBIO()
	{
		return shareDeviceQueryBIO;
	}

	/**
	 * @param shareDeviceQueryBIO
	 *            the shareDeviceQueryBIO to set
	 */
	public void setShareDeviceQueryBIO(ShareDeviceQueryBIO shareDeviceQueryBIO)
	{
		this.shareDeviceQueryBIO = shareDeviceQueryBIO;
	}

	/**
	 * @return the softStrategy_type
	 */
	public String getSoftStrategy_type()
	{
		return softStrategy_type;
	}

	/**
	 * @param softStrategy_type
	 *            the softStrategy_type to set
	 */
	public void setSoftStrategy_type(String softStrategy_type)
	{
		this.softStrategy_type = softStrategy_type;
	}

	/**
	 * @return the taskDesc
	 */
	public String getTaskDesc()
	{
		return taskDesc;
	}

	/**
	 * @param taskDesc
	 *            the taskDesc to set
	 */
	public void setTaskDesc(String taskDesc)
	{
		this.taskDesc = taskDesc;
	}
}
