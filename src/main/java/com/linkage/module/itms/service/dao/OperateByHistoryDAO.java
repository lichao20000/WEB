package com.linkage.module.itms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

public class OperateByHistoryDAO extends SuperDAO  {
	
	// 日志记录
		private static Logger logger = LoggerFactory
				.getLogger(OperateByHistoryDAO.class);
		
		private Map<String, String> cityMap = null;
		
		private Map<String,String> typeMap = new HashMap<String, String>();
		private Map<String,String>  servMap = new HashMap<String, String>();
		public OperateByHistoryDAO(){
			String instAreaName = Global.instAreaShortName;
			if (Global.CQDX.equals(instAreaName)) {
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
				typeMap.put("17", "修改宽带上网(修改终端连接数)");
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
				servMap.put("23", "WIFI业务");
				servMap.put("25", "机顶盒业务");
			}
			
			if (Global.SXLT.equals(instAreaName)) {
				servMap.put("20", "用户");
			}
		}
		
	
	public List<Map> getOperateByHistoryInfo(String isGl,String starttime, String endtime,
			String username, String city_id, String servType, String resultType,
			String resultId, String usernameType,String gw_type, int curPage_splitPage, int num_splitPage) {
		StringBuffer sql = new StringBuffer();
		String instAreaName = Global.instAreaShortName;
		//安徽联通新增状态字段 区分作废单和正常工单 AHLT_RMS-REQ-20200520-DXL-001（RMS页面优化相关功能需求)
		if(instAreaName.equals(Global.AHLT)){
			sql.append("select bss_sheet_id,username,city_id, receive_date, product_spec_id, type,result,returnt_context,remark,servUsername,status from tab_bss_sheet where 1=1 ");
		}else {
			sql.append("select bss_sheet_id,username,city_id, receive_date, product_spec_id, type,result,returnt_context,remark,servUsername from tab_bss_sheet where 1=1 ");
		}
		if("1".equals(usernameType)){
			if(!StringUtil.IsEmpty(username)){
				if (Global.SXLT.equals(instAreaName) && username.length()>=6) {
					sql.append(" and username like '%").append(username).append("' ");
				}
				else{
					sql.append(" and username='").append(username).append("' ");
				}
				
			}
		}else if("2".equals(usernameType)){
			if(!StringUtil.IsEmpty(username)){
				sql.append(" and servUsername='").append(username).append("' ");
			}
		}else if("3".equals(usernameType)){
			if(!StringUtil.IsEmpty(username)){
				sql.append(" and bss_sheet_id='").append(username).append("' ");
			}
		}
		
		if (!StringUtil.IsEmpty(city_id) &&  !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		
//		if(!StringUtil.IsEmpty(servType) && !"-1".equals(servType)){
//			sql.append(" and product_spec_id!=").append(servType);
//		}
		if("1".equals(isGl)){
			sql.append(" and product_spec_id!=").append(servType);
		}
		else if(!StringUtil.IsEmpty(servType) && !"-1".equals(servType))
		{
			sql.append(" and product_spec_id=").append(servType);
		}
		
		if(!StringUtil.IsEmpty(resultType) && !"-1".equals(resultType)){
			sql.append(" and type=").append(resultType);
		}
		if(!StringUtil.IsEmpty(resultId) && !"-1".equals(resultId)){
			sql.append(" and result=").append(resultId);
		}
		if(!StringUtil.IsEmpty(starttime)){
			sql.append(" and receive_date>=").append(starttime);
		}
		if(!StringUtil.IsEmpty(endtime)){
			sql.append(" and receive_date<=").append(endtime);
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and gw_type=").append(gw_type);
		}
		
		//机顶盒的不展示
		if (Global.SXLT.equals(instAreaName)){
			sql.append(" and product_spec_id!=25");
		}
		
		sql.append(" order by  receive_date  desc ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = Global.G_CityId_CityName_Map;
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String,String> map = new HashMap<String, String>();
						map.put("bss_sheet_id", rs.getString("bss_sheet_id"));
						map.put("username", rs.getString("username"));
						String cityId = rs.getString("city_id");
						if(StringUtil.IsEmpty(cityId)){
							map.put("cityName", "");
						}else{
							map.put("cityName", cityMap.get(cityId));
						}
						String instAreaName = Global.instAreaShortName;
						
						try
						{
							long receive_date = StringUtil.getLongValue(rs.getString("receive_date"));
							DateTimeUtil dt = new DateTimeUtil(receive_date*1000);
							map.put("receive_date", dt.getLongDate());
						}
						catch (NumberFormatException e)
						{
							map.put("receive_date", "");
						}
						catch (Exception e)
						{
							map.put("receive_date", "");
						}
						
						if (Global.CQDX.equals(instAreaName)) {
							map.put("receive_date_long", rs.getString("receive_date"));
						}
						
						String servType = StringUtil.getStringValue(rs.getString("product_spec_id"));
						
						if(StringUtil.IsEmpty(servType)){
							map.put("servType", "");
						}else{
							map.put("servType", servMap.get(servType));
						}
						
						String resultType = StringUtil.getStringValue(rs.getString("type"));
						if(StringUtil.IsEmpty(resultType)){
							map.put("resultType", "");
						}else{
							map.put("resultType", typeMap.get(resultType));
						}
						
						String resultId = StringUtil.getStringValue(rs.getString("result"));
						
						if(StringUtil.IsEmpty(resultId)){
							map.put("resultId", "");
						}else{
							if("1".equals(resultId)){
								map.put("color", "red");
							}else{
								map.put("color", "");
							}
							map.put("resultId", getResult(resultId));
						}
						
						if (Global.CQDX.equals(instAreaName)) {
							if(StringUtil.IsEmpty(map.get("resultId"))){
								map.put("resultId", "成功");
							}
						}
						map.put("returnt_context", rs.getString("returnt_context"));
						
						//重庆展示错误原因
						if (Global.CQDX.equals(instAreaName)&&"1".equals(resultId)) {
							String returnt_context_short = rs.getString("returnt_context");
							try{
								int ix_begin = returnt_context_short.indexOf("<errcomment>") + 12;
								int ix_end = returnt_context_short.indexOf("</errcomment>");
								returnt_context_short = returnt_context_short.substring(ix_begin, ix_end);
								map.put("returnt_context_short", returnt_context_short);
							}
							catch (Exception e){
								map.put("returnt_context_short", "");
							}
						}
						else if(Global.CQDX.equals(instAreaName)){
							map.put("returnt_context_short", "");
						}
						map.put("remark",rs.getString("remark"));
						map.put("servUsername", rs.getString("servUsername"));
						if(Global.AHLT.equals(instAreaName)){
							//安徽联通 增加状态字段表示 新装、作废、拆机  目前安徽联通工单type只有开户和销户类型
							String status = rs.getString("status");
							if(status.equals("-1")){
								map.put("statusDesc","作废");
							}else {
								map.put("statusDesc",resultType.equals("1") ? "新装" : "拆机");
							}
						}
						return map;
					}
				});
		return list;
	}

	
	public List<Map> getOperateByHistoryInfoStb(String isGl,String starttime, String endtime,
			String username, String city_id, String servType, String resultType,
			String resultId, String usernameType,String gw_type, int curPage_splitPage, int num_splitPage) {
		StringBuffer sql = new StringBuffer();
		String instAreaName = Global.instAreaShortName;
		//安徽联通新增状态字段 区分作废单和正常工单 AHLT_RMS-REQ-20200520-DXL-001（RMS页面优化相关功能需求)
		if(instAreaName.equals(Global.AHLT)){
			sql.append("select bss_sheet_id,username,city_id, receive_date, product_spec_id, type,result,returnt_context,remark,servUsername,status from tab_bss_sheet where 1=1 ");
		}else {
			sql.append("select bss_sheet_id,username,city_id, receive_date, product_spec_id, type,result,returnt_context,remark,servUsername from tab_bss_sheet where 1=1 ");
		}
		if("1".equals(usernameType)){
			if(!StringUtil.IsEmpty(username)){
				if (Global.SXLT.equals(instAreaName) && username.length()>=6) {
					sql.append(" and username like '%").append(username).append("' ");
				}
				else{
					sql.append(" and username='").append(username).append("' ");
				}
				
			}
		}else if("2".equals(usernameType)){
			if(!StringUtil.IsEmpty(username)){
				sql.append(" and servUsername='").append(username).append("' ");
			}
		}else if("3".equals(usernameType)){
			if(!StringUtil.IsEmpty(username)){
				sql.append(" and bss_sheet_id='").append(username).append("' ");
			}
		}
		
		if (!StringUtil.IsEmpty(city_id) &&  !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		
//		if(!StringUtil.IsEmpty(servType) && !"-1".equals(servType)){
//			sql.append(" and product_spec_id!=").append(servType);
//		}
		if("1".equals(isGl)){
			sql.append(" and product_spec_id!=").append(servType);
		}
		else if(!StringUtil.IsEmpty(servType) && !"-1".equals(servType))
		{
			sql.append(" and product_spec_id=").append(servType);
		}
		
		if(!StringUtil.IsEmpty(resultType) && !"-1".equals(resultType)){
			sql.append(" and type=").append(resultType);
		}
		if(!StringUtil.IsEmpty(resultId) && !"-1".equals(resultId)){
			sql.append(" and result=").append(resultId);
		}
		if(!StringUtil.IsEmpty(starttime)){
			sql.append(" and receive_date>=").append(starttime);
		}
		if(!StringUtil.IsEmpty(endtime)){
			sql.append(" and receive_date<=").append(endtime);
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and gw_type=").append(gw_type);
		}
		
		//机顶盒
		sql.append(" and product_spec_id=25");
		
		sql.append(" order by  receive_date  desc ");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		cityMap = Global.G_CityId_CityName_Map;
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper() {
					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						Map<String,String> map = new HashMap<String, String>();
						map.put("bss_sheet_id", rs.getString("bss_sheet_id"));
						map.put("username", rs.getString("username"));
						String cityId = rs.getString("city_id");
						if(StringUtil.IsEmpty(cityId)){
							map.put("cityName", "");
						}else{
							map.put("cityName", cityMap.get(cityId));
						}
						String instAreaName = Global.instAreaShortName;
						
						try
						{
							long receive_date = StringUtil.getLongValue(rs.getString("receive_date"));
							DateTimeUtil dt = new DateTimeUtil(receive_date*1000);
							map.put("receive_date", dt.getLongDate());
						}
						catch (NumberFormatException e)
						{
							map.put("receive_date", "");
						}
						catch (Exception e)
						{
							map.put("receive_date", "");
						}
						
						if (Global.CQDX.equals(instAreaName)) {
							map.put("receive_date_long", rs.getString("receive_date"));
						}
						
						String servType = StringUtil.getStringValue(rs.getString("product_spec_id"));
						
						if(StringUtil.IsEmpty(servType)){
							map.put("servType", "");
						}else{
							map.put("servType", servMap.get(servType));
						}
						
						String resultType = StringUtil.getStringValue(rs.getString("type"));
						if(StringUtil.IsEmpty(resultType)){
							map.put("resultType", "");
						}else{
							map.put("resultType", typeMap.get(resultType));
						}
						
						String resultId = StringUtil.getStringValue(rs.getString("result"));
						
						if(StringUtil.IsEmpty(resultId)){
							map.put("resultId", "");
						}else{
							if("1".equals(resultId)){
								map.put("color", "red");
							}else{
								map.put("color", "");
							}
							map.put("resultId", getResult(resultId));
						}
						
						if (Global.CQDX.equals(instAreaName)) {
							if(StringUtil.IsEmpty(map.get("resultId"))){
								map.put("resultId", "成功");
							}
						}
						map.put("returnt_context", rs.getString("returnt_context"));
						
						//重庆展示错误原因
						if (Global.CQDX.equals(instAreaName)&&"1".equals(resultId)) {
							String returnt_context_short = rs.getString("returnt_context");
							try{
								int ix_begin = returnt_context_short.indexOf("<errcomment>") + 12;
								int ix_end = returnt_context_short.indexOf("</errcomment>");
								returnt_context_short = returnt_context_short.substring(ix_begin, ix_end);
								map.put("returnt_context_short", returnt_context_short);
							}
							catch (Exception e){
								map.put("returnt_context_short", "");
							}
						}
						else if(Global.CQDX.equals(instAreaName)){
							map.put("returnt_context_short", "");
						}
						map.put("remark",rs.getString("remark"));
						map.put("servUsername", rs.getString("servUsername"));
						if(Global.AHLT.equals(instAreaName)){
							//安徽联通 增加状态字段表示 新装、作废、拆机  目前安徽联通工单type只有开户和销户类型
							String status = rs.getString("status");
							if(status.equals("-1")){
								map.put("statusDesc","作废");
							}else {
								map.put("statusDesc",resultType.equals("1") ? "新装" : "拆机");
							}
						}
						return map;
					}
				});
		return list;
	}


	public int countOperateByHistoryInfo(String isGl, String starttime, String endtime,
			String username, String city_id, String servType, String resultType,
			String resultId, String usernameType, String gw_type,int curPage_splitPage, int num_splitPage) {
		String instAreaName = Global.instAreaShortName;
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from tab_bss_sheet where 1=1 ");
		if("1".equals(usernameType)){
			if(!StringUtil.IsEmpty(username)){
				if (Global.SXLT.equals(instAreaName) && username.length()>=6) {
					sql.append(" and username like '%").append(username).append("' ");
				}
				else{
					sql.append(" and username='").append(username).append("' ");
				}
			}
		}else if("2".equals(usernameType)){
			if(!StringUtil.IsEmpty(username)){
				sql.append(" and servUsername='").append(username).append("' ");
			}
		}else if("3".equals(usernameType)){
			if(!StringUtil.IsEmpty(username)){
				sql.append(" and bss_sheet_id='").append(username).append("' ");
			}
		}
		
		if (!StringUtil.IsEmpty(city_id) &&  !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		
//		if(!StringUtil.IsEmpty(servType) && !"-1".equals(servType)){
//			sql.append(" and product_spec_id!=").append(servType);
//		}
		if("1".equals(isGl)){
			sql.append(" and product_spec_id!=").append(servType);
		}
		else if(!StringUtil.IsEmpty(servType) && !"-1".equals(servType))
		{
			sql.append(" and product_spec_id=").append(servType);
		}
		
		if(!StringUtil.IsEmpty(resultType) && !"-1".equals(resultType)){
			sql.append(" and type=").append(resultType);
		}
		if(!StringUtil.IsEmpty(resultId) && !"-1".equals(resultId)){
			sql.append(" and result=").append(resultId);
		}
		if(!StringUtil.IsEmpty(starttime)){
			sql.append(" and receive_date>=").append(starttime);
		}
		if(!StringUtil.IsEmpty(endtime)){
			sql.append(" and receive_date<=").append(endtime);
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and gw_type=").append(gw_type);
		}
		
		//机顶盒的不展示
		if (Global.SXLT.equals(instAreaName)){
			sql.append(" and product_spec_id!=25");
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
	
	
	public int countOperateByHistoryInfoStb(String isGl, String starttime, String endtime,
			String username, String city_id, String servType, String resultType,
			String resultId, String usernameType, String gw_type,int curPage_splitPage, int num_splitPage) {
		String instAreaName = Global.instAreaShortName;
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from tab_bss_sheet where 1=1 ");
		if("1".equals(usernameType)){
			if(!StringUtil.IsEmpty(username)){
				if (Global.SXLT.equals(instAreaName) && username.length()>=6) {
					sql.append(" and username like '%").append(username).append("' ");
				}
				else{
					sql.append(" and username='").append(username).append("' ");
				}
			}
		}else if("2".equals(usernameType)){
			if(!StringUtil.IsEmpty(username)){
				sql.append(" and servUsername='").append(username).append("' ");
			}
		}else if("3".equals(usernameType)){
			if(!StringUtil.IsEmpty(username)){
				sql.append(" and bss_sheet_id='").append(username).append("' ");
			}
		}
		
		if (!StringUtil.IsEmpty(city_id) &&  !"-1".equals(city_id))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(city_id);
			sql.append(" and city_id in (").append(StringUtils.weave(cityIdList))
					.append(")");
			cityIdList = null;
		}
		
//		if(!StringUtil.IsEmpty(servType) && !"-1".equals(servType)){
//			sql.append(" and product_spec_id!=").append(servType);
//		}
		if("1".equals(isGl)){
			sql.append(" and product_spec_id!=").append(servType);
		}
		else if(!StringUtil.IsEmpty(servType) && !"-1".equals(servType))
		{
			sql.append(" and product_spec_id=").append(servType);
		}
		
		if(!StringUtil.IsEmpty(resultType) && !"-1".equals(resultType)){
			sql.append(" and type=").append(resultType);
		}
		if(!StringUtil.IsEmpty(resultId) && !"-1".equals(resultId)){
			sql.append(" and result=").append(resultId);
		}
		if(!StringUtil.IsEmpty(starttime)){
			sql.append(" and receive_date>=").append(starttime);
		}
		if(!StringUtil.IsEmpty(endtime)){
			sql.append(" and receive_date<=").append(endtime);
		}
		if(!StringUtil.IsEmpty(gw_type)){
			sql.append(" and gw_type=").append(gw_type);
		}
		
		//机顶盒
		sql.append(" and product_spec_id=25");
		
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
	
	
	public Map<String,String> getOperateMessage(String bss_sheet_id,String receive_date, String gw_type){
		logger.debug("OperateByHistoryDAO-->getOperateMessage");
		Map<String,String> map = new HashMap<String, String>();
		if(StringUtil.IsEmpty(bss_sheet_id)){
			return map;
		}
		PrepareSQL psql = new PrepareSQL("select bss_sheet_id,product_spec_id,result,sheet_context,returnt_context from tab_bss_sheet where bss_sheet_id=? and gw_type=?");
		String instAreaName = Global.instAreaShortName;
		if (Global.CQDX.equals(instAreaName)) {
			psql.append(" and receive_date=? and rownum<2");
			psql.setLong(3, StringUtil.getLongValue(receive_date));
		}
		psql.setString(1, bss_sheet_id);
		psql.setInt(2, StringUtil.getIntegerValue(gw_type));
		map = queryForMap(psql.getSQL());
		if(!map.isEmpty()){
			map.put("bss_sheet_id", StringUtil.getStringValue(map.get("bss_sheet_id")));
			map.put("servType", servMap.get(StringUtil.getStringValue(map.get("product_spec_id"))));
			map.put("resultId", getResult(StringUtil.getStringValue(map.get("result"))));
			String sheet_context = StringUtil.getStringValue(map.get("sheet_context"));
			
			sheet_context = replacePass(sheet_context,"<passwd>","</passwd>");
			sheet_context = replacePass(sheet_context,"<auth_password>","</auth_password>");
			map.put("sheet_context", sheet_context);
			
			map.put("returnt_context",StringUtil.getStringValue(map.get("returnt_context")));
		}
		return map;
	}
	
	
	private String replacePass(String xml,String begin,String end){
		int l = xml.indexOf(begin) + begin.length() -1;//<>结尾下标
		int r = xml.indexOf(end);//</>开头下标
		if(l!=-1&&r!=-1)
		xml = xml.substring(0, l+1) + "******" + xml.substring(r);
		return xml;
	}
	/*public static void main(String[] args)
	{
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><itms_97_interface><service_type>22</service_type><service_opt>1</service_opt><result><work_asgn_id>web15165973952309</work_asgn_id><pass>1234</pass><value>01</value><errcomment>操作成功</errcomment></result></itms_97_interface>";
		
		System.out.println(replacePass(xml,"<pass>","</pass>"));
	}*/
	
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
