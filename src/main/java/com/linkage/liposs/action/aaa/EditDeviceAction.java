package com.linkage.liposs.action.aaa;
import static com.linkage.liposs.action.cst.AJAX;
import static com.linkage.liposs.action.cst.EDIT;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.liposs.dao.aaa.DevAuthenticationDAO;
import com.linkage.liposs.dao.aaa.LogQueryDAO;
import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionSupport;

public class EditDeviceAction extends ActionSupport implements SessionAware{
	private static Logger log = LoggerFactory.getLogger(EditDeviceAction.class);
	private Map session;					//服务器的会话对象
	private UserRes res;					//用户资源
	private List<Map<String,String>> getGatherList;				//采集点
	
	private LogQueryDAO lqd;
	private DevAuthenticationDAO devAuthDao;
	
	private String device_id = null;
	private String gather_id = null;
	private String authen_prtc = null;
	private String tac_key = null;
	
	private String user_name = null;
	
	private String ajax = "";
	/**
	 * 和设备已经关联的帐号
	 */
	private List<Map<String,String>> existUserList = null;
	
	private List<Map<String,String>> noExistUserList = null;
	
	private Map<String,String> deviceInfo = null;
	
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getGetGatherList() {
		return getGatherList;
	}
	public String execute() throws Exception{
		return edit();
	}
	/**
	 * 编辑页面
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception{
		
		res = (UserRes) session.get("curUser");
		String cid = res.getCityId();
		List<Map<String,String>> getherList = res.getUserProcesses();
		//获取采集点
		getGatherList = lqd.getGatherList(getherList);
		//查询出已经和设备关联的帐号
		existUserList = devAuthDao.getExistUser(device_id);
		//查询出没有和设备关联的帐号
		noExistUserList = devAuthDao.getNotExistUser(device_id);
		//编辑界面需要获取设备基本信息
		this.deviceInfo = devAuthDao.getDeviceInfo(device_id);
		
		return EDIT;
	}
	public String delete()throws Exception{
		int codes = devAuthDao.deleteDeviceAndUser(device_id,user_name);
		if(codes <= 0)
			ajax = "删除失败!";
		return AJAX;
	}
	/**
	 * 编辑设备界面 修改页面保存
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		try{
			if("".equals(tac_key)){
				tac_key = null;
			}else{
				tac_key = tac_key.replace("&amp;", "&");
			}
			
			if(devAuthDao.updateDevice(device_id, Integer.parseInt(authen_prtc), tac_key, gather_id) < 1)
				ajax = "fail";
		}catch(Exception e){
			log.debug(e.getMessage());
			ajax = "fail";
		}
		return AJAX;
	}
	/**
	 *编辑认证设备界面添加帐号
	 */
	public String add() throws Exception{
		int[] codes = devAuthDao.insertDeviceUser(new String[]{device_id}, new String[]{user_name});
		try{
			if(codes == null || codes[0] <= 0)
				ajax = "添加失败";
		}catch(Exception e){
			log.debug(e.getMessage());
			ajax = e.getMessage();
		}
		return AJAX;
	}

	@SuppressWarnings("unchecked")
	public void setSession(Map session) {
		this.session = session;
	}

	public LogQueryDAO getLqd() {
		return lqd;
	}

	public void setLqd(LogQueryDAO lqd) {
		this.lqd = lqd;
	}

	public String getDevice_id() {
		return device_id;
	}

	public List<Map<String,String>> getExistUserList() {
		return existUserList;
	}

	public List<Map<String,String>> getNoExistUserList() {
		return noExistUserList;
	}

	public void setDevAuthDao(DevAuthenticationDAO devAuthDao) {
		this.devAuthDao = devAuthDao;
	}
	public String getGather_id() {
		return gather_id;
	}
	public void setGather_id(String gather_id) {
		this.gather_id = gather_id;
	}
	public String getAuthen_prtc() {
		return authen_prtc;
	}
	public void setAuthen_prtc(String authen_prtc) {
		this.authen_prtc = authen_prtc;
	}
	public String getTac_key() {
		return tac_key;
	}
	public void setTac_key(String tac_key) {
		this.tac_key = tac_key;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public Map<String, String> getDeviceInfo() {
		return deviceInfo;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	public String getAjax() {
		return ajax;
	}
}
