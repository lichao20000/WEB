package dao.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.corba.WebCorbaInst;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.resource.DeviceAct;
import com.linkage.litms.system.AreaManager;
import com.linkage.litms.system.User;
import com.linkage.litms.system.dbimpl.AreaManageSyb;
import com.linkage.litms.webtopo.Scheduler;

@SuppressWarnings("unchecked")
public class ImportTopoDAO
{
	private static Logger log = LoggerFactory.getLogger(ImportTopoDAO.class);
	
	// 属地对应关系
	private Map<String, String> cityMap = new HashMap<String, String>();
	// 设备类型对应关系
	private Map<String, String> typeMap = new HashMap<String, String>();
	// jdbc
	private JdbcTemplate jt;
	/**
	 * 初始化数据连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
	/**
	 * 解析topo图的导入文件，入库并通知后台
	 * 
	 * @param file
	 * @param user
	 * @param pid
	 * @param x
	 * @param y
	 * @return 0：成功 -1：通知后台失败 -2：入库失败 -3：获取设备id失败
	 */
	public int importTopo(File file, User user, String pid, String x, String y)
	{
		// 取登陆用户的域及默认采集点
		String gather_id = getGather(user.getAreaId());
		if (pid == null || "".equals(pid))
			{
				pid = "1/0";
			}
		if (x == null || "".equals(x))
			{
				x = "1000";
			}
		if (y == null || "".equals(y))
			{
				y = "1000";
			}
		List<String> list = readFile(file);
		Iterator it = list.iterator();
		String line = "";
		int device_id = 0;
		// 取设备id
		WebCorbaInst corba = new WebCorbaInst("db");
		String m_DeviceSerial = corba.getDeviceSerial(user, list.size());
		if (m_DeviceSerial == null || "".equals(m_DeviceSerial)
				|| m_DeviceSerial.toLowerCase().equals("null"))
			{
				return -3;
			}
		else
			{
				device_id = Integer.parseInt(m_DeviceSerial);
			}
		// 初始化map
		getCityMap();
		getTypeMap();
		// sql数组
		String[] sqlArr = new String[list.size()];
		// 设备数组
		String[] devArr = new String[list.size()];
		int i = 0;
		// 生成sql
		String tmp = "";
		while (it.hasNext())
			{
				line = (String) it.next();
				String[] data = line.split(",");
				tmp = getSql(data, String.valueOf(device_id), gather_id);
				// 记录设备数组
				if (!"".equals(tmp)){
					sqlArr[i] = tmp;
					devArr[i] = String.valueOf(device_id);
					device_id++;
					i++;
				}
			}
		// 执行sql
		int[] ret = jt.batchUpdate(sqlArr);
		if (ret != null && ret.length > 0 && ret[0] > 0)
			{
				// 增加域权限
				for (int j = 0; j < devArr.length; j++)
					{
						addAreaRes(devArr[j], user.getAreaId());
					}
				// 通知后台MC
				Scheduler scheduler = new Scheduler();
				int topo = 0;
				try
					{
						topo = scheduler.importTopo(String.valueOf(user.getAreaId()),
								devArr, pid, Integer.parseInt(x), Integer.parseInt(y));
					}
				catch (Exception e)
					{
						e.printStackTrace();
						topo = 0;
					}
				if (topo == devArr.length)
					{
						return 0;
					}
				else
					{
						return -1;
					}
			}
		else
			{
				return -2;
			}
	}
	/**
	 * 解析文件，输出数据列表
	 * 
	 * @param file
	 * @return
	 */
	private List<String> readFile(File file)
	{
		// 初始化
		FileReader in = null;
		BufferedReader bf = null;
		String s = "";
		List<String> list = new ArrayList<String>();
		// 读文件
		try
			{
				in = new FileReader(file);
				bf = new BufferedReader(in);
				// 第一行不取
				s = bf.readLine();
				while ((s = bf.readLine()) != null)
					{
						list.add(s);
					}
			}
		catch (Exception e)
			{
				e.printStackTrace();
			}finally{
				// 关闭流
				if (in != null)
					{
						try
							{
								in.close();
								in = null;
							}
						catch (Exception e1)
							{
								e1.printStackTrace();
							}
					}
				// 关闭流
				if (bf != null)
					{
						try
							{
								bf.close();
								bf = null;
							}
						catch (Exception e2)
							{
								e2.printStackTrace();
							}
					}
			}
		return list;
	}
	/**
	 * 根据文件数据生成入库sql
	 * 
	 * @param data
	 * @param device_id
	 * @return
	 */
	private String getSql(String[] data, String device_id, String gather_id)
	{
		String sql = "";
		String oui = "";
		String serialnumber = "";
		String device_model = "";
		String softwareversion = "";
		String city_name = "";
		String devicetype_id = "";
		String city_id = "";
		String device_model_id = "";
		//oui、序列号为空则直接返回空
		if (data[0] == null || "".equals(data[0])){
			return "";
		}
		if (data[1] == null || "".equals(data[1])){
			return "";
		}
		// 当前时间
		DateTimeUtil dt = new DateTimeUtil();
		if (data != null && data.length >= 5)
			{
				oui = data[0];
				serialnumber = data[1];
				device_model = data[2];
				softwareversion = data[3];
				city_name = data[4];
				//属地
				city_id = cityMap.get(city_name);
				//设备类型、软件版本
				devicetype_id = DeviceAct.getDeviceTypeID(oui, "", device_model, "", "", softwareversion);
				//设备型号
				device_model_id = DeviceAct.getDeviceModelID(oui, device_model);
				sql = "insert into tab_gw_device (device_id,oui,device_serialnumber,devicetype_id,city_id,device_status,gather_id,port,cpe_currentupdatetime,device_name,gw_type,device_model_id) values ('"
						+ device_id
						+ "','"
						+ oui
						+ "','"
						+ serialnumber
						+ "',"
						+ devicetype_id
						+ ",'"
						+ city_id
						+ "',0,'"
						+ gather_id
						+ "',0,"
						+ dt.getLongTime()
						+ ",'"
						+ oui
						+ "-"
						+ serialnumber
						+ "',2,'"
						+ device_model_id + "') ";
				sql += "insert into sgw_security (device_id,snmp_version,is_enable,security_username,security_model,engine_id,context_name,security_level,auth_protocol,auth_passwd,privacy_protocol,privacy_passwd,snmp_r_passwd,snmp_w_passwd)"
						+ " select '"
						+ device_id
						+ "',snmp_version,is_enable,security_username,security_model,engine_id,context_name,security_level,auth_protocol,auth_passwd,privacy_protocol,privacy_passwd,snmp_r_passwd,snmp_w_passwd from sgw_model_security_template where device_model_id='"
						+ device_model_id + "'";
			}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		return sql;
	}
	/**
	 * 初始化地市对应关系列表
	 */
	private void getCityMap()
	{
		PrepareSQL psql = new PrepareSQL("select city_id,city_name from tab_city");
    	psql.getSQL();
		List list = jt.queryForList("select city_id,city_name from tab_city");
		Iterator it = list.iterator();
		Map map = null;
		while (it.hasNext())
			{
				map = (Map) it.next();
				cityMap.put((String) map.get("city_name"), (String) map.get("city_id"));
			}
	}
	/**
	 * 初始化设备类型对应关系
	 */
	private void getTypeMap()
	{
//		List list = jt.queryForList("select * from tab_devicetype_info");
		PrepareSQL psql = new PrepareSQL("select oui,device_model,softwareversion,devicetype_id" +
				" from tab_vendor_oui a,tab_devicetype_info b,gw_device_model c" +
				" where a.vendor_id=b.vendor_id and b.device_model_id=c.device_model_id");
    	psql.getSQL();
		List list = jt.queryForList("select oui,device_model,softwareversion,devicetype_id" +
				" from tab_vendor_oui a,tab_devicetype_info b,gw_device_model c" +
				" where a.vendor_id=b.vendor_id and b.device_model_id=c.device_model_id");
		Iterator it = list.iterator();
		Map map = null;
		String key = "";
		while (it.hasNext())
			{
				map = (Map) it.next();
				key = map.get("oui") + "_" + map.get("device_model") + "_"
						+ map.get("softwareversion");
				typeMap.put(key, map.get("devicetype_id").toString());
			}
	}
	/**
	 * 将当前area_id及所有父节点都录入表tab_gw_res_area
	 * 
	 */
	private void addAreaRes(String device_id, long area_id)
	{
		// 获取到用户自身所属区域
		ArrayList m_AreaList = getUpperToTopAreaIds((Integer.parseInt(String
				.valueOf(area_id))));
		m_AreaList.add(String.valueOf(area_id));
		// 删除表tab_gw_res_area中已有的设备数据
		String sqlDelete = "delete from tab_gw_res_area where res_id = '" + area_id
				+ "' and res_type = 1";
		PrepareSQL psql = new PrepareSQL(sqlDelete);
    	psql.getSQL();
		jt.update(sqlDelete);
		// 循环处理域信息，生成sql
		String[] sqlList = new String[m_AreaList.size()];
		Iterator it = m_AreaList.iterator();
		int i = 0;
		while (it.hasNext())
			{
				sqlList[i] = "insert into tab_gw_res_area(res_type,res_id,area_id) values(1,'"
						+ device_id + "'," + it.next() + ") ";
				psql = new PrepareSQL(sqlList[i]);
		    	psql.getSQL();
				i++;
			}
		jt.batchUpdate(sqlList);
	}
	/**
	 * 获取所有父域id
	 * 
	 * @param m_AreaId
	 * @return
	 */
	private ArrayList getUpperToTopAreaIds(int m_AreaId)
	{
		// 需要为此用户区域注入此设备权限，也需要给此用户所属区域上层属地注入此设备权限
		AreaManager Manager = new AreaManageSyb();
		ArrayList list = new ArrayList();
		list.clear();
		// 如果域id为1，则表示为admin.com，已经为最上层的域
		// 如果域id为0，则无父域。
		if (m_AreaId == 1 || m_AreaId == 0)
			return list;
		// 最大循环8次
		int number = 0;
		while (number < 8)
			{
				m_AreaId = Manager.getUpperAreaIds(m_AreaId);
				// 如果为0：则该area_id的area_pid为0，无上层域
				// 如果为-1：则该area_id无上层域
				// tab_area中area_pid默认为0
				if (m_AreaId == 0 || m_AreaId == -1)
					break;
				else
					list.add(String.valueOf(m_AreaId));
				number++;
			}
		log.debug("WEB>>>Parent AreaID :" + list);
		return list;
	}
	/**
	 * 根据域获取默认采集点
	 * 
	 * @param area_id
	 * @return
	 */
	private String getGather(long area_id)
	{
		PrepareSQL psql = new PrepareSQL("select gather_id from tab_process_desc where area_id="
				+ area_id);
    	psql.getSQL();
		List list = jt
				.queryForList("select gather_id from tab_process_desc where area_id="
						+ area_id);
		if (list != null && list.size() > 0)
			{
				Map map = (Map) list.get(0);
				return (String) map.get("gather_id");
			}
		else
			{
				return "";
			}
	}
}
