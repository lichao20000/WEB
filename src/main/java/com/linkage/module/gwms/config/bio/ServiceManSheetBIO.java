package com.linkage.module.gwms.config.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.HgwCustDAO;
import com.linkage.module.gwms.dao.tabquery.HgwServUserDAO;
import com.linkage.module.gwms.dao.tabquery.OfficeDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.gw.VoiceServiceProfileObj;
import com.linkage.module.gwms.obj.tabquery.HgwCustObj;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.strategy.StrategyXml;

/**
 * @author Jason(3412)
 * @date 2009-9-22
 */
public class ServiceManSheetBIO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(ServiceManSheetBIO.class);

	// 用户DAO
	HgwCustDAO custDao;

	// 业务用户DAO
	HgwServUserDAO servUserDao;

	/**
	 * 根据用户账号或设备序列号或者两者获取用户对象,不对用户账号和设备序列号判空
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-22
	 * @return HgwCustObj
	 */
	public HgwCustObj[] getCustObj(String username, String devSn, String gw_type) {
		logger.debug("getCustObj({},{})", username, devSn);
		custDao.setGw_type(gw_type);
		HgwCustObj[] custObjArr = custDao.getCustObjArrInfo(username, devSn);
		return custObjArr;
	}

	/**
	 * 根据用户ID和业务类型获取业务用户对象
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-22
	 * @return HgwServUserObj
	 */
	public HgwServUserObj getServUserObj(String userId, String servTypeId, String gw_type) {
		logger.debug("getServUserObj({},{})", userId, servTypeId);
		servUserDao.setGw_type(gw_type);
		HgwServUserObj servUserObj = servUserDao
				.getUserInfo(userId, servTypeId);
		return servUserObj;
	}

	/**
	 * @Deprecated
	 * 该方法已不再使用，被子类覆盖
	 * 
	 * 上网开户
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-23
	 * @return int
	 * 1:成功  -1:策略信息失败或传入参数为空  -2:业务用户信息失败  -4:调用后台预读模块失败
	 * 
	 */
	public int netOpen(HgwServUserObj hgwServUserObj, String hasServUser, StrategyOBJ strategyObj, String gw_type) {
		logger.debug("netOpen({},{},{},{},{})", new Object[]{hgwServUserObj,hasServUser,strategyObj});
		if (null == hgwServUserObj || null == strategyObj) {
			logger.warn("hgwServUserObj is null or strategyObj is null");
			return -2;
		}
		if ("-1".equals(hasServUser)) {
			// 添加到业务用户表
			if(servUserDao.saveServUser(hgwServUserObj) < 1){
				logger.warn("入业务用户表失败");
				return -2;
			}
		} else {
			// 更新业务用户表
			if(servUserDao.updateServUser(hgwServUserObj) < 1){
				logger.warn("更新业务用户信息失败");
				return -2;
			}
		}
		//旧的工单参数方式
		//String strategyParam = StrategyXml.getSheetParam(hgwServUserObj);
		//改用新模板
		String strategyParam = StrategyXml.ServUserObj2Xml(hgwServUserObj);
		strategyObj.setSheetPara(strategyParam);

		if(servUserDao.addStrategy(strategyObj)){
			if(CreateObjectFactory.createPreProcess(gw_type).processOOBatch(StringUtil.getStringValue(strategyObj.getId()))){
				logger.debug("业务工单成功");
				return 1;
			}else{
				logger.warn("调用后台预读模块失败");
				return -4;
			}
		}else{
			logger.warn("入策略失败");
			return -1;
		}
	}

	/**
	 * @Deprecated
	 * 该方法已不再使用，被子类覆盖
	 * 
	 * IPTV开户(不支持了)
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-23
	 * @return void
	 */
	public int iptvOpen(HgwServUserObj hgwServUserObj) {
		logger.debug("iptvOpen()");
		//
		return -1;
	}

	/**
	 * @Deprecated
	 * 该方法已不再使用，被子类覆盖
	 * 
	 * VOIP开户
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-9-23
	 * @return int
	 *1:成功  -1:策略信息失败或传入参数为空  -2:业务用户信息失败  -3:局向信息获取不到相应的VOIP服务器地址  -4:调用后台预读模块失败
	 */
	public int voipOpen(HgwServUserObj hgwServUserObj, String officeId,
			String hasServUser, StrategyOBJ strategyObj, String gw_type) {
		logger.debug("voipOpen({},{},{},{})", new Object[]{hgwServUserObj,officeId,hasServUser,strategyObj});
		if (null == hgwServUserObj || null == strategyObj) {
			logger.warn("hgwServUserObj is null or strategyObj is null");
			return -1;
		}
		if ("-1".equals(hasServUser)) {
			// 添加到业务用户表
			if(servUserDao.saveServUser(hgwServUserObj) < 1){
				logger.warn("入业务用户表失败");
				return -2;
			}
		} else {
			// 更新业务用户表
			if(servUserDao.updateServUser(hgwServUserObj) < 1){
				logger.warn("更新业务用户信息失败");
				return -2;
			}
		}
		logger.debug("office_id:{}",officeId);
		VoiceServiceProfileObj voipProfObj = OfficeDAO.getInstance()
				.getOfficeIdVoip().get(officeId);
		if(null == voipProfObj){
			logger.debug("officeId ({})没有对应的服务器信息", officeId);
			return -3;
		}
		// 策略参数
		String strategyParam = StrategyXml.voipObj2Xml(hgwServUserObj,
				voipProfObj);
		strategyObj.setSheetPara(strategyParam);
		
		if(servUserDao.addStrategy(strategyObj)){
			if(CreateObjectFactory.createPreProcess(gw_type).processOOBatch(StringUtil.getStringValue(strategyObj.getId()))){
				logger.debug("业务工单成功");
				return 1;
			}else{
				logger.warn("调用后台预读模块失败");
				return -4;
			}
		}else{
			logger.warn("入策略失败");
			return -1;
		}
	}

	public void setServUserDao(HgwServUserDAO servUserDao) {
		this.servUserDao = servUserDao;
	}

	public void setCustDao(HgwCustDAO custDao) {
		this.custDao = custDao;
	}
}
