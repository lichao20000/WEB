package dao.hgwip;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import bio.hgwip.IPGlobal;
import bio.hgwip.IpTool;
import bio.hgwip.Subnet;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;

/**
 * 网段操作类
 * 
 * @author wangp
 * 
 */
public class SubnetOperationDAO {
	Logger log = LoggerFactory.getLogger(SubnetOperationDAO.class);

	// 数据库操作类
	private JdbcTemplate jt;

	// sql封装类
	private PrepareSQL pSQL;

	// ip管理的系统类
	private IPManagerDAO ipManagerDao;

	/**
	 * 模糊查询用户权限范围内的网络地址
	 */
	private static final String selectSubnetSQL = "select subnet,netmask,inetmask,childcount,assigntime,subnetcomment,subnetgrp,highaddr,lowaddr,purpose,totaladdr from gw_subnets where subnetgrp=? and inetmask >0 ?  order by subnetcomment,assigntime,fip desc";

	/**
	 * 模糊查询用户权限范围内的未分配的网络地址
	 */
	private static final String selectUnAssignIPSQL = "select subnet,netmask,inetmask,childcount,assigntime,assign,subnetcomment,subnetgrp,highaddr,lowaddr,purpose,totaladdr from gw_subnets where childcount=0 and assign=0 and subnet not in(select subnet from gw_ipmain) and city_id=? and inetmask >0 ?  order by subnetcomment,assigntime,fip desc";

	/**
	 * 模糊查询用户权限范围内的已分配的网络地址
	 */
	private static final String selectAssignIPSQL = "select a.subnet,a.netmask,a.subnetgrp,a.inetmask,a.assign,a.childcount,b.purpose1,b.purpose2,b.purpose3,b.city_id,b.country  from gw_subnets a,gw_ipmain b where a.subnet=b.subnet and a.inetmask=b.inetmask and a.assign=1 and a.city_id=? ? order by a.subnetcomment,a.assigntime,a.fip desc";

	/**
	 * 增加子网
	 */
	private static final String addSubnetSQL = "insert into gw_subnets(subnet,inetmask,subnetgrp,grandgroup,igroupmask,netmask,"
			+ "totaladdr,childcount,assign,mailstatus,city_id,subnetcomment,approve,purpose,assigntime,fip,fhighaddress,flowaddress,highaddr,lowaddr) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 判断新增的网络地址在系统中是不是被包含或有子网
	 */
	private static final String isConfigSQL = "select count(*) num from gw_subnets where (flowaddress<=? and fhighaddress>=?) or (fip>=? and fip<=?)";

	/**
	 * 查询网段的详细信息
	 */
	private static final String subnetDetailSQL = "select *  from gw_subnets where subnet=? and subnetgrp=? and inetmask=?";

	/**
	 * 删除总表中某个网段和其子网的记录
	 */
	private static final String deleteIPMainSQL = "delete from gw_ipmain where inetmask>=? and subnet in(select subnet from gw_subnets where inetmask>=? and fip<=? and fip>=?)";

	/**
	 * 删除总表中的某个网段的子网记录
	 */
	private static final String deleteIPMainNotBySelfSQL = "delete from gw_ipmain where inetmask>? and subnet in(select subnet from gw_subnets where inetmask>? and fip<=? and fip>=?)";

	/**
	 * 删除专线用户申请表中的某个网段和其子网的记录
	 */
	@SuppressWarnings("unused")
	private static final String deleteApplyipSQL = "delete from applyip where subnet in(select subnet from gw_subnets where inetmask>=? and fip<=? and fip>=?)";

	/**
	 * 删除用户信息表中的某个网段和其子网的记录
	 */
	@SuppressWarnings("unused")
	private static final String deleteUserIPSQL = "delete from userip where subnet in(select subnet from gw_subnets where inetmask>=? and fip <=? and fip>=?)";

	/**
	 * 把某个网段和其子网中邮件状态为等待回信或注册成功的记录插入delsub表中
	 */
	@SuppressWarnings("unused")
	private static final String addDelSubSQL = "insert into delsub(subnet,inetmask,subnetgrp,lowaddr,highaddr,city_id)"
			+ " select subnet,inetmask,subnetgrp,lowaddr,highaddr,city_id from gw_subnets where inetmask>=? and subnet in(select subnet from gw_subnets where inetmask>=? and fip<=? and fip>=? and mailstatus in(1,3))";

	/**
	 * 删除子网表中的某个网段和其子网的记录
	 */
	private static final String deleteSubnetSQL = "delete from gw_subnets where inetmask>=? and fip <=? and fip>=?";

	/**
	 * 删除指定网段的子网信息（不删除自身）
	 */
	private static final String deleteSubnetNotBySelfSQL = "delete from gw_subnets where inetmask>? and fip <=? and fip>=?";

	/**
	 * 查询某个网段的子网，第一个参数为网段的子网掩码位数、第二个参数为网段的最大可用地址，第三个参数为网段的最小可用地址
	 */
	private static final String selectChildrenSQL = "select * from gw_subnets where inetmask>? and fip<=? and fip>=?";

	/**
	 * 更新网段信息
	 */
	private static final String updateSubnetChildCountSQL = "update gw_subnets set childcount=?,approve=? where subnet=? and subnetgrp=? and inetmask=?";

	/**
	 * 删除专线用户申请表中对指定字段的申请记录
	 */
	@SuppressWarnings("unused")
	private static final String deleteApplyipOFSubnetSQL = "delete from applyip where subnet=? and subnetgrp=? and inetmask=?";

	/**
	 * 网段信息入总表
	 */
	private static final String insertIpmainSQL = "insert into gw_ipmain(subnet,inetmask,city_id,country,purpose1,purpose2,purpose3,subnetcomment) values(?,?,?,?,?,?,?,?)";

	/**
	 * 更新指定网段的总表分配情况
	 */
	private static final String updateIpmainSQL = "update gw_ipmain set subnet=?,inetmask=?,city_id=?,country=?,purpose1=?,purpose2=?,purpose3=?,subnetcomment=? where subnet=? and inetmask=?";

	/**
	 * 更新指定网段的信息
	 */
	private static final String updateSubnetSQL = "update gw_subnets set ? where subnet=? and subnetgrp=? and inetmask=?";

	/**
	 * 删除去注册记录
	 */
	private static final String deleteDelSubSQL = "delete from delsub where subnet=?";

	/**
	 * 删除注册记录
	 */
	private static final String deleteApnicMailSQL = "delete from apnicmail where subnet=?";

	/**
	 * 用户表插入信息
	 */
	private static final String insertUserSQL = "insert into userip(subnetgrp,subnet,inetmask,is_import,addrnum,usernamezw,usernameyw,usernamepyjc,usernameywjc,"
			+ "address,managerhandle,techname,techduty,techphone,techemail,techfax,techaddr,techaddre,techpc,technamep,"
			+ "netname,netnamejc,netnamee,rwaddr,applydate,permitdate,purpose,memo,cntmode,cntspeed,cntaddr,localun,country)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 更新指定网段的专线用户信息
	 */
	private static final String updateUserSQL = "update userip set subnetgrp=?,subnet=?,inetmask=?,is_import=?,addrnum=?,usernamezw=?,usernameyw=?,usernamepyjc=?,"
			+ "usernameywjc=?,address=?,managerhandle=?,techname=?,techduty=?,techphone=?,techemail=?,techfax=?,techaddr=?,techaddre=?,techpc=?,technamep=?,"
			+ "netname=?,netnamejc=?,netnamee=?,rwaddr=?,applydate=?,permitdate=?,purpose=?,memo=?,cntmode=?,cntspeed=?,cntaddr=?,localun=?,country=? where subnet=? and subnetgrp=? and inetmask=?";

	/**
	 * 查询总表指定网段的信息
	 */
	private static final String selcetDetailIpMainSubnetSQL = "select a.*,b.assigntime from gw_ipmain a,gw_subnets b where a.subnet=b.subnet and a.inetmask=b.inetmask and a.city_id =b.subnetgrp and a.subnet=? and a.inetmask=?";

	/**
	 * 查询用户表指定网段的信息
	 */
	private static final String selectUserIPSubentSQL = "select * from userip where subnet=? and subnetgrp=? and inetmask=?";

	/**
	 * 查询指定网段的子网总表分配情况
	 */
	private static final String selectIPMainChildSubentSQL = " select a.*,b.city_name from gw_ipmain a,tab_city b,gw_subnets c where a.city_id= b.city_id and a.subnet= c.subnet and a.inetmask=c.inetmask"
			+ " and c.fip<=? and c.fip>=? and c.inetmask>?";

	/**
	 * 查询指定网段的子网用户分配信息
	 */
	private static final String selectUserIpChildSubnetSQL = "select b.* from gw_subnets a,userip b where a.subnet=b.subnet and a.subnetgrp=b.subnetgrp and a.inetmask=b.inetmask and a.fip<=? and a.fip>=? and a.inetmask>?";

	/**
	 * 删除指定网段在总表中的记录
	 */
	private static final String deleteIpMainSubnetSQL = "delete from gw_ipmain where subnet=? and inetmask=?";

	/**
	 * 删除指定网段的用户信息
	 */
	@SuppressWarnings("unused")
	private static final String deleteUserIPOfSubnetSQL = " delete from userip where subnet=? and subnetgrp=? and inetmask=?";

	/**
	 * 专线用户IP申请
	 */
	private static final String insertApplyipSQL = "insert into applyip(subnetgrp,subnet,inetmask,city_id,totaladdr,assigninfo,agree,applydate,applycomment,attachdoc,mem,dealdate) values(?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 删除分配给地市的网段
	 */
	private static final String deleteAssignToCitySubnetSQL = "delete from gw_subnets where subnet=? and inetmask=? and subnetgrp!=?";

	/**
	 * 根据导出条件查询导出字段（用户ip表）
	 */
	private static final String searchUserForExcel = "select a.*,b.netmask,c.city_name from userip a,gw_subnets b,tab_city c where a.subnet=b.subnet and a.inetmask=b.inetmask and a.subnetgrp=b.subnetgrp and a.rwaddr=c.city_id ? order by a.subnet,a.inetmask,a.subnetgrp";

	/**
	 * 获取专线用户资料（导出）
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getExpDataVipUser(int status, final String[] column,
			String cityid, String subnet, String addrnum, String netnamee,
			String city_id, String country, String starttime, String endtime,
			String cntmode, String cntspeed, String cntaddr, String localun,
			String usernamezw) {
		String para = "";
		// 条件过滤
		// 省局用户：不过虑，地市用户：过滤为本身
		if (status == 0) {
			if (city_id != null && !"".equals(city_id.trim())
					&& !"-1".equals(city_id.trim())) {
				para += " and a.rwaddr='" + city_id + "'";
			}
		} else {
			para += " and a.rwaddr='" + cityid + "'";
		}
		// 输入IP地址
		if (subnet != null && !"".equals(subnet.trim())) {
			para += " and a.subnet like'%" + subnet + "%'";
		}
		// 地址个数
		if (addrnum != null && !"".equals(addrnum.trim())) {
			para += " and a.addrnum =" + Integer.parseInt(addrnum);
		}
		// 用户网络英文名
		if (netnamee != null && !"".equals(netnamee.trim())) {
			para += " and a.netnamee like'%" + netnamee + "%'";
		}
		// 县
		if (country != null && !"".equals(country.trim())
				&& !"-1".equals(country.trim())) {
			para += " and a.country ='" + country + "'";
		}
		// 开始时间
		if (starttime != null && !"".equals(starttime.trim())) {
			DateTimeUtil sdt = new DateTimeUtil(starttime);
			long st = sdt.getLongTime();
			para += " and a.applydate >=" + st;
		}
		// 结束时间
		if (endtime != null && !"".equals(endtime.trim())) {
			DateTimeUtil edt = new DateTimeUtil(endtime);
			long et = edt.getLongTime();
			para += " and a.applydate <=" + et;
		}
		// 接入方式
		if (cntmode != null && !"".equals(cntmode.trim())
				&& !"-1".equals(cntmode.trim())) {
			para += " and a.cntmode ='" + cntmode + "'";
		}
		// 接入速率
		if (cntspeed != null && !"".equals(cntspeed.trim())
				&& !"-1".equals(cntspeed.trim())) {
			para += " and a.cntspeed ='" + cntspeed + "'";
		}
		// 互联地址
		if (cntaddr != null && !"".equals(cntaddr.trim())) {
			para += " and a.cntaddr like'%" + cntaddr + "%'";
		}
		// 本地用户编号
		if (localun != null && !"".equals(localun.trim())) {
			para += " and a.localun like'%" + localun + "%'";
		}
		// 用户名称（中文）
		if (usernamezw != null && !"".equals(usernamezw.trim())) {
			para += " and a.usernamezw like'%" + usernamezw + "%'";
		}

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String searchUserForExcelMysql = "select a.subnetgrp, a.subnet, a.inetmask, a.is_import, a.addrnum, a.usernamezw, a.usernameyw, a.usernamepyjc," +
					"a.usernameywjc, a.address, a.managerhandle, a.techname, a.techduty, a.techphone, a.techemail, a.techfax, a.techaddr,a.techaddre," +
					"a.techpc, a.technamep, a.netname, a.netnamejc, a.netnamee, a.rwaddr, a.applydate, a.permitdate, a.purpose, a.memo, a.cntmode," +
					"a.cntspeed, a.cntaddr, a.localun, a.country," +
					"b.netmask,c.city_name from userip a,gw_subnets b,tab_city c where a.subnet=b.subnet and a.inetmask=b.inetmask and a.subnetgrp=b.subnetgrp and a.rwaddr=c.city_id ? order by a.subnet,a.inetmask,a.subnetgrp";
			pSQL.setSQL(searchUserForExcelMysql);
		}
		else {
			pSQL.setSQL(searchUserForExcel);
		}
		pSQL.setStringExt(1, para, false);
		List<Map> UserToExcelList = jt.query(pSQL.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				HashMap map = new HashMap();
				for (int i = 0; i < column.length; i++) {
					if (column[i].equals("rwaddr")) {
						map.put("rwaddr", rs.getString("city_name"));
					} else if (column[i].equals("applydate")) {
						map.put("applydate", new Date(
								rs.getLong("applydate") * 1000));
					} else {
						map.put(column[i], rs.getString(column[i]));
					}
				}
				return map;
			}

		});
		return UserToExcelList;
	}

	/**
	 * 获取分配到地市网段的用途map
	 * 
	 * @param subnet
	 *            子网ip
	 * @param inetMask
	 *            子网掩码位数
	 * @return key：purpose1(用途1)，purpose2(用途2)，purpose3(用途3)
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getDetailPurposeSubnet(String subnet,
			int inetMask) {
		Map<String, String> purposeMap = new HashMap();
		Map<String, String> subnetMap = null;
		try {
			subnetMap = getDetailIpMainInfo(subnet, inetMask);
		} catch (Exception e) {
			log.debug("subnet:" + subnet + "   inetMask:" + inetMask
					+ "  isn't assign to city!");
		}

		if (null != subnetMap && 0 != subnetMap.size()) {
			// 这个网段是地市用户的情况下
			if (IPGlobal.NET_PURPOSE.equals(subnetMap.get("purpose1"))) {
				purposeMap.put("purpose1", subnetMap.get("purpose1"));
				purposeMap.put("purpose2", subnetMap.get("purpose2"));
				purposeMap.put("purpose3", subnetMap.get("purpose3"));
			}
		}
		// clear
		subnetMap = null;
		return purposeMap;
	}

	/**
	 * 查询指定网段的approve状态
	 * 
	 * @param subnet
	 *            子网ip
	 * @param subnetGrp
	 *            子网父节点
	 * @param inetMask
	 *            子网掩码
	 * @return 0：同意 1：不同意 2：未审批
	 */
	public int getUserAssignStatus(String subnet, String subnetGrp, int inetMask) {
		Map<String, String> map = getDetailSubnet(subnet, subnetGrp, inetMask);
		return Integer.parseInt(map.get("approve"));
	}

