package com.linkage.module.gtms.blocTest.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.module.gtms.blocTest.dao.BusinessSheetDispatchDAO;
import com.linkage.module.gtms.blocTest.obj.DeviceObj;
import com.linkage.module.gtms.blocTest.obj.VoipSheetOBJ;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;

public class BusinessSheetDispatchServImpl implements BusinessSheetDispatchServ {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(BusinessSheetDispatchServImpl.class);


	private BusinessSheetDispatchDAO busDao;
	/** 业务用户是否存在 */
	boolean hasServUser = false;

	/**
	 * 上网业务,IPTV业务
	 * 
	 * @param
	 * @return int 
	 * 1:成功 
	 * -1:策略信息失败或传入参数为空 
	 * -2:业务用户信息失败 
	 * -4:调用后台预读模块失败
	 * 
	 */
	public int netOpen(HgwServUserObj hgwServUserObj, String devId,
			String devSn, String operTypeId, String gw_type) {
		logger.debug("netOpen({},{},{},{},{})", new Object[] { hgwServUserObj,
				devId, devSn });
		hasServUser = busDao.servUserIsExists(StringUtil
				.getLongValue(hgwServUserObj.getUserId()), StringUtil
				.getIntegerValue(hgwServUserObj.getServTypeId()));
		if (null == hgwServUserObj) {
			logger.warn("hgwServUserObj is null");
			return -2;
		}
		if (hasServUser) {
			// 更新业务用户表
			if (busDao.updateServUser(hgwServUserObj,gw_type) < 1) {
				logger.warn("更新业务用户信息失败");
				return -2;
			}

		} else {
			// 添加到业务用户表
			if (busDao.saveServUser(hgwServUserObj,gw_type) < 1) {
				logger.warn("入业务用户表失败");
				return -2;
			}
		}

		// 预读调用对象 *****************该部分的参数还没有确定****2012年5月16日
		// 11:08:59
		PreServInfoOBJ preInfoObj = new PreServInfoOBJ(StringUtil
				.getStringValue(hgwServUserObj.getUserId()), devId, "", devSn,
				StringUtil.getStringValue(hgwServUserObj.getServTypeId()),
				operTypeId);
		if (1 == CreateObjectFactory.createPreProcess(gw_type)
				.processServiceInterface(CreateObjectFactory.createPreProcess()
						.GetPPBindUserList(preInfoObj))) {
			logger.debug("调用后台预读模块成功");
			return 1;
		} else {
			logger.warn("调用后台预读模块失败");
			return -4;
		}
	}

