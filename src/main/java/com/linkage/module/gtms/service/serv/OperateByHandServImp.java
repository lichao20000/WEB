package com.linkage.module.gtms.service.serv;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.service.dao.OperateByHandActionDAO;


/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-5-14
 * @category com.linkage.module.gtms.service.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class OperateByHandServImp implements OperateByHandServ
{	
	private static Logger logger = LoggerFactory
			.getLogger(OperateByHandServImp.class);
	
	private OperateByHandActionDAO operateByHandActionDAO;

	@Override
	public List<Map> getOperateByHandInfo(long accOid,String starttime, String endtime,
			String city_id, String servTypeId, String username, String resultType,
			int curPage_splitPage, int num_splitPage)
	{
		logger
		.debug("OperateByHandServImp==>getOperateByHandInfo({},{},{},{},{},{},{},{})",
				new Object[] {  starttime,  endtime,
				 city_id,  servTypeId,  username,  resultType,
				 curPage_splitPage,  num_splitPage});
		
		List<Map> list = operateByHandActionDAO.getOperateByHandInfo(accOid,starttime, endtime, city_id, servTypeId, username, resultType, curPage_splitPage, num_splitPage);
		return list;
	}
	
	public int countOperateByHandInfo(long accOid,String starttime, String endtime,
			String city_id, String servTypeId, String username, String resultType,
			int curPage_splitPage, int num_splitPage){
		
		logger
		.debug("OperateByHandServImp==>countOperateByHandInfo({},{},{},{},{},{},{},{})",
				new Object[] {  starttime,  endtime,
				 city_id,  servTypeId,  username,  resultType,
				 curPage_splitPage,  num_splitPage});
		
		return operateByHandActionDAO.countOperateByHandInfo(accOid,starttime, endtime, city_id, servTypeId, username, resultType, curPage_splitPage, num_splitPage);
	}
	
	@Override
	public int countOperateByHandInfoExcel(String starttime, String endtime,
			String city_id, String servTypeId, String username, String resultType)
	{
		logger
		.debug("OperateByHandServImp==>countOperateByHandInfoExcel({},{},{},{},{},{},{},{})",
				new Object[] {  starttime,  endtime,
				 city_id,  servTypeId,  username,  resultType});
		
		return operateByHandActionDAO.countOperateByHandInfoExcel(starttime, endtime, city_id, servTypeId, username, resultType);
	}
	
	@Override
	public List<Map> getOperateByHandInfoExcel(String starttime, String endtime,
			String city_id, String servTypeId, String username, String resultType)
	{	
		logger
		.debug("OperateByHandServImp==>getOperateByHandInfoExcel({},{},{},{},{},{},{},{})",
				new Object[] {  starttime,  endtime,
				 city_id,  servTypeId,  username,  resultType});
		return operateByHandActionDAO.getOperateByHandInfoExcel(starttime, endtime, city_id, servTypeId, username, resultType);
	}

	
	public OperateByHandActionDAO getOperateByHandActionDAO()
	{
		return operateByHandActionDAO;
	}

	
	public void setOperateByHandActionDAO(OperateByHandActionDAO operateByHandActionDAO)
	{
		this.operateByHandActionDAO = operateByHandActionDAO;
	}
	
}
