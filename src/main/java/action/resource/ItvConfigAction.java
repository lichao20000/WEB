package action.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.resource.ItvConfigBIO;

import com.linkage.litms.system.UserRes;

import dao.resource.DeviceVendorModelDao;

/**
 * @author Jason(3412)
 * @date 2008-12-17
 */
public class ItvConfigAction implements ServletRequestAware{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(ItvConfigAction.class);
	
	private ItvConfigBIO itvConfigBIO;
	
	/** 终端类型  1：ITMS    2：BBMS */
	private String gw_type = null;
	
	
	private List<String> device_id;
	
	private String vpi;
	private String vci;
	private String wanType;
	//策略方式
	private String lanstrategy_type;
	private String wlanstrategy_type;
	
	private String softStrategy_type;
	
	private String itvLstrategyHTML;
	private String itvWstrategyHTML;
	private String softStrategyHTML;
	
	private String needUpSoftware;
	private String goalVersion;
	private String goalVersionHTML;
	private String confWlan;
	private String auWay;
	private String wepPw;
	private String wpaPw;
	
	//查询方式
	private String checkType;
	private String vendorname;
	private String deviceModel;
	private String softwareversion;
	private String city_id;
	private String username;
	private String telephone;
	private String deviceSerialnumber;
	private String ipAddress;
	private DeviceVendorModelDao devDao;
	
	private String isAddLan;
	private String isAddWlan;
	
	private String allSelected;
	
	// request取登陆帐号使用
	private HttpServletRequest request;
	
	public String execute(){
		softStrategyHTML = itvConfigBIO.getStrategyList("softStrategy_type", new String[]{"4", "5","0"});
		
		itvLstrategyHTML = itvConfigBIO.getStrategyList("lanstrategy_type", new String[]{"4","5"});
		//itvWstrategyHTML = itvConfigBIO.getStrategyList("wlanstrategy_type", new String[]{"4","5"});
		
		//goalVersionHTML = itvConfigBIO.getVersionList("goalVersion");
		return "success";
	}
	
