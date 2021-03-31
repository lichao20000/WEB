package com.linkage.module.liposs.performance.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.liposs.performance.bio.PMEEGlobal;
import com.linkage.module.liposs.performance.bio.Pm_Map_Instance;
import com.linkage.module.liposs.system.basesupport.BaseSupportDAO;

/**
 * 性能配置DAO
 *
 * @author BENYP(5260) E-MAIL:BENYP@LIANCHUANG.COM
 * @version 1.0
 * @since 2008-09-25
 */
public class ConfigPmeeDao extends BaseSupportDAO implements I_configPmeeDao {
	private static Logger log = LoggerFactory.getLogger(ConfigPmeeDao.class);
	private List<Map<String,String>> CityList;//存放属地列表
	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#ChangeInterval(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean ChangeInterval(String interval,String expressionid,String device_id){
		String sql="update pm_map set interval="+interval+" where device_id='"+device_id+"' and expressionid="+expressionid;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		try{
			jt.execute(sql);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#getDevListByName(java.lang.String, java.lang.String, long)
	 *
	 * 将原方法做了以下修改
	 * 将原来的SQL拼接做了改造
	 * 注释了sql += " and a.device_type in('Navigator1-2','Navigator2-1','Navigator2-2')"，如果不注释，将查询不到相关设备
	 * 增加了psql.append(" and a.gw_type = "+gw_type);  // 设备类型 gw_type=1 家庭网关设备，gw_type=2 企业网关设备
	 *
	 */
	public List<Map<String,String>> getDevListByName(String gw_type, String device_name,String loopback_ip,long area_id,boolean isadmin){

		log.debug("ConfigPmeeDao==>getDevListByName({},{},{},{},{})", new Object[] {
				gw_type, device_name, loopback_ip, area_id, isadmin });

		PrepareSQL psql = new PrepareSQL();

		psql.append("select a.device_id,a.device_name,a.loopback_ip,a.device_serialnumber as device_model,b.city_name ");
		psql.append(" from tab_gw_device a left join tab_city b on a.city_id = b.city_id");
		if(isadmin){
			psql.append(" where 1=1 ");
		}else{
			psql.append(" where a.device_id in(select distinct res_id from tab_gw_res_area where res_type=1 and area_id=");
			psql.append(area_id+"");
			psql.append(") ");
		}

		psql.append(" and a.gw_type = "+gw_type);

//		sql += " and a.device_type in('Navigator1-2','Navigator2-1','Navigator2-2')";

		String param="";
		//选择设备名称
		if(device_name!=null && !device_name.equals("")){
//			param+=" and a.device_name like'%"+device_name+"%'";
			param+=" and a.device_serialnumber like'%"+device_name+"%'";
		}
		//选择设备IP
		if(loopback_ip!=null && !loopback_ip.equals("")){
			param+=" and a.loopback_ip like '%"+loopback_ip+"%'";
		}
		param+=" order by a.device_id";

		psql.append(param);

		return jt.queryForList(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#getVendorList()
	 */
	public List<Map<String,String>> getVendorList(){
//		String sql="select vendor_id,vendor_name from tab_vendor where vendor_id in(select distinct oui from tab_gw_device)";
		String sql="select vendor_id,vendor_add from tab_vendor";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#getCityList(java.lang.String)
	 */
	public List<Map<String,String>> getCityList(String city_id){
		Map<String,String> map=getShelfCityName(city_id);
		CityList=new ArrayList<Map<String,String>>();
		CityList.add(map);
		List<Map<String,String>> tmplist=new ArrayList<Map<String,String>>();
		tmplist.add(map);
		getChildCityList(tmplist);
		tmplist=null;
		String tmp="";
		for(Map<String,String> m:CityList){
			tmp+=",'"+m.get("city_id")+"'";
		}
		tmp=tmp.substring(1);
		String sql="select city_id,city_name from tab_city where " ;
		if(city_id.equals("00")){
			sql += " city_id = '00'";
		}else{
			sql += " city_id in ("+tmp+")" ;
		}
			sql += " order by city_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	private List<Map<String,String>> getCityListNotOrder(String city_id){
		Map<String,String> map=getShelfCityName(city_id);
		CityList=new ArrayList<Map<String,String>>();
		CityList.add(map);
		List<Map<String,String>> tmplist=new ArrayList<Map<String,String>>();
		tmplist.add(map);
		getChildCityList(tmplist);
		tmplist=null;
		return CityList;
	}
	/**
	 * 获取子属地列表
	 * @param parent_id
	 * @return
	 */
	private List<Map<String,String>> getChildCityList(String parent_id){
		String sql="select city_id,city_name from tab_city where parent_id in("+parent_id+") order by city_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	/**
	 * 获取自身属地信息
	 * @param city_id
	 * @return
	 */
	private Map<String,String> getShelfCityName(String city_id){
		String sql="select city_id,city_name from tab_city where city_id='"+city_id+"'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForMap(sql);
	}
	/**
	 * 获取子属地
	 * @param citylist
	 */
	private void getChildCityList(List<Map<String,String>> citylist){
		if(citylist==null || citylist.isEmpty()){
			return;
		}
		String tmp="";
		for(Map<String,String> m:citylist){
			tmp+=",'"+m.get("city_id")+"'";
		}
		tmp=tmp.substring(1);
		List<Map<String,String>> list=getChildCityList(tmp);
		CityList.addAll(list);
		getChildCityList(list);
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#getDeviceModelByVendor(java.lang.String)
	 */
	public List<Map<String,String>> getDeviceModelByVendor(String vendor_id){
		//String sql="select distinct device_serialnumber as device_model from tab_gw_device where oui='"+vendor_id+"'";
//		String sql="select device_model_id,device_model from gw_device_model where oui='"+vendor_id+"'";
		String sql="select device_model_id,device_model from gw_device_model where vendor_id='"+vendor_id+"'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#getDeviceList(java.lang.String, java.lang.String, java.lang.String, long)
	 *
	 * 将原方法做了以下修改
	 * 将原来的SQL拼接做了改造
	 * 注释了sql += " and a.device_type in('Navigator1-2','Navigator2-1','Navigator2-2')"，如果不注释，将查询不到相关设备
	 * 增加了psql.append(" and a.gw_type = "+gw_type);  // 设备类型 gw_type=1 家庭网关设备，gw_type=2 企业网关设备
	 *
	 */
	public List<Map<String, String>> getDeviceList(String gw_type, String city_id,
			String vendor_id, String device_model, long area_id, boolean isadmin) {

		log.debug("ConfigPmeeDao==>getDeviceList({},{},{},{},{},{})", new Object[] {
				gw_type, city_id, vendor_id, device_model, area_id, isadmin });

		PrepareSQL psql = new PrepareSQL();

		psql.append("select a.device_id,a.device_name,a.loopback_ip,a.device_serialnumber as device_model,b.city_name ");
		psql.append(" from tab_gw_device a left join tab_city b on a.city_id = b.city_id");
		if(isadmin){
			psql.append(" where 1=1 ");
		}else{
			psql.append(" where a.device_id in(select distinct res_id from tab_gw_res_area where res_type=1 and area_id="+area_id+") ");
		}

		psql.append(" and a.gw_type = "+gw_type);

//		sql += " and a.device_type in('Navigator1-2','Navigator2-1','Navigator2-2')";
		String param="";
		//选择属地
		if(city_id!=null && !city_id.equals("")){
			List<Map<String,String>> list=getCityListNotOrder(city_id);
			String cid="";
			for(Map<String,String> m:list){
				cid+=",'"+m.get("city_id")+"'";
			}
			if(city_id.equals("00")){
				param+=" and a.city_id = '00' ";
			}else{
				param+=" and a.city_id in ("+cid.substring(1)+")";
			}
		}
		//选择厂商
		if(vendor_id!=null && !vendor_id.equals("")){
			param+=" and a.vendor_id='"+vendor_id+"'";
		}
		//选择设备型号
		if(device_model!=null && !device_model.equals("")){
			param+=" and a.device_model_id='"+device_model+"'";
		}
		param+=" order by a.device_id";

		psql.append(param);


		return jt.queryForList(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#EditPmeeExpID(java.lang.String)
	 */
	public boolean EditPmeeExpID(String sql){
		log.debug("编辑单个实例："+sql);
		String[] sqltmp=sql.split(";");
		int[] n=jt.batchUpdate(sqltmp);
		if(null==n || n.length<1){
			return false;
		}
		return true;
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#DelPmeeExpression(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean DelPmeeExpression(String device_id, String expressionid,
			String id, String type) {
		if("all".equalsIgnoreCase(type)){
			return DelAllPmeeExp(device_id, expressionid);
		}else{
			return DelSinglePmeeExpID(device_id, expressionid, id);
		}
	}

	/**
	 * 删除该性能表达式的所有实例
	 * @param device_id
	 * @param expressionid
	 * @return
	 */
	private boolean DelAllPmeeExp(String device_id,String expressionid){
		boolean flg=true;
		String[] sql=new String[3];
		sql[0]="delete from pm_map_instance where device_id='"+device_id+"' and expressionid="+expressionid;
		sql[1]="delete from pm_map where device_id='"+device_id+"' and expressionid="+expressionid;
		sql[2]="delete from pm_domain_instance where device_id='"+device_id+"' and expressionid="+expressionid;
		PrepareSQL psql = new PrepareSQL(sql[0]);
		psql.getSQL();
		psql = new PrepareSQL(sql[1]);
		psql.getSQL();
		psql = new PrepareSQL(sql[2]);
		psql.getSQL();
		int[] n=jt.batchUpdate(sql);
		if(null==n || n.length<1 ){
			flg=false;
		}
		return flg;
	}
	/**
	 * 删除单个实例,如果pm_map_instance中没有相应的数据，则将pm_map中相关数据也删除
	 * @param device_id
	 * @param expressionid
	 * @param id
	 * @return
	 */
	private boolean DelSinglePmeeExpID(String device_id,String expressionid,String id){
		String[] sql=new String[2];
		sql[0]="delete from pm_domain_instance where domain_index in(select indexid from pm_map_instance where id='"+id+"')";
		sql[1]="delete from pm_map_instance where id='"+id+"'";
		PrepareSQL psql = new PrepareSQL(sql[0]);
		psql.getSQL();
		psql = new PrepareSQL(sql[1]);
		psql.getSQL();
		int[] n=jt.batchUpdate(sql);
		if(null==n || n.length<1 || n[1]!=1){
			return false;
		}
		//下面检查实例表pm_map_instance中是否还有数据,若无数据则将 pm_map/设备表达式映射表 中的相关数据删除
		sql[0]="select count(*) as num from pm_map_instance where device_id='"+device_id+"' and expressionid="+expressionid;
		psql = new PrepareSQL(sql[0]);
		psql.getSQL();
		int num=jt.queryForInt(sql[0]);
		if(num<1){
			sql[0]="delete from pm_map where device_id='"+device_id+"' and expressionid="+expressionid;
			psql = new PrepareSQL(sql[0]);
			psql.getSQL();
			n=jt.batchUpdate(sql);
			if(null==n || n.length<1 || n[0]!=1){
				return false;
			}
		}
		return true;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#getConfigedExp(java.lang.String,
	 *      java.lang.String)
	 */
	public List<Map<String, String>> getConfigedExpInfo(String device_id,
			String expressionid) {
		String devid="";
		if(device_id.contains(",")){
			String[] tmp=device_id.split(",");
			int n=tmp.length;
			for(int i=0;i<n;i++){
				devid+=tmp[i].contains("'")?tmp[i]:",'"+tmp[i]+"'";
			}
			devid=devid.substring(1);
		}else{
			devid=device_id.contains("'")?device_id:"'"+device_id+"'";
		}
		String sql = "select distinct a.descr, a.indexid, a.id, a.intodb, a.mintype, a.minthres, a.mindesc, a.mincount, " +
				"a.minwarninglevel, a.minreinstatelevel, a.maxtype, a.maxthres, a.maxdesc, a.maxcount, a.maxwarninglevel, " +
				"a.maxreinstatelevel, a.dynatype, a.beforeday, a.dynacount, a.dynadesc, a.dynawarninglevel, a.dynareinstatelevel, " +
				"a.mutationtype, a.mutationthres, a.mutationcount, a.mutationdesc, a.mutationwarninglevel, b.name " +
			"from pm_map_instance a,pm_expression b "
		   +"where a.expressionid=b.expressionid and a.expressionid in("
		   + expressionid + ") and a.device_id in(" + devid + ")";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#getVendorID(java.lang.String)
	 */
	public String getVendorID(String device_id) {
		String devid="";
		if(device_id.contains(",")){
			String[] tmp=device_id.split(",");
			int n=tmp.length;
			for(int i=0;i<n;i++){
				devid+=tmp[i].contains("'")?tmp[i]:",'"+tmp[i]+"'";
			}
			devid=devid.substring(1);
		}else{
			devid=device_id.contains("'")?device_id:"'"+device_id+"'";
		}
		String sql = "select distinct vendor_id from tab_gw_device where device_id in("+ devid + ")";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List<Map<String,String>> list=jt.queryForList(sql);
		return list.get(0).get("vendor_id");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#getDevInfo(java.lang.String)
	 */
	public Map getDevInfo(String device_id) {
		device_id=device_id.contains("'")?device_id:"'"+device_id+"'";
		String sql = "select device_id,device_name,loopback_ip,vendor_id from tab_gw_device where device_id="+ device_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForMap(sql);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#getExpressList(java.lang.String)
	 */
	public List<Map> getExpressList(String vendor_id,String expressionid,boolean flg) {
		String sql = "select expressionid,name,descr from pm_expression where class2=1 and company='"
				   + vendor_id + (flg?"' and expressionid in( "+expressionid+" )":"' and expressionid not in ("+expressionid+")")+" order by expressionid";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#getConfigResultList(java.lang.String)
	 */
	public List<Map> getConfigResultList(String device_id) {
		String devid="";
		if(device_id.contains(",")){
			String[] tmp=device_id.split(",");
			int n=tmp.length;
			for(int i=0;i<n;i++){
				devid+=tmp[i].contains("'")?tmp[i]:",'"+tmp[i]+"'";
			}
			devid=devid.substring(1);
		}else{
			devid=device_id.contains("'")?device_id:"'"+device_id+"'";
		}
		String sql = " select a.device_id,a.expressionid,a.interval,a.isok,a.remark,b.name,b.descr "
				+ " from pm_map a,pm_expression b where  "
				+ " a.expressionid=b.expressionid and a.device_id in("
				+ devid + ") order by a.device_id, a.expressionid";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map map = new HashMap();
				map.put("expressionid", rs.getInt("expressionid"));// 表达式ID
				map.put("device_id", rs.getString("device_id"));// 设备ID
				map.put("result", PMEEGlobal.ConfigResultMap.get(rs.getInt("isok")));// 配置结果
				map.put("isok", rs.getInt("isok"));// 配置结果
				map.put("interval", rs.getInt("interval"));// 采集时间间隔
				map.put("remark", PMEEGlobal.ConfigFialMap.get(rs.getInt("remark")));// 失败原因描述
				map.put("name", rs.getString("name"));// 表达式名称
				map.put("descr", rs.getString("descr"));// 表达式描述
				return map;
			}
		});
	}



	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#isConfigPmee(java.lang.String, java.lang.String)
	 */
	public boolean isConfigPmee(String device_id,String expression_id)
	{
		String sql="select count(*) num from pm_map where isok=1 and device_id='"+device_id+"' and expressionid="+expression_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		int num=jt.queryForInt(sql);
		if(num==0)
		{
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#getPmeeConfigParam(java.lang.String, java.lang.String)
	 */
	public Map<String,Pm_Map_Instance> getPmeeConfigParam(String device_id,String expression_id)
	{
		Map<String,Pm_Map_Instance> resultMap = new HashMap<String,Pm_Map_Instance>();
		String sql="select a.interval, b.intodb, b.mintype, b.mindesc, b.minthres, b.mincount, b.minreinstatelevel, " +
			"b.minwarninglevel, b.maxtype, b.maxcount, b.maxdesc, b.maxthres, b.maxreinstatelevel, " +
			"b.maxwarninglevel, b.dynatype, b.dynadesc, b.dynacount, b.beforeday, b.dynathres, " +
			"b.dynawarninglevel, b.dynareinstatelevel, b.mutationtype, b.mutationthres, " +
			"b.mutationdesc, b.mutationcount, b.mutationwarninglevel, b.mutationreinstatelevel, b.indexid " +
		"from pm_map a,pm_map_instance b where a.device_id=b.device_id " +
		"and a.expressionid=b.expressionid and a.device_id='"
			+device_id+"' and a.expressionid="+expression_id;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List<Map> instanceList =jt.queryForList(sql);
		Iterator<Map> instanceIt =instanceList.iterator();
		Map record=null;
		while(instanceIt.hasNext())
		{
			record=instanceIt.next();
			Pm_Map_Instance instanceConfigParam = new Pm_Map_Instance();
			instanceConfigParam.setInterval(((BigDecimal)record.get("interval")).intValue());
			instanceConfigParam.setIntodb(((BigDecimal)record.get("intodb")).intValue());

			/**
			 * 配置固定阈值
			 */
			instanceConfigParam.setMintype(((BigDecimal)record.get("mintype")).intValue());
			instanceConfigParam.setMindesc((String)record.get("mindesc"));
			instanceConfigParam.setMinthres(((BigDecimal)record.get("minthres")).intValue());
			instanceConfigParam.setMincount(((BigDecimal)record.get("mincount")).intValue());
			instanceConfigParam.setMinreinstatelevel(((BigDecimal)record.get("minreinstatelevel")).intValue());
			instanceConfigParam.setMinwarninglevel(((BigDecimal)record.get("minwarninglevel")).intValue());

			instanceConfigParam.setMaxtype(((BigDecimal)record.get("maxtype")).intValue());
			instanceConfigParam.setMaxcount(((BigDecimal)record.get("maxcount")).intValue());
			instanceConfigParam.setMaxdesc((String)record.get("maxdesc"));
			instanceConfigParam.setMaxthres(((BigDecimal)record.get("maxthres")).intValue());
			instanceConfigParam.setMaxreinstateleve(((BigDecimal)record.get("maxreinstatelevel")).intValue());
			instanceConfigParam.setMaxwarninglevel(((BigDecimal)record.get("maxwarninglevel")).intValue());


			/**
			 * 动态阈值
			 */
			instanceConfigParam.setDynatype(((BigDecimal)record.get("dynatype")).intValue());
			instanceConfigParam.setDynadesc((String)record.get("dynadesc"));
			instanceConfigParam.setDynacount(((BigDecimal)record.get("dynacount")).intValue());
			instanceConfigParam.setBeforeday(((BigDecimal)record.get("beforeday")).intValue());
			instanceConfigParam.setDynathres(((BigDecimal)record.get("dynathres")).floatValue());
			instanceConfigParam.setDynawarninglevel(((BigDecimal)record.get("dynawarninglevel")).intValue());
			instanceConfigParam.setDynareinstatelevel(((BigDecimal)record.get("dynareinstatelevel")).intValue());


			/**
			 * 突变阈值
			 */
			instanceConfigParam.setMutationtype(((BigDecimal)record.get("mutationtype")).intValue());
			instanceConfigParam.setMutationthres(((BigDecimal)record.get("mutationthres")).floatValue());
			instanceConfigParam.setMutationdesc((String)record.get("mutationdesc"));
			instanceConfigParam.setMutationcount(((BigDecimal)record.get("mutationcount")).intValue());
			instanceConfigParam.setMutationwarninglevel(((BigDecimal)record.get("mutationwarninglevel")).intValue());
			instanceConfigParam.setMutationreinstatelevel(((BigDecimal)record.get("mutationreinstatelevel")).intValue());

			resultMap.put((String)record.get("indexid"),instanceConfigParam);
		}


		//clear
		record=null;
		instanceIt=null;
		instanceList=null;

		return resultMap;
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#deleteConfig(java.lang.String, java.lang.String)
	 */
	public boolean  deleteConfig(String device_id,String expression_id)
	{
		String[] sqlArray = new String[2];
		sqlArray[0]="delete from pm_map where device_id='"+device_id+"' and expressionid="+expression_id;
		sqlArray[1]="delete from pm_map_instance where device_id='"+device_id+"' and expressionid="+expression_id;
		PrepareSQL psql = new PrepareSQL(sqlArray[0]);
		psql.getSQL();
		psql = new PrepareSQL(sqlArray[1]);
		psql.getSQL();
		int[] resultCode=jt.batchUpdate(sqlArray);
		if(null!=resultCode)
		{
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#pmeeConfig(java.lang.String)
	 */
	public boolean pmeeConfig(List<String> sqlList)
	{
		String[] sqlArray=new String[sqlList.size()];
		sqlList.toArray(sqlArray);
		int[] resultCode=jt.batchUpdate(sqlArray);
		if(null!=resultCode)
		{
			return true;
		}
		return false;
	}


	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#isPopulationState(java.lang.String)
	 */
	public boolean isPopulationState(String expression_id)
	{
		String sql="select statall from pm_expression where expressionid="+expression_id;
		PrepareSQL psql = new PrepareSQL(sql);
		int statall=jt.queryForInt(psql.getSQL());
		//需要总体统计
		if(1==statall)
		{
			return true;
		}

		return  false;
	}



	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#getBasicInfoByExpressionId(java.lang.String)
	 */
	public String[] getBasicInfoByExpressionId(String expressionId)
	{
		log.debug("getBasicInfoByExpressionId:"+expressionId);
		if (expressionId == null || "".equals(expressionId))
			return null;
		String[] resultArr = null;
		String mysql = "select expression2id,class2,class3,class1 from pm_expression where expressionid=" + expressionId;
		PrepareSQL psql = new PrepareSQL(mysql);
		List<Map> recordList =jt.queryForList(psql.getSQL());
		if(null!=recordList&&!recordList.isEmpty())
		{
			resultArr = new String[4];
			//expression2id
			if(null!=recordList.get(0).get("expression2id"))
			{
				resultArr[0] = String.valueOf(recordList.get(0).get("expression2id"));
			}
			else
			{
				resultArr[0] = "";
			}

			//class2
			if(null!=recordList.get(0).get("class2"))
			{
				resultArr[1] = String.valueOf(recordList.get(0).get("class2"));
			}
			else
			{
				resultArr[1] = "";
			}

			//class3
			if(null!=recordList.get(0).get("class3"))
			{
				resultArr[2] = String.valueOf(recordList.get(0).get("class3"));
			}
			else
			{
				resultArr[2] = "";
			}

			//class1
			if(null!=recordList.get(0).get("class1"))
			{
				resultArr[3] = String.valueOf(recordList.get(0).get("class1"));
			}
			else
			{
				resultArr[3] = "";
			}
		}
		log.warn("resultArr[0]:"+resultArr[0]+"   resultArr[1]:"+resultArr[1]
		         +"       resultArr[2]:"+resultArr[2]+"       resultArr[3]:"+resultArr[3]);
		return resultArr;
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#isDeviceModelNotNeedDesc(java.lang.String)
	 */
	public boolean isDeviceModelNotNeedDesc(String device_id)
	{

		String mysql = "select a.device_model from gw_device_model a,tab_gw_device b where a.vendor_id=b.vendor_id and a.device_model_id=b.device_model_id and b.device_id='" + device_id + "'";
		PrepareSQL psql = new PrepareSQL(mysql);
		List<Map<String,String>> list =jt.queryForList(psql.getSQL());
		if (list.isEmpty())
		{
			// 这种情况说明该设备不是网管中的设备
			return false;
		}


		if(null!=list.get(0).get("device_model"))
		{
			String deviceModel = (String) list.get(0).get("device_model");
			if (deviceModel.equals("MA5200G") || deviceModel.equals("NE40"))
			{
				// 这两种型号的设备不需要用描述OID采描述信息,描述信息是写死的,slot(索引-1)
				return true;
			}
		}

		return false;

	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#getOIDList(java.lang.String)
	 */
	public List<String> getOIDList(String expression_id)
	{
		String mysql = "select distinct oid from pm_expression_context where nodetype in(2,3) and expressionid=" + expression_id;
		PrepareSQL psql = new PrepareSQL(mysql);
		List<Map<String,String>> list =jt.queryForList(psql.getSQL());
		List<String> oidList = new ArrayList<String> ();
		for(int i=0;i<list.size();i++)
		{
			oidList.add(list.get(i).get("oid"));
		}

		return oidList;

	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.I_configPmeeDao#getEquation(java.lang.String)
	 */
	public String getEquation(String expression_id)
	{
		String equation="";
		String sql="select equation from pm_expression where expressionid="+expression_id;
		PrepareSQL psql = new PrepareSQL(sql);
		Map<String,String> record =jt.queryForMap(psql.getSQL());
		if(null!=record)
		{
			equation=record.get("equation");
		}

		return equation;
	}
}
