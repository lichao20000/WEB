
package action.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.dbimpl.DbUserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.opensymphony.xwork2.ActionSupport;

import dao.resource.ExportUserDAO;

@SuppressWarnings("unchecked")
public class ExportUserAction extends ActionSupport implements SessionAware
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(ExportUserAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -8288612851382310136L;
	/** dao */
	private ExportUserDAO dao;
	// 用于分页，页码
	String stroffset;
	/** user */
	private User user;
	/** DbUserRes */
	private DbUserRes dbUserRes;
	/** 属地列表 */
	@SuppressWarnings("unused")
	private List<Map<String, String>> CityList = null;
	/** 套餐列表 */
	@SuppressWarnings("unused")
	private List<Map<String, String>> PackageList = null;
	/** getSession */
	Map session;
	// 导出数据
	private List<Map> data;
	// 导出文件列标题
	private String[] title;
	// 导出文件列
	private String[] column;
	// 导出文件名
	private String fileName;
	// 导入的资源类型 0：家庭网关 1：企业网关
	private String infoType = "0";
	/** 用户帐号 */
	private String username1 = "";
	/** 属地 */
	private String cityId = "-1";
	/** 套餐 */
	private String packageId = "-1";
	/** 开始时间 */
	private String starttime = "";
	/** 开始时间 */
	private String starttime1 = "";
	/** 结束时间 */
	private String endtime = "";
	/** 结束时间 */
	private String endtime1 = "";
	/** 属地列表 */
	private ArrayList<String> cityIdList = null;
	/** 绑定状态 */
	private String bindState;
	/** 时间类型 */
	private String timeType;
	/** 用户设备 */
	private String ouiState;
	/** 是否新疆电信 */
	private String isXJ;
	/**是否黑龙江电信*/
	private String isHljDx;
	/** 是江苏电信ITMS */
	private String isJSITMS = "0";
	/** 大订单类型 */
	private String bigOrderType;
	/** 订单类型 */
	private String orderType;
	
	/**
	 * 是否查询(特用于菜单进入的时候不触发查询) by zhangcong 2011-06-02
	 */
	private String no_query;
	private String gw_type;

	public String getNo_query() 
	{
		return no_query;
	}

	
	public String getGw_type()
	{
		return gw_type;
	}

	
	public void setGw_type(String gwType)
	{
		gw_type = gwType;
	}

	public void setNo_query(String no_query) 
	{
		this.no_query = no_query;
	}

	/**
	 * get:
	 * 
	 * @return the starttime
	 */
	public String getStarttime()
	{
		logger.debug("getStarttime");
		return starttime;
	}

	/**
	 * set:
	 * 
	 * @param starttime
	 *            the starttime to set
	 */
	public void setStarttime(String starttime)
	{
		logger.debug("setStarttime({})", starttime);
		this.starttime = starttime;
	}

	/**
	 * get:
	 * 
	 * @return the endtime
	 */
	public String getEndtime()
	{
		logger.debug("getEndtime");
		return endtime;
	}

	/**
	 * set:
	 * 
	 * @param endtime
	 *            the endtime to set
	 */
	public void setEndtime(String endtime)
	{
		logger.debug("setEndtime({})", endtime);
		this.endtime = endtime;
	}

	public void setStroffset(String stroffset)
	{
		this.stroffset = stroffset;
	}

	public String getStroffset()
	{
		return stroffset;
	}

	/**
	 * get infoType
	 * 
	 * @return
	 */
	public String getInfoType()
	{
		logger.debug("getInfoType()");
		//infoType = "" + LipossGlobals.SystemType();
		infoType =gw_type;
		return infoType;
	}

	public void setInfoType(String infoType)
	{
		this.infoType = infoType;
	}

	public String getFileName()
	{
		return fileName;
	}

	public String[] getColumn()
	{
		return column;
	}

	public List<Map> getData()
	{
		return data;
	}

	public String[] getTitle()
	{
		return title;
	}

	public void setExportUser(ExportUserDAO exportUser)
	{
		logger.debug("setExportUser(ExportUserDAO)");
		this.dao = exportUser;
	}

	/**
	 * export
	 */
	public String getInfoExcel() throws Exception
	{
		logger.debug("getInfoExcel()");
		// 取得session中的user属性
		dbUserRes = (DbUserRes) session.get("curUser");
		user = dbUserRes.getUser();
		//infoType = "" + LipossGlobals.SystemType();
		infoType =gw_type;
		column = dao.getColumn(infoType, bindState);
		// 取得属地列表
		CityList = CityDAO.getAllNextCityListByCityPid(user.getCityId());
		PackageList = dao.getPackageList();
		title = dao.getTitle(infoType, bindState);
		if (LipossGlobals.IsITMS())
		{
			if (Global.JSDX.equals(Global.instAreaShortName))
			{
				isJSITMS = "1";
			}
		}
		this.setTime();
		this.setCity(user.getCityId());
		if (username1 == null || "".equals(username1.trim()) == true)
		{
			username1 = null;
		}
		data = dao.getQueryUserInfo(infoType, cityIdList, username1, starttime1,
				endtime1, packageId, timeType, bindState, ouiState);
		fileName = "userInfo";
		return SUCCESS;
	}

	public void setUsername1(String username1)
	{
		logger.debug("setUsername1({})", username1);
		if ("".equals(username1))
		{
			username1 = null;
		}
		this.username1 = username1;
	}

	public void setSession(Map session)
	{
		logger.debug("setSession(Map)");
		this.session = session;
	}

	/**
	 * select
	 * 
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception
	{
		logger.debug("execute()");

		// 取得session中的user属性
		dbUserRes = (DbUserRes) session.get("curUser");
		user = dbUserRes.getUser();
		//infoType = "" + LipossGlobals.SystemType();
		infoType =gw_type;
		// 取得属地列表
		CityList = CityDAO.getAllNextCityListByCityPid(user.getCityId());
		PackageList = dao.getPackageList();
		title = dao.getTitle(infoType, bindState);
		isXJ = false == LipossGlobals.isXJDX() ? "0" : "1";
		isHljDx=false==Global.HLJDX.equals(Global.instAreaShortName)?"0":"1";
		// 判断是否是菜单进入，是则不查询 by 张聪 2011-06-02
		String query = getNo_query();
		if (query == null || query.trim().equalsIgnoreCase("")) 
		{
			if ("1".equals(gw_type)) 
			{
				if (Global.JSDX.equals(Global.instAreaShortName)) 
				{
					isJSITMS = "1";
				}
			}
			this.setTime();
			this.setCity(user.getCityId());
			if (username1 == null || "".equals(username1.trim()) == true) 
			{
				username1 = null;
			}
			data = dao.getQueryPageUserInfo(infoType, cityIdList, username1,
					starttime1, endtime1, packageId, timeType, bindState,
					ouiState, stroffset);
		} else 
		{
			//从菜单进入则直接置空结果集,避免查询时间太长，客户体验差by 张聪 2011-06-02
			data = new ArrayList<Map>();
		}
		return "list";
	}

	/**
	 * (云南bbms需求)按订单类型查询用户
	 * 
	 * @author wangsenbo
	 * @date Oct 25, 2010
	 * @param
	 * @return String
	 */
	public String userByOrder()
	{
		// 取得属地列表
		dbUserRes = (DbUserRes) session.get("curUser");
		user = dbUserRes.getUser();
		//infoType = "" + LipossGlobals.SystemType();
		infoType =gw_type;
		CityList = CityDAO.getAllNextCityListByCityPid(user.getCityId());
		this.setCity(user.getCityId());
		this.setTime();
		data = dao.getUserByOrder(infoType, cityIdList, null, starttime1, endtime1, "3",
				bindState, stroffset, bigOrderType, orderType);
		return "userByOrder";
	}

	/**
	 * (云南bbms需求)按订单类型查询用户导出
	 */
	public String userByOrderExcel()
	{
		// 取得session中的user属性
		dbUserRes = (DbUserRes) session.get("curUser");
		user = dbUserRes.getUser();
		//infoType = "" + LipossGlobals.SystemType();
		infoType =gw_type;
		column = new String[] { "city_name", "opendate", "customer_name", "username",
				"ordertype", "bandstate" };
		// 取得属地列表
		CityList = CityDAO.getAllNextCityListByCityPid(user.getCityId());
		title = new String[] { "属地", "开户时间", "客户名称", "用户账号", "订单类型", "绑定状态" };
		this.setTime();
		this.setCity(user.getCityId());
	
		data = dao.getUserByOrderExcel(infoType, cityIdList, null, starttime1, endtime1,
				"3", bindState, bigOrderType, orderType);
		fileName = "userInfo";
		return SUCCESS;
	}

	public void setModel(Object id)
	{
		logger.debug("setModel({})", id);
		infoType = id.toString();
	}

	public String getUsername1()
	{
		logger.debug("getUsername1()");
		return username1;
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
	 * 分析属地，如果属地为-1/00 则不要过滤，否则取当前属地及子属地
	 */
	private void setCity(String _cityId)
	{
		logger.debug("setCity");
		if (null == cityId || "-1".equals(cityId) || "".equals(cityId))
		{
			cityId = _cityId;
		}
		if ("00".equals(cityId))
		{
			cityIdList = null;
		}
		else
		{
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
		}
	}

	/**
	 * get CityList
	 * 
	 * @return
	 */
	public List getCityList()
	{
		logger.debug("getCityList()");
		return CityList;
	}

	/**
	 * set cityList
	 * 
	 * @param cityList
	 */
	public void setCityList(List cityList)
	{
		logger.debug("setCityList(cityList)");
		CityList = cityList;
	}

	/**
	 * get:
	 * 
	 * @return the cityId
	 */
	public String getCityId()
	{
		logger.debug("getCityId()");
		return cityId;
	}

	/**
	 * set:
	 * 
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(String cityId)
	{
		logger.debug("setCityId({})", cityId);
		this.cityId = cityId;
	}

	/**
	 * get:
	 * 
	 * @return the packageId
	 */
	public String getPackageId()
	{
		logger.debug("getPackageId()");
		return packageId;
	}

	/**
	 * set:
	 * 
	 * @param packageId
	 *            the packageId to set
	 */
	public void setPackageId(String packageId)
	{
		logger.debug("setPackageId({})", packageId);
		this.packageId = packageId;
	}

	/**
	 * get:
	 * 
	 * @return the packageList
	 */
	public List<Map<String, String>> getPackageList()
	{
		logger.debug("getPackageList()");
		return PackageList;
	}

	/**
	 * @return the bindState
	 */
	public String getBindState()
	{
		return bindState;
	}

	/**
	 * @param bindState
	 *            the bindState to set
	 */
	public void setBindState(String bindState)
	{
		this.bindState = bindState;
	}

	/**
	 * @return the timeType
	 */
	public String getTimeType()
	{
		return timeType;
	}

	/**
	 * @param timeType
	 *            the timeType to set
	 */
	public void setTimeType(String timeType)
	{
		this.timeType = timeType;
	}

	/**
	 * @return the ouiState
	 */
	public String getOuiState()
	{
		return ouiState;
	}

	/**
	 * @param ouiState
	 *            the ouiState to set
	 */
	public void setOuiState(String ouiState)
	{
		this.ouiState = ouiState;
	}

	/**
	 * @return the isXJ
	 */
	public String getIsXJ()
	{
		return isXJ;
	}

	/**
	 * @param isXJ
	 *            the isXJ to set
	 */
	public void setIsXJ(String isXJ)
	{
		this.isXJ = isXJ;
	}

	/**
	 * @return the isJSITMS
	 */
	public String getIsJSITMS()
	{
		return isJSITMS;
	}

	/**
	 * @param isJSITMS
	 *            the isJSITMS to set
	 */
	public void setIsJSITMS(String isJSITMS)
	{
		this.isJSITMS = isJSITMS;
	}

	/**
	 * @return the bigOrderType
	 */
	public String getBigOrderType()
	{
		return bigOrderType;
	}

	/**
	 * @param bigOrderType
	 *            the bigOrderType to set
	 */
	public void setBigOrderType(String bigOrderType)
	{
		this.bigOrderType = bigOrderType;
	}

	/**
	 * @return the orderType
	 */
	public String getOrderType()
	{
		return orderType;
	}

	/**
	 * @param orderType
	 *            the orderType to set
	 */
	public void setOrderType(String orderType)
	{
		this.orderType = orderType;
	}


	public String getIsHljDx()
	{
		return
				isHljDx;
	}


	public void setIsHljDx(String isHljDx)
	{
		this.isHljDx =
				isHljDx;
	}
	
}
