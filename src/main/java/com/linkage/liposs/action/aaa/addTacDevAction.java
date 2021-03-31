package com.linkage.liposs.action.aaa;

import static com.linkage.liposs.action.cst.AJAX;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.linkage.liposs.dao.aaa.DevAuthenticationDAO;
import com.linkage.liposs.dao.aaa.LogQueryDAO;
import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 添加认证设备
 * @author benyp(5260) email:benyp@lianchuang.com
 * @version1.0
 * @since 2007-12-3
 */
public class addTacDevAction extends ActionSupport implements SessionAware
{
	private Map session;					//服务器的会话对象
	private UserRes res;					//用户资源
	private LogQueryDAO lqd;
	private DevAuthenticationDAO da;
	private String ajax="";					//返回ajax
	//*******************************
	private List getCityList;				//属地
	private List<Map<String,String>> getGatherList;				//采集点
	private List getVendorList;				//设备厂商
	private List getModelList;				//设备型号
	private List getDeviceList;				//设备
	private List getUserList;				//帐号信息
	private int num;						//用户总数
	//********************************
	private String gather_id;				//区域
	private int authen_prtc;				//认证协议：1Trcacs+2:radius
	private String city_id;					//属地
	private String tac_key;					//共享密钥
	private String vendor;					//设备厂商
	private String model;					//设备型号
	private String chk;						//设备
	private String che;						//用户
	//********************************
	/**
	 * 初始化信息
	 * benyp(5260) 2008-04-28
	 * BUG:XJDX-XJ-BUG-20080402-XXF-001
	 * 
	 */
	public String execute() throws Exception{
		res = (UserRes) session.get("curUser");
		//获取用户属地
		String cid=res.getCityId();
		//获取用户管理的采集机列表
		List getherList=res.getUserProcesses();
		//属地
		getCityList=lqd.getCityList(true, cid);
		//取得第一个属地信息
		Map mp=(Map)getCityList.get(0);
		String ci=(String)mp.get("city_id");
		//采集点
		getGatherList=lqd.getGatherList(getherList);
		//厂商
		getVendorList=lqd.getVendorList();
		//取得第一个厂商
		Map map=(Map)getVendorList.get(0);
		String vid=(String)map.get("vendor_id");
		//设备型号
		getModelList=lqd.getModelList(vid);
		//取得第一个设备型号
		Map dmap=(Map)getModelList.get(0);
		String md=(String)dmap.get("device_model");
		//设备
		getDeviceList=da.getDeviceList(res.getUser().isAdmin(),getGatherList==null||getGatherList.size()==0?"":getGatherList.get(0).get("gather_id"),ci, vid, md);
		//获取用户信息
		getUserList=lqd.gettacUserList();
		//用户总数
		num=getUserList.size();
		return SUCCESS;
	}
	/**
	 * 增加设备资源
	 */
	public String sub(){
		List<DevAuthenticationDAO.DeviceInfo> listDev = new ArrayList<DevAuthenticationDAO.DeviceInfo>();
		String tmp[]=chk.split("/");
		String did[]=new String[tmp.length];
		int k=tmp.length;
		for(int i=0;i<k;i++){
			DevAuthenticationDAO.DeviceInfo dev = new DevAuthenticationDAO.DeviceInfo();
			dev.setAuthen_prtc(authen_prtc);
			dev.setDevice_id(tmp[i]);
			dev.setGather_id(gather_id);
			dev.setTac_key(tac_key);
			did[i]=tmp[i];
			listDev.add(dev);
		}
		int d[]=da.insertBatchDevice(listDev); 
		listDev=null;
		String usr[]=che.split("/");
		try
		{
			int u[]=da.insertDeviceUser(did,usr);
			for(int i=0;i<u.length;i++){
				if(u[i] <= 0){
					ajax+="第"+(i+1)+"个用户"+(String)tmp[i]+"添加不成功-";
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		for(int i=0;i<d.length;i++){
			if(d[i]<=0){
				ajax+="第"+(i+1)+"个设备"+(String)tmp[i]+"添加不成功-";
			}
		}
		if(ajax.equals("")){
			ajax+="添加成功！";
		}
		return AJAX;
	}
	/**
	 * 由厂商过滤出设备型号
	 */
	public String getmodel(){
		List<Map> list=lqd.getModelList(vendor);
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			ajax+="<option value='"+(String)map.get("device_model")+"'>=="+(String)map.get("device_model")+"==</option>";
		}
		return AJAX;
	}
	/**
	 * 由设备型号过滤出设备
	 * benyp(5260) 2008-04-28
	 * BUG:XJDX-XJ-BUG-20080402-XXF-001
	 */
	public String getdev(){
		res = (UserRes) session.get("curUser");
		List<Map<String, String>> list=da.getDeviceList(res.getUser().isAdmin(),gather_id,city_id, vendor, model);
		int n=list.size();
		if(n > 0){
			for(int i=0;i<n;i++){
				Map map=list.get(i);
				ajax+="<input type='checkbox' name='chk' value='"
					+(String)map.get("device_id")+"'>"
					+(String)map.get("device_name")+"/"
					+(String)map.get("loopback_ip")+"<br>";
			}	
		}else{
			ajax="查询无设备，或该改型号设备已经全部添加";
		}
		return AJAX;
	}
	public void setSession(Map session)
	{
		this.session = session;
	}
	public void setRes(UserRes res)
	{
		this.res = res;
	}
	public void setLqd(LogQueryDAO lqd)
	{
		this.lqd = lqd;
	}
	public List getGetCityList()
	{
		return getCityList;
	}
	public List getGetGatherList()
	{
		return getGatherList;
	}
	public List getGetVendorList()
	{
		return getVendorList;
	}
	public List getGetModelList()
	{
		return getModelList;
	}
	public List getGetDeviceList()
	{
		return getDeviceList;
	}
	public List getGetUserList()
	{
		return getUserList;
	}
	public void setGather_id(String gather_id)
	{
		this.gather_id = gather_id;
	}
	public void setAuthen_prtc(String authen_prtc)
	{
		this.authen_prtc = Integer.parseInt(authen_prtc);
	}
	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}
	public void setTac_key(String tac_key)
	{
		try
		{
			this.tac_key=java.net.URLDecoder.decode(tac_key, "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			this.tac_key = tac_key;
		}
	}
	public void setVendor(String vendor)
	{
		this.vendor = vendor;
	}
	public void setModel(String model)
	{
		this.model = model;
	}
	public String getAjax()
	{
		return ajax;
	}
	public void setDa(DevAuthenticationDAO da)
	{
		this.da = da;
	}
	public int getNum()
	{
		return num;
	}
	public void setChk(String chk)
	{
		this.chk = chk;
	}
	public void setChe(String che)
	{
		this.che = che;
	}
}