	/**
	 * 申请ip给专线用户
	 * 
	 * @param subnet
	 *            网段ip
	 * @param subnetGrp
	 *            网段父节点
	 * @param inetMask
	 *            网段子网掩码
	 * @param city_id
	 *            申请单位，与tab_city中的city_id一致
	 * @param addrNum
	 *            地址个数
	 * @param assignInfo
	 *            用户信息
	 * @param comment
	 *            备注
	 * @param fileRealPath
	 *            文件实际路径
	 * @return 成功（IPGlobal.SUNCCESS），失败（IPGlobal.DBOPERATION_FAIL）
	 */
	@SuppressWarnings("unchecked")
	public int applySubnetToUser(String subnet, String subnetGrp, int inetMask,
			String city_id, int addrNum, String assignInfo, String comment,
			String fileRealPath) {
		ArrayList<String> sqlList = new ArrayList();

		// 准备插入申请表
		pSQL.setSQL(insertApplyipSQL);
		pSQL.setString(1, subnetGrp);
		pSQL.setString(2, subnet);
		pSQL.setInt(3, inetMask);
		pSQL.setString(4, city_id);
		pSQL.setInt(5, addrNum);
		pSQL.setString(6, assignInfo);
		pSQL.setInt(7, IPGlobal.NOT_CHECK);
		pSQL.setLong(8, new DateTimeUtil().getLongTime());
		if (null == comment || "".equals(comment)) {
			pSQL.setStringExt(9, null, false);
		} else {
			pSQL.setString(9, comment);
		}
		pSQL.setString(10, fileRealPath);
		pSQL.setStringExt(11, null, false);
		pSQL.setStringExt(12, null, false);
		log.debug("applySubnetToUser_SQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		// 更新网段中的状态
		pSQL.setSQL(updateSubnetSQL);
		String param = " assign=" + IPGlobal.WAIT_APPROVE;
		pSQL.setStringExt(1, param, false);
		pSQL.setString(2, subnet);
		pSQL.setString(3, subnetGrp);
		pSQL.setInt(4, inetMask);
		log.debug("updateSubnetSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		String[] sqlArray = new String[sqlList.size()];
		sqlList.toArray(sqlArray);

		// clear
		sqlList = null;

		int[] resultCode = null;
		try {
			resultCode = jt.batchUpdate(sqlArray);
		} catch (Exception e) {
			log.error("applySubnetToUser fail!");
			e.printStackTrace();
		}

		return resultCode == null ? IPGlobal.DBOPERATION_FAIL
				: IPGlobal.SUNCCESS;

	}

	/**
	 * 批量分配ip到地市
	 * 
	 * @param paramList
	 *            list中嵌入map，key分别为subnet、subnetgrp、inetmask,city_id,country,purpose1,purpose2,purpose3,subnetcomment
	 * @return 返回操作失败的网段，在paramList的位置（以0开始）
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> batchAssignToCity(
			ArrayList<HashMap<String, String>> paramList) {
		ArrayList<String> resultList = new ArrayList();
		String subnet = "";
		String subnetGrp = "";
		int inetMask;
		int result = IPGlobal.DBOPERATION_FAIL;
		String city_id = "";
		String country = "";
		String purpose1 = "";
		String purpose2 = "";
		String purpose3 = "";
		String comment = "";
		HashMap paramMap = null;
		for (int i = 0; i < paramList.size(); i++) {
			paramMap = paramList.get(i);
			subnet = (String) paramMap.get("subnet");
			subnetGrp = (String) paramMap.get("subnetgrp");
			inetMask = Integer.parseInt((String) paramMap.get("inetmask"));
			city_id = (String) paramMap.get("city_id");
			country = (String) paramMap.get("country");
			purpose1 = (String) paramMap.get("purpose1");
			purpose2 = (String) paramMap.get("purpose2");
			purpose3 = (String) paramMap.get("purpose3");
			comment = (String) paramMap.get("subnetcomment");
			// 修改网段的市分配信息
			result = assignSubnetToCity(subnet, subnetGrp, inetMask, city_id,
					country, purpose1, purpose2, purpose3, comment);
			if (result != IPGlobal.SUNCCESS) {
				resultList.add(String.valueOf(i));
			}
		}

		return resultList;

	}

	/**
	 * 批量修改总表中某些网段的分配信息
	 * 
	 * @param paramList
	 *            list中嵌入map，key分别为subnet、subnetgrp、inetmask,city_id,country,purpose1,purpose2,purpose3,subnetcomment
	 * @return 返回操作失败的网段，在paramList的位置（以0开始）
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> batchModifyToCitySubnet(
			ArrayList<HashMap<String, String>> paramList) {
		ArrayList<String> resultList = new ArrayList();

		String subnet = "";
		String subnetGrp = "";
		int inetMask;
		int result = IPGlobal.DBOPERATION_FAIL;
		String city_id = "";
		String country = "";
		String purpose1 = "";
		String purpose2 = "";
		String purpose3 = "";
		String comment = "";
		HashMap paramMap = null;
		for (int i = 0; i < paramList.size(); i++) {
			paramMap = paramList.get(i);
			subnet = (String) paramMap.get("subnet");
			subnetGrp = (String) paramMap.get("subnetgrp");
			inetMask = Integer.parseInt((String) paramMap.get("inetmask"));
			city_id = (String) paramMap.get("city_id");
			country = (String) paramMap.get("country");
			purpose1 = (String) paramMap.get("purpose1");
			purpose2 = (String) paramMap.get("purpose2");
			purpose3 = (String) paramMap.get("purpose3");
			comment = (String) paramMap.get("subnetcomment");
			// 修改网段的市分配信息
			result = modifyToCitySubnet(subnet, subnetGrp, inetMask, city_id,
					country, purpose1, purpose2, purpose3, comment);
			if (result != IPGlobal.SUNCCESS) {
				resultList.add(String.valueOf(i));
			}
		}

		return resultList;
	}

	/**
	 * 批量回收IP资源
	 * 
	 * @param paramList
	 *            list中嵌入map，key分别为subnet、subnetgrp、inetmask，唯一标识一个网段
	 * @return 返回操作失败的网段，在paramList的位置（以0开始）
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> batchCancelAssignSubnet(
			ArrayList<HashMap> paramList) {
		ArrayList<String> resultList = new ArrayList();

		String subnet = "";
		String subnetGrp = "";
		int inetMask;
		int result = IPGlobal.DBOPERATION_FAIL;
		HashMap paramMap = null;
		for (int i = 0; i < paramList.size(); i++) {
			paramMap = paramList.get(i);
			subnet = (String) paramMap.get("subnet");
			subnetGrp = (String) paramMap.get("subnetgrp");
			inetMask = Integer.parseInt((String) paramMap.get("inetmask"));
			// 回收iP资源
			result = cancelAssignSubnet(subnet, subnetGrp, inetMask);
			if (result != IPGlobal.SUNCCESS) {
				resultList.add(String.valueOf(i));
			}
		}

		return resultList;
	}

	/**
	 * 回收IP资源
	 * 
	 * @param subnet
	 *            子网IP
	 * @param subnetGrp
	 *            子网父节点
	 * @param inetMask
	 *            子网掩码
	 * @return 操作结果 成功（IPGlobal.SUNCCESS）、失败（IPGlobal.DBOPERATION_FAIL）
	 */
	@SuppressWarnings("unchecked")
	public int cancelAssignSubnet(String subnet, String subnetGrp, int inetMask) {
		ArrayList<String> sqlList = new ArrayList();

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String subnetDetailSQLMysql = "select subnetgrp, subnet, inetmask, grandgrp, igroupmask, approve, lowaddr, highaddr, city_id, netmask, " +
					" assign, assigntime, purpose, fhighaddress, fip, flowaddress, subnetcomment, childcount, totaladdr, mailstatus  " +
					" from gw_subnets where subnet=? and subnetgrp=? and inetmask=?";
			pSQL.setSQL(subnetDetailSQLMysql);
		}
		else {
			// 查询网段详细信息
			pSQL.setSQL(subnetDetailSQL);
		}
		pSQL.setString(1, subnet);
		pSQL.setString(2, subnetGrp);
		pSQL.setInt(3, inetMask);
		Map<String, String> subnetMap = queryForMap(pSQL.getSQL());
		Subnet root = new Subnet(subnetMap);

		// 分配状态
		int assign = root.getAssign();
		/**
		 * 分配到地市的情况
		 */
		if (assign == IPGlobal.ASSIGN_TO_CITY) {
			// 删除总表中这个网段的记录
			pSQL.setSQL(deleteIpMainSubnetSQL);
			pSQL.setString(1, subnet);
			pSQL.setInt(2, inetMask);
			sqlList.add(pSQL.getSQL());

			// //删除专线用户申请表中的记录
			// pSQL.setSQL(deleteApplyipSQL);
			// pSQL.setInt(1,root.getInetMask());
			// pSQL.setString(2,root.getFhighaddress());
			// pSQL.setString(3,root.getFlowaddress());
			// log.debug("deleteApplyipSQL:"+pSQL.getSQL());
			// sqlList.add(pSQL.getSQL());
			//			
			// //删除用户表中的记录
			// pSQL.setSQL(deleteUserIPSQL);
			// pSQL.setInt(1,root.getInetMask());
			// pSQL.setString(2,root.getFhighaddress());
			// pSQL.setString(3,root.getFlowaddress());
			// log.debug("deleteUserIPSQL:"+pSQL.getSQL());
			// sqlList.add(pSQL.getSQL());
			//			
			// //把已经分配给专线用户的，并已向apnic注册的网段记录插入delsub表，准备发送去注册
			// pSQL.setSQL(addDelSubSQL);
			// pSQL.setInt(1,root.getInetMask());
			// pSQL.setInt(2,root.getInetMask());
			// pSQL.setString(3,root.getFhighaddress());
			// pSQL.setString(4,root.getFlowaddress());
			// log.debug("addDelSubSQL:"+pSQL.getSQL());
			// sqlList.add(pSQL.getSQL());

			// 删除网段信息
			pSQL.setSQL(deleteSubnetNotBySelfSQL);
			pSQL.setInt(1, root.getInetMask());
			pSQL.setString(2, root.getFhighaddress());
			pSQL.setString(3, root.getFlowaddress());
			sqlList.add(pSQL.getSQL());

			// 删除分配给地市的网段
			pSQL.setSQL(deleteAssignToCitySubnetSQL);
			pSQL.setString(1, root.getSubnet());
			pSQL.setInt(2, root.getInetMask());
			pSQL.setString(3, root.getSubnetGrp());
			sqlList.add(pSQL.getSQL());

			// 更新指定网段的分配状态为未分配
			pSQL.setSQL(updateSubnetSQL);
			String param = " assign=" + IPGlobal.NOT_ASSIGN;
			pSQL.setStringExt(1, param, false);
			pSQL.setString(2, root.getSubnet());
			pSQL.setString(3, root.getSubnetGrp());
			pSQL.setInt(4, root.getInetMask());
			log.debug("updateSubnetSQL:" + pSQL.getSQL());
			sqlList.add(pSQL.getSQL());
		}
		// //分配到用户
		// else if(assign==IPGlobal.ASSIGN_TO_USER)
		// {
		// //删除指定网段的专线用户申请信息
		// pSQL.setSQL(deleteApplyipOFSubnetSQL);
		// pSQL.setString(1,root.getSubnet());
		// pSQL.setString(2,root.getSubnetGrp());
		// pSQL.setInt(3,root.getInetMask());
		// log.debug("deleteApplyipOFSubnetSQL:"+pSQL.getSQL());
		// sqlList.add(pSQL.getSQL());
		//			
		// //删除指定网段的用户申请信息
		// pSQL.setSQL(deleteUserIPOfSubnetSQL);
		// pSQL.setString(1,root.getSubnet());
		// pSQL.setString(2,root.getSubnetGrp());
		// pSQL.setInt(3,root.getInetMask());
		// log.debug("deleteUserIPOfSubnetSQL:"+pSQL.getSQL());
		// sqlList.add(pSQL.getSQL());
		//			
		// //更新子网表中指定网段的状态为未划分状态，approve状态为不同意
		// String param=" assign="+IPGlobal.NOT_ASSIGN+"
		// ,approve="+IPGlobal.NOT_CHECK;
		// pSQL.setSQL(updateSubnetSQL);
		// pSQL.setStringExt(1,param,false);
		// pSQL.setString(2,root.getSubnet());
		// pSQL.setString(3,root.getSubnetGrp());
		// pSQL.setInt(4,root.getInetMask());
		// log.debug("updateSubnetSQL:"+pSQL.getSQL());
		// sqlList.add(pSQL.getSQL());
		// }
		//		
		// //待审批的情况
		// else
		// {
		// //删除指定网段的专线用户申请信息
		// pSQL.setSQL(deleteApplyipOFSubnetSQL);
		// pSQL.setString(1,root.getSubnet());
		// pSQL.setString(2,root.getSubnetGrp());
		// pSQL.setInt(3,root.getInetMask());
		// log.debug("deleteApplyipOFSubnetSQL:"+pSQL.getSQL());
		// sqlList.add(pSQL.getSQL());
		//			
		//			
		// //更新子网表中指定网段的状态为未划分状态，approve状态为不同意
		// String param=" assign="+IPGlobal.NOT_ASSIGN+"
		// ,approve="+IPGlobal.NOT_CHECK;
		// pSQL.setSQL(updateSubnetSQL);
		// pSQL.setStringExt(1,param,false);
		// pSQL.setString(2,root.getSubnet());
		// pSQL.setString(3,root.getSubnetGrp());
		// pSQL.setInt(4,root.getInetMask());
		// log.debug("updateSubnetSQL:"+pSQL.getSQL());
		// sqlList.add(pSQL.getSQL());
		//			
		// }

		String[] sqlArray = new String[sqlList.size()];
		sqlList.toArray(sqlArray);

		// clear
		sqlList = null;

		int[] resultCode = null;
		try {
			resultCode = jt.batchUpdate(sqlArray);
		} catch (Exception e) {
			log.error("cancelAssignSubnet fail!");
			e.printStackTrace();
		}
		return resultCode == null ? IPGlobal.DBOPERATION_FAIL
				: IPGlobal.SUNCCESS;

	}

	/**
	 * @author liuli
	 * @category liuli@lianchuang.com IP地址总表查询
	 * @param IpAdress
	 *            IP地址
	 * @param CityOwe
	 *            所属地市
	 * @param XianOwe
	 *            所属县
	 * @param UseOne
	 *            用途一
	 * @param UseSecord
	 *            用途二
	 * @param UseThrid
	 *            用途三
	 * @param StartTime
	 *            分配起始时间
	 * @param OverTime
	 *            分配结束时间
	 * @return 查询到的结果结合 形式:ArrayList<Map> Map:
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getMsgLog(String IpAdress, String CityOwe, String XianOwe,
			String UseOne, String UseSecord, String UseThrid, String StartTime,
			String OverTime) throws Exception {
		long Start;
		long Over;
		String getLogSql = "select a.subnet as subnet,a.inetmask as inetmask,a.subnetgrp as subnetgrp,a.assigntime as assigntime,a.netmask as netmask,a.assign,a.subnetcomment,c.city_name as city_name,b.country,b.purpose1,b.purpose2,b.purpose3 from subnets a,gw_ipmain b,tab_city c where b.city_id=c.city_id and a.subnet=b.subnet and a.inetmask=b.inetmask and  a.assign=1 ";
		if ((IpAdress != null) && !"".equals(IpAdress)) {
			getLogSql += " and a.subnet like '%" + IpAdress + "%'";
		}
		if (CityOwe != null && !"".equals(CityOwe) && !"-1".equals(CityOwe)) {
			getLogSql += " and b.city_id='" + CityOwe + "'";
		}
		if (XianOwe != null && !"".equals(XianOwe) && !"-1".equals(XianOwe)) {
			getLogSql += " and b.country='" + XianOwe + "'";
		}
		if (UseOne != null && !"".equals(UseOne) && !"-1".equals(UseOne)) {
			getLogSql += " and b.purpose1='" + UseOne + "'";
		}
		if (UseSecord != null && !"".equals(UseSecord)
				&& !"-1".equals(UseSecord)) {
			getLogSql += " and b.purpose2='" + UseSecord + "'";
		}
		if (UseThrid != null && !"".equals(UseThrid) && !"-1".equals(UseThrid)) {
			getLogSql += " and b.purpose3='" + UseThrid + "'";
		}

		if (OverTime != null && !OverTime.equals("")) {
			Over = new DateTimeUtil(OverTime).getLongTime();
			getLogSql += " and a.assigntime <=" + Over + "";
		}
		if (StartTime != null && !StartTime.equals("")) {
			Start = new DateTimeUtil(StartTime).getLongTime();
			getLogSql += " and  a.assigntime >=" + Start + "";
		}
		getLogSql += " order by a.fip";
		PrepareSQL psql = new PrepareSQL(getLogSql);
		psql.getSQL();
		List<Map> res = jt.query(getLogSql, new RowMapper() {
			public Object mapRow(ResultSet re, int arg1) throws SQLException {
				Map map = new HashMap();
				map.put("subnet", re.getString("subnet"));
				map.put("netmask", re.getString("netmask"));
				map.put("assign", re.getInt("assign"));
				map.put("city_name", re.getString("city_name"));
				map.put("country", re.getString("country"));
				map.put("purpose1", re.getString("purpose1"));
				map.put("purpose2", re.getString("purpose2"));
				map.put("purpose3", re.getString("purpose3"));
				map.put("subnetcomment", re.getString("subnetcomment"));
				return map;
			}
		});
		// if(res==null || res.size()<1){
		// res =null;
		// }
		return res;
	}

	// add
	public void add(String liebiao1, String addname) {
		String sql = "insert into datadic(type_name,value) values('" + liebiao1
				+ "','" + addname + "')";

		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();

		jt.execute(sql);
	}

	/**
	 * 删除客户资料 liuli
	 * 
	 *            内部编号
	 */

	public void delete(String liebiao1, String addname) {
		String sql = "delete from datadic where type_name='" + liebiao1
				+ "' and value='" + addname + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		jt.execute(sql);

	}

	@SuppressWarnings("unchecked")
	public List<Map> getdatalist(String liebiao1) throws Exception {
		String getLogSql = "select * from datadic where type_name='" + liebiao1 + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			getLogSql = "select type_name,value from datadic where type_name='" + liebiao1 + "'";
		}
		getLogSql += " order by type_name desc";
		PrepareSQL psql = new PrepareSQL(getLogSql);
		psql.getSQL();
		List<Map> res = jt.queryForList(getLogSql);
		return res;
	}

