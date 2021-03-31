
package com.linkage.module.gwms.resource.bio;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.linkage.module.gwms.resource.dao.RouteInfoDAO;
import com.linkage.module.gwms.resource.obj.RouteInfoBean;


public class RouteInfoBIO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(RouteInfoBIO.class);
	private RouteInfoDAO dao;

	public List queryRouteInfo(int curPage_splitPage,int num_splitPage,RouteInfoBean bean){

		List list = null;
		if("2".equals(bean.getQuery_type())){
			bean.setEnd_time(String.valueOf(dealTime(bean.getEnd_time())));
			bean.setStart_time(String.valueOf(dealTime(bean.getStart_time())));
			list = dao.queryDevice(curPage_splitPage,num_splitPage,bean);
		}else if("1".equals(bean.getQuery_type())){
			list = dao.queryDevice(curPage_splitPage,num_splitPage,bean);
		}
		return list;
	}
	public int queryRouteInfoCount(int curPage_splitPage,int num_splitPage,RouteInfoBean bean){
		int num = 0;
			num = dao.queryDeviceCount(bean, curPage_splitPage, num_splitPage);
		return num;
	}

	public List queryRouteInfoForExcel(RouteInfoBean bean){

		List list = null;
		if("2".equals(bean.getQuery_type())){
			bean.setEnd_time(String.valueOf(dealTime(bean.getEnd_time())));
			bean.setStart_time(String.valueOf(dealTime(bean.getStart_time())));
			list = dao.queryDevice(bean);
		}else if("1".equals(bean.getQuery_type())){
			list = dao.queryDevice(bean);
		}
		return list;
	}
	public long dealTime(String time)
	{
		if(null!=time&&!"".equals(time.trim())){
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			Date str = new Date();
			try {
				str = date.parse(time);
			}
			catch (ParseException e) {
				logger.warn("选择开始或者结束的时间格式不对:" + time);
			}
			return str.getTime() / 1000L;
		}
		return 0;
	}


	public RouteInfoDAO getDao()
	{
		return dao;
	}

	public void setDao(RouteInfoDAO dao)
	{
		this.dao = dao;
	}


}
