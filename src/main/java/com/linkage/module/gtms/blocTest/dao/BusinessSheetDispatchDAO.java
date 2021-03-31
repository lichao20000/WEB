package com.linkage.module.gtms.blocTest.dao;

import java.util.List;
import java.util.Map;

import com.linkage.module.gtms.blocTest.obj.DeviceObj;
import com.linkage.module.gtms.blocTest.obj.VoipSheetOBJ;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;

public interface BusinessSheetDispatchDAO {

	/**
	 * 
	 * @param _userId
	 *            用户Id，
	 * @param _servTypeId
	 *            业务类型id
	 * @param itvUsername
	 *            用户名
	 * @return true or false
	 */
	public boolean servUserIsExists(long _userId, int _servTypeId,
			String itvUsername);

	/**
	 * 
	 * @param _userId
	 *            用户Id，
	 * @param _servTypeId
	 *            业务类型id
	 * @return true or false
	 */
	public boolean servUserIsExists(long _userId, int _servTypeId);

	/**
	 * 
	 * @param dbHgwObj
	 *            Voip业务模型
	 * @param _servTypeId
	 *            业务类型id
	 * @return  true or false
	 */
	public boolean servUserIsExists(VoipSheetOBJ dbHgwObj, int _servTypeId);

	/**
	 * 根据设备序列号或者用户名查询用户Id
	 * 
	 * @param deviceSn
	 *            设备序列号
	 * @param userAccount
	 *            用户名
	 * @return user_Id
	 */
	public Map getUserId(String deviceId);
	/**
	 * 
	 * @param voipSheetOBJ  
	 * @return 工单中SIP服务器参数ID
	 */
	public int getSipId(VoipSheetOBJ voipSheetOBJ);
	/**
	 * voip工单对象是否和库中存储的VOIP业务参数一致
	 * @param voipSheetOBJ
	 * @param sipId
	 * @return  int 如果一致返回1; 库中不存在相应的业务参数返回0; 如果存在且不一致返回-1
	 */
	public int equalVoipServParam(VoipSheetOBJ voipSheetOBJ,int sipId);
	/**
	 * 新增voip业务用户表
	 * @param hgwServUserObj
	 * @return String 返回新增业务用户表语句
	 */
	public String saveOpenServUserSql(HgwServUserObj hgwServUserObj,String gw_type);
	/**
	 * 更新业务用户表
	 * @param hgwServUserObj
	 * @param voipSheetOBJ
	 * @return String 返回更新业务用户表语句
	 */
	public String updateOpenServUserSql(HgwServUserObj hgwServUserObj,String gw_type);
	/**
	 *  增加业务参数记录
	 * @param voipSheetOBJ 
	 * @return String 增加业务参数记录sql
	 */
	public String saveVoipServParam(VoipSheetOBJ voipSheetOBJ, int sipId);
	/**
	 * 更新业务参数记录
	 * @param voipSheetOBJ
	 * @param sipId
	 * @return String 更新业务参数记录Sql
	 */
	public String updateVoipServParam(VoipSheetOBJ voipSheetOBJ, int sipId);
	/**
	 * 查询设备，用以判断设备存在性
	 * @param userId
	 * @return DeviceObj
	 */
	public DeviceObj checkDevice(int userId);
	/**
	 * 更新业务用户信息【上网和IPTV用】
	 * @param hgwServUserObj
	 * @param gw_type
	 * @return int 更新是否成功
	 */
	public int updateServUser(HgwServUserObj hgwServUserObj,String gw_type);
	/**
	 * 新增业务用户信息【上网和IPTV用】
	 * @param hgwServUserObj
	 * @param gw_type
	 * @return int 新增是否成功
	 */
	public int saveServUser(HgwServUserObj hgwServUserObj,String gw_type);
	/**
	 * 查询基本信息
	 * @param deviceSn 设备序列好
	 * @param userAccount 用户帐号
	 * @return
	 */
	public List<Map<String,Object>> getBaseInfo(String deviceSn,String userAccount);
}
 