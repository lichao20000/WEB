package com.linkage.module.gtms.stb.resource.serv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gtms.stb.resource.dao.ZeroConfigHistoryDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;

public class ZeroConfigHistoryBIO {

	private static Logger logger = LoggerFactory
			.getLogger(ZeroConfigHistoryBIO.class);
	private ZeroConfigHistoryDAO dao;
		
	/**
	 * 零配置机顶盒历史配置查询
	 * @param curPageSplitPage
	 * @param numSplitPage
	 * @param servAccount
	 * @param deviceSerialnumber
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Map> doZeroHistoryQueryPage(int curPageSplitPage,int numSplitPage,
			String servAccount,String deviceSerialnumber,String starttime,String endtime){
		logger.debug("ZeroConfigHistoryBIO == >doZeroHistoryQuery({})", new Object[] {servAccount,deviceSerialnumber,starttime,endtime });
		List<Map> zeroHistoryList = dao.getZeroConfigHistory(curPageSplitPage,numSplitPage,servAccount,deviceSerialnumber,starttime, endtime);
		convertToChin(zeroHistoryList);		
		return zeroHistoryList;
	}
	
	public int doZeroHistoryQueryCount(String servAccount,String deviceSerialnumber,String starttime,String endtime){
		return dao.getZeroConfigHistoryCount(servAccount, deviceSerialnumber, starttime, endtime);
	}
	
	/**
	 * 单个设备历史绑定流程查询
	 * @param curPageSplitPage
	 * @param numSplitPage
	 * @param deviceId
	 * @return
	 */
	public List<Map> getDeviceZeroConfigDetail(int curPageSplitPage,int numSplitPage,String deviceId,String starttime,String endtime,String failReasonMark,String reasonId,String ctiyId){
		logger.debug("ZeroConfigHistoryBIO == >getDeviceZeroConfigDetail({})", deviceId);
		List<Map> deviceZeroCofnigList = dao.getDeviceZeroConfigDetail(curPageSplitPage, numSplitPage, deviceId,starttime,endtime,failReasonMark,reasonId,ctiyId);
		if(deviceZeroCofnigList != null){
			for(Map deviceZeroConfig : deviceZeroCofnigList){
				
				deviceZeroConfig.put("start_time", DateUtil.transTime(StringUtil.getLongValue(deviceZeroConfig.get("start_time")),"yyyy-MM-dd HH:mm:ss"));
				deviceZeroConfig.put("fail_time", DateUtil.transTime(StringUtil.getLongValue(deviceZeroConfig.get("fail_time")),"yyyy-MM-dd HH:mm:ss"));
				
				if(deviceZeroConfig.get("bind_way") != null){
					switch (StringUtil.getIntegerValue(deviceZeroConfig.get("bind_way"))) {
					case 0:
						deviceZeroConfig.put("bind_way", "ITMS MAC绑定");
						break;
					case 1:
						deviceZeroConfig.put("bind_way", "AAA IP绑定");
						break;
					case 2:
						deviceZeroConfig.put("bind_way", "宽带账号自助安装");
						break;
					case 3:
						deviceZeroConfig.put("bind_way", "其他绑定");
						break;
					case 4:
						deviceZeroConfig.put("bind_way", "机顶盒更换");
						break;
					case 5:
						deviceZeroConfig.put("bind_way", "爱运维开通");
						break;
					default:
						deviceZeroConfig.put("bind_way", "无此类型");
						break;
					}
				}
				else{
					deviceZeroConfig.put("bind_way", "类型为空");
				}
				if(deviceZeroConfig.get("fail_reason_id") != null){
					switch (StringUtil.getIntegerValue(deviceZeroConfig.get("fail_reason_id"))) {
					case 6:
						deviceZeroConfig.put("fail_reason_id", "成功");
						break;
					case 13:
						deviceZeroConfig.put("fail_reason_id", "成功");
						break;
					case 14:
						deviceZeroConfig.put("fail_reason_id", "成功");
						break;
					case 15:
						deviceZeroConfig.put("fail_reason_id", "成功");
						break;
					default:
						deviceZeroConfig.put("fail_reason_id", "失败");
						break;
					}
				}
				else{
					deviceZeroConfig.put("fail_reason_id", "无此状态");
				}
			}
		}
		return deviceZeroCofnigList;
		
	}
	
