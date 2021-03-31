package dao.share;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

import dao.util.JdbcTemplateExtend;

/**
 * @author ASUS E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-2-2
 * @category dao.share
 * 
 */
public class SelectDeviceTagDAO {

	private static Logger log = LoggerFactory.getLogger(SelectDeviceTagDAO.class);
	
	private JdbcTemplateExtend jt;

	/**
	 * @category setDao
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		this.jt = new JdbcTemplateExtend(dao);
	}

	/**
	 * @category getAllCityId 获取所有的属地
	 * 
	 * @param city_id
	 *            当前登录人的属地，
	 * 
	 * @return List
	 */
	public List getAllCityId(String city_id) {

		StringBuffer strSQL = new StringBuffer();
		strSQL
				.append("select city_id,city_name from tab_city where parent_id='");
		strSQL.append(city_id);
		strSQL.append("' or city_id='");
		strSQL.append(city_id);
		strSQL.append("'");
		PrepareSQL psql = new PrepareSQL(strSQL.toString());
		psql.getSQL();
		return jt.queryForList(strSQL.toString());
	}
	
	/**
	 * @category getOfficeList 获取指定属地的局向
	 * 
	 * @param city_id 
	 * 
	 * @return List
	 */
	public List getOfficeList(String city_id) {

		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select a.office_id,a.office_name from tab_office a,tab_city_office_zone b where a.office_id=b.office_id and b.city_id='");
		strSQL.append(city_id);
		strSQL.append("'");
		PrepareSQL psql = new PrepareSQL(strSQL.toString());
		psql.getSQL();
		return jt.queryForList(strSQL.toString());
	}
	
	/**
	 * @category getVendor 获取所有的厂商
	 * 
	 * @param city_id
	 * 
	 * @return List
	 */
	public List getVendor() {

		String sql = "select vendor_id,vendor_name, vendor_add from tab_vendor";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return jt.queryForList(sql);
	}

	/**
	 * @category getDevicetype 获取所有的设备型号
	 * 
	 * @param vendor
	 * 
	 * @return List
	 */
	public List getDeviceModel(String vendor) {

		StringBuffer strSQL = new StringBuffer();

		strSQL
				.append("select device_model_id,device_model from gw_device_model where vendor_id='");
		strSQL.append(vendor);
		strSQL.append("'");
		PrepareSQL psql = new PrepareSQL(strSQL.toString());
		psql.getSQL();
		return jt.queryForList(strSQL.toString());
	}

	/**
	 * @category getVersionList 获取所有的设备版本
	 * 
	 * @param vendor_id
	 * @param deviceModelId
	 * 
	 * @return List
	 */
	public List getVersionList(String deviceModelId) {

		StringBuffer strSQL = new StringBuffer();

		strSQL.append("select a.devicetype_id,a.softwareversion from tab_devicetype_info a where 1=1 ");

		if (null != deviceModelId && !"".equals(deviceModelId)
				&& !"-1".equals(deviceModelId)) {
			strSQL.append(" and a.device_model_id='");
			strSQL.append(deviceModelId);
			strSQL.append("'");
		}

		PrepareSQL psql = new PrepareSQL(strSQL.toString());
		psql.getSQL();

		return jt.queryForList(strSQL.toString());
	}

	/**
	 * @category getDevice 获取所有的终端设备
	 * 
	 * @param city_id
	 * @param vendor_id
	 * @param device_model_id
	 * @param devicetype_id
	 * @param hguser
	 * @param device_serialnumber
	 * @param loopback_ip
	 * 
	 * @return List
	 */
	public List getDeviceUsername(int gw_type,String city_id,String param1, String param2 ) {

		StringBuffer buffrSQL = new StringBuffer();
		buffrSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.loopback_ip"
				+ " from tab_gw_device a, ");
		
		if(1==gw_type){
			buffrSQL.append("tab_hgwcustomer");
		}else{
			buffrSQL.append("tab_egwcustomer");
		}
		
		buffrSQL.append(" b where a.device_status=1 "
				+ " and a.device_id=b.device_id"
				+ " and b.user_state='1' "); 
		
		if(!"00".equals(city_id)){
			buffrSQL.append("and a.city_id in (");
			List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
			buffrSQL.append(StringUtils.weave(list));
			buffrSQL.append(") ");
			list = null;
		}
		
		// 根据用户名
			
