package com.linkage.module.liposs.performance.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.liposs.performance.dao.module.ModuleManagerRelateFactory;
import com.linkage.module.liposs.system.basesupport.BaseSupportDAO;
import com.linkage.module.liposs.system.moduleinterface.ModuleManager;
import com.linkage.system.systemlog.core.SystemLog;
import com.linkage.system.systemlog.core.SystemLogBean;
import com.linkage.system.systemlog.core.SystemLogModuleCons;

public class ModuleManagerDaoImp extends BaseSupportDAO implements ModuleManagerDao {
	private static Logger log = LoggerFactory.getLogger(ModuleManagerDaoImp.class);
	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.ModuleManagerDao#getVendorList()
	 */
	public List<Map> getVendorList() {
		String sql="select vendor_id,vendor_name from tab_vendor order by vendor_id";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.ModuleManagerDao#getSerialListByVendorID(java.lang.String)
	 */
	public List<Map> getSerialListByVendorID(String vendor_id) {
		String sql="select serial ,device_name from tab_devicetype_info where vendor_id="+vendor_id+" order by serial";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.ModuleManagerDao#getConfigTypeList()
	 */
	// 没有类调用 注释 2020/10/26
	public List<Map> getConfigTypeList() {
		String sql="select * from tab_template_config order by configtype";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.ModuleManagerDao#getRelateExpression(java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, java.lang.String)
	 */
	public List<Map> getRelateExpression(ModuleManager mm) {
		return ModuleManagerRelateFactory.factory(mm.getConfigtype()).getRelateTabAllData(mm, jt);
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.ModuleManagerDao#getConfigNumBySerial(java.lang.String)
	 */
	public int getConfigNumBySerial(String serial) {
		String sql="select count(*) as num from tab_devicemodel_template where serial="+serial;
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForInt(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.ModuleManagerDao#saveTemplate(java.lang.String, java.lang.String, java.lang.String[][], java.lang.String[], com.linkage.system.systemlog.core.SystemLogBean)
	 */
	public boolean saveTemplate(String template_name, String serial,
			String[][] atrrvalue, String[] configtype,SystemLogBean slb) {
		String sql="";
		ArrayList<String> list=new ArrayList<String>();
		//删除已配置模板
		sql="delete from tab_devicemodel_template where serial="+serial;
		PrepareSQL psql = new PrepareSQL(sql);
		list.add(psql.getSQL());
		//删除指标
		sql="delete from tab_devicemodel_template_info where serial="+serial;
		psql = new PrepareSQL(sql);
		list.add(psql.getSQL());
		//添加模板表
		int n=configtype.length;
		for(int i=0;i<n;i++){
			sql="insert into tab_devicemodel_template(template_name,serial,configtype) values('"+template_name+"',"+serial+","+configtype[i].split("-/-")[0]+")";
			psql = new PrepareSQL(sql);
			list.add(psql.getSQL());
		}
		//添加指标信息表
		if(atrrvalue!=null){
			n=atrrvalue.length;
			for(int i=0;i<n ;i++){
				sql="insert into tab_devicemodel_template_info(serial,configtype,atrrvalue) values("+serial+","+atrrvalue[i][0]+","+atrrvalue[i][1]+")";
				psql = new PrepareSQL(sql);
				list.add(psql.getSQL());
			}
		}

		n=list.size();
		String[] sqlArr=new String[n];
		int[] num=jt.batchUpdate(list.toArray(sqlArr));
		list=null;
		boolean flg=num.length<n?false:true;
		if(flg){
			SystemLog.success(slb, SystemLogModuleCons.MODULE_CONFIG,"【模板配置】设备型号ID:"+serial+",模板名称:"+template_name+"成功!");

		}else{
			SystemLog.error(slb, SystemLogModuleCons.MODULE_CONFIG,"【模板配置】设备型号ID:"+serial+",模板名称:"+template_name+"失败!");
		}
		return flg;
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.ModuleManagerDao#getConfigTemplateInfo()
	 */
	public List<Map> getConfigTemplateInfo() {
		String sql="select a.vendor_id,a.vendor_name,b.device_name,b.serial,c.template_name,c.configtype,d.name "
			+" from tab_vendor a,tab_devicetype_info b,tab_devicemodel_template c,tab_template_config d"
			+" where a.vendor_id=b.vendor_id and b.serial=c.serial and c.configtype=d.configtype order by a.vendor_id,b.serial";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.ModuleManagerDao#getConfigRelateExpression(com.linkage.module.liposs.performance.ModuleManager, boolean)
	 */
	public List<Map> getConfigRelateExpression(ModuleManager mm,boolean isconfig) {
		//获取已配置的
		if(isconfig){
			return ModuleManagerRelateFactory.factory(mm.getConfigtype()).getConfigRelateTabData(mm, jt);
		}else{//获取未配置的
			return ModuleManagerRelateFactory.factory(mm.getConfigtype()).getUnConfigRelateTabData(mm, jt);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.ModuleManagerDao#delTemplate(java.lang.String)
	 */
	public boolean delTemplate(String serial,SystemLogBean slb) {
		String[] sql=new String[2];
		sql[0]="delete from tab_devicemodel_template where serial="+serial;
		sql[1]="delete from tab_devicemodel_template_info where serial="+serial;
		log.debug("【北京酒店网管 模板管理】删除模板：\n"+sql[0]+"\n"+sql[1]);
		PrepareSQL psql = new PrepareSQL(sql[0]);
		psql.getSQL();
		psql = new PrepareSQL(sql[1]);
		psql.getSQL();
		int[] num=jt.batchUpdate(sql);
		boolean flg=num.length<2?false:true;
		if(flg){
			SystemLog.success(slb, SystemLogModuleCons.MODULE_CONFIG,"【模板配置】设备型号ID:"+serial+"删除成功!");
		}else{
			SystemLog.error(slb, SystemLogModuleCons.MODULE_CONFIG,"【模板配置】设备型号ID:"+serial+"删除失败!");
		}
		return num.length==2?true:false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.ModuleManagerDao#getTemplateInfoByDevID(java.lang.String)
	 */
	public List<Map> getTemplateInfoByDevID(String device_id) {
		//由于有的设备型号有几个版本，因此需要区分版本
		String sql="select a.serial,b.vendor_id from tab_devicetype_info a,tab_deviceresource b "
			+"where a.device_name=b.device_model and a.vendor_id=b.vendor_id and b.device_id='"+device_id+"'";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> serialList=jt.queryForList(psql.getSQL());
		String serial;
		String vendor_id;
		if(serialList==null || serialList.isEmpty()){
			return null;
		}else if(serialList.size()>1){
			sql="select a.serial,b.vendor_id from tab_devicetype_info a,tab_deviceresource b "
				+"where a.device_name=b.device_model and a.os_version=b.os_version and a.vendor_id=b.vendor_id and b.device_id='"+device_id+"'";
			psql = new PrepareSQL(sql);
			serialList=jt.queryForList(psql.getSQL());
			if(serialList==null || serialList.isEmpty() || serialList.size()>1){
				return null;
			}else{
				serial=serialList.get(0).get("serial")+"";
				vendor_id=serialList.get(0).get("vendor_id")+"";
			}
		}else{
			serial=serialList.get(0).get("serial")+"";
			vendor_id=serialList.get(0).get("vendor_id")+"";
		}
		sql="select a.isrelate as isrelate,a.name as template_name,a.id as id,a.configtype as configtype,a.url as url,"
			+vendor_id+" as vendor_id, "+serial+" as serial"
			+" from tab_template_config a,tab_devicemodel_template b where a.configtype=b.configtype and b.serial="+serial;
		psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.ModuleManagerDao#getDeviceInfo(java.lang.String)
	 */
	public Map<String,String> getDeviceInfo(String device_id) {
		String sql="select gather_id,loopback_ip,snmp_ro_community from tab_deviceresource where device_id='"+device_id+"'";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForMap(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.ModuleManagerDao#getIDByConfigtype(java.lang.String)
	 */
	public List<Map> getIDByConfigtype(String configtype) {
		String sql="select id,name,configtype from tab_template_config where configtype in("+configtype+")";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.liposs.performance.dao.ModuleManagerDao#getAttrValue(java.lang.String, java.lang.String)
	 */
	public List<Map<String,Integer>> getAttrValue(String configtype,String serial) {
		String sql="select atrrvalue,configtype from tab_devicemodel_template_info where configtype in("+configtype+") and serial="+serial;
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForList(psql.getSQL());
	}

}
