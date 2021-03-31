/**
 * 终端业务下发实现操作
 */
package dao.confTaskView;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.StaticTypeCommon;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author ASUS E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-3-12
 * @category dao.confTaskView
 * 
 */
public class StrategyConfigDao extends SuperDAO {

	Logger logger = LoggerFactory.getLogger(StrategyConfigDao.class);
	
//	/**
//	 * db
//	 */
//	private JdbcTemplateExtend jt;
//
//	/**
//	 * 注入
//	 * 
//	 * @param jt
//	 * 
//	 * @return void
//	 */
//	public void setDao(DataSource dao) {
//		
//		logger.debug("DataSource({})",dao);
//		
//		this.jt = new JdbcTemplateExtend(dao);
//	}

	/**
	 * 取得该用户账号的北向工单工单号
	 */
	public List getBssSheetId(String username){
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select bss_sheet_id from tab_bss_sheet where type=1 and bind_state=0 ");
		sql.append(" and username='");
		sql.append(username);
		sql.append("' order by receive_date desc");
		PrepareSQL psql = new PrepareSQL(sql.toString());
    	psql.getSQL();
		return jt.queryForList(sql.toString());
	}
	
	/**
	 * 是否下发业务工单
	 * 
	 * @return boolean
	 */
	public boolean isDoBusiness() {
		
		logger.debug("isDoBusiness()");
		
		String execution = LipossGlobals.getLipossProperty("doBusiness");
		
		boolean temp = false;
		
		if("1".equals(execution)){
			temp = true;
		}else{
			temp = false;
		}
		
		return temp;
	}

	/**
	 * 下发工单具体的业务
	 * 
	 * @return boolean
	 */
	public String getDoBusinessParam() {
		
		logger.debug("isDoBusinessParam()");
		
		String execution = LipossGlobals.getLipossProperty("doBusinessParam");
		
		return execution;
	}
	
	/**
	 * 获取终端类型
	 * 
	 * @param deviceId
	 * 
	 * @return String
	 */
	public String getDeviceType(String deviceId) {

		logger.debug("getDeviceType({})",deviceId);
		
		StringBuffer sql = new StringBuffer();

		sql.append("select device_type from tab_gw_device where device_id='");
		sql.append(deviceId);
		sql.append("'");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		
		List list = jt.queryForList(sql.toString());
		
		String deviceType = null;
		
		if(0<list.size()){
			deviceType = (String) ((Map)list.get(0)).get("device_type");
		}
		
		return deviceType;

	}

	/**
	 * 插入业务配置策略工单关联表
	 */
	public int insertGwStrategySheet(String id,String sheet_id ){
		
		StringBuffer sql = new StringBuffer();
		sql.append("insert into gw_strategy_sheet (id,bss_sheet_id,remark) values(");
		sql.append(id);
		sql.append(",'");
		sql.append(sheet_id);
		sql.append("','现场安装插入')");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.update(sql.toString());
	}
	
	/**
	 * 更新tab_bss_sheet
	 */
	public int updateTabBssSheet(String sheet_id ,String result_spec_state){
		
		StringBuffer sql = new StringBuffer();
		long time = (new Date()).getTime()/1000;
		sql.append("update tab_bss_sheet set bind_state=1 ,bind_time=");
		sql.append(time);
		sql.append(",result_spec_state=");
		sql.append(result_spec_state);
		sql.append(",result_spec_time=");
		sql.append(time);
		sql.append(",remark='现场安装更新' where bss_sheet_id='");
		sql.append(sheet_id);
		sql.append("'");
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		return jt.update(sql.toString());
		
	}
	
	/**
	 * 是否为多PVC
	 * 
	 * @param userId
	 * 
	 * @return boolean
	 */
	public boolean isSinglePVC(int userId) {

		logger.debug("isSinglePVC({})",userId);
		
		boolean boolFlag = true;
		
		StringBuffer sql = new StringBuffer();

		sql.append("select flag_pvc from tab_egwcustomer where user_id =");
		sql.append(userId);

		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		
		String flagPvc = null;
		List list = jt.queryForList(sql.toString());
		if(0<list.size()){
			flagPvc  = String.valueOf(((Map)list.get(0)).get("flag_pvc")).toString();
			logger.debug("flagPvc:{}"+flagPvc);
		}
		
		//1标示单PVC，否则为多PVC
		if("1".equals(flagPvc)){
			boolFlag = true;
		}else{
			boolFlag = false;
		}
		logger.debug("boolFlag:{}"+boolFlag);
		return boolFlag;

	}
	
