package com.linkage.module.gwms.dao.gw;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.gw.LanHostObj;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-7-6
 */
public class LanHostDAO extends SuperDAO{
	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(LanHostDAO.class);

	
	/**
	 * 获取lan测连接主机的状态,不状态
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return LanDeviceHost[]
	 */
	public LanHostObj[] queryLanHost(String deviceId) {
		logger.debug("queryLanHost(deviceId) :" + deviceId);
		return queryLanHost(deviceId, 0);
	}

	/**
	 * 获取lan测连接主机的状态
	 * 
	 * @param state:1 表示active=1
	 * @author Jason(3412)
	 * @date 2009-7-9
	 * @return LanHostObj[]
	 */
	public LanHostObj[] queryLanHost(String deviceId, int state) {
		logger.debug("queryLanHost(deviceId, state) :" + deviceId + "," + state);
		LanHostObj[] lanHost = null;
		if(true == StringUtil.IsEmpty(deviceId)){
			logger.warn("deviceId is null");
		}else{
			PrepareSQL psql = new PrepareSQL();
			if(DBUtil.GetDB()==Global.DB_MYSQL){
				psql.append("select active,address_source,host_inst,hostname,ipaddress,");
				psql.append("lan_inst,mac_address,layer2Interface ");
			}else{
				psql.append("select * ");
			}
			psql.append("from gw_lan_host where device_id=? ");
			if(1 == state){
				psql.append(" and active='1'");
			}
			psql.setString(1, deviceId);
			
			List rList = jt.queryForList(psql.getSQL());
			if (null != rList && false == rList.isEmpty()) {
				int lSize = rList.size();
				lanHost = new LanHostObj[lSize];
				for (int i = 0; i < lSize; i++) 
				{
					Map rMap = (Map) rList.get(i);
					lanHost[i] = new LanHostObj();
					lanHost[i].setActive(StringUtil.getStringValue(rMap,"active"));
					lanHost[i].setAddressSource(StringUtil.getStringValue(rMap,"address_source"));
					lanHost[i].setHost_inst(StringUtil.getIntValue(rMap,"host_inst"));
					lanHost[i].setHostname(StringUtil.getStringValue(rMap,"hostname"));
					lanHost[i].setIPAddress(StringUtil.getStringValue(rMap,"ipaddress"));
					lanHost[i].setLan_inst(StringUtil.getIntValue(rMap,"lan_inst"));
					lanHost[i].setMacAddress(StringUtil.getStringValue(rMap,"mac_address"));
					lanHost[i].setInterf(StringUtil.getStringValue(rMap,"layer2Interface"));
				}
			}else{
				logger.warn("gw_lan_host表中数据为空");
			}
		}
		return lanHost;
	}
	
}
