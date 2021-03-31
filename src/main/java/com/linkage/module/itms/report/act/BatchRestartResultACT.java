package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.BatchRestartResultBIO;

/**
 * @author songxq
 * @version 1.0
 * @since 2019-8-6 上午10:28:20
 * @category 
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchRestartResultACT extends splitPageAction implements SessionAware
{
	/** 日志记录 */
	private static Logger logger = LoggerFactory.getLogger(BatchRestartResultACT.class);
	
	/** session */
	private Map session = null;
	
	/** 设备属地 */
	private String cityId = null;
	
	/** 注册时间 */
	private String starttime = null;
	
	private String starttime1 = null;
	
	/** 注册时间 */
	private String endtime = null;
	
	private String endtime1 = null;
	
	private String flag = null;
	
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	
	/** 导出数据 */
	private List<Map> data = null;
	
	/** 导出文件列标题 */
	private String[] title = null;
	
	/** 导出文件列 */
	private String[] column = null;
	
	/** 导出文件名 */
	private String fileName = null;
	
	/** 详细信息展示 */
	private List<Map> detailResultList = null;
	
	BatchRestartResultBIO bio = null;

	public String countAll()
	{
		logger.debug("countAll()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
		
		logger.warn("长时间在线光猫批量重启情况统计   操作人ID："+curUser.getUser().getId()+
				    "   统计开始时间："+starttime+"   统计结束时间："+endtime+
				    "   属地："+cityMap.get(cityId));
		
		this.setTime();
		
		data = bio.countAll(cityId, starttime1, endtime1);
		
		return "list";
	}
	
	
	public String getDetail()
	{
		if(!StringUtil.IsEmpty(cityId) && !StringUtil.IsEmpty(flag))
		{
			session.put("cityId", cityId);
			session.put("flag", flag);
			session.put("starttime", starttime);
			session.put("endtime", endtime);
		}
		else {
			cityId = (String) session.get("cityId");
			flag = (String) session.get("flag");
			starttime = (String) session.get("starttime");
			endtime = (String) session.get("endtime");
		}
		
		this.setTime();
		
		detailResultList = bio.getDetail("1", cityId, flag, curPage_splitPage, num_splitPage,starttime1,endtime1);
		
		maxPage_splitPage = bio.getcount("1", cityId, flag, curPage_splitPage, num_splitPage,starttime1,endtime1);
		
		return "detail";
	}
	
	public String getDetailExcel()
	{
		if(!StringUtil.IsEmpty(cityId) && !StringUtil.IsEmpty(flag))
		{
			session.put("cityId", cityId);
			session.put("flag", flag);
			session.put("starttime", starttime);
			session.put("endtime", endtime);
		}
		else {
			cityId = (String) session.get("cityId");
			flag = (String) session.get("flag");
			starttime = (String) session.get("starttime");
			endtime = (String) session.get("endtime");
		}
		
		fileName = "重启设备详情";
		
		title = new String[8];
		column = new String[8];
		
		title[0] = "厂商";
		title[1] = "型号";
		title[2] = "软件版本";
		title[3] = "属地";
		title[4] = "设备序列号";
		title[5] = "LOID";
		title[6] = "重启时间";
		title[7] = "重启结果";	
		
		column[0] = "vendor_name";
		column[1] = "device_model";
		column[2] = "softwareversion";
		column[3] = "city_name";
		column[4] = "device_serialnumber";
		column[5] = "loid";
		column[6] = "restart_time";
		column[7] = "restart_status";
		
		this.setTime();
		
		data = bio.getDetailExcel("1", cityId, flag,starttime1,endtime1);
		
		return "excel";
	}
	
	public String countAllExcel() {
		
		fileName = "长时间在线光猫批量重启成功率统计";
		
		title = new String[6];
		column = new String[6];
		
		title[0] = "属地";
		title[1] = "总数";
		title[2] = "成功数";
		title[3] = "失败数";
		title[4] = "未触发";
		title[5] = "成功率";
		
		column[0] = "city_name";
		column[1] = "allRestartNum";
		column[2] = "succRestartNum";
		column[3] = "failRestartNum";
		column[4] = "noRestartNum";
		column[5] = "percent";
		data = bio.countAll(cityId, starttime, endtime);
		
		return "excel";
	}
	
	/**
	 * 时间转化
	 */
	private void setTime(){
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime)){
			starttime1 = null;
		}else{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime)){
			endtime1 = null;
		}else{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}
	
	/**
	 * 初始化统计查询页面
	 * @return
	 */
	public String init() {
		
		logger.debug("inti()");
		
		UserRes curUser = (UserRes) session.get("curUser");
		cityId = curUser.getCityId();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();  // 获取当前时间
		starttime = dt.getFirtDayOfMonth();
		
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		
		return "init";
	}

	
	public Map getSession()
	{
		return session;
	}

	
	public String getCityId()
	{
		return cityId;
	}

	
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	
	public String getStarttime()
	{
		return starttime;
	}

	
	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	
	public String getStarttime1()
	{
		return starttime1;
	}

	
	public void setStarttime1(String starttime1)
	{
		this.starttime1 = starttime1;
	}

	
	public String getEndtime()
	{
		return endtime;
	}

	
	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	
	public String getEndtime1()
	{
		return endtime1;
	}

	
	public void setEndtime1(String endtime1)
	{
		this.endtime1 = endtime1;
	}

	
	public String getFlag()
	{
		return flag;
	}

	
	public void setFlag(String flag)
	{
		this.flag = flag;
	}

	
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	
	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	
	public List<Map> getData()
	{
		return data;
	}

	
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	
	public String[] getTitle()
	{
		return title;
	}

	
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	
	public String[] getColumn()
	{
		return column;
	}

	
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	
	public String getFileName()
	{
		return fileName;
	}

	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	
	public BatchRestartResultBIO getBio()
	{
		return bio;
	}

	
	public void setBio(BatchRestartResultBIO bio)
	{
		this.bio = bio;
	}

	
	public List<Map> getDetailResultList()
	{
		return detailResultList;
	}

	
	public void setDetailResultList(List<Map> detailResultList)
	{
		this.detailResultList = detailResultList;
	}

	
	public void setSession(Map session)
	{
		this.session = session;
	}

	

	
	
}

