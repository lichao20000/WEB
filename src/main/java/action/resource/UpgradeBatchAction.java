package action.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.resource.DeviceActUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;

import dao.resource.ItvConfigDAO;
import dao.resource.UpgradeBatchDAO;
import dao.share.SelectDeviceTagDAO;

/**
 * @author Jason(3412)
 * @date 2009-4-8
 */
public class UpgradeBatchAction implements ServletRequestAware {
	private static final Logger LOG = LoggerFactory
			.getLogger(UpgradeBatchAction.class);

	/** request取登陆帐号使用 * */
	private HttpServletRequest request;

	/** 属地ID* */
	private String selectCity;
	/** 属地List* */
	private List cityList;

	/** 厂商ID* */
	private String selectVendor;
	/** 厂商List* */
	private List vendorList;

	/** 型号ID* */
	private String selectDevModel;
	/** 型号List* */
	private List devModelList;

	/** 版本ID* */
	private String selectDevType;
	/** 版本List* */
	private List devTypeList;

	private List device_id;
	/** 设备ID List* */
	private List deviceList;

	/** *软件版本ID* */
	private String selectSoftfile;
	/** *软件版本List* */
	private List softfileList;

	/** *策略执行方式ID* */
	private String selectExecuteType;
	/** *策略执行方式List* */
	private List executeTypeList;

	/** *获取设备DAO* */
	private SelectDeviceTagDAO selectDeviceTagDAO;
	/** *处理软件升级策略DAO* */
	private UpgradeBatchDAO upgradeBatchDAO;
	/** *入任务策略表DAO* */
	private ItvConfigDAO itvConfigDAO;

	private String ajax;

	/**
	 * 软件批量升级配置页面入口： 初始化软件版本文件列表，以及策略方式列表
	 * 
	 * @author Jason(3412)
	 * @date 2009-4-8
	 * @return String
	 */
	public String execute() {
		// city_id init
		if (selectCity == null || "".equals(selectCity)
				|| "-1".equals(selectCity)) {
			UserRes curUser = (UserRes) request.getSession().getAttribute(
					"curUser");
			selectCity = curUser.getCityId();
		}
		LOG.debug("city_id :" + selectCity);
		// get city List
		cityList = CityDAO.getAllNextCityListByCityPid(selectCity);

		// get vendor list
		vendorList = selectDeviceTagDAO.getVendor();

		// get softfilename list
		softfileList = upgradeBatchDAO.getSofefileList();

		// get execute strategy type list
		executeTypeList = upgradeBatchDAO.getStrategyTypeList(false);

		// go page
		return "initpage";
	}

	/**
	 * 策略执行软件批量升级
	 * 
	 * @author Jason(3412)
	 * @date 2009-4-8
	 * @return String
	 */
	public String strategyExecute() {

		List<String> sqlList = new ArrayList<String>();

		// 获取当前时间 单位s
		long lnow = System.currentTimeMillis() / 1000;

		// 获取配置用户
		User curUser = ((UserRes) request.getSession().getAttribute("curUser"))
				.getUser();

		/** *入任务表* */
		// 任务ID
		long task_id = lnow * 1000;
		// 任务名称
		String task_name = curUser.getAccount() + "_" + lnow + "_软件升级";
		// 模板ID(软件升级批量配置) 外键约束表gw_conf_template
		int temp_id = 2;
		// 定制人
		long order_acc_oid = curUser.getId();
		// 定制时间
		long order_time = lnow;
		// 审核状态
		int is_check = 1;
		// 审核人
		long check_acc_oid = curUser.getId();
		// 审核时间
		long check_time = lnow;
		// 入任务表
		sqlList
				.add(itvConfigDAO.inTaskTable(task_id, task_name, temp_id,
						order_acc_oid, order_time, is_check, check_acc_oid,
						check_time));
		// 入域权限表
		sqlList.addAll(ItvConfigDAO.insertAreaTable(13,
				String.valueOf(task_id), String.valueOf(curUser.getAreaId()),
				true));

		/** *入策略表* */
		// 策略id
		Long id = 0L;
		// 管理员id
		long acc_oid = curUser.getId();
		// 定制时间
		long time = lnow;
		// 策略方式
		int iType = Integer.parseInt(selectExecuteType);
		// 获取软件版本文件信息
		String softSheetParam = upgradeBatchDAO
				.getSoftUpSheetParam(selectSoftfile);
		String gatherId = null;
		String deviceId = null;
		String oui = null;
		String serialnumber = null;
		String username = "";
		// service_id:软件升级： 5 | IPTV开户：1101
		String service_id = "5";
		// 数据同下表一致：gw_conf_template_service
		int order_id = 1;
		// 设备逐台加入到策略表中
		int devNum = device_id.size();
		for (int i = 0; i < devNum; i++) {
			deviceId = String.valueOf(device_id.get(i));
			Map map = DeviceActUtil.getDeviceInfo(deviceId);
			if (map == null || map.isEmpty()) {
				LOG.warn("根据deivce_id没有获取到设备信息...");
			} else {
				id = StrategyOBJ.createStrategyId();
				gatherId = String.valueOf(map.get("gather_id"));
				oui = String.valueOf(map.get("oui"));
				serialnumber = String.valueOf(map.get("device_serialnumber"));
				sqlList.add(itvConfigDAO.inStrategyTable(id,
						acc_oid, time, iType, gatherId, deviceId, oui,
						serialnumber, username, softSheetParam, service_id,
						task_id, order_id, 1, 5, 1, service_id));
			}
		}
		// 加入到批量sql执行线程中
		LipossGlobals.ALL_SQL_IPTV.addAll(sqlList);
		return "batchConfigResult";
	}

