
package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.DeviceTypeVoiceQueryDAO;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-10-29
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class DeviceTypeVoiceQueryBIO
{

	private static Logger logger = LoggerFactory
			.getLogger(DeviceTypeVoiceQueryBIO.class);
	private DeviceTypeVoiceQueryDAO dao;
	
	private Map<String,String> modelMap;

	/**
	 * 查询厂商信息
	 * 
	 * @return
	 */
	public Map<String, String> getVendor()
	{
		return dao.getVendor();
	}

	/**
	 * 设备型号统计语音端口信息
	 * 
	 * @param vendorId
	 *            厂商
	 * @param modelId
	 *            设备型号
	 * @param start_time
	 *            开始时间
	 * @param end_time
	 *            结束时间
	 * @return
	 */
	public List<Map> deviceVoiceInfo(String vendorId, String modelId, String start_time,
			String end_time)
	{
		List<Map> list = new ArrayList<Map>();
		String models = getModleId(vendorId, modelId);
		List vendorList = dao.getModel(vendorId,modelId);
		//过滤设备型号
		
		logger.warn("设备型号列表："+models);
		// 语音端口1，统计信息
		Map<String, String> lineOne = dao.getDeviceOneInfo(models,
				start_time, end_time);
		// 语音端口1，未其用户信息
		Map<String, String> lineNoOne = dao.getDeviceOneNoInfo(models, start_time, end_time);
		// 语音端口2，统计信息
		Map<String, String> linetwo = dao.getDeviceTwoInfo(models,start_time, end_time);
		// 语音端口2，未其用户信息
		Map<String, String> lineNotwo = dao.getDeviceTwoNoInfo(models, start_time, end_time);
		// 语音端口1,2，同时未启用信息
		Map<String, String> lineNoonetwo = dao.getDeviceOneTwoNoInfo(models, start_time, end_time);
		
		//设备型号列表
		modelMap = dao.getDeviceModel();
		// 语音端口1，统计信息
		long lineOneNum = 0;
		// 语音端口1，未启用统计信息
		long lineOneNoNum = 0;
		// 语音端口1，统计信息
		long lineTwoNum = 0;
		// 语音端口1，未启用统计信息
		long lineTwoNoNum = 0;
		// 语音端口1,2，同时为未启用信息
		long lineOneTwoNoNum = 0;
		for (int i = 0; i < vendorList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			// 语音端口1，统计信息
			lineOneNum = 0;
			// 语音端口1，未启用统计信息
			lineOneNoNum = 0;
			// 语音端口1，统计信息
			lineTwoNum = 0;
			// 语音端口1，未启用统计信息
			lineTwoNoNum = 0;
			// 语音端口1,2，同时为未启用信息
			lineOneTwoNoNum = 0;
			String device_model = StringUtil.getStringValue(vendorList.get(i));
			if (!StringUtil.IsEmpty(device_model))
			{
				lineOneNum += StringUtil.getLongValue(lineOne.get(device_model));
				lineOneNoNum += StringUtil.getLongValue(lineNoOne.get(device_model));
				lineTwoNum += StringUtil.getLongValue(linetwo.get(device_model));
				lineTwoNoNum += StringUtil.getLongValue(lineNotwo.get(device_model));
				lineOneTwoNoNum += StringUtil
						.getLongValue(lineNoonetwo.get(device_model));
			}
			String model_id = StringUtil.getStringValue(vendorList.get(i));
			String modelType = modelMap.get(model_id);
			tmap.put("model_id", model_id);
			tmap.put("modelType", modelType);
			tmap.put("lineOneNum", lineOneNum);
			tmap.put("lineOneNoNum", lineOneNoNum);
			tmap.put("lineTwoNum", lineTwoNum);
			tmap.put("lineTwoNoNum", lineTwoNoNum);
			tmap.put("lineOneTwoNoNum", lineOneTwoNoNum);
			list.add(tmap);
		}
		return list;
	}

	public List<Map> voiceDeviceQueryInfo(String vendorId, String modelId,
			String start_time, String end_time, String numInfo, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("voiceDeviceQueryInfo()");
		return dao.voiceDeviceQueryInfo(getModleId(vendorId, modelId), start_time,
				end_time,numInfo, curPage_splitPage, num_splitPage);
	}

	public int countVoiceDeviceQueryInfo(String vendorId, String modelId,
			String start_time, String end_time, String numInfo, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("countVoiceDeviceQueryInfo()");
		return dao.countVoiceDeviceQueryInfo(getModleId(vendorId, modelId), start_time,
				end_time, numInfo, curPage_splitPage, num_splitPage);
	}
	
	public List<Map> voiceDeviceQueryExcel(String vendorId, String modelId,
			String start_time, String end_time, String numInfo)
	{
		logger.debug("voiceDeviceQueryExcel()");
		return dao.voiceDeviceQueryExcel(getModleId(vendorId, modelId), start_time, end_time, numInfo);
	}
	

	private String getModleId(String vendorId, String modelId)
	{
		
		StringBuffer edtionIdBuffer = new StringBuffer();
		// 设备型号
		if (!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId) && !"null".equals(modelId))
		{
			edtionIdBuffer.append("('").append(modelId).append("')");
		}
		else
		{
			List<String> vendorList = dao.getModel(vendorId,"-1");
			edtionIdBuffer.append("(");
			// 同一个设备版本小于20的版本id
			for (int i = 0; i < vendorList.size(); i++)
			{
				if (null == vendorList || vendorList.size() == 0)
				{
				}
				else
				{
					String device_model_id = StringUtil.getStringValue(vendorList.get(i));
					if (i + 1 == vendorList.size())
					{
						edtionIdBuffer.append("'" + device_model_id+"'");
					}
					else
					{
						edtionIdBuffer.append("'" + device_model_id + "',");
					}
				}
			}
			edtionIdBuffer.append(" )");
		}
		return edtionIdBuffer.toString();
	}

	public DeviceTypeVoiceQueryDAO getDao()
	{
		return dao;
	}

	public void setDao(DeviceTypeVoiceQueryDAO dao)
	{
		this.dao = dao;
	}
}
