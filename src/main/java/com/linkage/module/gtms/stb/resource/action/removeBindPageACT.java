package com.linkage.module.gtms.stb.resource.action;

import action.splitpage.splitPageAction;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.removeBindPageBIO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-2-2
 * @category com.linkage.module.gtms.stb.resource.action
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
@SuppressWarnings("rawtypes")
public class removeBindPageACT extends splitPageAction implements SessionAware,ServletRequestAware
{
	private static final long serialVersionUID = 1L;

	private HttpServletRequest request;
	private Map session;
	// 开始时间
	private String starttime;
	// 结束时间
	private String endtime;
	// 业务账号
	private String servaccount;
	// MAC地址
	private String MAC;
	// 鉴权账号
	private String authUser;
	// 鉴权密码
	private String authPwd;
	// 属地列表
	private List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();
	private String ajax;
	private removeBindPageBIO bio;
	// 结果集
	private List<Map> date = new ArrayList<Map>();
	
		
	/**
	 * 初始化页面
	 * @return
	 */
	public String init()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		// cityId = curUser.getCityId();
		DateTimeUtil dt = new DateTimeUtil();
		endtime = dt.getDate();
		starttime = dt.getFirtDayOfMonth();
		dt = new DateTimeUtil(endtime);
		long end_time = dt.getLongTime();
		dt = new DateTimeUtil((end_time + 24 * 3600 - 1) * 1000);
		endtime = dt.getLongDate();
		dt = new DateTimeUtil(starttime);
		starttime = dt.getLongDate();
		cityList = CityDAO.getNextCityListByCityPid(curUser.getCityId());
		return "init";
	}
	
	public String removebindcheck()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		String city_id=curUser.getCityId();
		String loginAccountName = curUser.getUser().getAccount();
		ajax=bio.removebindcheck(authUser, authPwd, endtime, city_id, MAC, servaccount,loginAccountName);
		
		long  user_id=((UserRes) session.get("curUser")).getUser().getId();
		String user_ip=request.getRemoteHost();
		bio.addLogUnBind(user_id,user_ip,city_id, MAC, servaccount);
		
		return "ajax";
	}
	
	public String getMacList()
	{
		UserRes curUser = (UserRes) session.get("curUser");
		String city_id =curUser.getCityId();
		date=bio.getMacList(MAC, servaccount,city_id);
		return "list";
	}
	
	
	
	public Map getSession(){
		return session;
	}
	
	public void setSession(Map session){
		this.session = session;
	}
	
	public String getStarttime(){
		return starttime;
	}
	
	public void setStarttime(String starttime){
		this.starttime = starttime;
	}
	
	public String getEndtime(){
		return endtime;
	}
	
	public void setEndtime(String endtime){
		this.endtime = endtime;
	}
	
	public String getServaccount(){
		return servaccount;
	}
	
	public void setServaccount(String servaccount){
		this.servaccount = servaccount;
	}
	
	public String getMAC(){
		return MAC;
	}
	
	public void setMAC(String mAC){
		MAC = mAC;
	}
	
	public String getAuthUser(){
		return authUser;
	}
	
	public void setAuthUser(String authUser){
		this.authUser = authUser;
	}
	
	public String getAuthPwd(){
		return authPwd;
	}
	
	public void setAuthPwd(String authPwd){
		this.authPwd = authPwd;
	}
	
	public List<Map<String, String>> getCityList(){
		return cityList;
	}
	
	public void setCityList(List<Map<String, String>> cityList){
		this.cityList = cityList;
	}
	
	public String getAjax(){
		return ajax;
	}
	
	public void setAjax(String ajax){
		this.ajax = ajax;
	}
	
	public removeBindPageBIO getBio(){
		return bio;
	}
	
	public void setBio(removeBindPageBIO bio){
		this.bio = bio;
	}
	
	public List<Map> getDate(){
		return date;
	}
	
	public void setDate(List<Map> date){
		this.date = date;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
}