	/**
	 * 根据厂商获取设备型号list
	 * 
	 * @author Jason(3412)
	 * @date 2009-4-8
	 */
	public String getDevModelSelectList() {
		// 根据厂商获取型号
		devModelList = selectDeviceTagDAO.getDeviceModel(selectVendor);
		ajax = upgradeBatchDAO.getSelectOptiones(devModelList,
				"device_model_id", "device_model");
		return "ajax";
	}

	/**
	 * 根据型号获取版本list
	 * 
	 * @author Jason(3412)
	 * @date 2009-4-8
	 */
	public String getDevTypeSelectList() {
		// 根据型号获取版本
		devTypeList = selectDeviceTagDAO.getVersionList(selectDevModel);
		ajax = upgradeBatchDAO.getSelectOptiones(devTypeList, "devicetype_id",
				"softwareversion");
		return "ajax";
	}

	public String getDeviceCheckboxList() {
		// 根据版本获取设备list
		// deviceList = selectDeviceTagDAO.getDeviceBySenior(selectCity, null,
		// null, null, selectDevType, null, null);
		LOG.debug("selectCity:" + selectCity);
		if (selectCity == null || "".equals(selectCity)
				|| "-1".equals(selectCity)) {
			UserRes curUser = (UserRes) request.getSession().getAttribute(
					"curUser");
			selectCity = curUser.getCityId();
		}
		deviceList = upgradeBatchDAO.getDevListByType(selectCity,
				selectDevType, true);
		ajax = upgradeBatchDAO.getCheckboxDevice(deviceList, "device_id",
				"checkbox");
		return "ajax";
	}

	/** *属性的getter setter方法* */

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getSelectCity() {
		return selectCity;
	}

	public void setSelectCity(String selectCity) {
		this.selectCity = selectCity;
	}

	public List getCityList() {
		return cityList;
	}

	public void setCityList(List cityList) {
		this.cityList = cityList;
	}

	public String getSelectVendor() {
		return selectVendor;
	}

	public void setSelectVendor(String selectVendor) {
		this.selectVendor = selectVendor;
	}

	public List getVendorList() {
		return vendorList;
	}

	public void setVendorList(List vendorList) {
		this.vendorList = vendorList;
	}

	public String getSelectDevModel() {
		return selectDevModel;
	}

	public void setSelectDevModel(String selectDevModel) {
		this.selectDevModel = selectDevModel;
	}

	public List getDevModelList() {
		return devModelList;
	}

	public void setDevModelList(List devModelList) {
		this.devModelList = devModelList;
	}

	public String getSelectDevType() {
		return selectDevType;
	}

	public void setSelectDevType(String selectDevType) {
		this.selectDevType = selectDevType;
	}

	public List getDevTypeList() {
		return devTypeList;
	}

	public void setDevTypeList(List devTypeList) {
		this.devTypeList = devTypeList;
	}

	public List getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List deviceList) {
		this.deviceList = deviceList;
	}

	public void setSelectDeviceTagDAO(SelectDeviceTagDAO selectDeviceTagDAO) {
		this.selectDeviceTagDAO = selectDeviceTagDAO;
	}

	public String getSelectSoftfile() {
		return selectSoftfile;
	}

	public void setSelectSoftfile(String selectSoftfile) {
		this.selectSoftfile = selectSoftfile;
	}

	public List getSoftfileList() {
		return softfileList;
	}

	public void setSoftfileList(List softfileList) {
		this.softfileList = softfileList;
	}

	public String getSelectExecuteType() {
		return selectExecuteType;
	}

	public void setSelectExecuteType(String selectExecuteType) {
		this.selectExecuteType = selectExecuteType;
	}

	public List getExecuteTypeList() {
		return executeTypeList;
	}

	public void setExecuteTypeList(List executeTypeList) {
		this.executeTypeList = executeTypeList;
	}

	public void setUpgradeBatchDAO(UpgradeBatchDAO upgradeBatchDAO) {
		this.upgradeBatchDAO = upgradeBatchDAO;
	}

	public List getDevice_id() {
		return device_id;
	}

	public void setDevice_id(List device_id) {
		this.device_id = device_id;
	}

	public void setItvConfigDAO(ItvConfigDAO itvConfigDAO) {
		this.itvConfigDAO = itvConfigDAO;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

}