	/**
	 * VOIP开户
	 * 
	 * @param
	 * @return int 
	 * 1:成功 
	 * -1:策略信息失败或传入参数为空 
	 * -2:业务用户信息失败 
	 * -3:局向信息获取不到相应的VOIP服务器地址
	 * -4:调用后台预读模块失败
	 */
	public int voipOpen(HgwServUserObj hgwServUserObj,
			VoipSheetOBJ voipSheetOBJ, String deviceSn,
			String deviceId,String operTypeId, String gw_type) {
		logger.debug("voipOpen({},{},{},{})", new Object[] { hgwServUserObj,
				voipSheetOBJ, deviceSn,deviceId,operTypeId ,gw_type });
		//各种异常
		int flag = 0;
		// 业务处理sqlList
		ArrayList<String> servSqlList = new ArrayList<String>();
		// 判断业务用户是否存在
		hasServUser = busDao.servUserIsExists(StringUtil
				.getLongValue(hgwServUserObj.getUserId()), StringUtil
				.getIntegerValue(hgwServUserObj.getServTypeId()));
		
	    //获取工单中SIP服务器参数ID
		int sipId = busDao.getSipId(voipSheetOBJ);

		if (null == hgwServUserObj) {
			logger.warn("hgwServUserObj  is null");
			return -1;
		}
		if (hasServUser) {
			// 更新业务用户表
			// 业务用户存在
			logger.debug("业务用户存在");
			// 判断业务参数是否一致
			int iRet = busDao.equalVoipServParam(voipSheetOBJ,sipId);
			if (1 == iRet) {
				// 如果一致则退出
				return 0;
			}
			// 更新业务用户  *********更新时有几个字段不能确定         *****
			servSqlList.add(busDao.updateOpenServUserSql(hgwServUserObj,gw_type));
			if (0 == iRet) {
				// 库中不存在业务参数记录，添加
				servSqlList.add(busDao.saveVoipServParam(
						voipSheetOBJ, sipId));
			} else {
				// 如果不一致，则更新业务参数
				servSqlList.add(busDao.updateVoipServParam(
						voipSheetOBJ, sipId));
			}
		} else {
			// 业务用户不存在
			logger.debug("业务用户不存在");
			// 添加到业务用户表
			// 添加业务用户
			servSqlList.add(busDao.saveOpenServUserSql(hgwServUserObj,gw_type));
			servSqlList.add(busDao.saveVoipServParam(voipSheetOBJ,sipId));
		}
		// 用户已经有绑定的终端
		boolean userHasDev = false;
		DeviceObj devObj = null;
		// 设备存在性判断
		if (hasServUser) {
			// 判断用户是否已经绑定了设备
			devObj = busDao.checkDevice(StringUtil.getIntegerValue(hgwServUserObj.getUserId()));
			if (null != devObj) {
				logger.debug("用户已经绑定了终端");
				userHasDev = true;
			} else {
				userHasDev = false;
			}
		}

		// 需要做自动开通(业务下发)
		boolean needPreprocess = false;

		// 用户已经有绑定终端
		if (true == userHasDev) {
			needPreprocess = true;
		}

		// 有需要执行的数据库操作,业务执行
		if (servSqlList.size() > 0) {
			if (DBOperation.executeUpdate(servSqlList) > 0) {
				// 入库成功
				if (userHasDev) {
					PreServInfoOBJ preInfoObj = null;
					// 调预读做业务下发
					if (needPreprocess) {
						//预读参数待定
						preInfoObj = new PreServInfoOBJ(StringUtil.
								getStringValue(hgwServUserObj.getUserId()), 
								devObj.getDeviceId(),
								devObj.getOui(), devObj.getDevSn(), 
								StringUtil.getStringValue(hgwServUserObj.getServTypeId()), 
								StringUtil.getStringValue(operTypeId));
						if(1==CreateObjectFactory.createPreProcess(gw_type).processServiceInterface(CreateObjectFactory.createPreProcess()
								.GetPPBindUserList(preInfoObj))){
							logger.debug("调用后台预读模块成功");
							flag =  1;
						}else {
							logger.warn("调用后台预读模块失败");
							flag =  -4;
						}
					}
				}else{
					logger.warn("用户未绑定终端");
				}
			}else{
				logger.warn("入业务用户表或者业务参数表是失败");
				flag =  -2;
			}
			
		}
		return flag;
		
	}
   /**
    * 根据deviceId 查找userID
    * @param  String deviceId
    * @return String userId
    */
	public String getUserId(String deviceId) {
		String userId = "";
		Map map =  busDao.getUserId(deviceId);
		if(null != map){
			userId = StringUtil.getStringValue(map.get("user_id"));
		}
		return userId;
	}
	/**
	 * 根据设备序列好或者是用户帐号查设备的基本信息
	 * @param deviceSn 设备序列号 
	 * @param userAccount 用户帐号
	 * @return Mao<String,Object>
	 */
	public Map<String,Object> getBaseInfo(String deviceSn,String userAccount){
		List<Map<String,Object>> list =  busDao.getBaseInfo(deviceSn,userAccount);
		Map<String,Object> map = null;
		if (null != list && list.size() >0){
			map = new HashMap<String,Object>();
			map.put("deviceNum", list.size());
			map.put("deviceId", list.get(0).get("device_id"));
			map.put("deviceSn", list.get(0).get("device_serialnumber"));
			map.put("openStatus", list.get(0).get("open_status"));
			map.put("accessType", list.get(0).get("adsl_hl"));
			map.put("userAccount", list.get(0).get("username"));
			map.put("userId", list.get(0).get("user_id"));
		}
		return map;
	}
	public BusinessSheetDispatchDAO getBusDao() {
		return busDao;
	}

	public void setBusDao(BusinessSheetDispatchDAO busDao) {
		this.busDao = busDao;
	}

}
