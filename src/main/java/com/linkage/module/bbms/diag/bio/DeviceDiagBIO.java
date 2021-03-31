package com.linkage.module.bbms.diag.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.cao.gw.PingCAO;
import com.linkage.module.gwms.dao.gw.LanEthDAO;
import com.linkage.module.gwms.dao.gw.WanDAO;
import com.linkage.module.gwms.dao.gw.WlanDAO;
import com.linkage.module.gwms.diagnostics.bio.ComputeUtil;
import com.linkage.module.gwms.diagnostics.dao.DeviceDiagnosticDAO;
import com.linkage.module.gwms.diagnostics.dao.HealthInfoDAO;
import com.linkage.module.gwms.diagnostics.obj.DiagResult;
import com.linkage.module.gwms.obj.gw.DeviceWireInfoObj;
import com.linkage.module.gwms.obj.gw.HealthLanWlanOBJ;
import com.linkage.module.gwms.obj.gw.HealthWanDslOBJ;
import com.linkage.module.gwms.obj.gw.LanEthObj;
import com.linkage.module.gwms.obj.gw.LanHostObj;
import com.linkage.module.gwms.obj.gw.PingObject;
import com.linkage.module.gwms.obj.gw.WanConnObj;
import com.linkage.module.gwms.obj.gw.WanConnSessObj;
import com.linkage.module.gwms.obj.gw.WlanOBJ;
import com.linkage.module.gwms.obj.tabquery.HgwServUserObj;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;
import com.linkage.module.gwms.util.service.WanTypeUtil;

/**
 * @author Jason(3412)
 * @date 2009-6-15
 */
public class DeviceDiagBIO {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(DeviceDiagBIO.class);

	private DeviceDiagnosticDAO diagDao;
	//LAN信息DAO
	private LanEthDAO lanEthDao;
	//WLAN信息DAO
	private WlanDAO wlanDao;
	//健康库使用DAO
	private HealthInfoDAO healthInfoDao;
	
	private WanDAO wanDao;
	
	// 如果诊断未通过,则写明诊断的情况,解决的建议
	public static final String Diag_Fault = "passMessage";
	public static final String Diag_Suggest = "passSuggest";

//	static{
//		ComputeUtil.initExpertMap();
//	}
	
