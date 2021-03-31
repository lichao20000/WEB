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
import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

public class OperateByHistoryCQOldDAO extends SuperDAO  {
	
	// 日志记录
		private static Logger logger = LoggerFactory
				.getLogger(OperateByHistoryCQOldDAO.class);
		
		private Map<String, String> cityMap = null;
		
		private Map<String,String> typeMap = new HashMap<String, String>();
		private Map<String,String>  servMap = new HashMap<String, String>();
		public OperateByHistoryCQOldDAO(){
			if (Global.CQDX.equals(Global.instAreaShortName)) {
				servMap.put("10", "宽带业务");
				servMap.put("11", "IPTV业务");
				servMap.put("14", "VOIP业务");
				servMap.put("25", "WIFI业务");
				servMap.put("999", "机卡分离");
				
				typeMap.put("1", "新装");
				typeMap.put("2", "拆机");
				typeMap.put("5", "更改宽带用户信息");
				typeMap.put("6", "更改设备ID");
				typeMap.put("7", "改账号");
				typeMap.put("8", "移机");
				typeMap.put("9", "模式更改");
				typeMap.put("10", "改密码");
				typeMap.put("20", "解除绑定");
				typeMap.put("13", "修改WIFI密码");
				typeMap.put("14", "更改IPV6或IPV4");
				typeMap.put("15", "信息修改");
				typeMap.put("18", "修改客户信息");
				typeMap.put("16", "调整IPTV组播参数");
				typeMap.put("11", "SS H.248 -> IMS H.248割接业务");
				typeMap.put("12", "MS/SS SIP 修改用户鉴权密码");
				typeMap.put("17", "voip修改业务参数");
				typeMap.put("999", "机卡分离");
			}
			else{
				typeMap.put("1", "开户");
				typeMap.put("2", "变更");
				typeMap.put("3", "销户");
				typeMap.put("4", "暂停");
				typeMap.put("5", "复机");
				
				servMap.put("20", "用户资料");
				servMap.put("10", "宽带业务");
				servMap.put("11", "IPTV业务");
				servMap.put("14", "VOIP业务");
				servMap.put("28", "VPDN业务");
			}
		}
		
	
		
