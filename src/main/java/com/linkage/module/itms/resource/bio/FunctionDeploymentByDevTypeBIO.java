package com.linkage.module.itms.resource.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.FunctionDeploymentByDevTypeDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-12-9
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class FunctionDeploymentByDevTypeBIO
{
	private static Logger logger = LoggerFactory.getLogger(FunctionDeploymentByDevTypeBIO.class);
	
	private FunctionDeploymentByDevTypeDAO dao;

	private Map<String,String> modelMap;
	
	
	public List<Map> quertFunctionDeployByDevType(String vendorId, String modelId, String end_time, String gn){
		logger.debug("FunctionDeploymentByDevTypeBIO==>quertFunctionDeployByDevType()");
		List<Map> list = new ArrayList<Map>();
		String models = getModleId(vendorId, modelId);
		List vendorList = dao.getModel(vendorId,modelId);
		//过滤设备型号
		
		logger.warn("设备型号列表："+models);
		// 语音端口1，统计信息
		Map<String, String> lineOne = dao.quertFunctionDeployByDevType(models,end_time,gn);
		
		//设备型号列表
		modelMap = dao.getDeviceModel();
		// 语音端口1，统计信息
		long num = 0;

		for (int i = 0; i < vendorList.size(); i++)
		{
			Map<String, Object> tmap = new HashMap<String, Object>();
			// 语音端口1，统计信息
			num = 0;
			String device_model = StringUtil.getStringValue(vendorList.get(i));
			if (!StringUtil.IsEmpty(device_model))
			{
				num += StringUtil.getLongValue(lineOne.get(device_model));
			}
			String model_id = StringUtil.getStringValue(vendorList.get(i));
			String modelType = modelMap.get(model_id);
			tmap.put("model_id", model_id);
			tmap.put("modelType", modelType);
			tmap.put("deploy_total", num);
			list.add(tmap);
		}
		return list;
	}
	
	private String getModleId(String vendorId, String modelId)
	{
		
		StringBuffer edtionIdBuffer = new StringBuffer();
		// 设备型号
		if (!StringUtil.IsEmpty(modelId) && !"-1".equals(modelId))
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
	
	public List<Map>  quertFunctionDeployByDevTypeList(String vendorId, String modelId, String end_time, String gn, int curPage_splitPage, int num_splitPage){
		logger.debug("FunctionDeploymentByDevTypeBIO==>quertFunctionDeployByDevTypeList()");
		String edtionIdBuffer = this.getModleId(vendorId, modelId);
		return dao.quertFunctionDeployByDevTypeList(edtionIdBuffer, end_time, gn, curPage_splitPage, num_splitPage);
	}
	
	public int  countQuertFunctionDeployByDevTypeList(String vendorId, String modelId, String end_time, String gn, int curPage_splitPage, int num_splitPage){
		logger.debug("FunctionDeploymentByDevTypeBIO==>quertFunctionDeployByDevTypeList()");
		String edtionIdBuffer = this.getModleId(vendorId, modelId);
		return dao.countQuertFunctionDeployByDevTypeList(edtionIdBuffer, end_time, gn, curPage_splitPage, num_splitPage);
	}
	
	public List<Map>  excelQuertFunctionDeployByDevTypeList(String vendorId, String modelId,String gn, String end_time){
		logger.debug("FunctionDeploymentByDevTypeBIO==>quertFunctionDeployByDevTypeList()");
		String edtionIdBuffer = this.getModleId(vendorId, modelId);
		return dao.excelQuertFunctionDeployByDevTypeList(edtionIdBuffer, gn, end_time);
	}

	/**
	 * 查询厂商信息
	 * 
	 * @return
	 */
	public Map<String, String> getVendor()
	{
		return dao.getVendor();
	}
	
	public FunctionDeploymentByDevTypeDAO getDao()
	{
		return dao;
	}

	
	public void setDao(FunctionDeploymentByDevTypeDAO dao)
	{
		this.dao = dao;
	}
	
	
}