	/**
	 * 初始化ping检测的页面参数：(地址，时延，包大小，次数)
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-8-12
	 * @return Map
	 */
	public Map initPingParam(){
		logger.debug("initPingParam()");
		return diagDao.initPingParam();
	}
	
	
	/**
	 * 获取设备线路信息(调用采集模块,成功后从数据库中获取)
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-24
	 * @return DeviceWireInfoObj
	 */
	public DiagResult getWireInfo(String deviceId) {
		logger.debug("getWanDeviceWireInfo(deviceId):" + deviceId);
		DeviceWireInfoObj[] arrayObj = null;
		DiagResult diagResult = new DiagResult();
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(deviceId);
		// 调用采集模块
		int iresult = new SuperGatherCorba(gw_type).getCpeParams(deviceId, ConstantClass.GATHER_WAN);
		//int iresult = 1;
		// 成功
		if (1 == iresult) {
			logger.debug("采集线路信息成功");
			// 从数据库中取
			arrayObj = diagDao.queryDevWireInfo(deviceId);
			if (null != arrayObj && arrayObj.length > 0) {
				logger.debug("从数据库中获取到线路信息");
				int len = arrayObj.length;
				// 获取健康库中的线路信息值
				HealthWanDslOBJ healthWireInfoObj = null;
				HealthWanDslOBJ[] healthWireInfoArr = healthInfoDao.getDslHealthInfo(deviceId);
				if(null != healthWireInfoArr && healthWireInfoArr.length > 0){
					healthWireInfoObj = healthWireInfoArr[0];
				}else{
					healthWireInfoObj = new HealthWanDslOBJ();
				}
				String[] diagArr = null;
				for (int i = 0; i < len; i++) {
					if (null != arrayObj[i]) {
						Map<String, String> rMap = new HashMap<String, String>();
						rMap.put(Diag_Fault,"");
						rMap.put(Diag_Suggest,"");
						// 线路状态健康判断
						diagArr = ComputeUtil.getSuggest("wireStatus", arrayObj[i]
								.getWireStatus(), ComputeUtil.WIRESTATUS);
						//判断是否有故障描述,没有故障描述表示正常
						if (StringUtil.IsEmpty(diagArr[0])) {
							logger.debug("线路状态正常,或没有对应的标准值");
							rMap.put("wireStatus", arrayObj[i].getWireStatus());
						} else {
							logger.debug("线路状态为非标准值");
							// 线路状态和健康值不匹配
							rMap.put("wireStatus", getErrMeg(arrayObj[i]
									.getWireStatus()));
							// 置为诊断出异常
							diagResult.setPass(-3);
							rMap.put(Diag_Fault,rMap.get(Diag_Fault) + diagArr[0] + ";");
							rMap.put(Diag_Suggest,rMap.get(Diag_Suggest) + diagArr[1] + ";");
						}
						
						// 线路协议健康判断
						if(StringUtil.IsEmpty(healthWireInfoObj.getModType())){
							logger.debug("线路协议没有标准值");
							rMap.put("modulationType", arrayObj[i].getModulationType());
						}else{
							diagArr = ComputeUtil.getSuggest("wireType", arrayObj[i]
    						         .getModulationType(), healthWireInfoObj.getModType());
							//判断是否有故障描述,没有故障描述表示正常
							if (StringUtil.IsEmpty(diagArr[0])) {
    							logger.debug("线路协议为标准值");
    							rMap.put("modulationType", arrayObj[i]
    									.getModulationType());
    						} else {
    							logger.debug("线路协议为非标准值");
    							// 线路协议和健康值不匹配
    							rMap.put("modulationType", getErrMeg(arrayObj[i]
    									.getModulationType()));
    							// 置为诊断出异常
    							diagResult.setPass(-3);
    							rMap.put(Diag_Fault,rMap.get(Diag_Fault) + diagArr[0] + ";");
    							rMap.put(Diag_Suggest,rMap.get(Diag_Suggest) + diagArr[1] + ";");
    						}
						}
						
						// 上行速率健康判断
						if(0 == healthWireInfoObj.getUpMaxRateMin() ||
								0 == healthWireInfoObj.getUpMaxRateMax()){
							logger.debug("上行速率没有标准值");
							rMap.put("upRate", StringUtil.getStringValue(arrayObj[i]
							              		.getUpstreamMaxRate()));
						}else{
							diagArr = ComputeUtil.getSuggest("wireUpRate", arrayObj[i].getUpstreamMaxRate(), 
									healthWireInfoObj.getUpMaxRateMin(), 
									healthWireInfoObj.getUpMaxRateMax());
							//判断是否有故障描述,没有故障描述表示正常
							if (StringUtil.IsEmpty(diagArr[0])) {
								logger.debug("上行速率在健康范围内");
								rMap.put("upRate", StringUtil.getStringValue(arrayObj[i]
										.getUpstreamMaxRate()));
							} else {
								logger.debug("上行速率在健康范围之外");
								// 线路协议和健康值不匹配
								rMap.put("upRate", getErrMeg(StringUtil.getStringValue(arrayObj[i]
										.getUpstreamMaxRate())));
								// 置为诊断出异常
								diagResult.setPass(-3);
								rMap.put(Diag_Fault,rMap.get(Diag_Fault) + diagArr[0] + ";");
								rMap.put(Diag_Suggest,rMap.get(Diag_Suggest) + diagArr[1] + ";");
							}
						}
						
						// 下行速率健康判断
						if(0 == healthWireInfoObj.getDownMaxRateMin() ||
								0 == healthWireInfoObj.getDownMaxRateMax()){
							logger.debug("下行速率没有标准值");
							rMap.put("downRate", StringUtil.getStringValue(arrayObj[i]
							              		.getDownstreamMaxRate()));
						}else{
							diagArr = ComputeUtil.getSuggest("wireDownRate", arrayObj[i].getDownstreamMaxRate(), 
									healthWireInfoObj.getDownMaxRateMin(), 
									healthWireInfoObj.getDownMaxRateMax());
							if (StringUtil.IsEmpty(diagArr[0])) {
								logger.debug("下行速率在健康范围内");
								rMap.put("downRate", StringUtil.getStringValue(arrayObj[i]
										.getDownstreamMaxRate()));
							} else {
								logger.debug("下行速率在健康范围之外");
								// 线路协议和健康值不匹配
								rMap.put("downRate", getErrMeg(StringUtil.getStringValue(arrayObj[i]
										.getDownstreamMaxRate())));
								// 置为诊断出异常
								diagResult.setPass(-3);
								rMap.put(Diag_Fault,rMap.get(Diag_Fault) + diagArr[0] + ";");
								rMap.put(Diag_Suggest,rMap.get(Diag_Suggest) + diagArr[1] + ";");
							}
						}
						
						// 上行线路衰减表示为0.1dB健康判断
						if(0 == healthWireInfoObj.getUpAttenMin() ||
								0 == healthWireInfoObj.getUpAttenMax()){
							logger.debug("上行衰减没有标准值");
							rMap.put("upAttenuation", StringUtil.getStringValue(arrayObj[i]
							              		.getUpstreamAttenuation()));
						}else{
							diagArr = ComputeUtil.getSuggest("wireUpAtten", arrayObj[i].getUpstreamAttenuation(), 
									healthWireInfoObj.getUpAttenMin(), 
									healthWireInfoObj.getUpAttenMax());
							if (StringUtil.IsEmpty(diagArr[0])) {
								logger.debug("上行衰减在健康范围内");
								rMap.put("upAttenuation", StringUtil.getStringValue(arrayObj[i]
										.getUpstreamAttenuation()));
							} else {
								logger.debug("上行衰减在健康范围之外");
								// 线路协议和健康值不匹配
								rMap.put("upAttenuation", getErrMeg(StringUtil.getStringValue(arrayObj[i]
										.getUpstreamAttenuation())));
								// 置为诊断出异常
								diagResult.setPass(-3);
								rMap.put(Diag_Fault,rMap.get(Diag_Fault) + diagArr[0] + ";");
								rMap.put(Diag_Suggest,rMap.get(Diag_Suggest) + diagArr[1] + ";");
							}
						}
						
						// 下行线路衰减表示为0.1dB健康判断
						if(0 == healthWireInfoObj.getDownAttenMin() ||
								0 == healthWireInfoObj.getDownAttenMax()){
							logger.debug("下行衰减没有标准值");
							rMap.put("downAttenuation", StringUtil.getStringValue(arrayObj[i]
							              		.getDownstreamAttenuation()));
						}else{
							diagArr = ComputeUtil.getSuggest("wireUpAtten", arrayObj[i].getDownstreamAttenuation(), 
									healthWireInfoObj.getDownAttenMin(), 
									healthWireInfoObj.getDownAttenMax());
							if (StringUtil.IsEmpty(diagArr[0])) {
								logger.debug("下行衰减在健康范围内");
								rMap.put("downAttenuation", StringUtil.getStringValue(arrayObj[i]
										.getDownstreamAttenuation()));
							} else {
								logger.debug("下行衰减在健康范围之外");
								// 线路协议和健康值不匹配
								rMap.put("downAttenuation", getErrMeg(StringUtil.getStringValue(arrayObj[i]
										.getDownstreamAttenuation())));
								// 置为诊断出异常
								diagResult.setPass(-3);
								rMap.put(Diag_Fault,rMap.get(Diag_Fault) + diagArr[0] + ";");
								rMap.put(Diag_Suggest,rMap.get(Diag_Suggest) + diagArr[1] + ";");
							}
						}
						logger.debug("线路信息完成...");
						diagResult.getRList().add(rMap);
					} else {
						logger.warn("DeviceWireInfoObj[]" + i + " is null");
					}
				}
			} else {
				logger.warn("采集成功,获取线路信息参数失败或者设备不存在线路信息");
				diagResult.setPass(-1);
				diagResult.setFaultDesc(ComputeUtil.diagDescMap.get("30")[0]);
				diagResult.setSuggest(ComputeUtil.diagDescMap.get("30")[1]);
			}
		} else {
			logger.warn(getErrMeg(iresult));
			logger.warn("采集线路信息失败");
			diagResult.setPass(-2);
			diagResult.setFailture(getErrMeg(iresult));
		}
		return diagResult;
	}

	
	/**
	 * 检查业务参数
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return String
	 */
	public DiagResult servParamCheck(String deviceId, String userId,
			String servTypeId) {
		logger.debug("servParamCheck({}, {}, {}):", new Object[]{deviceId ,userId, servTypeId});
		DiagResult diagResult = new DiagResult();
		HgwServUserObj hgwServUserObj = null;
//		HgwServUserObj hgwServUserObj = diagDao.getUserInfo(userId, servTypeId);
//		String pvcVlan = null;
//		if(null == hgwServUserObj){
//			logger.warn("...获取PVC或VLAN值失败");
//			diagResult.setPass(-1);
//			//80 用户无业务
//			diagResult.setFaultDesc(ComputeUtil.diagDescMap.get("80")[0]);
//			diagResult.setSuggest(ComputeUtil.diagDescMap.get("80")[1]);
//		}else{ //设备有对应的业务用户
//			logger.warn("从业务用户表，获取PVC信息");
//			int accessType = WanTypeUtil.getAccessType(StringUtil
//					.getIntegerValue(hgwServUserObj.getWanType()));
//			boolean userIsAdsl = false;
//			if(-1 == accessType){
//				logger.warn("未知接入方式");
//				pvcVlan = null;
//			}else if (ConstantClass.ACCESS_ADSL == accessType){
//				// ADSL上行
//				userIsAdsl = true;
//				pvcVlan = hgwServUserObj.getVpiid() + "/" + hgwServUserObj.getVciid();
//			}else{
//				//LAN或者EPON
//				pvcVlan = hgwServUserObj.getVlanid();
//			}
			

		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(deviceId);
			// 调用采集模块
			int iresult = new SuperGatherCorba(gw_type).getCpeParams(deviceId, 2);
			logger.debug("servParamCheck iresult:" + iresult);
			if (1 == iresult) {
				List<WanConnSessObj> paramList = getWanConnSessObjList(deviceId);
				Map<String, String> tMap = null;
				if (null == paramList || paramList.size() <= 0) {
					logger.warn("设备上不存在WANConnectSession结点");
					diagResult.setPass(-1);
					//100 设备上未配置业务PVC
					diagResult.setFaultDesc(ComputeUtil.diagDescMap.get("100")[0]);
					diagResult.setSuggest(ComputeUtil.diagDescMap.get("100")[1]);
				} else {
					//判断设备是ADSL上行还是其他方式上行
					boolean devIsAdsl = wanDao.isADSL(deviceId);
					int psize = paramList.size();
					String[] diagArr = null;
					for (int i = 0; i < psize; i++) {
						WanConnSessObj sessObj = paramList.get(i);
						if (null != sessObj) {
							tMap = new HashMap<String, String>();
							tMap.put(Diag_Fault,"");
							tMap.put(Diag_Suggest,"");
							// 健康判断，PVC的值为主键值，一定存在且正确，所以不需要做健康判断
							if(true == devIsAdsl){
								tMap.put("pvc", sessObj.getPvc());
							}else{
								tMap.put("pvc", sessObj.getVlanid());
							}
							//检查是否有业务用户，健康值来自业务用户表
							
							
							//暂时写死，不需要做健康判断
							boolean hasUser = false;
							if(true == hasUser){
								logger.debug("设备有对应的业务用户，对其进行健康判断");
								//业务账号健康判断
								if(StringUtil.IsEmpty(hgwServUserObj.getUsername())){
									logger.debug("用户账号没有标准值");
									tMap.put("username", sessObj.getUsername());
								}else{
									diagArr = ComputeUtil.getSuggest("username", sessObj.getUsername(), 
											hgwServUserObj.getUsername());
									if (StringUtil.IsEmpty(diagArr[0])) {
										tMap.put("username", sessObj.getUsername());
									} else {
										//用户账号和健康值不匹配
										tMap.put("username", getErrMeg(sessObj.getUsername()));
										// 置为诊断出异常
										diagResult.setPass(-3);
										tMap.put(Diag_Fault,tMap.get(Diag_Fault) + diagArr[0] + ";");
										tMap.put(Diag_Suggest,tMap.get(Diag_Suggest) + diagArr[1] + ";");
									}
								}

								//DNS健康判断
								if(StringUtil.IsEmpty(hgwServUserObj.getDns())){
									logger.debug("DNS没有标准值");
									tMap.put("dns", sessObj.getDns());
								}else{
									diagArr = ComputeUtil.getSuggest("dns", sessObj.getDns(), 
											hgwServUserObj.getDns());
									if (StringUtil.IsEmpty(diagArr[0])) {
										tMap.put("dns", sessObj.getDns());
									} else {
										// DNS和健康值不匹配
										tMap.put("dns", getErrMeg(sessObj.getDns()));
										// 置为诊断出异常
										diagResult.setPass(-3);
										tMap.put(Diag_Fault,tMap.get(Diag_Fault) + diagArr[0] + ";");
										tMap.put(Diag_Suggest,tMap.get(Diag_Suggest) + diagArr[1] + ";");
									}
								}
							}else{
								logger.debug("设备没有对应的业务用户，无法做健康判断直接返回从设备上采集到的值");
								tMap.put("username", sessObj.getUsername());
								tMap.put("dns", sessObj.getDns());
							}

							//NAT开关健康判断
							diagArr = ComputeUtil.getSuggest("nat", sessObj.getNatEnable(), ComputeUtil.NAT);
							if (StringUtil.IsEmpty(diagArr[0])) {
								tMap.put("nat", getNatDesc(sessObj.getNatEnable()));
							} else {
								tMap.put("nat", getErrMeg(getNatDesc(sessObj.getNatEnable())));
								// 置为诊断出异常
								diagResult.setPass(-3);
								tMap.put(Diag_Fault,tMap.get(Diag_Fault) + diagArr[0] + ";");
								tMap.put(Diag_Suggest,tMap.get(Diag_Suggest) + diagArr[1] + ";");
							}

							//连接状态健康判断
							diagArr = ComputeUtil.getSuggest("wanInternetStatus", sessObj.getStatus(), ComputeUtil.WANSTATUS);
							if (StringUtil.IsEmpty(diagArr[0])) {
								tMap.put("status", sessObj.getStatus());
							} else {
								// 线路协议和健康值不匹配
								tMap.put("status", getErrMeg(sessObj.getStatus()));
								// 置为诊断出异常
								diagResult.setPass(-3);
								tMap.put(Diag_Fault,tMap.get(Diag_Fault) + diagArr[0] + ";");
								tMap.put(Diag_Suggest,tMap.get(Diag_Suggest) + diagArr[1] + ";");
							}
							
							//连接类型
							tMap.put("connType", Global.G_Src_Key_Map.get("1").get(sessObj.getConnType()));
							//拨号错误码健康判断
							diagArr = ComputeUtil.getSuggest("lastErr", sessObj.getConnError(), ComputeUtil.LASTERR);
							if (StringUtil.IsEmpty(diagArr[0])) {
								tMap.put("connErr", Global.G_Src_Key_Map.get("7").get(sessObj.getConnError()));
							} else {
								// 线路协议和健康值不匹配
								tMap.put("connErr", getErrMeg(Global.G_Src_Key_Map.get("7").get(sessObj.getConnError())));
								// 置为诊断出异常
								diagResult.setPass(-3);
								tMap.put(Diag_Fault,tMap.get(Diag_Fault) + diagArr[0] + ";");
								tMap.put(Diag_Suggest,tMap.get(Diag_Suggest) + diagArr[1] + ";");
							}
							
							diagResult.getRList().add(tMap);
							logger.debug("业务信息检查完成...");
						} else {
							logger.warn("WANConnectSession结点值为null");
						}
					}
				}
			} else {
				logger.warn("获取wan结点下信息失败");
				logger.warn(getErrMeg(iresult));
				diagResult.setPass(-2);
				diagResult.setFailture(getErrMeg(iresult));
			}
		//}
		return diagResult;
	}
	
