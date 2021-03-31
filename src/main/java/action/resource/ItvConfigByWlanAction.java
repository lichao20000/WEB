package action.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import bio.resource.ItvConfigBIO;

import com.linkage.litms.system.UserRes;

/**
 * @author onelinesky
 * @date 2009-05-13
 */
public class ItvConfigByWlanAction implements ServletRequestAware{

	private ItvConfigBIO itvConfigBIO;
	
	private List<String> device_id;
	
	private String vpi;
	private String vci;
	private String wanType;
	//策略方式
	private String lanstrategy_type;
	private String wlanstrategy_type;
	
	private String softStrategy_type;
	
	private String goalVersion;
	private String goalVersionHTML;
	private String confWlan;
	private String auWay;
	private String wepPw;
	private String wpaPw;
	
	private static final String LAN2 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2";
	
	private static final String WLAN2 = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.2";
	
	private String gw_type;
	
	// request取登陆帐号使用
	private HttpServletRequest request;

	public String execute(){
		return "success";
	}
	
	/**
	 * 策略执行
	 * 
	 * @param 
	 * @author onelinesky(4174)
	 * @date 2008-12-22
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String doStatery(){
		
		lanstrategy_type = "4";
//		lanstrategy_type = wlanstrategy_type;
		softStrategy_type = wlanstrategy_type;
		
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		//String sheetParam = "PVC:"+vpi+"/"+vci + "|||" + LAN2;
		
		String[] arrayPara = {vpi,vci,"LAN2",wanType};
		
		//如果需要配置无线
		//sheetParam = sheetParam + "," + WLAN2;
		arrayPara[2] = "LAN2,WLAN2";
		
		itvConfigBIO.doStratery("0", "1", device_id, goalVersion, curUser.getUser(),
				lanstrategy_type, softStrategy_type, arrayPara, wlanstrategy_type, auWay, wepPw, wpaPw, 2, gw_type);
		
		return "configResult";
	}
	
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

	public String getSoftStrategy_type() {
		return softStrategy_type;
	}

	public String getGoalVersion() {
		return goalVersion;
	}

	public void setGoalVersion(String goalVersion) {
		this.goalVersion = goalVersion;
	}

	public String getGoalVersionHTML() {
		return goalVersionHTML;
	}

	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}

	/**
	 * @return the itvConfigBIO
	 */
	public ItvConfigBIO getItvConfigBIO() {
		return itvConfigBIO;
	}

	/**
	 * @param itvConfigBIO the itvConfigBIO to set
	 */
	public void setItvConfigBIO(ItvConfigBIO itvConfigBIO) {
		this.itvConfigBIO = itvConfigBIO;
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
