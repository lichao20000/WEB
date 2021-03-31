package com.linkage.module.liposs.performance.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.corba.interfacecontrol.PmeeInterface;
import com.linkage.module.liposs.performance.dao.I_configPmeeDao;
import com.linkage.system.systemlog.core.SystemLog;
import com.linkage.system.systemlog.core.SystemLogBean;
import com.linkage.system.systemlog.core.SystemLogModuleCons;

public class ConfigPmeeBio implements I_configPmeeBio {
	private I_configPmeeDao cpd;
	private ThreadPool threadpool;//线程池【需要注入】
	private static Logger log = LoggerFactory.getLogger(ConfigPmeeBio.class);

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.bio.performance.I_configPmeeBio#ChangeInterval(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean ChangeInterval(String interval,String expressionid,String device_id,SystemLogBean slb){
		if(cpd.ChangeInterval(interval, expressionid, device_id)){
			return NoticePmeeReadDevices(device_id,"修改采集时间间隔入库成功",slb);
		}else{
			SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,"修改采集时间间隔入库失败！");
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.bio.performance.I_configPmeeBio#SaveWarn(java.lang.String, java.lang.String, com.linkage.liposs.bio.performance.Pm_Map_Instance)
	 */
	public boolean SaveWarn(String device_id,String expression_id,Pm_Map_Instance pm,SystemLogBean slb){
		//组装编辑实例的SQL
		String sql="update pm_map_instance set mintype="+pm.getMintype()
										    +",minthres="+pm.getMinthres()
										    +",mindesc='"+pm.getMindesc()
										    +"',mincount="+pm.getMincount()
										    +",minwarninglevel="+pm.getMinwarninglevel()
										    +",minreinstatelevel="+pm.getMinreinstatelevel()
										    +",maxtype="+pm.getMaxtype()
										    +",maxthres="+pm.getMaxthres()
										    +",maxdesc='"+pm.getMaxdesc()
										    +"',maxcount="+pm.getMaxcount()
										    +",maxwarninglevel="+pm.getMaxwarninglevel()
										    +",maxreinstatelevel="+pm.getMaxreinstateleve()
										    +",dynatype="+pm.getDynatype()
										    +",beforeday="+pm.getBeforeday()
										    +",dynathres="+pm.getDynathres()
										    +",dynacount="+pm.getDynacount()
										    +",dynadesc='"+pm.getDynadesc()
										    +"',dynawarninglevel="+pm.getDynawarninglevel()
										    +",dynareinstatelevel="+pm.getDynareinstatelevel()
										    +",mutationtype="+pm.getMutationtype()
										    +",mutationthres="+pm.getMutationthres()
										    +",mutationcount="+pm.getMutationcount()
										    +",mutationdesc='"+pm.getMutationdesc()
										    +"',mutationwarninglevel="+pm.getMutationwarninglevel()
										    +" where device_id='"+device_id+"' and expressionid="+expression_id;

		PrepareSQL psql = new PrepareSQL(sql);
		boolean flg=cpd.EditPmeeExpID(psql.getSQL());
		if(flg){
			flg=NoticePmeeReadDevices(device_id,"配置告警入库成功，",slb);
		}else{
			SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,"配置告警入库失败！");
		}
		return flg;
	}
	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.bio.performance.I_configPmeeBio#DelPmeeExpression(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean DelPmeeExpression(String device_id,String expressionid,String id,String type,SystemLogBean slb){
		boolean flg=cpd.DelPmeeExpression(device_id, expressionid, id, type);
		if(flg){
			flg=NoticePmeeReadDevices(device_id,"删除性能实例，操作数据库成功",slb);
		}else{
			SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,"删除性能实例，操作数据库失败！");
		}
		return true;
	}

	/**
	 * 单个设备通知后台
	 * @param device_id
	 * @return
	 */
	private boolean NoticePmeeReadDevices(String device_id,String str,SystemLogBean slb){
		try{
			int retflag = PmeeInterface.GetInstance().readDevices(new String[]{ device_id });
			if(retflag==0){
				SystemLog.success(slb,SystemLogModuleCons.MODULE_CONFIG,str+",通知后台成功！");
				return true;
			}else{
				SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,str+",通知后台失败！");
				return false;
			}
		}catch(Exception e){
			log.error("通知后台PMEE失败！");
			e.printStackTrace();
			SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,str+",通知后台失败！");
			return false;
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.bio.performance.I_configPmeeBio#EditPmeeExpression(java.lang.String, java.lang.String, java.lang.String, com.linkage.liposs.bio.performance.Pm_Map_Instance)
	 */
	public boolean EditPmeeExpression(String device_id,String expressionid,String id,Pm_Map_Instance pm,SystemLogBean slb){
		//组装编辑实例的SQL
		String sql="update pm_map_instance set mintype="+pm.getMintype()
											+",intodb="+pm.getIntodb()
										    +",minthres="+pm.getMinthres()
										    +",mindesc='"+pm.getMindesc()
										    +"',mincount="+pm.getMincount()
										    +",minwarninglevel="+pm.getMinwarninglevel()
										    +",minreinstatelevel="+pm.getMinreinstatelevel()
										    +",maxtype="+pm.getMaxtype()
										    +",maxthres="+pm.getMaxthres()
										    +",maxdesc='"+pm.getMaxdesc()
										    +"',maxcount="+pm.getMaxcount()
										    +",maxwarninglevel="+pm.getMaxwarninglevel()
										    +",maxreinstatelevel="+pm.getMaxreinstateleve()
										    +",dynatype="+pm.getDynatype()
										    +",beforeday="+pm.getBeforeday()
										    +",dynathres="+pm.getDynathres()
										    +",dynacount="+pm.getDynacount()
										    +",dynadesc='"+pm.getDynadesc()
										    +"',dynawarninglevel="+pm.getDynawarninglevel()
										    +",dynareinstatelevel="+pm.getDynareinstatelevel()
										    +",mutationtype="+pm.getMutationtype()
										    +",mutationthres="+pm.getMutationthres()
										    +",mutationcount="+pm.getMutationcount()
										    +",mutationdesc='"+pm.getMutationdesc()
										    +"',mutationwarninglevel="+pm.getMutationwarninglevel()
										    +" where id='"+id+"'";
		PrepareSQL psql = new PrepareSQL(sql);
		boolean flg=cpd.EditPmeeExpID(psql.getSQL());

		if(flg){
			flg=NoticePmeeReadDevices(device_id,"编辑性能实例，入库成功",slb);
		}else{
			SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,"编辑性能实例，操作数据库失败！");
		}
		return flg;
	}
	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.bio.performance.I_configPmeeBio#getConfigExp(java.lang.String, java.lang.String)
	 */
	public String getConfigExp(String device_id,String expressionid){
		List<Map<String,String>> list=cpd.getConfigedExpInfo(device_id, expressionid);
		Map<String,String> map=new HashMap<String,String>();

		for(Map<String,String> m:list){
			map.put(m.get("name"),m.get("name"));
		}
		if(map==null || map.isEmpty()){
			return "";
		}
		String ajax="";
		Iterator<String> it=map.keySet().iterator();
		while(it.hasNext()){
			ajax+=it.next()+"\n";
		}
		return ajax;
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.bio.performance.I_configPmeeBio#ConfigPmeeDB(java.lang.String, java.lang.String)
	 */
	public boolean ConfigPmeeDB(String device_id,String expressionid,Pm_Map_Instance pm,SystemLogBean slb){
		log.debug("\ndevice_id="+device_id);
		log.debug("\nexpressionid="+expressionid);
		//将设备列表转换为List
		String[] tmp=device_id.split(",");
		int n=tmp.length;
		List<String> dev_list=new ArrayList<String>(n);
		for(int i=0;i<n;i++){
			dev_list.add(tmp[i]);
		}
		//将性能表达式转换为List
		tmp=expressionid.split(",");
		n=tmp.length;
		List<Integer> exp_list=new ArrayList<Integer>(n);
		for(int i=0;i<n;i++){
			exp_list.add(Integer.parseInt(tmp[i]));
		}
		try{
			PmConfigMainThread pt=new PmConfigMainThread(pm,dev_list,exp_list,threadpool,cpd);
			threadpool.submitHighLevelTask(pt);
		}catch(Exception e){
			e.printStackTrace();
			SystemLog.error(slb,SystemLogModuleCons.MODULE_CONFIG,"配置性能实例，通知后台失败！");
			return false;
		}
		SystemLog.success(slb,SystemLogModuleCons.MODULE_CONFIG,"配置性能实例，通知后台成功！");
		return true;
	}

	//**************************************初始化DAO实例***************************//
	public void setCpd(I_configPmeeDao cpd) {
		this.cpd = cpd;
	}

	public void setThreadpool(ThreadPool threadpool) {
		this.threadpool = threadpool;
	}
}
