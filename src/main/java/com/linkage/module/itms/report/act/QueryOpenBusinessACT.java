
package com.linkage.module.itms.report.act;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import action.splitpage.splitPageAction;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.itms.report.bio.QueryOpenBusinessBIO;

@SuppressWarnings("rawtypes")
public class QueryOpenBusinessACT extends splitPageAction implements SessionAware
{

	private List cityList = new ArrayList();
	private String city_id = "";
	private String city_name = "";
	private String starttime = "";
	/** 开始时间 */
	private String endtime = "";
	/** 结束时间 */
	private String dataType = "";
	private String[] title;
	/** 导出数据使用的List **/
	private List<Map> userMap = new ArrayList<Map>();
	private List<Map> listDetail = new ArrayList<Map>();
	private List<Map<String, String>> strCityList = new ArrayList<Map<String, String>>();;
	/** session */
	private Map session;
	/** BIO */
	private QueryOpenBusinessBIO queryBusinessBio;
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(QueryOpenBusinessACT.class);

	public String execute()
	{
		String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -10);
		/* 结束时间是当前时间（后退了10分钟） */
		this.endtime = sdf.format(cal.getTime());
		/* 开始时间当天  */
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		this.starttime = sdf.format(cal.getTime());
		/** 处理属地的情况 **/
		UserRes curUser = (UserRes) session.get("curUser");
		String cityId = curUser.getCityId();
		/**只获取二级属地**/
		strCityList = CityDAO.getNextCityListByCityPid(cityId);
		return "init";
	}
	/**
	 * 根据下拉列表传过来的city_id和事件进行查询！
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getDayReport()
	{
		this.cityList = queryBusinessBio.getChildCityList(city_id);
		queryBusinessBio.prepareForToalQuery(starttime, endtime);
		/** 开始遍历每一级的子城市 （包括自己） **/
		for (int i = 0; i < this.cityList.size(); i++)
		{
			Map cityMap = (Map) cityList.get(i);
			logger.debug("ACT->getDatReport->for->cityid={}", cityMap.get("city_id"));
			/**
			 * 获取每一个城市的的统计信息，我们需要循环去使用已经查询过数据库得到的queryBusinessBio中的totalList
			 * 注意成功数和失败数成功率都是在BIO层中进行处理返回的！
			 */
			Map oneZeroDataMap = queryBusinessBio.queryUserByCityId((String) cityMap
					.get("city_id"));
			oneZeroDataMap.put("city_id", (String) cityMap.get("city_id"));
			oneZeroDataMap.put("hasCityId", (String) cityMap.get("hasCityId"));
			oneZeroDataMap.put("city_name", cityMap.get("city_name"));
			userMap.add(oneZeroDataMap);
		}
		/* 设置显示的标题 */
		this.title = new String[5];
		this.title[0] = "属地";
		this.title[1] = "总数";
		this.title[2] = "成功数";
		this.title[3] = "失败数";
		this.title[4] = "成功率";
		return "userList";
	}

	/**
	 * 分页:QueryOpenBusinessACT需要继承splitPageAction！
	 * 这个函数的主要功能就是根据查询页面的总数、成功数、失败数
	 * @return
	 */
	public String gopageUserList()
	{
		logger.warn("gopageUserList=====>curPage_splitPage={}", curPage_splitPage);
		listDetail = queryBusinessBio.queryDetailListPage(dataType, city_id, starttime,
				endtime, curPage_splitPage, num_splitPage);
		/** 获取总的页数 **/
		maxPage_splitPage = queryBusinessBio.getDeviceCount(dataType, city_id, starttime,
				endtime, curPage_splitPage, num_splitPage);
		return "detailList";
	}

	/* 导出文件的文件名 */
	private String fileName;
	private String[] column;
	private List<Map> data = new ArrayList<Map>();

	@SuppressWarnings("unchecked")
	public String getExcel()
	{
		fileName = "当日开通用户数查询结果";
		title = new String[] { "属地", "用户总数", "开通用户数", "开通失败数", "开通成功率" };
		column = new String[] { "city_name", "total", "success", "fail", "pert" };
		this.cityList = queryBusinessBio.getChildCityList(city_id);
		queryBusinessBio.prepareForToalQuery(starttime, endtime);
		for (int i = 0; i < this.cityList.size(); i++)
		{
			Map cityMap = (Map) cityList.get(i);
			logger.warn("ACT->getDatReport->for->cityid={}", cityMap.get("city_id"));
			Map oneZeroDataMap = queryBusinessBio.queryUserByCityId((String) cityMap
					.get("city_id"));
			oneZeroDataMap.put("city_name", cityMap.get("city_name"));
			data.add(oneZeroDataMap);
		}
		return "excel";
	}

	/**
	 * 详细信息报表获取": 需要的参数： 属地：city_id Excel表的类型：dataType
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getDetailExcel()
	{
		if (dataType.equals("1"))
		{
			fileName = "开通用户总数明细";
		}
		else if (dataType.equals("2"))
		{
			fileName = "开通用户成功数明细";
		}
		else
		{
			fileName = "开通用户失败数明细";
		}
		title = new String[] { "属地", "逻辑SN", "设备序列号", "绑定时间" };
		column = new String[] { "city_name", "username", "device_serialnumber",
				"binddate" };
		data = queryBusinessBio.queryDetailListExcel(dataType, city_id, starttime,
				endtime);
		Map<String, String> allCityMap = CityDAO.getCityIdCityNameMap();
		for (int i = 0; i < data.size(); i++)
		{
			Map map = data.get(i);
			map.put("city_name", allCityMap.get(map.get("city_id")));
		}
		logger.warn("================>data size={}", data.size());
		return "excel";
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

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getDataType()
	{
		return dataType;
	}

	public void setDataType(String dataType)
	{
		this.dataType = dataType;
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

	public String[] getTitle()
	{
		return title;
	}

	public void setTitle(String[] title)
	{
		this.title = title;
	}

	public List<Map> getUserMap()
	{
		return userMap;
	}

	public void setUserMap(List<Map> userMap)
	{
		this.userMap = userMap;
	}

	public QueryOpenBusinessBIO getQueryBusinessBio()
	{
		return queryBusinessBio;
	}

	public void setQueryBusinessBio(QueryOpenBusinessBIO queryBusinessBio)
	{
		this.queryBusinessBio = queryBusinessBio;
	}

	public String getCity_id()
	{
		return city_id;
	}

	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	public String getCity_name()
	{
		return city_name;
	}

	public void setCity_name(String city_name)
	{
		this.city_name = city_name;
	}

	public List<Map> getListDetail()
	{
		return listDetail;
	}

	public void setListDetail(List<Map> listDetail)
	{
		this.listDetail = listDetail;
	}

	public List<Map<String, String>> getStrCityList()
	{
		return strCityList;
	}

	public void setStrCityList(List<Map<String, String>> strCityList)
	{
		this.strCityList = strCityList;
	}

	public Map getSession()
	{
		return session;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}
}
