package action.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import bio.report.StateDevUserBio;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Jason(3412)
 * @date 2008-11-12
 */
public class StateDevUserAction extends ActionSupport implements SessionAware, 
					ServletRequestAware, ServletResponseAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String startTime;
	private String endTime;
	private String city_id;
	private String gw_type;
	//'ADSL'或'LAN'
	private String accessType;
	
	private List cityList;
	private String strResult;
	private String ajax;
	
	private StateDevUserBio stateBio;
	private Map session;
	@SuppressWarnings("unused")
	private HttpServletRequest servletRequest;
	@SuppressWarnings("unused")
	private HttpServletResponse servletResponse;
	
	//列名称
	private String[] title = null;
	//MAP的key
	private String[] column =  null;
	private ArrayList<Map> data;
	private String fileName =  null;
	
	public String execute(){
		
		if(city_id == null || "".equals(city_id)){
			UserRes curUser = (UserRes) session.get("curUser");
			String curCity = curUser.getCityId();
			//获取属地下拉框(属地及下级属地)
			//cityList = stateBio.getCityList(curCity);
			city_id = curCity;
		}
		
		Calendar cal = Calendar.getInstance();
		String DATE_2_FORMAT = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_2_FORMAT);
		
		this.endTime = sdf.format(cal.getTime());
		cal.set(Calendar.DATE, 1);
		this.startTime = sdf.format(cal.getTime());
		
		if(null == gw_type || "".equals(gw_type)){
			gw_type = String.valueOf(LipossGlobals.SystemType());
		}
		
		return "success";
	}
	
	public String getReportByCity(){
		if(city_id == null || "".equals(city_id)){
			UserRes curUser = (UserRes) session.get("curUser");
			String curCity = curUser.getCityId();
			//获取属地下拉框(属地及下级属地)
			//cityList = stateBio.getCityList(curCity);
			city_id = curCity;
		}
		//获取属地及下级属地的统计数据
		strResult = stateBio.getStateByCity(gw_type, startTime, endTime, city_id);
		ajax = strResult;
		return "ajax";
	}
	
	public String toExcel(){
		ArrayList<Map> listTmp = new ArrayList<Map>();
		strResult = stateBio.getStateByCity(gw_type, startTime, endTime, city_id);
		String[] atmp = strResult.split("\\|\\|\\|");
		int len = atmp.length;
		for(int i = 0; i < len; i++){
			String[] aString = atmp[i].split("\\|");
			Map<String, String> mapTmp = new HashMap<String, String>();
			mapTmp.put("city_name", aString[1]);
			mapTmp.put("devNum", aString[2]);
			mapTmp.put("userNum", aString[3]);
			mapTmp.put("adslUserNum", aString[4]);
			mapTmp.put("lanUserNum", aString[5]);
			mapTmp.put("eponUserNum", aString[6]);
			mapTmp.put("percent", aString[7]);
			listTmp.add(mapTmp);
		}
		data = listTmp;
		this.title = new String[7];
		this.title[0] = "属地";
		this.title[1] = "上报上来与用户有绑定关系的设备数量";
		this.title[2] = "CRM工单过来的在用用户数";
		this.title[3] = "ADSL用户数";
		this.title[4] = "LAN用户数";
		this.title[5] = "光纤用户数";
		this.title[6] = "设备占用户的比例";
		
		//MAP的key
		this.column = new String[7];
		this.column[0] = "city_name";
		this.column[1] = "devNum";
		this.column[2] = "userNum";
		this.column[3] = "adslUserNum";
		this.column[4] = "lanUserNum";
		this.column[5] = "eponUserNum";
		this.column[6] = "percent";
		this.fileName = "统计报表";
		return "excel";
	}
	
	@SuppressWarnings("unchecked")
	public String toUserExcel(){
		
		List list = stateBio.getStateUser(gw_type,city_id,startTime, endTime, accessType);
		Calendar time=Calendar.getInstance(); 
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		this.data = new ArrayList();
		
		for(int i=0;i<list.size();i++){
			
			Map map = (Map)list.get(i);
			Map<String, String> rsMap = new HashMap<String, String>();
			
			String device_serialnumber = String.valueOf(map.get("device_serialnumber"));
			String device_id = String.valueOf(map.get("device_id"));
			//如果是新疆用户则根据device_id来判断
			if(LipossGlobals.isXJDX()){
				if(null == device_id || "".equals(device_id.trim()) || "null".equals(device_id)){
					rsMap.put("bindType","否");
				}
				else{
					rsMap.put("bindType","是");
				}
			}else{
				if(null == device_serialnumber || "".equals(device_serialnumber.trim()) || "null".equals(device_serialnumber)){
					rsMap.put("bindType","否");
				}
				else{
					rsMap.put("bindType","是");
				}
			}
			String dealdate = String.valueOf(map.get("dealdate"));
			if( null != dealdate && !("null".equals(dealdate)) && !"".equals(dealdate) && !dealdate.equals("0")){
				time.setTimeInMillis((Long.parseLong(dealdate))*1000);
				rsMap.put("formatTime",df.format(time.getTime()));
			}else{
				rsMap.put("formatTime","-");
			}
			String updatetime = String.valueOf(map.get("updatetime"));
			if(null != updatetime && !("null".equals(updatetime)) && !("".equals(updatetime)) && !(updatetime.equals("0"))){
				time.setTimeInMillis((Long.parseLong(updatetime))*1000);
				rsMap.put("updateTime",df.format(time.getTime()));
			}else{
				rsMap.put("updateTime","-");
			}
			String username = String.valueOf(map.get("username"));
			String isFTTP = "否";      //新疆需求 增加判断是否FTTH用户
			if(null == username || "".equals(username)){
				rsMap.put("username","-");
			}else{
				rsMap.put("username",username);
				if(username.trim().toUpperCase().endsWith("C")){
					isFTTP = "是";
				}
				rsMap.put("isFTTP",isFTTP);
			}
			String user_type_id = String.valueOf(map.get("user_type_id"));
			String tmp = "BSS用户";
			if(null != user_type_id && !user_type_id.equals("")){
				if(user_type_id.equals("1")){
					tmp = "现场安装";
				} else if(user_type_id.equals("2")){
					tmp = "BSS用户";
				} else if(user_type_id.equals("4")){
					tmp = "BSS同步";
				}
			}
			String phonenumber = String.valueOf(map.get("phonenumber"));
			if(null == phonenumber || "null".equals(phonenumber) || "".equals(phonenumber)){
				rsMap.put("phonenumber","-");
			}else{
				rsMap.put("phonenumber",phonenumber);
			}
			
			rsMap.put("tmp",tmp);
			this.data.add(rsMap);
		}
		//新疆需求 增加判断是否FTTH用户
		this.title = new String[7];
		this.title[0] = "用户帐号";
		this.title[1] = "绑定电话";
		this.title[2] = "用户来源";
		this.title[3] = "是否绑定设备";
		this.title[4] = "是否FTTP用户";
		this.title[5] = "受理时间";
		this.title[6] = "更新时间";
		
		//MAP的key
		this.column = new String[7];
		this.column[0] = "username";
		this.column[1] = "phonenumber";
		this.column[2] = "tmp";
		this.column[3] = "bindType";
		this.column[4] = "isFTTP";
		this.column[5] = "formatTime";
		this.column[6] = "updateTime";
		this.fileName = "统计报表";
		
		return "excel";
	}
	
	public void setSession(Map arg0){
		this.session = arg0;
	}
	
	public String getCity_id() {
		return city_id;
	}


	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}


	public List getCityList() {
		return cityList;
	}


	public String getStrResult() {
		return strResult;
	}

	public String getStartTime() {
		return this.startTime;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public void setStateBio(StateDevUserBio stateBio) {
		this.stateBio = stateBio;
	}


	public String getGw_type() {
		return gw_type;
	}


	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.servletRequest = request;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	public ArrayList<Map> getData() {
		return data;
	}

	public void setData(ArrayList<Map> data) {
		this.data = data;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

}
