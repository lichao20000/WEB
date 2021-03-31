package com.linkage.module.ids.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.ids.dao.IdsAlarmInfoDAO;

/**
 * 
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2014-2-18
 * @category com.linkage.module.ids.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class IdsAlarmInfoBIO
{
	private static Logger logger = LoggerFactory.getLogger(IdsAlarmInfoBIO.class);
	private IdsAlarmInfoDAO dao;
	
	/**
	 * 预检预修告警信息查询条件
	 * @param start_time
	 * @param end_time
	 * @param alarm_type
	 * @param alarm_code
	 * @param alarm_count
	 * @param is_send_sheet
	 * @param is_pre_release
	 * @param is_release
	 * @param device_id
	 * @param loid
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getIdsarmInfoList(String start_time, String end_time,
			String alarm_type, String alarm_code, String alarm_count, String is_send_sheet,
			String is_pre_release, String is_release, String device_id, String loid,String city_id,
			int curPage_splitPage, int num_splitPage){
		return dao.getIdsarmInfoList(start_time, end_time, alarm_type, alarm_code, alarm_count, is_send_sheet, is_pre_release, is_release, device_id, loid, city_id,curPage_splitPage, num_splitPage);
	}
	
	/**
	 * 预检预修告警信息最大数
	 * @param start_time
	 * @param end_time
	 * @param alarm_type
	 * @param alarm_code
	 * @param alarm_count
	 * @param is_send_sheet
	 * @param is_pre_release
	 * @param is_release
	 * @param device_id
	 * @param loid
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public int countIdsarmInfoList(String start_time, String end_time, String alarm_type,
			String alarm_code, String alarm_count, String is_send_sheet, String is_pre_release,
			String is_release, String device_id, String loid,String city_id, int curPage_splitPage,
			int num_splitPage){
		return dao.countIdsarmInfoList(start_time, end_time, alarm_type, alarm_code, alarm_count, is_send_sheet, is_pre_release, is_release, device_id, loid, city_id,curPage_splitPage, num_splitPage);
	}
	
	public List<Map> getIdsarmInfoListExcel(String start_time, String end_time,
			String alarm_type, String alarm_code, String alarm_count, String is_send_sheet,
			String is_pre_release, String is_release, String device_id, String loid,String city_id){
		return dao.getIdsarmInfoListExcel(start_time, end_time, alarm_type, alarm_code, alarm_count, is_send_sheet, is_pre_release, is_release, device_id, loid,city_id);
	}
			
	
	
	public IdsAlarmInfoDAO getDao()
	{
		return dao;
	}

	
	public void setDao(IdsAlarmInfoDAO dao)
	{
		this.dao = dao;
	}

	
}
