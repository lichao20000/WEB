/**
 * 
 */

package com.linkage.module.gwms.diagnostics.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.gw.GwTr069DAO;
import com.linkage.module.gwms.dao.gw.LanHostDAO;
import com.linkage.module.gwms.dao.gw.LocalServParamDAO;
import com.linkage.module.gwms.dao.gw.VoiceServiceProfileDAO;
import com.linkage.module.gwms.dao.gw.VoiceServiceProfileLineDAO;
import com.linkage.module.gwms.dao.gw.WanConnDAO;
import com.linkage.module.gwms.dao.gw.WanConnSessDAO;
import com.linkage.module.gwms.dao.gw.WireInfoDAO;
import com.linkage.module.gwms.dao.gw.WlanDAO;
import com.linkage.module.gwms.dao.tabquery.HgwCustDAO;
import com.linkage.module.gwms.dao.tabquery.HgwServUserDAO;
import com.linkage.module.gwms.diagnostics.dao.interf.I_DeviceInfoDAO;
import com.linkage.module.gwms.diagnostics.obj.PONInfoOBJ;
import com.linkage.module.gwms.obj.gw.DeviceWireInfoObj;
import com.linkage.module.gwms.obj.gw.GwTr069OBJ;
import com.linkage.module.gwms.obj.gw.LanHostObj;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileH248OBJ;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileLineObj;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileObj;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.util.corba.ACSCorba;

import dao.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-6-25
 * @category com.linkage.module.gwms.diagnostics.dao
 */
