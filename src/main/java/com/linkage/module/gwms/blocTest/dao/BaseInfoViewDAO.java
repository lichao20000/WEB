package com.linkage.module.gwms.blocTest.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

import dao.util.JdbcTemplateExtend;

public class BaseInfoViewDAO {

	//日志记录
	private static Logger logger = LoggerFactory.getLogger(BaseInfoViewDAO.class);
	
	private JdbcTemplateExtend jt;
	
	public void setDao(DataSource dao) {
		this.jt = new JdbcTemplateExtend(dao);
	}
	
	public List getBaseInfoViewList(int curPage_splitPage, int num_splitPage,
			String customerName,String linkphone,String cityId,
			String username,String deviceId,String deviceSn,
			String bssSheetId,String result,String loopbackIp)
	{
		logger.debug("getBaseInfoViewList()");
		PrepareSQL pSql = new PrepareSQL();
		//TODO wait
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			pSql.setSQL("select a.customer_id,a.customer_name,a.linkphone,a.city_id," +
					"b.username,b.user_id,b.device_id,b.oui,b.device_serialnumber," +
					"c.bss_sheet_id,c.result as result_1,d.loopback_ip " +
					"from tab_customerinfo a,tab_egwcustomer b left " +
					" join tab_gw_device d on b.device_id=d.device_id,tab_bss_sheet c " +
					" where a.customer_id=b.customer_id and b.serv_type_id=c.product_spec_id " +
					" and b.username=c.username and b.user_state=cast(c.type as char) ");
		}else{
			pSql.setSQL("select a.customer_id,a.customer_name,a.linkphone,a.city_id," +
					"b.username,b.user_id,b.device_id,b.oui,b.device_serialnumber,c.bss_sheet_id,c.result as result_1," +
					"d.loopback_ip from tab_customerinfo a,tab_egwcustomer b left " +
					" join tab_gw_device d on b.device_id=d.device_id,tab_bss_sheet c " +
					" where a.customer_id=b.customer_id and b.serv_type_id=c.product_spec_id " +
					" and b.username=c.username and b.user_state=convert(varchar,c.type) ");
		}
		
		if(null!=customerName){
			pSql.appendAndString("a.customer_name", PrepareSQL.EQUEAL, customerName);
		}
		if(null!=linkphone){
			pSql.appendAndString("a.linkphone", PrepareSQL.EQUEAL, linkphone);
		}
		if(null!=cityId && !"".equals(cityId) && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSql.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		if(null!=username){
			pSql.appendAndString("b.username", PrepareSQL.EQUEAL, username);
		}
		if(null!=deviceId){
			pSql.appendAndString("b.device_id", PrepareSQL.EQUEAL, deviceId);
		}
		if(null!=deviceSn){
			pSql.appendAndString("b.device_serialnumber", PrepareSQL.EQUEAL, deviceSn);
		}
		if(null!=bssSheetId){
			pSql.appendAndString("c.bss_sheet_id", PrepareSQL.EQUEAL, bssSheetId);
		}
		if(null!=result){
			pSql.appendAndNumber("c.result", PrepareSQL.EQUEAL, result);
		}
		if(null!=loopbackIp){
			pSql.appendAndString("d.loopback_ip", PrepareSQL.EQUEAL, loopbackIp);
		}
		
		return jt.querySP(pSql.getSQL(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper() {
				public Object mapRow(ResultSet rs, int arg1)
						throws SQLException {
					
					Map<String, String> map = new HashMap<String, String>();
	
					map.put("customerId", rs.getString("customer_id"));
					map.put("userId", rs.getString("user_id"));
					map.put("customerName", rs.getString("customer_name"));
					map.put("linkphone", rs.getString("linkphone"));
					map.put("username", rs.getString("username"));
					map.put("cityName", CityDAO.getCityIdCityNameMap().get(rs.getString("city_id")));
					String _deviceId = rs.getString("device_id");
					if(null==_deviceId || "null".equals(_deviceId)){
						_deviceId = "";
					}
					String _oui = rs.getString("oui");
					if(null==_oui || "null".equals(_oui)){
						_oui = "";
					}
					String _deviceSn = rs.getString("device_serialnumber");
					if(null==_deviceSn || "null".equals(_deviceSn)){
						_deviceSn = "";
					}else{
						_deviceSn = "_" + _deviceSn;
					}
					map.put("deviceId",_deviceId);
					map.put("deviceSn", _oui + _deviceSn);
					map.put("bssSheetId",rs.getString("bss_sheet_id"));
					map.put("loopbackIp",rs.getString("loopback_ip"));
					if("0".equals(rs.getString("result_1"))){
						map.put("result","成功");
					}else {
						map.put("result","失败");
					}
					return map;
				}
			});
		}
	
	public int getBaseInfoViewCount(int num_splitPage,
			String customerName,String linkphone,String cityId,
			String username,String deviceId,String deviceSn,
			String bssSheetId,String result,String loopbackIp){
		logger.debug("getBaseInfoViewCount()");
		PrepareSQL pSql = new PrepareSQL();
		//TODO wait
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			pSql.append("select count(*) from tab_customerinfo a,tab_egwcustomer b left " +
					" join tab_gw_device d on b.device_id=d.device_id,tab_bss_sheet c " +
					" where a.customer_id=b.customer_id and b.serv_type_id=c.product_spec_id " +
					" and b.username=c.username and b.user_state=cast(c.type as char) ");
		}else{
			pSql.append("select count(1) from tab_customerinfo a,tab_egwcustomer b left " +
					" join tab_gw_device d on b.device_id=d.device_id,tab_bss_sheet c " +
					" where a.customer_id=b.customer_id and b.serv_type_id=c.product_spec_id " +
					" and b.username=c.username and b.user_state=convert(varchar,c.type) ");
		}
		
		if(null!=customerName){
			pSql.appendAndString("a.customer_name", PrepareSQL.EQUEAL, customerName);
		}
		if(null!=linkphone){
			pSql.appendAndString("a.linkphone", PrepareSQL.EQUEAL, linkphone);
		}
		if(null!=cityId && !"".equals(cityId) && !"00".equals(cityId)){
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			pSql.append(" and a.city_id in ('" + StringUtils.weave(cityArray,"','") + "')");
			cityArray = null;
		}
		if(null!=username){
			pSql.appendAndString("b.username", PrepareSQL.EQUEAL, username);
		}
		if(null!=deviceId){
			pSql.appendAndString("b.device_id", PrepareSQL.EQUEAL, deviceId);
		}
		if(null!=deviceSn){
			pSql.appendAndString("b.device_serialnumber", PrepareSQL.EQUEAL, deviceSn);
		}
		if(null!=bssSheetId){
			pSql.appendAndString("c.bss_sheet_id", PrepareSQL.EQUEAL, bssSheetId);
		}
		if(null!=result){
			pSql.appendAndNumber("c.result", PrepareSQL.EQUEAL, result);
		}
		if(null!=loopbackIp){
			pSql.appendAndString("d.loopback_ip", PrepareSQL.EQUEAL, loopbackIp);
		}
		
		int total = jt.queryForInt(pSql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}

		return maxPage;
	}
	
	public List getConfigFile(String deviceId)
	{
		PrepareSQL pSql = new PrepareSQL();
	/**	
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
			//无/gwms/blocTest/deviceConfigFileList.jsp
		}
	*/	
		pSql.append("select * from tab_vercon_file a,tab_file_server b ");
		pSql.append("where a.dir_id=b.dir_id and a.verconfile_isexist=1 and a.device_id='" + deviceId + "'");
		
		return jt.queryForList(pSql.toString());
	}
}
