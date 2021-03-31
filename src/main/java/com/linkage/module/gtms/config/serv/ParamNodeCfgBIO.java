
package com.linkage.module.gtms.config.serv;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.config.dao.ParamNodeCfgDAO;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.system.utils.database.DataSetBean;

public class ParamNodeCfgBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ParamNodeCfgBIO.class);
	private ParamNodeCfgDAO dao = null;

	/**
	 * 获取设备基本信息
	 * 
	 * @param deviceIds
	 * @param gwType
	 * @return List
	 */
	public List<Map<String, String>> querySingleDeviceList(String deviceIds, String gwType)
	{
		logger.debug("ParamNodeCfgBIO=>querySingleDeviceList({},{})", new Object[] {
				deviceIds, gwType });
		return dao.querySingleDeviceList(deviceIds, gwType);
	}

	/**
	 * 获取节点类型
	 * 
	 * @return List
	 */
	public List<Map<String, String>> getNodeTypeList()
	{
		logger.debug("ParamNodeCfgBIO=>getNodeTypeList()");
		return dao.getNodeTypeList();
	}

	/**
	 * 填充节点类型 根据MapList获取<option value=value>textarea<option>
	 * 
	 * @param mapList
	 * @param value
	 *            conf_type_id
	 * @param textarea
	 *            cong_type_name
	 * @return String
	 */
	public String getSelectOptiones(List<Map<String, String>> mapList, String value,
			String textarea)
	{
		logger.debug("ParamNodeCfgBIO=>getSelectOptiones({},{},{})", new Object[] {
				mapList, value, textarea });
		return dao.getSelectOptiones(mapList, value, textarea);
	}

	/**
	 * 获取配置节点列表
	 * 
	 * @param conf_type_id
	 * @return List
	 */
	public List<Map<String, String>> getConfParam(int conf_type_id)
	{
		logger.debug("ParamNodeCfgBIO=>getConfParam({})", new Object[] { conf_type_id });
		return dao.getConfParam(conf_type_id);
	}

	/**
	 * 单台设备校验
	 * 
	 * @param devId
	 * @param conf_type_id
	 * @param nodeIdList
	 * @return int
	 */
	public int checkDevExist(String devId, int conf_type_id, List<String> nodeIdList)
	{
		return dao.checkDevExist(devId, conf_type_id, nodeIdList);
	}

	/**
	 * 获取正则表达式
	 * 
	 * @param nodeCheckId
	 * @return
	 */
	public String getCheckVal(String nodeCheckId)
	{
		return StringUtil.getStringValue(dao.getCheckVal(nodeCheckId).get(0),
				"type_check");
	}

	/**
	 * 单台节点入库
	 * 
	 * @param taskId
	 * @param nodeIdList
	 * @param conf_type_id
	 * @param nodeValueList
	 * @param remarkList
	 * @param device_serialnumber
	 * @param deviceIds
	 * @param currTime
	 * @param userId
	 * @return String
	 */
	public String doSingleConfig(long taskId, long currTime, List<String> nodeIdList,
			int conf_type_id, List<String> nodeValueList, String device_serialnumber,
			String deviceIds, String do_type)
	{
		List<String> sqlList = new ArrayList<String>();
		if (null != nodeIdList && nodeIdList.size() > 0)
		{
			for (int i = 0; i < nodeIdList.size(); i++)
			{
				int nodeId = StringUtil.getIntegerValue(nodeIdList.get(i));
				String nodeValue = StringUtil.getStringValue(nodeValueList.get(i));
				String sql = dao.saveConfig(taskId, nodeId, conf_type_id, nodeValue);
				sqlList.add(sql);
			}
		}
		
		UserRes curUser = WebUtil.getCurrentUser();
		long userId = curUser.getUser().getId();
		String taskSql = dao.saveTask(taskId, userId, currTime, conf_type_id, do_type);
		String devSql = dao.saveDev(taskId, currTime, device_serialnumber, deviceIds);
		sqlList.add(taskSql);
		sqlList.add(devSql);
		int[] iCodes = DataSetBean.doBatch(sqlList);
		if (iCodes != null && iCodes.length > 0)
		{
			return "定制成功!";
		}
		else
		{
			return "定制失败!";
		}
	}

	/**
	 * 导入文件查询节点配置
	 * 
	 * @param nodeIdList
	 * @param conf_type_id
	 * @param nodeValueList
	 * @param remark
	 * @param targetFileName
	 * @param filePath
	 * @param do_type
	 * @return String
	 */
	public String importBatchParmNode(long taskId, List<String> nodeIdList,
			int conf_type_id, List<String> nodeValueList, String remark,
			String targetFileName, String filePath)
	{
		List<String> sqlList = new ArrayList<String>();
		long currTime = new Date().getTime() / 1000L;
		UserRes curUser = WebUtil.getCurrentUser();
		long userId = curUser.getUser().getId();
		if (null != nodeIdList && nodeIdList.size() > 0)
		{
			for (int i = 0; i < nodeIdList.size(); i++)
			{
				int nodeId = StringUtil.getIntegerValue(nodeIdList.get(i));
				String nodeValue = StringUtil.getStringValue(nodeValueList.get(i));
				String sql = dao.saveConfig(taskId, nodeId, conf_type_id, nodeValue);
				sqlList.add(sql);
			}
		}
		String taskSql = dao.saveImportTask(taskId, userId, currTime, conf_type_id,
				remark, filePath);
		sqlList.add(taskSql);
		int[] iCodes = DataSetBean.doBatch(sqlList);
		if (iCodes != null && iCodes.length > 0)
		{
			return "定制成功!";
		}
		else
		{
			return "定制失败!";
		}
	}

	/**
	 * 高级查询节点配置
	 * 
	 * @param cityId
	 * @param onlineStatus
	 * @param vendorId
	 * @param deviceModelId
	 * @param devicetypeId
	 * @param bindType
	 * @param device_serialnumber
	 * @param nodeIdList
	 * @param conf_type_id
	 * @param nodeValueList
	 * @param remark
	 * @param do_type
	 * @return String
	 */
	public String batchParmNode(long taskId, long currTime, String cityId,
			String onlineStatus, String vendorId, String deviceModelId,
			String devicetypeId, String bindType, String device_serialnumber,
			List<String> nodeIdList, int conf_type_id, List<String> nodeValueList,
			String remark)
	{
		List<String> sqlList = new ArrayList<String>();
		UserRes curUser = WebUtil.getCurrentUser();
		long userId = curUser.getUser().getId();
		if (null != nodeIdList && nodeIdList.size() > 0)
		{
			for (int i = 0; i < nodeIdList.size(); i++)
			{
				int nodeId = StringUtil.getIntegerValue(nodeIdList.get(i));
				String nodeValue = StringUtil.getStringValue(nodeValueList.get(i));
				String sql = dao.saveConfig(taskId, nodeId, conf_type_id, nodeValue);
				sqlList.add(sql);
			}
		}
		
		String taskSql = dao.doSeniorConfig(taskId, currTime, conf_type_id, userId,
				cityId, onlineStatus, vendorId, deviceModelId, devicetypeId, bindType,
				device_serialnumber);
		
		sqlList.add(taskSql);
		int[] iCodes = DataSetBean.doBatch(sqlList);
		if (iCodes != null && iCodes.length > 0)
		{
			return "定制成功!";
		}
		else
		{
			return "定制失败!";
		}
	}

	public void setDao(ParamNodeCfgDAO dao)
	{
		this.dao = dao;
	}
}
