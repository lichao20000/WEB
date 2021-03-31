
package com.linkage.module.gtms.stb.resource.serv;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.resource.dao.ShowAdverDAO;
import com.linkage.module.gtms.stb.utils.DeviceTypeUtil;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author yinlei3 (73167)
 * @version v4.0.0.23_20150528
 * @since 2015年5月31日
 * @category com.linkage.module.gtms.stb.resource.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class ShowAdverBIO
{

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(ShowAdverBIO.class);
	/** 下发查询用dao */
	private ShowAdverDAO dao;

	/**
	 * 获取厂商集合
	 * 
	 * @return 厂商集合
	 */
	@SuppressWarnings("rawtypes")
	public List getVendorList()
	{
		logger.debug("dao.getVendor({})" + dao.getVendor());
		return dao.getVendor();
	}

	/**
	 * 根据厂商获取型号
	 * 
	 * @param vendorId
	 * @return 型号
	 */
	@SuppressWarnings("rawtypes")
	public String getDeviceModel(String vendorId)
	{
		List list = dao.getDeviceModel(vendorId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			if (i > 0)
				bf.append("#");
			bf.append(map.get("device_model_id"));
			bf.append("$");
			bf.append(map.get("device_model"));
		}
		return bf.toString();
	}

	/**
	 * 根据型号获取版本
	 * 
	 * @param deviceModelId
	 * @return 版本
	 */
	@SuppressWarnings("rawtypes")
	public String getSoftVersion(String deviceModelId)
	{
		List list = dao.getVersionList(deviceModelId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			if (i > 0)
				bf.append("#");
			bf.append(map.get("devicetype_id"));
			bf.append("$");
			bf.append(map.get("softwareversion"));
		}
		return bf.toString();
	}

	/**
	 * 获取下发广告结果
	 * 
	 * @param curPage_splitPage
	 *            当前页
	 * @param num_splitPage
	 *            每页行数
	 * @param vendorId
	 *            厂商ID
	 * @param deviceModelId
	 *            型号ID
	 * @param deviceTypeId
	 *            版本ID
	 * @param loopbackIp
	 *            最近一次上报IP
	 * @param deviceSerialnumber
	 *            设备序列号
	 * @param cpeMac
	 *            mac地址
	 * @param servAccount
	 *            业务账号
	 * @param faultType
	 *            下发状态
	 * @param tsakName
	 *            策略名称
	 * @return 下发广告查询 结果
	 */
	@SuppressWarnings("rawtypes")
	public List getAdverResultList(int curPage_splitPage, int num_splitPage,
			String vendorId, String deviceModelId, String deviceTypeId,
			String loopbackIp, String deviceSerialnumber, String cpeMac,
			String servAccount, String faultType, String taskName)
	{
		return dao.getAdverResultList(curPage_splitPage, num_splitPage, vendorId,
				deviceModelId, deviceTypeId, loopbackIp, deviceSerialnumber, cpeMac,
				servAccount, faultType, taskName);
	}

	/**
	 * 获取定制的总页数
	 * 
	 * @param num_splitPage
	 *            每页行数
	 * @param vendorId
	 *            厂商ID
	 * @param deviceModelId
	 *            型号ID
	 * @param deviceTypeId
	 *            版本ID
	 * @param loopbackIp
	 *            最近一次上报IP
	 * @param deviceSerialnumber
	 *            设备序列号
	 * @param cpeMac
	 *            mac地址
	 * @param servAccount
	 *            业务账号
	 * @param faultType
	 *            下发状态
	 * @param tsakName
	 *            策略名称
	 * @return 页数
	 */
	public int countAdverResultList(int num_splitPage, String vendorId,
			String deviceModelId, String deviceTypeId, String loopbackIp,
			String deviceSerialnumber, String cpeMac, String servAccount,
			String faultType, String taskName)
	{
		return dao.countAdverResultList(num_splitPage, vendorId, deviceModelId,
				deviceTypeId, loopbackIp, deviceSerialnumber, cpeMac, servAccount,
				faultType, taskName);
	}

	/**
	 * 根据任务Id获取详细情报
	 * 
	 * @param taskId
	 *            任务Id
	 * @return 详细情报map
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, String> getTaskDetail(String taskId)
	{
		Map<String, String> taskMap = dao.getTaskDetail(taskId);
		List verList = dao.getVerList(taskId);
		StringBuffer modelBuf = new StringBuffer();
		StringBuffer typeBuf = new StringBuffer();
		String ven = "";
		String model = "";
		String type = "";
		Set<String> tempDeviceModelSet = new HashSet<String>();
		Map tempMap = null;
		for (int i = 0; i < verList.size(); i++)
		{
			tempMap = (Map) verList.get(i);
			ven = DeviceTypeUtil.getVendorName(StringUtil.getStringValue(tempMap,
					"vendor_id"));
			model = DeviceTypeUtil.getDeviceModel(StringUtil.getStringValue(tempMap,
					"device_model_id"));
			tempDeviceModelSet.add(model);
			type = DeviceTypeUtil.getDeviceSoftVersion(StringUtil.getStringValue(tempMap,
					"devicetype_id"));
			// 版本之间"   "分隔
			typeBuf.append(type + "   ");
		}
		for (String str : tempDeviceModelSet)
		{
			// 型号之间"   "分隔
			modelBuf.append(str + "   ");
		}
		taskMap.put("vendorName", ven);
		taskMap.put("deviceModelName", modelBuf.toString());
		taskMap.put("deviceTypeName", typeBuf.toString());
		String webAddress = LipossGlobals.getLipossProperty("attachmentsDir.web");
		String file_path = taskMap.get("file_path");
		String[] arrayStr = new String[6];
		if (!StringUtil.IsEmpty(file_path))
		{
			arrayStr = file_path.split("/");
		}
		if (!StringUtil.IsEmpty(file_path))
		{
			file_path = "http://" + webAddress + arrayStr[3] + "/" + arrayStr[4] + "/"
					+ arrayStr[5];
			taskMap.put("file_path", file_path);
		}
		else
		{
			taskMap.put("file_path", "否");
		}
		return taskMap;
	}

	/**
	 * 获取任务中定制的Ip地址
	 * 
	 * @param taskId
	 *            任务Id
	 * @return IP地址
	 */
	@SuppressWarnings("rawtypes")
	public List getIpList(String taskId)
	{
		return dao.getIpList(taskId);
	}

	/**
	 * 获取任务中定制的mac地址
	 * 
	 * @param taskId任务Id
	 * @return mac地址
	 */
	@SuppressWarnings("rawtypes")
	public List getMacList(String taskId)
	{
		return dao.getMacList(taskId);
	}

	public void setDao(ShowAdverDAO dao)
	{
		this.dao = dao;
	}
}
