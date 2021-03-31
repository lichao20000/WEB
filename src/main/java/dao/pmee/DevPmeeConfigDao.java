package dao.pmee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.webtopo.common.PM_instance;
import com.linkage.litms.webtopo.common.PefDef_MasterTread;
import com.linkage.module.gwms.Global;

public class DevPmeeConfigDao {
	
	private static Logger log = LoggerFactory.getLogger(DevPmeeConfigDao.class);
	
	// jdbc模板
	private JdbcTemplate jt;
	/**
	 * 初始化数据库连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
	//******************    METHOD    ******************//
	public boolean DelExpressionID(String device_id,String expressionid){
		String[] sql=new String[2];
		sql[0]=" delete pm_map_instance where device_id='"+device_id+"' and expressionid="+expressionid;
		sql[1]="delete pm_map where device_id='"+device_id+"' and expressionid="+expressionid;

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql[0]=" delete from pm_map_instance where device_id='"+device_id+"' and expressionid="+expressionid;
			sql[1]="delete from pm_map where device_id='"+device_id+"' and expressionid="+expressionid;
		}

		PrepareSQL psql = new PrepareSQL(sql[0]);
		psql.getSQL();
		psql = new PrepareSQL(sql[1]);
		psql.getSQL();
		int[] num=jt.batchUpdate(sql);
		boolean flg=true;
		for(int i=0;i<num.length;i++){
			if(num[i]==-1) flg=false;
		}
		return flg;
	}
	/**
	 * 查看配置结果
	 * @param device_id:选中的设备ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getConfigResult(String device_id){
		String sql="select a.expressionid,a.interval,a.isok,a.remark,b.name,b.descr,b.class1,c.device_name,c.loopback_ip,c.device_serialnumber,c.device_id from pm_map a,pm_expression b,tab_gw_device c";
		sql+=" where a.expressionid=b.expressionid and c.device_id=a.device_id and a.device_id in ("+device_id+") order by a.expressionid";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	/**
	 *取得当前用户的属地及下属属地
	 *@param
	 *       city_id:当前用户属地
	 *@return
	 *       List 属地列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getCity(String city_id){
		String sql="select city_id,city_name from tab_city where parent_id='"+city_id+"' or city_id='"+city_id+"'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	/**
	 * 获取当前用户所管理设备的设备厂商列表
	 * @param area_id
	 * @param isadmin
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getVendor(){
		String sql="select vendor_id,vendor_name,vendor_add from tab_vendor";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	/**
	 * 根据厂商ID获取相应的设备型号
	 * @param vendor_id
	 * @param isadmin
	 * @param area_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getDevModel(String vendor_id){
//		String sql="select distinct device_model from tab_devicetype_info where oui='"+vendor_id+"'";
		String sql="select device_model from gw_device_model where vendor_id='"+vendor_id+"'";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	/**
	 * 根据厂商ID,设备型号获取设备版本
	 * @param vendor_id
	 * @param dev_model
	 * @param isadmin
	 * @param area_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getVersion(String vendor_id,String dev_model){
//		String sql="select devicetype_id,softwareversion from tab_devicetype_info where oui='"+vendor_id+"' and device_model='"+dev_model+"'";
		String sql="select devicetype_id,softwareversion " +
				" from tab_devicetype_info where vendor_id="
				+ vendor_id + " and device_model_id=" + dev_model;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	/**
	 * 根据厂商ID,设备型号版本获取设备
	 * @param isadmin
	 * @param area_id
	 * @param vendor_id
	 * @param devicetype_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getDevByVersion(boolean isadmin,long area_id,String vendor_id,String devicetype_id,String city_id){
		String sql="select oui,device_id,loopback_ip,gather_id,device_serialnumber from tab_gw_device where oui='"+vendor_id+"' and devicetype_id = "+devicetype_id;
		if(!isadmin){
			sql+=" and city_id='"+city_id+"'  and device_id in(select distinct res_id from tab_gw_res_area where res_type=1 and area_id="+area_id+")";
		}else{
			if(!"00".equals(city_id)){
				sql+=" and city_id='"+city_id+"'";
			}
		}
		sql+=" order by device_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	/**
	 * 根据用户查询设备
	 * @param isadmin
	 * @param area_id
	 * @param username
	 * @param phone
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getDevByUser(boolean isadmin,long area_id,String username,String phone){
		String sql="select a.oui,a.device_id,a.loopback_ip,a.gather_id,a.device_serialnumber from tab_gw_device a,tab_egwcustomer b";
		//ADMIN 用户
		if(!isadmin){
			sql+=",tab_gw_res_area c where a.device_id=c.res_id and c.res_type=1 and c.area_id="+area_id+" and a.device_id=b.device_id";
		}else{
			sql+=" where a.device_id = b.device_id";
		}
		//用户名不为空
		if(username!=null && !username.trim().equals("")){
			sql+=" and b.username = '"+username+"'";
		}
		//电话号码不为空
		if(phone!=null && !phone.trim().equals("")){
			sql+=" and b.phonenumber='"+phone+"'";
		}
		sql+=" order by a.device_id";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	/**
	 * 根据设备序列号或设备IP获取设备(模糊查询)
	 * @param isadmin
	 * @param area_id
	 * @param serial
	 * @param ip
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getDevByIP(boolean isadmin,long area_id,String serial,String ip){
		String sql="select a.oui,a.device_id,a.loopback_ip,a.gather_id,a.device_serialnumber from tab_gw_device a";
		if(isadmin){
			sql+=" where 1=1";
		}else{
			sql+=",tab_gw_res_area b where a.device_id=b.res_id and b.res_type=1 and b.area_id="+area_id;
		}
		//设备序列号
		if(serial!=null && !serial.trim().equals("")){
			if(serial.length()>5){
				sql += " and a.dev_sub_sn ='" + serial.substring(serial.length()-6, serial.length()) + "'";
			}
			sql+=" and a.device_serialnumber like '%"+serial+"'";
		}
		//IP
	    if(ip!=null && !ip.trim().equals("")){
	    	sql+=" and a.loopback_ip='"+ip+"'";
	    }
	    PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	/**
	 * 根据device_serialnumber获取用户信息
	 * @param device_serialnumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getUser(String device_serialnumber){
		String sql="select username from tab_egwcustomer where device_serialnumber='"+device_serialnumber+"'";
		List<Map<String,String>> list=jt.queryForList(sql);
		String tmp="";
		for(Map m:list){
			tmp+=","+m.get("username");
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return tmp.equals("")?"":tmp.substring(1);
	}
	/**
	 * 根据厂商获取性能表达式
	 * @param company
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getExpression(String company){
		String sql="select distinct expressionid,name from pm_expression where company in("+company+")";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	/**
	 * 查看设备性能表达式是否已经配置
	 * @param expressionid
	 * @param device_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> HasConfigExpression(String expressionid,String device_id){
		String sql="select a.device_id,b.device_serialnumber,b.loopback_ip from pm_map_instance a,tab_gw_device b where a.device_id=b.device_id and a.expressionid="+expressionid+" and a.device_id in("+device_id+")";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}
	/**
	 * 
	 * @param expressionid
	 * @param device_id
	 * @param interval
	 * @return
	 */
	public int actionPerformedOne(String expressionid,String device_id,String interval){
		String[] sqlStrs = new String[2];
		sqlStrs[0] ="delete from pm_map_instance where expressionid="+expressionid+" and device_id="+device_id;
		String mySQL = "select count(1) as num from pm_map where expressionid="+expressionid+" and device_id="+device_id;
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			mySQL = "select count(*) as num from pm_map where expressionid="+expressionid+" and device_id="+device_id;
		}
		int num=jt.queryForInt(mySQL);
		if (num > 0) {
			mySQL = "update pm_map set isok=-1, interval="+interval+",remark=null where expressionid="+expressionid+" and device_id="+device_id;
		} else {
			mySQL = "insert into pm_map(expressionid,device_id,interval,isok) values("+expressionid+" ,"+device_id+","+interval+",-1)";
		}
		sqlStrs[1] = mySQL;
		PrepareSQL psql = new PrepareSQL(sqlStrs[0]);
		psql.getSQL();
		psql = new PrepareSQL(sqlStrs[1]);
		psql.getSQL();
		int[] retflag =jt.batchUpdate(sqlStrs);
		return retflag==null?-1:0;
	}
	/**
	 * 
	 * @param accounts
	 * @param password
	 * @param srcType
	 * @param device_id
	 * @param pm_Name
	 * @param expressionid
	 * @param mindesc
	 * @param maxdesc
	 * @param dynadesc
	 * @param mutationdesc
	 * @param dynatype
	 * @param dynacount
	 * @param beforeday
	 * @param dynawarninglevel
	 * @param dynareinstatelevel
	 * @param maxtype
	 * @param maxcount
	 * @param maxwarninglevel
	 * @param maxreinstatelevel
	 * @param mintype
	 * @param mincount
	 * @param minwarninglevel
	 * @param minreinstatelevel
	 * @param mutationtype
	 * @param mutationcount
	 * @param mutationwarninglevel
	 * @param mutationreinstatelevel
	 * @param interval
	 * @param intodb
	 * @param maxthres
	 * @param minthres
	 * @param dynathres
	 * @param mutationthres
	 */
	@SuppressWarnings("unchecked")
	public void actionTwo(
			String accounts,String password,String srcType,
			String device_id,
			String pm_Name,String expressionid,
			String mindesc,String maxdesc,String dynadesc,
			String mutationdesc,int dynatype,
			int dynacount,int beforeday,int dynawarninglevel,int dynareinstatelevel,
			int maxtype,int maxcount,int maxwarninglevel,int maxreinstatelevel,
			int mintype,int mincount,int minwarninglevel,int minreinstatelevel,
			int mutationtype,int mutationcount,int mutationwarninglevel,int mutationreinstatelevel,
			int interval,int intodb,float maxthres,float minthres,float dynathres,float mutationthres
	){
		PM_instance pm = new PM_instance();
		pm.setName(pm_Name, expressionid);
		pm.setDesc(mindesc, maxdesc, dynadesc, mutationdesc);
		pm.setDyna(dynatype, dynacount, beforeday, dynawarninglevel,dynareinstatelevel);
		pm.setMax(maxtype, maxcount, maxwarninglevel, maxreinstatelevel);
		pm.setMin(mintype, mincount, minwarninglevel, minreinstatelevel);
		pm.setMutation(mutationtype, mutationcount, mutationwarninglevel,mutationreinstatelevel);
		pm.setPerf(interval, intodb);
		pm.setThres(maxthres, minthres, dynathres, mutationthres);
		ArrayList list = new ArrayList();
		list.add(device_id);
		pm.setDevList(list);
		
		log.debug("begin PefDef_MasterTread");
		PefDef_MasterTread mt = new PefDef_MasterTread(2);		
		mt.setPM(pm);		
		mt.setAccountInfo(accounts,password);		
		if(null!=srcType&&!"".equals(srcType))
		{			
			int type = Integer.parseInt(srcType);			
			if(2==type)
			{				
				mt.setType(type);				
			}
			
		}		
		mt.start();		
	}
	//******************     END      ******************//
}
