package dao.hgwip;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import bio.hgwip.Subnet;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * vbras、bras管理的数据库操作系统类
 * @author fanjm (Ailk No.35572)
 * @version 1.0
 * @since 2017年2月20日
 *
 */
public class BrasVbrasManageDAO {
	Logger log = LoggerFactory.getLogger(BrasVbrasManageDAO.class);
	
	// 数据库操作类
	private JdbcTemplate jt;
		
	// sql封装类
	private PrepareSQL pSQL;
		
	//判断新增的网络地址在系统中是不是被包含或有子网
	private static final String isContainBrasIp = "select count(*) num from tab_bras where (flowaddress<=? and fhighaddress>=?) or (fip>=? and fip<=?)";

	//查询同属地、vbras类型的网段已经存在的数量
	private static final String qryCountByVbrasCity = "select count(*) num from tab_bras where city_id=? and type=?";
		
	//增加子网
	private static final String addBras = "insert into tab_bras(city_id,name,ip,highaddr,lowaddr,netmask,"
			+ "inetmask,type,fip,flowaddress,fhighaddress,assigntime) values(?,?,?,?,?,?,?,?,?,?,?,?)";
	
	//查询ip所在地址范围的vbras/bras记录
	private static final String qryBrasByIp = "select city_id,name,type,ip,netmask from tab_bras where fhighaddress>=? and flowaddress <=? order by city_id,name,fip desc";

	//查询全部的vbras/bras记录
	private static final String qryAllBras = "select city_id,name,type,ip,netmask from tab_bras order by city_id,name,fip desc";
		
	//查询待删除的记录
	private static final String qryBrasNeedDel = "select count(*) num from tab_bras where ip=? and netmask=?";
	