	/**
	 * 查询业务用户user_id
	 * 
	 * @param username
	 * 
	 * @return boolean
	 */
	public int getBbmsUserId(String username) {

		logger.debug("getUserId({})",username);
		
		StringBuffer sql = new StringBuffer();

		sql.append("select user_id from tab_egwcustomer where user_state = '1' and username='");
		sql.append(username);
		sql.append("'");
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		
		int userId = -1;
		List list = jt.queryForList(sql.toString());
		
		if(0<list.size()){
			userId = Integer.parseInt(String.valueOf(((Map)list.get(0)).get("user_id")).toString());
		}
		
		return userId;

	}

	/**
	 * 判断该设备是否已经配置过该业务
	 * 
	 * @param username
	 * 
	 * @return boolean
	 */
	public boolean isAlreadyConfig(String deviceId,int servTypeId) {

		logger.debug("isAlreadyConfig({},{})",deviceId,servTypeId);
		
		boolean flag = false;
		
		StringBuffer sql = new StringBuffer();

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql.append("select count(*) as num from  gw_dev_serv where serv_state!=-1 and device_id = '");
		}
		else {
			sql.append("select count(1) as num from  gw_dev_serv where serv_state!=-1 and device_id = '");
		}

		sql.append(deviceId);
		sql.append("' and serv_type_id=");
		sql.append(servTypeId);
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		
		int num = jt.queryForInt(sql.toString());
		if(0<num){
			flag = true;
		}
		return flag;

	}
	
	/**
	 * 判断该设备是否已经配置过IPTV业务
	 * 
	 * @param username
	 * 
	 * @return boolean
	 */
	public boolean isAlreadyConfigIPTV(String deviceId,int serviceId) {

		logger.debug("isAlreadyConfigIPTV({},{})",deviceId,serviceId);
		
		boolean flag = false;
		
		StringBuffer sql = new StringBuffer();

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql.append("select count(*) as num from  gw_serv_strategy where device_id = '");
		}
		else {
			sql.append("select count(1) as num from  gw_serv_strategy where device_id = '");
		}

		sql.append(deviceId);
		sql.append("' and service_id=");
		sql.append(serviceId);
		
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		
		int num = jt.queryForInt(sql.toString());
		if(0<num){
			flag = true;
		}
		
		return flag;

	}
	
	/**
	 * 获取改用的上网数
	 * 
	 * @param userId
	 * 
	 * @return int
	 */
	public int getChildDeviceTotal(int userId) {

		logger.debug("getChildDeviceTotal({})",userId);
		
		StringBuffer sql = new StringBuffer();

		sql.append("select max_user_number from tab_egwcustomer where user_id=");
		sql.append(userId);

		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		
		int maxUserNumber = -1;
		
		List list = jt.queryForList(sql.toString());
		
		if(0<list.size()){
			Object maxObj = ((Map)list.get(0)).get("max_user_number");
			if(maxObj == null){
				maxUserNumber = 80;
			}else{
				maxUserNumber = Integer.parseInt(maxObj.toString());
			}
		}
		
		return maxUserNumber;

	}

	/**
	 * 获取设备信息
	 * 
	 * @param deviceId
	 *            需获取设备的设备主键
	 * 
	 * @return Map 该设备的信息组装成Map返回
	 */
	public Map getDeviceInfo(String deviceId) {

		logger.debug("getDeviceInfo({})",deviceId);
		
		StringBuffer sql = new StringBuffer();

		sql.append("select device_id,oui,device_serialnumber,gather_id,vendor_id,device_type from ");
		sql.append(" tab_gw_device where device_id='");
		sql.append(deviceId);
		sql.append("'");

		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		
		return jt.queryForMap(sql.toString());
	}