	/**
	 * 策略执行
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2008-12-22
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String doStatery(){
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		//String sheetParam = "PVC:"+vpi+"/"+vci + "|||" + LAN2;
		
		String[] arrayPara = {vpi,vci,"LAN2",wanType};
		
		//如果是全选，则在后台用相同的sql重新查询设备
		//如果未全选，则用页面提交的设备信息
		if("1".equals(allSelected)){
			String queryCity = city_id;
			if(queryCity == null || "".equals(queryCity)){
				queryCity = curUser.getCityId();
			}
			if("1".equals(checkType)){ //按用户查询
				device_id = devDao.getSplitDeviceList(devDao.getDeviceList(username, telephone, checkType, queryCity, null, null));
			}else if("2".equals(checkType)){ //按设备查询
				device_id = devDao.getSplitDeviceList(devDao.getDeviceList(deviceSerialnumber, ipAddress, checkType, queryCity, null, null));
			}else if("3".equals(checkType)){ //按属地等查询
				if("".equals(softwareversion) || "-1".equals(softwareversion)){
					device_id = devDao.getSplitDeviceList(devDao.getDeviceList(deviceModel, null, checkType, queryCity,vendorname, null));
				}else{
					device_id = devDao.getSplitDeviceList(devDao.getDeviceList(deviceModel, softwareversion, checkType, queryCity,vendorname, null));
				}
			}else{
				logger.debug("根据用户批量导入的数据不需要重查数据库！");
			}
		}
		
		if("0".equals(needUpSoftware) || "-1".equals(needUpSoftware)){
			//暂时未走此步
			//itvConfigBIO.doStratery(device_id, curUser.getUser());
			
		} else if ("1".equals(confWlan)) {
			
			//如果需要配置无线
			
			isAddLan = "1";
			isAddWlan = "1";
			
			arrayPara[2] = "LAN2,WLAN2";
			
			itvConfigBIO.doStratery(isAddLan, isAddWlan, device_id, goalVersion, curUser.getUser(),
					lanstrategy_type, softStrategy_type, arrayPara, wlanstrategy_type, auWay, wepPw, wpaPw, 1, gw_type);
			
		} else {
			//只软件升级和配置有线
			isAddLan = "1";
			isAddWlan = "0";
			
			itvConfigBIO.doStratery(isAddLan, isAddWlan, device_id, goalVersion, curUser.getUser(),
					lanstrategy_type, softStrategy_type, arrayPara, null, null, null, null, 1, gw_type);
		}
		
		return "configResult";
	}
	
	@SuppressWarnings("unchecked")
	public List getDevice_id() {
		return device_id;
	}

	@SuppressWarnings("unchecked")
	public void setDevice_id(List device_id) {
		this.device_id = device_id;
	}

	public String getVpi() {
		return vpi;
	}

	public void setVpi(String vpi) {
		this.vpi = vpi;
	}

	public String getVci() {
		return vci;
	}

	public void setVci(String vci) {
		this.vci = vci;
	}

	
	public String getWanType() {
		return wanType;
	}

	public void setWanType(String wanType) {
		this.wanType = wanType;
	}


	public String getLanstrategy_type() {
		return lanstrategy_type;
	}

	public void setLanstrategy_type(String lanstrategy_type) {
		this.lanstrategy_type = lanstrategy_type;
	}

	public String getWlanstrategy_type() {
		return wlanstrategy_type;
	}

	public void setWlanstrategy_type(String wlanstrategy_type) {
		this.wlanstrategy_type = wlanstrategy_type;
	}

	public void setSoftStrategy_type(String softStrategy_type) {
		this.softStrategy_type = softStrategy_type;
	}

	public String getSoftStrategyHTML() {
		return softStrategyHTML;
	}

	public void setSoftStrategyHTML(String softStrategyHTML) {
		this.softStrategyHTML = softStrategyHTML;
	}

	public String getSoftStrategy_type() {
		return softStrategy_type;
	}

	public String getNeedUpSoftware() {
		return needUpSoftware;
	}

	public void setNeedUpSoftware(String needUpSoftware) {
		this.needUpSoftware = needUpSoftware;
	}

	public String getGoalVersion() {
		return goalVersion;
	}

	public void setGoalVersion(String goalVersion) {
		this.goalVersion = goalVersion;
	}


	public String getItvLstrategyHTML() {
		return itvLstrategyHTML;
	}

	public void setItvLstrategyHTML(String itvLstrategyHTML) {
		this.itvLstrategyHTML = itvLstrategyHTML;
	}

	public String getItvWstrategyHTML() {
		return itvWstrategyHTML;
	}

	public void setItvWstrategyHTML(String itvWstrategyHTML) {
		this.itvWstrategyHTML = itvWstrategyHTML;
	}

	public String getGoalVersionHTML() {
		return goalVersionHTML;
	}

	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}
	
	public void setItvConfigBIO(ItvConfigBIO itvConfigBIO) {
		this.itvConfigBIO = itvConfigBIO;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getVendorname() {
		return vendorname;
	}

	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getSoftwareversion() {
		return softwareversion;
	}

	public void setSoftwareversion(String softwareversion) {
		this.softwareversion = softwareversion;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getDeviceSerialnumber() {
		return deviceSerialnumber;
	}

	public void setDeviceSerialnumber(String deviceSerialnumber) {
		this.deviceSerialnumber = deviceSerialnumber;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getAllSelected() {
		return allSelected;
	}

	public void setAllSelected(String allSelected) {
		this.allSelected = allSelected;
	}

	public void setDevDao(DeviceVendorModelDao devDao) {
		this.devDao = devDao;
	}

	public String getConfWlan() {
		return confWlan;
	}

	public void setConfWlan(String confWlan) {
		this.confWlan = confWlan;
	}

	public String getAuWay() {
		return auWay;
	}

	public void setAuWay(String auWay) {
		this.auWay = auWay;
	}

	public String getWepPw() {
		return wepPw;
	}

	public void setWepPw(String wepPw) {
		this.wepPw = wepPw;
	}

	public String getWpaPw() {
		return wpaPw;
	}

	public void setWpaPw(String wpaPw) {
		this.wpaPw = wpaPw;
	}

	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

}