	//依据IP和子网掩码删除vbras/bras
	private static final String delBrasByIpNetMask = "delete from tab_bras where ip=? and netmask=?";
	
	
	/**
	 * 判断该网段与tab_bras已有网段是否存在冲突
	 * 
	 * @param subnet 网络地址对象
	 * @return 冲突：true 不冲突：false
	 */
	public boolean isConfigSubnet(Subnet subnet) {
		log.debug("isConfigSubnet()==>方法开始{}",new Object[]{subnet});
		boolean result = false;
		String fip = subnet.getFip();
		String flowAddress = subnet.getFlowaddress();
		String fhighAddress = subnet.getFhighaddress();
		pSQL.setSQL(isContainBrasIp);
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
		log.debug("isConfigSubnet()==>方法结束{}",new Object[]{result});
		return result;
	}
	
	
	/**
	 * 查询同属地、vbras类型的网段已经存在的数量
	 * @param brasType vbras/bras类型
	 * @param city 属地
	 * @return 数量
	 */
	public int qryCountByVbrasCity(String brasType, String city){
		log.debug("qryCountByVbrasCity()==>方法开始{}",new Object[]{brasType, city});
		
		pSQL.setSQL(qryCountByVbrasCity);
		pSQL.setString(1, city);
		pSQL.setString(2, brasType);
		log.debug("qryCountByVbrasCity:" + pSQL.getSQL());
		int number = jt.queryForInt(pSQL.getSQL());
		
		log.debug("qryCountByVbrasCity()==>方法结束{}",number);
		return number;
	}
	
	
	/**
	 * 数据库操作，向tab_bras表插入
	 * @param subnetObj 网络地址对象
	 * @param cityName bras/vbras归属地名
	 * @param brasType bras/vbras
	 * @return 结果（0成功  -3数据库操作失败）
	 */
	public int addBras(Subnet subnetObj,String cityName, String brasType) {
		log.debug("addBras()==>方法开始{}",new Object[]{subnetObj, cityName, brasType});
		pSQL.setSQL(addBras);
		pSQL.setString(1, subnetObj.getCity_id());
		pSQL.setString(2, cityName);
		pSQL.setString(3, subnetObj.getSubnet());
		pSQL.setString(4, subnetObj.getHighAddr());
		pSQL.setString(5, subnetObj.getLowAddr());
		pSQL.setString(6, subnetObj.getNetMask());
		pSQL.setInt(7, subnetObj.getInetMask());
		pSQL.setString(8, brasType);
		pSQL.setString(9, subnetObj.getFip());
		pSQL.setString(10, subnetObj.getFlowaddress());
		pSQL.setString(11, subnetObj.getFhighaddress());
		pSQL.setString(12, subnetObj.getAssignTime());
		String sql = pSQL.getSQL();
		
		int resultCode = IPGlobal.DBOPERATION_FAIL;
		try {
			resultCode = jt.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		log.debug("addBras()==>方法结束{}",resultCode > 0 ? IPGlobal.SUNCCESS : IPGlobal.DBOPERATION_FAIL);
		return resultCode > 0 ? IPGlobal.SUNCCESS : IPGlobal.DBOPERATION_FAIL;
	}
	
	/**
	 * 根据IP查询vbras、bras信息
	 * @param ip 查询条件IP
	 * @return	vbras、bras信息结果集
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getBrasList(String fip){
		log.debug("getBrasList()==>方法开始{}",new Object[]{fip});
		if(!StringUtil.IsEmpty(fip)){
			pSQL.setSQL(qryBrasByIp);
			pSQL.setString(1, fip);
			pSQL.setString(2, fip);
		}
		else{
			pSQL.setSQL(qryAllBras);
		}
		
		log.debug("getSubnetList_SQL:" + pSQL.getSQL());

		List<Map<String,String>> brasList = new ArrayList<Map<String,String>>();
		brasList = jt.query(pSQL.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1)
					throws NumberFormatException, SQLException {
				Map<String,String> subnetMap = new HashMap<String,String>();
				String cityId = rs.getString("city_id");
				subnetMap.put("cityId", cityId);
				String cityName = CityDAO.getNextCityMapByCityPid("00").get(cityId);
				subnetMap.put("cityName", StringUtil.IsEmpty(cityName)?"":cityName);
				subnetMap.put("name", rs.getString("name"));
				subnetMap.put("type", rs.getString("type"));
				subnetMap.put("ip", rs.getString("ip"));
				subnetMap.put("netmask", rs.getString("netmask"));
				
				return subnetMap;
			}
		});
		
		log.debug("getBrasList()==>方法结束{}",new Object[]{brasList});
		return brasList;
	}
	
	
	/**
	 * 删除tab_bras符合条件的Bras/Vbras网段
	 * 
	 * @param ip 网段地址
	 * @param netMask 子网掩码
	 * @return 数据库删除结果(-1 参数错误 -3 数据库操作失败 0成功)
	 */
	public int delVbras(String ip, String netMask) {
		log.debug("deleteSubnet()==>方法开始{}",new Object[]{ip, netMask});
		pSQL.setSQL(qryBrasNeedDel);
		pSQL.setString(1, ip);
		pSQL.setString(2, netMask);
		log.debug("qryBrasNeedDel:" + pSQL.getSQL());
		int number = jt.queryForInt(pSQL.getSQL());
		if (0 == number) {
			return IPGlobal.PARAM_ERROR;
		}

		// 删除
		pSQL.setSQL(delBrasByIpNetMask);
		pSQL.setString(1, ip);
		pSQL.setString(2, netMask);
		pSQL.setString(3, netMask);
		log.debug("delBrasByIpNetMask:" + pSQL.getSQL());
		int num = jt.update(pSQL.getSQL());

		if (0 == num) {
			return IPGlobal.DBOPERATION_FAIL;
		} else {
			return IPGlobal.SUNCCESS;
		}
		
	}
	
	
	
	/**
	 * 查询某属地的设备Bras/Vbras统计数量
	 * 
	 * @param city 属地
	 * @return 查询得到的结果集(List<Map<String,String>> example：<"cityId","791">,<"type","BRAS">,<"num","30">)
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> brasState(String city) {
		log.debug("brasState()==>方法开始{}",new Object[]{city});
		StringBuffer sql = new StringBuffer("select city_id, type, sum(nvl(actDevNum, 0)) num from tab_bras");

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = new StringBuffer("select city_id, type, sum(IFNULL(actDevNum, 0)) num from tab_bras");
		}

		if(!(StringUtil.IsEmpty(city)||"00".equals(city))){
			sql.append(" where city_id=?");
		}
		sql.append(" group by city_id, type order by city_id");
		pSQL.setSQL(sql.toString());
		if(!(StringUtil.IsEmpty(city)||"00".equals(city))){
			pSQL.setString(1, city);
		}
		
		List<Map<String,String>> brasStateList = new ArrayList<Map<String,String>>();
		brasStateList = jt.query(pSQL.getSQL(), new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1)
					throws NumberFormatException, SQLException {
				Map<String,String> mapInfo = new HashMap<String,String>();
				String cityId = rs.getString("city_id");
				mapInfo.put("cityId", cityId);
				mapInfo.put("type", rs.getString("type"));
				mapInfo.put("num", rs.getString("num"));
				
				return mapInfo;
			}
		});
		
		log.debug("brasState()==>方法结束{}",new Object[]{brasStateList});
		return brasStateList;
	}
	
	
	
	public void setPSQL(PrepareSQL psql) {
		pSQL = psql;
	}
	
	public void setDao(DataSource dao) {
		this.jt = new JdbcTemplate(dao);
	}

	
}
