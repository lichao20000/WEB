package dao.resource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.DBAdapter;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.ZooCuratorLockUtil;
import com.linkage.module.gwms.Global;


/**
 * @author Jason(3412)
 * @date 2008-11-10
 */
public class DeviceModelDao {

	private static Logger log = LoggerFactory.getLogger(DeviceModelDao.class);
	
	private JdbcTemplate jt;

	private PrepareSQL pSQL = null;

	/**
	 * 增加设备厂商
	 */
	private String m_VendorAdd_SQL="insert into stb_tab_vendor(vendor_id,vendor_name,vendor_add,telephone,staff_id,remark) values(?,?,?,?,?,?)";

	/**
	 * 更新设备厂商
	 */
	private String m_VendorUpdate_SQL ="update stb_tab_vendor set vendor_name=?,vendor_add=?,telephone=?,staff_id=?,remark=? where vendor_id=?";

	/**
	 * 删除设备厂商
	 */
	private String m_VendorDelete_SQL ="delete from stb_tab_vendor where vendor_id =?";

	public List getAllModelList() {
//		String strSQL = "select device_model_id modelId, vendor_add vendorName, vendor_id ouiName, device_model modelName"
//				+ " from gw_device_model m, tab_vendor v where m.oui=v.vendor_id order by vendor_name,vendor_id,device_model";
		String strSQL = "select m.device_model_id modelid, v.vendor_add vendorname, v.vendor_id ouiname, m.device_model modelname"
			+ " from gw_device_model m, tab_vendor v where m.vendor_id=v.vendor_id order by v.vendor_name,m.vendor_id,m.device_model";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		List tmpList = jt.queryForList(strSQL);
		return tmpList;
	}

    /**
     * 获取型号列表
     * @param vendor_add
     * @param deviceModelName
     * @param oui
     * @return
     */
    public List getAllModelListSxlt(String vendor_add, String deviceModelName) {
    	String strSQL = "select m.device_model_id modelid, v.vendor_add vendorname, v.vendor_id ouiname, m.device_model modelname" +
                " from gw_device_model m, tab_vendor v where m.vendor_id=v.vendor_id ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select m.device_model_id modelid, v.vendor_add vendorname, v.vendor_id ouiname, m.device_model modelname" +
					" from gw_device_model m, tab_vendor v where m.vendor_id=v.vendor_id ";
		}
    	if(!StringUtil.IsEmpty(vendor_add)) {
    		strSQL += " and v.vendor_add like '%" + vendor_add.trim() + "%' ";
        }       
    	if(!StringUtil.IsEmpty(deviceModelName)) {
    		strSQL += " and m.device_model like '%" + deviceModelName.trim() + "%' ";
    	}       
		strSQL += "order by v.vendor_name,m.vendor_id,m.device_model";
		
