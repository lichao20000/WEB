
package com.linkage.module.itms.report.act;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.PVCReportBIO;

/**
 * 考核所有的用户
 * 
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class PVCReportACT extends splitPageAction implements SessionAware
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(PVCReportACT.class);
	// session
	private Map session;
	/** 开始时间 */
	private String starttime = "";
	/** 开始时间 */
	private String starttime1 = "";
	/** 结束时间 */
	private String endtime = "";
	/** 结束时间 */
	private String endtime1 = "";
	/** 属地列表 */
	private List<Map<String, String>> cityList = null;
	/** 属地ID */
	private String cityId = "-1";
	/** 上行方式ID */
	private String prodSpecId = "-1";
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	private PVCReportBIO pvcReportBio;
	private String reform_flag;
	// 是否小计 1是小计 0不是小计
	private String isAll;
	private List<Map> hgwList = null;
	private String isNew;
	//是否纯ITV   0代表未开启，1代表开启
	private String isItv;

	/**
	 * 新用户考核初始化页面
	 * 
	 * @author wangsenbo
	 * @date Feb 25, 2010
	 * @param
	 * @return String
	 */
	public String newUserinit()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time+24*3600-1)* 1000);
		endtime = dt.getLongDate();
		cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		return "newUserinit";
	}

	/**
	 * 老用户用户考核初始化页面
	 * 
	 * @author wangsenbo
	 * @date Feb 25, 2010
	 * @param
	 * @return String
	 */
	public String oldUserinit()
	{
		logger.debug("init()");
		UserRes curUser = (UserRes) session.get("curUser");
		starttime = pvcReportBio.getCompletedate();
		cityList = CityDAO.getAllNextCityListByCityPid(curUser.getCityId());
		return "oldUserinit";
	}

	/**
	 * 新老用户考核统计
	 * 
	 * @author wangsenbo  isItv
	 * @date Feb 25, 2010
	 * @param
	 * @return String
	 */
	public String countPvc()
	{
		logger.debug("packagePvc()");
		this.setTime();
		
		data = pvcReportBio.countPVC(starttime1, endtime1, cityId, isNew, prodSpecId,isItv);
		return "list";
	}

	/**
	 * 获得IPTV信息
	 * 
	 * @author wangsenbo
	 * @date Feb 25, 2010
	 * @param
	 * @return String
	 */
	public String getHgw()
	{
		logger.debug("getHgw()");
		hgwList = pvcReportBio.getHgwList(starttime1, endtime1, cityId, reform_flag,
				curPage_splitPage, num_splitPage, isAll, isNew, prodSpecId,isItv);
		maxPage_splitPage = pvcReportBio.getHgwCount(starttime1, endtime1, cityId,
				reform_flag, curPage_splitPage, num_splitPage, isAll, isNew, prodSpecId, isItv);
		return "hgwlist";
	}

	/**
	 * 获得IPTV用户信息Excel
	 * 
	 * @author wangsenbo
	 * @date Jan 7, 2010
	 * @return String
	 */
	public String getHgwExcel()
	{
		logger.debug("getHgw()");
		fileName = "IPTV用户";
		title = new String[] { "宽带账号", "属地", "客户ID", "客户名称", "IPTV生效时间", "BAS地址", "VLAN值" };
		column = new String[] { "username", "city_name", "customer_id", "customer_name",
				"completedate", "bas_ip", "vlanid" };
		data = pvcReportBio.getHgwExcel(starttime1, endtime1, cityId, reform_flag, isNew,prodSpecId,isItv);
		return "excel";
	}

	/**
	 * 导出Excel
	 * 
	 * @author wangsenbo
	 * @date Feb 25, 2010
	 * @param
	 * @return String
	 */
	public String getExcel()
	{
		logger.debug("getExcel()");
		fileName = "IPTV";
		this.setTime();
		title = pvcReportBio.getTitle();
		column = pvcReportBio.getColumn();
		data = pvcReportBio.countPVC(starttime1, endtime1, cityId, isNew, prodSpecId,isItv);
		return "excel";
	}

	/**
	 * 时间转化
	 */
	private void setTime()
	{
		logger.debug("setTime()" + starttime);
		DateTimeUtil dt = null;// 定义DateTimeUtil
		if (starttime == null || "".equals(starttime))
		{
			starttime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(starttime);
			starttime1 = String.valueOf(dt.getLongTime());
		}
		if (endtime == null || "".equals(endtime))
		{
			endtime1 = null;
		}
		else
		{
			dt = new DateTimeUtil(endtime);
			endtime1 = String.valueOf(dt.getLongTime());
		}
	}

	/**
	 * @return the session
	 */
	public Map getSession()
	{
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Map session)
	{
		this.session = session;
	}

	/**
	 * @return the starttime
	 */
	public String getStarttime()
	{
		return starttime;
	}

	/**
	 * @param starttime
	 *            the starttime to set
	 */
	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	/**
	 * @return the starttime1
	 */
	public String getStarttime1()
	{
		return starttime1;
	}

	/**
	 * @param starttime1
	 *            the starttime1 to set
	 */
	public void setStarttime1(String starttime1)
	{
		this.starttime1 = starttime1;
	}

	/**
	 * @return the cityList
	 */
	public List<Map<String, String>> getCityList()
	{
		return cityList;
	}

	/**
	 * @param cityList
	 *            the cityList to set
	 */
	public void setCityList(List<Map<String, String>> cityList)
	{
		this.cityList = cityList;
	}

	/**
	 * @return the cityId
	 */
	public String getCityId()
	{
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	/**
	 * @return the data
	 */
	public List<Map> getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Map> data)
	{
		this.data = data;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	/**
	 * @return the column
	 */
	public String[] getColumn()
	{
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String[] column)
	{
		this.column = column;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the reform_flag
	 */
	public String getReform_flag()
	{
		return reform_flag;
	}

	/**
	 * @param reform_flag
	 *            the reform_flag to set
	 */
	public void setReform_flag(String reform_flag)
	{
		this.reform_flag = reform_flag;
	}

	/**
	 * @return the hgwList
	 */
	public List<Map> getHgwList()
	{
		return hgwList;
	}

	/**
	 * @param hgwList
	 *            the hgwList to set
	 */
	public void setHgwList(List<Map> hgwList)
	{
		this.hgwList = hgwList;
	}

	/**
	 * @return the isAll
	 */
	public String getIsAll()
	{
		return isAll;
	}

	/**
	 * @param isAll
	 *            the isAll to set
	 */
	public void setIsAll(String isAll)
	{
		this.isAll = isAll;
	}

	/**
	 * @return the endtime
	 */
	public String getEndtime()
	{
		return endtime;
	}

	/**
	 * @param endtime
	 *            the endtime to set
	 */
	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	/**
	 * @return the endtime1
	 */
	public String getEndtime1()
	{
		return endtime1;
	}

	/**
	 * @param endtime1
	 *            the endtime1 to set
	 */
	public void setEndtime1(String endtime1)
	{
		this.endtime1 = endtime1;
	}

	/**
	 * @return the pvcReportBio
	 */
	public PVCReportBIO getPvcReportBio()
	{
		return pvcReportBio;
	}

	/**
	 * @param pvcReportBio
	 *            the pvcReportBio to set
	 */
	public void setPvcReportBio(PVCReportBIO pvcReportBio)
	{
		this.pvcReportBio = pvcReportBio;
	}
	
	public String getProdSpecId()
	{
		return prodSpecId;
	}

	
	public void setProdSpecId(String prodSpecId)
	{
		this.prodSpecId = prodSpecId;
	}
	/**
	 * @return the isNew
	 */
	public String getIsNew()
	{
		return isNew;
	}

	/**
	 * @param isNew
	 *            the isNew to set
	 */
	public void setIsNew(String isNew)
	{
		this.isNew = isNew;
	}
	
	public  String getIsItv()
	{
		return isItv;
	}
	
	public  void setIsItv(String isItv)
	{
		this.isItv = isItv;
	}
}