	/**
	 *  查询2015年以后老平台历史工单
	 * @param starttime
	 * @param endtime
	 * @param username
	 * @param ACCOUNT_NAME
	 * @param SERIAL_NUMBER
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getOperateByHistoryInfo(String starttime, String endtime,
			String username, String ACCOUNT_NAME,String SERIAL_NUMBER, int curPage_splitPage, int num_splitPage) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select id as work_asgn_id,bus_type service_type,order_tpye service_opt,");
		sql.append("userid account_name,serialnumber serial_number,loid,");
		sql.append("exec_condition status,begintime arrive_time,endtime deal_time,customer_id,area area_code ");
		sql.append("from order_his ");
		if(DBUtil.GetDB()==3){
			sql.append("where 1=1 ");
		}else{
			sql.append("where rownum<35 ");
		}
		
		if(!StringUtil.IsEmpty(username)){
			sql.append(" and LOID='").append(username).append("' ");
		}
		
		if (!StringUtil.IsEmpty(ACCOUNT_NAME)){
			sql.append(" and USERID ='").append(ACCOUNT_NAME).append("' ");
		}
		if (!StringUtil.IsEmpty(SERIAL_NUMBER)){
			sql.append(" and SERIALNUMBER ='").append(SERIAL_NUMBER).append("' ");
		}
		if(DBUtil.GetDB()==3){
			sql.append("limit 35 ");
		}
		sql.append(" order by  ARRIVE_TIME ");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = Global.G_CityId_CityName_Map;
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String,String> map = new HashMap<String, String>();
						map.put("WORK_ASGN_ID", rs.getString("WORK_ASGN_ID"));
						String SERVICE_TYPE = rs.getString("SERVICE_TYPE");
						map.put("SERVICE_TYPE", SERVICE_TYPE);
						String SERVICE_OPT = rs.getString("SERVICE_OPT");
						map.put("SERVICE_OPT", SERVICE_OPT);
						map.put("ACCOUNT_NAME", rs.getString("ACCOUNT_NAME"));
						map.put("SERIAL_NUMBER", rs.getString("SERIAL_NUMBER"));
						map.put("LOID", rs.getString("LOID"));
						String status = rs.getString("STATUS");
						map.put("STATUS", status);
						map.put("ARRIVE_TIME", rs.getString("ARRIVE_TIME"));
						map.put("DEAL_TIME", rs.getString("DEAL_TIME"));
						//map.put("DURING_TIME", StringUtil.getStringValue(during));
						
						map.put("CUSTOMER_ID", rs.getString("CUSTOMER_ID"));
						String cityId = rs.getString("AREA_CODE");
						map.put("cityName", cityId);
						map.put("isHistory", "no");
						return map;
					}
				});
		return list;
	}



	/**
	 * 查询2015年及以前老平台历史工单
	 * @param starttime
	 * @param endtime
	 * @param username
	 * @param ACCOUNT_NAME
	 * @param SERIAL_NUMBER
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> getOperateByHistoryInfoBe2015(String starttime, String endtime,
			String username, String ACCOUNT_NAME,String SERIAL_NUMBER, int curPage_splitPage, int num_splitPage) 
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select work_asgn_id,service_type,service_opt,");
		sql.append("account_name,serial_number,loid,status,");
		
		if(DBUtil.GetDB()==3){
			sql.append("date_format(arrive_time,'%Y-%m-%d %H:%i:%s') arrive_time,");
			sql.append("date_format(deal_time,'%Y-%m-%d %H:%i:%s') deal_time,");
			sql.append("customer_id,area_code from worktickets_history where 1=1");
		}else{
			sql.append("to_char(arrive_time,'yyyy-MM-dd HH24:mi:ss') arrive_time,");
			sql.append("to_char(deal_time,'yyyy-MM-dd HH24:mi:ss') deal_time,");
			sql.append("customer_id,area_code from worktickets_history where rownum<35 ");
		}
		
		if(!StringUtil.IsEmpty(username)){
			sql.append(" and LOID='").append(username).append("' ");
		}
		
		if (!StringUtil.IsEmpty(ACCOUNT_NAME)){
			sql.append(" and ACCOUNT_NAME ='").append(ACCOUNT_NAME).append("' ");
		}
		if (!StringUtil.IsEmpty(SERIAL_NUMBER)){
			sql.append(" and SERIAL_NUMBER ='").append(SERIAL_NUMBER).append("' ");
		}
		if(DBUtil.GetDB()==3){
			sql.append("limit 35 ");
		}
		sql.append(" order by  ARRIVE_TIME ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = Global.G_CityId_CityName_Map;
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String,String> map = new HashMap<String, String>();
						map.put("WORK_ASGN_ID", rs.getString("WORK_ASGN_ID"));
						String SERVICE_TYPE = rs.getString("SERVICE_TYPE");
						if("1".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "DSL 宽带上网业务类型";
						}else if("2".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "DSL IPTV业务类型";
						}else if("8".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "DSL VOIP业务类型";
						}else if("5".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "DSL WIFI 业务类型";
						}else if("6".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "DSL 门禁业务类型";
						}else if("99".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "DSL STB IPTV业务类型";
						}else if("97".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "DSL STB IMS业务类型";
						}else if("11".equals(SERVICE_TYPE)||"3".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "LAN 宽带上网业务类型";
						}else if("12".equals(SERVICE_TYPE)||"4".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "LAN IPTV业务类型";
						}else if("13".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "LAN VOIP业务类型";
						}else if("15".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "LAN WIFI 业务类型";
						}else if("16".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "LAN 门禁业务类型";
						}else if("919".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "LAN STB IPTV业务类型";
						}else if("917".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "LAN STB IMS业务类型";
						}else if("21".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "PON 宽带上网业务类型";
						}else if("22".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "PON IPTV业务类型";
						}else if("23".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "PON VOIP业务类型";
						}else if("25".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "PON WIFI业务类型";
						}else if("26".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "PON 门禁业务类型";
						}else if("929".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "PON STB IPTV业务类型";
						}else if("927".equals(SERVICE_TYPE)){
							SERVICE_TYPE = "PON STB IMS业务类型";
						}else{
							SERVICE_TYPE = "非法类型";
						}
						map.put("SERVICE_TYPE", SERVICE_TYPE);
						
						String SERVICE_OPT = rs.getString("SERVICE_OPT");
						if("1".equals(SERVICE_OPT)){
							SERVICE_OPT = "开通";
						}
						else if("2".equals(SERVICE_OPT)){
							SERVICE_OPT = "开通";
						}
						else if("2".equals(SERVICE_OPT)){
							SERVICE_OPT = "删除";
						}
						else if("3".equals(SERVICE_OPT)){
							SERVICE_OPT = "停机";
						}
						else if("4".equals(SERVICE_OPT)){
							SERVICE_OPT = "恢复";
						}
						else if("5".equals(SERVICE_OPT)){
							SERVICE_OPT = "改用户信息";
						}
						else if("6".equals(SERVICE_OPT)){
							SERVICE_OPT = "是更改设备ID";
						}
						else if("7".equals(SERVICE_OPT)){
							SERVICE_OPT = "改登录账号";
						}
						else if("8".equals(SERVICE_OPT)){
							SERVICE_OPT = "移机";
						}
						else if("9".equals(SERVICE_OPT)){
							SERVICE_OPT = "模式更改";
						}
						else if("10".equals(SERVICE_OPT)){
							SERVICE_OPT = "修改密码";
						}
						else if("11".equals(SERVICE_OPT)){
							SERVICE_OPT = "SS H.248 ->IMSH.248割接";
						}
						else if("12".equals(SERVICE_OPT)){
							SERVICE_OPT = "IMS SIP 修改用户鉴权密码";
						}
						else if("16".equals(SERVICE_OPT)){
							SERVICE_OPT = "组播下移";
						}
						else if("20".equals(SERVICE_OPT)){
							SERVICE_OPT = "解除绑定";
						}
						else if("102".equals(SERVICE_OPT)){
							SERVICE_OPT = "更改IPV6工单";
						}
						else if("100015".equals(SERVICE_OPT)){
							SERVICE_OPT = "数图更新工单（仅手工工单使用）";
						}
						map.put("SERVICE_OPT", SERVICE_OPT);
						map.put("ACCOUNT_NAME", rs.getString("ACCOUNT_NAME"));
						map.put("SERIAL_NUMBER", rs.getString("SERIAL_NUMBER"));
						map.put("LOID", rs.getString("LOID"));
						String status = rs.getString("STATUS");
						if("1".equals(status)){
							status = "成功";
						}
						else if("0".equals(status)){
							status = "失败";
						}
						else if("-1".equals(status)){
							status = "未处理";
						}
						else{
							status = "非法状态";
						}
						map.put("STATUS", status);
						/*String arrive = rs.getString("ARRIVE_TIME");
						long arrivel = new DateTimeUtil(arrive).getLongTime();
						String deal = rs.getString("DEAL_TIME");
						long deall = new DateTimeUtil(deal).getLongTime();
						long during = deall - arrivel;
						if(arrivel>deall){
							during = 0;
						}*/
						
