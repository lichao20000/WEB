
package com.linkage.module.gwms.config.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DevManageDAO extends SuperDAO
{
	private static Logger logger = LoggerFactory.getLogger(DevManageDAO.class);

	public boolean doSQLList(ArrayList<String> sqllist)
	{
		int iCode[] = DataSetBean.doBatch(sqllist);
		if (iCode != null && iCode.length > 0)
		{
			logger.debug("批量执行策略入库：  成功");
			return true;
		}
		else
		{
			logger.debug("批量执行策略入库：  失败");
			return false;
		}
	}

	/**
	 * 获取软件升级的目标型号，版本对应关系
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return Map
	 */
	public Map getSoftUp()
	{
		String strSQL = "select convert(varchar(8),temp_id)+'|'"
						+ "+ convert(varchar(4),devicetype_id_old),devicetype_id";
		
		//add by zhangcong@ 2011-06-21
		if(LipossGlobals.isOracle())
		{
			strSQL = "select to_char(temp_id,'99999999') || '|'"
				+ " ||  to_char(devicetype_id_old,'9999'),devicetype_id";
		}
		
		if(DBUtil.GetDB()==3){
			strSQL = "select temp_id,devicetype_id_old,devicetype_id";
		}
		strSQL = strSQL + " from gw_soft_upgrade_temp_map";
		PrepareSQL psql = new PrepareSQL(strSQL);
		
		Map m;
		if(DBUtil.GetDB()==3){
			m=new HashMap();
			Cursor cursor=DataSetBean.getCursor(psql.getSQL());
			if(cursor!=null)
			{
				Map fields = cursor.getNext();
				while (null != fields)
				{
					m.put(StringUtil.getStringValue(fields,"temp_id")
							+"|"+StringUtil.getStringValue(fields,"devicetype_id_old"),
								StringUtil.getStringValue(fields,"devicetype_id"));
					fields = cursor.getNext();
				}
			}
		}else{
			m=DataSetBean.getMap(psql.getSQL());
		}
		return m;
	}

	/**
	 * 获取设备的型号
	 * 
	 * @param
	 * @author wangsenbo
	 * @date 2009-12-24
	 * @return Map
	 */
	public String getDevicetypeId(String deviceId)
	{
		String sql = "select devicetype_id from tab_gw_device where device_id = ?";
		PrepareSQL psql = new PrepareSQL();
		psql.setSQL(sql);
		psql.setString(1, deviceId);
		return StringUtil.getStringValue(queryForMap(psql.getSQL()).get("devicetype_id"));
	}

	/**
	 * 根据devicetype_id获取软件升级的工单参数
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2008-12-17
	 * @return Map
	 */
	public Map getSoftFileInfo()
	{
		String strSQL = "select devicetype_id,'|||'+outter_url+'/'+server_dir+'/'+softwarefile_name"
			+ "+'|||||||||'+convert(varchar(8),softwarefile_size)+'|||'+softwarefile_name"
			+ "+'|||0||||||' from tab_software_file a, tab_file_server b where a.dir_id=b.dir_id"
			+ " and softwarefile_isexist=1";
		
		//add by zhangcong@ 2011-06-21
		if(LipossGlobals.isOracle())
		{
			strSQL = "select devicetype_id,'|||' || outter_url || '/' || server_dir || '/' || softwarefile_name"
				+ " || '|||||||||' || to_char(softwarefile_size,'99999999') || '|||' || softwarefile_name"
				+ " || '|||0||||||' from tab_software_file a, tab_file_server b where a.dir_id=b.dir_id"
				+ " and softwarefile_isexist=1";
		}
		
		if(DBUtil.GetDB()==3){
			strSQL = "select devicetype_id,outter_url,server_dir,softwarefile_name,softwarefile_size,softwarefile_name "
					+ "from tab_software_file a,tab_file_server b "
					+ "where a.dir_id=b.dir_id and softwarefile_isexist=1";
		}
		
		PrepareSQL psql = new PrepareSQL(strSQL);
		Map map;
		if(DBUtil.GetDB()==3){
			map=new HashMap();
			Cursor cursor=DataSetBean.getCursor(psql.getSQL());
			if(cursor!=null)
			{
				Map fields = cursor.getNext();
				String value=null;
				while (null != fields)
				{
					value="|||" + StringUtil.getStringValue(fields,"outter_url")
							+ "/" + StringUtil.getStringValue(fields,"server_dir")
							+ "/" + StringUtil.getStringValue(fields,"softwarefile_name")
							+ "|||||||||" + StringUtil.getStringValue(fields,"softwarefile_size")
							+ "|||" + StringUtil.getStringValue(fields,"softwarefile_name")
							+ "|||0||||||" ;
					map.put(StringUtil.getStringValue(fields,"devicetype_id"),value);
					
					fields = cursor.getNext();
					value=null;
				}
			}
		}else{
			map=DataSetBean.getMap(psql.getSQL());
		}
		
		return map;
	}

	/**
	 * 获取预处理的IOR
	 * 
	 * @return
	 */
	public String getPreProcessIOR()
	{
		String iorSQL = "select ior from tab_ior where object_name='PreProcess' and object_poa='PreProcess_Poa'";
		PrepareSQL psql = new PrepareSQL(iorSQL);
		Map<String, String> iorMap = DataSetBean.getRecord(psql.getSQL());
		return iorMap.get("ior");
	}

	/**
	 * 获得策略类型
	 * 
	 * @return
	 */
	public Map getStrategyType(String... typeIds)
	{
		StringBuffer strBuff = new StringBuffer();
		strBuff.append("select type_id,type_name from gw_strategy_type where ");
		for (int i = 0; i < typeIds.length; i++)
		{
			if (i == typeIds.length - 1){
				strBuff.append(" type_id =").append(typeIds[i]);
			}else{
				strBuff.append(" type_id =").append(typeIds[i]).append(" or ");
			}
		}
		PrepareSQL psql = new PrepareSQL(strBuff.toString());
		return DataSetBean.getMap(psql.getSQL());
	}
}