	// 分配IP地址个数权限设置功能
	@SuppressWarnings("unchecked")
	public List<Map> getcityiplist() throws Exception {
		String getLogSql = "select a.city_id,a.ip_num,b.city_name  from city_ipnum a,tab_city b where a.city_id=b.city_id";
		getLogSql += " order by a.city_id";
		PrepareSQL psql = new PrepareSQL(getLogSql);
		psql.getSQL();
		List<Map> res = jt.queryForList(getLogSql);
		return res;
	}

	// 增加分配IP地址个数权限设置
	public void ADDIPNum(List citycompay, String maxaddr) {
		for (int i = 0; i < citycompay.size(); i++) {
			HashMap cityID = (HashMap) citycompay.get(i);
			log.debug("cityID:"+cityID.size());
			String citycity = (String) cityID.get("city_id");
			
			String sql = "update  city_ipnum set ip_num=" + maxaddr
					+ " where city_id='" + citycity + "' ";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			jt.execute(sql);
			// }
		}
	}

	// 编辑分配IP地址个数权限设置
	public void editIPNum(String citycompay, String maxaddr) {
		if (maxaddr != null) {
			String sql = "update  city_ipnum set ip_num=" + maxaddr
					+ " where city_id='" + citycompay + "'";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			jt.execute(sql);
		}
	}

	// 县名称设置功能
	@SuppressWarnings("unchecked")
	public List<Map> getCountryByCity(String countyName) throws Exception {
		String getLogSql = "select a.city_id,a.country_name,b.city_name from country a,tab_city b where a.city_id=b.city_id and a.city_id='"
				+ countyName + "'";
		getLogSql += " order by a.country_name";
		PrepareSQL psql = new PrepareSQL(getLogSql);
		psql.getSQL();
		List<Map> res = jt.queryForList(getLogSql);
		return res;
	}

	// 增加县名称设置
	public void addcountyname(String city, String county) {
		String sql = "insert into country(city_id,country_name) values('"
				+ city + "','" + county + "')";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		jt.execute(sql);
	}

	// 编辑县名称设置
	public void editcountyname(String city, String county, String xiankk) {
		if (county != null) {
			String sql = "update country set country_name='" + county
					+ "' where city_id='" + city + "' and country_name='"
					+ xiankk + "'";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			jt.execute(sql);
		}
	}

	// 删除县名称设置
	public void deletecounty(String city, String county) {
		String sql = "delete from country where city_id='" + city
				+ "' and country_name='" + county + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		jt.execute(sql);
	}

	// APNIC邮件参数设置
	@SuppressWarnings("unchecked")
	public void getAPNICinfo(String mailto, String whoisserver,
			String apnicpersonal, String apnicmail, String mailfrom,
			String mailserver, String mailprotocol, String mailuser,
			String mailpassword, String netmail, String mailadress) {
		if (mailto != null) {
			log.debug("aaaaaaaaaaaaa");
			String tmpSql = "select * from sysparameter";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				tmpSql = "select mailto,whoisserver,apnicpersonal,apnicmail,mailfrom,mailserver,mailprotocol,mailuser,mailpassword,netmail from sysparameter";
			}

			com.linkage.commons.db.PrepareSQL psql1 = new com.linkage.commons.db.PrepareSQL(tmpSql);
			psql1.getSQL();
			List<Map> map = null;
			String sql = "";
			map = jt.queryForList(tmpSql);
			log.debug("bbbbbbbbbbbbbbbbb" + map.size());
			if (map == null || map.size() == 0) {
				sql = "insert into sysparameter(mailto,whoisserver,apnicpersonal,apnicmail,mailfrom,mailserver,mailprotocol,mailuser,mailpassword,netmail) values('"
						+ mailto
						+ "','"
						+ whoisserver
						+ "','"
						+ apnicpersonal
						+ "','"
						+ apnicmail
						+ "','"
						+ mailfrom
						+ "','"
						+ mailserver
						+ "','"
						+ mailprotocol
						+ "','"
						+ mailuser
						+ "','" + mailpassword + "','" + netmail + "')";
				PrepareSQL psql = new PrepareSQL(sql);
				psql.getSQL();
				jt.execute(sql);
			} else {
				sql = "update sysparameter set mailto='" + mailto
						+ "',whoisserver='" + whoisserver + "',apnicpersonal='"
						+ apnicpersonal + "',apnicmail='" + apnicmail
						+ "',mailfrom='" + mailfrom + "',mailserver='"
						+ mailserver + "',mailprotocol='" + mailprotocol
						+ "',mailuser='" + mailuser + "',mailpassword='"
						+ mailpassword + "',netmail='" + netmail
						+ "' where mailto='" + mailadress + "'";
				PrepareSQL psql = new PrepareSQL(sql);
				psql.getSQL();
				jt.execute(sql);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map> getAPNIClist() throws Exception {
		String getLogSql = "select * from sysparameter";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			getLogSql = "select mailto,whoisserver,apnicpersonal,apnicmail,mailfrom,mailserver,mailprotocol,mailuser,mailpassword,netmail from sysparameter";
		}
		PrepareSQL psql = new PrepareSQL(getLogSql);
		psql.getSQL();
		List<Map> res = jt.queryForList(getLogSql);
		log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + res.size());
		return res;
	}

