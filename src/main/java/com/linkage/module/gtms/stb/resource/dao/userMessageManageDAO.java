
package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2018-1-30
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class userMessageManageDAO extends SuperDAO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(userMessageManageDAO.class);

	private static String writeLogSql = "insert into tab_oper_log("
			+ "acc_oid,acc_login_ip,operationlog_type,operation_time,operation_name"
			+ ",operation_object,operation_content,operation_device"
			+ ",operation_result,result_id) values(?,?,?,?,?,?,?,?,?,?)";


	public List<Map> queryList(String cityId, String starttime, String endtime,
			String servaccount, String platformType, String MAC, String userGroupID,
			String stbaccessStyle,String iptvBindPhone, int curPage_splitPage, int num_splitPage,String stbuser)
	{
		logger.debug("queryList({},{},{},{},{},{},{},{})", new Object[] { cityId, starttime, endtime , servaccount, platformType
				, MAC, userGroupID, stbaccessStyle});
		StringBuffer sql = new StringBuffer();
		sql.append("select a.auth_user, auth_pwd, a.serv_account, a.phone_number, a.pppoe_user, a.pppoe_pwd, a.serv_pwd, a.cpe_mac, a.belong, a.user_grp," +
				"a.city_id, a.stbuptyle, a.addressing_type, a.openUserdate, b.device_id " +
				"from stb_tab_customer a left join stb_tab_gw_device  b on a.customer_id=b.customer_id where 1=1 ");
		if (!StringUtil.IsEmpty(cityId)&&!"-1".equals(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
			.append(")");
				cityIdList = null;
		}
		if (!StringUtil.IsEmpty(servaccount))
		{
			sql.append(" and a.serv_account like'%" + servaccount.replace(" ", "") + "%'");
		}
		if(!StringUtil.IsEmpty(stbuser))
		{
			sql.append(" and a.pppoe_user like'%" + stbuser.replace(" ", "") + "%'");
		}
		if (!StringUtil.IsEmpty(platformType)&&!"-1".equals(platformType))
		{
			sql.append(" and a.belong='" + platformType + "'");
		}
		if (!StringUtil.IsEmpty(MAC))
		{
			sql.append(" and a.cpe_mac like'%" + MAC.replace(" ", "") + "%'");
		}
		if (!StringUtil.IsEmpty(userGroupID)&&!"-1".equals(userGroupID))
		{
			sql.append(" and a.user_grp='" + userGroupID + "'");
		}
		if (!StringUtil.IsEmpty(stbaccessStyle)&&!"-1".equals(stbaccessStyle))
		{
			sql.append(" and a.addressing_type='" + stbaccessStyle + "'");
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and a.openUserdate>=" + starttime + "");
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and a.openUserdate<=" + endtime + "");
		}
		if(!StringUtil.IsEmpty(iptvBindPhone))
		{
			sql.append(" and a.phone_number like'%" + iptvBindPhone.replace(" ", "") + "%'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage+1,
				num_splitPage, new RowMapper()
				{
					@Override
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{String city_name = "";
					String stbaccessStyle="";
					String stbuptyle="";
					Map<String, String> map = new HashMap<String, String>();
					map.put("servaccount", rs.getString("serv_account"));
					map.put("authUser", rs.getString("auth_user"));
					map.put("authPwd", rs.getString("auth_pwd"));
					if(StringUtil.IsEmpty(rs.getString("phone_number")))
					{
						map.put("iptvBindPhone", "");
					}else
					{
						map.put("iptvBindPhone", rs.getString("phone_number"));
					}

					map.put("stbuser", rs.getString("pppoe_user"));
					if(StringUtil.IsEmpty(rs.getString("pppoe_pwd")))
					{
						map.put("stbpwd", "");
					}else
					{
						map.put("stbpwd", rs.getString("pppoe_pwd"));
					}
					if(StringUtil.IsEmpty(rs.getString("serv_pwd")))
					{
						map.put("servpwd", "");
					}else
					{
						map.put("servpwd", rs.getString("serv_pwd"));
					}
					map.put("MAC", rs.getString("cpe_mac"));
					map.put("platformType", getplatformType(rs.getString("belong")));
					map.put("userGroupID", getuserGroupID(rs.getString("user_grp")));
					map.put("groupid", rs.getString("user_grp"));
					map.put("platformid", rs.getString("belong"));
					String cityid = StringUtil.getStringValue(rs.getString("city_id"));
					if (!StringUtil.IsEmpty(cityid))
					{
						city_name = StringUtil.getStringValue(CityDAO.getCityIdCityNameMap()
								.get(cityid));
					}
					map.put("city_name", city_name);
					map.put("cityId", rs.getString("city_id"));
					String stb=StringUtil.getStringValue(rs.getString("stbuptyle"));
					if(stb.equals("1")){
						stbuptyle="FTTH";
					}else if(stb.equals("2"))
					{
						stbuptyle="FTTB";
					}else if(stb.equals("3"))
					{
						stbuptyle="LAN";
					}else if(stb.equals("4"))
					{
						stbuptyle="HGW";
					}
					map.put("stbuptyle", stbuptyle);
					String stbaccess=StringUtil.getStringValue(rs.getString("addressing_type"));
					if(stbaccess.equals("1")){
						stbaccessStyle="DHCP";
					}else if(stbaccess.equals("2"))
					{
						stbaccessStyle="Static";
					}else if(stbaccess.equals("3"))
					{
						stbaccessStyle="PPPoE";
					}else if(stbaccess.equals("4"))
					{
						stbaccessStyle="IPoE";
					}
					map.put("stbaccessStyle", stbaccessStyle);
					map.put("dealDate", transDate(rs.getString("openUserdate")));
					map.put("device_id", rs.getString("device_id"));
					return map;
					}
				});
		return list;
	}
	public List<Map> queryList1(String cityId, String starttime, String endtime,
			String servaccount, String platformType, String MAC, String userGroupID,
			String stbaccessStyle,String iptvBindPhone, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryList({},{},{},{},{},{},{},{})", new Object[] { cityId, starttime, endtime , servaccount, platformType
				, MAC, userGroupID, stbaccessStyle});
		StringBuffer sql = new StringBuffer();
		sql.append("select auth_user, auth_pwd, serv_account, phone_number, pppoe_user, pppoe_pwd, serv_pwd, " +
				"cpe_mac, belong, user_grp, city_id, stbuptyle, addressing_type, opendate " +
				"from stb_tab_customer where 1=1 ");
		if (!StringUtil.IsEmpty(cityId)&&!"-1".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in (").append(StringUtils.weave(cityIdList))
			.append(")");
				cityIdList = null;
		}
		if (!StringUtil.IsEmpty(servaccount))
		{
			sql.append(" and serv_account ='" + servaccount + "'");
		}
		if (!StringUtil.IsEmpty(platformType)&&!"-1".equals(platformType))
		{
			sql.append(" and belong='" + platformType + "'");
		}
		if (!StringUtil.IsEmpty(MAC))
		{
			sql.append(" and cpe_mac like'%" + MAC + "%'");
		}
		if (!StringUtil.IsEmpty(userGroupID)&&!"-1".equals(userGroupID))
		{
			sql.append(" and user_grp='" + userGroupID + "'");
		}
		if (!StringUtil.IsEmpty(stbaccessStyle)&&!"-1".equals(stbaccessStyle))
		{
			sql.append(" and addressing_type='" + stbaccessStyle + "'");
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and opendate>=" + starttime + "");
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and opendate<=" + endtime + "");
		}
		if(!StringUtil.IsEmpty(iptvBindPhone))
		{
			sql.append(" and phone_number ='" + iptvBindPhone + "'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage+1,
				num_splitPage, new RowMapper()
				{
					@Override
					public Object mapRow(ResultSet rs, int arg1) throws SQLException
					{String city_name = "";
					String stbaccessStyle="";
					String stbuptyle="";
//					for (int i = 0; i < rs.size(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("servaccount", rs.getString("serv_account"));
					map.put("authUser", rs.getString("auth_user"));
					map.put("authPwd", rs.getString("auth_pwd"));
					if(StringUtil.IsEmpty(rs.getString("phone_number")))
					{
						map.put("iptvBindPhone", " ");
					}else
					{
						map.put("iptvBindPhone", rs.getString("phone_number"));
					}

					map.put("stbuser", rs.getString("pppoe_user"));
					if(StringUtil.IsEmpty(rs.getString("pppoe_pwd")))
					{
						map.put("stbpwd", " ");
					}else
					{
						map.put("stbpwd", rs.getString("pppoe_pwd"));
					}
					if(StringUtil.IsEmpty(rs.getString("serv_pwd")))
					{
						map.put("servpwd", " ");
					}else
					{
						map.put("servpwd", rs.getString("serv_pwd"));
					}
					map.put("MAC", rs.getString("cpe_mac"));
					map.put("platformType", getplatformType(rs.getString("belong")));
					map.put("belong", rs.getString("belong"));
					map.put("userGroupID", getuserGroupID(rs.getString("user_grp")));
					map.put("groupid", rs.getString("user_grp"));
					map.put("platformid", rs.getString("belong"));
					String cityid = StringUtil.getStringValue(rs.getString("city_id"));
					if (!StringUtil.IsEmpty(cityid))
					{
						city_name = StringUtil.getStringValue(CityDAO.getCityIdCityNameMap()
								.get(cityid));
					}
					map.put("city_name", city_name);
					map.put("cityId", rs.getString("city_id"));
					if (!cityid.endsWith("0")){
						map.put("citynext", cityid);
						map.put("parentcity", cityid.substring(0, cityid.length()-2)+"00");
					}else{
						map.put("parentcity", cityid);
						map.put("citynext", "-1");
					}
					String stb=StringUtil.getStringValue(rs.getString("stbuptyle"));
					if(stb.equals("1")){
						stbuptyle="FTTH";
					}else if(stb.equals("2"))
					{
						stbuptyle="FTTB";
					}else if(stb.equals("3"))
					{
						stbuptyle="LAN";
					}else if(stb.equals("4"))
					{
						stbuptyle="HGW";
					}
					map.put("stbuptyle", stbuptyle);
					map.put("stbuptyle1", rs.getString("stbuptyle"));
					String stbaccess=StringUtil.getStringValue(rs.getString("addressing_type"));
					if(stbaccess.equals("1")){
						stbaccessStyle="DHCP";
					}else if(stbaccess.equals("2"))
					{
						stbaccessStyle="Static";
					}else if(stbaccess.equals("3"))
					{
						stbaccessStyle="PPPoE";
					}else if(stbaccess.equals("4"))
					{
						stbaccessStyle="IPoE";
					}
					map.put("stbaccessStyle", stbaccessStyle);
					map.put("stbaccessStyle1", rs.getString("addressing_type"));
					map.put("dealDate", transDate(rs.getString("opendate")));
					return map;
					}
				});
		return list;
	}
	public List<Map> queryplatform()
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct platform_id,platform_name from stb_serv_platform ");
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		return jt.queryForList(pSQL.getSQL());
	}
	public List<Map> getparentid(String parentid)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select city_name from tab_city where parent_id= '"+parentid+"'");
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		return jt.queryForList(pSQL.getSQL());
	}
	public List<Map> queryCustomerGroup()
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct group_id,group_name from stb_customer_group");
		PrepareSQL pSQL = new PrepareSQL(sql.toString());
		return jt.queryForList(pSQL.getSQL());
	}
	public int getquerypaging(String cityId, String starttime, String endtime,
			String servaccount, String platformType, String MAC, String userGroupID,
			String stbaccessStyle,String iptvBindPhone, int curPage_splitPage, int num_splitPage,String stbuser)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*)  from stb_tab_customer a left join stb_tab_gw_device  b on a.customer_id=b.customer_id where 1=1 ");
		if (!StringUtil.IsEmpty(cityId)&&!"-1".equals(cityId) && !"00".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and a.city_id in (").append(StringUtils.weave(cityIdList))
			.append(")");
				cityIdList = null;
		}
		if(!StringUtil.IsEmpty(stbuser))
		{
			sql.append(" and a.pppoe_user like'%" + stbuser.replace(" ", "") + "%'");
		}
		if (!StringUtil.IsEmpty(servaccount))
		{
			sql.append(" and a.serv_account like'%" + servaccount.replace(" ", "") + "%'");
		}
		if (!StringUtil.IsEmpty(platformType)&&!"-1".equals(platformType))
		{
			sql.append(" and a.belong='" + platformType + "'");
		}
		if (!StringUtil.IsEmpty(MAC))
		{
			sql.append(" and a.cpe_mac like'%" + MAC.replace(" ", "") + "%'");
		}
		if (!StringUtil.IsEmpty(userGroupID)&&!"-1".equals(userGroupID))
		{
			sql.append(" and a.user_grp='" + userGroupID + "'");
		}
		if (!StringUtil.IsEmpty(stbaccessStyle)&&!"-1".equals(stbaccessStyle))
		{
			sql.append(" and a.addressing_type='" + stbaccessStyle + "'");
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and a.openUserdate>=" + starttime + "");
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and a.openUserdate<=" + endtime + "");
		}
		if(!StringUtil.IsEmpty(iptvBindPhone))
		{
			sql.append(" and a.phone_number like'%" + iptvBindPhone.replace(" ", "") + "%'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	public int getquerypaging1(String cityId, String starttime, String endtime,
			String servaccount, String platformType, String MAC, String userGroupID,
			String stbaccessStyle,String iptvBindPhone, int curPage_splitPage, int num_splitPage)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from stb_tab_customer where 1=1 ");
		if (!StringUtil.IsEmpty(cityId)&&!"-1".equals(cityId))
		{
			List cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and city_id in (").append(StringUtils.weave(cityIdList))
			.append(")");
				cityIdList = null;
		}
		if (!StringUtil.IsEmpty(servaccount))
		{
			sql.append(" and serv_account ='" + servaccount + "'");
		}
		if (!StringUtil.IsEmpty(platformType)&&!"-1".equals(platformType))
		{
			sql.append(" and belong='" + platformType + "'");
		}
		if (!StringUtil.IsEmpty(MAC))
		{
			sql.append(" and cpe_mac like'%" + MAC + "%'");
		}
		if (!StringUtil.IsEmpty(userGroupID)&&!"-1".equals(userGroupID))
		{
			sql.append(" and user_grp='" + userGroupID + "'");
		}
		if (!StringUtil.IsEmpty(stbaccessStyle)&&!"-1".equals(stbaccessStyle))
		{
			sql.append(" and addressing_type='" + stbaccessStyle + "'");
		}
		if (!StringUtil.IsEmpty(starttime))
		{
			sql.append(" and opendate>=" + starttime + "");
		}
		if (!StringUtil.IsEmpty(endtime))
		{
			sql.append(" and opendate<=" + endtime + "");
		}
		if(!StringUtil.IsEmpty(iptvBindPhone))
		{
			sql.append(" and phone_number = '" + iptvBindPhone + "'");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	private String getuserGroupID(String userGroupID)
	{
		List<Map> list=new ArrayList<Map>();
		Map<String,String> map=new HashMap<String, String>();
		list=queryCustomerGroup();
		for(int i=0;i<list.size();i++)
		{
			map.put(String.valueOf(list.get(i).get("group_id")), String.valueOf(list.get(i).get("group_name")));
		}
		String str=map.get(userGroupID);
		return str;
	}
public List<Map> parentid(String city_id)
{
	StringBuffer sql = new StringBuffer();
	sql.append("select  parent_id from tab_city where   city_id="+city_id);
	PrepareSQL pSQL = new PrepareSQL(sql.toString());
	return jt.queryForList(pSQL.getSQL());
}
	/**
	 * 平台类型
	 *
	 * @param platformType
	 * @return
	 */
	private String getplatformType(String platformType)
	{

		List<Map> list=new ArrayList<Map>();
		Map<String,String> map=new HashMap<String, String>();
		list=queryplatform();
		for(int i=0;i<list.size();i++)
		{
			map.put(String.valueOf(list.get(i).get("platform_id")), String.valueOf(list.get(i).get("platform_name")));
		}
		String str=map.get(platformType);
		return str;
	}

	private static final String transDate(Object seconds)
	{
		if (seconds != null)
		{
			try
			{
				DateTimeUtil dt = new DateTimeUtil(
						Long.parseLong(seconds.toString()) * 1000);
				return dt.getLongDate();
			}
			catch (NumberFormatException e)
			{
				logger.error(e.getMessage(), e);
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}

	/**
	 * 获取用户信息
	 */
	public Map<String,String> getCustomerInfo(String servaccount) {
		PrepareSQL psql = new PrepareSQL();
		psql.append("select belong,user_grp,phone_number,city_id,pppoe_user,");
		psql.append("pppoe_pwd,cpe_mac,serv_pwd from stb_tab_customer ");
		psql.append("where serv_account=? ");
		psql.setString(1, servaccount);

		return DBOperation.getRecord(psql.getSQL());
	}

	/**
	 * 记录新增信息日志
	 * */
	public void addLogInsert(Map<String,String> customerMap,long user_id, String user_ip, String ajax,
			String platformType, String userGroupID, String iptvBindPhone,
			String city_id, String stbuser, String stbpwd, String mac,
			String servaccount, String servpwd)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(writeLogSql);
		psql.setLong(1,user_id);
		psql.setString(2,user_ip);
		psql.setInt(3,1);
		psql.setLong(4,System.currentTimeMillis()/1000L);
		psql.setString(5,"5");
		psql.setString(6,"WEB");

		StringBuffer sb=new StringBuffer();

		if(customerMap==null || customerMap.isEmpty())
		{
			sb.append("新增用户["+servaccount+"]信息：[");
			if(!StringUtil.IsEmpty(platformType)){
				sb.append("用户平台："+platformType);
			}
			if(!StringUtil.IsEmpty(userGroupID)){
				sb.append("，用户分组："+userGroupID);
			}
			if(!StringUtil.IsEmpty(iptvBindPhone)){
				sb.append("，手机号："+iptvBindPhone);
			}
			if(!StringUtil.IsEmpty(city_id)){
				sb.append("，属地："+city_id);
			}
			if(!StringUtil.IsEmpty(stbuser)){
				sb.append("，PPPoE账号："+stbuser);
			}
			if(!StringUtil.IsEmpty(stbpwd)){
				sb.append("，PPPoE密码："+stbpwd);
			}
			if(!StringUtil.IsEmpty(mac)){
				sb.append("，cpe_mac："+mac.replaceAll(":","").toUpperCase());
			}
			if(!StringUtil.IsEmpty(servaccount)){
				sb.append("，业务账号："+servaccount);
			}
			if(!StringUtil.IsEmpty(servpwd)){
				sb.append("，业务账号密码："+servpwd);
			}
		}
		else
		{
			sb.append("修改用户["+servaccount+"]信息：[");
			sb=combinStr(sb,"用户平台",customerMap.get("belong"),platformType);
			sb=combinStr(sb,"用户分组",customerMap.get("user_grp"),userGroupID);
			sb=combinStr(sb,"手机号",customerMap.get("phone_number"),iptvBindPhone);
			sb=combinStr(sb,"属地","00".equals(customerMap.get("city_id"))?"0010000":customerMap.get("city_id"),
												"00".equals(city_id)?"0010000":city_id);
			sb=combinStr(sb,"PPPoE账号",customerMap.get("pppoe_user"),stbuser);
			sb=combinStr(sb,"PPPoE密码",customerMap.get("pppoe_pwd"),stbpwd);
			sb=combinStr(sb,"cpe_mac",customerMap.get("cpe_mac"),mac);
			sb=combinStr(sb,"业务账号密码",customerMap.get("serv_pwd"),servpwd);
		}
		sb.append("]");


		psql.setString(7,sb.toString());
		psql.setString(8,"Web");
		psql.setString(9,"成功".equals(ajax)?"成功":"失败");
		psql.setInt(10,1);
		DBOperation.executeUpdate(psql.getSQL());
	}

	/**
	 * 组装参数
	 */
	public StringBuffer combinStr(StringBuffer sb,String str_name,String old_data,String new_data){
		if(sb.toString().indexOf("->")!=-1){
			sb.append("，");
		}

		if(!StringUtil.IsEmpty(new_data)){
			if(StringUtil.IsEmpty(old_data)){
				sb.append(str_name+"： ->"+new_data);
			}else if(!new_data.equals(old_data)){
				sb.append(str_name+"："+old_data+"->"+new_data);
			}else{
				sb.deleteCharAt(sb.toString().length()-1);
			}
		}else{
			if(!StringUtil.IsEmpty(old_data)){
				sb.append(str_name+"："+old_data+"->  ");
			}else{
				sb.deleteCharAt(sb.toString().length()-1);
			}
		}

		return sb;
	}

	/**
	 * 记录修改信息日志
	 * */
	public void addLogUpdate(Map<String,String> customerMap,long user_id, String user_ip, String ajax,
			String authUser, String authPwd, String platformType,
			String userGroupID, String iptvBindPhone, String city_id,
			String stbuser, String stbpwd, String mac, String stbaccessStyle,
			String servaccount, String servpwd, String stbuptyle1) {
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(writeLogSql);
		psql.setLong(1,user_id);
		psql.setString(2,user_ip);
		psql.setInt(3,1);
		psql.setLong(4,System.currentTimeMillis()/1000L);
		psql.setString(5,"5");
		psql.setString(6,"WEB");

		StringBuffer sb=new StringBuffer("修改用户["+servaccount+"]信息：[");
		if(customerMap!=null && !customerMap.isEmpty()){
			sb=combinStr(sb,"用户平台",customerMap.get("belong"),platformType);
			sb=combinStr(sb,"用户分组",customerMap.get("user_grp"),userGroupID);
			sb=combinStr(sb,"手机号",customerMap.get("phone_number"),iptvBindPhone);
			sb=combinStr(sb,"属地","00".equals(customerMap.get("city_id"))?"0010000":customerMap.get("city_id"),
												"00".equals(city_id)?"0010000":city_id);
			sb=combinStr(sb,"PPPoE账号",customerMap.get("pppoe_user"),stbuser);
			sb=combinStr(sb,"PPPoE密码",customerMap.get("pppoe_pwd"),stbpwd);
			sb=combinStr(sb,"cpe_mac",customerMap.get("cpe_mac"),mac);
			sb=combinStr(sb,"业务账号密码",customerMap.get("serv_pwd"),servpwd);
		}

		sb.append("]");

		psql.setString(7,sb.toString());
		psql.setString(8,"Web");
		psql.setString(9,"成功".equals(ajax)?"成功":"失败");
		psql.setInt(10,1);
		DBOperation.executeUpdate(psql.getSQL());
	}

	/**
	 * 记录删除信息日志
	 * */
	public void addLogDelete(Map<String, String> customerMap, long user_id, String user_ip, String ajax,
			String servaccount)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(writeLogSql);
		psql.setLong(1,user_id);
		psql.setString(2,user_ip);
		psql.setInt(3,1);
		psql.setLong(4,System.currentTimeMillis()/1000L);
		psql.setString(5,"5");
		psql.setString(6,"WEB");

		//belong,user_grp,phone_number,city_id,pppoe_user,pppoe_pwd,cpe_mac,serv_account,serv_pwd
		//platformType,userGroupID,iptvBindPhone,cityId,stbuser,stbpwd,mac,servaccount,servpwd

		StringBuffer sb=new StringBuffer();
		sb.append("删除用户["+servaccount+"]信息：[");
		sb.append("属地："+customerMap.get("city_id"));
		sb.append("，cpe_mac："+customerMap.get("cpe_mac"));
		sb.append("，PPPoE账号："+customerMap.get("pppoe_pwd"));
		sb.append("]");

		psql.setString(7,sb.toString());
		psql.setString(8,"Web");
		psql.setString(9,"成功".equals(ajax)?"成功":"失败");
		psql.setInt(10,1);
		DBOperation.executeUpdate(psql.getSQL());
	}
	public String cleanAccountPwd(String servaccount) {
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL("update stb_tab_customer set pppoe_user = null,pppoe_pwd = null where serv_account ='" + servaccount + "'");
		int executeUpdate = DBOperation.executeUpdate(psql.getSQL());

		if(1==executeUpdate){
			return "成功";
		}
		return "根据"+servaccount+"清空接入账号和密码未更新到数据";
	}
	public void addLogcleanAccountPwd(Map<String, String> customerMap,
			long user_id, String user_ip, String ajax, String servaccount) {
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(writeLogSql);
		psql.setLong(1,user_id);
		psql.setString(2,user_ip);
		psql.setInt(3,1);
		psql.setLong(4,System.currentTimeMillis()/1000L);
		psql.setString(5,"5");
		psql.setString(6,"WEB");

		StringBuffer sb=new StringBuffer("清空用户["+servaccount+"]信息：[");
		if(customerMap!=null && !customerMap.isEmpty()){
			sb=combinStr(sb,"PPPoE账号",StringUtil.IsEmpty(customerMap.get("pppoe_user"))?"空":customerMap.get("pppoe_user"),"空");
			sb=combinStr(sb,"PPPoE密码",StringUtil.IsEmpty(customerMap.get("pppoe_pwd"))?"空":customerMap.get("pppoe_pwd"),"空");
		}
		sb.append("]");
		psql.setString(7,sb.toString());
		psql.setString(8,"Web");
		psql.setString(9,"成功".equals(ajax)?"成功":"失败");
		psql.setInt(10,1);
		DBOperation.executeUpdate(psql.getSQL());
	}

}
