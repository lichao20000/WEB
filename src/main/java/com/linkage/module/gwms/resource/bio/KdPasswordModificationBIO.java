package com.linkage.module.gwms.resource.bio;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;
import ACS.Rpc;
import PreProcess.UserInfo;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.dao.KdPasswordModificationDAO;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.PreProcessInterface;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;

/**
 * 
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-4-13
 * @category com.linkage.module.gwms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class KdPasswordModificationBIO
{
	//日志记录
	private static Logger logger = LoggerFactory.getLogger(KdPasswordModificationDAO.class);
	private KdPasswordModificationDAO dao;
	public String updateKdPassword(String loid,String username,String netPasswd)
	{
		String str="";
		List<Map<String,String>> list=dao.getusername(loid, username, netPasswd);
		if(list!=null&&list.size()>0)
		{
			if(String.valueOf(list.get(0).get("wan_type")).equals("1"))
			{
				int flag=dao.updatepassword(StringUtil.getLongValue(list.get(0), "user_id", 0), username, netPasswd);
				if(flag>0)
				{
					str="修改成功";
				}else{
					str="修改失败";
				}
			}else if(String.valueOf(list.get(0).get("wan_type")).equals("2"))
			{
				int flag=testConnection(String.valueOf(list.get(0).get("device_id")));
				if(flag == 1){
					String user_id=String.valueOf(list.get(0).get("user_id"));
					String device_id=String.valueOf(list.get(0).get("device_id"));
					String oui=String.valueOf(list.get(0).get("oui"));
					String device_serialnumber=String.valueOf(list.get(0).get("device_serialnumber"));
					dao.updateServOpenStatus(Long.valueOf(user_id));
					dao.updatepassword(Long.valueOf(user_id), username, netPasswd);
					PreServInfoOBJ obj = new PreServInfoOBJ(
							StringUtil.getStringValue(user_id), device_id, oui,
							device_serialnumber, StringUtil.getStringValue("10"), "1");
					obj.setGatherId("1");
					UserInfo uinfo = CreateObjectFactory.createPreProcess().GetPPBindUserList(obj);
					UserInfo[] uinfoarray = new UserInfo[] { uinfo };
					/*flag=CreateObjectFactory.createPreProcess("1").processServiceInterface(
							CreateObjectFactory.createPreProcess()
							.GetPPBindUserList(preInfoObj));*/
					PreProcessInterface ppc = CreateObjectFactory.createPreProcess(
							LipossGlobals.getGw_Type(device_id));
					logger.warn(
							"inter_ PreProcess (userId:{}, deviceId:{}, gatherId:{}, oui:{}, deviceSN:{}, servTypeId:{}, operTypeId:{})",
							new Object[] { user_id, device_id, "1", oui, device_serialnumber,
									"10", "1" });
					int ret = ppc.processServiceInterface(uinfoarray);
					if(ret==1)
					{
						str = "修改成功，已调用业务下发";	
					}else if(ret==-1){
						str = "参数为空";	
					}else{
						str="绑定失败";
					}
				}else if (flag == 0){
					str = "发生未知连接错误！";
				}
				else if (flag == -1){
					str = "设备连接不上！";
				}
				else if (flag == -2){
					str = "设备参数为空！";
				}
				else if (flag == -3){
					str = "设备正被操作！";
				}
				else if (flag == -4){
					str = "未知错误原因！";
				}
				else {
					str = "发生未知连接错误！";
				}
			}
		}else
		{
			str="请输入正确的宽带账号和宽带密码!";
		}
				
		return str;
	}
	/**
	 * 获取设备在线状态
	 * @param request
	 * @return
	 */
	public int testConnection(String device_id)
	{
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "";
		rpcArr[0].rpcValue = "";
		devRPCArr[0].rpcArr = rpcArr;
		// corba
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.RpcTest_Type);
		int flag = 0;
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
		}
		else
		{
			flag = devRPCRep.get(0).getStat();
		}
		return flag;
	}
	
	public KdPasswordModificationDAO getDao()
	{
		return dao;
	}
	
	public void setDao(KdPasswordModificationDAO dao)
	{
		this.dao = dao;
	}
	
}
