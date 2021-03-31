package dao.hgwip;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;






/**
 * ip管理的数据库操作系统类，
 * 提供获取用户状态、省中心city_id,一级地市、用途等方法
 * @author wangp
 *
 */
public class IPManagerDAO
{
	Logger log = LoggerFactory.getLogger(IPManagerDAO.class);
    //数据库操作类
	private JdbcTemplate jt;	
	
	/**
	 * 获取专线用户
	 */
	@SuppressWarnings("unused")
	private static final String selectUserIpInfoSQL="select a.subnet,a.inetmask,a.subnetgrp,a.addrnum,a.usernamezw,a.netnamee,a.applydate,a.country,a.rwaddr,b.city_name from userip a,tab_city b where a.rwaddr=b.city_id ? order by a.subnet,a.inetmask,a.subnetgrp";
	/**
	 * 获取一级地市的信息
	 */
	private String selectFirstLevelCitySQL="select city_id,city_name from tab_city where (parent_id=? or city_id=?) order by city_id";
	
	/**
	 * 获取指定地市下的县
	 */
	private String selectCountryByCitySQL="select city_id,city_name from tab_city where parent_id=?";
	
	/**
	 * 展示系统中的各地市的ip分配权限
	 */
	private String selectAssignNumSQL ="select b.city_name,a.ip_num from city_ipnum a,tab_city b where a.city_id= b.city_id order by b.city_name";
	
	/**
	 * 展示系统中的指定地市可以分配的最大ip地址数目
	 */
	private String selectAssignNumByCitySQL="select * from city_ipnum where city_id=?";

	/**
	 * 获取类别，给数据字典维护使用
	 */
	private String selectTypesSQL="select type_name,types from gw_dic";
	
	/**
	 * 查询指定类别下的数据
	 */
	private String selectDataByTypeSQL = "select * from gw_datadic where type_name=?";
	
