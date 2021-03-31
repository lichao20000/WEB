package dao.resource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import com.linkage.litms.LipossGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.ZooCuratorLockUtil;
import com.linkage.module.gwms.Global;

/**
 * @author Jason(3412)
 * @date 2008-11-10
 */
public class DeviceModelWifiDao {

	private static Logger log = LoggerFactory.getLogger(DeviceModelWifiDao.class);
	
	private JdbcTemplate jt;

	public List getAllModelWifiList() {
//		String strSQL = "select device_model_id modelId, vendor_add vendorName, vendor_id ouiName, device_model modelName"
//				+ " from gw_device_model m, tab_vendor v where m.oui=v.vendor_id order by vendor_name,vendor_id,device_model";
		String strSQL = "select m.device_model_id modelid, v.vendor_id ouiname ,min(d.wifi_ability) wifiability ,min(wifi_ability_desc) wifiabilitydesc ,v.vendor_add vendorname, m.device_model modelname "
			+ " from gw_device_model m, tab_vendor v,tab_devicetype_info b,tab_device_version_attribute  d  "
			+ " where m.vendor_id=v.vendor_id and m.device_model_id = b.device_model_id " 
			+ " and v.vendor_id = b.vendor_id  and  b.devicetype_id = d.devicetype_id  group by  v.vendor_id,m.device_model_id ";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		List tmpList = jt.queryForList(strSQL);
		return tmpList;
	}
	
	public List getAllModelList4jlEther() {
		String strSQL = "select e.ethernum,e.etherrate,m.device_model_id modelid, v.vendor_add vendorname, v.vendor_id ouiname, m.device_model modelname"
			+ " from gw_device_model m inner join tab_vendor v on m.vendor_id=v.vendor_id left join ether_num_rate e on e.device_model_id=m.device_model_id order by convert(int,m.device_model_id) desc";

		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select e.ethernum,e.etherrate,m.device_model_id modelid, v.vendor_add vendorname, v.vendor_id ouiname, m.device_model modelname"
					+ " from gw_device_model m inner join tab_vendor v on m.vendor_id=v.vendor_id left join ether_num_rate e on e.device_model_id=m.device_model_id order by cast(m.device_model_id as unsigned int) desc";
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		List tmpList = jt.queryForList(strSQL);
		return tmpList;
	}

	
	/**设备型号修改**/
	public void updateDeviceModel(String vendor_id, String modelName, String modelId,int wifi_ability) {
		String wifi_ability_desc = "";
		switch (wifi_ability)
		{
			case 0:
				wifi_ability_desc = "";
				break;
			case 1:
				wifi_ability_desc = "802.11b";
				break;
			case 2:
				wifi_ability_desc = "802.11b/g";
				break;
			case 3:
				wifi_ability_desc = "802.11b/g/n";
				break;
			default:
				wifi_ability_desc = "802.11b/g/n/ac";
				break;
		}
		String strUpdate = "update tab_device_version_attribute set wifi_ability = " + wifi_ability +" ,wifi_ability_desc = '" +wifi_ability_desc+"' "
				+ " where devicetype_id in "
				+ " (select devicetype_id from tab_devicetype_info where vendor_id = '"+vendor_id+"' and device_model_id = '"+modelId+"' )";
		PrepareSQL psql = new PrepareSQL(strUpdate);
		psql.getSQL();
		jt.update(strUpdate);
	}
	
	public void updateEther(String ethernum, String etherrate, String modelId) {
		String strUpdate = "update ether_num_rate set ethernum='" + ethernum
				+ "',etherrate='" + etherrate + "'"
				+ " where device_model_id='" + modelId + "'";
		PrepareSQL psql = new PrepareSQL(strUpdate);
		psql.getSQL();
		jt.update(strUpdate);
	}
	
	public void initEther(){
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			String sqlSelect = "select device_model_id from gw_device_model where device_model_id not in(select device_model_id from ether_num_rate)";

			PrepareSQL psqlTwo = new PrepareSQL(sqlSelect);
			List<Map> deviceModelIdList = jt.queryForList(psqlTwo.getSQL());
			if (deviceModelIdList == null || deviceModelIdList.isEmpty()) {
				log.warn("dao.resource.DeviceModelWifiDao.initEther() deviceModelIdList is empty!");
			}
			else {
				String[] sql = new String[deviceModelIdList.size()];
				int index = 0;
				for (Map deviceModelIdMap : deviceModelIdList) {
					String device_model_id = com.linkage.module.gwms.util.StringUtil.getStringValue(deviceModelIdMap, "device_model_id");
					String sqlInsert = "insert into ether_num_rate (device_model_id) " + "values ('" + device_model_id + "')";
					sql[index] = sqlInsert;
					index++;
				}
				jt.batchUpdate(sql);
			}
		}
		else {
			String strinsert = "insert into ether_num_rate(device_model_id) (select device_model_id from gw_device_model where device_model_id not in(select device_model_id from ether_num_rate))";
			jt.update(strinsert);
		}

