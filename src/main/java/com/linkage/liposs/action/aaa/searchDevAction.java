package com.linkage.liposs.action.aaa;

import static com.linkage.liposs.action.cst.AJAX;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.linkage.liposs.dao.aaa.DevAuthenticationDAO;
import com.linkage.liposs.dao.aaa.LogQueryDAO;
import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 认证设备查询
 * @author benyp(5260)
 * @version 1.0
 */
public class searchDevAction extends ActionSupport implements SessionAware
{
	private Map session;					//服务器的会话对象
	private UserRes res;					//用户资源
	private LogQueryDAO lqd;
	private DevAuthenticationDAO da;
	private String ajax="";					//返回ajax
	//*****************************************************
	private String lookbackip;				//ip
	private String device_name;				//设备名称
	private String device_id;				//设备id
	private String devid="";				//dev_id为了删除全部设备时取得设备id：将所有的id用/分割
	//*****************************************************
	private List<Map> getDevList;			//取得设备列表
	/**
	 * 初始化
	 */
	public String execute() throws Exception
	{
		res = (UserRes) session.get("curUser");
		String cid=res.getCityId();
		//取得设备列表
		getDevList=lqd.getDevList(cid, null, null);
		int k=getDevList.size();
		devid="";
		for(int i=0;i<k;i++){
			Map map=getDevList.get(i);
			devid+=map.get("device_id")+"/";
		}
		return SUCCESS;
	}
	/**
	 * 查找设备列表
	 * @return
	 */
	public String search(){
		res = (UserRes) session.get("curUser");
		String cid=res.getCityId();
		getDevList=lqd.getDevList(cid, lookbackip, device_name);
		int k=getDevList.size();
		devid="";
		for(int i=0;i<k;i++){
			Map map=getDevList.get(i);
			devid+=map.get("device_id")+"/";
		}
		return SUCCESS;
	}
	/**
	 * 批量删除设备（可以删除单个）
	 * @return
	 */
	public String del(){
		String tmp[]=device_id.split("/");
		int n=tmp.length;
		for(int i=0;i<n;i++){
			int k=lqd.deleteDev(tmp[i]);
			if(k==1){
				ajax+="第"+i+"条数据"+tmp[i]+"删除帐号失败，请重试！";
			}else if(k==2){
				ajax+="第"+i+"条数据"+tmp[i]+"删除设备失败，请重试！";
			}
		}
		if(ajax.trim().equals("")){
			ajax="删除成功！";
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
	public void setDa(DevAuthenticationDAO da)
	{
		this.da = da;
	}
	public String getLookbackip()
	{
		return lookbackip;
	}
	public void setLookbackip(String lookbackip)
	{
		this.lookbackip = lookbackip;
	}
	public String getDevice_name()
	{
		return device_name;
	}
	public void setDevice_name(String device_name)
	{
		this.device_name = device_name;
	}
	public List<Map> getGetDevList()
	{
		return getDevList;
	}
	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}
	public String getAjax()
	{
		return ajax;
	}
	public String getDevid()
	{
		return devid;
	}
	public void setDevid(String devid)
	{
		this.devid = devid;
	}

}