	/**
	 * 获取某个类别下的数据
	 * @param type 用途1（purpose1）、用途2（purpose2）、用途3（purpose3）、用户接入方式（cntmode）、用户接入速率（cntspeed）
	 * @return   map中的key:value
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getDataByType(String type)
	{
		log.debug("getDataByType:"+type);
		String[] params = new String[1];
		params[0]=type;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			selectDataByTypeSQL = "select type_name, value from gw_datadic where type_name=?";
		}
		PrepareSQL psql = new PrepareSQL(selectDataByTypeSQL);
		psql.getSQL();
		return jt.queryForList(selectDataByTypeSQL,params);
	}
	/**
	 * 获取类别
	 * @return key: type_name、types
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getTypes()
	{
		PrepareSQL psql = new PrepareSQL(selectTypesSQL);
		psql.getSQL();
		return jt.queryForList(selectTypesSQL);
	}
	
	/**
	 * 返回用户权限范围内的一级地市信息
	 * @param userCity_id  用户当前的city_id
	 * @return  如果地市用户，只会返回长度为1的list，如果省局用户会比较多点
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getFirstLevelCity(String userCity_id)
	{
		String szx_cityID=getSZXCityID();
		log.debug("szx_cityID:"+szx_cityID);	
		String[] params = null;
		//省中心用户
		if(userCity_id.equals(szx_cityID))
		{
			params = new String[2];
			params[0] = szx_cityID;
			params[1] = szx_cityID;
			return jt.queryForList(selectFirstLevelCitySQL,params);				
		}
		
		
		String sql ="select city_id,parent_id from tab_city";
		List cityList = null;		
		Map map = null;
		HashMap cityMap = new HashMap();
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		cityList = jt.queryForList(sql);			
		for(int i=0;i<cityList.size();i++)
		{
			map =(Map)cityList.get(i);
			cityMap.put((String)map.get("city_id"),(String)map.get("parent_id"));				
		}   		
		
		String city_id = userCity_id;
		String parent_id=userCity_id;
		//父节点是省中心的情况,退出循环
		while(!szx_cityID.equals((String)cityMap.get(city_id)))
		{
			parent_id = (String)cityMap.get(city_id);
			city_id=parent_id;    			
		} 
		
		//clear
		map = null;
		cityMap = null;
		cityList = null;
		
		params = new String[1];
		params[0] = city_id;
		sql ="select city_id,city_name from tab_city where city_id=?";
		psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql,params);
			
	}
	
	/**
	 * 获取专线用户IP分配
	 * @param status(0:省局用户1:地市用户),cityid(用户id),subnet(IP地址)、addrnum(地址个数)、netnamee(用户网络英文名)、city_id(入网地点)、country(县)、starttime(开始时间)、endtime(结束时间)、cntmode(接入方式)、cntspeed(接入速率)、cntaddr(互联地址)、localun(本地用户编号)、usernamezw(用户名称（中文）)
	 * @return List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getVipUsersIPAssign(int status,String cityid,String subnet,String addrnum,String netnamee,String city_id,String country,String starttime,String endtime,String cntmode,String cntspeed,String cntaddr,String localun,String usernamezw){
		String para = "";
		//省局用户：不过虑，地市用户：过滤为本身
		if(status==0){
			if(city_id!=null && !"".equals(city_id.trim()) && !"-1".equals(city_id.trim())){
				para+=" and a.rwaddr='"+city_id+"'";
			}
		}else{
			     para+=" and a.rwaddr='"+cityid+"'";
		}
		//输入IP地址
		if(subnet!=null && !"".equals(subnet.trim())){
			para+=" and a.subnet like'%"+subnet+"%'";
		}
		//地址个数
		if(addrnum!=null && !"".equals(addrnum.trim())){
			para+=" and a.addrnum ="+Integer.parseInt(addrnum);
		}
		//用户网络英文名
		if(netnamee!=null && !"".equals(netnamee.trim())){
			para+=" and a.netnamee like'%"+netnamee+"%'";
		}
		//县
		if(country!=null && !"".equals(country.trim()) && !"-1".equals(country.trim())){
			para+=" and a.country ='"+country+"'";
		}
		//开始时间
		if(starttime!=null && !"".equals(starttime.trim())){
			DateTimeUtil sdt=new DateTimeUtil(starttime);
			long st=sdt.getLongTime();
			para+=" and a.applydate >="+st;
		}
		//结束时间
		if(endtime!=null && !"".equals(endtime.trim())){
			DateTimeUtil edt=new DateTimeUtil(endtime);
			long et=edt.getLongTime();
			para+=" and a.applydate <="+et;
		}
		//接入方式
		if(cntmode!=null && !"".equals(cntmode.trim()) && !"-1".equals(cntmode.trim())){
			para+=" and a.cntmode ='"+cntmode+"'";
		}
		//接入速率
		if(cntspeed!=null && !"".equals(cntspeed.trim()) && !"-1".equals(cntspeed.trim())){
			para+=" and a.cntspeed ='"+cntspeed+"'";
		}
		//互联地址
		if(cntaddr!=null && !"".equals(cntaddr.trim())){
			para+=" and a.cntaddr like'%"+cntaddr+"%'";
		}
		//本地用户编号
		if(localun!=null && !"".equals(localun.trim())){
			para+=" and a.localun like'%"+localun+"%'";
		}
		//用户名称（中文）
		if(usernamezw!=null && !"".equals(usernamezw.trim())){
			para+=" and a.usernamezw like'%"+usernamezw+"%'";
		}
		String sql="select a.subnet,a.inetmask,a.subnetgrp,a.addrnum,a.usernamezw,a.netnamee,a.applydate,a.country,a.rwaddr,b.city_name from userip a,tab_city b where a.rwaddr=b.city_id "
		          + para
		          +" order by a.subnet,a.inetmask,a.subnetgrp";
		//List userInfoList=jt.queryForList(sql);
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List<Map> userInfoList=jt.query(sql, new RowMapper(){			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				HashMap map=new HashMap();
				map.put("subnet", rs.getString("subnet"));
				map.put("inetmask", rs.getInt("inetmask"));
				map.put("subnetgrp", rs.getString("subnetgrp"));
				map.put("addrnum", rs.getInt("addrnum"));
				map.put("usernamezw", rs.getString("usernamezw"));
				map.put("netnamee", rs.getString("netnamee"));
				map.put("applydate", new Date(rs.getLong("applydate") * 1000));
				map.put("country", rs.getString("country"));
				map.put("city_name", rs.getString("city_name"));
				// TODO Auto-generated method stub
				return map;
			}
			
		});
		
		return userInfoList;
	}
	
	/**
	 * 获取地市下的县信息
	 * @param city_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getCountryByCity(String city_id)
	{
		String[] params = new String[1];
		params[0] =city_id;	
		log.warn("getCountryByCity:"+city_id);
		PrepareSQL psql = new PrepareSQL(selectCountryByCitySQL);
		psql.getSQL();
		return jt.queryForList(selectCountryByCitySQL,params);	
	}
	
	/**
	 * 返回系统中定义的各地市的最大分配地址数限制
	 * @return  map中city_name(属地)ip_num(最大地址数)
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getAssignNum()
	{
		PrepareSQL psql = new PrepareSQL(selectAssignNumSQL);
		psql.getSQL();
		return jt.queryForList(selectAssignNumSQL);
	}
	
	
	/**
	 * 返回指定城市可以分配的最大地址数目
	 * @param city_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getAssignNumByCity(String city_id)
	{
		int ipnum=IPGlobal.MAX_ASSIGNNUMBER;
		String[] params = new String[1];
		params[0] =city_id;

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			selectAssignNumByCitySQL = "select ip_num from city_ipnum where city_id=?";
		}

		PrepareSQL psql = new PrepareSQL(selectAssignNumByCitySQL);
		psql.getSQL();
		Map<String,String> map =queryForMap(selectAssignNumByCitySQL,params);
		if(null!=map)
		{
			ipnum = Integer.parseInt(map.get("ip_num"));
		}
		
		return ipnum;
	}
	
	/**
	 * 根据用户所在市ID，返回用户状态
	 * @param userCity_id
	 * @return 0:省局用户 1：地市用户
	 */
	public int getUserState(String userCity_id)
	{
		String szx_cityID = getSZXCityID();
		log.debug("szx_cityID:"+szx_cityID+"    userCity_id:"+userCity_id);
		int resultCode =IPGlobal.CITY_USER;
		
		if(userCity_id.equals(szx_cityID))
		{
			resultCode = IPGlobal.SZX_USER;
		}
		
		return resultCode;
	}

