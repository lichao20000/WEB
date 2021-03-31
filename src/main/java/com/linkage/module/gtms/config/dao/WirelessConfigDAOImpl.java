package com.linkage.module.gtms.config.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

public class WirelessConfigDAOImpl extends SuperDAO implements WirelessConfigDAO {
	private static Logger logger = LoggerFactory.getLogger(WirelessConfigDAOImpl.class);
	
	public void doConfig(String deviceId) {
		String sql =" update  hgwcust_serv_info set  ssid2Status=1 where user_id =" +
				"( select a.user_id from hgwcust_serv_info a, tab_hgwcustomer b  " +
				" where a.user_id=b.user_id and  a.serv_type_id = 11 and b.device_id=?) ";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.setString(1, deviceId);
		jt.update(psql.toString());
	}

	public String getUserInfo(String deviceId) {
		String specId ="";
		Map <String,Object> map =null;
		String sql ="  select a.spec_id  from  tab_hgwcustomer a right join tab_bss_dev_port b on a.spec_id = b.id  where a.device_id=?";
		PrepareSQL psql = new PrepareSQL(sql); 
		psql.setString(1, deviceId);
		map  =  queryForMap(psql.toString());
		if(null!=map){
			specId = StringUtil.getStringValue(map.get("spec_id"));
		}
		return specId;
	}

	@Override
	public List isExists(String username,String gwType) {
		logger.debug("getUserByServ({},{})", username,gwType);
		StringBuffer sql = new StringBuffer();
		if(gwType.equals(Global.GW_TYPE_ITMS))
		{
//			sql.append("select a.user_id,b.device_id from hgwcust_serv_info a right join tab_hgwcustomer  b on " );
//			sql.append("  a.user_id = b.user_id  where a.username='").append(
//					username).append("' and a.serv_type_id=10 ");
			sql.append("select a.user_id,b.device_id,b.username from hgwcust_serv_info a right join tab_hgwcustomer  b on " );
			sql.append("  a.user_id = b.user_id  where a.username='").append(
					username).append("' and a.serv_type_id=10 ");
		}
		else if(gwType.equals(Global.GW_TYPE_BBMS))
		{
//			sql.append("select a.user_id,b.device_id from egwcust_serv_info a right join tab_hgwcustomer  b on " );
//			sql.append("  a.user_id = b.user_id  where a.username='").append(
//					username).append("' and a.serv_type_id=10 ");
			sql.append("select a.user_id,b.device_id,b.username from egwcust_serv_info a right join tab_hgwcustomer  b on " );
			sql.append("  a.user_id = b.user_id  where a.username='").append(
					username).append("' and a.serv_type_id=10 ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		
		return  jt.queryForList(psql.getSQL()); 
	}
	

	public String getLoid(String username,String gwType) {
		logger.debug("getUserByServ({},{})", username,gwType);
		String result = "";
		Map map = null;
		StringBuffer sql = new StringBuffer();
		if(gwType.equals(Global.GW_TYPE_ITMS))
		{
			sql.append("select a.user_id,b.username from hgwcust_serv_info a right join tab_hgwcustomer  b on " );
			sql.append("  a.user_id = b.user_id  where a.username='").append(
					username).append("' and a.serv_type_id=10 ");
		}
		else if(gwType.equals(Global.GW_TYPE_BBMS))
		{
			sql.append("select a.user_id,b.username from egwcust_serv_info a right join tab_hgwcustomer  b on " );
			sql.append("  a.user_id = b.user_id  where a.username='").append(
					username).append("' and a.serv_type_id=10 ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());

		List list = jt.queryForList(psql.getSQL()); 
		if(null!=list && list.size()>0)
		{
			map = (Map)list.get(0);
			result = StringUtil.getStringValue(map.get("username"));
		}
		
		return  result;
	}

}
