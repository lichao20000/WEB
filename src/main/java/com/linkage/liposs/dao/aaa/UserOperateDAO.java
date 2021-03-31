package com.linkage.liposs.dao.aaa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.Encoder;
import com.linkage.module.gwms.Global;

/**
 * 新增、修改AAA认证帐号功能的dao
 * @author 陈仲民  2007-12-03
 * @version 1.0
 */

public class UserOperateDAO {
	
	private JdbcTemplate jt;
	
	private String sql = "";
	
	private ArrayList<String> sqlList = new ArrayList<String>();
	
	private String user_name = "";
	
	//列分隔符
	private String code1 = String.valueOf((char)1);
	
	//行分隔符
	private String code2 = String.valueOf((char)2);
	
	private String vendorSQL = "select distinct a.vendor_id,a.vendor_name from tab_vendor a,tab_gw_device b,tac_device c where b.device_id=c.device_id and a.vendor_id=b.vendor_id";
	
	private String deviceTypeSQL = "select distinct a.device_model_id as device_model,a.vendor_id from tab_gw_device a,tac_device b where a.device_id=b.device_id";
	
	private String deviceSQL = "select b.device_id,b.device_name,b.device_model_id as device_model,b.vendor_id from tac_device a,tab_gw_device b where a.device_id=b.device_id";
	