		if (null != param1 && !"".equals(param1)) {
			buffrSQL.append(" and b.username = '" + param1 + "'");
		}
		PrepareSQL psql = new PrepareSQL(buffrSQL.toString());
		psql.getSQL();
		return jt.queryForList(buffrSQL.toString());
	}
	
	
	
	
	/**
	 * 根据VOIP电话号码 查询设备
	 * 
	 * 用于宁夏 add by zhangchy 2012-02-21  
	 * 
	 * @param city_id
	 * @param voipPara
	 * @return
	 */
	public List getDeviceByVoipTelNo(int gw_type,String city_id,String voipPara ) {
		
		log.debug("getDeviceByVoipTelNo({},{})", new Object[]{city_id, voipPara});
		
		PrepareSQL psql = new PrepareSQL();
		
		psql.append("select c.device_id, c.oui, c.device_serialnumber, c.loopback_ip");
		psql.append("  from tab_hgwcustomer a, tab_voip_serv_param b, tab_gw_device c  ");
		psql.append(" where a.user_id = b.user_id");
		psql.append("   and a.device_id = c.device_id");
		psql.append("   and a.user_state = '1'");  // 开户
		psql.append("   and c.device_status = 1"); // 设备已确认
		
		if(!"00".equals(city_id)){
			psql.append("   and c.city_id in (");
			List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
			psql.append(StringUtils.weave(list));
			psql.append(") ");
			list = null;
		}
		
		// 根据VOIP电话号码
		if (null != voipPara && !"".equals(voipPara)) {
			psql.append("   and b.voip_phone = '" + voipPara + "'");
		}
		
		if (0 != gw_type ) {
			psql.append(" and c.gw_type = " + gw_type );
		}
		psql.getSQL();
		
		return jt.queryForList(psql.getSQL());
	}
	
	
	/**
	 * 按iTV业务帐号  江苏需求单：JSDX_ITMS-REQ-20120222-LUHJ-001 
	 * 
	 * add by zhangchy 2012-03-01
	 * 
	 * @param city_id
	 * @param iTVUserName iTV业务帐号
	 * @return
	 */
	public List getDeviceByItvUserName(int gw_type,String city_id,String iTVUserName) {
		
		log.debug("SelectDeviceTagDAO==>getDeviceByItvUserName({},{})", new Object[]{city_id, iTVUserName});
		
		PrepareSQL psql = new PrepareSQL();
		
		psql.append("select c.device_id, c.oui, c.device_serialnumber, c.loopback_ip ");
		psql.append("  from tab_hgwcustomer a, hgwcust_serv_info b, tab_gw_device c ");
		psql.append(" where 1 = 1 ");
		psql.append("   and a.user_id = b.user_id ");
		psql.append("   and a.device_id = c.device_id");
		psql.append("   and b.serv_type_id = 11 "); // iTV业务
		psql.append("   and a.user_state = '1'");   // 开户
		psql.append("   and c.device_status = 1");  // 设备已确认
		
		if(!"00".equals(city_id)){
			psql.append("   and c.city_id in (");
			List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
			psql.append(StringUtils.weave(list));
			psql.append(") ");
			list = null;
		}
		
		// 根据VOIP电话号码
		if (null != iTVUserName && !"".equals(iTVUserName)) {
			psql.append("   and b.username = '" + iTVUserName + "'");
		}
		if (0 != gw_type ) {
			psql.append(" and c.gw_type = " + gw_type );
		}
		
		return jt.queryForList(psql.getSQL());
	}
	
	
	/**
	 * 按宽带主帐号   江苏需求单：JSDX_ITMS-REQ-20120222-LUHJ-001  
     * 
     * dd by zhangchy 2012-03-01
	 * 
	 * @param city_id
	 * @param wideNetAccount 宽带主帐号
	 * @return
	 */
	public List getDeviceByWideNetAccount(int gw_type,String city_id,String wideNetAccount) {
		
		log.debug("SelectDeviceTagDAO==>getDeviceByWideNetAccount({},{})", new Object[]{city_id, wideNetAccount});
		
		PrepareSQL psql = new PrepareSQL();
		
		psql.append("select c.device_id, c.oui, c.device_serialnumber, c.loopback_ip ");
		psql.append("  from tab_hgwcustomer a, hgwcust_serv_info b, tab_gw_device c ");
		psql.append(" where 1 = 1 ");
		psql.append("   and a.user_id = b.user_id ");
		psql.append("   and a.device_id = c.device_id");
		psql.append("   and b.serv_type_id = 10 "); // 宽带业务
		psql.append("   and a.user_state = '1'");   // 开户
		psql.append("   and c.device_status = 1");  // 设备已确认
		
		if(!"00".equals(city_id)){
			psql.append("   and c.city_id in (");
			List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
			psql.append(StringUtils.weave(list));
			psql.append(") ");
			list = null;
		}
		
		// 根据VOIP电话号码
		if (null != wideNetAccount && !"".equals(wideNetAccount)) {
			psql.append("   and b.username = '" + wideNetAccount + "'");
		}
		if (0 != gw_type ) {
			psql.append(" and c.gw_type = " + gw_type );
		}
		
		return jt.queryForList(psql.getSQL());
	}
	
	
	
	/**
	 * 获取所有的终端设备
	 * 
	 * @param gw_type
	 * @param city_id
	 * @param param1
	 * @param param2
	 * 
	 * @return List
	 */
	public List getDeviceBySerialno(int gw_type,String city_id,String param1, String param2 ) {

		StringBuffer buffrSQL = new StringBuffer();
		buffrSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.loopback_ip"
				+ " from tab_gw_device a where a.device_status=1 ");
		
		if(!"00".equals(city_id)){
			buffrSQL.append("and a.city_id in (");
			List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
			buffrSQL.append(StringUtils.weave(list));
			buffrSQL.append(") ");
			list = null;
		}
		
		if (null != param1 && !"".equals(param1)) {
			if(param1.length()>5){
				buffrSQL.append(" and a.dev_sub_sn ='").append(param1.substring(param1.length()-6, param1.length())).append("'");
			}
			buffrSQL.append(" and a.device_serialnumber like '%" + param1
					+ "'");
		}
		if (null != param2 && !"".equals(param2)) {
			buffrSQL.append(" and a.loopback_ip = '" + param2 + "'");
		}
		
		if (0 != gw_type ) {
			buffrSQL.append(" and a.gw_type = " + gw_type);
		}
		
		
		
		
		PrepareSQL psql = new PrepareSQL(buffrSQL.toString());
		psql.getSQL();
		return jt.queryForList(buffrSQL.toString());
	}
	
	/**
	 * @category getDevice 获取所有的终端设备
	 * 
	 * @param city_id
	 * @param vendor_id
	 * @param device_model_id
	 * @param devicetype_id
	 * @param hguser
	 * @param device_serialnumber
	 * @param loopback_ip
	 * 
	 * @return List
	 */
	public List getDeviceBySenior(int gw_type,String city_id,String office_id,String vendor_id, String device_model_id , String devicetype_id,String loopback_ip,String online_status) {

		StringBuffer buffrSQL = new StringBuffer();
		buffrSQL.append("select a.device_id,"
				+ " a.oui,a.device_serialnumber,a.loopback_ip"
				+ " from tab_gw_device a ");

		//设备在线状态
		if (null != online_status && !"".equals(online_status) && !"-1".equals(online_status)) {
			buffrSQL.append(" , gw_devicestatus c where a.device_status=1 and a.device_id=c.device_id ");
			buffrSQL.append(" and c.online_status =" + online_status);
		}else{
			buffrSQL.append(" where a.device_status=1 ");
		}
				
		//属地
		if(null != city_id && !"".equals(city_id) && !"-1".equals(city_id) && !"00".equals(city_id)){
			
			List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
			buffrSQL.append( " and a.city_id in ("
					+ StringUtils.weave(list) + ")");
			list = null;
		}
			
		//局向
		if(null != office_id && !"".equals(office_id) && !"-1".equals(office_id)){
			buffrSQL.append(" and a.office_id ='");
			buffrSQL.append(office_id);
			buffrSQL.append("' ");
		}
		
		//设备厂商
		if (null != vendor_id && !"".equals(vendor_id) && !"-1".equals(vendor_id)) {
			buffrSQL.append(" and a.vendor_id ='");
			buffrSQL.append(vendor_id);
			buffrSQL.append("' ");
		}
		
		//设备型号
		if (null != device_model_id && !"".equals(device_model_id) && !"-1".equals(device_model_id)) {
			buffrSQL.append(" and a.device_model_id ='");
			buffrSQL.append(device_model_id);
			buffrSQL.append("' ");
		}
		
		// 设备版本
		if (null != devicetype_id && !"".equals(devicetype_id) && !"-1".equals(devicetype_id)) {
			buffrSQL.append(" and a.devicetype_id =" + devicetype_id);
		}
		
		//设备IP
		if (null != loopback_ip && !"".equals(loopback_ip) && !"-1".equals(loopback_ip)) {
			buffrSQL.append(" and a.loopback_ip = '" + loopback_ip + "' ");
		}
		
		
		//设备IP
		if (0 != gw_type ) {
			buffrSQL.append(" and a.gw_type = " + gw_type);
		}
		PrepareSQL psql = new PrepareSQL(buffrSQL.toString());
		psql.getSQL();
		return jt.queryForList(buffrSQL.toString());
	}
	
	/**
	 * 根据传入的List查询满足条件的数据
	 * 
	 * @param
	 * @author qxq(4174)
	 * @date 2009-02-16
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List getDeviceNormalList(int gw_type,String city_id,List<String> userList) {
		
		StringBuffer buffrSQL = new StringBuffer();
		buffrSQL.append("select a.device_id,a.oui,a.device_serialnumber,a.loopback_ip"
				+ " from tab_gw_device a, ");
		
		if(1==gw_type){
			buffrSQL.append("tab_hgwcustomer");
		}else{
			buffrSQL.append("tab_egwcustomer");
		}
		
		buffrSQL.append(" b where a.device_status=1 "
				+ " and a.device_id=b.device_id"
				+ " and b.user_state='1' ");
		
		if(!"00".equals(city_id)){
			buffrSQL.append("and a.city_id in (");
			List list = CityDAO.getAllNextCityIdsByCityPid(city_id);
			buffrSQL.append(StringUtils.weave(list));
			buffrSQL.append(") ");
			list = null;
		}
		
		// 根据用户名
			
		buffrSQL.append(" and b.username in (");
		
		for(int i=0;i<userList.size();i++){
			buffrSQL.append("'");
			buffrSQL.append(userList.get(i));
			if(i==(userList.size()-1)){
				buffrSQL.append("') ");
			}else{
				buffrSQL.append("',");
			}
		}
		PrepareSQL psql = new PrepareSQL(buffrSQL.toString());
		psql.getSQL();
		return jt.queryForList(buffrSQL.toString());
	}
	
}
