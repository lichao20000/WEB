package com.linkage.module.gtms.config.serv;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.linkage.module.gtms.config.dao.VOIPDigitMapBatchDAO;

public interface VOIPDigitMapBatchServ {
	/**
	 * 查询所有语音数图
	 * @return 
	 */
	public List<Map> queryAllDigitMap(String flag);
	/**
	 * 调用配置模块	
	 * @return 配置是否成功
	 */
	public String doConfigAll(String[] deviceIds,
			String 	serviceId,String[] paramArr,String gw_type);
	/**
	 * 查询语音数图
	 * @return 
	 */
	public String queryDigitMapById(String mapId,String flag);
	public void download(String filepath, HttpServletResponse response);
	public VOIPDigitMapBatchDAO getDao();
	public int getDetailsCount(List<Map> device_idList,String starttime ,String endtime,
			 String openState,int num_splitPage);
	public List<Map> getDetailsForPage(List<Map> device_idList,String starttime ,String endtime ,
			String openState,int curPage_splitPage, int num_splitPage);
}
