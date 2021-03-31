
package com.linkage.module.liposs.performance.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.liposs.performance.bio.Flux_Map_Instance;
import com.linkage.module.liposs.performance.bio.PMEEGlobal;
import com.linkage.module.liposs.performance.bio.snmpGather.FluxPortInfo;

/**
 * 其他地市使用
 *
 * @author Administrator
 */
public class ConfigFluxDao_shortWarn extends ConfigFluxDao
{
	private static Logger log = LoggerFactory.getLogger(ConfigFluxDao_shortWarn.class);
	/*
		 * (non-Javadoc)
		 * @see com.linkage.liposs.dao.performance.ConfigFluxDao#editFluxPort(java.lang.String, java.lang.String[], int, int, com.linkage.liposs.bio.performance.Flux_Map_Instance)
		 */
		public boolean editFluxPort(String device_id, String[] port_info,int intodb,int gatherflag,
				Flux_Map_Instance fmi) {
			String sql="";
			List<String> list=new ArrayList<String>();
			int n=port_info.length;
			String[] tmp;
			for(int i=0;i<n;i++){
				tmp=port_info[i].split("\\|\\|\\|");
				if(tmp.length!=2){
					continue;
				}
				sql=" update flux_deviceportconfig set intodb="+intodb
				+",gatherflag="+gatherflag+" where device_id='"+device_id+"' and getway="+tmp[0]+" and port_info='"+tmp[1]+"' ";
				PrepareSQL psql = new PrepareSQL(sql);
				psql.getSQL();
				list.add(sql);
				sql=" update flux_interfacedeviceport set ifinoctetsbps_max="+fmi.getIfinoctetsbps_max()
				+",ifoutoctetsbps_max="+fmi.getIfoutoctetsbps_max()
				+",ifindiscardspps_max="+fmi.getIfindiscardspps_max()
				+",ifoutdiscardspps_max="+fmi.getIfoutdiscardspps_max()
				+",ifinerrorspps_max="+fmi.getIfinerrorspps_max()
				+",ifouterrorspps_max="+fmi.getIfouterrorspps_max()
				+",warningnum="+fmi.getWarningnum()
				+",warninglevel="+fmi.getWarninglevel()
				+",reinstatelevel="+fmi.getReinstatelevel()
				+",overper="+fmi.getOverper()
				+",overnum="+fmi.getOvernum()
				+",com_day="+fmi.getCom_day()
				+",overlevel="+fmi.getOverlevel()
				+",reinoverlevel="+fmi.getReinoverlevel()
				+",intbflag="+fmi.getIntbflag()
				+",ifinoctets="+fmi.getIfinoctets()
				+",inoperation="+fmi.getInoperation()
				+",inwarninglevel="+fmi.getInwarninglevel()
				+",inreinstatelevel="+fmi.getInreinstatelevel()
				+",outtbflag="+fmi.getOuttbflag()
				+",ifoutoctets="+fmi.getIfoutoctets()
				+",outwarninglevel="+fmi.getOutwarninglevel()
				+",outreinstatelevel="+fmi.getOutreinstatelevel()
				+",outoperation="+fmi.getOutoperation()
				+"	 where device_id='"+device_id+"' and getway="+tmp[0]+" and port_info = '"+tmp[1]+"' ";
				psql = new PrepareSQL(sql);
				list.add(psql.getSQL());
			}
			log.debug("编辑设备流量端口:"+list);
			n=list.size();
			String str[]=new String[n];
			for(int i=0;i<n;i++){
				str[i]=list.get(i);
			}
			try{
				jt.batchUpdate(str);
				return true;
			}catch(Exception e){
				return false;
			}
		}
	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.ConfigFluxDao#configDevicePortFlux(java.util.Map, com.linkage.liposs.bio.performance.snmpGather.FluxPortInfo, com.linkage.liposs.bio.performance.Flux_Map_Instance)
	 */
	public boolean configDevicePortFlux(Map<String,String> param,FluxPortInfo fpi,Flux_Map_Instance alarmParam)
	{
		boolean result=false;
		String[] sqlArray=new String[2];
		String device_id=param.get("device_id");
		String snmpversion=param.get("snmpversion");

		String model=param.get("model");
		String counternum=param.get("counternum");
		String polltime=param.get("polltime");
		String gatherflag=param.get("gatherflag");
		String ifconfigflag=param.get("ifconfigflag");
		String intodb=param.get("intodb");
		String getarg=param.get("getarg");
		String iftype=param.get("iftype");
		String ifmtu=param.get("ifmtu");
		String ifspeed=param.get("ifspeed");
		String ifhighspeed=param.get("ifhighspeed");
		String if_real_speed=param.get("if_real_speed");


		/**
		 * flux_deviceportconfig表
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("insert into flux_deviceportconfig(device_id,ifindex,ifdescr,");
		sb.append("ifname,ifnamedefined,ifportip,port_info,snmpversion,model,counternum,polltime,");
		sb.append("gatherflag,ifconfigflag,getway,intodb) values('");
		sb.append(device_id).append("','");
		sb.append(fpi.getIfindex()).append("','");
		sb.append(fpi.getIfdescr().replaceAll("'", "\\$\\$\\$")).append("','");
		sb.append(fpi.getIfname().replaceAll("'", "\\$\\$\\$")).append("','");
		sb.append(fpi.getIfnamedefined().replaceAll("'", "\\$\\$\\$")).append("','");
		sb.append(fpi.getIfportip().replaceAll("'", "\\$\\$\\$")).append("','");
		sb.append(fpi.getPort_info()).append("',");
		sb.append(snmpversion).append(",'");
		sb.append(model).append("',");
		sb.append(counternum).append(",");
		sb.append(polltime).append(",");
		sb.append(gatherflag).append(",");
		sb.append(ifconfigflag).append(",");
		sb.append(fpi.getGetway()).append(",");
		sb.append(intodb).append(")");
		sqlArray[0]=sb.toString().replaceAll(",''", ",'null'").replaceAll("'',", "'null',").replaceAll("\\$\\$\\$",
		"''");
		log(true,sqlArray[0]);
		PrepareSQL psql = new PrepareSQL(sqlArray[0]);
		psql.getSQL();
		sb=null;

		/**
		 * flux_interfacedeviceport表
		 */
		sb = new StringBuilder();
		sb.append("insert into flux_interfacedeviceport(device_id,ifindex,ifdescr,");
		sb.append("ifname,ifnamedefined,ifportip,port_info,getway,getarg,ifportwarn,ifexam,");
		sb.append("portwarnlevel,iftype,ifmtu,ifspeed,ifhighspeed,if_real_speed,");
		sb.append("ifinoctetsbps_max,ifoutoctetsbps_max,ifindiscardspps_max,ifoutdiscardspps_max,");
		sb.append("ifinerrorspps_max,ifouterrorspps_max,warningnum,warninglevel,reinstatelevel,");
		sb.append("overper,overnum,overlevel,reinoverlevel,com_day,ifinoctets,inwarninglevel,");
		sb.append("inreinstatelevel,inoperation,intbflag,ifoutoctets,outwarninglevel,outreinstatelevel,");
		sb.append("outoperation,outtbflag) values('");
		sb.append(device_id).append("','");
		sb.append(fpi.getIfindex()).append("','");
		sb.append(fpi.getIfdescr().replaceAll("'", "\\$\\$\\$")).append("','");
		sb.append(fpi.getIfname().replaceAll("'", "\\$\\$\\$")).append("','");
		sb.append(fpi.getIfnamedefined().replaceAll("'", "\\$\\$\\$")).append("','");
		sb.append(fpi.getIfportip().replaceAll("'", "\\$\\$\\$")).append("','");
		sb.append(fpi.getPort_info()).append("',");
		sb.append(fpi.getGetway()).append(",'");
		sb.append(getarg).append("',0,0,0,");
		sb.append(iftype).append(",");
		sb.append(ifmtu).append(",");
		sb.append(ifspeed).append(",");
		sb.append(ifhighspeed).append(",");
		sb.append(if_real_speed).append(",");
		sb.append(alarmParam.getIfinoctetsbps_max()).append(",");
		sb.append(alarmParam.getIfoutoctetsbps_max()).append(",");
		sb.append(alarmParam.getIfindiscardspps_max()).append(",");
		sb.append(alarmParam.getIfoutdiscardspps_max()).append(",");
		sb.append(alarmParam.getIfinerrorspps_max()).append(",");
		sb.append(alarmParam.getIfouterrorspps_max()).append(",");
		sb.append(alarmParam.getWarningnum()).append(",");
		sb.append(alarmParam.getWarninglevel()).append(",");
		sb.append(alarmParam.getReinstatelevel()).append(",");
		sb.append(alarmParam.getOverper()).append(",");
		sb.append(alarmParam.getOvernum()).append(",");
		sb.append(alarmParam.getOverlevel()).append(",");
		sb.append(alarmParam.getReinoverlevel()).append(",");
		sb.append(alarmParam.getCom_day()).append(",");
		sb.append(alarmParam.getIfinoctets()).append(",");
		sb.append(alarmParam.getInwarninglevel()).append(",");
		sb.append(alarmParam.getInreinstatelevel()).append(",");
		sb.append(alarmParam.getInoperation()).append(",");
		sb.append(alarmParam.getIntbflag()).append(",");
		sb.append(alarmParam.getIfoutoctets()).append(",");
		sb.append(alarmParam.getOutwarninglevel()).append(",");
		sb.append(alarmParam.getOutreinstatelevel()).append(",");
		sb.append(alarmParam.getOutoperation()).append(",");
		sb.append(alarmParam.getOuttbflag()).append(")");
		sqlArray[1]=sb.toString().replaceAll(",''", ",'null'").replaceAll("'',", "'null',").replaceAll("\\$\\$\\$",
		"''");
		log(true,sqlArray[1]);
		psql = new PrepareSQL(sqlArray[1]);
		psql.getSQL();
		int[] resultCode=null;
		try
		{
			resultCode=jt.batchUpdate(sqlArray);
			log(true,"－－－－－－－－－－－－－－－－－－－执行成功－－－－－－－－－－－－－－－－－");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log(true,"－－－－－－－－－－－－－－－－－－－执行失败－－－－－－－－－－－－－－－－－");
		}

		if(null!=resultCode)
		{
			result=true;
		}

		return result;

	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.liposs.dao.performance.ConfigFluxDao#loadDeviceAlarm(java.lang.String)
	 */
	public Map<String,Flux_Map_Instance> loadDeviceAlarm(String device_id)
	{
		Map<String,Flux_Map_Instance> map = new HashMap<String,Flux_Map_Instance>();
		String sql="select ifinoctetsbps_max, ifoutoctetsbps_max, ifindiscardspps_max, ifoutdiscardspps_max, ifinerrorspps_max, " +
				"ifouterrorspps_max, warningnum, warninglevel, reinstatelevel, overper, overnum, overlevel, reinoverlevel, " +
				"com_day, ifinoctets, inwarninglevel, inreinstatelevel, inoperation, intbflag, ifoutoctets, outwarninglevel, " +
				"outreinstatelevel, outoperation, outtbflag, getway, port_info " +
			"from flux_interfacedeviceport where device_id='"+device_id+"'";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list=jt.queryForList(psql.getSQL());
		for(Map record:list)
		{
			Flux_Map_Instance alarmInstance = new Flux_Map_Instance();
			alarmInstance.setIfinoctetsbps_max(Float.parseFloat(String.valueOf(record.get("ifinoctetsbps_max"))));
			alarmInstance.setIfoutoctetsbps_max(Float.parseFloat(String.valueOf(record.get("ifoutoctetsbps_max"))));
			alarmInstance.setIfindiscardspps_max(Float.parseFloat(String.valueOf(record.get("ifindiscardspps_max"))));
			alarmInstance.setIfoutdiscardspps_max(Float.parseFloat(String.valueOf(record.get("ifoutdiscardspps_max"))));
			alarmInstance.setIfinerrorspps_max(Float.parseFloat(String.valueOf(record.get("ifinerrorspps_max"))));
			alarmInstance.setIfouterrorspps_max(Float.parseFloat(String.valueOf(record.get("ifouterrorspps_max"))));
			alarmInstance.setWarningnum(Integer.parseInt(String.valueOf(record.get("warningnum"))));
			alarmInstance.setWarninglevel(Integer.parseInt(String.valueOf(record.get("warninglevel"))));
			alarmInstance.setReinstatelevel(Integer.parseInt(String.valueOf(record.get("reinstatelevel"))));
			alarmInstance.setOverper(Float.parseFloat(String.valueOf(record.get("overper"))));
			alarmInstance.setOvernum(Integer.parseInt(String.valueOf(record.get("overnum"))));
			alarmInstance.setOverlevel(Integer.parseInt(String.valueOf(record.get("overlevel"))));
			alarmInstance.setReinoverlevel(Integer.parseInt(String.valueOf(record.get("reinoverlevel"))));
			alarmInstance.setCom_day(Integer.parseInt(String.valueOf(record.get("com_day"))));
			alarmInstance.setIfinoctets(Float.parseFloat(String.valueOf(record.get("ifinoctets"))));
			alarmInstance.setInwarninglevel(Integer.parseInt(String.valueOf(record.get("inwarninglevel"))));
			alarmInstance.setInreinstatelevel(Integer.parseInt(String.valueOf(record.get("inreinstatelevel"))));
			alarmInstance.setInoperation(Integer.parseInt(String.valueOf(record.get("inoperation"))));
			alarmInstance.setIntbflag(Integer.parseInt(String.valueOf(record.get("intbflag"))));
			alarmInstance.setIfoutoctets(Float.parseFloat(String.valueOf(record.get("ifoutoctets"))));
			alarmInstance.setOutwarninglevel(Integer.parseInt(String.valueOf(record.get("outwarninglevel"))));
			alarmInstance.setOutreinstatelevel(Integer.parseInt(String.valueOf(record.get("outreinstatelevel"))));
			alarmInstance.setOutoperation(Integer.parseInt(String.valueOf(record.get("outoperation"))));
			alarmInstance.setOuttbflag(Integer.parseInt(String.valueOf(record.get("outtbflag"))));

            String key = String.valueOf(record.get("device_id")) + "||"
					+ String.valueOf(record.get("getway")) + "||"
					+ String.valueOf(record.get("port_info"));
			map.put(key, alarmInstance);
		}
		return map;
	}

	/**
	 * 查询已配置端口列表
	 *
	 * @param device_id:设备ID
	 * @param long_warn：是否是多告警字段：注：江苏true，其他false
	 * @return List
	 *         <ul>
	 *         <li><font color='blue'>port_info:</font>端口信息</li>
	 *         <li><font color='blue'>ifindex:</font>端口索引</li>
	 *         <li><font color='blue'>ifdescr:</font>端口描述</li>
	 *         <li><font color='blue'>ifname:</font>端口名字</li>
	 *         <li><font color='blue'>ifnamedefined:</font>端口别名</li>
	 *         <li><font color='blue'>ifportip:</font>端口IP</li>
	 *         <li><font color='blue'>getway:</font>采集方式</li>
	 *         <li><font color='blue'>getwayStr:</font>索引</li>
	 *         <li><font color='blue'>confignum:</font>已配阈值数量<font
	 *         color='red'>(注：江苏使用字段)</font></li>
	 *         <li><font color='blue'>gatherflag</font>采集标志</li>
	 *         <li><font color='blue'>ifconfigflag</font>是否已经配置</li>
	 *         <li><font color='blue'>polltime</font>采集时间间隔</li>
	 *         <li><font color='blue'>intodb</font>是否入库</li>
	 *         <li><font color='blue'>snmpversion</font>SNMP版本</li>
	 *         </ul>
	 */
	public List<Map<String, String>> getConfigPortList(String device_id)
	{
		String sql = "select a.port_info,a.ifindex,a.ifdescr,a.ifname,a.ifnamedefined,a.ifportip,"
				+ " a.getway,b.gatherflag,b.polltime,b.intodb,b.snmpversion,b.ifconfigflag"
				+ " from flux_interfacedeviceport a, flux_deviceportconfig b "
				+ " where a.device_id=b.device_id and a.getway=b.getway and a.port_info=b.port_info"
				+ " and a.device_id='" + device_id + "' order by a.ifindex";
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.query(psql.getSQL(), new RowMapper()
		{

			String tmp = "";

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map map = new HashMap();
				map.put("port_info", rs.getString("port_info").replaceAll("\"", "%22").replaceAll("'", "%27"));// 端口信息
				map.put("ifindex", changeString(rs.getString("ifindex")));// 端口索引
				map.put("ifdescr", changeString(rs.getString("ifdescr")));// 端口描述
				map.put("ifname", changeString(rs.getString("ifname")));// 端口名字
				map.put("ifnamedefined", changeString(rs.getString("ifnamedefined")));// 端口别名
				map.put("ifportip", changeString(rs.getString("ifportip")));// 端口IP
				map.put("getway", rs.getInt("getway"));// 采集方式
				tmp = PMEEGlobal.GetWayMap.get(rs.getInt("getway"));
				if (tmp == null)
				{
					map.put("getwayStr", "索引");//
				}
				else
				{
					map.put("getwayStr", tmp);
				}
				// map.put("confignum",rs.getInt("confignum"));//已配阈值数量
				map.put("gatherflag", PMEEGlobal.GatherFlgMap
						.get(rs.getInt("gatherflag")));
				if (rs.getInt("gatherflag") != 1 || rs.getInt("ifconfigflag") != 1)
				{
					map.put("ifconfigflag", "未配置");
				}
				else
				{
					map.put("ifconfigflag", "已配置");
				}
				map.put("polltime", rs.getInt("polltime"));// 采集时间间隔
				map.put("intodb", PMEEGlobal.IntoDBMap.get(rs.getInt("intodb")));// 是否入库
				map.put("snmpversion", changeString(rs.getString("snmpversion")));// SNMP版本
				return map;
			}
		});
	}
}
