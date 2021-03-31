package dao.bbms;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;

import dao.util.JdbcTemplateExtend;

/**
 * 客户资料导入
 * 
 * @author 陈仲民（5243）
 * @version 1.0
 * @since 2008-6-3
 * @category 资源管理
 */
@SuppressWarnings("unchecked")
public class ImportCustomerDAO
{
	// jdbc
	private JdbcTemplateExtend jt;
	//客户类型
	private Map typeMap = new HashMap();
	//企业规模
	private Map sizeMap = new HashMap();
	//客户状态
	private Map statusMap = new HashMap();
	//属地
	private Map cityMap = new HashMap();
	//局向
	private Map officeMap = new HashMap();
	//小区
	private Map zoneMap = new HashMap();
	
	public ImportCustomerDAO(){
		typeMap.put("企业单位", "1");
		typeMap.put("事业单位", "2");
		
		sizeMap.put("小", "0");
		sizeMap.put("中", "1");
		sizeMap.put("大", "2");
		
		statusMap.put("开通", "1");
		statusMap.put("暂停", "2");
		statusMap.put("销户", "3");
	}
	/**
	 * 初始化数据连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplateExtend(dao);
	}
	/**
	 * 解析文件并入库
	 * @param file
	 * @return 成功标志  1：成功  0：失败
	 */
	public int importData(File file){
		//解析文件
		List data = dao.util.ExcelUtil.importToExcel(file);
		Iterator it = data.iterator();
		Map map = null;
		//初始化
		initData();
		String[] sqlList = new String[data.size()];
		int i = 0;
		//生成sql
		while (it.hasNext()){
			map = (Map)it.next();
			sqlList[i] = getSQL(map);
			i++;
		}
		//执行
		int[] ret = jt.batchUpdate(sqlList);
		if (ret != null && ret.length == data.size() && ret.length > 0){
			return 1;
		}
		else{
			return 0;
		}
	}
	/**
	 * 生成sql
	 * @param map
	 * @return
	 * modify by zhaixf 增加email字段
	 */
	private String getSQL(Map map){
		String sql = "";
		//获取数据
		String customer_name = (String)map.get("客户名称");
		String customer_id = (String)map.get("客户ID");
		String customer_pwd = (String)map.get("客户密码");
		String customer_type = (String)map.get("客户类型");
		String customer_size = (String)map.get("企业规模");
		String customer_address = (String)map.get("企业地址");
		String linkman = (String)map.get("联系人姓名");
		String linkphone = (String)map.get("联系人电话");
		String customer_state = (String)map.get("客户状态");
		String city_id = (String)map.get("属地");
		String office_id = (String)map.get("局向");
		String zone_id = (String)map.get("小区");
		String email = (String)map.get("E-mail");
		//没有客户ID则返回空
		if (customer_id == null || "".equals(customer_id)){
			return "";
		}
		customer_type = (String)typeMap.get(customer_type);
		if (customer_type == null || "".equals(customer_type)){
			customer_type = "-1";
		}
		customer_size = (String)sizeMap.get(customer_size);
		if (customer_size == null || "".equals(customer_size)){
			customer_size = "-1";
		}
		customer_state = (String)statusMap.get(customer_state);
		if (customer_state == null || "".equals(customer_state)){
			customer_state = "-1";
		}
		city_id = (String)cityMap.get(city_id);
		if (city_id == null || "".equals(city_id)){
			city_id = "-1";
		}
		office_id = (String)officeMap.get(office_id);
		if (office_id == null || "".equals(office_id)){
			office_id = "-1";
		}
		zone_id = (String)zoneMap.get(zone_id);
		if (zone_id == null || "".equals(zone_id)){
			zone_id = "-1";
		}
		//当前时间
		DateTimeUtil dt = new DateTimeUtil();
		long time = dt.getLongTime();
		//sql
		sql = "insert into tab_customerinfo (customer_id,city_id,office_id,zone_id," 
			+ "customer_name,customer_pwd,customer_type,customer_size,customer_address," 
			+ "linkman,linkphone,customer_state,update_time,email) " 
			+ "values ('" + customer_id + "','" + city_id + "','" + office_id + "','" 
			+ zone_id + "','" + customer_name + "','" + customer_pwd + "','" + customer_type + "','" 
			+ customer_size + "','" + customer_address + "','" + linkman + "','" + linkphone + "'," 
			+ customer_state + "," + time + ",'" + email + "')";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return sql;
	}
	/**
	 * 初始化信息
	 */
	private void initData(){
		String sql = "";
		List list = null;
		Iterator it = null;
		Map map = null;
		//属地
		sql = "select city_id,city_name from tab_city";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		list = jt.queryForList(sql);
		it = list.iterator();
		while (it.hasNext()){
			map = (Map)it.next();
			cityMap.put(map.get("city_name"), map.get("city_id"));
		}
		//局向
		sql = "select office_name,office_id from tab_office";
		psql = new PrepareSQL(sql);
		psql.getSQL();
		list = jt.queryForList(sql);
		it = list.iterator();
		while (it.hasNext()){
			map = (Map)it.next();
			officeMap.put(map.get("office_name"), map.get("office_id"));
		}
		//小区
		sql = "select zone_name,zone_id from tab_zone";
		psql = new PrepareSQL(sql);
		psql.getSQL();
		list = jt.queryForList(sql);
		it = list.iterator();
		while (it.hasNext()){
			map = (Map)it.next();
			zoneMap.put(map.get("zone_name"), map.get("zone_id"));
		}
	}
}
