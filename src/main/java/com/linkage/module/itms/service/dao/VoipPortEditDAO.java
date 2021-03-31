package com.linkage.module.itms.service.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

public class VoipPortEditDAO extends SuperDAO
{
  private static Logger logger = LoggerFactory.getLogger(VoipPortEditDAO.class);

	public List<Map> getAllVoipInfo(int curPage_splitPage, int num_splitPage,String dev_sn,String loid,String gw_type)
	{
		String tabName = "tab_voip_serv_param";
		String hgwName = "tab_hgwcustomer";
		String servName = "hgwcust_serv_info";
		if ("2".equals(gw_type))
		{
			tabName = "tab_egw_voip_serv_param";
			hgwName = "tab_egwcustomer";
			servName = "egwcust_serv_info";
		}
		
		PrepareSQL psql = new PrepareSQL();
		/**	if(DBUtil.GetDB()==3){
			//TODO wait
		}else{
			
		}*/
		psql.append("select a.device_serialnumber,b.username loid,b.user_id,d.voip_phone,d.voip_port,d.line_id ");
		psql.append("from tab_gw_device a," +hgwName+ " b," +servName+ " c ," +tabName+ " d ");
		if(DBUtil.GetDB()==3){
			psql.append("where a.customer_id=cast(b.user_id as char) ");
		}else{
			psql.append("where a.customer_id=to_char(b.user_id) ");
		}
		psql.append("and b.user_id=c.user_id and c.serv_type_id=14 ");
		psql.append("and c.user_id=d.user_id and a.gw_type=" + gw_type + " ");
		
		if(!StringUtil.IsEmpty(dev_sn)){
			psql.append("and a.device_serialnumber='" + dev_sn.trim() + "' ");
		}
		
		if(!StringUtil.IsEmpty(loid)){
			psql.append("and b.username='" + loid.trim() + "' ");
		}
		
		List<Map> list = querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage
				+ 1, num_splitPage, new RowMapper()
		{
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();
				
				map.put("device_serialnumber", StringUtil.getStringValue(rs.getString("device_serialnumber")));
				map.put("loid", StringUtil.getStringValue(rs.getString("loid")));
				map.put("voip_phone", StringUtil.getStringValue(rs.getString("voip_phone")));
				map.put("voip_port", StringUtil.getStringValue(rs.getString("voip_port")));
				map.put("line_id", StringUtil.getStringValue(rs.getString("line_id")));
				map.put("userId", StringUtil.getStringValue(rs.getString("user_id")));
				return map;
			}
		});
		return list;
	}
	
	public int getAllVoipInfoCount(String dev_sn,String loid,String gw_type,int curPage_splitPage, int num_splitPage)
	{
		String tabName = "tab_voip_serv_param";
		String hgwName = "tab_hgwcustomer";
		String servName = "hgwcust_serv_info";
		if ("2".equals(gw_type))
		{
			tabName = "tab_egw_voip_serv_param";
			hgwName = "tab_egwcustomer";
			servName = "egwcust_serv_info";
		}
		
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			//TODO wait
			psql.append("select count(*) ");
		}else{
			psql.append("select count(1) ");
		}
		
		psql.append("from tab_gw_device a," +hgwName+ " b," +servName+ " c ," +tabName+ " d ");
		if(DBUtil.GetDB()==3){
			psql.append("where a.customer_id=cast(b.user_id as signed) ");
		}else{
			psql.append("where a.customer_id=to_char(b.user_id) ");
		}
		psql.append("and b.user_id=c.user_id and c.serv_type_id=14 ");
		psql.append("and c.user_id=d.user_id and a.gw_type="+gw_type+" ");
		
		if(!StringUtil.IsEmpty(dev_sn)){
			psql.append("and a.device_serialnumber='" + dev_sn.trim() + "' ");
		}
		
		if(!StringUtil.IsEmpty(loid)){
			psql.append("and b.username='" + loid.trim() + "' ");
		}
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
 
  public String checkIsUsed(String userId,String lineId,String gwType)
  {
	    String tabName = "tab_voip_serv_param";
		if ("2".equals(gwType)){
			tabName = "tab_egw_voip_serv_param";
		}
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==3){
			psql.append("select count(*) num ");
		}else{
			psql.append("select count(1) num ");
		}
		psql.append("from " + tabName + " where user_id=? and line_id=" + lineId);
		psql.setString(1, userId);
		
		String msg = "";
		Map retMap =  DBOperation.getRecord(psql.getSQL());
		if(null != retMap && !retMap.isEmpty() && (StringUtil.getIntValue(retMap, "num") > 0)){
			msg = "0,语音口存在，请重新输入合适的语音口";
		} 
		return msg;
  }
  
  public String changeVoipPort(String userId,String gw_type,String lineId,String tmpPort,String oldLineId)
  {
	    String port = "";
	    String tabName = "tab_voip_serv_param";
		if ("2".equals(gw_type))
		{
			tabName = "tab_egw_voip_serv_param";
		}
		if(!StringUtil.IsEmpty(tmpPort) && !"null".equals(tmpPort) && !"NULL".equals(tmpPort)){
			if(oldLineId.length() > 1){
				port = tmpPort.substring(0, tmpPort.length()-2) + lineId;
			}else{
				port = tmpPort.substring(0, tmpPort.length()-1) + lineId;
			}
		}
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("update " + tabName + " ");
		psql.append("set line_id =? ");
		if(!StringUtil.IsEmpty(port)){
			psql.append(",voip_port='" + port + "' ");
		}
		psql.append("where user_id = " + userId + " and line_id = " + oldLineId);
		psql.setInt(1, StringUtil.getIntegerValue(lineId));
		
		String msg = "0,更新失败";
		try
		{
			int ret = DBOperation.executeUpdate(psql.getSQL());
			if(ret > 0){
				msg = "1,更新成功";
			}
		}
		catch (Exception e)
		{
			logger.error("changeVoipPort err : msgs {}",e.getMessage());
			 msg = "0,更新异常";
		} 
		return msg;
  }	 
 

  private int getLineId(String linePort)
  {
    int lineId = 1;
    if (("V1".equals(linePort)) || ("A0".equals(linePort)) || ("USER001".equals(linePort)))
    {
      lineId = 1;
    }
    else if (("V2".equals(linePort)) || ("A1".equals(linePort)) || ("USER002".equals(linePort)))
    {
      lineId = 2;
    }
    else if (("V3".equals(linePort)) || ("A3".equals(linePort)) || ("USER003".equals(linePort)))
    {
      lineId = 3;
    }
    else if (("V4".equals(linePort)) || ("A4".equals(linePort)) || ("USER004".equals(linePort)))
    {
      lineId = 4;
    }
    else if (("V5".equals(linePort)) || ("A5".equals(linePort)) || ("USER005".equals(linePort)))
    {
      lineId = 5;
    }
    else if (("V6".equals(linePort)) || ("A6".equals(linePort)) || ("USER006".equals(linePort)))
    {
      lineId = 6;
    }
    else if (("V7".equals(linePort)) || ("A7".equals(linePort)) || ("USER007".equals(linePort)))
    {
      lineId = 7;
    }
    else if (("V8".equals(linePort)) || ("A8".equals(linePort)) || ("USER008".equals(linePort)))
    {
      lineId = 8;
    }
    return lineId;
  }
}