        PrepareSQL psql = new PrepareSQL(strSQL);
        psql.getSQL();
        List tmpList = jt.queryForList(strSQL);
        return tmpList;
    }
    
    /**
     * 获取机顶盒型号列表
     * @param vendor_add
     * @param deviceModelName
     * @param oui
     * @return
     */
	public List getStbAllModelList(String vendor_add, String deviceModelName) {
		String strSQL = "select m.device_model_id modelid, v.vendor_add vendorname, v.vendor_id ouiname, m.device_model modelname" +
                " from stb_gw_device_model m, stb_tab_vendor v where m.vendor_id=v.vendor_id ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select m.device_model_id modelid, v.vendor_add vendorname, v.vendor_id ouiname, m.device_model modelname" +
					" from stb_gw_device_model m, stb_tab_vendor v where m.vendor_id=v.vendor_id ";
		}

		if(!StringUtil.IsEmpty(vendor_add)) {
    		strSQL += " and v.vendor_add like '%" + vendor_add.trim() + "%' ";
        }       
    	if(!StringUtil.IsEmpty(deviceModelName)) {
    		strSQL += " and m.device_model like '%" + deviceModelName.trim() + "%' ";
    	}       
    	
		strSQL += "order by v.vendor_name,m.vendor_id,m.device_model";
		
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		List tmpList = jt.queryForList(strSQL);
		return tmpList;
	}

	public List getStbVendorInfoList(String vendor_name, String vendor_add) {
		String strSQL = "select * FROM stb_tab_vendor where 1=1 ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select vendor_id, vendor_name, vendor_add, telephone, remark, staff_id FROM stb_tab_vendor where 1=1 ";
		}
		
		if(!StringUtil.IsEmpty(vendor_name)) {
			strSQL += " and vendor_name like '%" + vendor_name.trim() + "%' ";
		}
		if(!StringUtil.IsEmpty(vendor_add)) {
			strSQL += " and vendor_add like '%" + vendor_add.trim() + "%' ";
		}
		
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		List tmpList = jt.queryForList(strSQL);
		return tmpList;
	}

	public List getVendorInfoListForSxlt(String vendor_name, String vendor_add) {
		String strSQL = "select * FROM tab_vendor where 1=1 ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select vendor_id, vendor_name, vendor_add, telephone, remark, staff_id FROM tab_vendor where 1=1 ";
		}
		
		if(!StringUtil.IsEmpty(vendor_name)) {
			strSQL += " and vendor_name like '%" + vendor_name.trim() + "%' ";
		}
		if(!StringUtil.IsEmpty(vendor_add)) {
			strSQL += " and vendor_add like '%" + vendor_add.trim() + "%' ";
		}
		
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		List tmpList = jt.queryForList(strSQL);
		return tmpList;
	}

	public List getAllModelList4jlEther() {
		String strSQL = "select e.ethernum,e.etherrate,m.device_model_id modelid, v.vendor_add vendorname, v.vendor_id ouiname, m.device_model modelname"
			+ " from gw_device_model m inner join tab_vendor v on m.vendor_id=v.vendor_id left join ether_num_rate e on e.device_model_id=m.device_model_id order by ";

		// oracle
		if(Global.DB_ORACLE == DBUtil.GetDB()){
			strSQL = strSQL + " to_number(m.device_model_id) desc";
		}
		// sysbase
		else if (Global.DB_SYBASE == DBUtil.GetDB()) {
			strSQL = strSQL + " convert(int,m.device_model_id) desc";
		}
		// teledb
		else if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = strSQL + " cast(m.device_model_id as unsigned int) desc";
		}

		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		List tmpList = jt.queryForList(strSQL);
		return tmpList;
	}

	
	/**设备型号修改**/
	public Map<String, Object> updateDeviceModel(String vendor_id, String modelName, String modelId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 1);
		
		String strUpdate = "update gw_device_model set vendor_id='" + vendor_id
				+ "',device_model='" + modelName + "'"
				+ " where device_model_id='" + modelId + "'";
		PrepareSQL psql = new PrepareSQL(strUpdate);
		psql.getSQL();
		int reuslt = jt.update(strUpdate);
		if(reuslt <= 0) {
			resultMap.put("code", 0);
			resultMap.put("message", "更新失败");
		}
		return resultMap;
	}

	/**
	 * 更新机顶盒型号
	 * @param vendor_id
	 * @param modelName
	 * @param modelId
	 * @return
	 */
	public Map<String, Object> updateStbDeviceModel(String vendor_id, String modelName, String modelId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 1);
		
		String strUpdate = "update stb_gw_device_model set vendor_id='" + vendor_id
				+ "',device_model='" + modelName + "'"
				+ " where device_model_id='" + modelId + "'";
		PrepareSQL psql = new PrepareSQL(strUpdate);
		psql.getSQL();
		int reuslt = jt.update(strUpdate);
		if(reuslt <= 0) {
			resultMap.put("code", 0);
			resultMap.put("message", "更新失败");
		}
		return resultMap;
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
				log.warn("dao.resource.DeviceModelDao.initEther() deviceModelIdList is empty!");
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
	/**
	 *@描述 删除机顶盒设备型号
	 *@参数  [modelId]
	 *@返回值  void
	 *@创建人  lsr
	 *@创建时间  2019/11/18
	 *@throws
	 *@修改人和其它信息
	 */
	public void delStbDeviceModel(String modelId) {
		String strDel = "delete from stb_gw_device_model where device_model_id='"
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
			String device_model_id = "";
			CallableStatement cstmt = null;
			Connection conn = null;
			String sql = "{call getTgwModelIdProc(?,?,?)}";
			try
			{
				conn = DBAdapter.getJDBCConnection();
				cstmt = conn.prepareCall(sql);
				cstmt.setString(1, oui);
				cstmt.setString(2, modelName);
				cstmt.registerOutParameter(3, Types.VARCHAR);
				cstmt.execute();
				device_model_id = cstmt.getString(3);
			}
			catch (Exception e)
			{
				log.error("getTgwModelIdProc Exception:{}", e.getMessage());
			}
			finally
			{
				sql = null;
				if (cstmt != null)
				{
					try
					{
						cstmt.close();
					}
					catch (SQLException e)
					{
						log.error("cstmt.close SQLException:{}", e.getMessage());
					}
					cstmt = null;
				}
				if (conn != null)
				{
					try
					{
						conn.close();
					}
					catch (Exception e)
					{
						log.error("conn.close error:{}", e.getMessage());
					}
					conn = null;
				}
			}
			return device_model_id;
		}
	}
	/**
	 *@描述 添加机顶盒设备厂商，调用存储过程
	 *@参数  [modelName, oui]
	 *@返回值  java.lang.String
	 *@创建人  lsr
	 *@创建时间  2019/11/18
	 *@throws
	 *@修改人和其它信息
	 */
	public String addStbDeviceModel(String modelName, String oui) {
		
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			return ZooCuratorLockUtil.getInstance().getTgwModelId(oui, modelName, "4");
		}else {
			String device_model_id = "";
			CallableStatement cstmt = null;
			Connection conn = null;
			String sql = "{call getStbModelIdProc(?,?,?)}";
			try
			{
				conn = DBAdapter.getJDBCConnection();
				cstmt = conn.prepareCall(sql);
				cstmt.setString(1, oui);
				cstmt.setString(2, modelName);
				cstmt.registerOutParameter(3, Types.VARCHAR);
				cstmt.execute();
				device_model_id = cstmt.getString(3);
			}
			catch (Exception e)
			{
				log.error("getStbModelIdProc Exception:{}", e);
			}
			finally
			{
				sql = null;
				if (cstmt != null)
				{
					try
					{
						cstmt.close();
					}
					catch (SQLException e)
					{
						log.error("cstmt.close SQLException:{}", e);
					}
					cstmt = null;
				}
				if (conn != null)
				{
					try
					{
						conn.close();
					}
					catch (Exception e)
					{
						log.error("conn.close error:{}", e);
					}
					conn = null;
				}
			}
			return device_model_id;
		}	
	}

	public Map<String, Object> addStbVendorInfo(String str_vendor_name, String str_vendor_add, String str_telephone, String str_staff_id, String str_remark){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 1);
		
		String getVendorIdSql = "select nvl(max(to_number(vendor_id)),0)+1 as vendorid from stb_tab_vendor";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			getVendorIdSql = "select ifnull(max(to_number(vendor_id)),0)+1 as vendorid from stb_tab_vendor";
		}
		String str_vendor_id = StringUtil.getStringValue(DataSetBean.getRecord(getVendorIdSql),"vendorid");
		String queryByName = "select vendor_id from stb_tab_vendor where vendor_name ='" + str_vendor_name + "'";
		if (DataSetBean.getRecord(queryByName) == null) {
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.setSQL("insert into stb_tab_vendor(vendor_id,vendor_name,vendor_add,telephone,staff_id,remark) values(?,?,?,?,?,?)");
			pSQL.setStringExt(1, str_vendor_id, true);
			pSQL.setStringExt(2, str_vendor_name, true);
			pSQL.setStringExt(3, str_vendor_add, true);
			pSQL.setStringExt(4, str_telephone, true);
			pSQL.setStringExt(5, str_staff_id, true);
			pSQL.setStringExt(6, str_remark, true);
			int row = DataSetBean.executeUpdate(pSQL.getSQL());
			if (row <= 0) {
				resultMap.put("code", 0);
				resultMap.put("message", "新增失败");
			}
		} else {
			resultMap.put("code", -2);
			resultMap.put("message", "厂商名已存在");
		}
		return resultMap;
	}

	public Map<String, Object> addVendorInfoForSxlt(String str_vendor_name, String str_vendor_add, String str_telephone, String str_staff_id, String str_remark){
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 1);

		String getVendorIdSql = "select nvl(max(to_number(vendor_id)),0)+1 as vendorid from tab_vendor";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			getVendorIdSql = "select ifnull(max(to_number(vendor_id)),0)+1 as vendorid from tab_vendor";
		}
		String str_vendor_id = StringUtil.getStringValue(DataSetBean.getRecord(getVendorIdSql),"vendorid");
		String queryByName = "select vendor_id from tab_vendor where vendor_name ='" + str_vendor_name + "'";
		if (DataSetBean.getRecord(queryByName) == null) {
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.setSQL("insert into tab_vendor(vendor_id,vendor_name,vendor_add,telephone,staff_id,remark) values(?,?,?,?,?,?)");
			pSQL.setStringExt(1, str_vendor_id, true);
			pSQL.setStringExt(2, str_vendor_name, true);
			pSQL.setStringExt(3, str_vendor_add, true);
			pSQL.setStringExt(4, str_telephone, true);
			pSQL.setStringExt(5, str_staff_id, true);
			pSQL.setStringExt(6, str_remark, true);
			int row = DataSetBean.executeUpdate(pSQL.getSQL());
			if (row <= 0) {
				resultMap.put("code", 0);
				resultMap.put("message", "新增失败");
			}
		} else {
			resultMap.put("code", -1);
			resultMap.put("message", "厂商名已存在");
		}
		return resultMap;
	}

	public Map<String, Object> updateStbVendorInfo(String str_vendor_id, String str_vendor_name, String str_vendor_add, String str_telephone, String str_staff_id, String str_remark){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 1);
		
		if (null == str_vendor_id) {
			resultMap.put("code", -1);
			resultMap.put("message", "厂商ID不能为空");
		} else {
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.setSQL("update stb_tab_vendor set vendor_name=?,vendor_add=?,telephone=?,staff_id=?,remark=? where vendor_id=?");
			pSQL.setStringExt(1, str_vendor_name, true);
			pSQL.setStringExt(2, str_vendor_add, true);
			pSQL.setStringExt(3, str_telephone, true);
			pSQL.setStringExt(4, str_staff_id, true);
			pSQL.setStringExt(5, str_remark, true);
			pSQL.setStringExt(6, str_vendor_id, true);
			int row = DataSetBean.executeUpdate(pSQL.getSQL());
			if (row < 0) {
				resultMap.put("code", 0);
				resultMap.put("message", "更新失败");
			}
		}
		return resultMap;
	}

	public Map<String, Object> updateVendorInfoForSxlt(String str_vendor_id, String str_vendor_name, String str_vendor_add, String str_telephone, String str_staff_id, String str_remark){
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 1);
		
		if (null == str_vendor_id) {
			resultMap.put("code", -1);
			resultMap.put("message", "厂商ID不能为空");
		} else {
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.setSQL("update tab_vendor set vendor_name=?,vendor_add=?,telephone=?,staff_id=?,remark=? where vendor_id=?");
			pSQL.setStringExt(1, str_vendor_name, true);
			pSQL.setStringExt(2, str_vendor_add, true);
			pSQL.setStringExt(3, str_telephone, true);
			pSQL.setStringExt(4, str_staff_id, true);
			pSQL.setStringExt(5, str_remark, true);
			pSQL.setStringExt(6, str_vendor_id, true);
			int row = DataSetBean.executeUpdate(pSQL.getSQL());
			if (row < 0) {
				resultMap.put("code", 0);
				resultMap.put("message", "更新失败");
			}
		}
		return resultMap;
	}

	public Map<String, Object> deleteStbVendorInfo(String str_vendor_id){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 1);
		
		// 查询是否存在关联型号
		String queryModelByVendorId = "select * from stb_gw_device_model where vendor_id ='" + str_vendor_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			queryModelByVendorId = "select device_model_id, vendor_id, device_model, prot_id from stb_gw_device_model where vendor_id ='" + str_vendor_id + "'";
		}

		boolean result = true;
		if (null == str_vendor_id) {
			resultMap.put("code", -1);
			resultMap.put("message", "厂商ID不能为空");
		} else if(DataSetBean.getRecord(queryModelByVendorId) != null) {
			resultMap.put("code", -2);
			resultMap.put("message", "该厂商下存在关联型号，请先删除型号");
		} else {
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.setSQL("delete from stb_tab_vendor where vendor_id =?");
			pSQL.setStringExt(1, str_vendor_id, true);
			int row = DataSetBean.executeUpdate(pSQL.getSQL());
			if (row < 0) {
				resultMap.put("code", 0);
				resultMap.put("message", "删除失败");
			}
		}
		return resultMap;
	}

	public Map<String, Object> deleteVendorInfoForSxlt(String str_vendor_id){

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 1);
		// 查询是否存在关联型号
		String queryModelByVendorId = "select * from gw_device_model where vendor_id ='" + str_vendor_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			queryModelByVendorId = "select device_model_id, vendor_id, device_model, prot_id from gw_device_model where vendor_id ='" + str_vendor_id + "'";
		}
		
		if (null == str_vendor_id) {
			resultMap.put("code", -1);
			resultMap.put("message", "厂商ID不能为空");
		} else if(DataSetBean.getRecord(queryModelByVendorId) != null) {
			resultMap.put("code", -2);
			resultMap.put("message", "该厂商下存在关联型号，请先删除型号");
		} else {
			PrepareSQL pSQL = new PrepareSQL();
			pSQL.setSQL("delete from tab_vendor where vendor_id =?");
			pSQL.setStringExt(1, str_vendor_id, true);
			int row = DataSetBean.executeUpdate(pSQL.getSQL());
			if (row < 0) {
				resultMap.put("code", 0);
				resultMap.put("message", "删除失败");
			}
		}
		return resultMap;
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

	public int getOuiCounts(String deviceModelId,String gwType){
		String strSQL = "select count(1) total from tab_gw_device_init_oui where DEVICE_MODEL_ID = ? and gw_type = ?";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select count(*) total from tab_gw_device_init_oui where DEVICE_MODEL_ID = ? and gw_type = ?";
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, deviceModelId);
		psql.setString(2, gwType);
		psql.getSQL();
		return jt.queryForInt(psql.getSQL());
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

	public void addDeviceOui(String ouiId,String modelId,String gw_type) {
		String id = getId();
		PrepareSQL psql = new PrepareSQL("insert into tab_gw_device_init_oui(id,oui,DEVICE_MODEL_ID,gw_type) values(?,?,?,?)");
		int num = 1;
		boolean isNum = id.matches("[0-9]+");
		if(isNum){
			num = Integer.parseInt(id)+1;
		}
		psql.setInt(1,num);
		psql.setString(2,ouiId);
		psql.setString(3,modelId);
		psql.setString(4,gw_type);
		jt.update(psql.getSQL());
	}

	public String getId(){
		//sybase数据库用法
		//PrepareSQL psql = new PrepareSQL("select max(convert(numeric, t.id)) id from tab_gw_device_init_oui t");
		//oracle数据库用法
		PrepareSQL psql = new PrepareSQL("select max(TO_NUMBER(t.id)) id from tab_gw_device_init_oui t");
		Map map = DataSetBean.getRecord(psql.getSQL());
		return StringUtil.getStringValue(map.get("id"));
	}

	public void updateDeviceOui(int id,String ouiId) {
		PrepareSQL psql = new PrepareSQL("update tab_gw_device_init_oui set oui = ? where id = ?");
		psql.setString(1,ouiId);
		psql.setInt(2,id);
		jt.update(psql.getSQL());
	}

	public void deleteDeviceOui(int id) {
		PrepareSQL psql = new PrepareSQL("delete from tab_gw_device_init_oui where id = ?");
		psql.setInt(1,id);
		jt.update(psql.getSQL());
	}
	
	/**
	 * 添加型号
	 * @param deviceModelName 
	 * @param oui
	 * @param ouiId
	 * @return
	 */
	public Map<String, Object> addModel(String deviceModelName, String vendor_id, String oui) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 1);
        // 查询是否存在关联型号
		String queryModelByModelNameAndVendorId = "select device_model_id from gw_device_model "
				+ "where device_model ='" + deviceModelName + "' and vendor_id = '" + vendor_id + "'";
		
		// 如果存在型号，则只插入oui表
		Map modelMap = DataSetBean.getRecord(queryModelByModelNameAndVendorId);
		if(modelMap != null) 
		{
			String device_model_id = StringUtil.getStringValue(modelMap, "device_model_id");
			
			// 查询是否存在相同的OUI
			String queryOui = "select id from tab_gw_device_init_oui "
					+ "where device_model_id ='" + device_model_id +"' and vendor_id = '" + vendor_id 
					+ "' and oui = '" + oui + "'";
			
			// 如果存在相同厂商、型号与oui，则不插入
			if(DataSetBean.getRecord(queryOui) != null) 
			{
				resultMap.put("code", -1);
				resultMap.put("message", "该型号及关联OUI已经存在");
			} else 
			{
				int addResult = addOui (vendor_id, device_model_id, deviceModelName, oui, "1");
				if(addResult <= 0) {
					resultMap.put("code", 0);
					resultMap.put("message", "添加失败");
				}
			}
		}
		else {
			// 添加型号
			String device_model_id = addDeviceModel(deviceModelName, vendor_id);
			// 添加OUI
			int addResult = addOui (vendor_id, device_model_id, deviceModelName, oui, "1");
			if(addResult <= 0) {
				resultMap.put("code", 0);
				resultMap.put("message", "添加失败");
			}
			
		}
		
		return resultMap;
	}
	
	/**
	 * 添加机顶盒型号
	 * @param deviceModelName 
	 * @param oui
	 * @param ouiId
	 * @return
	 */
	public Map<String, Object> addStbModel(String deviceModelName, String vendor_id, String oui) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 1);
		// 查询是否存在关联型号
		String queryModelByModelNameAndVendorId = "select device_model_id from stb_gw_device_model "
				+ "where device_model ='" + deviceModelName + "' and vendor_id = '" + vendor_id + "'";
		
		// 如果存在型号，则只插入oui表
		Map modelMap = DataSetBean.getRecord(queryModelByModelNameAndVendorId);
		if(modelMap != null) 
		{
			String device_model_id = StringUtil.getStringValue(modelMap, "device_model_id");
			
			// 查询是否存在相同的OUI
			String queryOui = "select id from tab_gw_device_init_oui "
					+ "where device_model_id ='" + device_model_id +"' and vendor_id = '" + vendor_id 
					+ "' and oui = '" + oui + "'";
			
			// 如果存在相同厂商、型号与oui，则不插入
			if(DataSetBean.getRecord(queryOui) != null) 
			{
				resultMap.put("code", -1);
				resultMap.put("message", "该型号及关联OUI已经存在");
			} else 
			{
				int addResult = addOui (vendor_id, device_model_id, deviceModelName, oui, "4");
				if(addResult <= 0) {
					resultMap.put("code", 0);
					resultMap.put("message", "添加失败");
				}
			}
		}
		else {
			// 添加型号
			String device_model_id = addStbDeviceModel(deviceModelName, vendor_id);
			// 添加OUI
			int addResult = addOui (vendor_id, device_model_id, deviceModelName, oui, "4");
			if(addResult <= 0) {
				resultMap.put("code", 0);
				resultMap.put("message", "添加失败");
			}
			
		}
		
		return resultMap;
	}

	/**
	 * 删除型号
	 * @param deviceModelId 型号id
	 * @param id ouiId
	 * @return
	 */
	public Map<String, Object> delModel(String deviceModelId, int id) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 1);
        // 查询是否存在关联型号
		String queryVersionByModelId = "select * from tab_devicetype_info where device_model_id ='" + deviceModelId + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			queryVersionByModelId = "select devicetype_id, vendor_id, device_model_id, softwareversion from tab_devicetype_info where device_model_id ='" + deviceModelId + "'";
		}
		// 查询oui是否激活
		String queryIsLinkById = "select is_link from tab_gw_device_init_oui where id =" + id +  " and gw_type = '1'";
		HashMap map = DataSetBean.getRecord(queryIsLinkById);
		// 查询是否存在多个OUI
    	int count = getOuiCounts(deviceModelId,"1");
    	
    	// 如果oui为已激活则不能删除
    	if(map != null && StringUtil.getIntValue(map, "is_link") == 1) 
    	{
			resultMap.put("code", -1);
			resultMap.put("message", "关联OUI已激活，请先删除OUI信息");
		}
    	else if(count > 1)
    	{	// 如果存在多个oui则只删除oui表
			deleteDeviceOui(id);
		} 
		else if(DataSetBean.getRecord(queryVersionByModelId) != null) 
		{
			resultMap.put("code", -2);
			resultMap.put("message", "该型号下存在关联版本, 请先删除版本");
		}
		else 
		{
			delDeviceModel(deviceModelId);
			//delDeviceModelType(deviceModelId);
			if(id != 0) {
				deleteDeviceOui(id);
			}
		}
		return resultMap;
	}
	
	/**
	 * 删除机顶盒型号
	 * @param deviceModelId 型号id
	 * @param id ouiId
	 * @return
	 */
	public Map<String, Object> delStbModel(String deviceModelId, int id) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 1);
		// 查询是否存在关联型号
		String queryVersionByModelId = "select * from stb_tab_devicetype_info where device_model_id ='" + deviceModelId + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			queryVersionByModelId = "select devicetype_id, vendor_id, device_model_id, softwareversion from stb_tab_devicetype_info where device_model_id ='" + deviceModelId + "'";
		}

		// 查询oui是否激活
		String queryIsLinkById = "select is_link from tab_gw_device_init_oui where id =" + id +  " and gw_type = '4'";
		HashMap map = DataSetBean.getRecord(queryIsLinkById);
		// 查询是否存在多个OUI
		int count = getOuiCounts(deviceModelId,"4");
		
		// 如果oui为已激活则不能删除
		if(map != null && StringUtil.getIntValue(map, "is_link") == 1) 
		{
			resultMap.put("code", -1);
			resultMap.put("message", "关联OUI已激活，请先删除OUI信息");
		}
		else if(count > 1)
		{	// 如果存在多个oui则只删除oui表
			deleteDeviceOui(id);
		} 
		else if(DataSetBean.getRecord(queryVersionByModelId) != null) 
		{
			resultMap.put("code", -2);
			resultMap.put("message", "该型号下存在关联版本, 请先删除版本");
		}
		else 
		{
			delStbDeviceModel(deviceModelId);
			if(id != 0) {
				deleteDeviceOui(id);
			}
		}
		return resultMap;
	}
	
	/**
	 * 添加OUI
	 * @param vendor_id
	 * @param device_model_id
	 * @param device_model
	 * @param oui
	 * @param gw_type
	 * @return
	 */
	public int addOui (String vendor_id, String device_model_id, String device_model, 
			String oui, String gw_type) {
		String id = getId();
		PrepareSQL psql = new PrepareSQL("insert into tab_gw_device_init_oui"
				+ "(id, oui, vendor_id, device_model_id, device_model, gw_type) values (?,?,?,?,?,?)");
		int num = 1;
		boolean isNum = id.matches("[0-9]+");
		if(isNum){
			num = Integer.parseInt(id)+1;
		}
		psql.setInt(1, num);
		psql.setString(2, oui);
		psql.setString(3, vendor_id);
		psql.setString(4, device_model_id);
		psql.setString(5, device_model);
		psql.setString(6, gw_type);
		return jt.update(psql.getSQL());
	}

}