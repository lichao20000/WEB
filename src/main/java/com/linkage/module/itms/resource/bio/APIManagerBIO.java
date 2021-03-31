
package com.linkage.module.itms.resource.bio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.itms.resource.dao.APIManagerDAO;

public class APIManagerBIO
{

	private static Logger logger = LoggerFactory.getLogger(APIManagerBIO.class);
	private APIManagerDAO dao;

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
	public List<Map> getApiManagerList(String servicenameZh, String servicenameEn,String classifyId, String functionDesc,
			 int curPage_splitPage, int num_splitPage)
	{
		return dao.getApiManagerList(servicenameZh, servicenameEn, classifyId, functionDesc,
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
	public int countApiManagerList(String servicenameZh, String servicenameEn,String classifyId, String functionDesc,
			 int curPage_splitPage, int num_splitPage)
	{
		return dao.countApiManagerList(servicenameZh, servicenameEn, classifyId, functionDesc,
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
	public List<HashMap<String, String>> getclassifyName()
	{
		// 取所有属地的ID
		return dao.getclassifyName();
	}


	public String addApiManager(String managerId,String servicenameZh, String servicenameEn,String classifyId,
			String functionDesc ,String apiListName )
	{
		String msg = "";
		int result =  dao.addApiManager(managerId, servicenameZh, servicenameEn, classifyId, functionDesc,
				apiListName);
		if (result != 0) {
			msg = "设备版本入库成功";
		} else {
			msg = "设备版本入库失败";
		}
		return msg;
	}

	public String updateApiManager(String managerId,String servicenameZh, String servicenameEn,String classifyId,
			String functionDesc ,String apiListName)
	{
		logger.warn("bio1");
		int res=dao.updateApiManager(managerId, servicenameZh, servicenameEn, classifyId, functionDesc,
				apiListName);
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

	public void deleteApiManager(String classifyId)
	{
		dao.deleteApiManager(classifyId);
	}

	
	public APIManagerDAO getDao()
	{
		return dao;
	}

	
	public void setDao(APIManagerDAO dao)
	{
		this.dao = dao;
	}
	
	
}