	private String accounts = "";

	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}

	/**
	 * 解析数据并进行入库操作
	 * @param data1 基本信息数据 (user_name,user_password,default_privilege,eff_date,exp_date)
	 * @param data2 操作权限数据 (user_name,privilege_op,cmd_name,cmd_value)
	 * @param data3 关联设备数据 (device_id)
	 * @param isNew 是否新增数据 1：新增 0：修改
	 * @param basicInfo  原始的基本信息
	 * @param privilegeInfo  原始的权限信息
	 * @param deviceInfo  原始的设备信息
	 * @return 成功返回success 失败返回error
	 */
	public String initData(String data1,String data2,String data3,String isNew,String basicInfo,String privilegeInfo,String deviceInfo){
		
		//解析基本信息
		if (data1 != null && !"".equals(data1)){
			String[] basicInfoList = data1.split(code1);
			
			//初始化帐号
			user_name = basicInfoList[0];
			
			getBasicInfoSQL(basicInfoList, isNew);
		}
		
		//解析帐号权限，只有在有更改的情况下才更新
		if (data2 != null && !privilegeInfo.equals(data2)){
			
			//先删除已有的权限
			deleteCurPrivilege();
			
			//增加新的权限
			if (!"".equals(data2)){
				String[] privilegeInfoList = data2.split(code2);
				getPrivilegeInfo(privilegeInfoList);
			}
		}
		
		//解析设备信息，只有在有更改的情况下才更新
		if (data2 != null && !deviceInfo.equals(data3)){
			
			//先删除已关联的设备
			deleteCurDevice();
			
			//增加新的关联设备
			if (!"".equals(data3)){
				String[] deviceList = data3.split(code2);
				getDeviceList(deviceList);
			}
		}
		
		//执行完成的sql
		excuteSQl();
		
		return "success";
	}
	
	/**
	 * 根据帐号查询基本信息
	 * @param user_name 认证帐号
	 * @return
	 */
	public String getBasciInfo(String user_name){
		
		String basicInfo = "";
		
		//查询帐号基本信息
		sql = "select * from tac_users where user_name='" + user_name + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select eff_date, exp_date, user_name, user_password, default_privilege from tac_users where user_name='" + user_name + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		List list = jt.queryForList(psql.getSQL());
		
		int length = list.size();
		
		for (int i=0;i<length;i++){
			Map data = (Map)list.get(i);
			
			//格式化时间
			long eff_date = Long.parseLong(data.get("eff_date").toString());
			long exp_date = Long.parseLong(data.get("exp_date").toString());
			String eff_str = (new DateTimeUtil(eff_date*1000)).getLongDate();
			String exp_str = (new DateTimeUtil(exp_date*1000)).getLongDate();
			
			basicInfo += (String)data.get("user_name");
			basicInfo += code1;
			basicInfo += Encoder.getFromBase64((String)data.get("user_password"));
			basicInfo += code1;
			basicInfo += data.get("default_privilege").toString();
			basicInfo += code1;
			basicInfo += eff_str;
			basicInfo += code1;
			basicInfo += exp_str;
		}
		
		return basicInfo;
	}
	
	/**
	 * 根据帐号查询权限信息
	 * @param user_name 认证帐号
	 * @return
	 */
	public String getPrivilege(String user_name){
		String privilegeInfo = "";
		
		//查询帐号权限
		sql = "select * from user_privilege where user_name='" + user_name + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select privilege_op, cmd_name, cmd_value, remark from user_privilege where user_name='" + user_name + "'";
		}

		PrepareSQL psql = new PrepareSQL(sql);
		List list = jt.queryForList(psql.getSQL());
		
		int length = list.size();
		
		for (int i=0;i<length;i++){
			Map data = (Map)list.get(i);
			
			if (!"".equals(privilegeInfo)){
				privilegeInfo += code2;
			}
			
			//操作规则 1：禁止 2：允许
			String privilege_op = data.get("privilege_op").toString();
			String privilegeStr = "允许";
			if ("1".equals(privilege_op)){
				privilegeStr = "禁止";
			}
			
			privilegeInfo += user_name;
			privilegeInfo += code1;
			privilegeInfo += (String)data.get("cmd_name");
			privilegeInfo += code1;
			privilegeInfo += (String)data.get("cmd_value");
			privilegeInfo += code1;
			privilegeInfo += privilegeStr;
			privilegeInfo += code1;
			privilegeInfo += (String)data.get("remark");
			
		}
		
		return privilegeInfo;
	}
	
	/**
	 * 根据帐号查询绑定的设备信息
	 * @param user_name
	 * @return
	 */
	public String getDeviceInfo(String user_name){
		
		String deviceInfo = "";
		
		//查询绑定的设备
		sql = "select b.device_id,b.device_name from tac_device_user a,tab_gw_device b where a.device_id=b.device_id and a.user_name='" + user_name + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		List list = jt.queryForList(psql.getSQL());
		
		int length = list.size();
		
		for (int i=0;i<length;i++){
			Map data = (Map)list.get(i);
			
			if (!"".equals(deviceInfo)){
				deviceInfo += code2;
			}
			
			deviceInfo += (String)data.get("device_id");
			deviceInfo += code1;
			deviceInfo += (String)data.get("device_name");
		}
		
		return deviceInfo;
	}
	
	/**
	 * 生成基本信息的sql
	 * @param basicInfoList
	 * @param isNew
	 */
	private void getBasicInfoSQL(String[] basicInfoList, String isNew){
		
		if (basicInfoList.length > 4){
			
			//格式化时间
			DateTimeUtil now = new DateTimeUtil();
			DateTimeUtil data1 = new DateTimeUtil(basicInfoList[3]);
			DateTimeUtil data2 = new DateTimeUtil(basicInfoList[4]);
			
			//对密码进行加密
			String password = Encoder.getBase64(basicInfoList[1]);
			
			if ("1".equals(isNew)){
				sql = "insert into tac_users(user_name,user_password,default_privilege,eff_date,exp_date,create_date,creator)" 
					+ " values('" + user_name + "','" + password + "'," 
					+ basicInfoList[2] + "," + data1.getLongTime() + "," 
					+ data2.getLongTime() + "," + now.getLongTime() + ",'" 
					+ accounts + "')";
			}
			else{
				sql = "update tac_users set " 
					+ "user_password='" + password + "'" 
					+ ",default_privilege=" + basicInfoList[2]
					+ ",eff_date=" + data1.getLongTime()
					+ ",exp_date=" + data2.getLongTime()
					+ ",creator='" + accounts + "'"
					+ " where user_name='" + user_name + "'";
			}
			
//			logger.debug("getBasicInfoSQL:" + sql);
			PrepareSQL psql = new PrepareSQL(sql);
			sqlList.add(psql.getSQL());
		}
		
	}
	
	/**
	 * 生成权限的初始化sql
	 * @param privilegeInfoList
	 */
	private void getPrivilegeInfo(String[] privilegeInfoList){
		int length = privilegeInfoList.length;
		
		//循环
		for (int i=0;i<length;i++){
			
			//为空则不作处理
			String temp = privilegeInfoList[i];
			if (temp != null && !"".equals(temp)){
				
				//权限信息以code1分隔
				String[] data = temp.split(code1);
				
				if (data != null && data.length > 3){
					
					//操作规则 1：禁止 2：允许
					String privilege_op = "2";
					if ("禁止".equals(data[3])){
						privilege_op = "1";
					}
					
					sql = "insert into user_privilege(user_name,cmd_name,privilege_op,cmd_value)" 
						+ " values('" + user_name + "','" + data[1] + "'," 
						+ privilege_op + ",'" + data[2] + "')";
					
//					logger.debug("getPrivilegeInfo:" + sql);
					PrepareSQL psql = new PrepareSQL(sql);
					sqlList.add(psql.getSQL());
				}
			}
		}
	}
	
	/**
	 * 生成关联设备的初始化sql
	 * @param deviceList
	 */
	private void getDeviceList(String[] deviceList){
		int length = deviceList.length;
		
		//处理设备
		for (int i=0;i<length;i++){
			if (deviceList[i] != null && !"".equals(deviceList[i])){
				
				//获取设备的信息，device_id + code1 + device_name
				String[] deviceInfo = deviceList[i].split(code1);
				
				sql = "insert into tac_device_user(device_id,user_name)" 
					+ " values ('" + deviceInfo[0] + "','" + user_name + "')";
				
//				logger.debug("getDeviceList:" + sql);
				PrepareSQL psql = new PrepareSQL(sql);
				sqlList.add(psql.getSQL());
			}
		}
	}
	
	/**
	 * 删除已有的帐号权限
	 *
	 */
	private void deleteCurPrivilege(){
		sql = "delete from user_privilege where user_name='" + user_name + "'";
		
//		logger.debug("deleteCurPrivilege:" + sql);
		PrepareSQL psql = new PrepareSQL(sql);
		sqlList.add(psql.getSQL());
	}
	
	/**
	 * 删除已关联的设备
	 *
	 */
	private void deleteCurDevice(){
		sql = "delete from tac_device_user where user_name='" + user_name + "'";
		
//		logger.debug("deleteCurDevice:" + sql);
		PrepareSQL psql = new PrepareSQL(sql);
		sqlList.add(psql.getSQL());
	}
	
	/**
	 * 执行批量sql
	 *
	 */
	private void excuteSQl(){
		Object[] tmpArr = sqlList.toArray();
		
		int length = tmpArr.length;
		String[] sqlArr = new String[length];
		for (int i=0;i<length;i++){
			sqlArr[i] = tmpArr[i].toString();
		}
		
		jt.batchUpdate(sqlArr);
	}
	
	/**
	 * 获取厂商、设备型号、设备的json数据
	 * @return json数据
	 */
	public StringBuffer getJsonTree(){
		
		int length = 0;
		String vendor_id = "";
		String vendor_name = "";
		String tmp = "";
		
		//初始化树型的json数据
		StringBuffer jsonTree = new StringBuffer();
		jsonTree.append("[{'name':'设备选择','id':'test','leaf':false,'child':[");
		
		//定义hashMap存放数据
		Map dataMap = null;
		//查询设备型号
		Map<String,String> devicetypeMap = getDeviceType();
		//查询设备
		Map<String,String> deviceMap = getDevice();
		
		//查询厂商
		List vendorList = jt.queryForList(vendorSQL);
		length = vendorList.size();
				
		//对厂商进行循环处理
		for (int i=0;i<length;i++){
			dataMap = (Map) vendorList.get(i);
					
			vendor_id = dataMap.get("vendor_id").toString();
			vendor_name = (String)dataMap.get("vendor_name");
			
			if (i != 0){
				tmp += ",{'name':'" + vendor_name + "','id':'" + vendor_id + "','leaf':false,'child':[";
			}
			else{
				tmp += "{'name':'" + vendor_name + "','id':'" + vendor_id + "','leaf':false,'child':[";
			}
			
			//取得厂商下的设备型号的json数据
			tmp += getTypeJson(devicetypeMap,vendor_id,deviceMap);
			
			
			tmp += "],'attr':''}";
			
		}
		
		jsonTree.append(tmp);
		jsonTree.append("],'attr':''}]");
		
		return jsonTree;
	}
	
	/**
	 * 查询设备型号
	 * @return HashMap 键为vendor_id，值为device_model的组合字符串
	 */
	private Map<String,String> getDeviceType(){

		Map dataMap = null;
		String vendor_id = "";
		String device_model = "";
		String key = "";
		String value= "";
		
		Map<String,String> devicetypeMap = new HashMap<String,String>();
		
		//查询设备型号
		PrepareSQL psql = new PrepareSQL(deviceTypeSQL);
		List deviceTypeList = jt.queryForList(psql.getSQL());
		int length = deviceTypeList.size();
				
		//将数据组合成字符串
		for (int i=0;i<length;i++){
			dataMap = (Map) deviceTypeList.get(i);
					
			device_model = (String)dataMap.get("device_model");
			vendor_id = dataMap.get("vendor_id").toString();
			
			//将同一厂商的设备型号组合成字符串，以code1分隔
			key = vendor_id;
			if (devicetypeMap.get(key) != null){
				value = devicetypeMap.get(key) + code1 + device_model;
				
				devicetypeMap.put(key, value);
			}
			else{
				devicetypeMap.put(key, device_model);
			}
		}
		
		return devicetypeMap;
	}
	
	/**
	 * 查询设备信息
	 * @return HashMap 键为vendor_id + code1 + device_model，值为device_id和device_name的组合字符串
	 */
	private Map<String,String> getDevice(){
		
		Map dataMap = null;
		String vendor_id = "";
		String device_model = "";
		String device_id = "";
		String device_name = "";
		String key = "";
		String value= "";
		
		Map<String,String> deviceMap = new HashMap<String,String>();
		

		//查询设备
		PrepareSQL psql = new PrepareSQL(deviceSQL);
		List deviceList = jt.queryForList(psql.getSQL());
		int length = deviceList.size();
		
		//将数据组合成字符串
		for (int i=0;i<length;i++){
			dataMap = (Map) deviceList.get(i);
			
			device_id = (String)dataMap.get("device_id");
			device_name = (String)dataMap.get("device_name");
			device_model = (String)dataMap.get("device_model");
			vendor_id = dataMap.get("vendor_id").toString();
			
			//将同一型号中的设备组合成字符串，设备之间以code1分隔，设备的id和name已code2分隔
			key = vendor_id + code1 + device_model;
			if (deviceMap.get(key) != null){
				value = deviceMap.get(key) + code1 + device_id + code2 + device_name;
				deviceMap.put(key, value);
			}
			else{
				deviceMap.put(key, device_id + code2 + device_name);
			}
		}
		
		return deviceMap;
	}
	
	/**
	 * 获取厂商下的设备型号json数据
	 * @param devicetypeMap 设备类型map
	 * @param vendor_id 厂商id
	 * @param deviceMap 设备map
	 * @return
	 */
	private String getTypeJson(Map<String,String> devicetypeMap,String vendor_id,Map<String,String> deviceMap){
		
		String tmp = "";
		String device_model = "";
		String model_tmp = "";
		
		//得到对应厂商的类型字符串
		String typeTmp = devicetypeMap.get(vendor_id);
		
		if (typeTmp != null && !"".equals(typeTmp)){
			//拆分成数组
			String[] typeArr = typeTmp.split(code1);
			int length = typeArr.length;
			
			//对型号进行循环处理
			for (int i=0;i<length;i++){
				device_model = typeArr[i];
				
				//空格会影响树的生成，全部替换成"_"
				model_tmp = device_model.replaceAll("\\s+", "_");
				
				//第一种型号前不加“,”
				if (i != 0){
					tmp += ",{'name':'" + device_model + "','id':'" + model_tmp + "','leaf':false,'child':[";
				}
				else{
					tmp += "{'name':'" + device_model + "','id':'" + model_tmp + "','leaf':false,'child':[";
				}
				
				//得到该类型下设备的json数据
				tmp += getDeviceJson(deviceMap,vendor_id,device_model);
				
				tmp += "],'attr':''}";
			}
		}
		
		return tmp;
	}
	
	/**
	 * 获取设备型号下的设备json数据
	 * @param deviceMap 设备map
	 * @param vendor_id 厂商id
	 * @param device_model 设备类型
	 * @return
	 */
	private String getDeviceJson(Map<String,String> deviceMap,String vendor_id,String device_model){
		
		String tmp = "";
		String device_id = "";
		String device_name = "";
		
		//得到对应型号的设备字符串
		String deviceTmp = deviceMap.get(vendor_id + code1 + device_model);
		
		if (deviceTmp != null && !"".equals(deviceTmp)){
			//拆分成数组
			String[] deviceArr = deviceTmp.split(code1);
			int length = deviceArr.length;
			
			//对设备循环处理
			for (int i=0;i<length;i++){
				String[] deviceInfo = deviceArr[i].split(code2);
				
				if (deviceInfo != null && deviceInfo.length > 1){
					
					device_id = deviceInfo[0];
					device_name = deviceInfo[1];
					
					if (i != 0){
						tmp += ",{'name':'" + device_name + "','id':'" + device_id + "','leaf':true,'attr':'1'}";
					}
					else{
						tmp += "{'name':'" + device_name + "','id':'" + device_id + "','leaf':true,'attr':'1'}";
					}
				}
			}
		}
		
		return tmp;
	}
	
	/**
	 * 初始化数据连接
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}
}
