package com.linkage.module.liposs.performance.bio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.litms.common.corba.interfacecontrol.FluxManagerInterface;
import com.linkage.module.liposs.performance.bio.snmpGather.FluxConfigInit;
import com.linkage.module.liposs.performance.bio.snmpGather.FluxPortInfo;
import com.linkage.module.liposs.performance.bio.snmpGather.PortJudgeAttr;
import com.linkage.module.liposs.performance.bio.snmpGather.ReadFluxConfigPortInfo;
import com.linkage.module.liposs.performance.dao.ConfigFluxDao;
import com.linkage.system.systemlog.core.SystemLog;
import com.linkage.system.systemlog.core.SystemLogBean;
import com.linkage.system.systemlog.core.SystemLogModuleCons;

public class ConfigFluxBio {
	protected ThreadPool threadpool;//线程池【需要注入】
	protected ConfigFluxDao cfd;//
	protected FluxConfigInit fci;//
	protected SystemLogBean slb;
	/**
	 * 保存流量
	 * @param devID_List:设备ID列表
	 * @param fmi：告警数据类
	 * @param portList：端口列表
	 * @param isauto：是否自动配置
	 * @param total：是否整体配置
	 * @param gather_flag：采集标志
	 * @param intodb：是否入库
	 * @param polltime：采集时间间隔
	 * @param isKeep:是否保留配置
	 * @return
	 */
	public boolean saveFlux(List<String> devID_List,Flux_Map_Instance fmi,List<FluxPortInfo> portList,int isauto, int total, String gather_flag, int intodb,int polltime,boolean isKeep){
		try{
			FluxConfigMainThread fmt=new FluxConfigMainThread(threadpool,devID_List,fmi,fci,cfd,portList,isauto,total,gather_flag,intodb,polltime,isKeep);
			threadpool.submitHighLevelTask(fmt);
			SystemLog.success(slb,SystemLogModuleCons.MODULE_CONFIG,"配置流量，通知后台成功！");
		}catch(Exception e){
			e.printStackTrace();
			SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,"配置流量，通知后台失败！");
			return false;
		}
		return true;
	}
	/**
	 * 默认模板配置流量
	 * @param device_id
	 * @return
	 */
	public boolean defaultSaveFlux(String device_id){
		try{
			FluxConfigMainThread fmt=new FluxConfigMainThread(threadpool,Arrays.asList(device_id),fci,cfd);
			threadpool.submitHighLevelTask(fmt);
			SystemLog.success(slb,SystemLogModuleCons.MODULE_CONFIG,"配置流量，通知后台成功！");
		}catch(Exception e){
			e.printStackTrace();
			SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,"配置流量，通知后台失败！");
			return false;
		}
		return true;
	}
	/**
	 * 编辑流量端口
	 * @param device_id:设备ID
	 * @param port_info：端口信息:getway|||port_info
	 * @param intodb:是否入库 0:不入库 1：入库
	 * @param gatherflag:是否采集 0：不采集 1：采集
	 * @param fmi
	 * @return
	 */
	public int editFluxPort(String device_id,String[] port_info,int intodb,int gatherflag,Flux_Map_Instance fmi){
		String pif="";
		int n=port_info.length;
		for(int i=0;i<n;i++){
			pif+=","+port_info[i];
		}
		pif=pif.equals("")?"":pif.substring(1);
		if(cfd.editFluxPort(device_id, port_info, intodb, gatherflag, fmi)){
			if(informMCFluxConfig(device_id.split(","))){
				 SystemLog.success(slb,SystemLogModuleCons.MODULE_CONFIG,"设备"+device_id+"端口"+pif+",编辑流量端口成功，通知后台成功！");
				 return PMEEGlobal.Success;
			 }else{
				 SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,"设备"+device_id+"端口"+pif+",编辑流量端口成功，通知后台失败！");
				 return PMEEGlobal.Error_Process;
			 }
		}else{
			SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,"设备"+device_id+"端口"+pif+",编辑流量端口失败！");
			return PMEEGlobal.Error_DataBase;
		}
	}
	/**
	 * 删除流量配置
	 * @param device_id:设备ID
	 * @return 0:成功 -1：删除失败 -2：通知后台失败
	 */
	public int delFluxConfig(String device_id) {
		if(cfd.delFluxConfig(device_id)){
			if(informMCFluxConfig(device_id.split(","))){
				 SystemLog.success(slb,SystemLogModuleCons.MODULE_CONFIG,"删除设备"+device_id+"流量配置成功，通知后台成功！");
				 return PMEEGlobal.Success;
			 }else{
				 SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,"删除设备"+device_id+"流量配置成功，通知后台失败！");
				 return PMEEGlobal.Error_Process;
			 }
		}else{
			SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,"删除设备"+device_id+"流量配置失败！");
			return PMEEGlobal.Error_DataBase;
		}
	}
	
	/**
	 * 删除已配置的端口
	 * @param device_id:设备ID
	 * @param port_info：端口信息【之间用-/-分隔】:-/-getway|||port_info-/-
	 * @return 0:成功 -1：删除失败 -2：通知后台失败
	 */
	public int delFluxPort(String device_id, String port_info) {
		String[]ports=port_info.split("-/-");
		if(cfd.delFluxPort(device_id, ports)){
			if(informMCFluxConfig(device_id.split(","))){
				 SystemLog.success(slb,SystemLogModuleCons.MODULE_CONFIG,"删除设备"+device_id+"端口"+port_info+"流量配置成功，通知后台成功！");
				 return PMEEGlobal.Success;
			 }else{
				 SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,"删除设备"+device_id+"端口"+port_info+"流量配置成功，通知后台失败！");
				 return PMEEGlobal.Error_Process;
			 }
		}else{
			SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,"删除设备"+device_id+"端口"+port_info+"流量配置失败！");
			return PMEEGlobal.Error_DataBase;
		}
	}
	/**
	 * 通知MC配置信息改变
	 * 
	 * @param device_ids
	 * @return
	 */
	private boolean informMCFluxConfig(String[] device_ids)
	{
		boolean result = true;
		try
		{
			FluxManagerInterface.GetInstance().readDevices(device_ids);
		}
		catch(Exception e)
		{
			result = false;
			e.printStackTrace();
		}		
		return result;
	}
	
	
	
	/**
     * 获取设备的基本端口信息
     * @param oidList  oid列表
     * @param device_id   设备ID
     * @param serial  设备型号ID
     * @return  
     */
	public List<FluxPortInfo> getPortBaseInfo(List<PortJudgeAttr> oidList,String device_id,String serial)
	{

		ArrayList<FluxPortInfo> retList = new ArrayList<FluxPortInfo>();
		// 从后台获取相关数据
		retList = ReadFluxConfigPortInfo.getDeviceInfo(device_id,oidList);
		return fci.getWayofBaseInfo(retList);

	}
	
	
	/**
	 * 获取指定模型的OID
	 * @param serial
	 * @param snmpversion
	 * @param counternum
	 * @param oidtype
	 * @return
	 */
	public List<PortJudgeAttr> getOIDList(String serial, String snmpversion,String counternum, String oidtype)
	{
		return fci.getOIDList(serial,snmpversion,counternum,oidtype);
	}
	
	/**
	 * 查询设备配置情况【支持批量配置,device_id间用,分隔】
	 * @param deviceList:设备ID
	 * @return
	 * <ul>
	 * 		<li><font color='blue'>设备ID：</font>device_id</li>
	 *      <li><font color='blue'>设备名称:</font>device_name</li>
	 *      <li><font color='blue'>设备IP：</font>loopback_ip</li>
	 *      <li><font color='blue'>设备厂商：</font>vendor_name</li>
	 *      <li><font color='blue'>设备型号：</font>device_model</li>
	 *      <li><font color='blue'>设备型号</font>serial</li>
	 *      <li><font color='blue'>配置状态：</font>result</li>
	 * </ul>
	 */
	public List<Map> getDevConfigResult(List<String> deviceList) {
		// TODO Auto-generated method stub
		List<Map> devInfoList=cfd.getDevInfo(deviceList);
		List<Map<String,String>> configResult=cfd.getDevConfigFluxResult(deviceList);
		Map<String,String> map=new HashMap<String,String>();
		for(Map<String,String> m:configResult){
			map.put(m.get("device_id"),m.get("device_id"));
		}
		for(Map<String,String> m:devInfoList){
			m.put("result",map.get(m.get("device_id"))!=null?"true":"false");
		}
		return devInfoList;
	}
	
	
	
	public void setThreadpool(ThreadPool threadpool) {
		this.threadpool = threadpool;
	}
	public ConfigFluxDao getCfd() {
		return cfd;
	}
	public void setCfd(ConfigFluxDao cfd) {
		this.cfd = cfd;
	}
	public ThreadPool getThreadpool() {
		return threadpool;
	}
	public FluxConfigInit getFci() {
		return fci;
	}
	public void setFci(FluxConfigInit fci) {
		this.fci = fci;
	}
	public SystemLogBean getSlb() {
		return slb;
	}
	public void setSlb(SystemLogBean slb) {
		this.slb = slb;
	}
}
