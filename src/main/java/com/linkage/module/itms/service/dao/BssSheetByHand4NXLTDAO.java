package com.linkage.module.itms.service.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.itms.service.obj.SheetObj;

public class BssSheetByHand4NXLTDAO extends SuperDAO
{
  private static Logger logger = LoggerFactory.getLogger(BssSheetByHand4NXLTDAO.class);


  /**
	 * 查询终端规格
	 * @return
	 */
	public List<HashMap<String, String>> getSpecId()
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select lan_num, wlan_num, spec_name, id from tab_bss_dev_port");

		return DBOperation.getRecords(psql.getSQL());
	}

	public Map<String, String> getSpecId(String id)
	{
		PrepareSQL psql = new PrepareSQL();
		psql.append("select lan_num, wlan_num, spec_name from tab_bss_dev_port where id = " + id);

		return DBOperation.getRecord(psql.getSQL());
	}

  /*未调用 2020/11/11
  public List getUserIdFromTabHgwcustomer(String loid, String userType)
  {
    logger.debug("BssSheetByHandDAO==>getCountUserNameFromTabHgwcustomer()", loid);

    PrepareSQL psql = new PrepareSQL();
    psql.append("select user_id from tab_hgwcustomer where username = '" + loid + "'");

    return this.jt.queryForList(psql.getSQL());
  }*/

  /*未调用 注释2020/11/11
  public List<HashMap<String, String>> getBssSheetInfo(String userId)
  {
    logger.debug("BssSheetByHandDAO==>getBssSheetInfo()", userId);

    StringBuffer sqlBuffer = new StringBuffer();
    sqlBuffer.append("select a.username as loid, a.passwd as pwd, a.city_id, a.adsl_hl, a.user_state, a.cust_type_id, a.user_type_id, ");
    sqlBuffer.append("       a.dealdate, a.open_status, ");
    sqlBuffer.append("       a.linkman, a.linkphone, a.email, a.mobile, a.linkaddress,a.linkman_credno, b.*, c.*");
    sqlBuffer.append(" from tab_hgwcustomer a, hgwcust_serv_info b, tab_bss_dev_port c");
    sqlBuffer.append(" where 1=1 ");
    sqlBuffer.append("   and a.user_id = b.user_id ");
    sqlBuffer.append("   and a.spec_id = c.id ");
    sqlBuffer.append("   and a.user_id = " + userId);

    PrepareSQL psql = new PrepareSQL(sqlBuffer.toString());

    return DBOperation.getRecords(psql.getSQL());
  }*/

  /*未调用 注释2020/11/11
  public List<HashMap<String, String>> getUserSheetInfo(String userId)
  {
    logger.debug("BssSheetByHandDAO==>getUserSheetInfo()", userId);

    StringBuffer sqlBuffer = new StringBuffer();
    sqlBuffer.append("select a.username as loid, a.passwd as pwd, a.city_id, a.adsl_hl, a.cust_type_id, a.user_type_id, ");
    sqlBuffer.append("       a.linkman, a.linkphone, a.email, a.mobile, a.linkaddress,a.linkman_credno, c.*");
    sqlBuffer.append(" from tab_hgwcustomer a, tab_bss_dev_port c");
    sqlBuffer.append(" where 1=1 ");
    sqlBuffer.append("   and a.spec_id = c.id ");
    sqlBuffer.append("   and a.user_id = " + userId);

    PrepareSQL psql = new PrepareSQL(sqlBuffer.toString());

    return DBOperation.getRecords(psql.getSQL());
  }*/

  /*未调用 注释2020/11/11
  public List<HashMap<String, String>> getCityIdExFromGwCityMap(String cityId)
  {
    logger.debug("BssSheetByHandDAO==>getCityIdExFromGwCityMap()", cityId);
    StringBuffer sqlBuffer = new StringBuffer();
    sqlBuffer.append("select * from gw_city_map ");
    sqlBuffer.append(" where 1=1 ");
    sqlBuffer.append("   and city_id = '" + cityId + "'");

    PrepareSQL psql = new PrepareSQL(sqlBuffer.toString());

    return DBOperation.getRecords(psql.getSQL());
  }*/

  /*未调用 注释2020/11/11
  public List<HashMap<String, String>> getVoipinfo(String userId)
  {
    logger.debug("BssSheetByHandDAO==>getVoipinfo()", userId);

    StringBuffer sqlBuffer = new StringBuffer();
    sqlBuffer.append("select a.*, b.prox_serv, b.prox_port, b.stand_prox_serv, b.stand_prox_port ");
    sqlBuffer.append("  from tab_voip_serv_param a, tab_sip_info b ");
    sqlBuffer.append(" where 1=1 ");
    sqlBuffer.append("   and a.sip_id = b.sip_id ");
    sqlBuffer.append("   and a.user_id = " + userId);

    PrepareSQL psql = new PrepareSQL(sqlBuffer.toString());

    return DBOperation.getRecords(psql.getSQL());
  }*/

  public boolean isExistCustomer(String customerId)
  {
    logger.debug("getCustomer");

    String sql = "select customer_name,city_id,office_id,customer_account from tab_customerinfo where customer_id='" +
      customerId + "'";
    PrepareSQL psql = new PrepareSQL(sql);
    Map map = DBOperation.getRecord(psql.getSQL());
    if (map != null) {
      return true;
    }
    return false;
  }

  public boolean isLoidExists(String loid, String userType)
  {
    logger.debug("isLoidExists({})", new Object[] { loid });
    String tabName = "tab_hgwcustomer";
    if ("2".equals(userType))
    {
      tabName = "tab_egwcustomer";
    }

    if (StringUtil.IsEmpty(loid, true))
    {
      logger.warn("用户账号为空");
      return false;
    }
    PrepareSQL psql = new PrepareSQL();
    psql.append("select user_id  from ");
    psql.append(tabName);
    psql.append(" where (user_state='1' or user_state='2') and username=? ");
    psql.setString(1, loid);

    List map = DBOperation.getRecords(psql.getSQL());
    if (map.isEmpty())
    {
      return false;
    }
    return true;
  }



  public boolean isLoidStbExists(String loid)
  {
    logger.debug("isLoidStbExists({})", new Object[] { loid });
    String tabName = "stb_tab_customer";

    if (StringUtil.IsEmpty(loid, true))
    {
      logger.warn("机顶盒用户账号loid为空");
      return false;
    }
    PrepareSQL psql = new PrepareSQL();
    psql.append("select customer_id from ");
    psql.append(tabName);
    psql.append(" where (cust_stat='1' or cust_stat='2') and loid=? ");
    psql.setString(1, loid);

    List map = DBOperation.getRecords(psql.getSQL());
    if (map.isEmpty())
    {
      return false;
    }
    return true;
  }


  public Map<String, String> getUserInfo(String loid, String userType)
  {
    logger.debug("isLoidExists({})", new Object[] { loid });
    String tabName = "tab_hgwcustomer";
    String user_dev_type = "gw_cust_user_dev_type u";
    String specialColumn = "";
    if ("2".equals(userType))
    {
      tabName = "tab_egwcustomer";
      specialColumn = ",customer_id ";
    }

    if (StringUtil.IsEmpty(loid, true))
    {
      logger.warn("用户账号为空");
      return null;
    }
    PrepareSQL psql = new PrepareSQL();
    psql.append("select a.*,u.type_id from " +
            "(select user_id, city_id, office_id, zone_id, access_style_id, linkman, linkphone, email, mobile, linkaddress, credno, spec_id ");
    psql.append( specialColumn + " from " +  tabName);
    psql.append(" where (user_state='1' or user_state='2') and username=? ");
    psql.append(" ) a left join ");
    psql.append(user_dev_type);
    psql.append(" on a.user_id = u.user_id");

    psql.setString(1, loid);

    List map = DBOperation.getRecords(psql.getSQL());
    if (map.isEmpty())
    {
      return null;
    }
    return (Map)map.get(0);
  }


  public Map<String, String> getStbUserInfo(String loid)
  {
    logger.debug("getStbUserInfo({})", new Object[] { loid });

    if (StringUtil.IsEmpty(loid, true))
    {
      logger.warn("机顶盒用户loid为空");
      return null;
    }
    PrepareSQL psql = new PrepareSQL();
    psql.append("select city_id, serv_account, serv_pwd, ntp_server1, ntp_server2, browser_url1  " +
            "from stb_tab_customer where (cust_stat='1' or cust_stat='2') and loid=? ");

    psql.setString(1, loid);

    List map = DBOperation.getRecords(psql.getSQL());
    if (map.isEmpty())
    {
      return null;
    }
    return (Map)map.get(0);
  }


  public List<HashMap<String, String>> getServInfo(String loid, String userType)
  {
    logger.debug("isLoidExists({})", new Object[] { loid });
    String tabUser = "tab_hgwcustomer a";
    String tabServ = "hgwcust_serv_info b";
    if ("2".equals(userType))
    {
      tabUser = "tab_egwcustomer a";
      tabServ = "egwcust_serv_info b";
    }

    if (StringUtil.IsEmpty(loid, true))
    {
      logger.warn("用户账号为空");
      return null;
    }
    PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
    psql.append("select l.*,v.eid,n.speed from " +
            "(select b.serv_type_id, b.oltfactory, b.username, b.passwd," +
            " b.vlanid, b.wan_type, b.bind_port, b.ipaddress, b.ipmask, b.gateway, " +
            " b.adsl_ser, b.ip_type, b.user_id, b.serv_num, b.multicast_vlanid from ");
    psql.append(tabUser);
    psql.append(",");
    psql.append(tabServ);
    psql.append(" where 1=1 and a.user_id=b.user_id ");
    psql.append(" and a.username ='" + loid + "'");
    psql.append(") l left join tab_voip_serv_param v on l.user_id = v.user_id left join tab_netspeed_param n on l.user_id=n.user_id");

    List map = DBOperation.getRecords(psql.getSQL());
    return map;
  }

  public HashMap<String, String> getVoipParaInfo(String userId, String userType, String servType)
  {
    logger.debug("isLoidExists({})", new Object[] { userId });
    String tabServ = "hgwcust_serv_info b";
    String tabParam = "tab_voip_serv_param a";
    String tabSip = "tab_sip_info c ";
    if ("2".equals(userType))
    {
      tabServ = "egwcust_serv_info b";
      tabParam = "tab_egw_voip_serv_param a";
    }

    if (StringUtil.IsEmpty(userId, true))
    {
      logger.warn("用户账号为空");
      return null;
    }
    PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
    psql.append("select a.protocol, a.voip_phone, a.reg_id, a.reg_id_type,  a.voip_port, a.voip_username, a.voip_passwd, " +
            "a.uri, a.user_agent_domain, c.prox_serv, c.prox_port, c.stand_prox_serv, c.stand_prox_port, c.regi_serv, " +
            "c.regi_port, c.stand_regi_serv, c.stand_regi_port, c.out_bound_proxy, c.out_bound_port, c.stand_out_bound_proxy, " +
            "c.stand_out_bound_port from ");
    psql.append(tabParam);
    psql.append(",");
    psql.append(tabServ);
    psql.append(",");
    psql.append(tabSip);
    psql.append(" where 1=1 and a.user_id=b.user_id and a.sip_id = c.sip_id ");
    psql.append(" and b.user_id =" + userId);
    psql.append(" and b.serv_type_id =" + servType);

    List map = DBOperation.getRecords(psql.getSQL());
    if (map.isEmpty())
    {
      return null;
    }
    return (HashMap)map.get(0);
  }

  public List<HashMap<String, String>> getServInfoByVoipPort(String userId, String userType, String sipVoipPort, String servType)
  {
    logger.debug("getServInfoByVoipPort({})", new Object[] { userId + "-" + userType + "-" + sipVoipPort });
    String tabServ = "hgwcust_serv_info b";
    String tabParam = "tab_voip_serv_param a";
    String tabSip = "tab_sip_info c ";
    if ("2".equals(userType))
    {
      tabServ = "egwcust_serv_info b";
      tabParam = "tab_egw_voip_serv_param a";
    }

    if (StringUtil.IsEmpty(userId, true))
    {
      logger.warn("用户账号为空");
      return null;
    }
    PrepareSQL psql = new PrepareSQL();// TODO wait (more table related)
    psql.append("select a.voip_phone, a.reg_id, a.reg_id_type, a.uri, a.user_agent_domain, a.protocol, a.voip_username, " +
            "a.voip_passwd,  b.vlanid, b.wan_type, b.ipaddress, b.ipmask, b.gateway, b.adsl_ser, c.regi_serv, c.regi_port, " +
            "stand_regi_serv, c.stand_regi_port, c.out_bound_proxy, c.out_bound_port, c.stand_out_bound_proxy, " +
            "c.stand_out_bound_port, c.prox_serv, c.prox_port, c.stand_prox_serv, c.stand_prox_port from ");
    psql.append(tabParam);
    psql.append(",");
    psql.append(tabServ);
    psql.append(",");
    psql.append(tabSip);
    psql.append(" where 1=1 and a.user_id=b.user_id and a.sip_id = c.sip_id ");
    psql.append(" and b.user_id =" + userId);
    psql.append(" and a.voip_port ='" + sipVoipPort + "' ");
    psql.append(" and b.serv_type_id =" + servType);

    List map = DBOperation.getRecords(psql.getSQL());
    return map;
  }

  public void addHandSheetLog(SheetObj userInfoSheet, UserRes curUser, int servType, int operType, int resultId, String resultDesc)
  {
    logger.debug("addHandSheetLog()");
    User user = curUser.getUser();
    PrepareSQL psql = new PrepareSQL();
    psql.append("insert into tab_handsheet_log (id,username,city_id,serv_type_id,oper_type,  result_id,result_desc,oper_time,occ_id,occ_ip) values(?,?,?,?,?,?,?,?,?,?) ");
    psql.setString(1, user.getId() + StringUtil.getStringValue(Long.valueOf(Math.round(Math.random() * 1000000000000.0D))));
    psql.setString(2, userInfoSheet.getLoid());
    psql.setString(3, userInfoSheet.getCityId());
    psql.setInt(4, servType);
    psql.setInt(5, operType);
    psql.setInt(6, resultId);
    psql.setString(7, resultDesc);
    psql.setLong(8, new Date().getTime() / 1000L);
    psql.setLong(9, user.getId());
    psql.setString(10, "");
    DBOperation.executeUpdate(psql.getSQL());
  }

  private int getLineId(String linePort)
  {
    logger.debug("getLineId({})", linePort);
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
