
package com.linkage.module.gtms.stb.resource.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.stb.resource.dao.BatchConfigNodeDAO;
import com.linkage.module.gtms.stb.utils.DeviceTypeUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class BatchConfigNodeBIO
{

	private BatchConfigNodeDAO dao;

	public List getBatchConfigNodeTask(int curPage_splitPage, int num_splitPage,
			String queryVaild, String startTime, String endTime, String cityId,
			String taskName)
	{
		return dao.getBatchConfigNodeTask(curPage_splitPage, num_splitPage, queryVaild,
				startTime, endTime, cityId, taskName);
	}

	public int countBatchConfigNodeTask(int curPage_splitPage, int num_splitPage,
			String queryVaild, String startTime, String endTime, String cityId,
			String taskName)
	{
		return dao.countBatchConfigNodeTask(curPage_splitPage, num_splitPage, queryVaild,
				startTime, endTime, cityId, taskName);
	}

	public int updateTaskStatus(String taskId, String status)
	{
		return dao.updateTaskStatus(taskId, status);
	}

	public void deleteTask(String taskId)
	{
		dao.deleteTask(taskId);
	}

	public List getTaskResult(String taskId)
	{
		List reList = new ArrayList();
		List list = dao.getTaskResult(taskId);
		// logger.warn("@@@@@@@list="+list);
		if (null != list && !list.isEmpty())
		{
			Map<String, String> resMap = null;
			Map<String, String> tempMap = null;
			String citName = "";
			String vendorName = "";
			String deviceModelName = "";
			String deviceTypeName = "";
			String result = "";
			for (int i = 0; i < list.size(); i++)
			{
				tempMap = (Map<String, String>) list.get(i);
				resMap = new HashMap<String, String>();
				vendorName = DeviceTypeUtil.getVendorName(StringUtil
						.getStringValue(tempMap.get("vendor_id")));
				deviceModelName = DeviceTypeUtil.getDeviceModel(StringUtil
						.getStringValue(tempMap.get("device_model_id")));
				deviceTypeName = DeviceTypeUtil.getDeviceSoftVersion(StringUtil
						.getStringValue(tempMap.get("devicetype_id")));
				// logger.warn("@@@@@########deviceTypeName="+DeviceTypeUtil.devTypeMap.get("2"));
				// logger.warn("@@@@@########deviceTypeName="+deviceTypeName);
				citName = CityDAO.getCityName(tempMap.get("city_id"));
				resMap.put("citName", citName);
				resMap.put("vendorName", vendorName);
				resMap.put("deviceModelName", deviceModelName);
				resMap.put("deviceTypeName", deviceTypeName);
				resMap.put("deviceSn", tempMap.get("device_serialnumber"));
				resMap.put("servAccount", tempMap.get("serv_account"));
				resMap.put("loopbackIp", tempMap.get("loopback_ip"));
				resMap.put("cpeMac", tempMap.get("cpe_mac"));
				String resultId = StringUtil.getStringValue(tempMap.get("result_id"));
				if ("0".equals(resultId))
				{
					result = "未操作";
				}
				else if ("1".equals(resultId))
				{
					result = "配置更新成功";
				}
				else
				{
					result = "配置更新失败";
				}
				resMap.put("result", result);
				try
				{
					long updateTime = StringUtil.getLongValue(tempMap.get("end_time"));
					DateTimeUtil dt = new DateTimeUtil(updateTime * 1000);
					resMap.put("updateTime", dt.getLongDate());
				}
				catch (NumberFormatException e)
				{
					resMap.put("updateTime", "");
				}
				catch (Exception e)
				{
					resMap.put("updateTime", "");
				}
				reList.add(resMap);
			}
		}
		return reList;
	}

	public Map getTaskDetail(String taskID)
	{
		Map taskMap = dao.getTaskDetail(taskID);
		List verList = dao.getVerList(taskID);
		StringBuffer venBuf = new StringBuffer();
		StringBuffer modelBuf = new StringBuffer();
		StringBuffer typeBuf = new StringBuffer();
		String ven = "";
		String model = "";
		String type = "";
		Set<String> tempDeviceModelSet = new HashSet<String>();
		Set<String> tempDeviceVenSet = new HashSet<String>();
		for (int i = 0; i < verList.size(); i++)
		{
			Map tempMap = (Map) verList.get(i);
			ven = DeviceTypeUtil.getVendorName(StringUtil.getStringValue(tempMap
					.get("vendor_id")));
			tempDeviceVenSet.add(ven);
			model = DeviceTypeUtil.getDeviceModel(StringUtil.getStringValue(tempMap
					.get("device_model_id")));
			tempDeviceModelSet.add(model);
			type = DeviceTypeUtil.getDeviceSoftVersion(StringUtil.getStringValue(tempMap
					.get("devicetype_id")));
			typeBuf.append(type + "   ");
		}
		for (String str : tempDeviceModelSet)
		{
			modelBuf.append(str + "   ");
		}
		
		for (String str : tempDeviceVenSet)
		{
			venBuf.append(str + "   ");
		}
		taskMap.put("vendorName", venBuf);
		taskMap.put("deviceModelName", modelBuf.toString());
		taskMap.put("deviceTypeName", typeBuf.toString());
		taskMap.put("cityName",
				CityDAO.getCityName(String.valueOf(taskMap.get("city_id"))));
		return taskMap;
	}

	public List getIpList(String taskId)
	{
		return dao.getIpList(taskId);
	}

	public List getMacList(String taskId)
	{
		return dao.getMacList(taskId);
	}

	public List getParamList(String taskId)
	{
		return dao.getParaList(taskId);
	}

	public BatchConfigNodeDAO getDao()
	{
		return dao;
	}

	public void setDao(BatchConfigNodeDAO dao)
	{
		this.dao = dao;
	}
}
