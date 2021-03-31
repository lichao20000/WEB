package com.linkage.liposs.action.aaa;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.linkage.liposs.dao.aaa.UserOperateDAO;
import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 新增、修改AAA认证帐号功能的action
 * @author 陈仲民  2007-12-03
 * @version 1.0
 */

public class UserOperateAction extends ActionSupport implements ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5746349462093300454L;
	
	//第一步中的基本信息
	private String data1 = "";
	
	//原始的基本信息
	private String data1_old = "";
	
	//操作权限数据
	private String data2 = "";
	
	//原始的权限信息
	private String data2_old = "";
	
	//关联的设备信息
	private String data3 = "";
	
	//原始的设备信息
	private String data3_old = "";
	
	//当前的阶段
	private String nowStep = "";
	
	//当前操作 next：下一步  pre：上一步  complete：完成
	private String operate = "next";
	
	//DAO类
	private UserOperateDAO userOperate;
	
	//认证帐号
	private String user_name = "";
	
	//是否新增数据 1：新增 0：修改
	private String isNew = "";
	
	//request取登陆帐号使用
	private HttpServletRequest request;
	
	//
	private String default_privilege = "0";
	
	private String ajax;
	
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public void setUserOperate(UserOperateDAO userOperate) {
		this.userOperate = userOperate;
	}

	public String getData1() {
		return data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}

	public String getData2() {
		return data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}

	public String getData3() {
		return data3;
	}

	public void setData3(String data3) {
		this.data3 = data3;
	}

	public void setNowStep(String nowStep) {
		this.nowStep = nowStep;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}
	
	public String getIsNew() {
		return isNew;
	}

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	public String execute() throws Exception {
		
		//读取用户登录名
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String accounts = curUser.getUser().getAccount();
		userOperate.setAccounts(accounts);
		
		//第一步操作
		if ("1".equals(nowStep)){
			
			//下一步
			if ("next".equals(operate)){
				
				//查询当前用户的权限
				if ("".equals(data2)){
					data2 = userOperate.getPrivilege(user_name);
					data2_old = data2;
				}
				
				return "step2";
			}
			//没有上一步
			else if ("pre".equals(operate)){
				return "error";
			}
			//完成入库
			else if ("complete".equals(operate)){
				//进行入库操作
				return userOperate.initData(data1, data2, data3, isNew, data1_old, data2_old, data3_old);
			}
		}
		//第二步操作
		else if ("2".equals(nowStep)){
			
			//下一步
			if ("next".equals(operate)){
				
				//查询当前用户的绑定设备
				if ("".equals(data3)){
					data3 = userOperate.getDeviceInfo(user_name);
					data3_old = data3;
				}
				
				return "step3";
			}
			//上一步
			else if ("pre".equals(operate)){
				return "step1";
			}
			//完成入库
			else if ("complete".equals(operate)){
				//进行入库操作
				return userOperate.initData(data1, data2, data3, isNew, data1_old, data2_old, data3_old);
			}
		}
		//第三步操作
		else if ("3".equals(nowStep)){
			
			//没有下一步
			if ("next".equals(operate)){
				return "error";
			}
			//上一步
			else if ("pre".equals(operate)){
				return "step2";
			}
			//完成入库
			else if ("complete".equals(operate)){
				//进行入库操作
				return userOperate.initData(data1, data2, data3, isNew, data1_old, data2_old, data3_old);
			}
		}
		else{
			data1 = userOperate.getBasciInfo(user_name);
			data1_old = data1;
			
			if (!"".equals(data1)){
				isNew = "0";
			}
			else{
				isNew = "1";
			}
			
			return "step1";
		}
		
		//若不是以上任一情况则返回错误
		return "error";
	}
	
	/**
	 * 查询当前设备树的下级子节点
	 * @return
	 * @throws Exception
	 */
	public String getDeviceTree() throws Exception{
		ajax = userOperate.getJsonTree().toString();
		
		
		return "ajax";
	}

	public String getAjax() {
		return ajax;
	}

	public String getDefault_privilege() {
		return default_privilege;
	}

	public void setDefault_privilege(String default_privilege) {
		this.default_privilege = default_privilege;
	}

	public String getData1_old() {
		return data1_old;
	}

	public void setData1_old(String data1_old) {
		this.data1_old = data1_old;
	}

	public String getData2_old() {
		return data2_old;
	}

	public void setData2_old(String data2_old) {
		this.data2_old = data2_old;
	}

	public String getData3_old() {
		return data3_old;
	}

	public void setData3_old(String data3_old) {
		this.data3_old = data3_old;
	}

}