	/**
	 * 获取设备上所有session对象
	 * 
	 * @param deviceId
	 * @param pvc
	 */
	public List<WanConnSessObj> getWanConnSessObjList(String deviceId) {
		logger.debug("getWanConnSessObjList({})", deviceId);
		List<WanConnSessObj> retList = new ArrayList<WanConnSessObj>();
		// 普通ADSL
		WanConnObj[] wanConnObj = diagDao.queryDevWanConn(deviceId);
		if (null != wanConnObj && wanConnObj.length > 0) {
			int alen = wanConnObj.length;
			for (int i = 0; i < alen; i++) {
				if (null != wanConnObj[i]) {
					String ipvc = wanConnObj[i].getVpi_id() + "/"
							+ wanConnObj[i].getVci_id();
					WanConnSessObj[] sessObj = diagDao
							.queryDevWanConnSession(wanConnObj[i]);
					if (null != sessObj && sessObj.length > 0) {
						int sessLen = sessObj.length;
						for (int j = 0; j < sessLen; j++) {
							if (null != sessObj[j]) {
								retList.add(sessObj[j]);
							}
						}
					}
				}
			}
		}
		return retList;
	}
	
	
	/**
	 * 获取指定PVC结点的信息：session对象
	 * 
	 * @param deviceId
	 * @param pvc
	 */
	public List<WanConnSessObj> getPvcRelationInfo(String deviceId, String[] pvc) {
		logger
				.debug("getPvcRelationInfo(deviceId,pvc):" + deviceId + ","
						+ pvc);
		List<WanConnSessObj> retList = new ArrayList<WanConnSessObj>();
		// 普通ADSL
		WanConnObj[] wanConnObj = diagDao.queryDevWanConn(deviceId);
		if (null != wanConnObj && wanConnObj.length > 0) {
			int alen = wanConnObj.length;
			for (int i = 0; i < alen; i++) {
				if (null != wanConnObj[i]) {
					String ipvc = wanConnObj[i].getVpi_id() + "/"
							+ wanConnObj[i].getVci_id();
					// 判断ipvc是否在pvc数组中
					if (true == contain(pvc, ipvc)) {
						WanConnSessObj[] sessObj = diagDao
								.queryDevWanConnSession(wanConnObj[i]);
						if (null != sessObj && sessObj.length > 0) {
							int sessLen = sessObj.length;
							for (int j = 0; j < sessLen; j++) {
								if (null != sessObj[j]) {
									retList.add(sessObj[j]);
								}
							}
						}
					}
				}
			}
		}
		return retList;
	}

