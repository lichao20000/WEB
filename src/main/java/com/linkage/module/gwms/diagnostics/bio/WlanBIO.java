package com.linkage.module.gwms.diagnostics.bio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.paramConfig.ParamInfoAct;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.cao.gw.WlanCAO;
import com.linkage.module.gwms.dao.gw.WlanDAO;
import com.linkage.module.gwms.diagnostics.bio.interf.I_AdvanceSearchBIO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.gw.WlanOBJ;
import com.linkage.module.gwms.obj.interf.I_DevConfOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.strategy.StrategyXml;

/**
 * BIO: advance search.MWBAN.
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Jun 16, 2009
 * @see
 * @since 1.0
 */
@SuppressWarnings({"rawtypes"})
public class WlanBIO implements I_AdvanceSearchBIO 
{
	private static Logger logger = LoggerFactory.getLogger(WlanBIO.class);

	/** type of SG */
	private final int sgType = 12;
	private WlanDAO wlanDAO;
	private WlanCAO wlanCAO;
	/** 结果 */
	private int resultId = -9;
	/** 结果描述 */
	private String result;
	/**华为厂商*/
	public static final String VENDORID_HW="2";
	/**中兴厂商*/
	public static final String VENDORID_ZX="10";
	/**运营商*/
	public static String instArea=Global.instAreaShortName;
	/**db为直接查表，否则直接采集*/
	private String queryFrom="";
	
	/**
	 * get data
	 */
	public List<WlanOBJ> getData(String deviceId) 
	{
		List<WlanOBJ> list = null;

		if (deviceId == null) {
			logger.warn("getData deviceId is null");
			resultId = -9;
			return list;
		}

		// SG
		if(Global.XJDX.equals(instArea))
		{
			resultId=1;
			if(!"db".equals(queryFrom)){
				if (null==new ParamInfoAct().getWlanObj(deviceId)) {
					logger.warn("getData sg fail");
					resultId = -9;
					return list;
				}
			}
		}else{
			if ((resultId = wlanCAO.getDataFromSG(deviceId, sgType)) != 1) {
				logger.warn("getData sg fail");
				return list;
			}
		}

		// db
		List dbList = wlanDAO.getData(deviceId);
		if (dbList == null || dbList.size() == 0) {
			logger.warn("dbList == null");
			resultId = 0;
			return list;
		}

		list = new ArrayList<WlanOBJ>();
		Map map = null;
		for (int i = 0; i < dbList.size(); i++) 
		{
			map = (Map) dbList.get(i);
			if (map != null){
				list.add(new WlanOBJ(map));
			}
		}

		return list;
	}

	/**
	 * config device.
	 */
	public String configDev(I_DevConfOBJ obj, long accOId, int type,
			int serviceId, String gw_type) 
	{
		String flag = "true";

		WlanOBJ wlanOBJ = (WlanOBJ) obj;
		resultId = wlanCAO.configWlan(wlanOBJ);
		logger.warn("[{}] configDev() acs-resultId:{}",wlanOBJ.getDeviceId(),resultId);
		if (resultId != 0 && resultId != 1) {
			flag = "true";
		} else {
			flag = "false";
			if(Global.XJDX.equals(instArea)){
				flag=wlanDAO.configWlan(wlanOBJ);
			}
		}

		wlanOBJ = null;
		return flag;
	}

	/**
	 * 新增
	 */
	public String add(WlanOBJ wlanOBJ) 
	{
		String flag = "true";

		resultId = wlanCAO.addSsid(wlanOBJ);
		logger.warn("[{}] add() acs-resultId:{}",wlanOBJ.getDeviceId(),resultId);
		if (resultId != 0 && resultId != 1) {
			flag = "true";
		} else {
			flag = "false";
			if(Global.XJDX.equals(instArea)){
				resultId=wlanDAO.add(wlanOBJ);
			}
		}

		wlanOBJ = null;
		return flag;
	}

	/**
	 * 编辑
	 */
	public String edit(WlanOBJ wlanOBJ) 
	{
		String flag = "true";

		wlanOBJ.setEnable("1");
		wlanOBJ.setRadioEnable("1");
		if(!Global.XJDX.equals(instArea))
		{
			if ("None".equals(wlanOBJ.getBeacontype())) {
				wlanOBJ.setBasicAuthMode("OpenSystem");
			} else if ("Basic".equals(wlanOBJ.getBeacontype())) {
				wlanOBJ.setBasicAuthMode("Both");
				wlanOBJ.setWepEncrLevel("40-bit");
			} else if ("WPA".equals(wlanOBJ.getBeacontype())) {
				wlanOBJ.setWpaAuthMode("PSKAuthentication");
				wlanOBJ.setWpaEncrMode("TKIPEncryption");
			} else if ("11i".equals(wlanOBJ.getBeacontype())) {
				wlanOBJ.setIeeeAuthMode("PSKAuthentication");
				wlanOBJ.setIeeeEncrMode("TKIPEncryption");
			}
		}
		resultId = wlanCAO.editSsid(wlanOBJ);
		logger.warn("[{}] edit() acs-resultId:{}",wlanOBJ.getDeviceId(),resultId);
		if (resultId != 0 && resultId != 1) {
			flag = "true";
		} else {
			flag = "false";
			if(Global.XJDX.equals(instArea)){
				resultId=wlanDAO.edit(wlanOBJ);
			}
		}

		wlanOBJ = null;
		return flag;
	}