	public int getDeviceZeroConfigDetailCount(String deviceId,String starttime,String endtime,String failReasonMark,String reasonId,String ctiyId){
		return dao.getDeviceZeroConfigDetailCount(deviceId,starttime,endtime,failReasonMark,reasonId,ctiyId);
	}
	
	/**
	 * 零配置用户历史配置导出
	 * @param servAccount
	 * @param deviceSerialnumber
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<Map> doZeroHistoryExcel(String servAccount,String deviceSerialnumber,String starttime,String endtime){
		logger.debug("ZeroConfigHistoryBIO == >doZeroHistoryExcel({})", new Object[] {servAccount,deviceSerialnumber,starttime,endtime });
		List<Map> zeroHistoryList = dao.doZeroHistoryExcel(servAccount, deviceSerialnumber, starttime, endtime);
		convertToChin(zeroHistoryList);		
		return zeroHistoryList;
	}
	
	private void convertToChin(List<Map> zeroHistoryList){
		if(zeroHistoryList != null){
			for(Map zeroHistory : zeroHistoryList){
				zeroHistory.put("city_name", CityDAO.getCityName(StringUtil.getStringValue(zeroHistory.get("city_id"))));	
				zeroHistory.put("bind_time", DateUtil.transTime(StringUtil.getLongValue(zeroHistory.get("bind_time")),"yyyy-MM-dd HH:mm:ss"));
				if(zeroHistory.get("bind_state") != null){
					if("3".equals(StringUtil.getStringValue(zeroHistory.get("bind_way")))){
						zeroHistory.put("bind_state", "成功");
					}
					else{
						switch (StringUtil.getIntegerValue(zeroHistory.get("bind_state"))) {
						case 0:
							zeroHistory.put("bind_state", "未绑定");
							break;
						case 1:
							zeroHistory.put("bind_state", "成功");
							break;
						case -1:
							zeroHistory.put("bind_state", "失败");
							break;
						default:
							zeroHistory.put("bind_state", "无此状态");
							break;
						}
					}					
				}
				else{
					zeroHistory.put("bind_state", "无此状态");
				}
				
				if(zeroHistory.get("bind_way") != null){
					switch (StringUtil.getIntegerValue(zeroHistory.get("bind_way"))) {
					case 0:
						zeroHistory.put("bind_way", "ITMS MAC绑定");
						zeroHistory.put("config_type", "零配置");
						break;
					case 1:
						zeroHistory.put("bind_way", "AAA IP绑定");
						zeroHistory.put("config_type", "零配置");
						break;
					case 2:
						zeroHistory.put("bind_way", "宽带账号自助安装");
						zeroHistory.put("config_type", "零配置");
						break;
					case 3:
						zeroHistory.put("bind_way", "其他绑定");
						zeroHistory.put("config_type", "其他绑定");
						break;
					case 4:
						zeroHistory.put("bind_way", "机顶盒更换");
						zeroHistory.put("config_type", "更换机顶盒");
						break;
					case 5:
						zeroHistory.put("bind_way", "爱运维开通");
						zeroHistory.put("config_type", "零配置");
						break;
					default:
						zeroHistory.put("bind_way", "无此类型");
						zeroHistory.put("config_type", "无此业务类型");
						break;
					}
				}
				else{
					zeroHistory.put("bind_way", "类型为空");
					zeroHistory.put("config_type", "无此业务类型");
				}
				
			}
		}
	}

	public ZeroConfigHistoryDAO getDao() {
		return dao;
	}

	public void setDao(ZeroConfigHistoryDAO dao) {
		this.dao = dao;
	}
}
