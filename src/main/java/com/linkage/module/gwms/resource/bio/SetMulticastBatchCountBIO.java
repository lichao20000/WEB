package com.linkage.module.gwms.resource.bio;

import java.util.List;

import com.linkage.module.gwms.resource.dao.SetMulticastBatchCountDAO;

/**
 * 
 * @author zhaixx (Ailk No.)
 * @version 1.0
 * @since 2018年11月9日
 * @category com.linkage.module.gwms.config.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class SetMulticastBatchCountBIO {

	private SetMulticastBatchCountDAO dao;
	

	/**新报表查询使用：查询列表*/
	public List getOrderTaskListNew(int curPage_splitPage, int num_splitPage, String startTime, String endTime,
			String taskname) {
		return dao.getOrderTaskListNew(curPage_splitPage, num_splitPage,
				 startTime, endTime, taskname);
	}
	
	/**新报表查询使用：查询列表分页总量*/
	public int countOrderTaskNew(int curPage_splitPage, int num_splitPage, String startTime, String endTime,
			String taskname) {
		return dao.countOrderTaskNew(curPage_splitPage, num_splitPage,
				 startTime, endTime, taskname);
	}
	/**新报表查询使用：查询列表详情
	 * @param taskname */
	public List getOrderTaskDetailNew(String taskId, String taskname, int curPage_splitPage, int num_splitPage) {
		
		return dao.getOrderTaskDetailNew(taskname,curPage_splitPage, num_splitPage,taskId);
	}
	
	/**新报表查询使用：查询列表详情总量
	 * @param taskname */
	public int countOrderTaskDetailNew(String taskId, String taskname, int curPage_splitPage, int num_splitPage) {
		return dao.countOrderTaskDetailNew(taskname,curPage_splitPage, num_splitPage,taskId);
	}
	

	/**新报表查询使用：根据属地，成功状态查询详情*/
	public List showDetailListByType(String taskId, String cityId, String type, int curPage_splitPage, int num_splitPage) {
		return dao.showDetailListByType(curPage_splitPage, num_splitPage,taskId,cityId,type);
	}

	/**新报表查询使用：根据属地，成功状态查询总数*/
	public int showDetailListCountByType(String taskId, String cityId, String type, int curPage_splitPage,
			int num_splitPage) {
		return dao.showDetailListCountByType(curPage_splitPage, num_splitPage,taskId,cityId,type);
	}

	/**根据task_id查询详情页面导出
	 * @param task_name */
	public List getOrderTaskDetailNewExcel(String taskId, String task_name) {
		return dao.getOrderTaskDetailNewExcel(taskId,task_name);
	}

	
	public SetMulticastBatchCountDAO getDao() {
		return dao;
	}

	
	public void setDao(SetMulticastBatchCountDAO dao) {
		this.dao = dao;
	}
	
	
}
