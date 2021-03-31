package dao.hgwip;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;






/**
 * ip管理的数据库操作系统类，
 * 提供获取用户状态、省中心city_id,一级地市、用途等方法
 * @author wangp
 *
 */
public class ItvIPManagerDAO extends SuperDAO
{
	Logger log = LoggerFactory.getLogger(ItvIPManagerDAO.class);
	
	/**
	 * 获取地市下的县信息
	 * @param city_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getCityList(String city_id)
	{
		log.debug("getCityList:"+city_id);
		PrepareSQL psql = new PrepareSQL("select city_id,city_name from tab_city where parent_id=? or city_id = ?");
		psql.setString(1, city_id);
		psql.setString(2, city_id);
		return jt.queryForList(psql.getSQL());	
	}
	
	/**
	 * 入库信息
	 * @param city_id,acc_oid,startIp,endIp
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int insertInto(long acc_oid,String cityId,String startIp,String endIp)
	{
		log.debug("insert into itvip:{},{},{},{}"+ new Object[]{acc_oid,startIp,endIp,cityId});
		PrepareSQL psql = new PrepareSQL("insert into itvip(id,city_id,start_ip,end_ip,acc_oid,updatetime) values(?,?,?,?,?,?) ");
		psql.setLong(1, DataSetBean.getMaxId("itvip", "id"));
		psql.setString(2, cityId);
		psql.setString(3, startIp);
		psql.setString(4, endIp);
		psql.setLong(5, acc_oid);
		psql.setLong(6, System.currentTimeMillis()/1000);
		return DBOperation.executeUpdate(psql.getSQL());	
	}
	
	/**
	 * 更新信息
	 * @param city_id,acc_oid,startIp,endIp
	 * @return
	 */
	public int update(int id,String startIp,String endIp)
	{
		log.debug("insert into itvip:{},{},{}"+ new Object[]{id,startIp,endIp});
		PrepareSQL psql = new PrepareSQL("update itvip set start_ip = ?,end_ip = ?,updatetime = ? where id = ? ");
		psql.setString(1, startIp);
		psql.setString(2, endIp);
		psql.setLong(3, System.currentTimeMillis()/1000);
		psql.setLong(4, id);
		return DBOperation.executeUpdate(psql.getSQL());	
	}
	
	/**
	 * 删除ip地址分配
	 * @param city_id
	 * @return
	 */
	public int delete(int ipId,long acc_oid)
	{
		log.debug("delete id:"+ipId + ",acc_oid:" + acc_oid);
		PrepareSQL psql = new PrepareSQL("delete from itvip where id = ? and acc_oid = ? ");
		psql.setInt(1, ipId);
		psql.setLong(2, acc_oid);
		return DBOperation.executeUpdate(psql.getSQL());	
	}
	/**
	 * 获取用户IP分配
	 * @param startIp(起始IP地址)、endIp(终止IP地址)、acc_oid(用户id)、city_id(入网地点)、starttime(开始时间)、endtime(结束时间)
	 * @return List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getIpList(int curPage_splitPage, int num_splitPage,
			String starttime, String endtime, long acc_oid,String ip){
		String para = "";
		
		//输入IP地址
//		if(startIp!=null && !"".equals(startIp.trim())){
//			para+=" and a.start_ip like'%"+startIp+"%'";
//		}
//		if(startIp!=null && !"".equals(startIp.trim())){
//			para+=" and a.end_ip like'%"+endIp+"%'";
//		}
		
		//开始时间
		if(starttime!=null && !"".equals(starttime.trim())){
			DateTimeUtil sdt=new DateTimeUtil(starttime + " 00:00:00");
			long st=sdt.getLongTime();
			para+=" and a.updatetime >="+st;
		}
		//结束时间
		if(endtime!=null && !"".equals(endtime.trim())){
			DateTimeUtil edt=new DateTimeUtil(endtime + " 23:59:59");
			long et=edt.getLongTime();
			para+=" and a.updatetime <="+et;
		}
		
		String sql="select a.id,a.city_id,a.start_ip,a.end_ip,a.updatetime,b.acc_loginname from itvip a,tab_accounts b where a.acc_oid=b.acc_oid and a.acc_oid = " + acc_oid
		          + para
		          +" order by a.updatetime";
		//List userInfoList=jt.queryForList(sql);
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		List<Map> infoList=querySP(sql.toString(), (curPage_splitPage - 1) * num_splitPage + 1,
				num_splitPage, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1)
					throws SQLException {
				HashMap map=new HashMap();
				String cityId = rs.getString("city_id");
				String city_name = CityDAO.getCityName(cityId);
				DateTimeUtil dt = new DateTimeUtil(rs.getLong("updatetime") * 1000);
				map.put("ip_id", rs.getInt("id"));
				map.put("city_id", cityId);
				map.put("city_name", city_name);
				map.put("start_ip", rs.getString("start_ip"));
				map.put("end_ip", rs.getString("end_ip"));
				map.put("updatetime", dt.getLongDate());
				map.put("acc_loginname", rs.getString("acc_loginname"));
				return map;
			}
			
		});
		
		return infoList;
	}
	
	/**
	 * 获取用户IP分配最大页
	 * @param startIp(起始IP地址)、endIp(终止IP地址)、acc_oid(用户id)、city_id(入网地点)、starttime(开始时间)、endtime(结束时间)
	 * @return List
	 * 
	 */
	public int getIpListMax(int curPage_splitPage, int num_splitPage,
			String starttime, String endtime, long acc_oid,String ip){
		String para = "";
		
		//输入IP地址
//		if(startIp!=null && !"".equals(startIp.trim())){
//			para+=" and a.start_ip like'%"+startIp+"%'";
//		}
//		if(startIp!=null && !"".equals(startIp.trim())){
//			para+=" and a.end_ip like'%"+endIp+"%'";
//		}
		
		//开始时间
		if(starttime!=null && !"".equals(starttime.trim())){
			DateTimeUtil sdt=new DateTimeUtil(starttime + " 00:00:00");
			long st=sdt.getLongTime();
			para+=" and a.updatetime >="+st;
		}
		//结束时间
		if(endtime!=null && !"".equals(endtime.trim())){
			DateTimeUtil edt=new DateTimeUtil(endtime + " 23:59:59");
			long et=edt.getLongTime();
			para+=" and a.updatetime <="+et;
		}
		
		String sql="select count(1) num from itvip a where  a.acc_oid = " + acc_oid
		          + para
		          +" ";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql="select count(*) num from itvip a where  a.acc_oid = " + acc_oid
					+ para
					+" ";
		}
		//List userInfoList=jt.queryForList(sql);
		PrepareSQL psql = new PrepareSQL(sql);
		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	
	
//	public void setDao(DataSource dao)
//	{
//		this.jt = new JdbcTemplate(dao);
//	}
//	
	
	/**
	 * 查询数据库中某行记录
	 * @param sql
	 * @return
	 */
	@SuppressWarnings({"unused","unchecked"})
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
