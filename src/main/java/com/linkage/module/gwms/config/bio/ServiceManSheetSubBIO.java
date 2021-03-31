package com.linkage.module.gwms.config.bio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.OfficeDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileObj;
import com.linkage.module.gwms.obj.tabquery.HgwCustObj;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;
import com.linkage.module.gwms.util.strategy.StrategyXml;

/**
 * @author Jason(3412)
 * @date 2010-5-7
 */
public class ServiceManSheetSubBIO extends ServiceManSheetBIO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(ServiceManSheetSubBIO.class);

	
	/**
	 * 上网业务,IPTV业务,无线业务
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-23
	 * @return int 1:成功 -1:策略信息失败或传入参数为空 -2:业务用户信息失败 -4:调用后台预读模块失败
	 * 
	 */
	public int netOpen(HgwServUserObj hgwServUserObj, String hasServUser,
			String devId, String oui, String devSn, String operTypeId,
			String gw_type) {
		logger.debug("netOpen({},{},{},{},{})", new Object[] { hgwServUserObj,
				hasServUser, devId, oui, devSn });
		servUserDao.setGw_type(gw_type);
		if (null == hgwServUserObj) {
			logger.warn("hgwServUserObj is null");
			return -2;
		}
		if ("-1".equals(hasServUser)) {
			// 添加到业务用户表
			if (servUserDao.saveServUser(hgwServUserObj) < 1) {
				logger.warn("入业务用户表失败");
				return -2;
			}
		} else {
			// 更新业务用户表
			if (servUserDao.updateServUser(hgwServUserObj) < 1) {
				logger.warn("更新业务用户信息失败");
				return -2;
			}
		}

		// 预读调用对象
		PreServInfoOBJ preInfoObj = new PreServInfoOBJ(
				StringUtil.getStringValue(hgwServUserObj.getUserId()), devId,
				oui, devSn, StringUtil.getStringValue(hgwServUserObj
						.getServTypeId()), operTypeId);
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
	 * @author Jason(3412)
	 * @date 2009-9-23
	 * @return int 1:成功 -1:策略信息失败或传入参数为空 -2:业务用户信息失败 -3:局向信息获取不到相应的VOIP服务器地址
	 *         -4:调用后台预读模块失败
	 */
	public int voipOpen(HgwServUserObj hgwServUserObj, String officeId,
			String hasServUser, StrategyOBJ strategyObj, String gw_type) {
		logger.debug("voipOpen({},{},{},{})", new Object[] { hgwServUserObj,
				officeId, hasServUser, strategyObj });
		if (null == hgwServUserObj || null == strategyObj) {
			logger.warn("hgwServUserObj is null or strategyObj is null");
			return -1;
		}
		if ("-1".equals(hasServUser)) {
			// 添加到业务用户表
			if (servUserDao.saveServUser(hgwServUserObj) < 1) {
				logger.warn("入业务用户表失败");
				return -2;
			}
		} else {
			// 更新业务用户表
			if (servUserDao.updateServUser(hgwServUserObj) < 1) {
				logger.warn("更新业务用户信息失败");
				return -2;
			}
		}
		logger.debug("office_id:{}", officeId);
		VoiceServiceProfileObj voipProfObj = OfficeDAO.getInstance()
				.getOfficeIdVoip().get(officeId);
		if (null == voipProfObj) {
			logger.debug("officeId ({})没有对应的服务器信息", officeId);
			return -3;
		}
		// 策略参数
		String strategyParam = StrategyXml.voipObj2Xml(hgwServUserObj,
				voipProfObj);
		strategyObj.setSheetPara(strategyParam);

		if (servUserDao.addStrategy(strategyObj)) {
			if (CreateObjectFactory.createPreProcess(gw_type).processOOBatch(StringUtil
					.getStringValue(strategyObj.getId()))) {
				logger.debug("业务工单成功");
				return 1;
			} else {
				logger.warn("调用后台预读模块失败");
				return -4;
			}
		} else {
			logger.warn("入策略失败");
			return -1;
		}
	}

	public int queryUndoNum() {
		logger.debug("queryUndoNum()");
		return servUserDao.queryUndoNum();
	}
	
	public String queryMulticastNum() {
		logger.debug("queryMulticastNum()");
		String maxnum = LipossGlobals.getLipossProperty("MaxMulticast");
		int num = StringUtil.getIntegerValue(maxnum);
		long time = new DateTimeUtil(new DateTimeUtil().getDate()).getLongTime();
		
		int actnum = servUserDao.queryMulticastNum(time);
		if(actnum>=num){
			return "今天配置数已达到上限("+maxnum+")，请明日再配置！";
		}
		return "goon";
	}
	
	
	
	/**
	 * 触发终端开通业务
	 * 
	 * @param deviceId
	 *            :设备ID
	 * @param servTypeId
	 *            :业务类型ID
	 * 
	 * @author Jason(3412)
	 * @date 2010-6-11
	 * @return int 1:成功； -2:deviceId为空； -3:终端未绑定用户； -4:终端对应的用户未开通该业务；
	 *         -5:调用后台预读模块失败
	 */
	public int serviceDone(String deviceId, int servTypeId, String gw_type) {
		logger.debug("servDone({},{})", deviceId, servTypeId);
		if (null == deviceId) {
			logger.warn("deviceId is null");
			return -2;
		}

		// set gw_type
		custDao.setGw_type(gw_type);
		servUserDao.setGw_type(gw_type);

		// 查询对应的用户信息
		HgwCustObj hgwCustObj = custDao.getCustObjByDevId(deviceId);
		if (null == hgwCustObj) {
			// 未查询到终端对应的用户
			logger.debug("终端未绑定用户");
			return -3;
		}
		// 用户ID
		long userId = StringUtil.getLongValue(hgwCustObj.getUserId());
		// 用户的业务信息

		HgwServUserObj[] arrHgwServUserObj = servUserDao
				.queryHgwcustServUserByDevId(userId);

		if (null == arrHgwServUserObj) {
			// 未查询到终端对应用户的业务信息
			logger.debug("终端对应的用户未受理任何业务");
			return -4;
		} else {
			// 有对应的业务信息
			if (0 == servTypeId) { // 对用户的所有业务做业务下发

				// 更新业务用户表的开通状态
				servUserDao.updateServOpenStatus(userId, servTypeId);

				// 预读调用对象
				PreServInfoOBJ preInfoObj = new PreServInfoOBJ(
						StringUtil.getStringValue(arrHgwServUserObj[0]
								.getUserId()), deviceId, hgwCustObj.getOui(),
						hgwCustObj.getDeviceSerial(), "", "1");
				if (1 == CreateObjectFactory.createPreProcess(gw_type)
						.processServiceInterface(CreateObjectFactory.createPreProcess()
								.GetPPBindUserList(preInfoObj))) {
					logger.debug("调用后台预读模块成功");
					return 1;
				} else {
					logger.debug("调用后台预读模块失败");
					return -5;
				}
			} else {
				// 遍历业务信息
				for (HgwServUserObj hgwServUserObj : arrHgwServUserObj) {
					if (servTypeId == StringUtil.getIntegerValue(hgwServUserObj
							.getServTypeId())) {

						// 更新业务用户表的业务开通状态
						servUserDao.updateServOpenStatus(userId, servTypeId);

						// 预读调用对象
						PreServInfoOBJ preInfoObj = new PreServInfoOBJ(
								StringUtil.getStringValue(arrHgwServUserObj[0]
										.getUserId()), deviceId,
								hgwCustObj.getOui(),
								hgwCustObj.getDeviceSerial(),
								StringUtil.getStringValue(servTypeId), "1");
						if (1 == CreateObjectFactory.createPreProcess(gw_type)
								.processServiceInterface(CreateObjectFactory.createPreProcess()
										.GetPPBindUserList(preInfoObj))) {
							logger.debug("调用后台预读模块成功");
							return 1;
						} else {
							logger.debug("调用后台预读模块失败");
							return -5;
						}
					}
				}

				logger.debug("用户未受理该业务，无法进行业务下发");
				return -4;
			}
		}
	}

	/**
	 * 
	 * @param list
	 *            :设备集合
	 * @param servTypeId
	 *            业务类型
	 * @param gw_type
	 * @param user_oid  用户编码
	 * @return returnFlag int 1:成功； -1:失败
	 */
	@SuppressWarnings("unchecked")
	public int serviceDoneByBatch(List<String> list, int servTypeId,
			String gw_type, long user_oid) {
		logger.warn("serviceDoneByBatch({},{},{})",
				new Object[] { list.toString(), servTypeId, gw_type });
		int total = 0;
		boolean isJiangsu=Global.JSDX.equals(Global.instAreaShortName);
		if(isJiangsu){
			long taskId=curMillAndRan(3);
			custDao.addBatOptTask(taskId, user_oid, 1, servTypeId,
					0, Integer.parseInt(gw_type),"",System.currentTimeMillis()/1000);
			List<Map> bodList=new ArrayList<Map>();
			Map bod=null;
			for (int i = 0; i < list.size(); i++) {
				String deviceId = list.get(i);
				bod=new HashMap();
				bod.put("taskId", taskId);
				bod.put("deviceId",deviceId);
				bod.put("doStatus",0);
				bod.put("serviceId",servTypeId);
				bod.put("addTime",System.currentTimeMillis()/1000);
				bodList.add(bod);
				total++;
			}
			custDao.addBatOptDevByBatch(bodList);
		}else{
			for (int i = 0; i < list.size(); i++) {
				String deviceId = list.get(i);
				// 调用单台下发函数
				int returnFlag = serviceDone(deviceId, servTypeId, gw_type);
				if (1 != returnFlag) {
					continue;
				}
				total++;
			}
		}
		logger.warn("调用后台预读模块成功" + total + "个");
		if (total > 0) {
			return 1;
		}
		return -1;
	}
	/**
	 *
	 * @param list
	 *            :设备集合
	 * @param servTypeId
	 *            业务类型
	 * @param gw_type
	 * @param user_oid  用户编码
	 * @return returnFlag int 1:成功； -1:失败
	 */
	@SuppressWarnings("unchecked")
	public int serviceDoneByBatchXJDX(List list, int servTypeId,
								  String gw_type, long user_oid) {
		logger.warn("serviceDoneByBatch({},{},{})",
				new Object[] { list.toString(), servTypeId, gw_type });
			int total = 0;

			long taskId=curMillAndRan(3);
			custDao.addBatOptTask(taskId, user_oid, 1, servTypeId,
					0, Integer.parseInt(gw_type),"",System.currentTimeMillis()/1000);
			List<Map> bodList=new ArrayList<Map>();
			Map bod=null;
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				String deviceId =StringUtil.getStringValue(map.get("device_id")) ;
				bod=new HashMap();
				bod.put("taskId", taskId);
				bod.put("deviceId",deviceId);
				bod.put("doStatus",0);
				bod.put("serviceId",servTypeId);
				bod.put("addTime",System.currentTimeMillis()/1000);
				bodList.add(bod);
				total++;
			}
			custDao.addBatOptDevByBatch(bodList);

		logger.warn("调用后台预读模块成功" + total + "个");
		if (total > 0) {
			return 1;
		}
		return -1;
	}
	
	
	
	/**
	 * 批量组播下移
	 * @param list
	 *            :设备集合
	 * @param loidlist 
	 * @param snlist 
	 * @param useridlist 
	 * @param user_oid 
	 * @param gw_type
	 * @return returnFlag int 1:成功； -1:失败
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void setMulticastBatch(List<String> list, List<String> snlist, List<String> loidlist, List<String> useridlist, long user_oid) throws Exception {
		logger.warn("setMulticastBatch({},{})",
				new Object[] { list.size(),user_oid });
		custDao.addMulticastBatch(list,snlist,loidlist,useridlist,user_oid);
	}
	
	
	
	
	/**
	 * 当前秒数值省去三位后，拼接上一个随机正整数
	 * @param ranLen ranLen是这个随机正整数的位数
	 * @return 
	 */
	public static long curMillAndRan(int ranLen){
		if(ranLen<1)
			return 0;
		String curMill=Long.toString(System.currentTimeMillis()/1000);
		String ran= Long.toString(Math.round((Math.random()*9+1)*Math.pow(10,ranLen-1)));
		return Long.parseLong(curMill+ran);
	}
	
	public void serviceDoneRecord(long user_oid,int operlog_type,long oper_time,String oper_object,String oper_result) {
		Map map = new HashMap();
		map.put("user_oid", user_oid);
		map.put("operlog_type", operlog_type);
		map.put("oper_time", oper_time);
		map.put("oper_object", oper_object);
		map.put("oper_result", oper_result);
		map.put("acc_login_ip", "0");
		custDao.recordServiceDone(map);
	}

	public List<String> getListBySql(String matchSQL) {
		return custDao.getListBySql(matchSQL);
	}

	public List<Map> queryTaskList(String servType_query, String status_query, String expire_time_start, String expire_time_end, int curPage_splitPage, int num_splitPage) {
		int status = -1;
		if(!StringUtil.IsEmpty(status_query)){
			status = Integer.valueOf(status_query);
		}
		long expireTimeStart;
		long expireTimeEnd;
		if(null==expire_time_start||"".equals(expire_time_start)){
			expireTimeStart = -1;
		}else{
			expireTimeStart = dealTime(expire_time_start);
		}
		if(null==expire_time_end||"".equals(expire_time_end)){
			expireTimeEnd = -1;
		}else{
			expireTimeEnd = dealTime(expire_time_end);
		}

		return custDao.queryTaskList(servType_query, status, expireTimeStart, expireTimeEnd, curPage_splitPage, num_splitPage);

	}

	public long dealTime(String time) {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date str = new Date();
		try {
			str = date.parse(time);
		}
		catch (ParseException e) {
			logger.warn("选择开始或者结束的时间格式不对:" + time);
		}

		return str.getTime() / 1000L;
	}

	public int queryTaskCount(String servType_query, String status_query,
							  String expire_time_start, String expire_time_end, int curPage_splitPage,
							  int num_splitPage) {
		int status = -1;
		long expireTimeStart;
		long expireTimeEnd;
		if(!StringUtil.IsEmpty(status_query)){
			status = Integer.valueOf(status_query);
		}
		if(null==expire_time_start||"".equals(expire_time_start)){
			expireTimeStart = -1;
		}else{
			expireTimeStart = dealTime(expire_time_start);
		}
		if(null==expire_time_end||"".equals(expire_time_end)){
			expireTimeEnd = -1;
		}else{
			expireTimeEnd = dealTime(expire_time_end);
		}

		return custDao.queryTaskCount(servType_query, status, expireTimeStart, expireTimeEnd, curPage_splitPage, num_splitPage);

	}

	public String delTask(String task_id) {
		return (custDao.delTask(task_id)+"");
	}

	public List<Map> queryTaskGyCityList(String task_id, String servTypeId, int curPage_splitPage, int num_splitPage) {
		return custDao.queryTaskGyCityList(task_id, servTypeId, curPage_splitPage, num_splitPage);
	}

	public int queryTaskGyCityCount(String task_id, String servTypeId, int curPage_splitPage, int num_splitPage) {
		int total =  custDao.queryTaskGyCityCount(task_id);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}

	public List<Map> queryDevList(String task_id, String city_id, String type, int curPage_splitPage, int num_splitPage) {
		return custDao.queryDevList(task_id, city_id, type, curPage_splitPage, num_splitPage);
	}

	public int queryDevCount(String countNum, int curPage_splitPage, int num_splitPage) {
		int total = StringUtil.getIntegerValue(countNum);
		int maxPage = 1;
		if (total % num_splitPage == 0) {
			maxPage = total / num_splitPage;
		} else {
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}
	// /**
	// * <p>
	// * 获取用户ID
	// * </p>
	// * @param deviceId
	// * @return 获取设备绑定的用户
	// */
	// public String getUserIdByDeviceId(String deviceId)
	// {
	// // 查询对应的用户信息
	// HgwCustObj hgwCustObj = custDao.getCustObjByDevId(deviceId);
	//
	// if (null == hgwCustObj) {
	// // 未查询到终端对应的用户
	// logger.warn("终端未绑定用户");
	// return "errorMsg:终端未绑定用户";
	// }
	//
	// // 用户ID
	// long userId = StringUtil.getLongValue(hgwCustObj.getUserId());
	//
	// // 用户的全业务信息
	// HgwServUserObj[] arrHgwServUserObj = servUserDao
	// .queryHgwcustServUserByDevId(userId,"0");
	//
	// if (null == arrHgwServUserObj) {
	// // 未查询到终端对应用户的业务信息
	// logger.warn("终端对应的用户未受理任何业务，无法进行业务下发");
	// return "errorMsg:终端对应的用户未受理任何业务，无法进行业务下发";
	// }
	//
	// // 用户ID
	// return hgwCustObj.getUserId();
	// }
	//
	// /**
	// * <p>
	// * 获取用户对应的业务帐号
	// * </p>
	// * @param user_id 用户ID
	// * @param servTypeId 业务类型(不可能为0-全业务,只可能为10、11、14)
	// * @return
	// */
	// public String getUserNameByservTypeId(String user_id, String servTypeId)
	// {
	// logger.debug("getUserNameByservTypeId({},{})", user_id, servTypeId);
	//
	// StringBuilder userNames = new StringBuilder();
	//
	// // 用户ID
	// long userId = StringUtil.getLongValue(user_id);
	//
	// // 用户的业务信息
	// HgwServUserObj[] arrHgwServUserObj = servUserDao
	// .queryHgwcustServUserByDevId(userId,servTypeId);
	//
	// if (null == arrHgwServUserObj) {
	// // 未查询到终端对应用户的业务信息
	// logger.warn("终端对应的用户未受理该业务，无法进行业务下发");
	// return "errorMsg:终端对应的用户未受理该业务，无法进行业务下发";
	// } else {
	// // 有对应的业务信息
	// // 遍历业务信息
	// for (HgwServUserObj hgwServUserObj : arrHgwServUserObj)
	// {
	// if(!userNames.toString().equals(""))
	// {
	// userNames.append("#");
	// }
	// userNames.append(hgwServUserObj.getUsername());
	// }
	// }
	//
	// return userNames.toString();
	// }

	

}