	/**
	 * 获取省中心市ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getSZXCityID()
	{
		String szx_cityID = "00";
		String sql = "select city_id from tab_city where parent_id not in(select city_id from tab_city) order by city_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List<Map> list=jt.queryForList(sql);
		Map cityMap = list.get(0);
		szx_cityID=(String)cityMap.get("city_id");
		return szx_cityID;
	}
	
	
	/**
	 * 省局用户，返回当前用户属地ID，地市用户，返回用户所在一级地市ID
	 * @param userCity_id  用户所对应的真正属地
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getIPCityID(String userCity_id)
	{
		String ipCityID="";
		String szx_cityID = getSZXCityID();
		if(userCity_id.equals(szx_cityID))
		{
			ipCityID = userCity_id;
		}
		else 
		{
			String sql ="select city_id,parent_id from tab_city";
			List cityList = null;		
			Map map = null;
			HashMap cityMap = new HashMap();
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			cityList = jt.queryForList(sql);			
			for(int i=0;i<cityList.size();i++)
			{
				map =(Map)cityList.get(i);
				cityMap.put((String)map.get("city_id"),(String)map.get("parent_id"));				
			}   		
    		
    		String city_id = userCity_id;
    		String parent_id=userCity_id;
    		//父节点是省中心的情况,退出循环
    		while(!szx_cityID.equals((String)cityMap.get(city_id)))
    		{
    			parent_id = (String)cityMap.get(city_id);
    			city_id=parent_id;    			
    		}
    		
    		ipCityID=city_id;
    		
    		//clear
    		map = null;
    		cityMap = null;
    		cityList = null;
		}
		
		return ipCityID;	
	}
	
	/**
	 * 根据子网掩码位数计算出子网掩码
	 * @param inetMask
	 * @return
	 */
	public String getNetMask(int inetMask)
	{
		return IpTool.getNetMask(inetMask);
	}
	
	
	/**
	 * 网段的子网可以划分的子网掩码位数（默认不能超过30）
	 * @param inetMak 要划分子网的网段掩码位数
	 * @return 子网掩码为inetMak的网段划分子网可以选择的掩码位数
	 * key:子网掩码位数，value:子网掩码位数
	 */
	public HashMap<String,String> getInetMaskMap(int inetMak)
	{
		return IpTool.getInetMaskMap(inetMak);
	}
	
	/**
	 * 返回子网掩码位数为inetMask网段，划分子网可以选择的子网掩码个数
	 * @param inetMask 要划分子网的网段掩码位数
	 * @return key：子网掩码位数，value：子网个数
	 */
	public HashMap<String,String> getInetMaskAndChildCount(int inetMask)
	{
		return IpTool.getInetMaskAndChildCount(inetMask);
	}
	
	public void setDao(DataSource dao)
	{
		this.jt = new JdbcTemplate(dao);
	}
	
	
	/**
	 * 查询数据库中某行记录
	 * @param sql
	 * @return
	 */
	@SuppressWarnings({"unused","unchecked", "unchecked"})
	private Map<String,String> queryForMap(String sql,String[] params)
	{		
		Map<String,String> resultMap = (Map)jt.queryForObject(sql,params,new RowMapper()
						{
							public Object mapRow(ResultSet rs, int arg1)
							throws NumberFormatException, SQLException
							{
								Map<String,String> map = new HashMap();
								ResultSetMetaData metadata = rs.getMetaData();
								String key="";
								String value="";								
								for (int i = 1; i <= metadata.getColumnCount(); i++)
								{
									key =metadata.getColumnName(i);
									value=rs.getString(key);									
									map.put(key.toLowerCase(),value);
								}
								return map;				
							}		
						});		
		return resultMap;
		
	}

}
