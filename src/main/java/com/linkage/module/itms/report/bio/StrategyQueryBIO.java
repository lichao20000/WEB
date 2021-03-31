
package com.linkage.module.itms.report.bio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.report.dao.StrategyQueryDAO;

/**
 * 根据设备序列号或LOID或宽带账号（下拉列表）、开通时间、结束时间查询无线业务策略
 * 
 * @author wanghong5 2015-02-13
 */
@SuppressWarnings("unchecked")
public class StrategyQueryBIO{
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(StrategyQueryBIO.class);
	private StrategyQueryDAO strategyQueryDao;
	
	public StrategyQueryBIO(){		
	}
	
	/*
	 * 每页绑定数据
	 */
	public List<Map> getDetailsForPage(List<Map> device_idList,String starttime ,String endtime ,
				String openState,String type,int curPage_splitPage, int num_splitPage) {
		logger.warn("StrategyQueryBIO.getDetailsForPage()");
		List<Map> list=new ArrayList<Map>();
		long starttimeCount = startTimeOfMonth(new Date());
		long endtimeCount = endTimeOfMonth(new Date());
		if(!"".equals(starttime) && starttime != null){
			starttimeCount = DateUtil.getTimeInSecond(starttime, "yyyy-MM-dd HH:mm:ss");
		}
		if(!"".equals(endtime) && endtime != null){
			endtimeCount = DateUtil.getTimeInSecond(endtime, "yyyy-MM-dd HH:mm:ss");
		}
		String device_id="'";
		device_id += StringUtil.getStringValue(device_idList.get(0),"device_id")+"'";
		list=strategyQueryDao.getDetailsForPage(device_id,starttimeCount,
		endtimeCount,openState,type,curPage_splitPage,num_splitPage);
			
		return list;
		
	}
	
	/*
	 * 获取最大页数
	 */
	public int getDetailsCount(List<Map> device_idList,String starttime ,String endtime,
			 String openState,String type,int num_splitPage) {
		logger.warn("StrategyQueryBIO.getDetailsCount()");
		int maxPage = 1;
		long starttimeCount = startTimeOfMonth(new Date());
		long endtimeCount = endTimeOfMonth(new Date());
		if(!"".equals(starttime) && starttime != null){
			starttimeCount = DateUtil.getTimeInSecond(starttime, "yyyy-MM-dd HH:mm:ss");
		}
		if(!"".equals(endtime) && endtime != null){
			endtimeCount = DateUtil.getTimeInSecond(endtime, "yyyy-MM-dd HH:mm:ss");
		}
		String device_id="'";
		device_id += StringUtil.getStringValue(device_idList.get(0),"device_id")+"'";
		maxPage = strategyQueryDao.getDetailsCount(device_id,starttimeCount,endtimeCount,openState,type,num_splitPage);
		
		return maxPage;
		
	}
	
	/*
	 * 开始日期格式转换  yyyy-mm-dd --> 毫秒
	 */
	public static long startTimeOfMonth(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		System.out.println(cal.getTime());
		
		return cal.getTimeInMillis() / 1000;
	}
	
	/*
	 * 结束日期转换  yyyy-mm-dd --> 毫秒
	 */
	public static long endTimeOfMonth(Date date){
		// 将当前月设置为第一天后，加1个月，然后-1秒。
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		System.out.println(cal.getTime());
		
		return cal.getTimeInMillis() / 1000 - 1;
	}

	public StrategyQueryDAO getStrategyQueryDao() {
		return strategyQueryDao;
	}

	public void setStrategyQueryDao(StrategyQueryDAO strategyQueryDao) {
		this.strategyQueryDao = strategyQueryDao;
	}

}