						map.put("ARRIVE_TIME", rs.getString("ARRIVE_TIME"));
						map.put("DEAL_TIME", rs.getString("DEAL_TIME"));
						//map.put("DURING_TIME", StringUtil.getStringValue(during));
						
						map.put("CUSTOMER_ID", rs.getString("CUSTOMER_ID"));
						String cityId = rs.getString("AREA_CODE");
						if(StringUtil.IsEmpty(cityId)){
							map.put("cityName", "");
						}else{
							map.put("cityName", cityMap.get(cityId));
						}
						map.put("isHistory", "yes");
						
						return map;
					}
				});
		return list;
	}
	
	
	
	
	
	public int countOperateByHistoryInfo(String starttime, String endtime,
			String username, String ACCOUNT_NAME,String SERIAL_NUMBER,int curPage_splitPage, int num_splitPage) 
	{
		StringBuffer sql = new StringBuffer();
		if(DBUtil.GetDB()==3){
			sql.append("select count(*) from ORDER_HIS where 1=1 ");
		}else{
			sql.append("select count(1) from ORDER_HIS where 1=1 ");
		}
		
		if(!StringUtil.IsEmpty(username)){
			sql.append(" and LOID='").append(username).append("' ");
		}
		
		if (!StringUtil.IsEmpty(ACCOUNT_NAME)){
			sql.append(" and USERID ='").append(ACCOUNT_NAME).append("' ");
		}
		if (!StringUtil.IsEmpty(SERIAL_NUMBER)){
			sql.append(" and SERIALNUMBER ='").append(SERIAL_NUMBER).append("' ");
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
	
	
	public String getOperateMessagePass(String bss_sheet_id){
		logger.debug("OperateByHistoryDAO-->getOperateMessagePass");
		Map<String,String> map = new HashMap<String, String>();
		if(StringUtil.IsEmpty(bss_sheet_id)){
			return "";
		}
		PrepareSQL psql = new PrepareSQL("select ORDER_DETAIL origin_xml_info from ORDER_HIS where ID=?");
		psql.setString(1, bss_sheet_id);
		map = queryForMap(psql.getSQL());
		String pass;
		if(!map.isEmpty()){
			map.put("bss_sheet_id", bss_sheet_id);
			String sheet_context = StringUtil.getStringValue(map.get("origin_xml_info"));
			
			if(sheet_context.indexOf("passwd")!=-1){
				pass = getPass(sheet_context,"<passwd>","</passwd>");
			}
			else{
				pass = getPass(sheet_context,"<auth_password>","</auth_password>");
			}
			return pass;
		}
		return "";
	}
	
	
	
	public Map<String,String> getOperateMessage(String bss_sheet_id){
		logger.debug("OperateByHistoryDAO-->getOperateMessage");
		Map<String,String> map = new HashMap<String, String>();
		if(StringUtil.IsEmpty(bss_sheet_id)){
			return map;
		}
		PrepareSQL psql = new PrepareSQL("select ORDER_DETAIL origin_xml_info from ORDER_HIS where ID=?");
		psql.setString(1, bss_sheet_id);
		map = queryForMap(psql.getSQL());
		if(!map.isEmpty()){
			map.put("bss_sheet_id", bss_sheet_id);
			String sheet_context = StringUtil.getStringValue(map.get("origin_xml_info"));
			
			sheet_context = replacePass(sheet_context,"<passwd>","</passwd>","******");
			sheet_context = replacePass(sheet_context,"<auth_password>","</auth_password>","******");
			map.put("sheet_context", StringUtil.getStringValue(sheet_context));
		}
		return map;
	}
	
	
	public Map<String,String> getOperateMessageBe2015(String bss_sheet_id){
		logger.debug("OperateByHistoryDAO-->getOperateMessageBe2015");
		Map<String,String> map = new HashMap<String, String>();
		if(StringUtil.IsEmpty(bss_sheet_id)){
			return map;
		}
		PrepareSQL psql = new PrepareSQL("select origin_xml_info from WORKTICKETS_HISTORY where work_asgn_id=?");
		psql.setString(1, bss_sheet_id);
		map = queryForMap(psql.getSQL());
		if(!map.isEmpty()){
			map.put("bss_sheet_id", bss_sheet_id);
			String sheet_context = StringUtil.getStringValue(map.get("origin_xml_info"));
			sheet_context = replacePass(sheet_context,"<passwd>","</passwd>","******");
			sheet_context = replacePass(sheet_context,"<auth_password>","</auth_password>","******");
			map.put("sheet_context", StringUtil.getStringValue(sheet_context));
		}
		return map;
	}
	
	public String getOperateMessageBe2015Pass(String bss_sheet_id){
		logger.debug("OperateByHistoryDAO-->getOperateMessageBe2015Pass");
		Map<String,String> map = new HashMap<String, String>();
		if(StringUtil.IsEmpty(bss_sheet_id)){
			return "";
		}
		PrepareSQL psql = new PrepareSQL("select origin_xml_info from WORKTICKETS_HISTORY where work_asgn_id=?");
		psql.setString(1, bss_sheet_id);
		map = queryForMap(psql.getSQL());
		String pass;
		if(!map.isEmpty()){
			map.put("bss_sheet_id", bss_sheet_id);
			String sheet_context = StringUtil.getStringValue(map.get("origin_xml_info"));
			
			if(sheet_context.indexOf("passwd")!=-1){
				pass = getPass(sheet_context,"<passwd>","</passwd>");
			}
			else{
				pass = getPass(sheet_context,"<auth_password>","</auth_password>");
			}
			return pass;
		}
		return "";
	}
	
	
	private String replacePass(String xml,String begin,String end,String repStr){
		int l = xml.indexOf(begin) + begin.length() -1;//<>结尾下标
		int r = xml.indexOf(end);//</>开头下标
		if(l!=-1&&r!=-1)
		xml = xml.substring(0, l+1) + repStr + xml.substring(r);
		return xml;
	}
	
	private String getPass(String xml,String begin,String end){
		int l = xml.indexOf(begin) + begin.length() -1;//<>结尾下标
		int r = xml.indexOf(end);//</>开头下标
		if(l!=-1&&r!=-1)
		return xml.substring(l+1,r);
		else
		return "";
	}
	
	public static void main(String[] args)
	{
		String receive_date = "1512462920";
		System.out.println(StringUtil.getLongValue(receive_date));
	}
	
	private String getResult(String resultid){
		String result = "";
		if(!StringUtil.IsEmpty(resultid)){
			int type = Integer.parseInt(resultid);
			switch (type) {
			case 0:
				result="成功";
				break;
			case 1:
				result="失败";
				break;
			default:
				break;
			}
		}
		return result;
		
	}

}