	/**
	 * 获取WANDevice信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-8
	 * @return String
	 */
	public int getWanDevice(String deviceId) {
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(deviceId);
		int iresult = new SuperGatherCorba(gw_type).getCpeParams(deviceId, 2);
		logger.debug("getWanDevice iresult:" + iresult);
		if (1 != iresult) {
			logger.warn("getWanDevice iresult:" + getErrMeg(iresult));
		}
		return iresult;
	}


	/**
	 * ping检测
	 * 
	 * @param deviceId
	 * @param destAddress
	 */
	public DiagResult pingCheck(String deviceId, String userId,
			String servTypeId, String pingAddrType, PingObject pingObj) {
		logger.debug("ping({}, {}, {}, {}, {}):", new Object[]{deviceId, userId, servTypeId, pingAddrType, pingObj});
		DiagResult diagResult = new DiagResult();
		// 获取用户及业务类型，从业务用户表中取出所使用的PVC，根据PVC获取结点实例
		// 取出第一个值作为ping所使用的接口(一般就只能取出一个,为了兼容的取出多个值的情况)
		HgwServUserObj hgwServUserObj = diagDao.getUserInfo(userId, servTypeId);
		String pvcVlan = null;
		//设备有对应的业务用户
		if(null == hgwServUserObj){
			logger.warn("该设备没有对应的业务用户，需要从配置表中获取PVC信息");
		}else{
			logger.warn("从业务用户表，获取PVC信息");
			int accessType = WanTypeUtil.getAccessType(StringUtil
					.getIntegerValue(hgwServUserObj.getWanType()));
			if(-1 == accessType){
				logger.warn("未知接入方式");
				pvcVlan = null;
			}else if (ConstantClass.ACCESS_ADSL == accessType) {
				// ADSL上行
				pvcVlan = hgwServUserObj.getVpiid() + "/" + hgwServUserObj.getVciid();
			}else{
				//LAN或者EPON
				pvcVlan = hgwServUserObj.getVlanid();
			}
		}
		
		
		if (StringUtil.IsEmpty(pvcVlan)) {
			logger.warn("...获取PVC/VLAN值失败");
			//80 用户无业务
			diagResult.setPass(-1);
			diagResult.setFaultDesc(ComputeUtil.diagDescMap.get("80")[0]);
			diagResult.setSuggest(ComputeUtil.diagDescMap.get("80")[1]);
		} else {
			int ir = getWanDevice(deviceId);
			//int ir = 1;
			if (1 == ir) {
				// 根据pvc的值,拼接为端口字符串
				String devInterface = getPingInter(deviceId, pvcVlan);
				logger.debug("devInterface:" + devInterface);
				if (null == devInterface || "".equals(devInterface)) {
					logger.warn("...获取端口值失败");
					//100 设备上无业务PVC
					diagResult.setPass(-1);
					diagResult.setFaultDesc(ComputeUtil.diagDescMap.get("100")[0]);
					diagResult.setSuggest(ComputeUtil.diagDescMap.get("100")[1]);
				} else {
					//PingObject pingObj = diagDao.getPingParam(pingAddrType);
					// set ping 端口
					pingObj.setDevInterface(devInterface);
					logger.debug("ping: " + pingObj);
					// ping操作
					PingCAO pingCAO = new PingCAO();
					pingCAO.setDeviceId(deviceId);
					pingCAO.setPingObj(pingObj);
					// ping
					pingCAO.ping(LipossGlobals.getGw_Type(deviceId));
					Map<String, String> pingMap = new HashMap<String, String>();
					pingMap.put("address", pingObj.getPingAddress());
					pingMap.put("packageSize", String.valueOf(pingObj
							.getPackageSize()));
					pingMap.put("times", String.valueOf(pingObj
							.getNumOfRepetitions()));
					pingMap.put("averageTime", String.valueOf(pingObj.getAverageResponseTime()));
					pingMap.put("minTime", String.valueOf(pingObj.getMinimumResponseTime()));
					pingMap.put("maxTime", String.valueOf(pingObj.getMaximumResponseTime()));
					
					int suc = pingObj.getSuccessCount();
					int fal = pingObj.getFailureCount();
					
					if (true == pingObj.isSuccess()) {
						pingMap.put(Diag_Fault,"");
						pingMap.put(Diag_Suggest,"");
						if(0 == suc){
							pingMap.put("success", getErrMeg(String.valueOf(suc)));
							pingMap.put("failure", getErrMeg(String.valueOf(fal)));
							if("specialDomain".equals(pingAddrType)){
								diagResult.setPass(-3);
								pingMap.put(Diag_Fault,pingMap.get(Diag_Fault) + ComputeUtil.diagDescMap.get("104")[0] + ";");
								pingMap.put(Diag_Suggest,pingMap.get(Diag_Suggest) + ComputeUtil.diagDescMap.get("104")[1] + ";");
							}else if("specialAddress".equals(pingAddrType)){
								diagResult.setPass(-3);
								pingMap.put(Diag_Fault,pingMap.get(Diag_Fault) + ComputeUtil.diagDescMap.get("105")[0] + ";");
								pingMap.put(Diag_Suggest,pingMap.get(Diag_Suggest) + ComputeUtil.diagDescMap.get("105")[1] + ";");
							}else{
								diagResult.setPass(-3);
								pingMap.put(Diag_Fault,pingMap.get(Diag_Fault) + ComputeUtil.diagDescMap.get("103")[0] + ";");
								pingMap.put(Diag_Suggest,pingMap.get(Diag_Suggest) + ComputeUtil.diagDescMap.get("103")[1] + ";");
							}
						}else{
							if(fal >= suc){//丢包严重
								diagResult.setPass(-1);
								pingMap.put("success", getErrMeg(String.valueOf(suc)));
								pingMap.put("failure", getErrMeg(String.valueOf(fal)));
								diagResult.setPass(-3);
								pingMap.put(Diag_Fault,pingMap.get(Diag_Fault) + ComputeUtil.diagDescMap.get("106")[0] + ";");
								pingMap.put(Diag_Suggest,pingMap.get(Diag_Suggest) + ComputeUtil.diagDescMap.get("106")[1] + ";");
							}else{
								pingMap.put("success", String.valueOf(suc));
								pingMap.put("failure", String.valueOf(fal));
							}
						}
						diagResult.getRList().add(pingMap);
					} else {
						diagResult.setPass(-2);
						diagResult.setFailture(getErrMeg(Global.G_Fault_Map.get(pingObj
								.getFaultCode()).getFaultReason()));
					}
				}
			} else {
				logger.warn(getErrMeg(ir));
				diagResult.setPass(-2);
				diagResult.setFailture(getErrMeg(ir));
			}
		}
		return diagResult;
	}


