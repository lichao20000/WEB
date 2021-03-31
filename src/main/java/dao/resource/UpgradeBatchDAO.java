package dao.resource;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

import dao.util.JdbcTemplateExtend;

/**
 * @author Jason(3412)
 * @date 2009-4-9
 */
public class UpgradeBatchDAO {
	private static final Logger LOG = LoggerFactory
			.getLogger(UpgradeBatchDAO.class);

	private JdbcTemplateExtend jt;

	/**
	 * 获取所有版本文件列表file_id,file_name
	 * 
	 * @author Jason(3412)
	 * @date 2009-4-9
	 * @return List
	 */
	public List getSofefileList() {
		return getSofefileList(null, null);
	}

	/**
	 * 根据设备型号和设备类型获取版本文件列表file_id,file_name
	 * 
	 * @param device_model_id,
	 *            devicetype_id
	 * @author Jason(3412)
	 * @date 2009-4-9
	 * @return List
	 */
	public List getSofefileList(String deviceModelId, String devicetypeId) {
		LOG.debug("getSofefileList");
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select distinct softwarefile_name "
				+ " from tab_software_file where softwarefile_isexist=1 ");
		if (null != deviceModelId && !"".equals(deviceModelId)
				&& !"-1".equals(deviceModelId)) {
			strSQL.append(" and device_model_id='");
			strSQL.append(deviceModelId);
			strSQL.append("'");
		}

		if (null != devicetypeId && !"".equals(devicetypeId)
				&& !"-1".equals(devicetypeId)) {
			strSQL.append(" and devicetype_id=");
			strSQL.append(devicetypeId);
		}
		PrepareSQL psql = new PrepareSQL(strSQL.toString());
		psql.getSQL();
		return jt.queryForList(strSQL.toString());
	}

	/**
	 * 获取策略的执行方式type_id,type_name
	 * 
	 * @param needFirstType:是否需要立即执行选项
	 * @author Jason(3412)
	 * @date 2009-4-9
	 * @return List
	 */
	public List getStrategyTypeList(boolean needFirstType) {
		String strSQL = "select type_id,type_name from gw_strategy_type";
		if (needFirstType == false) {
			strSQL += " where type_id != 0";
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return jt.queryForList(strSQL);
	}

	/**
	 * 获取软件升级策略,工单参数
	 * 
	 * @param 软件文件ID
	 * @author Jason(3412)
	 * @date 2009-4-9
	 * @return String 工单参数
	 */
	public String getSoftUpSheetParam(String softfileName) {
		LOG.debug("getSoftUpSheetParam");
		StringBuffer sbParam = new StringBuffer(50);
		Map map = getSofefileInfo(softfileName);
		if (null == map || map.isEmpty()) {
			sbParam.append("");
		} else {
			// 关键字
			sbParam.append("soft_batch_upgrade");
			sbParam.append("|||");
			// 文件路径
			sbParam.append(map.get("outter_url"));
			sbParam.append("/");
			sbParam.append(map.get("server_dir"));
			sbParam.append("/");
			sbParam.append(map.get("softwarefile_name"));
			// 用户名密码为空
			sbParam.append("|||||||||");
			sbParam.append(map.get("softwarefile_size"));
			sbParam.append("|||");
			sbParam.append(map.get("softwarefile_name"));
			// 时延为0；成功URL,失败URL为空
			sbParam.append("|||0||||||");
		}
		LOG.debug("getSoftUpSheetParam:" + sbParam.toString());
		return sbParam.toString();
	}

	/**
	 * 获取版本文件的详细信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-4-9
	 * @return Map
	 */
	public Map getSofefileInfo(String softfileName) {
		String strSQL = "select distinct b.outter_url,b.server_dir,a.softwarefile_size,a.softwarefile_name"
				+ " from tab_software_file a, tab_file_server b";
		strSQL += " where a.dir_id=b.dir_id and a.softwarefile_name='"
				+ softfileName + "' and a.softwarefile_isexist=1";
		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.getSQL();
		return jt.queryForMap(strSQL);
	}

	/**
	 * 根据MapList获取<option value=value>textarea<option>
	 * 
	 * @param value值;
	 *            textarea显示文字
	 * @author Jason(3412)
	 * @date 2009-4-9
	 * @return String
	 */
	public String getSelectOptiones(List mapList, String value, String textarea) {
		if (mapList == null || mapList.size() <= 0) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			int size = mapList.size();
			Map map = null;
			for (int i = 0; i < size; i++) {
				map = (Map) mapList.get(i);
				if (map != null && !map.isEmpty()) {
					sb.append("<option value='");
					sb.append(map.get(value));
					sb.append("'>");
					sb.append(map.get(textarea));
					sb.append("</option>");
				}
			}
			return sb.toString();
		}
	}

	/**
	 * 获取设备列表，根据属地和devicetype_id
	 * 
	 * @param city_id,
	 *            devicetype_id, onlyOnce:已经配置过的设备是否能重新配置
	 * @author Jason(3412)
	 * @date 2009-4-10
	 * @return List (device_id,oui,device_serialnumber,loopback_ip)
	 */
	public List getDevListByType(String city_id, String devicetype_id,
			boolean onlyOnce) {
		StringBuffer buffrSQL = new StringBuffer();
		buffrSQL
				.append("select a.device_id,a.oui,a.device_serialnumber,a.loopback_ip"
						+ " from tab_gw_device a where 1=1");

		// 只允许配置一次(过滤已经配置软件升级但没有执行的设备)
		if (onlyOnce == true) {
			buffrSQL
					.append(" and not exists (select 1 from gw_serv_strategy b "
							+ " where b.device_id = a.device_id "
							+ " and b.service_id=5 and b.status=0)");
		}

		// 属地
		if (null != city_id && !"".equals(city_id) && !"-1".equals(city_id)) {

			List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
			buffrSQL.append(" and a.city_id in ("
					+ StringUtils.weave(list) + ")");
			list = null;
		}

		// 设备版本
		if (null != devicetype_id && !"".equals(devicetype_id)
				&& !"-1".equals(devicetype_id)) {
			buffrSQL.append(" and a.devicetype_id =" + devicetype_id);
		}
		PrepareSQL psql = new PrepareSQL(buffrSQL.toString());
		psql.getSQL();
		return jt.queryForList(buffrSQL.toString());
	}

	/**
	 * 获取设备的选择列表
	 * 
	 * @param devList:设备列表(包括device_id,oui,device_serialnumber,loopback_ip)
	 *            value：选择框的值(device_id) checkType:选择类型(checkbox|radio)
	 * @author Jason(3412)
	 * @date 2009-4-10
	 * @return String
	 */
	public String getCheckboxDevice(List devList, String value, String checkType) {
		if (devList == null || devList.size() <= 0) {
			return "没有符合条件的设备";
		} else {
			StringBuffer sb = new StringBuffer();
			int size = devList.size();
			Map map = null;
			for (int i = 0; i < size; i++) {
				map = (Map) devList.get(i);
				if (map != null && !map.isEmpty()) {
					sb.append("<input type='");
					sb.append(checkType);
					sb.append("' name='");
					sb.append(value);
					sb.append("' value='");
					sb.append(map.get(value));
					sb.append("'");
					sb.append("'> ");
					sb.append(map.get("oui"));
					sb.append("-");
					sb.append(map.get("device_serialnumber"));
					sb.append(" | ");
					sb.append(map.get("loopback_ip"));
					sb.append("<br>");
				}
			}
			return sb.toString();
		}
	}

	/**
	 * @category setDao
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		this.jt = new JdbcTemplateExtend(dao);
	}

}
