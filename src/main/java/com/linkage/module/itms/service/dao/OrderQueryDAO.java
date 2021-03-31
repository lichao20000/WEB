package com.linkage.module.itms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class OrderQueryDAO extends SuperDAO {
	
	private static Logger logger = LoggerFactory.getLogger(OrderQueryDAO.class);
	
	//操作类型
	private Map<String,String> typeMap = new HashMap<String, String>();
	
	//业务类型
	private Map<String, String> servMap = new HashMap<String, String>();
	
	
	private Map<String, String> cityMap = null;
	
	
	public OrderQueryDAO(){
		typeMap.put("1", "开户");
		typeMap.put("2", "暂停");
		typeMap.put("3", "销户");
		typeMap.put("4", "复机");
		typeMap.put("5", "更改速率");
		typeMap.put("6", "更改账号");
		typeMap.put("7", "更改设备");
		typeMap.put("8", "移机");
		typeMap.put("9", "改IPTV个数");
		typeMap.put("10", "修改VOIP认证密码");
		typeMap.put("11", "换卡");
		typeMap.put("12", "变更语音信息");
		
		servMap.put("10", "宽带");
		servMap.put("11", "IPTV");
		servMap.put("14", "VOIP");
		servMap.put("20", "E8-C 资料");
		servMap.put("21", "E8-C IPTV");
		servMap.put("22", "E8-C 宽带");
		servMap.put("23", "E8-C固话视频通话");
		servMap.put("24", "E8-CIPTV视频通话");
		servMap.put("30", "E8-C机卡分离资料");
		
		
	}
	
	public List<Map> getOrderInfo(String devicesn,String username,int curPage_splitPage, int num_splitPage){
		logger.debug("OrderQueryDAO->getOrderInfo{}");
		StringBuffer sql = new StringBuffer();
		sql.append("select bnet_account,username,product_spec_id,type,result,device_serialnumber,city_id,receive_date from tab_egw_bsn_open_original where 1=1 ");
		if(!StringUtil.IsEmpty(devicesn)){
			sql.append(" and device_serialnumber='").append(devicesn.trim()).append("' ");
		}
		
		if(!StringUtil.IsEmpty(username)){
			sql.append(" and username='").append(username.trim()).append("' ");
		}
		sql.append(" order by receive_date desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = CityDAO.getCityIdCityNameMap();
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String,String> map = new HashMap<String, String>();
						map.put("user_name", StringUtil.getStringValue(rs.getString("username")));
						map.put("bnet_account", StringUtil.getStringValue(rs.getString("bnet_account")));
						map.put("product_spec_id", servMap.get(StringUtil.getStringValue(rs.getString("product_spec_id"))));
						//map.put("product_spec_id", StringUtil.getStringValue(Global.Serv_type_Map.get(StringUtil.getStringValue(rs.getString("product_spec_id")))));
						map.put("type", typeMap.get(StringUtil.getStringValue(rs.getString("type"))));
						String result = StringUtil.getStringValue(rs.getString("result"));
						if("0".equals(result)){
							map.put("result", "成功");
						}else if("1".equals(result)){
							map.put("result", "失败");
						}else{
							map.put("result", "");
						}
						map.put("device_serialnumber", StringUtil.getStringValue(rs.getString("device_serialnumber")));
						map.put("city_name", cityMap.get(StringUtil.getStringValue(rs.getString("city_id"))));
						DateTimeUtil dt = new DateTimeUtil(StringUtil.getLongValue(rs.getString("receive_date")));
						map.put("receive_date", dt.getLongDate());
						return map;
					}
				});
		
		return list;
	}
	
	public int countOrderInfo(String devicesn,String username,int curPage_splitPage, int num_splitPage)
	{
		logger.debug("OrderQueryDAO->countOrderInfo");
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) ");
		}else{
			sql.append("select count(1) ");
		}
		sql.append("from tab_egw_bsn_open_original where 1=1 ");
		if(!StringUtil.IsEmpty(devicesn)){
			sql.append(" and device_serialnumber='").append(devicesn).append("' ");
		}
		if(!StringUtil.IsEmpty(username)){
			sql.append(" and username='").append(username).append("' ");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
}
