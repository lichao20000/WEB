
package com.linkage.module.itms.resource.bio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.resource.dao.APIPluginDAO;

public class APIPluginBIO
{

	private static Logger logger = LoggerFactory.getLogger(APIPluginBIO.class);
	private APIPluginDAO dao;

	/**
	 * 获取mqList
	 * 
	 * @author 岩
	 * @date 2016-10-13
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getApiPluginList(String classifyName, String creator,String starttime, String endtime,
			String status, int curPage_splitPage, int num_splitPage)
	{
		return dao.getApiPluginList(classifyName,creator,starttime, endtime,status,
				curPage_splitPage, num_splitPage);
	}

	/**
	 * 获取List总数
	 * 
	 * @author 岩
	 * @date 2016-10-13
	 * @param mqId
	 * @param starttime
	 * @param endtime
	 * @param topicName
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countApiPluginList(String classifyName, String creator,String starttime, String endtime,
			String status, int curPage_splitPage, int num_splitPage)
	{
		return dao.countApiPluginList(classifyName,creator,starttime, endtime,status,
				curPage_splitPage, num_splitPage);
	}


	/**
	 * 获取总数
	 * 
	 * @author 岩
	 * @date 2016-10-13
	 * @return
	 */
	public int getQueryCount()
	{
		logger.warn("QueryMqBIO——》" + dao.getQueryCount());
		return dao.getQueryCount();
	}

	/**
	 * 获取下拉框内容
	 * 
	 * @author 岩
	 * @date 2016-10-13
	 * @return
	 */
	public List<HashMap<String, String>> getMqIpPort()
	{
		// 取所有属地的ID
		return dao.getMqIpPort();
	}

	public APIPluginDAO getDao()
	{
		return dao;
	}

	public void setDao(APIPluginDAO dao)
	{
		this.dao = dao;
	}

	public String addApiPlugin(String classifyId,String classifyName, String classifyDesc,long addTime,
			String creator ,String status )
	{
		String msg = "";
		int result =  dao.addApiPlugin(classifyId,classifyName,classifyDesc,addTime,creator,status);
		if (result != 0) {
			msg = "设备版本入库成功";
		} else {
			msg = "设备版本入库失败";
		}
		return msg;
	}

	public String updateApiPlugin(String classifyId,String classifyName, String status, String classifyDesc)
	{
		logger.warn("bio1");
		int res=dao.updateApiPlugin(classifyId,classifyName, status, classifyDesc);
		// 通知ACS
		logger.warn("bio2");
		String msg=null;
		if (res != 0) {
			msg = "设备类型更新成功";
		} else {
			msg = "设备类型更新失败";
		}
		return msg;
	}

	public void deleteApiPlugin(String classifyId)
	{
		dao.deleteApiPlugin(classifyId);
	}
}