public class DeviceInfoDAO implements I_DeviceInfoDAO
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(DeviceInfoDAO.class);
	/**
	 * JdbcTemplate
	 */
	private JdbcTemplateExtend jt;
	// 使用到的DAO
	WireInfoDAO wireInfoDao;
	WanConnDAO wanConnDao;
	WanConnSessDAO wanConnSessDao;
	LanHostDAO lanHostDao;
	HgwCustDAO hgwCustDao;
	HgwServUserDAO hgwServUserDao;
	LocalServParamDAO localServParamDAO;
	VoiceServiceProfileDAO voiceDAO;
	VoiceServiceProfileLineDAO voiceeLineDAO;
	WlanDAO wlanDAO;
	GwTr069DAO tr069DAO;
	public static String TELECOM_CUC = "CUC"; //联通

	/**
	 * 注入
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplateExtend(dao);
	}

	/**
	 * @return the wanConnDao
	 */
	public WanConnDAO getWanConnDao()
	{
		return wanConnDao;
	}

	/**
	 * @param wanConnDao
	 *            the wanConnDao to set
	 */
	public void setWanConnDao(WanConnDAO wanConnDao)
	{
		this.wanConnDao = wanConnDao;
	}

	/**
	 * @return the wanConnSessDao
	 */
	public WanConnSessDAO getWanConnSessDao()
	{
		return wanConnSessDao;
	}

	/**
	 * @param wanConnSessDao
	 *            the wanConnSessDao to set
	 */
	public void setWanConnSessDao(WanConnSessDAO wanConnSessDao)
	{
		this.wanConnSessDao = wanConnSessDao;
	}

	/**
	 * @return the hgwCustDao
	 */
	public HgwCustDAO getHgwCustDao()
	{
		return hgwCustDao;
	}

	/**
	 * @param hgwCustDao
	 *            the hgwCustDao to set
	 */
	public void setHgwCustDao(HgwCustDAO hgwCustDao)
	{
		this.hgwCustDao = hgwCustDao;
	}

	/**
	 * @return the hgwServUserDao
	 */
	public HgwServUserDAO getHgwServUserDao()
	{
		return hgwServUserDao;
	}

	/**
	 * @param hgwServUserDao
	 *            the hgwServUserDao to set
	 */
	public void setHgwServUserDao(HgwServUserDAO hgwServUserDao)
	{
		this.hgwServUserDao = hgwServUserDao;
	}

	/**
	 * @return the lanHostDao
	 */
	public LanHostDAO getLanHostDao()
	{
		return lanHostDao;
	}

	/**
	 * @param lanHostDao
	 *            the lanHostDao to set
	 */
	public void setLanHostDao(LanHostDAO lanHostDao)
	{
		this.lanHostDao = lanHostDao;
	}

	/**
	 * @return the wireInfoDao
	 */
	public WireInfoDAO getWireInfoDao()
	{
		return wireInfoDao;
	}

	/**
	 * @param wireInfoDao
	 *            the wireInfoDao to set
	 */
	public void setWireInfoDao(WireInfoDAO wireInfoDao)
	{
		this.wireInfoDao = wireInfoDao;
	}

	/**
	 * @return the localServParamDAO
	 */
	public LocalServParamDAO getLocalServParamDAO()
	{
		return localServParamDAO;
	}

	/**
	 * @param localServParamDAO
	 *            the localServParamDAO to set
	 */
	public void setLocalServParamDAO(LocalServParamDAO localServParamDAO)
	{
		this.localServParamDAO = localServParamDAO;
	}

	/**
	 * @return the wlanDAO
	 */
	public WlanDAO getWlanDAO()
	{
		return wlanDAO;
	}

	/**
	 * @param wlanDAO
	 *            the wlanDAO to set
	 */
	public void setWlanDAO(WlanDAO wlanDAO)
	{
		this.wlanDAO = wlanDAO;
	}

	/**
	 * @return the voiceDAO
	 */
	public VoiceServiceProfileDAO getVoiceDAO()
	{
		return voiceDAO;
	}

	/**
	 * @param voiceDAO
	 *            the voiceDAO to set
	 */
	public void setVoiceDAO(VoiceServiceProfileDAO voiceDAO)
	{
		this.voiceDAO = voiceDAO;
	}

	/**
	 * @return the voiceeLineDAO
	 */
	public VoiceServiceProfileLineDAO getVoiceeLineDAO()
	{
		return voiceeLineDAO;
	}

	/**
	 * @param voiceeLineDAO
	 *            the voiceeLineDAO to set
	 */
	public void setVoiceeLineDAO(VoiceServiceProfileLineDAO voiceeLineDAO)
	{
		this.voiceeLineDAO = voiceeLineDAO;
	}

	/**
	 * @return the tr069DAO
	 */
	public GwTr069DAO getTr069DAO()
	{
		return tr069DAO;
	}

	/**
	 * @param tr069DAO
	 *            the tr069DAO to set
	 */
	public void setTr069DAO(GwTr069DAO tr069DAO)
	{
		this.tr069DAO = tr069DAO;
	}

	/**
	 * @param deviceId
	 * @return
	 */
	public List getDeviceInfo(String deviceId)
	{
		logger.debug("getDeviceInfo({})", deviceId);
		List reList=new ArrayList();
		StringBuffer sql = new StringBuffer();
		if(Global.HBLT.equals(Global.instAreaShortName))
		{
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				sql.append("select a.device_id,a.oui,a.device_serialnumber,a.loopback_ip,");
				sql.append("a.vendor_id,a.device_model_id,a.devicetype_id,a.cpe_mac,e.gigabit_port ");
				sql.append("from tab_gw_device a ");
				sql.append("left join tab_device_version_attribute e on a.devicetype_id=e.devicetype_id ");
				sql.append("where a.device_id='"+deviceId+"'");
				PrepareSQL psql = new PrepareSQL(sql.toString());
				List list=jt.queryForList(psql.getSQL());
				if(list!=null && !list.isEmpty()){
					Map<String,String> vn=getVendorAdd();
					List<String> vendor_ids=new ArrayList<String>();
					for(String key:vn.keySet()){
						vendor_ids.add(key);
					}
					
					Map<String,String> dm=getDeviceModel();
					Map<String,List<String>> dti=getDeviceTypeInfo();
					for(int i=0;i<list.size();i++){
						Map m=(Map)list.get(i);
						String vendor_id=dm.get(StringUtil.getStringValue(m,"vendor_id"));
						String device_model=dm.get(StringUtil.getStringValue(m,"device_model_id"));
						List<String> ver=dti.get(StringUtil.getStringValue(m,"devicetype_id"));
						if((StringUtil.IsEmpty(vendor_id) || !vendor_ids.contains(vendor_id)) 
								|| StringUtil.IsEmpty(device_model) 
								|| (ver==null || ver.isEmpty())){
							continue;
						}
						
						m.put("vendor_add", vn.get(vendor_id));
						m.put("device_model", device_model);
						m.put("hardwareversion", ver.get(0));
						m.put("softwareversion", ver.get(1));
						m.put("ip_model_type", ver.get(2));
						reList.add(m);
					}
					list=null;
					
				}
			}else{
				sql.append("select a.device_id,a.oui,a.device_serialnumber,a.loopback_ip,");
				sql.append(" b.vendor_add,c.device_model,d.hardwareversion,d.ip_model_type ,");
				sql.append(" d.softwareversion,a.cpe_mac,e.gigabit_port from tab_gw_device a left join tab_vendor b on a.vendor_id=b.vendor_id  ");
				sql.append(" left join gw_device_model c on  a.device_model_id=c.device_model_id left join tab_devicetype_info d on a.devicetype_id=d.devicetype_id " +
						"left join tab_device_version_attribute e on a.devicetype_id=e.devicetype_id where a.device_id='");
				sql.append(deviceId+"'");
			}
		}
		else if(Global.NXDX.equals(Global.instAreaShortName))
		{
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				sql.append("select a.device_id,a.oui,a.device_serialnumber,a.loopback_ip,");
				sql.append("a.vendor_id,a.device_model_id,a.devicetype_id,a.cpe_mac,f.mac ");
				sql.append("from tab_gw_device a ");
				sql.append("left join tab_device_e8c_remould f on a.device_serialnumber=f.device_serialnumber ");
				sql.append("where a.device_id='"+deviceId+"'");
				PrepareSQL psql = new PrepareSQL(sql.toString());
				List list=jt.queryForList(psql.getSQL());
				if(list!=null && !list.isEmpty()){
					Map<String,String> vn=getVendorAdd();
					List<String> vendor_ids=new ArrayList<String>();
					for(String key:vn.keySet()){
						vendor_ids.add(key);
					}
					
					Map<String,String> dm=getDeviceModel();
					Map<String,List<String>> dti=getDeviceTypeInfo();
					for(int i=0;i<list.size();i++){
						Map m=(Map)list.get(i);
						String vendor_id=dm.get(StringUtil.getStringValue(m,"vendor_id"));
						String device_model=dm.get(StringUtil.getStringValue(m,"device_model_id"));
						List<String> ver=dti.get(StringUtil.getStringValue(m,"devicetype_id"));
						if((StringUtil.IsEmpty(vendor_id) || !vendor_ids.contains(vendor_id)) 
								|| StringUtil.IsEmpty(device_model) 
								|| (ver==null || ver.isEmpty())){
							continue;
						}
						
						m.put("vendor_add", vn.get(vendor_id));
						m.put("device_model", device_model);
						m.put("hardwareversion", ver.get(0));
						m.put("softwareversion", ver.get(1));
						m.put("ip_model_type", ver.get(2));
						reList.add(m);
					}
					list=null;
					
				}
			}else{
				sql.append("select a.device_id,a.oui,a.device_serialnumber,a.loopback_ip,");
				sql.append("b.vendor_add,c.device_model,d.hardwareversion,d.ip_model_type ,");
				sql.append(" d.softwareversion,a.cpe_mac,f.mac ");
				sql.append("from tab_gw_device a left join tab_device_e8c_remould f on a.device_serialnumber=f.device_serialnumber,tab_vendor b ");
				sql.append(" ,gw_device_model c,tab_devicetype_info d where a.device_id='");
				sql.append(deviceId);
				sql.append("' and a.vendor_id=b.vendor_id and a.device_model_id= c.device_model_id and a.devicetype_id=d.devicetype_id");
			}
		}
		else
		{
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				sql.append("select a.device_id,a.oui,a.device_serialnumber,a.loopback_ip,");
				sql.append("b.vendor_add,a.device_model_id,a.devicetype_id,a.cpe_mac ");
				sql.append("from tab_gw_device a,tab_vendor b ");
				sql.append("where a.vendor_id=b.vendor_id and a.device_id='"+deviceId+"' ");
				PrepareSQL psql = new PrepareSQL(sql.toString());
				List list=jt.queryForList(psql.getSQL());
				
				if(list!=null && !list.isEmpty()){
					Map<String,String> dm=getDeviceModel();
					Map<String,List<String>> dti=getDeviceTypeInfo();
					for(int i=0;i<list.size();i++){
						Map m=(Map)list.get(i);
						
						String device_model=dm.get(StringUtil.getStringValue(m,"device_model_id"));
						List<String> ver=dti.get(StringUtil.getStringValue(m,"devicetype_id"));
						if(StringUtil.IsEmpty(device_model) || ver==null || ver.isEmpty()){
							continue;
						}
						
						m.put("device_model", device_model);
						m.put("hardwareversion", ver.get(0));
						m.put("softwareversion", ver.get(1));
						m.put("ip_model_type", ver.get(2));
						reList.add(m);
					}
					list=null;
				}
			}else{
				sql.append("select a.device_id,a.oui,a.device_serialnumber,a.loopback_ip,b.vendor_add,");
				sql.append("c.device_model,d.hardwareversion,d.ip_model_type,d.softwareversion,a.cpe_mac ");
				sql.append("from tab_gw_device a,tab_vendor b,gw_device_model c,tab_devicetype_info d ");
				sql.append("where a.device_id='"+deviceId+"' and a.vendor_id=b.vendor_id ");
				sql.append("and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id");
			}
		}
		
		if(DBUtil.GetDB()!=Global.DB_MYSQL){
			PrepareSQL psql = new PrepareSQL(sql.toString());
			reList=jt.queryForList(psql.getSQL());
		}
		
		return reList;
	}

	/**
	 * @param deviceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getAbilityInfo(String deviceId) {
		logger.debug("getAbilityInfo({})", deviceId);
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select verdor_id,verdor,devicemodel_id,devicemodel,");
			psql.append("devicetype,maxspeed,lannum,wifinum,flannum,glannum,");
			psql.append("voipnum,speedtest,sdfrequency ");
		}else{
			psql.append("select * ");
		}
		psql.append("from tab_speed_lannub a,tab_gw_device b ");
		psql.append("where a.verdor_id=b.vendor_id and a.devicemodel_id=b.device_model_id ");
		psql.append("and b.device_id ='" + deviceId + "'");
		
		try{
			return (Map<String, String>)jt.queryForObject(psql.getSQL(), new RowMapper(){
				@Override
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException
				{
					Map<String, String> map = new HashMap<String, String>();
					map.put("verdor_id", rs.getString("verdor_id"));
					map.put("verdor", rs.getString("verdor"));
					map.put("devicemodel_id", rs.getString("devicemodel_id"));
					map.put("devicemodel", rs.getString("devicemodel"));
					map.put("devicetype", rs.getString("devicetype"));
					map.put("maxspeed", rs.getString("maxspeed"));
					map.put("lannum", rs.getString("lannum"));
					map.put("wifinum", rs.getString("wifinum"));
					map.put("flannum", rs.getString("flannum"));
					map.put("glannum", rs.getString("glannum"));
					map.put("voipnum", rs.getString("voipnum"));
					map.put("speedtest", rs.getString("speedtest"));
					map.put("sdfrequency", rs.getString("sdfrequency"));
					int speedtest = rs.getInt("speedtest");
					int sdfrequency = rs.getInt("sdfrequency");
					map.put("speedtestdesc", speedtest == 1 ? "支持" : "不支持");
					map.put("sdfrequencydesc", sdfrequency == 1 ? "单频" : sdfrequency == 2 ? "双频" : "不支持");
					
					return map;
				}

			});
		}
		catch (EmptyResultDataAccessException e) {
		     return null;
		}
	}

	/**
	 * 查询IP
	 * 
	 * @param deviceId
	 * @return
	 */
	public String getDeviceIp(String deviceId)
	{
		logger.debug("getDeviceIp({})", deviceId);
		String loopback_ip = "";
		StringBuffer sql = new StringBuffer();
		sql.append("select a.loopback_ip from tab_gw_device a ");
		sql.append(" where a.device_id='"+deviceId+"' ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List list = jt.queryForList(psql.getSQL());
		if (list.size() > 0)
		{
			loopback_ip = String.valueOf(((Map) list.get(0)).get("loopback_ip"))
					.toString();
		}
		return loopback_ip;
	}

	/**
	 * 根据设备oui，设备序列号查询用户 modify by zhangchy 2012-01-05 将原SQL改成左连接
	 * 调整原因是：如果没有宽带业务的话，原SQL查询不出用户信息
	 * 
	 * @param oui
	 * @param device_serialnumber
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return
	 */
	public List getDeviceHUserInfo(String device_id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select a.user_id,a.city_id,a.username,a.linkman,a.linkaddress,");
		psql.append("a.credno,a.linkphone,a.device_port,a.macaddress,b.username as kdname");
		// sql.append(" from tab_hgwcustomer a, hgwcust_serv_info b ");
		// sql.append(" where a.user_id=b.user_id and a.user_state='1' and  a.device_id=? and b.serv_type_id=10");
		psql.append(" from tab_hgwcustomer a left join hgwcust_serv_info b on a.user_id=b.user_id and b.serv_type_id=10 ");
		psql.append(" where a.user_state='1' and a.device_id=?  ");
		// PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.setString(1, device_id);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * <p>
	 * [根据用户ID，查询用户开户的VOIP工单信息]
	 * </p>
	 * 
	 * @param userId
	 * @return
	 */
	public List getBssVoipSheet(String userId)
	{
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select protocol ");
		}else{
			psql.append("select * ");
		}
		psql.append("from tab_voip_serv_param where user_id = " + userId);
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 查询商务领航用户信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public List getDeviceEUserInfo(String deviceId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.customer_id,b.username,b.user_id,b.device_port,");
		sql.append(" a.customer_name,a.customer_address,a.linkman,a.linkphone, ");
		sql.append(" a.city_id from tab_customerinfo a,tab_egwcustomer b where ");
		sql.append(" a.customer_id=b.customer_id and b.user_state in ('1','2') and b.device_id = '");
		sql.append(deviceId);
		sql.append("' ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 查询cpe访问ACS的用户名和密码,以及ACS连CPE的用户名和密码
	 * 
	 * @param deviceId
	 * @return
	 */
	public List getDeviceTr069Username(String deviceId)
	{
		logger.debug("getDeviceTr069Username(deviceId:{})", deviceId);
		PrepareSQL psql = new PrepareSQL();
		psql.append("select cpe_username,cpe_passwd,acs_username,acs_passwd ");
		psql.append("from tab_gw_device where device_id='"+ deviceId + "'");
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 查询设备wlan关联设备情况
	 * 
	 * @param deviceId
	 * @param lanId
	 * @param lanWlanId
	 * @return
	 */
	public List getGwWlanAsso(String deviceId, String lanId, String lanWlanId)
	{
		logger.debug("getGwWlanAsso(deviceId:{},lanWlanId:{})", deviceId, lanWlanId);
		StringBuffer sql = new StringBuffer();
		sql.append("select device_id,lan_id,lan_wlan_id,asso_id,ip_address,mac_address,auth_state ");
		sql.append("from gw_wlan_asso ");
		sql.append("where device_id='"+deviceId);
		sql.append("' and lan_id="+lanId);
		sql.append(" and lan_wlan_id="+lanWlanId);
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 根据特定PVC获取相关连接
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return WanConnObj
	 */
	public WanConnObj queryDevWanConn(Map map)
	{
		logger.debug("queryDevWanConn(map:{})", map);
		return wanConnDao.queryDevWanConn(map);
	}

	/**
	 * 根据特定PVC获取相关连接
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return WanConnObj
	 */
	public WanConnObj queryDevWanConn(String deviceId, String vpiid, String vciid)
	{
		logger.debug("queryDevWanConn(deviceId:{}, vpi/vci:{})", deviceId, vpiid + "/"
				+ vciid);
		WanConnObj wanConnObj = wanConnDao.queryDevWanConn(deviceId, vpiid, vciid);
		return wanConnObj;
	}

	/**
	 * 根据特定PVC获取相关连接
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return WanConnObj
	 */
	public WanConnObj queryDevWanConnByVlan(String deviceId, String vlanId)
	{
		logger.debug("queryDevWanConnByVlan(deviceId:{}, vlanId:{})", deviceId, vlanId);
		WanConnObj wanConnObj = wanConnDao.queryDevWanConn(deviceId, vlanId);
		return wanConnObj;
	}

	/**
	 * 取出特定(设备)WanConnection下面所有的Sesstion连接属性
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return WanConnSessObj[]
	 */
	public WanConnSessObj[] queryDevWanConnSession(WanConnObj wanConnObj)
	{
		logger.debug("queryDevWanConnSession(wanConnObj:{})", wanConnObj);
		WanConnSessObj[] wanConnSessObj = wanConnSessDao
				.queryDevWanConnSession(wanConnObj);
		return wanConnSessObj;
	}

	/**
	 * 获取指定device_id的VOIP节点，不包含线路信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileObj[] getVoipProf(Map map)
	{
		logger.debug("getVoipProf(deviceId:{})",
				StringUtil.getStringValue(map.get("device_id")));
		return voiceDAO.getVoipProf(map);
	}

	/**
	 * 获取指定device_id的VOIP节点，不包含线路信息 H248
	 * 
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileH248OBJ[] getVoipProfH(Map map)
	{
		logger.debug("getVoipProfH(deviceId:{})",
				StringUtil.getStringValue(map.get("device_id")));
		return voiceDAO.getVoipProfH(map);
	}

	/**
	 * 获取指定GwVoipProfOBJ的VOIP所有Line节点
	 * 
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileLineObj[] getVoipProfLine(VoiceServiceProfileObj voipProf)
	{
		logger.debug("getVoipProfLine(voipProf:{})", voipProf);
		return voiceeLineDAO.getVoipProfLine(voipProf);
	}

	/**
	 * 获取指定GwVoipProfOBJ的VOIP所有Line节点 H248
	 * 
	 * @param deviceId
	 * @return
	 */
	public VoiceServiceProfileLineObj[] getVoipProfLineH(
			VoiceServiceProfileH248OBJ voipProf)
	{
		logger.debug("getVoipProfLine(voipProf:{})", voipProf);
		return voiceeLineDAO.getVoipProfLineForH(voipProf);
	}

	/**
	 * 获取业务用户ID,根据设备ID
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return String
	 */
	public String getUserId(String deviceId)
	{
		String userId = hgwCustDao.getUserId(deviceId);
		return userId;
	}

	/**
	 * 获取业务对应的pvc值，从配置表中取(考虑到没有办法确定设备用哪个PVC上网)
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return String[]
	 */
	public String[] getServPvc(String servType)
	{
		return localServParamDAO.getServPvc(servType);
	}

	/**
	 * 获取业务对应的pvc值，从配置表中取(考虑到没有办法确定设备用哪个PVC上网)
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return String[]
	 */
	public String[] getServPvc(String servType[])
	{
		return localServParamDAO.getServPvc(servType);
	}

	/**
	 * 获取业务用户的PVC
	 * 
	 * @param userId
	 * @param servTypeId
	 * @author qxq(4174)
	 * @date 2009-7-14
	 * @return
	 */
	public HgwServUserObj getUserInfo(String userId, String servTypeId)
	{
		HgwServUserObj servUserObj = hgwServUserDao.getUserInfo(userId, servTypeId);
		return servUserObj;
	}

	/**
	 * 获取线路信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public DeviceWireInfoObj[] queryDevWireInfo(String deviceId)
	{
		logger.debug("queryDevWireInfo(deviceId:{})", deviceId);
		return wireInfoDao.queryDevWireInfo(deviceId);
	}

	/**
	 * 获取LAN侧实例
	 * 
	 * @param deviceId
	 * @return
	 */
	public LanHostObj[] queryLanHost(String deviceId)
	{
		logger.debug("queryLanHost(deviceId:{})", deviceId);
		return lanHostDao.queryLanHost(deviceId, 0);
	}

	/**
	 * 获取LAN侧信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public List queryLanEth(String deviceId)
	{
		logger.debug("queryLanEth(deviceId:{})", deviceId);
		StringBuffer sql = new StringBuffer();
		sql.append("select device_id,lan_id,lan_eth_id,enable,status,");
		sql.append("mac_address,gather_time,max_bit_rate,dupl_mode,byte_sent,");
		sql.append("byte_rece,pack_sent,pack_rece,error_sent,drop_sent,");
		sql.append("error_rece,drop_rece from "+ Global.getTabName(deviceId, "gw_lan_eth"));
		sql.append(" where device_id='"+deviceId+"'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		return jt.queryForList(psql.getSQL());
	}

	/**
	 * 获取Wlan 信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public List getWalnData(String deviceId)
	{
		logger.debug("getWalnData(deviceId:{})", deviceId);
		return wlanDAO.getData(deviceId);
	}

	/**
	 * 查询地址池
	 * 
	 * @param deviceId
	 * @return
	 */
	public List getGwLanHostconf(String deviceId)
	{
		logger.debug("getGwLanHostconf(deviceId:{})", deviceId);
		PrepareSQL sql = new PrepareSQL();
		sql.append("select device_id,gather_time,lan_id,server_conf_enab,");
		sql.append("server_enab,dhcp_relay,max_addr,min_addr,rese_addr,lease_time,");
		sql.append("allow_mac,stb_max_addr,stb_min_addr,phone_max_addr,phone_min_addr,");
		sql.append("came_max_addr,came_min_addr,pc_max_addr,pc_min_addr from "
				+ Global.getTabName(deviceId, "gw_lan_hostconf"));
		sql.append(" where device_id='"+deviceId+"'");
		return jt.queryForList(sql.getSQL());
	}

	/**
	 * 获取tr069信息
	 * 
	 * @param deviceId
	 * @return
	 */
	public GwTr069OBJ getTr09Info(String deviceId)
	{
		logger.debug("getTr09Info(deviceId:{})", deviceId);
		return tr069DAO.getTr069Info(deviceId);
	}

	/**
	 * 查询PON设备信息
	 * 
	 * @author wangsenbo
	 * @date Nov 4, 2010
	 * @param
	 * @return PONInfoOBJ[]
	 */
	@SuppressWarnings("null")
	public PONInfoOBJ[] queryPONInfo(String deviceId,String accessType)
	{
		logger.debug("queryPONInfo in");
		if (null == deviceId)
		{
			return null;
		}
		PONInfoOBJ[] ponInfoOBJ = null;
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select tx_power,rx_power,status,transceiver_temperature,supply_vottage,");
			psql.append("bias_current,bytes_sent,bytes_received,packets_sent,packets_received,");
			psql.append("sunicast_packets,runicast_packets,smulticast_packets,rmulticast_packets,");
			psql.append("sbroadcast_packets,rbroadcast_packets,fec_error,hec_error,");
			psql.append("drop_packets,spause_packets,rpause_packets from ");
		}else{
			psql.append("select * from ");
		}
		psql.append(Global.getTabName(deviceId, "gw_wan_wireinfo_epon"));
		psql.append(" where device_id=? ");
		psql.setString(1, deviceId);

		List rList = jt.queryForList(psql.getSQL());
		if (null != rList && rList.size() > 0)
		{
			int lSize = rList.size();
			ponInfoOBJ = new PONInfoOBJ[lSize];
			for (int i = 0; i < lSize; i++)
			{
				Map rMap = (Map) rList.get(i);
				ponInfoOBJ[i] = new PONInfoOBJ();
				logger.debug(rMap.toString());
				if (null != rMap && rMap.isEmpty() == false)
				{
					if(StringUtil.IsEmpty(StringUtil.getStringValue(rMap.get("tx_power"))) 
							|| StringUtil.IsEmpty(StringUtil.getStringValue(rMap.get("rx_power")))){
						rMap = getGGL(deviceId, rMap, accessType);
					}
					ponInfoOBJ[i]
							.setStatus(StringUtil.getStringValue(rMap.get("status")));
					// int
					// tx_power=Integer.parseInt(StringUtil.getStringValue(rMap.get("tx_power")));
					// int
					// rx_power=Integer.parseInt(StringUtil.getStringValue(rMap.get("rx_power")));
					double tx_powerdouble = StringUtil.getDoubleValue(rMap
							.get("tx_power"));
					double rx_powerdouble = StringUtil.getDoubleValue(rMap
							.get("rx_power"));
					// 发射光功率
					String tx_power = null; 
					// 接受发功率
					String rx_power = null; 
					// 光衰
					String sub_power = null; 
					// 发射光功率和接受发功率在江西省中的算法
					// 判断省份
					if (Global.JXDX.equals(Global.instAreaShortName))
					{
						if (tx_powerdouble > 30)
						{
							double temp_tx_power = (Math.log(tx_powerdouble / 10000) / Math
									.log(10)) * 10;
							if (temp_tx_power % 10 >= 5)
							{
								tx_powerdouble = (temp_tx_power / 10 + 1) * 10;
							}
							else
							{
								tx_powerdouble = temp_tx_power / 10 * 10;
							}
						}
						if (rx_powerdouble > 0)
						{
							double temp_rx_power = (Math.log(rx_powerdouble / 10000) / Math
									.log(10)) * 10;
							if (temp_rx_power % 10 >= 5)
							{
								rx_powerdouble = (temp_rx_power / 10 + 1) * 10;
							}
							else
							{
								rx_powerdouble = temp_rx_power / 10 * 10;
							}
						}
						// 转换两位小数点
						tx_power = dateFormate(tx_powerdouble);
						rx_power = dateFormate(rx_powerdouble);
					}
					else if (Global.NXDX.equals(Global.instAreaShortName))
					{
						DecimalFormat df = new DecimalFormat("#.00");
						tx_power = StringUtil
								.getStringValue(df.format(10 * (Math.log10(StringUtil
										.getDoubleValue(new BigDecimal(tx_powerdouble)
												.divide(new BigDecimal(10000)))))));
						rx_power = StringUtil
								.getStringValue(df.format(10 * (Math.log10(StringUtil
										.getDoubleValue(new BigDecimal(rx_powerdouble)
												.divide(new BigDecimal(10000)))))));
					}
					else if(Global.SDLT.equals(Global.instAreaShortName)){
						
						tx_power = tx_powerdouble + "";
						rx_power = rx_powerdouble + "";
					}
					else
					{
						// 发射光功率和接受发功率在其他省份中的算法
						// 发射光功率判断
						if (tx_powerdouble > 30)
						{
							double temp_tx_power = (Math.log(tx_powerdouble / 10000) / Math
									.log(10)) * 10;
							tx_powerdouble = (int) temp_tx_power;
							if (tx_powerdouble % 10 >= 5)
							{
								tx_powerdouble = (tx_powerdouble / 10 + 1) * 10;
							}
							else
							{
								tx_powerdouble = tx_powerdouble / 10 * 10;
							}
						}
						// 接受发功率判断
						if (rx_powerdouble > 30)
						{
							double temp_rx_power = (Math.log(rx_powerdouble / 10000) / Math
									.log(10)) * 10;
							rx_powerdouble = (int) temp_rx_power;
							if (rx_powerdouble % 10 >= 5)
							{
								rx_powerdouble = (rx_powerdouble / 10 + 1) * 10;
							}
							else
							{
								rx_powerdouble = rx_powerdouble / 10 * 10;
							}
						}
						tx_power = StringUtil.getStringValue(tx_powerdouble);
						rx_power = StringUtil.getStringValue(rx_powerdouble);
						sub_power =StringUtil.getStringValue(tx_powerdouble - rx_powerdouble);
					}
					ponInfoOBJ[i].setTxpower(tx_power);
					ponInfoOBJ[i].setRxpower(rx_power);
					ponInfoOBJ[i].setSubpower(sub_power);
					if (Global.JXDX.equals(Global.instAreaShortName))
					{
						ponInfoOBJ[i]
								.setTransceiverTemperature(dateFormate(StringUtil
										.getDoubleValue(rMap
												.get("transceiver_temperature")) / 256));
						ponInfoOBJ[i]
								.setSupplyVottage(dateFormate(StringUtil
										.getDoubleValue(rMap.get("supply_vottage")) * 100 / 1000 / 1000));
						ponInfoOBJ[i].setBiasCurrent(dateFormate(StringUtil
								.getDoubleValue(rMap.get("bias_current")) * 2 / 1000));
					}
					else
					{
						ponInfoOBJ[i].setTransceiverTemperature(StringUtil
								.getStringValue(rMap.get("transceiver_temperature")));
						ponInfoOBJ[i].setSupplyVottage(StringUtil.getStringValue(rMap
								.get("supply_vottage")));
						ponInfoOBJ[i].setBiasCurrent(StringUtil.getStringValue(rMap
								.get("bias_current")));
					}
					ponInfoOBJ[i].setBytesSent(StringUtil.getStringValue(rMap
							.get("bytes_sent")));
					ponInfoOBJ[i].setBytesReceived(StringUtil.getStringValue(rMap
							.get("bytes_received")));
					ponInfoOBJ[i].setPacketsSent(StringUtil.getStringValue(rMap
							.get("packets_sent")));
					ponInfoOBJ[i].setPacketsReceived(StringUtil.getStringValue(rMap
							.get("packets_received")));
					ponInfoOBJ[i].setSunicastPackets(StringUtil.getStringValue(rMap
							.get("sunicast_packets")));
					ponInfoOBJ[i].setRunicastPackets(StringUtil.getStringValue(rMap
							.get("runicast_packets")));
					ponInfoOBJ[i].setSmulticastPackets(StringUtil.getStringValue(rMap
							.get("smulticast_packets")));
					ponInfoOBJ[i].setRmulticastPackets(StringUtil.getStringValue(rMap
							.get("rmulticast_packets")));
					ponInfoOBJ[i].setSbroadcastPackets(StringUtil.getStringValue(rMap
							.get("sbroadcast_packets")));
					ponInfoOBJ[i].setRbroadcastPackets(StringUtil.getStringValue(rMap
							.get("rbroadcast_packets")));
					ponInfoOBJ[i].setFecError(StringUtil.getStringValue(rMap
							.get("fec_error")));
					ponInfoOBJ[i].setHecError(StringUtil.getStringValue(rMap
							.get("hec_error")));
					ponInfoOBJ[i].setDropPackets(StringUtil.getStringValue(rMap
							.get("drop_packets")));
					ponInfoOBJ[i].setSpausePackets(StringUtil.getStringValue(rMap
							.get("spause_packets")));
					ponInfoOBJ[i].setRpausePackets(StringUtil.getStringValue(rMap
							.get("rpause_packets")));
				}
			}
		}
		return ponInfoOBJ;
	}
	
	private Map getGGL(String deviceId, Map rMap, String acessType){
		ACSCorba corba = new ACSCorba("1");
		String[] Path = new String[5];
		String configName = "";
		if(LipossGlobals.getLipossProperty("telecom").equals(TELECOM_CUC)){
			configName = "X_CU_WANGPONInterfaceConfig";
			if(!"4".equals(acessType)) configName = "X_CU_WANEPONInterfaceConfig";
		}
		else{
			return rMap;
		}
		
		Path[0] = "InternetGatewayDevice.WANDevice.1."+configName+".OpticalTransceiver.TXPower";
		Path[1] = "InternetGatewayDevice.WANDevice.1."+configName+".OpticalTransceiver.RXPower";
		Path[2] = "InternetGatewayDevice.WANDevice.1."+configName+".OpticalTransceiver.Temperature";
		Path[3] = "InternetGatewayDevice.WANDevice.1."+configName+".OpticalTransceiver.Vcc";
		Path[4] = "InternetGatewayDevice.WANDevice.1."+configName+".OpticalTransceiver.TXBias";
		// 调ACS获取节点值
		ArrayList<ParameValueOBJ> objList = corba.getValue(deviceId, Path);
		
		// 如果ACS没有获取到节点值，则返回错误提示信息
		if (null == objList || "".equals(objList)) {
			logger.warn("光功率返回的节点与值失败");
			return rMap;
		}
		
		String nodeName = "";  // 节点名称
		for(ParameValueOBJ obj : objList){
			nodeName = obj.getName();
			if (nodeName.contains("TXPower")) {
				rMap.put("tx_power", obj.getValue());
			}
			else if (nodeName.contains("RXPower")) {
				rMap.put("rx_power", obj.getValue());
			}
			else if (nodeName.contains("Temperature")) {
				rMap.put("transceiver_temperature", obj.getValue());
			}
			else if (nodeName.contains("Vcc")) {
				rMap.put("supply_vottage", obj.getValue());
			}
			else if (nodeName.contains("TXBias")) {
				rMap.put("bias_current", obj.getValue());
			}
		}
		return rMap;
	}

	public static void main(String[] args)
	{
		PONInfoOBJ[] ponInfoOBJ = null;
		String tx_power = null; // 发射光功率
		String rx_power = null; // 接受发功率
		String sub_power = null; // 光衰
		double tx_powerdouble = StringUtil.getDoubleValue(16943);
		double rx_powerdouble = StringUtil.getDoubleValue(245);
		// 发射光功率和接受发功率在其他省份中的算法
		// 发射光功率判断
		if (tx_powerdouble > 30)
		{
			double temp_tx_power = (Math.log(tx_powerdouble / 10000) / Math
					.log(10)) * 10;
			tx_powerdouble = (int) temp_tx_power;
			if (tx_powerdouble % 10 >= 5)
			{
				tx_powerdouble = (tx_powerdouble / 10 + 1) * 10;
			}
			else
			{
				tx_powerdouble = tx_powerdouble / 10 * 10;
			}
		}
		// 接受发功率判断
		if (rx_powerdouble > 30)
		{
			double temp_rx_power = (Math.log(rx_powerdouble / 10000) / Math
					.log(10)) * 10;
			rx_powerdouble = (int) temp_rx_power;
			if (rx_powerdouble % 10 >= 5)
			{
				rx_powerdouble = (rx_powerdouble / 10 + 1) * 10;
			}
			else
			{
				rx_powerdouble = rx_powerdouble / 10 * 10;
			}
		}
		tx_power = StringUtil.getStringValue(tx_powerdouble);
		rx_power = StringUtil.getStringValue(rx_powerdouble);
		System.out.println(tx_power);
		System.out.println(rx_power);
	}

	/** add by chenjie 2011-9-9 **/
	/**
	 * 获取servList
	 */
	public List<Map> getAllChannel(String device_id)
	{
		logger.debug("getAllChannel");
		// LipossGlobals.getGw_Type(device_id);
		String sql = "select device_id, wan_id, wan_conn_id, wan_conn_sess_id, serv_list,sess_type,ip from  "
				+ Global.getTabName(device_id, "gw_wan_conn_session")
				+ " where device_id=?";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, device_id);
		List<Map> list = jt.queryForList(psql.getSQL());
		return list;
	}

	@Override
	public WanConnSessObj[] queryDevWanConnSession(Map map)
	{
		WanConnSessObj[] wanConnSessObj = wanConnSessDao.queryDevWanConnSession(map);
		return wanConnSessObj;
	}

	/**
	 * 保留小数点后两位方法
	 * 
	 * @param str
	 * @return STR
	 */
	public String dateFormate(double str_double)
	{
		String str = StringUtil.getStringValue(str_double);
		if ("".equals(str) || str == null)
		{
			return null;
		}
		else
		{
			if (str.indexOf(".") != -1)
			{
				// 获取小数点的位置
				String dianAfter = str.substring(0, str.indexOf(".") + 1);
				String afterData = str.replace(dianAfter, "");
				// 获取小数点后面的数字 是否有两位 不足两位补足两位
				if (afterData.length() < 2)
				{
					afterData = afterData + "0";
				}
				str = str.substring(0, str.indexOf(".")) + "."
						+ afterData.substring(0, 2);
			}
			return str;
		}
	}
	
	public List getDeviceCheckProject(String gw_type)
	{
		String tableName="tab_hgw_gather_node_config";
		if (gw_type.equals("2")) {
			tableName="tab_egw_gather_node_config";
		}
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select test_require,id,test_name from ");
		}else{
			psql.append("select * from ");
		}
		psql.append(tableName+" where enable=1 order by priority asc");
		return jt.queryForList(psql.getSQL());
	}
	
	/**
	 * 获取所有厂商
	 */
	private Map<String,String> getVendorAdd()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select vendor_id,vendor_add from tab_vendor order by vendor_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				map.put(StringUtil.getStringValue(m,"vendor_id"),
						StringUtil.getStringValue(m,"vendor_add"));
			}
		}
		
		return map;
	}
	
	/**
	 * 获取所有型号
	 */
	private Map<String,String> getDeviceModel()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_model_id,device_model from gw_device_model order by device_model_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,String> map=new HashMap<String,String>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				map.put(StringUtil.getStringValue(m,"device_model_id"),
						StringUtil.getStringValue(m,"device_model"));
			}
		}
		
		return map;
	}
	
	/**
	 * 获取所有版本
	 */
	private Map<String,List<String>> getDeviceTypeInfo()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select devicetype_id,hardwareversion,ip_model_type,softwareversion ");
		psql.append("from tab_devicetype_info order by devicetype_id ");
		List<Map> list=jt.queryForList(psql.getSQL());
		Map<String,List<String>> map=new HashMap<String,List<String>>();
		if(list!=null && !list.isEmpty()){
			for(Map m:list){
				List<String> l=new ArrayList<String>();
				l.add(StringUtil.getStringValue(m,"hardwareversion"));
				l.add(StringUtil.getStringValue(m,"softwareversion"));
				l.add(StringUtil.getStringValue(m,"ip_model_type"));
				map.put(StringUtil.getStringValue(m,"devicetype_id"),l);
			}
		}
		
		return map;
	}
	
}
