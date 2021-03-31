package com.linkage.module.gwms.config.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.cao.gw.QosCAO;
import com.linkage.module.gwms.cao.gw.interf.IParamTree;
import com.linkage.module.gwms.config.dao.StrategyQosDAO;
import com.linkage.module.gwms.config.obj.StrategyQosParaOBJ;
import com.linkage.module.gwms.dao.gw.QosDAO;
import com.linkage.module.gwms.dao.gw.WlanDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.obj.gw.QoSAppOBJ;
import com.linkage.module.gwms.obj.gw.QoSClassificationOBJ;
import com.linkage.module.gwms.obj.gw.QoSClassificationTypeOBJ;
import com.linkage.module.gwms.obj.gw.QoSOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;
import com.linkage.module.gwms.util.strategy.StrategyXml;

/**
 * BIO: QoS config.
 * 
 * @author Jason(3412),alex(yanhj@)
 * @date 2009-7-20
 */
@SuppressWarnings("unchecked")
public class QosConfigBIO {

	private static Logger logger = LoggerFactory
			.getLogger(QosConfigBIO.class);

	/** 结果 */
	private int resultId = -9;

	private StrategyQosDAO strategyQosDao;

	private WlanDAO wlanDao;

	private QosDAO qosDAO;

	private QosCAO qosCAO;

	/** result */
	private String result = "";

	/**
	 * get data from db.
	 * 
	 * @param deviceId
	 * @return
	 */
	public QoSOBJ getData(String deviceId) {
		logger.debug("getData({})", deviceId);

		QoSOBJ obj = null;

		if (deviceId == null) {
			logger.warn("getData deviceId is null");
			resultId = -9;
			return obj;
		}

		// SG
		if ((resultId = qosCAO.getDataFromSG(deviceId, 5)) != 1) {
			logger.warn("getData sg fail");

			return obj;
		}

		// db
		Map map = qosDAO.getData(deviceId);
		if (map == null) {
			logger.debug("map == null");
			resultId = 0;
			return obj;
		}

		obj = new QoSOBJ();
		obj.setDeviceId(deviceId);
		obj.setEnable(StringUtil.getStringValue(map, "enable"));
		obj.setMode(StringUtil.getStringValue(map, "qos_mode"));

		return obj;
	}

	/**
	 * const
	 * 
	 * @param strategyId
	 */
	public boolean qosConfig(long strategyId, String gw_type) {
		logger.debug("qosConfig({})", strategyId);

		return CreateObjectFactory.createPreProcess(gw_type).processOOBatch(String.valueOf(strategyId));
	}

	/**
	 * 下发Qos策略,并调用预读模块
	 * 
	 * @param accOid
	 *            用户accOid, tempId Qos的模板ID,根据模板表
	 * @author Jason(3412)
	 * @date 2009-7-22
	 * @return void
	 */
	public long saveQosInfo(String deviceId, long accOid, QoSOBJ qosObj,
			int tempId) {
		logger.debug("qosConfig({},{},{})", new Object[] { deviceId, accOid,
				qosObj });
		StrategyOBJ strategyObj = new StrategyOBJ();
		// id,acc_oid,time,type,device_id,oui,device_serialnumber,username,sheet_para,service_id,task_id,order_id,sheet_type
		// 策略ID
		strategyObj.createId();
		// 策略配置时间
		strategyObj.setTime(new DateTimeUtil().getLongTime());
		// 用户id
		strategyObj.setAccOid(accOid);
		// 立即执行
		strategyObj.setType(0);
		// 设备ID
		strategyObj.setDeviceId(deviceId);
		// QOS serviceId
		strategyObj.setServiceId(1301);
		// 顺序,默认1
		strategyObj.setOrderId(1);
		// 工单类型: 新工单,工单参数为xml串的工单
		strategyObj.setSheetType(2);
		// 参数
		strategyObj.setSheetPara(StrategyXml.qosObj2Xml(qosObj));
		strategyObj.setTempId(1301);
		strategyObj.setIsLastOne(1);
		// 入策略表
		strategyQosDao.addStrategy(strategyObj);
		
		strategyQosDao.saveQosStrategyTemp(strategyObj.getId(), tempId);

		StrategyQosParaOBJ[] qosParaArr = qosObj.getQosParam();
		if (null != qosParaArr) {
			for (int i = 0; i < qosParaArr.length; i++) {
				qosParaArr[i].setId(strategyObj.getId());
				strategyQosDao.saveQosStrategyParam(qosParaArr[i]);
			}
		}

		return strategyObj.getId();
	}