	// APNIC邮件摸板
	@SuppressWarnings("unchecked")
	public void getApnicMailinfo(String CityOwe1, String city_desc,
			String province_desc, String changed, String status, String mntby,
			String mntlower, String password, String email, String cityadress) {
		if (CityOwe1 != null) {
			String tmpSql = "select * from apnicmailt";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				tmpSql = "select city_id,city_desc,province_desc,changed,status,mntby,mntlower,password,email from apnicmailt";
			}

			List<Map> map = null;
			String sql = "";
			map = jt.queryForList(tmpSql);
			if (map == null) {
				sql = "insert into apnicmailt(city_id,city_desc,province_desc,changed,status,mntby,mntlower,password,email) values('"
						+ CityOwe1
						+ "','"
						+ city_desc
						+ "','"
						+ province_desc
						+ "','"
						+ changed
						+ "','"
						+ status
						+ "','"
						+ mntby
						+ "','"
						+ mntlower
						+ "','"
						+ password
						+ "','"
						+ email
						+ "')";
				PrepareSQL psql = new PrepareSQL(sql);
				psql.getSQL();
				jt.execute(sql);
			} else {
				sql = "update apnicmailt set city_id='" + CityOwe1
						+ "',city_desc='" + city_desc + "',province_desc='"
						+ province_desc + "',changed='" + changed
						+ "',status='" + status + "',mntby='" + mntby
						+ "',mntlower='" + mntlower + "',password='" + password
						+ "',email='" + email + "' where city_id='"
						+ cityadress + "'";
				PrepareSQL psql = new PrepareSQL(sql);
				psql.getSQL();
				jt.execute(sql);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map> getApnicMaillist(String CityOwe1) throws Exception {
		String getLogSql = "select * from apnicmailt where city_id='" + CityOwe1 + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			getLogSql = "select city_id,city_desc,province_desc,changed,status,mntby,mntlower,password,email from apnicmailt where city_id='" + CityOwe1 + "'";
		}

		PrepareSQL psql = new PrepareSQL(getLogSql);
		psql.getSQL();
		List<Map> res = jt.queryForList(getLogSql);
		return res;
	}

	/**
	 * 查询指定网段的子网用户分配信息，在地市用户取消子网划分时使用
	 * 
	 * @param subnet
	 * @param subnetGrp
	 * @param inetMask
	 * @return subnet(ip)、inetmask(掩码位数)、addrnum(地址个数)、usernamezw（用户名称中文）
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getChildSubnetUseripList(String subnet, String subnetGrp,
			int inetMask) {

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String subnetDetailSQLMysql = "select subnetgrp, subnet, inetmask, grandgrp, igroupmask, approve, lowaddr, highaddr, city_id, netmask, " +
					" assign, assigntime, purpose, fhighaddress, fip, flowaddress, subnetcomment, childcount, totaladdr, mailstatus  " +
					" from gw_subnets where subnet=? and subnetgrp=? and inetmask=?";
			pSQL.setSQL(subnetDetailSQLMysql);
		}
		else {
			// 查询网段详细信息
			pSQL.setSQL(subnetDetailSQL);
		}
		pSQL.setString(1, subnet);
		pSQL.setString(2, subnetGrp);
		pSQL.setInt(3, inetMask);
		log.debug("subnetDetailSQL:" + pSQL.getSQL());
		Map<String, String> subnetMap = queryForMap(pSQL.getSQL());
		Subnet root = new Subnet(subnetMap);

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String selectUserIpChildSubnetSQLMysql = "select b.subnet, b.inetmask, b.addrnum, b.usernamezw from gw_subnets a,userip b where a.subnet=b.subnet and a.subnetgrp=b.subnetgrp and a.inetmask=b.inetmask and a.fip<=? and a.fip>=? and a.inetmask>?";
			pSQL.setSQL(selectUserIpChildSubnetSQLMysql);
		}
		else {
			// 查询指定网段的子网用户分配信息
			pSQL.setSQL(selectUserIpChildSubnetSQL);
		}
		pSQL.setString(1, root.getFhighaddress());
		pSQL.setString(2, root.getFlowaddress());
		pSQL.setInt(3, root.getInetMask());
		log.debug("selectUserIpChildSubnetSQL:" + pSQL.getSQL());
		return jt.queryForList(pSQL.getSQL());
	}

	/**
	 * 查询指定网段的子网总表分配情况，在省局用户取消子网划分时使用
	 * 
	 * @param subnet
	 * @param subnetGrp
	 * @param inetMask
	 * @return subnet(ip)、inetmask(子网掩码位数)、city_name(分配城市)
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getChildSubnetIpMainList(String subnet, String subnetGrp,
			int inetMask) {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String subnetDetailSQLMysql = "select subnetgrp, subnet, inetmask, grandgrp, igroupmask, approve, lowaddr, highaddr, city_id, netmask, " +
					" assign, assigntime, purpose, fhighaddress, fip, flowaddress, subnetcomment, childcount, totaladdr, mailstatus  " +
					" from gw_subnets where subnet=? and subnetgrp=? and inetmask=?";
			pSQL.setSQL(subnetDetailSQLMysql);
		}
		else {
			// 查询网段详细信息
			pSQL.setSQL(subnetDetailSQL);
		}
		pSQL.setString(1, subnet);
		pSQL.setString(2, subnetGrp);
		pSQL.setInt(3, inetMask);
		log.debug("subnetDetailSQL:" + pSQL.getSQL());
		Map<String, String> subnetMap = queryForMap(pSQL.getSQL());
		Subnet root = new Subnet(subnetMap);


		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String selectIPMainChildSubentSQLMysql = " select a.subnet, a.inetmask, b.city_name from gw_ipmain a,tab_city b,gw_subnets c where a.city_id= b.city_id and a.subnet= c.subnet and a.inetmask=c.inetmask"
					+ " and c.fip<=? and c.fip>=? and c.inetmask>?";
			pSQL.setSQL(selectIPMainChildSubentSQLMysql);
		}
		else {
			// 查询指定网段的子网总表分配情况
			pSQL.setSQL(selectIPMainChildSubentSQL);
		}

		pSQL.setString(1, root.getFhighaddress());
		pSQL.setString(2, root.getFlowaddress());
		pSQL.setInt(3, root.getInetMask());
		log.debug("selectIPMainChildSubentSQL:" + pSQL.getSQL());
		return jt.queryForList(pSQL.getSQL());
	}

	/**
	 * 取消指定网段的子网划分
	 * 
	 * @param subnet
	 * @param subnetGrp
	 * @param inetMask
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int cancelPartSubnet(String subnet, String subnetGrp, int inetMask) {
		ArrayList<String> sqlList = new ArrayList();

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String subnetDetailSQLMysql = "select subnetgrp, subnet, inetmask, grandgrp, igroupmask, approve, lowaddr, highaddr, city_id, netmask, " +
					" assign, assigntime, purpose, fhighaddress, fip, flowaddress, subnetcomment, childcount, totaladdr, mailstatus  " +
					" from gw_subnets where subnet=? and subnetgrp=? and inetmask=?";
			pSQL.setSQL(subnetDetailSQLMysql);
		}
		else {
			// 查询网段详细信息
			pSQL.setSQL(subnetDetailSQL);
		}
		pSQL.setString(1, subnet);
		pSQL.setString(2, subnetGrp);
		pSQL.setInt(3, inetMask);
		log.debug("subnetDetailSQL:" + pSQL.getSQL());
		Map<String, String> subnetMap = queryForMap(pSQL.getSQL());
		Subnet root = new Subnet(subnetMap);

		// 更新网段信息
		String param = " childcount=0";
		pSQL.setSQL(updateSubnetSQL);
		pSQL.setStringExt(1, param, false);
		pSQL.setString(2, subnet);
		pSQL.setString(3, subnetGrp);
		pSQL.setInt(4, inetMask);
		log.debug("updateSubnetSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		// //删除专线用户申请表中的记录
		// pSQL.setSQL(deleteApplyipSQL);
		// pSQL.setInt(1,root.getInetMask());
		// pSQL.setString(2,root.getFhighaddress());
		// pSQL.setString(3,root.getFlowaddress());
		// log.debug("deleteApplyipSQL:"+pSQL.getSQL());
		// sqlList.add(pSQL.getSQL());
		//		
		// //删除用户表中的信息
		// pSQL.setSQL(deleteUserIPSQL);
		// pSQL.setInt(1,root.getInetMask());
		// pSQL.setString(2,root.getFhighaddress());
		// pSQL.setString(3,root.getFlowaddress());
		// log.debug("deleteUserIPSQL:"+pSQL.getSQL());
		// sqlList.add(pSQL.getSQL());

		// 删除总表中的信息
		pSQL.setSQL(deleteIPMainNotBySelfSQL);
		pSQL.setInt(1, root.getInetMask());
		pSQL.setInt(2, root.getInetMask());
		pSQL.setString(3, root.getFhighaddress());
		pSQL.setString(4, root.getFlowaddress());
		log.debug("deleteIPMainNotBySelfSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		// //把已经分配给专线用户的，并已向apnic注册的网段记录插入delsub表，准备发送去注册
		// pSQL.setSQL(addDelSubSQL);
		// pSQL.setInt(1,root.getInetMask());
		// pSQL.setInt(2,root.getInetMask());
		// pSQL.setString(3,root.getFhighaddress());
		// pSQL.setString(4,root.getFlowaddress());
		// log.debug("addDelSubSQL:"+pSQL.getSQL());
		// sqlList.add(pSQL.getSQL());

		// 删除网段信息
		pSQL.setSQL(deleteSubnetNotBySelfSQL);
		pSQL.setInt(1, root.getInetMask());
		pSQL.setString(2, root.getFhighaddress());
		pSQL.setString(3, root.getFlowaddress());
		log.debug("deleteSubnetNotBySelfSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		String[] sqlArray = new String[sqlList.size()];
		sqlList.toArray(sqlArray);

		// clear
		sqlList = null;

		int[] resultCode = null;
		try {
			resultCode = jt.batchUpdate(sqlArray);
		} catch (Exception e) {
			log.error("cancelPartSubnet fail!");
			e.printStackTrace();
		}
		return resultCode == null ? IPGlobal.DBOPERATION_FAIL
				: IPGlobal.SUNCCESS;
	}

	/**
	 * 更新指定网段的专线用户信息
	 * 
	 * @param subnet
	 *            指定网段的ip
	 * @param subnetGrp
	 *            指定网段的父节点
	 * @param inetMask
	 *            指定网段的子网掩码
	 *            用户状态0：省局用户，1：地市用户
	 * @param paraMap
	 *            参数map
	 * @return IPGlobal.DBOPERATION_FAIL（失败），IPGlobal.SUNCCESS（成功）
	 */
	@SuppressWarnings("unchecked")
	public int modifyToUserSubnet(String subnet, String subnetGrp,
			int inetMask, HashMap<String, String> paraMap) {
		ArrayList<String> sqlList = new ArrayList();
		// 更新原有网段的状态为分配到用户、分配时间为当前时间
		String param = " assign=" + IPGlobal.ASSIGN_TO_USER + ", assigntime="
				+ new DateTimeUtil().getLongTime() + ",mailstatus="
				+ IPGlobal.WAIT_SENDMAIL;
		pSQL.setSQL(updateSubnetSQL);
		pSQL.setStringExt(1, param, false);
		pSQL.setString(2, subnet);
		pSQL.setString(3, subnetGrp);
		pSQL.setInt(4, inetMask);
		log.debug("updateSubnetSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		// 删除apnic去注册表中的信息
		pSQL.setSQL(deleteDelSubSQL);
		pSQL.setString(1, subnet);
		log.debug("deleteDelSubSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		// 删除apnic注册表中的信息
		pSQL.setSQL(deleteApnicMailSQL);
		pSQL.setString(1, subnet);
		log.debug("deleteApnicMailSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		// 手工修改后就会把导入方式改成手工
		paraMap.put("is_import", String.valueOf(IPGlobal.HAND_IMPORT));

		// 更新用户信息
		sqlList.add(modifyUserSql(subnet, subnetGrp, inetMask, paraMap));

		String[] sqlArray = new String[sqlList.size()];
		sqlList.toArray(sqlArray);

		// clear
		sqlList = null;

		int[] resultCode = null;
		try {
			resultCode = jt.batchUpdate(sqlArray);
		} catch (Exception e) {
			log.warn("assignSubnetToUser fail!");
			e.printStackTrace();
		}
		return resultCode == null ? IPGlobal.DBOPERATION_FAIL
				: IPGlobal.SUNCCESS;
	}

	/**
	 * 更新指定网段的总表信息
	 * 
	 * @param subnet
	 * @param subnetGrp
	 * @param inetMask
	 * @param city_id
	 * @param country
	 * @param purpose1
	 * @param purpose2
	 * @param purpose3
	 * @param comment
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int modifyToCitySubnet(String subnet, String subnetGrp,
			int inetMask, String city_id, String country, String purpose1,
			String purpose2, String purpose3, String comment) {
		ArrayList<String> sqlList = new ArrayList();

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String selcetDetailIpMainSubnetSQLMysql = "select a.city_id, b.assigntime " +
					"from gw_ipmain a,gw_subnets b where a.subnet=b.subnet and a.inetmask=b.inetmask and a.city_id =b.subnetgrp and a.subnet=? and a.inetmask=?";
			pSQL.setSQL(selcetDetailIpMainSubnetSQLMysql);
		}
		else {
			pSQL.setSQL(selcetDetailIpMainSubnetSQL);
		}
		pSQL.setString(1, subnet);
		pSQL.setInt(2, inetMask);
		log.debug("getDetailIpMainInfo_SQL:" + pSQL.getSQL());
		Map<String, String> map = queryForMap(pSQL.getSQL());

		// 分配地市发生变化
		if (!city_id.equals(map.get("city_id"))) {
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				String subnetDetailSQLMysql = "select subnetgrp, subnet, inetmask, grandgrp, igroupmask, approve, lowaddr, highaddr, city_id, netmask, " +
						" assign, assigntime, purpose, fhighaddress, fip, flowaddress, subnetcomment, childcount, totaladdr, mailstatus  " +
						" from gw_subnets where subnet=? and subnetgrp=? and inetmask=?";
				pSQL.setSQL(subnetDetailSQLMysql);
			}
			else {
				// 获取指定网段
				pSQL.setSQL(subnetDetailSQL);
			}
			pSQL.setString(1, subnet);
			pSQL.setString(2, subnetGrp);
			pSQL.setInt(3, inetMask);
			log.debug("subnetDetailSQL:" + pSQL.getSQL());
			Map<String, String> subnetMap = queryForMap(pSQL.getSQL());
			Subnet root = new Subnet(subnetMap);

			// //删除专线用户申请表中的记录
			// pSQL.setSQL(deleteApplyipSQL);
			// pSQL.setInt(1,root.getInetMask());
			// pSQL.setString(2,root.getFhighaddress());
			// pSQL.setString(3,root.getFlowaddress());
			// log.debug("deleteApplyipSQL:"+pSQL.getSQL());
			// sqlList.add(pSQL.getSQL());
			//			
			// //删除用户表中的记录
			// pSQL.setSQL(deleteUserIPSQL);
			// pSQL.setInt(1,root.getInetMask());
			// pSQL.setString(2,root.getFhighaddress());
			// pSQL.setString(3,root.getFlowaddress());
			// log.debug("deleteUserIPSQL:"+pSQL.getSQL());
			// sqlList.add(pSQL.getSQL());
			//			
			// //把已经分配给专线用户的，并已向apnic注册的网段记录插入delsub表，准备发送去注册
			// pSQL.setSQL(addDelSubSQL);
			// pSQL.setInt(1,root.getInetMask());
			// pSQL.setInt(2,root.getInetMask());
			// pSQL.setString(3,root.getFhighaddress());
			// pSQL.setString(4,root.getFlowaddress());
			// log.debug("addDelSubSQL:"+pSQL.getSQL());
			// sqlList.add(pSQL.getSQL());

			// 删除网段信息
			pSQL.setSQL(deleteSubnetNotBySelfSQL);
			pSQL.setInt(1, root.getInetMask());
			pSQL.setString(2, root.getFhighaddress());
			pSQL.setString(3, root.getFlowaddress());
			log.debug("deleteSubnetNotBySelfSQL:" + pSQL.getSQL());
			sqlList.add(pSQL.getSQL());

			// 删除分配给地市的网段
			pSQL.setSQL(deleteAssignToCitySubnetSQL);
			pSQL.setString(1, root.getSubnet());
			pSQL.setInt(2, root.getInetMask());
			pSQL.setString(3, root.getSubnetGrp());
			log.debug("deleteAssignToCitySubnetSQL:" + pSQL.getSQL());
			sqlList.add(pSQL.getSQL());

			// 把新分配地市的网段插入数据库
			root.setSubnetGrp(city_id);
			root.setCity_id(city_id);
			root.setComment(comment);
			root.setPurpose(purpose1);
			sqlList.add(addSubnetSql(root));
		}

		// 更新指定网段的总表记录
		pSQL.setSQL(updateIpmainSQL);
		pSQL.setString(1, subnet);
		pSQL.setInt(2, inetMask);
		pSQL.setString(3, city_id);
		if (null != country && !"".equals(country)) {
			pSQL.setString(4, country);
		} else {
			pSQL.setStringExt(4, null, false);
		}
		if (null != purpose1 && !"".equals(purpose1)) {
			pSQL.setString(5, purpose1);
		} else {
			pSQL.setStringExt(5, null, false);
		}
		if (null != purpose2 && !"".equals(purpose2)) {
			pSQL.setString(6, purpose2);
		} else {
			pSQL.setStringExt(6, null, false);
		}
		if (null != purpose3 && !"".equals(purpose3)) {
			pSQL.setString(7, purpose3);
		} else {
			pSQL.setStringExt(7, null, false);
		}
		if (null != comment && !"".equals(comment)) {
			pSQL.setString(8, comment);
		} else {
			pSQL.setStringExt(8, null, false);
		}
		pSQL.setString(9, subnet);
		pSQL.setInt(10, inetMask);
		log.debug("updateIpmainSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		// 更新原有网段的分配时间
		String param = " assigntime=" + new DateTimeUtil().getLongTime();
		pSQL.setSQL(updateSubnetSQL);
		pSQL.setStringExt(1, param, false);
		pSQL.setString(2, subnet);
		pSQL.setString(3, subnetGrp);
		pSQL.setInt(4, inetMask);
		log.debug("updateSubnetSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		String[] sqlArray = new String[sqlList.size()];
		sqlList.toArray(sqlArray);

		// clear
		sqlList = null;

		int[] resultCode = null;
		try {
			resultCode = jt.batchUpdate(sqlArray);
		} catch (Exception e) {
			log.warn("modifyToCitySubnet fail!");
			e.printStackTrace();
		}
		return resultCode == null ? IPGlobal.DBOPERATION_FAIL
				: IPGlobal.SUNCCESS;
	}

	/**
	 * 查询指定网段的专线用户分配情况
	 * 
	 * @param subnet
	 *            指定网段的IP
	 * @param subnetGrp
	 *            指定网段的父节点
	 * @param inetMask
	 *            指定网段的子网掩码
	 * @return userip中的对应关系
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getDetailUserIPInfo(String subnet,
			String subnetGrp, int inetMask) {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String selectUserIPSubentSQLMysql = "select subnetgrp, subnet, inetmask, is_import, addrnum, usernamezw, usernameyw, usernamepyjc, usernameywjc, address," +
					"managerhandle, techname, techduty, techphone, techemail, techfax, techaddr, techaddre, techpc, technamep, " +
					"netname, netnamejc, netnamee, rwaddr, applydate, permitdate, purpose, memo, cntmode, cntspeed," +
					"cntaddr, localun, country from userip where subnet=? and subnetgrp=? and inetmask=?";
			pSQL.setSQL(selectUserIPSubentSQLMysql);
		}
		else {
			pSQL.setSQL(selectUserIPSubentSQL);
		}
		pSQL.setString(1, subnet);
		pSQL.setString(2, subnetGrp);
		pSQL.setInt(3, inetMask);
		log.debug("getDetailUserIPInfo_SQL:" + pSQL.getSQL());
		Map map = queryForMap(pSQL.getSQL());
		map.put("applydate", new DateTimeUtil(Long.parseLong((String) map
				.get("applydate")) * 1000).getDate());
		return map;
	}

	/**
	 * 获取指定网段总表分配情况
	 * 
	 * @param subnet
	 *            指定网段的ip
	 * @param inetMask
	 *            指定网段的子网掩码
	 * @return subnet(IP地址)，city_id(地市)，country(县)，purpose1（用途1）,purpose2(用途2)，purpose3（用途3），subnetcomment（备注）
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getDetailIpMainInfo(String subnet, int inetMask) {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String selcetDetailIpMainSubnetSQLMysql = "select a.city_id, b.assigntime " +
					"from gw_ipmain a,gw_subnets b where a.subnet=b.subnet and a.inetmask=b.inetmask and a.city_id =b.subnetgrp and a.subnet=? and a.inetmask=?";
			pSQL.setSQL(selcetDetailIpMainSubnetSQLMysql);
		}
		else {
			pSQL.setSQL(selcetDetailIpMainSubnetSQL);
		}
		pSQL.setString(1, subnet);
		pSQL.setInt(2, inetMask);
		log.debug("getDetailIpMainInfo_SQL:" + pSQL.getSQL());
		Map<String, String> map = queryForMap(pSQL.getSQL());
		map.put("assigntime", new DateTimeUtil(Long.parseLong((String) map
				.get("assigntime")) * 1000).getDate());
		return map;
	}

	/**
	 * 查询某个网段的详细信息
	 * 
	 * @param subnet
	 *            指定网段的ip
	 * @param subnetGrp
	 *            指定网段的父节点
	 * @param inetMask
	 *            指定网段的子网掩码
	 * @return netmask（子网掩码），childcount（子网个数），totaladdr（总地址数），highaddr（最高地址、广播地址），lowaddr（最低地址）
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getDetailSubnet(String subnet, String subnetGrp,
			int inetMask) {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String subnetDetailSQLMysql = "select subnetgrp, subnet, inetmask, grandgrp, igroupmask, approve, lowaddr, highaddr, city_id, netmask, " +
					" assign, assigntime, purpose, fhighaddress, fip, flowaddress, subnetcomment, childcount, totaladdr, mailstatus  " +
					" from gw_subnets where subnet=? and subnetgrp=? and inetmask=?";
			pSQL.setSQL(subnetDetailSQLMysql);
		}
		else {
			pSQL.setSQL(subnetDetailSQL);
		}
		pSQL.setString(1, subnet);
		pSQL.setString(2, subnetGrp);
		pSQL.setInt(3, inetMask);
		log.debug("getDetailSubnet_SQL:" + pSQL.getSQL());
		Map<String, String> map = queryForMap(pSQL.getSQL());
		map.put("assigntime", new DateTimeUtil(Long.parseLong(map
				.get("assigntime")) * 1000).getDate());
		return map;
	}

	/**
	 * 地市用户分配IP到专线用户，要先使用这个方法，看是否能分配， 不能分配，要跳转到填写申请单的页面
	 * 
	 * @param inetMask
	 *            指定网段的子网掩码
	 * @param ip_cityID
	 *            省局用户，返回当前用户属地ID，地市用户，返回用户所在一级地市ID
	 * @return
	 */
	public boolean isAllowAssign(String subnet, String subnetGrp, int inetMask,
			String ip_cityID) {
		boolean isAllow = true;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String subnetDetailSQLMysql = "select subnetgrp, subnet, inetmask, grandgrp, igroupmask, approve, lowaddr, highaddr, city_id, netmask, " +
					" assign, assigntime, purpose, fhighaddress, fip, flowaddress, subnetcomment, childcount, totaladdr, mailstatus  " +
					" from gw_subnets where subnet=? and subnetgrp=? and inetmask=?";
			pSQL.setSQL(subnetDetailSQLMysql);
		}
		else {
			// 查询网段详细信息
			pSQL.setSQL(subnetDetailSQL);
		}
		pSQL.setString(1, subnet);
		pSQL.setString(2, subnetGrp);
		pSQL.setInt(3, inetMask);
		log.debug("subnetDetailSQL:" + pSQL.getSQL());
		Map<String, String> subnetMap = queryForMap(pSQL.getSQL());
		Subnet root = new Subnet(subnetMap);

		// 对应的网段approve状态为不同意的情况才需要判断是否在允许分配的地址数范围内
		if (root.getApprove() != IPGlobal.AGREE) {
			int hostNumber = IpTool.getHostNumber(inetMask);
			int maxNum = ipManagerDao.getAssignNumByCity(ip_cityID);
			// 数据库中有配置这个地市能分配的最大地址数目，并且这个子网的地址数目超过了能分配的地址数目
			if (hostNumber > maxNum) {
				isAllow = false;
			}
		}
		return isAllow;
	}

	/**
	 * 分配IP到专线用户
	 * 
	 * @param subnet
	 *            指定网段的ip
	 * @param subnetGrp
	 *            指定网段的父节点
	 * @param inetMask
	 *            指定网段的子网掩码
	 * @param userState
	 *            用户状态0：省局用户，1：地市用户
	 * @param paraMap
	 *            参数map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int assignSubnetToUser(String subnet, String subnetGrp,
			int inetMask, HashMap<String, String> paraMap) {
		ArrayList<String> sqlList = new ArrayList();
		// 更新原有网段的状态为分配到用户、分配时间为当前时间
		String param = " assign=" + IPGlobal.ASSIGN_TO_USER + ", assigntime="
				+ new DateTimeUtil().getLongTime() + ",mailstatus="
				+ IPGlobal.WAIT_SENDMAIL;
		pSQL.setSQL(updateSubnetSQL);
		pSQL.setStringExt(1, param, false);
		pSQL.setString(2, subnet);
		pSQL.setString(3, subnetGrp);
		pSQL.setInt(4, inetMask);
		log.debug("updateSubnetSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		// 删除apnic去注册表中的信息
		pSQL.setSQL(deleteDelSubSQL);
		pSQL.setString(1, subnet);
		log.debug("deleteDelSubSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		// 删除apnic注册表中的信息
		pSQL.setSQL(deleteApnicMailSQL);
		pSQL.setString(1, subnet);
		log.debug("deleteApnicMailSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		paraMap.put("is_import", String.valueOf(IPGlobal.HAND_IMPORT));

		// 增加用户信息
		sqlList.add(addUserSql(subnet, subnetGrp, inetMask, paraMap));

		String[] sqlArray = new String[sqlList.size()];
		sqlList.toArray(sqlArray);

		// clear
		sqlList = null;

		int[] resultCode = null;
		try {
			resultCode = jt.batchUpdate(sqlArray);
		} catch (Exception e) {
			log.warn("assignSubnetToUser fail!");
			e.printStackTrace();
		}
		return resultCode == null ? IPGlobal.DBOPERATION_FAIL
				: IPGlobal.SUNCCESS;
	}

	/**
	 * 更新指定网段专线用户信息的SQL
	 * 
	 * @param subnet
	 *            指定网段的ip
	 * @param subnetGrp
	 *            指定网段的父节点
	 * @param inetMask
	 *            指定网段的子网掩码
	 * @param paraMap
	 *            专线用户的一些参数，与userip对应
	 * @return
	 */
	private String modifyUserSql(String subnet, String subnetGrp, int inetMask,
			HashMap<String, String> paraMap) {
		pSQL.setSQL(updateUserSQL);
		pSQL.setString(1, subnetGrp);
		pSQL.setString(2, subnet);
		pSQL.setInt(3, inetMask);
		pSQL.setInt(4, Integer.parseInt(paraMap.get("is_import")));
		if (null == paraMap.get("addrnum") || "".equals(paraMap.get("addrnum"))) {
			pSQL.setStringExt(5, null, false);
		} else {
			pSQL.setInt(5, Integer.parseInt(paraMap.get("addrnum")));
		}
		if (null == paraMap.get("usernamezw")
				|| "".equals(paraMap.get("usernamezw"))) {
			pSQL.setStringExt(6, null, false);
		} else {
			pSQL.setString(6, paraMap.get("usernamezw"));
		}
		if (null == paraMap.get("usernameyw")
				|| "".equals(paraMap.get("usernameyw"))) {
			pSQL.setStringExt(7, null, false);
		} else {
			pSQL.setString(7, paraMap.get("usernameyw"));
		}
		if (null == paraMap.get("usernamepyjc")
				|| "".equals(paraMap.get("usernamepyjc"))) {
			pSQL.setStringExt(8, null, false);
		} else {
			pSQL.setString(8, paraMap.get("usernamepyjc"));
		}
		if (null == paraMap.get("usernameywjc")
				|| "".equals(paraMap.get("usernameywjc"))) {
			pSQL.setStringExt(9, null, false);
		} else {
			pSQL.setString(9, paraMap.get("usernameywjc"));
		}
		if (null == paraMap.get("address") || "".equals(paraMap.get("address"))) {
			pSQL.setStringExt(10, null, false);
		} else {
			pSQL.setString(10, paraMap.get("address"));
		}
		if (null == paraMap.get("managerhandle")
				|| "".equals(paraMap.get("managerhandle"))) {
			pSQL.setStringExt(11, null, false);
		} else {
			pSQL.setString(11, paraMap.get("managerhandle"));
		}
		/*
		 * if(null==paraMap.get("managername")||"".equals(paraMap.get("managerhandle"))) {
		 * pSQL.setStringExt(12,null,false); } else {
		 * pSQL.setString(12,paraMap.get("managername")); }
		 * if(null==paraMap.get("managernamep")||"".equals(paraMap.get("managernamep"))) {
		 * pSQL.setStringExt(13,null,false); } else {
		 * pSQL.setString(13,paraMap.get("managernamep")); }
		 * if(null==paraMap.get("manageraddre")||"".equals(paraMap.get("manageraddre"))) {
		 * pSQL.setStringExt(14,null,false); } else {
		 * pSQL.setString(14,paraMap.get("manageraddre")); }
		 * if(null==paraMap.get("managerduty")||"".equals(paraMap.get("managerduty"))) {
		 * pSQL.setStringExt(15,null,false); } else {
		 * pSQL.setString(15,paraMap.get("managerduty")); }
		 * if(null==paraMap.get("managerphone")||"".equals(paraMap.get("managerphone"))) {
		 * pSQL.setStringExt(16,null,false); } else {
		 * pSQL.setString(16,paraMap.get("managerphone")); }
		 * if(null==paraMap.get("manageremail")||"".equals(paraMap.get("manageremail"))) {
		 * pSQL.setStringExt(17,null,false); } else {
		 * pSQL.setString(17,paraMap.get("manageremail")); }
		 * if(null==paraMap.get("managerfax")||"".equals(paraMap.get("managerfax"))) {
		 * pSQL.setStringExt(18,null,false); } else {
		 * pSQL.setString(18,paraMap.get("managerfax")); }
		 * if(null==paraMap.get("manageraddress")||"".equals(paraMap.get("manageraddress"))) {
		 * pSQL.setStringExt(19,null,false); } else {
		 * pSQL.setString(19,paraMap.get("manageraddress")); }
		 * if(null==paraMap.get("managerpc")||"".equals(paraMap.get("managerpc"))) {
		 * pSQL.setStringExt(20,null,false); } else {
		 * pSQL.setString(20,paraMap.get("managerpc")); }
		 * if(null==paraMap.get("techhandle")||"".equals(paraMap.get("techhandle"))) {
		 * pSQL.setStringExt(21,null,false); } else {
		 * pSQL.setString(21,paraMap.get("techhandle")); }
		 */
		if (null == paraMap.get("techname")
				|| "".equals(paraMap.get("techname"))) {
			pSQL.setStringExt(12, null, false);
		} else {
			pSQL.setString(12, paraMap.get("techname"));
		}
		if (null == paraMap.get("techduty")
				|| "".equals(paraMap.get("techduty"))) {
			pSQL.setStringExt(13, null, false);
		} else {
			pSQL.setString(13, paraMap.get("techduty"));
		}
		if (null == paraMap.get("techphone")
				|| "".equals(paraMap.get("techphone"))) {
			pSQL.setStringExt(14, null, false);
		} else {
			pSQL.setString(14, paraMap.get("techphone"));
		}
		if (null == paraMap.get("techemail")
				|| "".equals(paraMap.get("techemail"))) {
			pSQL.setStringExt(15, null, false);
		} else {
			pSQL.setString(15, paraMap.get("techemail"));
		}
		if (null == paraMap.get("techfax") || "".equals(paraMap.get("techfax"))) {
			pSQL.setStringExt(16, null, false);
		} else {
			pSQL.setString(16, paraMap.get("techfax"));
		}
		if (null == paraMap.get("techaddr")
				|| "".equals(paraMap.get("techaddr"))) {
			pSQL.setStringExt(17, null, false);
		} else {
			pSQL.setString(17, paraMap.get("techaddr"));
		}
		if (null == paraMap.get("techaddre")
				|| "".equals(paraMap.get("techaddre"))) {
			pSQL.setStringExt(18, null, false);
		} else {
			pSQL.setString(18, paraMap.get("techaddre"));
		}
		if (null == paraMap.get("techpc") || "".equals(paraMap.get("techpc"))) {
			pSQL.setStringExt(19, null, false);
		} else {
			pSQL.setString(19, paraMap.get("techpc"));
		}
		if (null == paraMap.get("technamep")
				|| "".equals(paraMap.get("technamep"))) {
			pSQL.setStringExt(20, null, false);
		} else {
			pSQL.setString(20, paraMap.get("technamep"));
		}
		if (null == paraMap.get("netname") || "".equals(paraMap.get("netname"))) {
			pSQL.setStringExt(21, null, false);
		} else {
			pSQL.setString(21, paraMap.get("netname"));
		}
		if (null == paraMap.get("netnamejc")
				|| "".equals(paraMap.get("netnamejc"))) {
			pSQL.setStringExt(22, null, false);
		} else {
			pSQL.setString(22, paraMap.get("netnamejc"));
		}
		if (null == paraMap.get("netnamee")
				|| "".equals(paraMap.get("netnamee"))) {
			pSQL.setStringExt(23, null, false);
		} else {
			pSQL.setString(23, paraMap.get("netnamee"));
		}
		if (null == paraMap.get("rwaddr") || "".equals(paraMap.get("rwaddr"))) {
			pSQL.setStringExt(24, null, false);
		} else {
			pSQL.setString(24, paraMap.get("rwaddr"));
		}
		pSQL.setLong(25, new DateTimeUtil().getLongTime());
		pSQL.setLong(26, new DateTimeUtil().getLongTime());
		if (null == paraMap.get("purpose") || "".equals(paraMap.get("purpose"))) {
			pSQL.setStringExt(27, null, false);
		} else {
			pSQL.setString(27, paraMap.get("purpose"));
		}
		if (null == paraMap.get("memo") || "".equals(paraMap.get("memo"))) {
			pSQL.setStringExt(28, null, false);
		} else {
			pSQL.setString(28, paraMap.get("memo"));
		}
		if (null == paraMap.get("cntmode") || "".equals(paraMap.get("cntmode"))) {
			pSQL.setStringExt(29, null, false);
		} else {
			pSQL.setString(29, paraMap.get("cntmode"));
		}
		if (null == paraMap.get("cntspeed")
				|| "".equals(paraMap.get("cntspeed"))) {
			pSQL.setStringExt(30, null, false);
		} else {
			pSQL.setString(30, paraMap.get("cntspeed"));
		}
		if (null == paraMap.get("cntaddr") || "".equals(paraMap.get("cntaddr"))) {
			pSQL.setStringExt(31, null, false);
		} else {
			pSQL.setString(31, paraMap.get("cntaddr"));
		}
		if (null == paraMap.get("localun") || "".equals(paraMap.get("localun"))) {
			pSQL.setStringExt(32, null, false);
		} else {
			pSQL.setString(32, paraMap.get("localun"));
		}
		if (null == paraMap.get("country") || "".equals(paraMap.get("country"))) {
			pSQL.setStringExt(33, null, false);
		} else {
			pSQL.setString(33, paraMap.get("country"));
		}
		pSQL.setString(34, subnet);
		pSQL.setString(35, subnetGrp);
		pSQL.setInt(36, inetMask);
		log.debug("updateUserSQL:" + pSQL.getSQL());
		return pSQL.getSQL();
	}

	/**
	 * 
	 * @param paraMap
	 * @return
	 */
	private String addUserSql(String subnet, String subnetGrp, int inetMask,
			HashMap<String, String> paraMap) {
		pSQL.setSQL(insertUserSQL);
		pSQL.setString(1, subnetGrp);
		pSQL.setString(2, subnet);
		pSQL.setInt(3, inetMask);
		pSQL.setInt(4, Integer.parseInt(paraMap.get("is_import")));
		if (null == paraMap.get("addrnum") || "".equals(paraMap.get("addrnum"))) {
			log.debug("addrnum is null!");
			pSQL.setStringExt(5, null, false);
		} else {
			pSQL.setInt(5, Integer.parseInt(paraMap.get("addrnum")));
		}
		if (null == paraMap.get("usernamezw")
				|| "".equals(paraMap.get("usernamezw"))) {
			pSQL.setStringExt(6, null, false);
		} else {
			pSQL.setString(6, paraMap.get("usernamezw"));
		}
		if (null == paraMap.get("usernameyw")
				|| "".equals(paraMap.get("usernameyw"))) {
			pSQL.setStringExt(7, null, false);
		} else {
			pSQL.setString(7, paraMap.get("usernameyw"));
		}
		if (null == paraMap.get("usernamepyjc")
				|| "".equals(paraMap.get("usernamepyjc"))) {
			pSQL.setStringExt(8, null, false);
		} else {
			pSQL.setString(8, paraMap.get("usernamepyjc"));
		}
		if (null == paraMap.get("usernameywjc")
				|| "".equals(paraMap.get("usernameywjc"))) {
			pSQL.setStringExt(9, null, false);
		} else {
			pSQL.setString(9, paraMap.get("usernameywjc"));
		}
		if (null == paraMap.get("address") || "".equals(paraMap.get("address"))) {
			pSQL.setStringExt(10, null, false);
		} else {
			pSQL.setString(10, paraMap.get("address"));
		}
		if (null == paraMap.get("managerhandle")
				|| "".equals(paraMap.get("managerhandle"))) {
			pSQL.setStringExt(11, null, false);
		} else {
			pSQL.setString(11, paraMap.get("managerhandle"));
		}
		/*
		 * if(null==paraMap.get("managername")||"".equals(paraMap.get("managerhandle"))) {
		 * pSQL.setStringExt(12,null,false); } else {
		 * pSQL.setString(12,paraMap.get("managername")); }
		 * if(null==paraMap.get("managernamep")||"".equals(paraMap.get("managernamep"))) {
		 * pSQL.setStringExt(13,null,false); } else {
		 * pSQL.setString(13,paraMap.get("managernamep")); }
		 * if(null==paraMap.get("manageraddre")||"".equals(paraMap.get("manageraddre"))) {
		 * pSQL.setStringExt(14,null,false); } else {
		 * pSQL.setString(14,paraMap.get("manageraddre")); }
		 * if(null==paraMap.get("managerduty")||"".equals(paraMap.get("managerduty"))) {
		 * pSQL.setStringExt(15,null,false); } else {
		 * pSQL.setString(15,paraMap.get("managerduty")); }
		 * if(null==paraMap.get("managerphone")||"".equals(paraMap.get("managerphone"))) {
		 * pSQL.setStringExt(16,null,false); } else {
		 * pSQL.setString(16,paraMap.get("managerphone")); }
		 * if(null==paraMap.get("manageremail")||"".equals(paraMap.get("manageremail"))) {
		 * pSQL.setStringExt(17,null,false); } else {
		 * pSQL.setString(17,paraMap.get("manageremail")); }
		 * if(null==paraMap.get("managerfax")||"".equals(paraMap.get("managerfax"))) {
		 * pSQL.setStringExt(18,null,false); } else {
		 * pSQL.setString(18,paraMap.get("managerfax")); }
		 * if(null==paraMap.get("manageraddress")||"".equals(paraMap.get("manageraddress"))) {
		 * pSQL.setStringExt(19,null,false); } else {
		 * pSQL.setString(19,paraMap.get("manageraddress")); }
		 * if(null==paraMap.get("managerpc")||"".equals(paraMap.get("managerpc"))) {
		 * pSQL.setStringExt(20,null,false); } else {
		 * pSQL.setString(20,paraMap.get("managerpc")); }
		 * if(null==paraMap.get("techhandle")||"".equals(paraMap.get("techhandle"))) {
		 * pSQL.setStringExt(21,null,false); } else {
		 * pSQL.setString(21,paraMap.get("techhandle")); }
		 */
		if (null == paraMap.get("techname")
				|| "".equals(paraMap.get("techname"))) {
			pSQL.setStringExt(12, null, false);
		} else {
			pSQL.setString(12, paraMap.get("techname"));
		}
		if (null == paraMap.get("techduty")
				|| "".equals(paraMap.get("techduty"))) {
			pSQL.setStringExt(13, null, false);
		} else {
			pSQL.setString(13, paraMap.get("techduty"));
		}
		if (null == paraMap.get("techphone")
				|| "".equals(paraMap.get("techphone"))) {
			pSQL.setStringExt(14, null, false);
		} else {
			pSQL.setString(14, paraMap.get("techphone"));
		}
		if (null == paraMap.get("techemail")
				|| "".equals(paraMap.get("techemail"))) {
			pSQL.setStringExt(15, null, false);
		} else {
			pSQL.setString(15, paraMap.get("techemail"));
		}
		if (null == paraMap.get("techfax") || "".equals(paraMap.get("techfax"))) {
			pSQL.setStringExt(16, null, false);
		} else {
			pSQL.setString(16, paraMap.get("techfax"));
		}
		if (null == paraMap.get("techaddr")
				|| "".equals(paraMap.get("techaddr"))) {
			pSQL.setStringExt(17, null, false);
		} else {
			pSQL.setString(17, paraMap.get("techaddr"));
		}
		if (null == paraMap.get("techaddre")
				|| "".equals(paraMap.get("techaddre"))) {
			pSQL.setStringExt(18, null, false);
		} else {
			pSQL.setString(18, paraMap.get("techaddre"));
		}
		if (null == paraMap.get("techpc") || "".equals(paraMap.get("techpc"))) {
			pSQL.setStringExt(19, null, false);
		} else {
			pSQL.setString(19, paraMap.get("techpc"));
		}
		if (null == paraMap.get("technamep")
				|| "".equals(paraMap.get("technamep"))) {
			pSQL.setStringExt(20, null, false);
		} else {
			pSQL.setString(20, paraMap.get("technamep"));
		}
		if (null == paraMap.get("netname") || "".equals(paraMap.get("netname"))) {
			pSQL.setStringExt(21, null, false);
		} else {
			pSQL.setString(21, paraMap.get("netname"));
		}
		if (null == paraMap.get("netnamejc")
				|| "".equals(paraMap.get("netnamejc"))) {
			pSQL.setStringExt(22, null, false);
		} else {
			pSQL.setString(22, paraMap.get("netnamejc"));
		}
		if (null == paraMap.get("netnamee")
				|| "".equals(paraMap.get("netnamee"))) {
			pSQL.setStringExt(23, null, false);
		} else {
			pSQL.setString(23, paraMap.get("netnamee"));
		}
		if (null == paraMap.get("rwaddr") || "".equals(paraMap.get("rwaddr"))) {
			pSQL.setStringExt(24, null, false);
		} else {
			pSQL.setString(24, paraMap.get("rwaddr"));
		}
		pSQL.setLong(25, new DateTimeUtil().getLongTime());
		pSQL.setLong(26, new DateTimeUtil().getLongTime());
		if (null == paraMap.get("purpose") || "".equals(paraMap.get("purpose"))) {
			pSQL.setStringExt(27, null, false);
		} else {
			pSQL.setString(27, paraMap.get("purpose"));
		}
		if (null == paraMap.get("memo") || "".equals(paraMap.get("memo"))) {
			pSQL.setStringExt(28, null, false);
		} else {
			pSQL.setString(28, paraMap.get("memo"));
		}
		if (null == paraMap.get("cntmode") || "".equals(paraMap.get("cntmode"))) {
			pSQL.setStringExt(29, null, false);
		} else {
			pSQL.setString(29, paraMap.get("cntmode"));
		}
		if (null == paraMap.get("cntspeed")
				|| "".equals(paraMap.get("cntspeed"))) {
			pSQL.setStringExt(30, null, false);
		} else {
			pSQL.setString(30, paraMap.get("cntspeed"));
		}
		if (null == paraMap.get("cntaddr") || "".equals(paraMap.get("cntaddr"))) {
			pSQL.setStringExt(31, null, false);
		} else {
			pSQL.setString(31, paraMap.get("cntaddr"));
		}
		if (null == paraMap.get("localun") || "".equals(paraMap.get("localun"))) {
			pSQL.setStringExt(32, null, false);
		} else {
			pSQL.setString(32, paraMap.get("localun"));
		}
		if (null == paraMap.get("country") || "".equals(paraMap.get("country"))) {
			pSQL.setStringExt(33, null, false);
		} else {
			pSQL.setString(33, paraMap.get("country"));
		}
		log.debug("insertUserSQL:" + pSQL.getSQL());
		return pSQL.getSQL();
	}

	/**
	 * 省局用户分配IP到地市
	 * 
	 * @param subnet
	 * @param subnetGrp
	 * @param inetMask
	 * @param city_id
	 *            页面上选中的市
	 * @param country
	 *            页面上选中的县
	 * @param purpose1
	 * @param purpose2
	 * @param purpose3
	 * @param comment
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int assignSubnetToCity(String subnet, String subnetGrp,
			int inetMask, String city_id, String country, String purpose1,
			String purpose2, String purpose3, String comment) {
		ArrayList<String> sqlList = new ArrayList();

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String subnetDetailSQLMysql = "select subnetgrp, subnet, inetmask, grandgrp, igroupmask, approve, lowaddr, highaddr, city_id, netmask, " +
					" assign, assigntime, purpose, fhighaddress, fip, flowaddress, subnetcomment, childcount, totaladdr, mailstatus  " +
					" from gw_subnets where subnet=? and subnetgrp=? and inetmask=?";
			pSQL.setSQL(subnetDetailSQLMysql);
		}
		else {
			// 准备分配给地市的网段信息入库
			pSQL.setSQL(subnetDetailSQL);
		}
		pSQL.setString(1, subnet);
		pSQL.setString(2, subnetGrp);
		pSQL.setInt(3, inetMask);
		log.debug("subnetDetailSQL:" + pSQL.getSQL());
		Map<String, String> subnetMap = queryForMap(pSQL.getSQL());
		Subnet subnetObj = new Subnet(subnetMap);
		subnetObj.setSubnetGrp(city_id);
		subnetObj.setCity_id(city_id);
		subnetObj.setComment(comment);
		subnetObj.setPurpose(purpose1);
		sqlList.add(addSubnetSql(subnetObj));

		// 网段信息入总表
		pSQL.setSQL(insertIpmainSQL);
		pSQL.setString(1, subnet);
		pSQL.setInt(2, inetMask);
		pSQL.setString(3, city_id);
		if (null != country && !"".equals(country)) {
			pSQL.setString(4, country);
		} else {
			pSQL.setStringExt(4, null, false);
		}
		if (null != purpose1 && !"".equals(purpose1)) {
			pSQL.setString(5, purpose1);
		} else {
			pSQL.setStringExt(5, null, false);
		}
		if (null != purpose2 && !"".equals(purpose2)) {
			pSQL.setString(6, purpose2);
		} else {
			pSQL.setStringExt(6, null, false);
		}
		if (null != purpose3 && !"".equals(purpose3)) {
			pSQL.setString(7, purpose3);
		} else {
			pSQL.setStringExt(7, null, false);
		}
		if (null != comment && !"".equals(comment)) {
			pSQL.setString(8, comment);
		} else {
			pSQL.setStringExt(8, null, false);
		}
		log.debug("insertIpmainSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		// 更新原有网段的分配状态、分配时间
		String param = " assign=" + IPGlobal.ASSIGN_TO_CITY + ", assigntime="
				+ new DateTimeUtil().getLongTime();
		pSQL.setSQL(updateSubnetSQL);
		pSQL.setStringExt(1, param, false);
		pSQL.setString(2, subnet);
		pSQL.setString(3, subnetGrp);
		pSQL.setInt(4, inetMask);
		log.debug("updateSubnetSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		String[] sqlArray = new String[sqlList.size()];
		sqlList.toArray(sqlArray);

		// clear
		sqlList = null;
		int[] resultCode = null;

		try {
			resultCode = jt.batchUpdate(sqlArray);
		} catch (Exception e) {
			log.error("assignSubnetToCity fail!   subnet:" + subnet
					+ "  subnetGrp:" + subnetGrp + "  inetMask:" + inetMask);
			e.printStackTrace();
		}

		return resultCode == null ? IPGlobal.DBOPERATION_FAIL
				: IPGlobal.SUNCCESS;
	}

	/**
	 * 对指定网段进行子网划分
	 * 
	 * @param subnet
	 *            指定网段的ip地址
	 * @param subnetGrp
	 *            指定网段的父节点
	 * @param inetMask
	 *            指定网段的子网掩码位数
	 * @param flag
	 *            是否级联，true：级联，false：不级联
	 * @param childInetMask
	 *            要划分的子网掩码位数
	 * @return 指定网段不能划分子网（IPGlobal.IS_FORBID）、因划分的层次数超过8或子网个数超过256，不允许划分（IPGlobal.OVER_MAXLEVEl）
	 *         数据库失败（IPGlobal.DBOPERATION_FAIL）、成功（IPGlobal.SUNCCESS）
	 */
	@SuppressWarnings("unchecked")
	public int partSubnet(String subnet, String subnetGrp, int inetMask,
			boolean flag, int childInetMask) {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String subnetDetailSQLMysql = "select subnetgrp, subnet, inetmask, grandgrp, igroupmask, approve, lowaddr, highaddr, city_id, netmask, " +
					" assign, assigntime, purpose, fhighaddress, fip, flowaddress, subnetcomment, childcount, totaladdr, mailstatus  " +
					" from gw_subnets where subnet=? and subnetgrp=? and inetmask=?";
			pSQL.setSQL(subnetDetailSQLMysql);
		}
		else {
			pSQL.setSQL(subnetDetailSQL);
		}
		pSQL.setString(1, subnet);
		pSQL.setString(2, subnetGrp);
		pSQL.setInt(3, inetMask);
		log.debug("subnetDetailSQL:" + pSQL.getSQL());
		Map<String, String> subnetMap = queryForMap(pSQL.getSQL());
		Subnet subnetObj = new Subnet(subnetMap);

		// 已分配或已划分的情况
		if (IPGlobal.NOT_ASSIGN != subnetObj.getAssign()
				|| subnetObj.getChildCount() > 0 || childInetMask > 30
				|| inetMask >= childInetMask) {
			log.warn("this subnet(subnet:" + subnet + "    inetMask:"
					+ inetMask + "    subnetGrp:" + subnetGrp
					+ "    childInetMask:" + childInetMask + "   flag" + flag
					+ ") param error!");
			return IPGlobal.IS_FORBID;
		}

		ArrayList<String> sqlList = new ArrayList();

		// 级联划分
		if (flag) {
			int level = childInetMask - inetMask;
			if (level > IPGlobal.MAX_LEVEL) {
				log.warn("level(" + level + ") is too many!");
				return IPGlobal.OVER_MAXLEVEl;
			}
			int netNumber = 2;
			// 更新划分子网的子网数和approve状态
			pSQL.setSQL(updateSubnetChildCountSQL);
			pSQL.setInt(1, netNumber);
			pSQL.setInt(2, IPGlobal.NOT_CHECK);
			pSQL.setString(3, subnetObj.getSubnet());
			pSQL.setString(4, subnetObj.getSubnetGrp());
			pSQL.setInt(5, subnetObj.getInetMask());
			log.debug("updateSubnetChildCountSQL:" + pSQL.getSQL());
			sqlList.add(pSQL.getSQL());

			// //删除专线用户申请表中，对这个ip的申请记录
			// pSQL.setSQL(deleteApplyipOFSubnetSQL);
			// pSQL.setString(1,subnetObj.getSubnet());
			// pSQL.setString(2,subnetObj.getSubnetGrp());
			// pSQL.setInt(3,subnetObj.getInetMask());
			// log.debug("deleteApplyipOFSubnetSQL:"+pSQL.getSQL());
			// sqlList.add(pSQL.getSQL());

			getSerialPartSubnetSqlList(childInetMask, subnetObj, sqlList);

		}
		// 非级联划分
		else {
			int hostNumber = IpTool.getHostNumber(childInetMask); // 主机数
			int netNumber = IpTool.getNetNumber(inetMask, childInetMask); // 子网个数

			if (netNumber > IPGlobal.MAX_NETNUMBER) {
				log.warn("netNumber(" + netNumber + ") is too big!");
				return IPGlobal.OVER_MAXLEVEl;
			}

			// 更新划分子网的子网数和approve状态
			pSQL.setSQL(updateSubnetChildCountSQL);
			pSQL.setInt(1, netNumber);
			pSQL.setInt(2, IPGlobal.NOT_CHECK);
			pSQL.setString(3, subnetObj.getSubnet());
			pSQL.setString(4, subnetObj.getSubnetGrp());
			pSQL.setInt(5, subnetObj.getInetMask());
			log.debug("updateSubnetChildCountSQL:" + pSQL.getSQL());
			sqlList.add(pSQL.getSQL());

			// //删除专线用户申请表中，对这个ip的申请记录
			// pSQL.setSQL(deleteApplyipOFSubnetSQL);
			// pSQL.setString(1,subnetObj.getSubnet());
			// pSQL.setString(2,subnetObj.getSubnetGrp());
			// pSQL.setInt(3,subnetObj.getInetMask());
			// log.debug("deleteApplyipOFSubnetSQL:"+pSQL.getSQL());
			// sqlList.add(pSQL.getSQL());

			// 设置子网的公共参数
			subnetObj.setChildCount(0);
			subnetObj.setAssign(0);
			subnetObj.setTotalAddrNum(hostNumber);
			subnetObj.setAssignTime(new DateTimeUtil().getDate());
			subnetObj.setGrandGrp(subnetObj.getSubnetGrp());
			subnetObj.setIgroupMask(subnetObj.getInetMask());
			subnetObj.setSubnetGrp(subnet);
			subnetObj.setInetMask(childInetMask);
			subnetObj.setNetMask(IpTool.getNetMask(childInetMask));
			subnetObj.setComment("");

			// 组装子网参数
			for (int i = 0; i < netNumber; i++) {
				if (i == 0) {
					subnetObj.setSubnet(IpTool
							.getLowAddr(subnet, childInetMask));
					subnetObj.setLowAddr(subnetObj.getSubnet());
					subnetObj.setHighAddr(IpTool.getHighAddr(subnetObj
							.getLowAddr(), subnetObj.getInetMask()));
					subnetObj.setFip(IpTool.getFillIP(subnetObj.getLowAddr()));
					subnetObj.setFlowaddress(subnetObj.getFip());
					subnetObj.setFhighaddress(IpTool.getFillIP(subnetObj
							.getHighAddr()));
				} else {
					subnetObj.setSubnet(IpTool.getNextSubnet(subnetObj
							.getSubnet(), subnetObj.getTotalAddrNum()));
					subnetObj.setLowAddr(subnetObj.getSubnet());
					subnetObj.setHighAddr(IpTool.getHighAddr(subnetObj
							.getLowAddr(), subnetObj.getInetMask()));
					subnetObj.setFip(IpTool.getFillIP(subnetObj.getLowAddr()));
					subnetObj.setFlowaddress(subnetObj.getFip());
					subnetObj.setFhighaddress(IpTool.getFillIP(subnetObj
							.getHighAddr()));
				}

				sqlList.add(addSubnetSql(subnetObj));
			}
		}

		String[] sqlArray = new String[sqlList.size()];
		sqlList.toArray(sqlArray);
		// clear
		sqlList = null;

		int result = IPGlobal.DBOPERATION_FAIL;
		try {
			jt.batchUpdate(sqlArray);
			result = IPGlobal.SUNCCESS;
		} catch (Exception e) {
			log.error("partSubnet fail!");
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 获取某个网段的所有子信息列表，包括自身
	 * 
	 * @param subnet
	 * @param subnetGrp
	 * @param inetMask
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Subnet> getAllChildSubnet(String subnet, String subnetGrp,
			int inetMask) {
		List<Subnet> list = new ArrayList<Subnet>();
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String subnetDetailSQLMysql = "select subnetgrp, subnet, inetmask, grandgrp, igroupmask, approve, lowaddr, highaddr, city_id, netmask, " +
					" assign, assigntime, purpose, fhighaddress, fip, flowaddress, subnetcomment, childcount, totaladdr, mailstatus  " +
					" from gw_subnets where subnet=? and subnetgrp=? and inetmask=?";
			pSQL.setSQL(subnetDetailSQLMysql);
		}
		else {
			pSQL.setSQL(subnetDetailSQL);
		}
		pSQL.setString(1, subnet);
		pSQL.setString(2, subnetGrp);
		pSQL.setInt(3, inetMask);
		log.debug("subnetDetailSQL:" + pSQL.getSQL());
		Map<String, String> subnetMap = queryForMap(pSQL.getSQL());
		Subnet root = new Subnet(subnetMap);
		log.debug("wp_childCount:" + root.getChildCount());
		if (root.getChildCount() > 0) {
			String fhighAddr = root.getFhighaddress();
			String flowAddr = root.getFlowaddress();

			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				String selectChildrenSQLMysql = "select subnetgrp, subnet, inetmask, grandgrp, igroupmask, approve, lowaddr, highaddr, city_id, netmask, " +
						" assign, assigntime, purpose, fhighaddress, fip, flowaddress, subnetcomment, childcount, totaladdr, mailstatus  " +
						" from gw_subnets where inetmask>? and fip<=? and fip>=?";
				pSQL.setSQL(selectChildrenSQLMysql);
			}
			else {
				// 查询子网信息
				pSQL.setSQL(selectChildrenSQL);
			}
			pSQL.setInt(1, inetMask);
			pSQL.setString(2, fhighAddr);
			pSQL.setString(3, flowAddr);
			log.debug("selectChildrenSQL:" + pSQL.getSQL());
			try {
				list = jt.query(pSQL.getSQL(), new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws NumberFormatException, SQLException {
						Subnet subnetObj = new Subnet();
						subnetObj.setSubnet(rs.getString("subnet"));
						subnetObj.setSubnetGrp(rs.getString("subnetgrp"));
						subnetObj.setInetMask(rs.getInt("inetmask"));
						if (null != rs.getString("grandgroup")) {
							subnetObj.setGrandGrp(rs.getString("grandgroup"));
						}
						if (null != rs.getString("igroupmask")) {
							subnetObj.setIgroupMask(Integer.parseInt(rs
									.getString("igroupmask")));
						}
						if (null != rs.getString("childcount")) {
							subnetObj.setChildCount(Integer.parseInt(rs
									.getString("childcount")));
						}
						if (null != rs.getString("assign")) {
							subnetObj.setAssign(Integer.parseInt(rs
									.getString("assign")));
						}
						subnetObj.setFip(rs.getString("fip"));

						return subnetObj;

					};

				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 根节点放在list的第一个位置
		list.add(0, root);

		log.debug("list_size:" + list.size());

		return list;
	}

	/**
	 * 查询数据库中某行记录
	 * 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings( { "unused", "unchecked", "unchecked" })
	private Map<String, String> queryForMap(String sql) {
		Map<String, String> resultMap = (Map) jt.queryForObject(sql,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws NumberFormatException, SQLException {
						Map<String, String> map = new HashMap();
						ResultSetMetaData metadata = rs.getMetaData();
						String key = "";
						String value = "";
						for (int i = 1; i <= metadata.getColumnCount(); i++) {
							key = metadata.getColumnName(i);
							value = rs.getString(key);
							map.put(key.toLowerCase(), value);
						}
						return map;
					}
				});
		return resultMap;

	}

	/**
	 * 模糊查询用户下的已分配的子网信息 add by benyp
	 * 
	 * @param ip
	 *            没有输入ip
	 * @param cityid
	 *            所属地市
	 * @param country:
	 *            所属县 used1:用途1 used2:用途2 used3:用途3
	 *            city_id：省局用户返回所属地市，地市用户，返回用户所在的一级地市
	 * @return subnet(IP地址),netmask(子网掩码),inetmask(子网掩码位数),subnetgrp(结点所在的组),city_id(所属地市),country(县),purpose1(用途1),purpose2(用途2),purpose3(用途3)
	 */
@SuppressWarnings("unchecked")
	public List<Map> getAssignSubnetList(String ip,String cityid,String country,String used1,String used2,String used3,String city_id)
	{
		String para = "";		

		// 用户有输入IP，则进行模糊查询
		if (null != ip && !"".equals(ip))
		{
			para += " and a.subnet like'%" + ip + "%'";
		}
		// 用户有输入所属地市
		if(null!=cityid && !"".equals(cityid) && !"-1".equals(cityid)){
			para+= "and b.city_id ='"+cityid+"'";
		}
		// 用户有输入县
		if(null!=country && !"".equals(country) && !"-1".equals(country)){
			para+=" and b.country = '"+country+"'";
		}
		// 用户有输入用途1
		if(null!=used1 && !"".equals(used1) && !"-1".equals(used1)){
			para+=" and b.purpose1 = '"+used1+"'";
		}
		// 用户有输入用途2
		if(null!=used2 && !"".equals(used2) && !"-1".equals(used2)){
			para+=" and b.purpose2 = '"+used2+"'";
		}
		// 用户有输入用途3
		if(null!=used3 && !"".equals(used3) && !"-1".equals(used3)){
			para+=" and b.purpose3 = '"+used3+"'";
		}
		pSQL.setSQL(selectAssignIPSQL);
		pSQL.setString(1, city_id);
		pSQL.setStringExt(2, para, false);
		log.debug("selectAssignIPSQL:" + pSQL.getSQL());
		List<Map> subnetList = new ArrayList();
		//准备属地信息
		HashMap<String,String> cityMap = new HashMap();
		
		subnetList = jt.query(pSQL.getSQL(), new RowMapper()
				{
					public Object mapRow(ResultSet rs, int arg1)
									throws NumberFormatException, SQLException
					{
						Map map = new HashMap();
						ResultSetMetaData metadata = rs.getMetaData();
						String key = "";
						String value = "";
						for (int i = 1; i <= metadata.getColumnCount(); i++) {
							key = metadata.getColumnName(i);
							value = rs.getString(key);
							map.put(key.toLowerCase(), value);
						}
						return map;
					}
				});
		
		//有记录的情况下
		if(subnetList.size()>0)
		{
			String sql="select city_id,city_name from tab_city";
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			List<Map> cityList = jt.queryForList(sql);
			for(int i=0;i<cityList.size();i++)
			{
				cityMap.put((String)cityList.get(i).get("city_id"),(String)cityList.get(i).get("city_name"));
			}
			
			Map<String,String> subnetMap = new HashMap();
			for(int i=0;i<subnetList.size();i++)
			{
				subnetMap = subnetList.get(i);
				subnetMap.put("city_name",cityMap.get(subnetMap.get("city_id")));
				if(null!=subnetMap.get("country"))
				{
					subnetMap.put("country",cityMap.get(subnetMap.get("country")));
				}
				//add showpara:用于显示IP的详细信息:userstat/subnet/subnetgrp/inetmask/assign/leaf edit by benyp
				String showpara="";
				boolean flg=false;
				if(Integer.parseInt(subnetMap.get("childcount"))>0)flg=true;//含有子节点
				showpara="0/"+subnetMap.get("subnet")+"/"+subnetMap.get("subnetgrp")+"/"+subnetMap.get("inetmask")+"/"+subnetMap.get("assign")+"/"+flg;
				subnetMap.put("showpara", showpara);
				//add onlyip:用于存放ip地址、子网掩码位数、父节点edit by benyp
				String onlyip="";
				onlyip+=subnetMap.get("subnet")+"/"+subnetMap.get("inetmask")+"/"+subnetMap.get("subnetgrp");
				subnetMap.put("onlyip",onlyip);			
			}
		}		
		
		return subnetList;
	}
	/**
	 * 模糊查询用户下的未分配的子网信息 add by benyp
	 * 
	 * @param ip
	 *            没有输入ip
	 * @param city_id
	 *            省局用户：对应自身的市id
	 * @return subnet(IP地址),netmask(子网掩码),inetmask(子网掩码位数),childcount(子网个数),assigntime(分配时间),
	 *         subnetcomment(备注),subnetgrp(父节点),highaddr(最大可用地址),lowaddr(最小可用地址),purpose(用途),totaladdr(地址总数)
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getUnDistributeSubnetList(String ip, String city_id) {
		String para = "";
		
		// 用户有输入IP，则进行模糊查询
		if (null != ip && !"".equals(ip)) {
			para += " and subnet like'%" + ip + "%'";
		}
		pSQL.setSQL(selectUnAssignIPSQL);
		pSQL.setString(1, city_id);
		pSQL.setStringExt(2, para, false);
		log.debug("getSubnetList_SQL:" + pSQL.getSQL());
		List<Map> subnetList = new ArrayList();
		subnetList = jt.query(pSQL.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1)
					throws NumberFormatException, SQLException {
				Map subnetMap = new HashMap();
				subnetMap.put("subnetgrp", rs.getString("subnetgrp"));
				subnetMap.put("subnet", rs.getString("subnet"));
				subnetMap.put("inetmask", rs.getInt("inetmask"));
				subnetMap.put("netmask", rs.getString("netmask"));
				subnetMap.put("assigntime", new Date(
						rs.getLong("assigntime") * 1000));
				subnetMap.put("highaddr", rs.getString("highaddr"));
				subnetMap.put("lowaddr", rs.getString("lowaddr"));
				subnetMap.put("childcount", rs.getInt("childcount"));
				if (null == rs.getString("subnetcomment")
						|| "".equals(rs.getString("subnetcomment"))) {
					subnetMap.put("subnetcomment", "");
				} else {
					subnetMap.put("subnetcomment", rs
							.getString("subnetcomment"));
				}
				if (null == rs.getString("purpose")
						|| "".equals(rs.getString("purpose"))) {
					subnetMap.put("purpose", "");
				} else {
					subnetMap.put("purpose", rs.getString("purpose"));
				}
				// add totaladdr:地址总数 edit by benyp
				subnetMap.put("totaladdr", rs.getString("totaladdr"));
				// add
				// showpara:用于显示IP的详细信息:userstat/subnet/subnetgrp/inetmask/assign/leaf
				// edit by benyp
				String showpara = "";
				boolean flg = false;
				if (rs.getInt("childcount") > 0)
					flg = true;// 含有子节点
				showpara = "0/" + rs.getString("subnet") + "/"
						+ rs.getString("subnetgrp") + "/"
						+ rs.getString("inetmask") + "/"
						+ rs.getString("assign") + "/" + flg;
				subnetMap.put("showpara", showpara);
				// add onlyip:用于存放ip地址、子网掩码位数、父节点edit by benyp
				String onlyip = "";
				onlyip += rs.getString("subnet") + "/"
						+ rs.getString("inetmask") + "/"
						+ rs.getString("subnetgrp");
				subnetMap.put("onlyip", onlyip);
				return subnetMap;
			}
		});

		return subnetList;
	}

	/**
	 * 模糊查询用户下的子网信息
	 * 
	 * @param ip
	 *            没有输入ip
	 * @param childState
	 *            0：未划分，1：划分 2：全部
	 * @param userState
	 *            0:省局用户 1：地市用户
	 * @param ip_cityid
	 *            省局用户：就是自身cityid;地市用户：其所在得一级地市的city_id
	 * @return subnet(IP地址),netmask(子网掩码),inetmask(子网掩码位数),childcount(子网个数),assigntime(分配时间),
	 *         subnetcomment(备注),subnetgrp(父节点),highaddr(最大可用地址),lowaddr(最小可用地址),purpose(用途),totaladdr(地址总数)
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getSubnetList(String ip, String childState, int userState,
			String ip_cityid) {
		String para = "";
		String subnetGrp = "";

		// 用户有输入IP，则进行模糊查询
		if (null != ip && !"".equals(ip)) {
			para += " and subnet like'%" + ip + "%'";
		}

		// 选择了未划分状态
		if (null != childState && "0".equals(childState)) {
			para += " and childcount=0";
		}

		// 选择了划分状态
		if (null != childState && "1".equals(childState)) {
			para += " and childcount>0";
		}

		// 省局用户
		if (IPGlobal.SZX_USER == userState) {
			subnetGrp = "root";
		}
		// 地市用户
		else {
			subnetGrp = ip_cityid;
		}

		pSQL.setSQL(selectSubnetSQL);
		pSQL.setString(1, subnetGrp);
		pSQL.setStringExt(2, para, false);
		log.debug("getSubnetList_SQL:" + pSQL.getSQL());

		List<Map> subnetList = new ArrayList();
		subnetList = jt.query(pSQL.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1)
					throws NumberFormatException, SQLException {
				Map subnetMap = new HashMap();
				subnetMap.put("subnetgrp", rs.getString("subnetgrp"));
				subnetMap.put("subnet", rs.getString("subnet"));
				subnetMap.put("inetmask", rs.getInt("inetmask"));
				subnetMap.put("netmask", rs.getString("netmask"));
				subnetMap.put("assigntime", new Date(
						rs.getLong("assigntime") * 1000));
				subnetMap.put("highaddr", rs.getString("highaddr"));
				subnetMap.put("lowaddr", rs.getString("lowaddr"));
				subnetMap.put("childCount", rs.getInt("childCount"));
				if (null == rs.getString("subnetcomment")
						|| "".equals(rs.getString("subnetcomment"))) {
					subnetMap.put("subnetcomment", "");
				} else {
					subnetMap.put("subnetcomment", rs
							.getString("subnetcomment"));
				}
				if (null == rs.getString("purpose")
						|| "".equals(rs.getString("purpose"))) {
					subnetMap.put("purpose", "");
				} else {
					subnetMap.put("purpose", rs.getString("purpose"));
				}
				// add totaladdr:地址总数 edit by benyp
				subnetMap.put("totaladdr", rs.getString("totaladdr"));
				// add onlyip:用于存放ip地址、子网掩码位数、父节点edit by benyp
				String onlyip = "";
				onlyip += rs.getString("subnet") + "/"
						+ rs.getString("inetmask") + "/"
						+ rs.getString("subnetgrp");
				subnetMap.put("onlyip", onlyip);
				return subnetMap;
			}
		});

		return subnetList;
	}

	/**
	 * 用户新增网络地址（只能省局用户使用）
	 * 
	 * @param ip
	 *            用户输入的ip
	 * @param inetMask
	 *            子网掩码位数
	 * @param netMask
	 *            子网掩码
	 * @param assignTime
	 *            分配时间（yyyy-mm-dd）
	 * @param comment
	 *            备注
	 * @param ip_cityid
	 *            省局用户属地
	 * @return -1:参数错误 0：成功 -2:地址已存在 -3:数据库操作失败
	 */
	public int addRootSubnet(String ip, int inetMask, String netMask,
			String assignTime, String comment, String ip_cityid) {
		String subnet = IpTool.getLowAddr(ip, inetMask);
		if ("".equals(subnet)) {
			return IPGlobal.PARAM_ERROR;
		}

		String subnetGrp = "root";
		Subnet subnetObj = new Subnet(subnetGrp, inetMask, subnet);
		subnetObj.setAssignTime(assignTime);
		subnetObj.setCity_id(ip_cityid);
		subnetObj.setComment(comment);
		subnetObj.setGrandGrp("");
		subnetObj.setIgroupMask(0);

		// 系统如果存在这个地址的局部地址，或系统中有网段包含了这个地址，都不能完成增加
		if (isConfigSubnet(subnetObj)) {
			return IPGlobal.SUBNET_EXIST;
		}

		return addSubnet(subnetObj);

	}

	/**
	 * 删除网段及其子网
	 * 
	 * @param subnet
	 *            网段地址
	 * @param inetMask
	 *            子网掩码位数
	 * @param subnetGrp
	 *            父节点
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int deleteSubnet(String subnet, int inetMask, String subnetGrp) {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String subnetDetailSQLMysql = "select subnetgrp, subnet, inetmask, grandgrp, igroupmask, approve, lowaddr, highaddr, city_id, netmask, " +
					" assign, assigntime, purpose, fhighaddress, fip, flowaddress, subnetcomment, childcount, totaladdr, mailstatus  " +
					" from gw_subnets where subnet=? and subnetgrp=? and inetmask=?";
			pSQL.setSQL(subnetDetailSQLMysql);
		}
		else {
			pSQL.setSQL(subnetDetailSQL);
		}
		pSQL.setString(1, subnet);
		pSQL.setString(2, subnetGrp);
		pSQL.setInt(3, inetMask);
		log.debug("subnetDetailSQL:" + pSQL.getSQL());
		Map<String, String> subnetMap = queryForMap(pSQL.getSQL());
		if (null == subnetMap || 0 == subnetMap.size()) {
			return IPGlobal.PARAM_ERROR;
		}

		String fhighAddr = subnetMap.get("fhighaddress");
		String flowAddr = subnetMap.get("flowaddress");
		ArrayList<String> sqlList = new ArrayList<String>(5);

//		// 删除专线用户表
//		pSQL.setSQL(deleteApplyipSQL);
//		pSQL.setInt(1, inetMask);
//		pSQL.setString(2, fhighAddr);
//		pSQL.setString(3, flowAddr);
//		log.debug("deleteApplyipSQL:" + pSQL.getSQL());
//		sqlList.add(pSQL.getSQL());
//
//		// 删除用户表
//		pSQL.setSQL(deleteUserIPSQL);
//		pSQL.setInt(1, inetMask);
//		pSQL.setString(2, fhighAddr);
//		pSQL.setString(3, flowAddr);
//		log.debug("deleteUserIPSQL:" + pSQL.getSQL());
//		sqlList.add(pSQL.getSQL());

		// 删除总表
		pSQL.setSQL(deleteIPMainSQL);
		pSQL.setInt(1, inetMask);
		pSQL.setInt(2, inetMask);
		pSQL.setString(3, fhighAddr);
		pSQL.setString(4, flowAddr);
		log.debug("deleteUserIPSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

//		// 把网段和其子网中的注册成功或等待回信的记录，拷贝进delsub
//		pSQL.setSQL(addDelSubSQL);
//		pSQL.setInt(1, inetMask);
//		pSQL.setInt(2, inetMask);
//		pSQL.setString(3, fhighAddr);
//		pSQL.setString(4, flowAddr);
//		log.debug("addDelSubSQL:" + pSQL.getSQL());
//		sqlList.add(pSQL.getSQL());

		// 删除子网表中的自身及其子网的记录
		pSQL.setSQL(deleteSubnetSQL);
		pSQL.setInt(1, inetMask);
		pSQL.setString(2, fhighAddr);
		pSQL.setString(3, flowAddr);
		log.debug("deleteSubnetSQL:" + pSQL.getSQL());
		sqlList.add(pSQL.getSQL());

		String sqlArray[] = new String[sqlList.size()];
		sqlList.toArray(sqlArray);

		int[] resultCode = null;
		try {
			resultCode = jt.batchUpdate(sqlArray);
		} catch (Exception e) {
			log.error("deleteSubnet fail!");
			e.printStackTrace();
		}

		// clear
		sqlList = null;
		sqlArray = null;

		if (null == resultCode) {
			return IPGlobal.DBOPERATION_FAIL;
		} else {
			return IPGlobal.SUNCCESS;
		}
	}

	/**
	 * 判断有没有系统中有没有这个网段的子网或这个网段是不是被包含于系统中的某个网段
	 * 
	 * @param subnet
	 * @return 上面条件成立：true 不成立：false
	 */
	private boolean isConfigSubnet(Subnet subnet) {
		boolean result = false;
		String fip = subnet.getSubnet();
		String flowAddress = subnet.getFlowaddress();
		String fhighAddress = subnet.getFhighaddress();
		pSQL.setSQL(isConfigSQL);
		pSQL.setString(1, fip);
		pSQL.setString(2, fip);
		pSQL.setString(3, flowAddress);
		pSQL.setString(4, fhighAddress);
		log.debug("isConfigSubnet_SQL:" + pSQL.getSQL());
		int number = jt.queryForInt(pSQL.getSQL());
		if (number > 0) {
			result = true;
		} else {
			result = false;
		}

		return result;
	}

	/**
	 * 数据库操作，向subnet表插入网段
	 * 
	 * @param subnetObj
	 * @return
	 */
	private int addSubnet(Subnet subnetObj) {
		String sql = addSubnetSql(subnetObj);
		int resultCode = IPGlobal.DBOPERATION_FAIL;
		try {
			resultCode = jt.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultCode > 0 ? IPGlobal.SUNCCESS : IPGlobal.DBOPERATION_FAIL;
	}

	/**
	 * 获取增加网段的sql
	 * 
	 * @param subnetObj
	 * @return
	 */
	private String addSubnetSql(Subnet subnetObj) {
		pSQL.setSQL(addSubnetSQL);
		pSQL.setString(1, subnetObj.getSubnet());
		pSQL.setInt(2, subnetObj.getInetMask());
		pSQL.setString(3, subnetObj.getSubnetGrp());
		pSQL.setString(4, subnetObj.getGrandGrp());
		pSQL.setInt(5, subnetObj.getIgroupMask());
		pSQL.setString(6, subnetObj.getNetMask());
		pSQL.setInt(7, subnetObj.getTotalAddrNum());
		pSQL.setInt(8, subnetObj.getChildCount());
		pSQL.setInt(9, subnetObj.getAssign());
		pSQL.setInt(10, subnetObj.getMailStatus());
		pSQL.setString(11, subnetObj.getCity_id());
		if ("".equals(subnetObj.getComment()) || null == subnetObj.getComment()) {
			pSQL.setStringExt(12, null, false);
		} else {
			pSQL.setString(12, subnetObj.getComment());
		}
		pSQL.setInt(13, subnetObj.getApprove());
		if ("".equals(subnetObj.getPurpose()) || null == subnetObj.getPurpose()) {
			pSQL.setStringExt(14, null, false);
		} else {
			pSQL.setString(14, subnetObj.getPurpose());
		}
		if ("".equals(subnetObj.getAssignTime())
				|| null == subnetObj.getAssignTime()) {
			subnetObj.setAssignTime(new DateTimeUtil().getDate());
		}
		pSQL.setLong(15, new DateTimeUtil(subnetObj.getAssignTime())
				.getLongTime());
		pSQL.setString(16, subnetObj.getFip());
		pSQL.setString(17, subnetObj.getFhighaddress());
		pSQL.setString(18, subnetObj.getFlowaddress());
		pSQL.setString(19, subnetObj.getHighAddr());
		pSQL.setString(20, subnetObj.getLowAddr());
		log.debug("addSubnet_SQL:" + pSQL.getSQL());
		return pSQL.getSQL();
	}

	/**
	 * 递归构造子网sql，适用于级联划分
	 * 
	 * @param lastInetMask
	 *            页面上选择的级联划分的子网掩码位数
	 * @param parentSubnet
	 *            父网段
	 * @param sqlList
	 *            存放sql的list
	 */
	private void getSerialPartSubnetSqlList(int lastInetMask,
			Subnet parentSubnet, ArrayList<String> sqlList) {
		// 没有划分到最底层的情况
		if (parentSubnet.getInetMask() + 1 < lastInetMask) {
			int netNumber = 2;
			// 第一个子网
			Subnet subnet1 = new Subnet(parentSubnet.getSubnet(), parentSubnet
					.getInetMask() + 1, parentSubnet.getSubnet());
			subnet1.setApprove(IPGlobal.NOT_CHECK);
			subnet1.setAssignTime(parentSubnet.getAssignTime());
			subnet1.setChildCount(netNumber);
			subnet1.setCity_id(parentSubnet.getCity_id());
			subnet1.setPurpose(parentSubnet.getPurpose());
			subnet1.setComment("");
			subnet1.setMailStatus(parentSubnet.getMailStatus());
			subnet1.setAssign(IPGlobal.NOT_ASSIGN);
			subnet1.setGrandGrp(parentSubnet.getSubnetGrp());
			subnet1.setIgroupMask(parentSubnet.getInetMask());
			sqlList.add(addSubnetSql(subnet1));

			// 继续划分
			getSerialPartSubnetSqlList(lastInetMask, subnet1, sqlList);

			// 准备第二个子网
			subnet1.setSubnet(IpTool.getNextSubnet(subnet1.getSubnet(), subnet1
					.getTotalAddrNum()));
			subnet1.setLowAddr(subnet1.getSubnet());
			subnet1.setHighAddr(IpTool.getHighAddr(subnet1.getLowAddr(),
					subnet1.getInetMask()));
			log.debug("lowAddr:" + subnet1.getLowAddr());
			subnet1.setFip(IpTool.getFillIP(subnet1.getLowAddr()));
			subnet1.setFlowaddress(subnet1.getFip());
			log.debug("highAddr:" + subnet1.getHighAddr());
			subnet1.setFhighaddress(IpTool.getFillIP(subnet1.getHighAddr()));
			sqlList.add(addSubnetSql(subnet1));
			getSerialPartSubnetSqlList(lastInetMask, subnet1, sqlList);

		}
		// 增加的网段,子网个数为0
		else if (parentSubnet.getInetMask() + 1 == lastInetMask) {
			int netNumber = 0;
			// 第一个子网
			Subnet subnet1 = new Subnet(parentSubnet.getSubnet(), parentSubnet
					.getInetMask() + 1, parentSubnet.getSubnet());
			subnet1.setApprove(IPGlobal.NOT_CHECK);
			subnet1.setAssignTime(parentSubnet.getAssignTime());
			subnet1.setChildCount(netNumber);
			subnet1.setCity_id(parentSubnet.getCity_id());
			subnet1.setPurpose(parentSubnet.getPurpose());
			subnet1.setComment("");
			subnet1.setMailStatus(parentSubnet.getMailStatus());
			subnet1.setAssign(IPGlobal.NOT_ASSIGN);
			subnet1.setGrandGrp(parentSubnet.getSubnetGrp());
			subnet1.setIgroupMask(parentSubnet.getInetMask());
			sqlList.add(addSubnetSql(subnet1));

			// 准备第二个子网
			subnet1.setSubnet(IpTool.getNextSubnet(subnet1.getSubnet(), subnet1
					.getTotalAddrNum()));
			subnet1.setLowAddr(subnet1.getSubnet());
			subnet1.setHighAddr(IpTool.getHighAddr(subnet1.getLowAddr(),
					subnet1.getInetMask()));
			subnet1.setFip(IpTool.getFillIP(subnet1.getLowAddr()));
			subnet1.setFlowaddress(subnet1.getFip());
			subnet1.setFhighaddress(IpTool.getFillIP(subnet1.getHighAddr()));
			sqlList.add(addSubnetSql(subnet1));
			getSerialPartSubnetSqlList(lastInetMask, subnet1, sqlList);
		}
	}

	public void setDao(DataSource dao) {
		this.jt = new JdbcTemplate(dao);
	}

	public void setIpManagerDao(IPManagerDAO ipManagerDao) {
		this.ipManagerDao = ipManagerDao;
	}

	public void setPSQL(PrepareSQL psql) {
		pSQL = psql;
	}

}
