package action.report;

import static action.cst.OK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;
import bio.report.NetWarnQueryBIO;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.dbimpl.DbUserRes;
import com.linkage.module.gwms.dao.gw.EventLevelLefDAO;

import dao.report.NetWarnQueryDAO;

/**
 * 历史告警查询
 * 
 * @author benyp(5260) 2008-1-3 王志猛
 * @author suixz(5253)
 * @version 1.1
 * @category:大客户使用，其中有中英文。 包括历史告警查询、分页、导出等功能
 */
public class NetWarnQueryAction extends splitPageAction implements SessionAware
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(NetWarnQueryAction.class);
	private User user; // 用户资源
	private DbUserRes dbUserRes;
	private Map session; // 服务器的会话对象
	private NetWarnQueryDAO nwqd; // 历史查询的dao类
	private NetWarnQueryBIO wqb;
	private String gatherid; // 采集点id
	private String severity_id; // 告警等级
	private String sourcename_name; // 设备名
	private String restype_id; // 设备层次
	private String grade; // 告警等级
	private String starttime; // 开始时间
	private String endtime; // 结束时间
	private String ip; // 设备ip
	private String dev_name; // 设备名称
	private String keyword; // 告警关键字
	private int maxnum; // 
	
	private String actstatus; // 确认状态
	private String clearstatus;// 清除状态
	private String creatortype;// 告警类型
	private String city_id;// 属地
	private int num; // 告警种类的数目
	private String local; // 当前中英文session：zh_CN,en_US
	private List GatherList; // 采集机列表
	private List LayList; // 设备层次列表
	private List WarnList; // 告警列表
	private List WarnListDetail; // 告警列表
	
	private List WarnNumList; // 告警个数列表
	private Map TitleMap; // 告警等级列名
	private Map warnMap;//告警Map
	private List TitleList; // 告警等级列名
	private List CityList;// 属地列表
	private String searchType;// 查询类型
	// ********Export All Data To Excel****************
	private List<Map> data; // 需要导出的结果集，必须是处理过的可以直接显示的结果
	private String[] column; // 需要导出的列名，对应data中的键值
	private String[] title; // 显示在导出文档上的列名
	private String fileName; // 导出的文件名（不包括后缀名）
	
	private long startTime_detail;
	private long endTime_detail;
	private String gather_id_detail;

	private EventLevelLefDAO eventLevelLefDao;
	// ************************************************
	
	/**
	 * @return the eventLevelLefDao
	 */
	public EventLevelLefDAO getEventLevelLefDao()
	{
		return eventLevelLefDao;
	}

	
	/**
	 * @param eventLevelLefDao the eventLevelLefDao to set
	 */
	public void setEventLevelLefDao(EventLevelLefDAO eventLevelLefDao)
	{
		this.eventLevelLefDao = eventLevelLefDao;
	}

	/**
	 * 查询条件初始化
	 */
	public String execute() throws Exception
	{
		// 取得session中的user属性
		dbUserRes = (DbUserRes) session.get("curUser");
		user = dbUserRes.getUser();
		// 取得采集点列表
		GatherList = nwqd.getGatherList(user.getAreaId());
		// 取得属地列表
		CityList = nwqd.getCityList(user.getCityId());
		// 取得设备层次列表
		// LayList = nwqd.getResourceTypeList();
		//获取告警列表Map
		warnMap = eventLevelLefDao.getWarnLevel();
		return SUCCESS;
	}

	/**
	 * 导出所有数据
	 * 
	 * @return OK-excel
	 * @throws Exception
	 */
	public String ExportAll() throws Exception
	{
		// 取得session中的user属性
		dbUserRes = (DbUserRes) session.get("curUser");
		user = dbUserRes.getUser();
		// 定义导出文件名
		fileName = "warn";
		// 定义导出字段的键值及列名
		getTitleandKey();
		logger.debug("grade=" + grade);
		// 获取告警等级Map，实现中英文切换
		WarnMap();
		// 获取要导出的详细数据
		data = nwqd.getAllHisWarnData(restype_id, gatherid, grade, Long
				.parseLong(starttime), Long.parseLong(endtime), ip, dev_name,
				actstatus, clearstatus, creatortype, city_id, user.getAreaId(),
				user.isAdmin(), TitleMap);
		return OK;
	}

	/**
	 * 得到列值和列所对应的key
	 */
	private void getTitleandKey()
	{
		column = new String[8]; // 定义健值数组长度
		title = new String[8]; // 定义列标题数组长度
		// key
		column[0] = "serialno";
		column[1] = "city";
		column[2] = "creatorname";
		column[3] = "sourceip";
		column[4] = "createtime";
		column[5] = "severity";
		column[6] = "sourcename";
		column[7] = "displaystring";
		// title
		title[0] = "ID";
		title[1] = "属地";
		title[2] = "告警源"; // 告警源
		title[3] = "设备IP地址"; // 设备IP地址
		title[4] = "创建时间"; // 创建时间
		title[5] = "等级"; // 等级
		title[6] = "设备名称"; // 设备名称
		title[7] = "告警内容"; // 告警内容
	}

	/**
	 * 查询告警信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String Query() throws Exception
	{
		logger.debug("**************************History Warn****************");
		logger.debug("actstatus=" + actstatus);
		logger.debug("clearstatus=" + clearstatus);
		logger.debug("creatortype=" + creatortype);
		logger.debug("city_id=" + city_id);
		logger.debug("******************************************************");
		// 设置每页显示的页数
		setNum_splitPage(50);
		// 取得session中的user属性
		dbUserRes = (DbUserRes) session.get("curUser");
		user = dbUserRes.getUser();
		// 返回选中的告警等级的个数
		num = (grade != null && !"".equals(grade.trim())) ? grade.split(",").length
				: 1;
		long st = 0; // 开始时间
		long et = 0; // 结束时间
		DateTimeUtil dt;// 定义DateTimeUtil
		// 开始时间不为空时，将开始时间转化为long型
		if (!"".equals(starttime))
		{
			dt = new DateTimeUtil(starttime);
			st = dt.getLongTime();
			starttime = String.valueOf(st); // 为了在结果页面察看每条告警的详细信息，作为页面的参数，将时间转为长整形
		}
		// 结束时间不为空时，将结束时间转化为long型
		if (!"".equals(endtime))
		{
			dt = new DateTimeUtil(endtime);
			et = dt.getLongTime();
			endtime = String.valueOf(et); // 为了在结果页面察看每条告警的详细信息，作为页面的参数，将时间转为长整形
		}
		// 取得各告警等级的记录条数
		List list = wqb.getSeverityAndWarnNum(restype_id, gatherid, grade, st,
				et, ip, dev_name, actstatus, clearstatus, creatortype, city_id,
				user.getAreaId(), user.isAdmin());
		maxPage_splitPage = 0; // 总共显示的页数
		WarnMap(); // 取得列名称
		WarnNumList = new ArrayList();
		Map tmpmap = new HashMap();
		// ********************************************//
		// 根据初使页面选中的告警等级，封装成同样顺序的list返回
		// 由于Map无序，所以用数组循环匹配，目的就是为了顺序不变，同时加上告警等级的相应中英文说明(标题)
		if (list != null && list.size() > 0)
		{
			String s[] = grade.split(",");
			int n = list.size();
			for (int i = 0; i < num; i++)
			{
				for (int j = 0; j < n; j++)
				{
					tmpmap = (Map) list.get(j);
					if (tmpmap.get("severity") != null
							&& tmpmap.get("severity").toString().trim().equals(
									s[i].trim()))
					{
						maxPage_splitPage += Integer.parseInt(tmpmap.get("num")
								.toString());
						tmpmap.put("title", TitleMap.get(s[i].trim()));
						WarnNumList.add(tmpmap);
						tmpmap = null;
					}
				}
				if (!WarnNumList.toString().contains("severity=" + s[i].trim()))
				{
					tmpmap = new HashMap();
					tmpmap.put("severity", s[i].trim());
					tmpmap.put("num", "0");
					tmpmap.put("title", TitleMap.get(s[i].trim()));
					WarnNumList.add(tmpmap);
					tmpmap = null;
				}
			}
			// 计算最大页大小
			maxPage_splitPage = ((maxPage_splitPage % num_splitPage == 0) ? (maxPage_splitPage / num_splitPage)
					: (maxPage_splitPage / num_splitPage + 1));
			// 取得告警的详细信息
			WarnList = nwqd.getHisWarnInfo(restype_id, gatherid, grade, st, et,
					ip, dev_name, actstatus, clearstatus, creatortype, city_id,
					user.getAreaId(), user.isAdmin(), curPage_splitPage,
					num_splitPage, TitleMap);
		}
		return "query";
	}

	/**
	 * 获取告警等级(列标题)
	 * 
	 * @return 2008-1-22 Administrator
	 */
	private void WarnMap()
	{
		TitleMap = new HashMap(); // 各告警等级对应的中英文
		TitleList = new ArrayList(); // 选中的告警等级的中英文
		warnMap = eventLevelLefDao.getWarnLevel();
		Map map = new HashMap();
		String[] tmp = grade.split(",");// 告警等级
		int n = tmp.length;
		for (int i = 0; i < n; i++)
		{
			map = new HashMap();
			TitleMap.put(tmp[i].trim(), warnMap.get(tmp[i].trim()));
			map.put("severity", warnMap.get(tmp[i].trim()));
//			if ("5".equals(tmp[i].trim()))
//			{
//				TitleMap.put("5", "紧急告警");
//				map.put("severity", "紧急告警");
//			} else if ("4".equals(tmp[i].trim()))
//			{
//				TitleMap.put("4",  "严重告警");
//				map.put("severity",  "严重告警");
//			} else if ("3".equals(tmp[i].trim()))
//			{
//				TitleMap.put("3",  "一般告警");
//				map.put("severity",  "一般告警");
//			} else if ("2".equals(tmp[i].trim()))
//			{
//				TitleMap.put("2", "提示告警");
//				map.put("severity", "提示告警");
//			} else if ("1".equals(tmp[i].trim()))
//			{
//				TitleMap.put("1", "正常日志");
//				map.put("severity", "正常日志");
//			} else if ("0".equals(tmp[i].trim()))
//			{
//				TitleMap.put("0", "自动清除");
//				map.put("severity", "自动清除");
//			}
			TitleList.add(map);
		}
		map = null;
	}

	public String getDevReport() throws Exception
	{
		long st = 0; // 开始时间
		long et = 0; // 结束时间
		DateTimeUtil dt;// 定义DateTimeUtil
		// 开始时间不为空时，将开始时间转化为long型
		if (!"".equals(starttime))
		{
			dt = new DateTimeUtil(starttime);
			st = dt.getLongTime();
			starttime = String.valueOf(st); // 为了在结果页面察看每条告警的详细信息，作为页面的参数，将时间转为长整形
		}
		// 结束时间不为空时，将结束时间转化为long型
		if (!"".equals(endtime))
		{
			dt = new DateTimeUtil(endtime);
			et = dt.getLongTime();
			endtime = String.valueOf(et); // 为了在结果页面察看每条告警的详细信息，作为页面的参数，将时间转为长整形
		}
		// 取得各告警等级的记录条数
		nwqd.getDevReport(restype_id, gatherid, grade, st, et, ip, dev_name,
				actstatus, clearstatus, creatortype, city_id, user.getAreaId(),
				user.isAdmin());

		return "";
	}

	// =======================================suixz(5253)
	// 北京企业网关测试=====================
	/**
	 * 统计每台设备的告警条数
	 */
	public String queryWarnInfoByDev() throws Exception
	{
		// 设置每页显示的页数
		setNum_splitPage(10);
		dbUserRes = (DbUserRes) session.get("curUser");
		user = dbUserRes.getUser();
		long st = 0; // 开始时间
		long et = 0; // 结束时间
		DateTimeUtil dt;// 定义DateTimeUtil
		// 开始时间不为空时，将开始时间转化为long型
		if (!"".equals(starttime))
		{
			dt = new DateTimeUtil(starttime);
			st = dt.getLongTime();
			
			starttime = String.valueOf(st); // 为了在结果页面察看每条告警的详细信息，作为页面的参数，将时间转为长整形
		}
		// 结束时间不为空时，将结束时间转化为long型
		if (!"".equals(endtime))
		{
			dt = new DateTimeUtil(endtime);
			et = dt.getLongTime();
			
			endtime = String.valueOf(et); // 为了在结果页面察看每条告警的详细信息，作为页面的参数，将时间转为长整形
		}
		maxPage_splitPage = 0; // 总共显示的页数
//		List<Map> list = new ArrayList<Map>();
		List<Map> list = nwqd.queryWarnNumByDevice("", gatherid, "", st,
				et, "", "", "", "", "", city_id,
				user.getAreaId(), user.isAdmin());
		
		if (null == list) {
			return action.cst.LIST;
		}
		for(Map m:list){
			if(Integer.parseInt(m.get("num").toString())!=0){
				maxPage_splitPage++;
			}
		}
		
		
		maxPage_splitPage = ((maxPage_splitPage % num_splitPage == 0) ? (maxPage_splitPage / num_splitPage)
				: (maxPage_splitPage / num_splitPage + 1));
		WarnList = nwqd.queryWarnInfoByDevice("", gatherid, "", st,
				et, "", "", "", "", "", city_id,
				user.getAreaId(), user.isAdmin(), curPage_splitPage,
				num_splitPage, TitleMap);
		
		return action.cst.LIST;
	}

	/**
	 * 针对某个设备，某个等级的告警，查看其详细信息
	 */
	public String detailWarnInfoByDev() throws Exception
	{
//		logger.debug("severity_id:" + severity_id);
//		logger.debug("sourcename_name:" + sourcename_name);
//		logger.debug("starttime:" + starttime);
//		logger.debug("endtime:" + endtime);
//		logger.debug("maxPage_splitPage:" + maxPage_splitPage);
//		logger.debug("num_splitPage:" + num_splitPage);
		
		logger.debug("gatherid:" + gatherid);
		long st = Long.parseLong(starttime);
		long et = Long.parseLong(endtime);
		Map TitleMap = new HashMap();
		TitleMap = eventLevelLefDao.getWarnLevel();
//		TitleMap.put("5", "紧急告警");
//		TitleMap.put("4", "严重告警");
//		TitleMap.put("3", "一般告警");
//		TitleMap.put("2", "提示告警");
//		TitleMap.put("1", "正常日志");
//		TitleMap.put("0", "自动清除");
		
		if (null == gatherid || "".equals(gatherid)) {
			dbUserRes = (DbUserRes) session.get("curUser");
			user = dbUserRes.getUser();
			// 取得采集点列表
			gatherid = nwqd.getGatherListStr(user.getAreaId());
		}
		
		//logger.debug("gatherid:" + gatherid);
		
		// 计算最大页大小
		maxPage_splitPage = ((maxnum % num_splitPage == 0) ? (maxnum / num_splitPage)
				: (maxnum / num_splitPage + 1));
		
		// 取得告警的详细信息
		WarnListDetail = nwqd.getHisWarnInfoDetail("", gatherid, severity_id, st, et,
				ip, sourcename_name, "", "", "", city_id,
				user.getAreaId(), user.isAdmin(), curPage_splitPage,
				num_splitPage, TitleMap);
		//logger.debug("WarnListDetail:" + WarnListDetail);
		return "detail";
	}
	
	
	
	public String getSeverity_id()
	{
		return severity_id;
	}
	public void setSeverity_id(String severity_id)
	{
		this.severity_id = severity_id;
	}
	
	public String getSourcename_name()
	{
		return sourcename_name;
	}
	public void setSourcename_name(String sourcename_name)
	{
		this.sourcename_name = sourcename_name;
	}
	
	public String getGatherid()
	{
		return gatherid;
	}

	public void setGatherid(String gatherid)
	{
		this.gatherid = gatherid;
	}

	public String getGrade()
	{
		return grade;
	}

	public void setGrade(String grade)
	{
		this.grade = grade;
	}

	public String getStarttime()
	{
		return starttime;
	}

	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	public String getEndtime()
	{
		return endtime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getDev_name()
	{
		return dev_name;
	}

	public void setDev_name(String dev_name)
	{
		this.dev_name = dev_name;
	}

	public String getKeyword()
	{
		return keyword;
	}

	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}

	public void setNwqd(NetWarnQueryDAO nwqd)
	{
		this.nwqd = nwqd;
	}

	public List getGatherList()
	{
		return GatherList;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public void setDbUserRes(DbUserRes dbUserRes)
	{
		this.dbUserRes = dbUserRes;
	}

	public List getWarnList()
	{
		return WarnList;
	}

	public List getWarnListDetail()
	{
		return WarnListDetail;
	}
	
	
	
	public List getWarnNumList()
	{
		return WarnNumList;
	}

	public int getNum()
	{
		return num;
	}

	public List<Map> getData()
	{
		return data;
	}

	public void setData(List<Map> data)
	{
		this.data = data;
	}

	public String[] getColumn()
	{
		return column;
	}

	public void setColumn(String[] column)
	{
		this.column = column;
	}

	public String[] getTitle()
	{
		return title;
	}

	public void setTitle(String[] title)
	{
		this.title = title;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public void setWqb(NetWarnQueryBIO wqb)
	{
		this.wqb = wqb;
	}

	public List getLayList()
	{
		return LayList;
	}

	public Map getTitleMap()
	{
		return TitleMap;
	}

	public String getRestype_id()
	{
		return restype_id;
	}

	public void setRestype_id(String restype_id)
	{
		this.restype_id = restype_id;
	}

	public List getTitleList()
	{
		return TitleList;
	}

	public String getLocal()
	{
		return local;
	}

	public String getActstatus()
	{
		return actstatus;
	}

	public void setActstatus(String actstatus)
	{
		this.actstatus = actstatus;
	}

	public String getClearstatus()
	{
		return clearstatus;
	}

	public void setClearstatus(String clearstatus)
	{
		this.clearstatus = clearstatus;
	}

	public String getCreatortype()
	{
		return creatortype;
	}

	public void setCreatortype(String creatortype)
	{
		this.creatortype = creatortype;
	}

	public String getCity_id()
	{
		return city_id;
	}

	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	public List getCityList()
	{
		return CityList;
	}

	public void setCityList(List cityList)
	{
		CityList = cityList;
	}

	public String getSearchType()
	{
		return searchType;
	}

	public void setSearchType(String searchType)
	{
		this.searchType = searchType;
	}

	public int getMaxnum() {
		return maxnum;
	}

	public void setMaxnum(int maxnum) {
		this.maxnum = maxnum;
	}

	
	/**
	 * @return the warnMap
	 */
	public Map getWarnMap()
	{
		return warnMap;
	}

	
	/**
	 * @param warnMap the warnMap to set
	 */
	public void setWarnMap(Map warnMap)
	{
		this.warnMap = warnMap;
	}

}