	/**
	 * 根据页面参数获取Qos配置对象,
	 * 
	 * @param paramWave格式为:'pram1,pram2'
	 * @author Jason(3412)
	 * @date 2009-7-22
	 * @return QoSOBJ
	 */
	public QoSOBJ genQosObj(String[] paramArr, String queueId,
			String[] aliaParamArr) {
		logger.debug("genQosObj({},{})", paramArr, queueId);
		QoSOBJ qosObj = new QoSOBJ();
		qosObj.setEnable("1");
		qosObj.setPlan("priority");
		qosObj.setEnab802p("2");
		qosObj.setEnabDscp("1");
		// class
		QoSClassificationOBJ claObj = new QoSClassificationOBJ();
		claObj.setQueueId(queueId);
		claObj.setValue8021p("0");
		claObj.setValueDscp("0");
		// type
		int len = paramArr.length;
		QoSClassificationTypeOBJ[] typeObjArr = new QoSClassificationTypeOBJ[len];
		StrategyQosParaOBJ[] qosParamArr = new StrategyQosParaOBJ[len];
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				typeObjArr[i] = new QoSClassificationTypeOBJ();
				typeObjArr[i].setTypeName("LANInterface");
				//路径InternetGatewayDevice.LANDevice.1.WLANConfiguration.3
				typeObjArr[i].setTypeMax(paramArr[i]);
				typeObjArr[i].setTypeMin(paramArr[i]);
				typeObjArr[i].setTypeProt("TCP,UDP");
				// qos参数
				qosParamArr[i] = new StrategyQosParaOBJ();
				//ssid名称 CU_AP3
				qosParamArr[i].setParaValue(aliaParamArr[i]);
				//队列
				qosParamArr[i].setQueueId(StringUtil.getIntegerValue(queueId));
				// class为2
				qosParamArr[i].setSubId(2);
				qosParamArr[i].setSubOrder(i + 1);
				qosParamArr[i].setTypeId(1);
				qosParamArr[i].setTypeMax(paramArr[i]);
				qosParamArr[i].setTypeMin(paramArr[i]);
				qosParamArr[i].setTypeName("LANInterface");
				qosParamArr[i].setTypeOrder(i + 1);
				qosParamArr[i].setTypeProt("TCP,UDP");
			}
		} else {
			logger.warn("ssidArr的长度小于了1,ssid参数不正确");
		}
		claObj.setClassType(typeObjArr);
		QoSClassificationOBJ[] claObjArr = { claObj };
		qosObj.setQosCalss(claObjArr);
		qosObj.setQosParam(qosParamArr);

		return qosObj;
	}

	/**
	 * 根据页面参数获取SSID的Qos配置对象
	 * 
	 * @param ssid:wlan结点字符串;
	 *            queueId:队列; aliaSsid:SSID名称
	 * @author Jason(3412)
	 * @date 2009-7-22
	 * @return QoSOBJ
	 */
	public QoSOBJ genSsidQosObj(String[] ssid, String queueId, String[] aliaSsid) {
		logger.debug("genSsidQosObj({},{})", ssid, queueId);
		return genQosObj(ssid, queueId, aliaSsid);
	}

	/**
	 * 根据页面参数获取IPTV的Qos配置对象
	 * 
	 * @param iptvLanPort:端口字符串;
	 *            queueId:队列; aliaIptv:"Lan{i}"
	 * @author Jason(3412)
	 * @date 2009-7-22
	 * @return QoSOBJ
	 */
	public QoSOBJ genIptvQosObj(String[] iptvLanPort, String queueId,
			String[] aliaIptv) {
		logger.debug("genIptvQosObj({},{})", iptvLanPort, queueId);
		return genQosObj(iptvLanPort, queueId, aliaIptv);
	}

	/**
	 * 获取Voip的Qos对象
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-22
	 * @return QoSOBJ
	 */
	public QoSOBJ genVoipQosObj() {
		logger.debug("genVoipQosObj()");

		QoSOBJ qosObj = new QoSOBJ();
		qosObj.setMode("INTERNET, TR069,VoIP");
		qosObj.setEnable("1");
		qosObj.setPlan("priority");
		qosObj.setBandwidth("0");
		qosObj.setEnabWidth("0");
		qosObj.setEnab802p("0");
		qosObj.setEnabDscp("0");
		// App 队列为1
		int queue = 1;
		QoSAppOBJ[] qosApp = new QoSAppOBJ[2];
		qosApp[0] = new QoSAppOBJ();
		qosApp[0].setQueueId("" + queue);
		qosApp[0].setAppName("VoIP");
		qosApp[1] = new QoSAppOBJ();
		qosApp[1].setQueueId("2");
		qosApp[1].setAppName("TR069");
		qosObj.setQosApp(qosApp);

		StrategyQosParaOBJ[] qosParamArr = new StrategyQosParaOBJ[1];
		StrategyQosParaOBJ qosParam = new StrategyQosParaOBJ();
		qosParam.setParaValue("Voip");
		qosParam.setQueueId(queue);
		// App为1
		qosParam.setSubId(1);
		qosParam.setSubOrder(1);
		qosParamArr[0] = qosParam;
		qosObj.setQosParam(qosParamArr);
		return qosObj;
	}

	/**
	 * 获取业务配置的Qos对象
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-8-18
	 * @return QoSOBJ
	 */
	public QoSOBJ genQosServ() {
		logger.debug("genQosServ()");

		QoSOBJ qosObj = new QoSOBJ();
		qosObj.setEnable("1");
		qosObj.setType(0);

		return qosObj;
	}

	/**
	 * 采集，获取wlan结点的ssid和结点信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-22
	 * @return String
	 */
	public String gatherWlan(String deviceId, String gw_type) {
		logger.debug("gatherWlan({})", deviceId);
		//
		String res = null;
		int iresult = new SuperGatherCorba(gw_type).getCpeParams(deviceId, 12);
		logger.debug("gatherWlan iresult:" + iresult);
		if (1 == iresult) {
			res = getWlanFromDb(deviceId);
		} else {
			res = getErrMeg(iresult);
		}
		return res;
	}

	/**
	 * 从数据库中获取ssid和结点信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-22
	 * @return String
	 */
	public String getWlanFromDb(String deviceId) {
		logger.debug("getWlanFromDb({})", deviceId);

		StringBuffer resSb = new StringBuffer();
		List wlanList = wlanDao.getData(deviceId);
		if (null != wlanList && wlanList.size() > 0) {
			int size = wlanList.size();
			for (int i = 0; i < size; i++) {
				Map tMap = (Map) wlanList.get(i);
				resSb.append(tMap.get("ssid"));
				resSb.append("|");
				resSb.append(wlanPort(tMap.get("lan_id"), tMap
						.get("lan_wlan_id")));
				resSb.append("$");
			}
			resSb.deleteCharAt(resSb.length() - 1);
		}
		logger.debug("getWlanFromDb return({})", resSb.toString());
		return resSb.toString();
	}
	
	private String getDesc(int tmp) {
		logger.debug("getResult()");

		result = Global.G_Fault_Map.get(tmp).getFaultDesc();
		if (result == null) {
			logger.debug("flag == null");

			result = Global.G_Fault_Map.get(100000).getFaultDesc();
		}

		return result;
	}

	/**
	 * 获取配置Qos的list
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-24
	 * @return List<Map>
	 */
	public List<Map> getQosConfigList(String deviceId, String tempId) {
		logger.debug("getQosConfigList({},{})", deviceId, tempId);

		List<Map> mList = null;
		if (true == StringUtil.IsEmpty(deviceId)
				|| true == StringUtil.IsEmpty(tempId)) {
			logger.warn("设备id或者模板id为空");
		} else {
			mList = new ArrayList<Map>();
			List strategyList = strategyQosDao.queryStrategyQos(deviceId,
					tempId);
			if (null != strategyList && false == strategyList.isEmpty()) {
				int size = strategyList.size();
				for (int i = 0; i < size; i++) {
					Map rMap = new HashMap();
					Map tMap = (Map) strategyList.get(i);
					// 策略ID
					long strategyId = StringUtil.getLongValue(tMap, "id");
					// 执行状态
					rMap.put("status", StringUtil.getStringValue(tMap
							.get("status")));
					// 执行结果描述
					rMap.put("result", this.getResult(StringUtil.getIntValue(
							tMap, "result_id")));
					rMap.put("resultDesc", this.getDesc(StringUtil.getIntValue(
							tMap, "result_id")));
					// 配置时间
					long time = StringUtil.getLongValue(tMap, "time");
					rMap.put("time", new DateTimeUtil(time * 1000)
							.getLongDate());

					// 获取qos配置参数
					StringBuffer qosPama = new StringBuffer();
					List qosParaList = strategyQosDao
							.getQosStrategyPara(strategyId);
					int qosSize = qosParaList.size();
					for (int j = 0; j < qosSize; j++) {
						Map qosParaMap = (Map) qosParaList.get(j);
						qosPama.append(StringUtil.getStringValue(qosParaMap,
								"para_value"));
						qosPama.append(";");
					}
					rMap.put("qosParam", qosPama.toString());
					mList.add(rMap);
				}
			} else {
				logger.debug("该设备未下发过该Qos模板的配置");
			}
		}

		return mList;
	}

	/**
	 * Wlan结点信息
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-7-22
	 * @return String
	 */
	public String wlanPort(Object lanId, Object wlanId) {
		logger.debug("wlanPort({},{})", lanId, wlanId);

		return IParamTree.LANDEVICE + "." + lanId + "." + IParamTree.WLANCONFIG
				+ "." + wlanId;
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
		logger.debug("getErrMeg({})", stat);

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

	public void setStrategyQosDao(StrategyQosDAO strategyQosDao) {
		this.strategyQosDao = strategyQosDao;
	}

	public void setWlanDao(WlanDAO wlanDao) {
		this.wlanDao = wlanDao;
	}

	/**
	 * get:
	 * 
	 * @return the qosDAO
	 */
	public QosDAO getQosDAO() {
		return qosDAO;
	}

	/**
	 * get:
	 * 
	 * @return the qosCAO
	 */
	public QosCAO getQosCAO() {
		return qosCAO;
	}

	/**
	 * set:qosDAO
	 * 
	 * @param qosDAO
	 *            the qosDAO to set
	 */
	public void setQosDAO(QosDAO qosDAO) {
		logger.debug("setQosDAO({})", qosDAO);

		this.qosDAO = qosDAO;
	}

	/**
	 * set:qosCAO
	 * 
	 * @param qosCAO
	 *            the qosCAO to set
	 */
	public void setQosCAO(QosCAO qosCAO) {
		logger.debug("setQosCAO({})", qosCAO);

		this.qosCAO = qosCAO;
	}

	/**
	 * get:resultId
	 * 
	 * @return the resultId
	 */
	public int getResultId() {
		logger.debug("getResultId()");

		return resultId;
	}

	/**
	 * set:resultId
	 * 
	 * @param resultId
	 *            the resultId to set
	 */
	public void setResultId(int resultId) {
		logger.debug("setResultId({})", resultId);

		this.resultId = resultId;
	}

	/**
	 * get:结果描述
	 * 
	 * @return the result
	 */
	public String getResult() {
		logger.debug("getResult()");

		result = Global.G_Fault_Map.get(resultId).getFaultReason();
		if (result == null) {
			logger.debug("flag == null");

			result = Global.G_Fault_Map.get(100000).getFaultReason();
		}

		return result;
	}
	
	public String getDesc() {
		logger.debug("getResult()");

		result = Global.G_Fault_Map.get(resultId).getFaultDesc();
		if (result == null) {
			logger.debug("flag == null");

			result = Global.G_Fault_Map.get(100000).getFaultDesc();
		}

		return result;
	}

	/**
	 * get:结果描述
	 * 
	 * @return the result
	 */
	private String getResult(int tmp) {
		logger.debug("getResult()");

		result = Global.G_Fault_Map.get(tmp).getFaultReason();
		if (result == null) {
			logger.debug("flag == null");

			result = Global.G_Fault_Map.get(100000).getFaultReason();
		}

		return result;
	}

}
