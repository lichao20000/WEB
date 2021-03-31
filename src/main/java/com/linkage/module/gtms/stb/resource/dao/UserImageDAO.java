package com.linkage.module.gtms.stb.resource.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 *
 * @author hp (Ailk No.)
 * @version 1.0
 * @since 2017-12-15
 * @category com.linkage.module.gtms.stb.resource.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class UserImageDAO extends SuperDAO
{
	/**
	 * 查询设备id
	 * @param con
	 * @param condition
	 * @return
	 */
	public List<Map<String,String>> getDevice_id(String con,String condition){
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.device_id,b.username,d.vendor_name,e.device_model,c.hardwareversion, c.softwareversion,f.loopback_ip,f.device_serialnumber  ");
		 if("0".equals(con)){// TODO wait (more table related)
			 sql.append("from tab_hgwcustomer a,hgwcust_serv_info b ,tab_devicetype_info c,tab_vendor d,gw_device_model e,tab_gw_device f where  a.user_id = b.user_id and a.device_id=f.device_id and f.devicetype_id=c.devicetype_id and c.vendor_id=d.vendor_id and c.device_model_id=e.device_model_id " +
						"and b.serv_type_id = 10 and a.username = '"+condition+"' ");
		}else{// TODO wait (more table related)
			sql.append("from tab_hgwcustomer a,hgwcust_serv_info b ,tab_devicetype_info c,tab_vendor d,gw_device_model e,tab_gw_device f where  a.user_id = b.user_id and a.device_id=f.device_id and f.devicetype_id=c.devicetype_id and c.vendor_id=d.vendor_id and c.device_model_id=e.device_model_id " +
					"and b.serv_type_id = 10");
			if (!(null == condition || "".equals(condition))) {
				if(condition.length()>5){
					sql.append(" and f.dev_sub_sn='");
					sql.append(condition.substring(condition.length()-6, condition.length()));
					sql.append("' ");
					sql.append(" and f.device_serialnumber like '%");
					sql.append(condition);
					sql.append("' ");
				}
			}
		}
		 sql.append(" and rownum<=1");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		list= jt.queryForList(psql.getSQL());
		return list;
	}
	/**
	 * 查询用户基本信息
	 */
	public List<Map<String,String>> getInformation(String con,String condition)
	{
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select a.linkman,a.linkaddress,a.linkman_credno,a.linkphone,b.username, a.bandwidth,a.username as aa,a.cust_type_id  ");
		 if("0".equals(con)){// TODO wait (more table related)
			 sql.append("from tab_hgwcustomer a,hgwcust_serv_info b ,tab_gw_device f where  a.user_id = b.user_id and a.device_id=f.device_id  " +
						"and b.serv_type_id = 10 and a.username = '"+condition+"' ");
		}else{// TODO wait (more table related)
			sql.append("from tab_hgwcustomer a,hgwcust_serv_info b ,tab_gw_device f where  a.user_id = b.user_id and a.device_id=f.device_id " +
					"and b.serv_type_id = 10 ");
			if (!(null == condition || "".equals(condition))) {
				if(condition.length()>5){
					sql.append(" and f.dev_sub_sn='");
					sql.append(condition.substring(condition.length()-6, condition.length()));
					sql.append("' ");
					sql.append(" and f.device_serialnumber like '%");
					sql.append(condition);
					sql.append("' ");
				}
			}

		}
		 sql.append(" and rownum<=1");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		list= jt.queryForList(psql.getSQL());
		return list;
	}
	/**
	 * MAC_ADDRESS
	 * @param deviceid
	 * @return
	 */
	public List<Map<String,String>> getMac_adderss(String deviceid)
	{
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct mac_address,layer2_interface,ip_address from tab_batch_gather_lan_mac ");
		if(!StringUtil.IsEmpty(deviceid))
		{
			sql.append(" where device_id='"+deviceid+"'");
		}
		PrepareSQL psql=new PrepareSQL(sql.toString());
		list=jt.queryForList(psql.getSQL());
		return list;
	}
	public int tote(String mac_address)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(mac_address) from tab_batch_gather_lan_mac where mac_address='"+mac_address+"'");
		PrepareSQL psql=new PrepareSQL(sql.toString());
		int tote=jt.queryForInt(psql.getSQL());
		return tote;

	}
	/**
	 * 查询stb_mac有匹配的数据，则表示该mac值是机顶盒mac，需要调用接口查询机顶盒信息展示
	 * @param mac_address
	 * @return
	 */
	public List<Map<String,String>> getStbMac(String mac_address)
	{
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select stb_mac from tab_gw_device_stbmac where stb_mac='"+mac_address+"'");
		PrepareSQL psql=new PrepareSQL(sql.toString());
		list=jt.queryForList(psql.getSQL());
		return list;
	}

	public List<Map<String,String>> companyname(String mac_address)
	{
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select company_name from tab_mac_company where mac='"+mac_address+"'");
		PrepareSQL psql=new PrepareSQL(sql.toString());
		list=jt.queryForList(psql.getSQL());
		return list;
	}

	public List<Map<String,String>> getphoneinfo(String username)
	{
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select net_account, phone_number, brand from tab_phone_info where net_account='"+username+"' and rownum<=1");
		PrepareSQL psql=new PrepareSQL(sql.toString());
		list=jt.queryForList(psql.getSQL());
		return list;
	}

	public List<Map<String,String>> getphonenumber(String username)
	{
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		StringBuffer sql = new StringBuffer();
		sql.append("select phone_number from tab_phone_info where net_account='"+username+"' and rownum<=1");
		PrepareSQL psql=new PrepareSQL(sql.toString());
		list=jt.queryForList(psql.getSQL());
		return list;
	}
}