	/**
	 * 有线 lan口设备状态
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return DiagResult
	 */
	public DiagResult lanHostCheck(String deviceId) {
		logger.debug("lanHostCheck(String deviceId)" + deviceId);
		DiagResult diagResult = new DiagResult();
		//LANEthernetInterfaceConfig. 4个LAN口的状态
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(deviceId);
		int iresult = new SuperGatherCorba(gw_type).getCpeParams(deviceId, 11);
		//int iresult = 1;
		logger.debug("LANEthernetInterfaceConfig iresult:" + iresult);
		if (1 == iresult) {
			LanEthObj[] lanEthArr = lanEthDao.getLanEthObjArr(deviceId);
			if (null != lanEthArr && lanEthArr.length > 0) {
				String[] diagArr = null;
				int aLen = lanEthArr.length;
				// 获取每个设备端口的状态
				for (int i = 0; i < aLen; i++) {
					Map<String, String> rMap = new HashMap<String, String>();
					rMap.put(Diag_Fault,"");
					rMap.put(Diag_Suggest,"");
					rMap.put("lanInter", "LAN" + lanEthArr[i].getLanEthid());
					//绑定的端口
					String status = lanEthArr[i].getStatus();
					diagArr = ComputeUtil.getSuggest("lanStatus", status, ComputeUtil.LANSTATUS);
					if(ComputeUtil.LANSTATUS.equals(status)){
						rMap.put("lanStatus", status);
					}else{
						rMap.put("lanStatus", getErrMeg(status));
						// 置为诊断出异常
						diagResult.setPass(-3);
						rMap.put(Diag_Fault,rMap.get(Diag_Fault) + diagArr[0] + ";");
						rMap.put(Diag_Suggest,rMap.get(Diag_Suggest) + diagArr[1] + ";");
					}
					diagResult.getRList().add(rMap);
				}
			} else {
				logger.warn("设备没有LAN侧的信息");
				diagResult.setPass(-1);
				//107 设备没有LAN侧信息
				diagResult.setFaultDesc(ComputeUtil.diagDescMap.get("107")[0]);
				diagResult.setSuggest(ComputeUtil.diagDescMap.get("107")[1]);
			}
		} else {
			logger.warn(getErrMeg(iresult));
			diagResult.setPass(-2);
			diagResult.setFailture(getErrMeg(iresult));
		}
		
		return diagResult;
	}

	
	/**
	 * 设备wlan状态
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return DiagResult
	 */
	public DiagResult wlanHostCheck(String deviceId) {
		logger.debug("wlanHostCheck(String deviceId)" + deviceId);
		DiagResult diagResult = new DiagResult();
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(deviceId);
		//WLANConfiguration. WLAN口的状态
		int iresult = new SuperGatherCorba(gw_type).getCpeParams(deviceId, 12);
		//int iresult = 1;
		logger.debug("WLANConfiguration iresult:" + iresult);
		if (1 == iresult) {
			//SSID1的信息
			WlanOBJ wlanObj = wlanDao.getWlanObj(deviceId, "1", "1");
			if (null != wlanObj) {
				// 获取无线模块的状态
				Map<String, String> rMap = new HashMap<String, String>();
				//获取Wlan的健康值
				HealthLanWlanOBJ healthWlanObj = healthInfoDao.getWlanHealthInfo(deviceId);
				String[] diagArr = null;
				rMap.put(Diag_Fault,"");
				rMap.put(Diag_Suggest,"");
				rMap.put("ssid", wlanObj.getSsid());
				//绑定的端口
				String status = wlanObj.getStatus();
				if(StringUtil.IsEmpty(status)){
					rMap.put("wlanStatus", "未知");
				}else if ("N/A".equals(status)){
					rMap.put("wlanStatus", "未知");
				}else{
					rMap.put("wlanStatus", status);
				}
				//802.11工作模式
				rMap.put("standard", wlanObj.getStandard());
				//关联设备数
				if("0".equals(wlanObj.getAssociatedNum())){
					rMap.put("assoNum", getErrMeg(wlanObj.getAssociatedNum()));
					diagResult.setPass(-3);
					rMap.put(Diag_Fault,rMap.get(Diag_Fault) + ComputeUtil.diagDescMap.get("108")[0] + ";");
					rMap.put(Diag_Suggest,rMap.get(Diag_Suggest) + ComputeUtil.diagDescMap.get("108")[1] + ";");
				}else{
					rMap.put("assoNum", wlanObj.getAssociatedNum());
				}
				
				//模块功率需要做健康判断
				if(null == healthWlanObj || StringUtil.IsEmpty(healthWlanObj.getPowervalue())){
					//没有健康值,不做健康判断
					//当前功率,单位：dBm
					logger.debug("模块功率没有健康值");
					rMap.put("power", wlanObj.getPowervalue());
				}else{
					//健康判断; 这里只有最小值判断,最大值取Integer.MAX_VALUE
					diagArr = ComputeUtil.getSuggest("wlanPowerValue", 
							StringUtil.getDoubleValue(wlanObj.getPowervalue()), 
							StringUtil.getDoubleValue(healthWlanObj.getPowervalue()),Integer.MAX_VALUE);
					if (StringUtil.IsEmpty(diagArr[0])) {
						//正常范围内
						rMap.put("power", wlanObj.getPowervalue());
					} else {
						// 无线模块功率和健康值不匹配
						rMap.put("power", getErrMeg(wlanObj.getPowervalue()));
						// 置为诊断出异常
						diagResult.setPass(-3);
						rMap.put(Diag_Fault,rMap.get(Diag_Fault) + diagArr[0] + ";");
						rMap.put(Diag_Suggest,rMap.get(Diag_Suggest) + diagArr[1] + ";");
					}
				}
				diagResult.getRList().add(rMap);
			} else {
				logger.warn("设备上没有WLAN");
				diagResult.setPass(-1);
				diagResult.setFaultDesc(ComputeUtil.diagDescMap.get("109")[0]);
				diagResult.setSuggest(ComputeUtil.diagDescMap.get("109")[1]);
			}
		} else {
			logger.warn(getErrMeg(iresult));
			diagResult.setPass(-2);
			diagResult.setFailture(getErrMeg(iresult));
		}
		
		return diagResult;
	}
	
	
	/**
	 * dhcp检查，检查lan口的下联设备信息，host结点下面
	 * 
	 * @param diagType 1:有线  2:无线
	 * @author Jason(3412)
	 * @date 2009-7-1
	 * @return String
	 */
	public DiagResult dhcpCheck(String deviceId, int diagType) {
		logger.debug("checkDhcp(String deviceId):" + deviceId);
		DiagResult diagResult = new DiagResult();
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(deviceId);
		//Hosts.
		int iresult = new SuperGatherCorba(gw_type).getCpeParams(deviceId, 13);
		//int iresult = 1;
		logger.debug("dhcpCheck iresult:" + iresult);
		if (1 == iresult) {
			// 获取内容信息
			LanHostObj[] aLanHost = diagDao.queryLanHost(deviceId);
			if (null != aLanHost && aLanHost.length > 0) {
				int aLen = aLanHost.length;
				String[] diagArr = null;
				// 获取每个下联设备的状态
				for (int i = 0; i < aLen; i++) {
					Map<String, String> rMap = new HashMap<String, String>();
					rMap.put(Diag_Fault,"");
					rMap.put(Diag_Suggest,"");
					rMap.put("hostid", String.valueOf(aLanHost[i].getHost_inst()));
					//绑定的端口
					String interf = aLanHost[i].getInterf();
					if(StringUtil.IsEmpty(interf) || "N/A".equals(interf)){
						rMap.put("interf", "未知");
					}else{
						if(interf.contains("WLANConfiguration")){
							rMap.put("interf", "WLAN" + interf.substring(interf.length() - 1));
						}else{
							rMap.put("interf", "LAN" + interf.substring(interf.length() - 1));
						}
					}
					
					//PC状态, 加健康判断
					String active = aLanHost[i].getActive();
					if("N/A".equals(active) || StringUtil.IsEmpty(active)){
						rMap.put("active", "未知");
					}else{
						diagArr = ComputeUtil.getSuggest("lanHost", active, ComputeUtil.HOSTACTIVE);
						if (StringUtil.IsEmpty(diagArr[0])) {
							rMap.put("active", getActive(active));
						} else {
							// active
							rMap.put("active", getErrMeg(getActive(active)));
							// 置为诊断出异常
							diagResult.setPass(-3);
							rMap.put(Diag_Fault,rMap.get(Diag_Fault) + diagArr[0] + ";");
							rMap.put(Diag_Suggest,rMap.get(Diag_Suggest) + diagArr[1] + ";");
						}
					}
					
					rMap.put("hostname", aLanHost[i].getHostname());
					rMap.put("hostIp", aLanHost[i].getIPAddress());
					rMap.put("addrSource", aLanHost[i].getAddressSource());
					if(2 == diagType){
						//只返回WLAN的关联设备
						if(true == rMap.get("interf").contains("WLANConfiguration")){
							diagResult.getRList().add(rMap);
						}
					}else{
						//只返回LAN的关联设备
						if(false == rMap.get("interf").contains("LANEthernetInterfaceConfig")){
							diagResult.getRList().add(rMap);
						}
					}
				}
				if(2 == diagType){
					if(diagResult.getRList().size() == 0){
						logger.warn("lan host无线没有下联设备");
						diagResult.setPass(-1);
						diagResult.setFaultDesc(ComputeUtil.diagDescMap.get("111")[0]);
						diagResult.setSuggest(ComputeUtil.diagDescMap.get("111")[1]);
					}
				}else{
					if(diagResult.getRList().size() == 0){
						logger.warn("lan host有线没有下联设备");
						diagResult.setPass(-1);
						diagResult.setFaultDesc(ComputeUtil.diagDescMap.get("110")[0]);
						diagResult.setSuggest(ComputeUtil.diagDescMap.get("110")[1]);
					}
				}
			} else {
				logger.warn("lan host没有下联设备");
				if(2 == diagType){
					diagResult.setPass(-1);
					diagResult.setFaultDesc(ComputeUtil.diagDescMap.get("111")[0]);
					diagResult.setSuggest(ComputeUtil.diagDescMap.get("111")[1]);
				}else{
					diagResult.setPass(-1);
					diagResult.setFaultDesc(ComputeUtil.diagDescMap.get("110")[0]);
					diagResult.setSuggest(ComputeUtil.diagDescMap.get("110")[1]);
				}
				
			}
		} else {
			logger.warn(getErrMeg(iresult));
			diagResult.setPass(-2);
			diagResult.setFailture(getErrMeg(iresult));
		}
		return diagResult;
	}

	
	/**
	 * 用lan口ping本地主机地址,ping的相关参数从页面传过来
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-1
	 * @return PingObject
	 */
	public DiagResult lanPing(String deviceId, PingObject pingObj){
		DiagResult diagResult = new DiagResult();
		String lanInterface = null;
		logger.debug("InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1");
		lanInterface = "InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.IPInterface.1";
		pingObj.setDevInterface(lanInterface);
		// ping操作
		PingCAO pingCAO = new PingCAO();
		pingCAO.setDeviceId(deviceId);
		pingCAO.setPingObj(pingObj);
		pingCAO.ping(LipossGlobals.getGw_Type(deviceId));
		Map<String, String> pingMap = new HashMap<String, String>();
		pingMap.put("address", pingObj.getPingAddress());
		pingMap.put("packageSize", String.valueOf(pingObj.getPackageSize()));
		pingMap.put("times", String.valueOf(pingObj.getNumOfRepetitions()));
		if (true == pingObj.isSuccess()) {
			int fal = pingObj.getFailureCount();
			int suc = pingObj.getSuccessCount();
			if(0 == suc){	
				pingMap.put("success", getErrMeg(String.valueOf(suc)));
				pingMap.put("failure", getErrMeg(String.valueOf(fal)));
				diagResult.setPass(-3);
				pingMap.put(Diag_Fault,pingMap.get(Diag_Fault) + ComputeUtil.diagDescMap.get("112")[0] + ";");
				pingMap.put(Diag_Suggest,pingMap.get(Diag_Suggest) + ComputeUtil.diagDescMap.get("112")[1] + ";");
			} else if(fal >= suc){	//丢包严重
				diagResult.setPass(-1);
				pingMap.put("success", getErrMeg(String.valueOf(suc)));
				pingMap.put("failure", getErrMeg(String.valueOf(fal)));
				diagResult.setPass(-3);
				pingMap.put(Diag_Fault,pingMap.get(Diag_Fault) + ComputeUtil.diagDescMap.get("106")[0] + ";");
				pingMap.put(Diag_Suggest,pingMap.get(Diag_Suggest) + ComputeUtil.diagDescMap.get("106")[1] + ";");
			}else{
				pingMap.put("success", String.valueOf(suc));
				pingMap.put("failure", String.valueOf(fal));
			}
			pingMap.put("averageTime", String.valueOf(pingObj.getAverageResponseTime()));
			pingMap.put("minTime", String.valueOf(pingObj.getMinimumResponseTime()));
			pingMap.put("maxTime", String.valueOf(pingObj.getMaximumResponseTime()));
			
			diagResult.getRList().add(pingMap);
		} else {
			diagResult.setPass(-2);
			diagResult.setFailture(getErrMeg(Global.G_Fault_Map.get(pingObj
					.getFaultCode()).getFaultReason()));
		}
		return diagResult;
	}
	

