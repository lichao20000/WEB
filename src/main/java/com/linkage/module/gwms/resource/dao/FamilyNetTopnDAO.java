
package com.linkage.module.gwms.resource.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.resource.model.TopoInfoModel;

/**
 * @author yages (Ailk No.78987)
 * @version 1.0
 * @since 2019-11-4
 * @category com.linkage.module.gwms.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class FamilyNetTopnDAO extends SuperDAO
{

	private static Logger logger = LoggerFactory.getLogger(FamilyNetTopnDAO.class);

	public List<TopoInfoModel> getNetTopnInfo(String deviceId)
	{
		List<TopoInfoModel> devInfos = new ArrayList<TopoInfoModel>();
		PrepareSQL pSql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			pSql.append("select mac,hostname,type,port,os,online_time,ip,devname,active,update_time,par_mac ");
		}else{
			pSql.append("select * ");
		}
		pSql.append("from tab_gw_topInfo where device_id=? ");
		pSql.setString(1, deviceId);
		List<HashMap<String, String>> result = DBOperation.getRecords(pSql.getSQL());
		if (null != result && !result.isEmpty())
		{
			for (HashMap<String, String> hashMap : result)
			{
				TopoInfoModel model = new TopoInfoModel();
				model.setMac(hashMap.get("mac"));
				model.setModel(hashMap.get("hostname"));
				model.setAcc_type(Integer.parseInt(hashMap.get("type")));
				model.setAcc_port(hashMap.get("port"));
				model.setOs(hashMap.get("os"));
				model.setOnline_time(StringUtil.IsEmpty(hashMap.get("online_time")) ? 0
						: Long.parseLong(hashMap.get("online_time")));
				model.setIp(hashMap.get("ip"));
				model.setHostname(hashMap.get("devname"));
				model.setActive(StringUtil.IsEmpty(hashMap.get("active")) ? 0 : Integer
						.parseInt(hashMap.get("active")));
				model.setTimestamp(StringUtil.IsEmpty(hashMap.get("update_time")) ? 0
						: Long.parseLong(hashMap.get("update_time")));
				model.setPar_mac(hashMap.get("par_mac"));
				devInfos.add(model);
			}
		}
		else
		{
			logger.warn("????????????id[{}],??????????????????????????????", deviceId);
		}
		return devInfos;
	}

	/**
	 * ??????????????????????????????????????????
	 * 
	 * @param deviceId
	 * @param model
	 * @return
	 */
	public boolean saveNetTopnInfo(String deviceId, List<TopoInfoModel> model,
			long timestamp)
	{
		boolean falg = true;
		/**
		 * ????????????????????????????????????????????????????????????????????????????????????
		 */
		PrepareSQL pSql_del = new PrepareSQL();
		pSql_del.append("delete from tab_gw_topInfo where device_Id = '" + deviceId + "'");
		int del_result = DBOperation.executeUpdate(pSql_del.getSQL());
		if (del_result == -1)
		{
			logger.warn("??????????????????sql:[{}],deviceId:[{}]", pSql_del.getSQL(), deviceId);
			return false;
		}
		
		for (TopoInfoModel topoInfoModel : model)
		{
			String mac = topoInfoModel.getMac(); // ??????????????????mac?????????????????????????????????
			if (!StringUtil.IsEmpty(mac))
			{
				PrepareSQL pSql = new PrepareSQL();
				pSql.append("insert into tab_gw_topInfo(device_id,mac,hostname,");
				pSql.append("type,port,os,online_time,ip,devname,active,update_time,vendor) ");
				pSql.append("values(?,?,?,?,?,?,?,?,?,?,?,?) ");
				pSql.setString(1, deviceId);
				pSql.setString(2, mac);
				pSql.setString(3, topoInfoModel.getModel());
				pSql.setLong(4, topoInfoModel.getAcc_type());
				pSql.setString(5, topoInfoModel.getAcc_port());
				pSql.setString(6, topoInfoModel.getOs());
				pSql.setLong(7, topoInfoModel.getOnline_time());
				pSql.setString(8, topoInfoModel.getIp());
				pSql.setString(9, topoInfoModel.getHostname());
				pSql.setLong(10, topoInfoModel.getActive());
				pSql.setLong(11, timestamp);
				pSql.setString(12, topoInfoModel.getVendor());
				int result = DBOperation.executeUpdate(pSql.getSQL());
				if (result == -1)
				{
					falg = false;
					logger.warn("????????????sql:[{}]", pSql.getSQL());
				}
			}
		}
		logger.warn("???????????????????????????????????????[{}]??????????????????[{}],????????????[{}]", 
				deviceId, model.size(),timestamp);
		return falg;
	}

	/**
	 * ????????????id????????????LOID
	 * 
	 * @param deviceId
	 * @return loid
	 */
	public Map<String, String> queryLoidByDeviceId(String deviceId)
	{
		String sql = "select username from tab_hgwcustomer where device_id= ? ";
		PrepareSQL pSql = new PrepareSQL(sql);
		pSql.setString(1, deviceId);
		return DBOperation.getRecord(pSql.getSQL());
	}

	/**
	 * ????????????id??????SN
	 * 
	 * @param deviceId
	 * @return loid
	 */
	public Map<String, String> querySNByDeviceId(String deviceId)
	{
		String sql = "select device_serialnumber,cpe_mac from tab_gw_device where device_id= ? ";
		PrepareSQL pSql = new PrepareSQL(sql);
		pSql.setString(1, deviceId);
		return DBOperation.getRecord(pSql.getSQL());
	}

	/**
	 * ????????????id???????????????????????????
	 * 
	 * @param deviceId
	 * @return devInfo
	 */
	public Map<String, String> queryDevInfoByDeviceId(String deviceId)
	{
	/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		} */
		String sql = "select a.device_serialnumber,a.cpe_mac,a.city_id,"
				+ "b.vendor_name,c.device_model,d.softwareversion,a.loopback_ip loopback_ip," 
				+ "d.hardwareversion,e.username loid,f.username kdname,g.voip_phone phone "
				+ "from tab_gw_device a,tab_vendor b,gw_device_model c,"
				+ "tab_devicetype_info d,hgwcust_serv_info f, "
				+ "tab_hgwcustomer e left join tab_voip_serv_param g on e.user_id=g.user_id "
				+ "where a.device_id=e.device_id and a.vendor_id = b.vendor_id "
				+ "and a.device_model_id=c.device_model_id and a.devicetype_id=d.devicetype_id "
				+ "and e.user_id=f.user_id and a.device_id=? ";
		PrepareSQL pSql = new PrepareSQL(sql);
		pSql.setString(1, deviceId);
		return DBOperation.getRecord(pSql.getSQL());
	}

	/**
	 * ????????????mac????????????
	 * 
	 * @param mac
	 * @return vendor
	 */
	public String queryVendorByMac(String mac)
	{
		mac = mac.substring(0, 6).toUpperCase();
		String sql = "select vendor from mac_vendor where mac = ?";
		PrepareSQL pSql = new PrepareSQL(sql);
		pSql.setString(1, mac);
		Map<String, String> result = DBOperation.getRecord(pSql.getSQL());
		return StringUtil.getStringValue(result,"vendor","null");
	}
}
