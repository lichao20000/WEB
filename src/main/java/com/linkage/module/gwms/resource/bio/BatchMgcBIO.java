
package com.linkage.module.gwms.resource.bio;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import PreProcess.UserInfo;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.dao.BatchMgcDAO;
import com.linkage.module.gwms.resource.obj.BatchMgcBean;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.PreProcessInterface;

/**
 * @author fangchao (Ailk No.)
 * @version 1.0
 * @since 2013-9-26
 * @category com.linkage.module.gwms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchMgcBIO
{

	private static Logger logger = LoggerFactory.getLogger(BatchMgcBIO.class);
	private static Object lock = new Object();
	private BatchMgcDAO batchMgcDAO;
	// private Map<String, String> accessTypeMap = null;
	private PreProcessInterface preProcessCorba = null;

	public void analizeMgc(BatchMgcBean mgcBean)
	{
		String validResult = validMgcBean(mgcBean);
		if (validResult != null)
		{
			mgcBean.setFailedCause(validResult);
			return;
		}
		long sipId = getSipId(mgcBean);
		long userId = batchMgcDAO.getExistVoipUserId(mgcBean);
		if (userId > 0)
		{
			// exists
			batchMgcDAO.updateVoipSipId(userId, sipId, mgcBean);
			batchMgcDAO.updateServOpenStatus(userId);
		}
		else
		{
			mgcBean.setFailedCause("逻辑ID[" + mgcBean.getUsername() + "]和电话号码["
					+ mgcBean.getVoipPhone() + "]不存在于用户信息中");
		}
		Map<String, Object> bindDeviceMap = batchMgcDAO.getUserBindDevice(mgcBean);
		if (bindDeviceMap != null)
		{
			// do bind device
			processServiceInterface(bindDeviceMap);
		}
	}

	private String validMgcBean(BatchMgcBean mgcBean)
	{
		if (StringUtil.IsEmpty(mgcBean.getUsername()))
		{
			return "逻辑ID为空";
		}
		if (StringUtil.IsEmpty(mgcBean.getMainMgcIp()))
		{
			return "主MGC地址为空";
		}
		if (StringUtil.IsEmpty(mgcBean.getMainMgcPort()))
		{
			return "主MGC端口为空";
		}
		if (StringUtil.IsEmpty(mgcBean.getStandMgcIp()))
		{
			return "备MGC地址为空";
		}
		if (StringUtil.IsEmpty(mgcBean.getStandMgcPort()))
		{
			return "备MGC端口为空";
		}
		if (StringUtil.IsEmpty(mgcBean.getVoipPhone()))
		{
			return "电话号码为空";
		}
		// valid city
		// String cityId = CityDAO.getCityId(mgcBean.getCityName());
		// if (StringUtil.IsEmpty(cityId))
		// {
		// return "属地[" + mgcBean.getCityName() + "]为空或非法";
		// }
		// mgcBean.setCityId(cityId);
		// // valid access type
		// String accessTypeId = getAccessTypeId(mgcBean.getAccessType());
		// if (StringUtil.IsEmpty(accessTypeId))
		// {
		// accessTypeId = "3";
		// // return "接入方式[" + mgcBean.getAccessType() + "]为空或非法";
		// }
		// mgcBean.setAccessTypeId(accessTypeId);
		// if ("企业".equals(mgcBean.getUserType()))
		// {
		// mgcBean.setUserTypeCode("2");
		// }
		// else
		// {
		// mgcBean.setUserTypeCode("1");
		// }
		return null;
	}

	/**
	 * 获取SIP表主键ID，如果存在，则直接返回，如果不存在，则新增后再返回
	 * 
	 * @param mgcBean
	 * @return
	 */
	private long getSipId(BatchMgcBean mgcBean)
	{
		long sipId = batchMgcDAO.getExistSipId(mgcBean);
		if (sipId > 0)
		{
			// exist
			return sipId;
		}
		synchronized (lock)
		{
			sipId = batchMgcDAO.getNextSipId();
			batchMgcDAO.addSip(sipId, mgcBean);
			return sipId;
		}
	}

	// private String getAccessTypeId(String accessType)
	// {
	// if (StringUtil.IsEmpty(accessType))
	// {
	// return null;
	// }
	// if (accessTypeMap == null)
	// {
	// accessTypeMap = batchMgcDAO.getAccessType();
	// }
	// return accessTypeMap.get(accessType);
	// }
	private void processServiceInterface(Map<String, Object> bindDeviceMap)
	{
		logger.info("send processServiceInterface, device[{}]", bindDeviceMap);
		UserInfo[] userInfo = new UserInfo[1];
		userInfo[0] = new UserInfo();
		userInfo[0].deviceId = StringUtil.getStringValue(bindDeviceMap, "device_id");
		userInfo[0].oui = StringUtil.getStringValue(bindDeviceMap, "oui");
		userInfo[0].deviceSn = StringUtil.getStringValue(bindDeviceMap,
				"device_serialnumber");
		userInfo[0].gatherId = "gatherId";
		userInfo[0].userId = StringUtil.getStringValue(bindDeviceMap, "user_id");
		userInfo[0].servTypeId = "14";
		userInfo[0].operTypeId = "1";
		if (preProcessCorba == null)
		{
			// default ITMS
			preProcessCorba = CreateObjectFactory.createPreProcess(Global.GW_TYPE_ITMS);
		}
		preProcessCorba.processServiceInterface(userInfo);
	}

	public void setBatchMgcDAO(BatchMgcDAO batchMgcDAO)
	{
		this.batchMgcDAO = batchMgcDAO;
	}
}