	/**
	 * 从配置表中获取业务类型对应的pvc值数组类型
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-1
	 * @return String[]
	 */
	public String[] getServPvc(String servTypeId) {
		logger.debug("getServPvc({})", servTypeId);
		// 获取PVC值从配置表中
		return diagDao.getServPvc(servTypeId);
	}

	
	/**
	 * 根据设备ID获取用户表用户ID
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return String
	 */
	public String getUserId(String deviceId) {
		return diagDao.getUserId(deviceId);
	}

	
	/**
	 * 根据pvc(8/35)获取端口字符串
	 * InternetGatewayDevice.WANDevice.1.WANConnectionDevice.2.WAN###Connection.1
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-27
	 * @return String
	 */
	public String getPingInter(String deviceId, String pvcVlan) {
		String deviceInterface = null;
		if(StringUtil.IsEmpty(pvcVlan) || StringUtil.IsEmpty(deviceId)){
			logger.warn("getPingInter() deviceId is null or pvcVlan is null");
			return null;
		}
		if(true == wanDao.isADSL(deviceId)){
			String vpi = "";
			String vci = "";
			if (2 == pvcVlan.split("/").length) {
				// 根据PVC的值获取实例
				vpi = pvcVlan.split("/")[0];
				vci = pvcVlan.split("/")[1];
				logger.debug("ADSL上行设备的PING端口字符串");
				deviceInterface = diagDao.getPvcInter(deviceId, vpi, vci);
			} else {
				logger.warn("pvc值错误:" + pvcVlan);
			}
		}else{
			logger.debug("LAN上行设备的PING端口字符串");
			deviceInterface = diagDao.getPvcInter(deviceId, pvcVlan);
		}
		return deviceInterface;
	}

	
	/**
	 * 字符串数组arrayStr是否包含字符串str
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return boolean
	 */
	public static boolean contain(String[] arrayStr, String str) {
		if (null != arrayStr && arrayStr.length > 0 && null != str) {
			int len = arrayStr.length;
			for (int i = 0; i < len; i++) {
				if (str.equals(arrayStr[i])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 调用采集模块失败的描述信息
	 * 
	 * @param 采集模块返回值
	 * @author Jason(3412)
	 * @date 2009-7-3
	 * @return String
	 */
	public String getErrMeg(int stat) {
		String errMeg = "<font color='red'>err</font>";
		if (2 == stat) {
			errMeg = errMeg.replace("err", "设备不在线");
		} else if (3 == stat) {
			errMeg = errMeg.replace("err", "设备正在被操作");
		} else if (4 == stat) {
			errMeg = errMeg.replace("err", "采集失败");
		} else {
			errMeg = errMeg.replace("err", "未知错误");
		}
		return errMeg;
	}

	/**
	 * 调用采集模块失败的描述信息
	 * 
	 * @param 错误信息
	 * @author Jason(3412)
	 * @date 2009-7-3
	 * @return String
	 */
	public String getErrMeg(String err) {
		String errMeg = "<font color='red'>" + err + "</font>";
		return errMeg;
	}

	/**
	 * @param bssSheetReportDao
	 *            the bssSheetReportDao to set
	 */
	public void setDiagDao(DeviceDiagnosticDAO diagDao) {
		this.diagDao = diagDao;
	}


	public void setLanEthDao(LanEthDAO lanEthDao) {
		this.lanEthDao = lanEthDao;
	}

	public void setWlanDao(WlanDAO wlanDao) {
		this.wlanDao = wlanDao;
	}

	public void setHealthInfoDao(HealthInfoDAO healthInfoDao) {
		this.healthInfoDao = healthInfoDao;
	}
	
	public void setWanDao(WanDAO wanDao) {
		this.wanDao = wanDao;
	}


	/**
	 * nat开关 结点翻译
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-3
	 * @return String
	 */
	public String getNatDesc(String nat){
		if("1".equals(nat)){
			return "开启";
		}else if ("0".equals(nat)){
			return "关闭";
		}else{
			return "未知";
		}
	}
	
	/**
	 * host active 结点翻译
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-3
	 * @return String
	 */
	public String getActive(String active){
		if("1".equals(active)){
			return "活跃";
		}else if("0".equals(active)){
			return "不活跃";
		}else{
			return "未知";
		}
		
	}

	/**
	 * WLAN诊断
	 * @param deviceId
	 * @return
	 */
	public DiagResult wlanDiagInfo(String deviceId) {

		logger.debug("wlanDiagInfo({})",deviceId);
		DiagResult diagResult = new DiagResult();
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(deviceId);
		//WLANConfiguration. WLAN口的状态
		int iresult = new SuperGatherCorba(gw_type).getCpeParams(deviceId, 12);
		//int iresult = 1;
		logger.debug("WLANConfiguration iresult:" + iresult);
		if (1 == iresult) {
			//SSID1的信息
			List wlanList = wlanDao.getWlanBbms(deviceId, null, null);
			if(null!=wlanList && wlanList.size()>0){
				
				// 获取所有IP
				List<Map> wlanAssoList = wlanDao.getWlanAsso_bbms(deviceId);
				StringBuffer hostIps = null;
				
				for(int i=0;i<wlanList.size();i++){
					Map _tempMap = (Map) wlanList.get(i);
					
					String _tempLanId = String.valueOf(_tempMap.get("lan_id"));
					String _tempLanWlanId = String.valueOf(_tempMap.get("lan_wlan_id"));
					
					hostIps = new StringBuffer();
					if( null != wlanAssoList &&  wlanAssoList.size() != 0)
					{
						for(int j=0; j<wlanAssoList.size(); j++)
						{
							String _lanId = String.valueOf(wlanAssoList.get(j).get("lan_id"));
							String _lanWlanId = String.valueOf(wlanAssoList.get(j).get("lan_wlan_id"));
							
							if(_tempLanId.equals(_lanId) && _tempLanWlanId.equals(_lanWlanId)){
								hostIps.append(StringUtil.getStringValue(wlanAssoList.get(j).get("ip_address"))).append(",");
							}
							
						}
						_tempMap.put("ipaddress", hostIps.substring(0, hostIps.length()-1));
					}
					else
					{
						_tempMap.put("ipaddress", "");
					}
					diagResult.getRList().add(_tempMap);
				}
			}
			else 
			{
				logger.warn("设备上没有WLAN");
				diagResult.setPass(-1);
				diagResult.setFaultDesc(ComputeUtil.diagDescMap.get("109")[0]);
				diagResult.setSuggest(ComputeUtil.diagDescMap.get("109")[1]);
			}
		} else {
			logger.warn(getErrMeg(iresult));
			diagResult.setPass(-2);
			diagResult.setFailture(getErrMeg(iresult));
		}
		
		return diagResult;
	
		
	}

}
