package com.linkage.module.gwms.dao.gw;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.gw.DeviceWireInfoObj;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-7-6
 */
public class WireInfoDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(WireInfoDAO.class);

	/**
	 * 获取设备线路信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-24
	 * @return DeviceWireInfoObj[]
	 */
	public DeviceWireInfoObj[] queryDevWireInfo(String deviceId) 
	{
		logger.debug("queryDevWireInfo in");
		if (null == deviceId) {
			return null;
		}
		DeviceWireInfoObj[] wireInfoObj = null; 
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select status,data_path,down_attenuation,down_maxrate,");
			psql.append("interleave_depth,modulation_type,up_attenuation,");
			psql.append("up_maxrate,wan_inst,up_noise,down_noise ");
		}else{
			psql.append("select * ");
		}
		psql.append("from "+Global.getTabName(deviceId, "gw_wan_wireinfo"));
		psql.append(" where device_id=? ");
		psql.setString(1, deviceId);
		logger.debug(psql.getSQL());
		List rList = jt.queryForList(psql.getSQL());
		if (null != rList && rList.size() > 0) {
			int lSize = rList.size();
			wireInfoObj = new DeviceWireInfoObj[lSize];
			for (int i = 0; i < lSize; i++) {
				Map rMap = (Map) rList.get(i);
				wireInfoObj[i] = new DeviceWireInfoObj();
				logger.debug(rMap.toString());
				if (null != rMap && rMap.isEmpty() == false) 
				{
					wireInfoObj[i].setWireStatus(
							String.valueOf(rMap.get("status")));
					wireInfoObj[i].setDataPath(
							String.valueOf(rMap.get("data_path")));
					if (null != rMap.get("down_attenuation")) {
						wireInfoObj[i].setDownstreamAttenuation(
								Long.valueOf(String.valueOf(rMap.get("down_attenuation"))));
					}
					if (null != rMap.get("down_maxrate")) {
						wireInfoObj[i].setDownstreamMaxRate(
								Long.valueOf(String.valueOf(rMap.get("down_maxrate"))));
					}
					wireInfoObj[i].setInterleaveDepth(
							String.valueOf(rMap.get("interleave_depth")));
					wireInfoObj[i].setModulationType(
							String.valueOf(rMap.get("modulation_type")));
					if (null != rMap.get("up_attenuation")) {
						wireInfoObj[i].setUpstreamAttenuation(
								Long.valueOf(String.valueOf(rMap.get("up_attenuation"))));
					}
					if (null != rMap.get("up_maxrate")) {
						wireInfoObj[i].setUpstreamMaxRate(
								Long.valueOf(String.valueOf(rMap.get("up_maxrate"))));
					}
					wireInfoObj[i].setWanDeviceInstance(
							String.valueOf(rMap.get("wan_inst")));
					wireInfoObj[i].setUpNoise(
							StringUtil.getStringValue(rMap.get("up_noise")));
					wireInfoObj[i].setDownNoise(
							StringUtil.getStringValue(rMap.get("down_noise")));
				}
			}
		}
		return wireInfoObj;
	}
	
}