		String strdel = "delete from ether_num_rate where device_model_id in (select device_model_id from ether_num_rate where device_model_id not in(select device_model_id from gw_device_model))";
		jt.update(strdel);
	}

	public void delDeviceModel(String modelId) {
		String strDel = "delete from gw_device_model where device_model_id='"
				+ modelId + "'";
		PrepareSQL psql = new PrepareSQL(strDel);
		psql.getSQL();
		jt.update(strDel);
	}
	
	public void delDeviceModelType(String modelId) {
		String strDel = "delete from gw_dev_model_dev_type where device_model_id='"
				+ modelId + "'";
		PrepareSQL psql = new PrepareSQL(strDel);
		psql.getSQL();
		jt.update(strDel);
	}

	public String addDeviceModel(String modelName, String oui) {
		
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			return ZooCuratorLockUtil.getInstance().getTgwModelId(oui, modelName, "1");
		}else {
			String callable = "getTgwModelIdProc '" + oui + "','" + modelName + "'";
			PrepareSQL psql = new PrepareSQL(callable);
			psql.getSQL();
			Map mapTmp = DataSetBean.getRecord(callable);
			if(mapTmp == null || mapTmp.isEmpty())
				return "";
			return mapTmp.get("device_model_id").toString();
		}
	}
	
	public String addDeviceModelForOracle(String modelName, String oui) {
		String result = "";
		int id = 0;
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
		long add_time = new Date().getTime() / 1000;;
		//查询是否存当前记录
		String sqlexists = " select 1 from gw_device_model where vendor_id = '"+oui +"' and device_model = '"+modelName+"'";
		
		PrepareSQL psql = new PrepareSQL(sqlexists);
		psql.getSQL();
		List list = jt.queryForList(sqlexists);
		//如果不存在则插入
		if(list !=null && list.size()==0)
		{
			String idSql = "select nvl(max(TO_NUMBER(device_model_id)),0) as id from gw_device_model";
			// teledb
			if (DBUtil.GetDB() == Global.DB_MYSQL) {
				idSql = "select ifnull(max(TO_NUMBER(device_model_id)),0) as id from gw_device_model";
			}
			PrepareSQL psql1 = new PrepareSQL(sqlexists);
			psql1.getSQL();
			Map listId = jt.queryForMap(idSql);
			log.info("----"+listId);
			id = Integer.parseInt(listId.get("id").toString())+1;
			
			String addsql = "insert into gw_device_model (device_model_id,vendor_id,device_model,add_time) values ('"+id+"','"+oui+"','"+modelName+"',"+add_time+")";
			PrepareSQL psql2 = new PrepareSQL(sqlexists);
			psql2.getSQL();
			int count = jt.update(addsql);
			log.info("--insert:"+count);
			if(count != 0)
			{
				result = id+"";
			}
		}
		return result;
	}

//	public int getRord(String deviceModelName, String oui){
//		String strSQL = "select count(1) total from gw_device_model where "
//				+ " device_model='" + deviceModelName + "' and oui='" + oui
//				+ "'";
//		return jt.queryForInt(strSQL);
//	}
	public int getRord(String deviceModelName, String vendor_id){
		String strSQL = "select count(1) total from gw_device_model where "
				+ " device_model='" + deviceModelName + "' and oui='" + vendor_id
				+ "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select count(*) total from gw_device_model where "
					+ " device_model='" + deviceModelName + "' and oui='" + vendor_id
					+ "'";
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return jt.queryForInt(strSQL);
	}
	
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}

	public Map getVendor(String vendorId)
	{
		String strSQL = "select top 1 * from tab_vendor where "
				+ " vendor_id='" + vendorId + "'";

		if(LipossGlobals.isOracle()){
			strSQL = "select * from tab_vendor where "
					+ " vendor_id='" + vendorId + "' and rownum < 2";
		}
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select vendor_id, vendor_add, vendor_name, telephone, remark, staff_id from tab_vendor where "
					+ " vendor_id='" + vendorId + "' limit 1";
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return jt.queryForMap(strSQL);
	}
}