	/**
	 * 删除
	 */
	public String del(WlanOBJ wlanOBJ) 
	{
		String flag = "true";

		resultId = wlanCAO.delSsid(wlanOBJ);
		logger.warn("[{}] del() acs-resultId:{}",wlanOBJ.getDeviceId(),resultId);
		if (resultId != 0 && resultId != 1) {
			flag = "true";
		} else {
			flag = "false";
			if(Global.XJDX.equals(instArea)){
				flag=wlanDAO.delWlan(wlanOBJ);
			}
		}

		wlanOBJ = null;
		return flag;
	}

	/**
	 * get:结果描述
	 */
	public String getResult() 
	{
		result = Global.G_Fault_Map.get(resultId).getFaultReason();
		if (result == null) {
			result = Global.G_Fault_Map.get(100000).getFaultReason();
		}

		return result;
	}
	
	/**
	 * BBMS配置WLAN, 返回策略下发结果"1|调用后台成功|strategyId" or "-1|失败原因"
	 * accOId 用户ID, wlanOBJ 对象, addOrEdit (0:新增 1:编辑)
	 */
	public String configWlan(long accOId, WlanOBJ wlanOBJ, int addOrEdit, String gw_type)
	{
		logger.debug("configWlan({}, {}, {})", new Object[]{accOId, wlanOBJ, addOrEdit});
		/** 入策略表，调预读 */
		// 立即执行
		int strategyType = 0;
		// WLAN配置的service_id
		int wlanServiceId = ConstantClass.WLAN;
		//获取WLAN的策略XML
		String strategyXmlParam = StrategyXml.wlan2Xml(wlanOBJ, addOrEdit);
		StrategyOBJ strategyObj = new StrategyOBJ();
		// 策略ID
		strategyObj.createId();
		// 策略配置时间
		strategyObj.setTime(TimeUtil.getCurrentTime());
		// 用户id
		strategyObj.setAccOid(accOId);
		// 立即执行
		strategyObj.setType(strategyType);
		// 设备ID
		strategyObj.setDeviceId(wlanOBJ.getDeviceId());
		
		// QOS serviceId
		strategyObj.setServiceId(wlanServiceId);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		// 参数
		strategyObj.setSheetPara(strategyXmlParam);
		strategyObj.setTempId(wlanServiceId);
		strategyObj.setIsLastOne(1);
		// 入策略表
		if (wlanDAO.addStrategy(strategyObj)) {
			// 调用预读
			if (CreateObjectFactory.createPreProcess(gw_type)
					.processOOBatch(String.valueOf(strategyObj.getId()))) {
				return 1 + "|调用后台成功|" + strategyObj.getId();
			} else {
				logger.warn("调用预读失败");
				return -1 + "|调用后台失败";
			}
		} else {
			logger.warn("策略入库失败");
			return -1 + "|策略入库失败";
		}
	}
	
	/**
	 * BBMS配置WLAN,添加SSID
	 */
	public String addWlan(long accOId, WlanOBJ wlanOBJ, String gw_type) 
	{
		logger.debug("addWlan({}, {})", accOId, wlanOBJ);
		return configWlan(accOId, wlanOBJ, 0, gw_type);
	}
	
	
	/**
	 * BBMS配置WLAN,编辑SSID相关功能
	 */
	public String editWlan(long accOId, WlanOBJ wlanOBJ, String gw_type) 
	{
		logger.debug("editWlan({}, {})", accOId, wlanOBJ);
		return configWlan(accOId, wlanOBJ, 1, gw_type);
	}

	public void setWlanDAO(WlanDAO wlanDAO) {
		this.wlanDAO = wlanDAO;
	}

	public void setWlanCAO(WlanCAO wlanCAO) {
		this.wlanCAO = wlanCAO;
	}

	public String getQueryFrom() {
		return queryFrom;
	}

	public void setQueryFrom(String queryFrom) {
		this.queryFrom = queryFrom;
	}


}