//	/**
//	 * 插入策略表
//	 * 
//	 * @param accOid
//	 *            登录人所属
//	 * @param deviceId
//	 *            待下发设备
//	 * @param sheet_para
//	 *            工单参数
//	 * @param service_id
//	 *            服务
//	 * @param username
//	 *            待下发设备所属用户
//	 * @param type
//	 *            执行方式
//	 * 
//	 * @return boolean 是否操作成功
//	 */
//	public int doStratery(String id, long accOid, String deviceId, String sheetPara,
//			int serviceId, String username, int type, Map deviceInfoMap) {
//
//		logger.debug("doStratery(多参数)");
//		
//		StringBuffer sql = new StringBuffer();
//
//		Date date = new Date();
//
//		sql.append("insert into gw_serv_strategy (id,acc_oid,time,type,");
//		sql.append("gather_id,device_id,oui,device_serialnumber,username,");
//		sql.append("sheet_para,service_id,order_id) values (");
//		sql.append(id);
//		sql.append(",");
//		sql.append(accOid);
//		sql.append(",");
//		sql.append(date.getTime() / 1000);
//		sql.append(",");
//		sql.append(type);
//		sql.append(",'");
//		sql.append(deviceInfoMap.get("gather_id"));
//		sql.append("','");
//		sql.append(deviceId);
//		sql.append("','");
//		sql.append(deviceInfoMap.get("oui"));
//		sql.append("','");
//		sql.append(deviceInfoMap.get("device_serialnumber"));
//		sql.append("','");
//		sql.append(username);
//		sql.append("','");
//		sql.append(sheetPara);
//		sql.append("',");
//		sql.append(serviceId);
//		sql.append(",1)");
//
//		
//		return jt.update(sql.toString());
//	}
	
	public boolean doStratery(String id, long accOid, String deviceId, String sheetPara,
			int serviceId, String username, int type, Map deviceInfoMap )
	{
		StrategyOBJ strategyObj = new StrategyOBJ();
		// 策略ID
		strategyObj.setId(StringUtil.getLongValue(id));
		// 策略配置时间
		strategyObj.setTime(TimeUtil.getCurrentTime());
		// 用户id
		strategyObj.setAccOid(accOid);
		// 立即执行
		strategyObj.setType(type);
		// 设备ID
		strategyObj.setDeviceId(deviceId);
		strategyObj.setOui(StringUtil.getStringValue(deviceInfoMap.get("oui")));
		strategyObj.setGatherId(StringUtil.getStringValue(deviceInfoMap.get("gather_id")));
		strategyObj.setSn(StringUtil.getStringValue(deviceInfoMap.get("device_serialnumber")));
		strategyObj.setUsername(username);
		// QOS serviceId
		strategyObj.setServiceId(serviceId);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(1);
		// 参数
		strategyObj.setSheetPara(sheetPara);
		// task_id
		strategyObj.setTaskId(StaticTypeCommon.generateId());
		strategyObj.setTempId(serviceId);
		strategyObj.setIsLastOne(1);
		return addStrategy(strategyObj);
	}
	/**
	 * 获得预读模块的IOR
	 * 
	 * @return
	 */
	public String getPreProcessIOR() {
		
		logger.debug("getPreProcessIOR()");
		
		String ior = null;
		
		String iorSQL = "select ior from tab_ior where object_name='PreProcess' and object_poa='PreProcess_Poa'";
		
		PrepareSQL psql = new PrepareSQL(iorSQL);
		psql.getSQL();
		
		List list = jt.queryForList(iorSQL);
		
		if(0<list.size()){
			ior = (String) ((Map)list.get(0)).get("ior");
		}
		
		return ior;
	}

	/**
	 * 获取家庭网关信息
	 * 
	 * @param username
	 */
	public Map getItmsUserInfo(String username,String userId){
		
		logger.debug("getItmsUserInfo(username:{})",username);
		
		StringBuffer sql = new StringBuffer();
		sql.append("select user_id,serv_type_id,username,serv_status,passwd,");
		sql.append(" wan_type,vpiid,vciid,vlanid,ipaddress,ipmask,gateway,");
		sql.append(" adsl_ser,bind_port,wan_value_1,wan_value_2,open_status,");
		sql.append(" dealdate,opendate,pausedate,closedate,updatetime");
		if(false==StringUtil.IsEmpty(userId)){
			sql.append(" from hgwcust_serv_info where user_id=");
			sql.append(userId);
			sql.append(" and serv_status=1");
		}else{
			sql.append(" from hgwcust_serv_info where username='");
			sql.append(username);
			sql.append("' and serv_status=1");
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		psql.getSQL();
		List list = jt.queryForList(sql.toString());
	   	if(list.size()>0){
			return (Map)list.get(0);
		}else{
			return null;
		}
	}
}
