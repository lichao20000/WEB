package action.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.report.StrategyConfigStatBIO;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Jason(3412)
 * @date 2009-4-29
 */
public class StrategyConfigStatAction extends ActionSupport implements
		SessionAware {

	/** log */
	private static Logger log = LoggerFactory
			.getLogger(StrategyConfigStatAction.class);

	// 开始时间
	private long startTime;
	// 结束时间
	private long endTime;
	// 属地
	private String cityId;
	// 属地下拉列表
	private List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();
	/**执行状态
	 * 1表示执行成功，0表示未执行，-1表示执行失败，100全部
	 * */
	private String exeStatus;
	// 策略业务ID
	private String serviceId;
	// BIO
	private StrategyConfigStatBIO statBio;
	// session用户取登陆用户属地信息
	private Map session;
	//统计数据
	private String ajax;

	//excel导出相关字段
	private String[] title;
	private String[] column;
	private ArrayList<Map> data;
	private String fileName;
	
	
	
	public String execute() {
		// 如果没有属地信息，则取当前用户的属地
		if (null == cityId || "".equals(cityId) || "-1".equals(cityId)) {
			UserRes curUser = (UserRes) session.get("curUser");
			
			cityId = curUser.getCityId();
			log.debug("curUser.city_id: " + cityId);
		}
		// 属地下拉列表
		
		cityList = CityDAO.getAllNextCityListByCityPid(cityId);
		
//		Map cMap = CityDAO.getAllNextCityMapByCityPid(cityId);
//		
//		
//		Map<String, String> map = null;
//		
//		Iterator it = cMap.keySet().iterator();
//		while(it.hasNext()){
//			String key = (String) it.next();
//			map = new HashMap<String, String>();
//			map.put("city_id", key);
//			map.put("city_name", (String)cMap.get(key));
//			
//			cityList.add(map);
//		}
//		
//		logger.debug("-------------2:" + cityList);
		
		// 返回页面
		return "success";
	}

	/**
	 * 统计
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-8
	 * @return String
	 */
	public String statExecute() {
		// 如果没有属地信息，则取当前用户的属地
		if (null == cityId || "".equals(cityId) || "-1".equals(cityId)) {
			UserRes curUser = (UserRes) session.get("curUser");
			cityId = curUser.getCityId();
		}
		log.debug("serviceId:" + serviceId);
		log.debug("cityId:" + cityId);
		log.debug("startTime" + startTime);
		log.debug("endTime" + endTime);
		List<Map> statList = statBio.getStatResList(Integer
				.valueOf(serviceId), cityId, startTime, endTime);
		ajax = statBio.statListToString(statList, serviceId, startTime, endTime);
		return "ajax";
	}

	
	/**
	 * 获取设备列表，导出excel文件
	 * 
	 * @author Jason(3412)
	 * @date 2009-5-6
	 * @return String
	 */
	public String servUserListExecute() {
		//列标题
		title = new String[]{"属地","用户账号","设备序列号","厂商","硬件版本","软件版本","设备型号","配置时间"};
		//data字段Map对应的字段名称
		column = new String[]{"city_name", "username","device_serialnumber","vendor_name","hardwareversion","softwareversion","device_model","time"};
		//数据
		data = statBio.getServUserListData(serviceId, cityId, exeStatus, startTime, endTime);
		//文件名称
		fileName = "iptvUserStat" + Math.round(Math.random()*100);
		return "excel";
	}
	
	
//	/**
//	 * 获取设备列表，导出excel文件
//	 * 
//	 * @author Jason(3412)
//	 * @date 2009-5-6
//	 * @return String
//	 */
//	public String devListExecute() {
//		//列标题
//		title = new String[]{"用户账号","设备序列号","配置时间"};
//		//data字段Map对应的字段名称
//		column = new String[]{"username","device_serialnumber","time"};
//		//数据
//		data = statBio.getDevListData(serviceId, cityId, exeStatus, startTime, endTime);
//		//文件名称
//		fileName = "configStat" + Math.round(Math.random()*100);
//		return "excel";
//	}

	public void setStartTime(String startTime) {
		this.startTime = Long.valueOf(startTime);
	}

	public void setEndTime(String endTime) {
		this.endTime = Long.valueOf(endTime);
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public void setExeStatus(String exeStatus) {
		this.exeStatus = exeStatus;
	}

	public void setStatBio(StrategyConfigStatBIO statBio) {
		this.statBio = statBio;
	}

	public List getCityList() {
		return cityList;
	}

	public void setCityList(List cityList) {
		this.cityList = cityList;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getAjax() {
		return ajax;
	}

	public String[] getTitle() {
		return title;
	}

	public String[] getColumn() {
		return column;
	}

	public ArrayList<Map> getData() {
		return data;
	}

	public String getFileName() {
		return fileName;
	}

	@Override
	public void setSession(Map arg0) {
		session = arg0;
	}

}
