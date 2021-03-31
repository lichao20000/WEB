package com.linkage.litms.common.corba;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import AlarmFilter.O_AlarmFilterHelper;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

//import RemoteDB.DB;

public class WebCorba {
	private static Logger m_logger = LoggerFactory.getLogger(WebCorba.class);

	private PrepareSQL pSQL = null;

	private Map field = null;

	private static NamingContext ncRef = null;

	// 进行服务定位
	private static NameComponent path[] = { new NameComponent("", "PoaName"),
			new NameComponent("", "ObjectName") };

	/**
	 * corba哈希映射
	 */
	private String LOAD_CORBA_IORS = "select object_name,object_poa,ior from tab_ior";

	/**
	 * Corba服务名与引用ior映射
	 */
	private static Map m_IorMap = null;

	/**
	 * 所有Corba对象哈希列表
	 */
	private static Map m_CorbaMap = null;

	/**
	 * 进程名
	 */
	private String m_ProcessName = null;

	/**
	 * 采集点编号
	 */
	private String m_GatherId = null;

	/**
	 * 设备编号
	 */
	// private String m_DeviceId = null;
	/**
	 * 子进程编号
	 */
	private String m_Location = null;

	/**
	 * 本地服务名
	 */
	private String m_LocalName = null;

	/**
	 * 本地POA服务名
	 */
	private String m_LocalPoaName = null;

	/**
	 * 临时LocalName LocalPoaName
	 */
	private String m_LocalName_PoaName_TMP = null;

	/**
	 * 进程数
	 */
	private String m_ProcessNum = null;

	/**
	 * Corba字符串引用
	 */
	private String m_Ior = null;

	/**
	 * MasterControl调用的LocalName
	 */
	private String WEBTOPO_OBJECT_NAME = "RemoteDbServant";

	/**
	 * MasterControl调用的PoaName
	 */
	private String WEBTOPO_OBJECT_POA = "lmgDataSource_poaJiangSu";

	/**
	 * VPNMasterControl调用的LocalName
	 */
//	private String WEBVPNTOPO_OBJECT_NAME = "VPNRemoteDbServant";

	/**
	 * VPNMasterControl调用的PoaName
	 */
//	private String WEBVPNTOPO_OBJECT_POA = "lmgDataSource_poaJiangSu";

	/**
	 * 创建的Corba对象
	 */
	private org.omg.CORBA.Object object = null;

	/**
	 * 得到采集点、进程名称、子进程编号与参数内容对照
	 */
	private static Map m_ProcessGatherLocationMap = null;

	/**
	 * 根据进程名称和采集点编号获得Corba引用服务名
	 */
	private String LOAD_POANAME_ALL = "select * from tab_process_config";

	/**
	 * 获得采集进程子进程数
	 */
	private String LOAD_CHILDPROCESS_NUM = "select process_number from tab_process where gather_id=? and process_name=?";

	/**
	 * 构造结构
	 * 
	 * @param m_ProcessName
	 * @param m_GatherId
	 * @param m_DeviceId
	 */
	public WebCorba(String m_ProcessName, String m_GatherId, String m_DeviceId) {
		pSQL = new PrepareSQL();
		this.m_ProcessName = m_ProcessName;
		this.m_GatherId = m_GatherId;
		// this.m_DeviceId = m_DeviceId;

		// 若为空，则获取所有Corba对象引用
		if (m_CorbaMap == null) {
			m_CorbaMap = new HashMap();
		}

		// 若为空，则获取所有ior引用
		if (m_IorMap == null) {
			m_IorMap = new HashMap();
			getIorMap();
		}

		if (m_ProcessGatherLocationMap == null) {
			m_ProcessGatherLocationMap = new HashMap();
			// 获得服务名
			getLocalPoaName();
		}

		// 获得子进程数 m_ProcessNum
		getChildProcessNum();

		// 表示为Java调度
		if (m_ProcessNum == null) {
			if (m_ProcessName.equalsIgnoreCase("MasterControl")) {
				// webtopo对MasterControl调用的LocalName获取
				WEBTOPO_OBJECT_NAME = LipossGlobals
						.getLipossProperty("webtopo.MC_NAME");
				WEBTOPO_OBJECT_POA = LipossGlobals
						.getLipossProperty("webtopo.MC_POA");

				this.m_Ior = String.valueOf(m_IorMap.get(WEBTOPO_OBJECT_NAME
						+ ";" + WEBTOPO_OBJECT_POA));
			} else if (m_ProcessName.equalsIgnoreCase("VpnTopo")) {
				WEBTOPO_OBJECT_NAME = LipossGlobals
						.getLipossProperty("webvpntopo.MC_NAME");
				WEBTOPO_OBJECT_POA = LipossGlobals
						.getLipossProperty("webvpntopo.MC_POA");
				this.m_Ior = String.valueOf(m_IorMap.get(WEBTOPO_OBJECT_NAME
						+ ";" + WEBTOPO_OBJECT_POA));
			}

			// Corba对象哈希里不存在引用对象,则初始化此对象
			if (m_CorbaMap.get(m_GatherId + ";" + m_ProcessName + ";"
					+ m_Location) == null) {
				reBindJavaCorba();
			}
		} else {
			// 获得采集点子进程编号
			this.m_Location = String.valueOf(Integer.parseInt(m_DeviceId)
					% Integer.parseInt(m_ProcessNum));

			this.m_Ior = String.valueOf(m_IorMap.get("NameService;/rootPoa"));

			m_LocalName_PoaName_TMP = String.valueOf(m_ProcessGatherLocationMap
					.get(m_GatherId + ";" + m_ProcessName + ";" + m_Location));
			this.m_LocalName = m_LocalName_PoaName_TMP.split(";")[0];
			this.m_LocalPoaName = m_LocalName_PoaName_TMP.split(";")[1];
			m_LocalName_PoaName_TMP = null;

			// Corba对象哈希里不存在引用对象,则初始化此对象
			if (m_CorbaMap.get(m_GatherId + ";" + m_ProcessName + ";"
					+ m_Location) == null) {
				m_logger.warn("get is null");
				reBind();
			}
		}
		Iterator it = m_CorbaMap.keySet().iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			m_logger.debug("key:"+key);
		}
	}
	public void removeKey(String gather_id,String process_name,String device_id){
		
//		 获得采集点子进程编号
		this.m_Location = String.valueOf(Integer.parseInt(device_id)
				% Integer.parseInt(m_ProcessNum));
		m_logger.debug("remove: "+gather_id + ";" + process_name + ";"
				+ m_Location);
		m_CorbaMap.remove(gather_id + ";" + process_name + ";"
					+ m_Location);
	}
	/**
	 * 获得Corba引用实例
	 * 
	 * @return org.omg.CORBA.Object
	 */
	public org.omg.CORBA.Object getInstance() {
//		m_logger.debug("getInstance() = " + m_GatherId + ";" + m_ProcessName
//				+ ";" + m_Location);
//		m_logger.debug(m_CorbaMap.get(m_GatherId + ";" + m_ProcessName + ";"
//				+ m_Location));
		return (org.omg.CORBA.Object) m_CorbaMap.get(m_GatherId + ";"
				+ m_ProcessName + ";" + m_Location);
	}

	/**
	 * 根据Corba服务名分别引用不同对象实例
	 * 
	 * @param m_CorbaName
	 *            MasterControl<br>
	 *            Pmee<br>
	 *            FluxManage<br>
	 *            AdslCheck<br>
	 *            TroubleLocation<br>
	 *            Simulate<br>
	 *            经过参数注入调用本方法返回成功对象之后，则可以直接使用具体的方法<br>
	 *            例如：对于AdslCheck Corba引用
	 *            getIDLCorba("AdslCheck").GetPerfData(String[] msg)
	 * @return Object
	 */
	public Object getIDLCorba(String m_CorbaName) {
		/**
		 * 获取MasterControl Corba引用
		 */
		if (m_CorbaName.equalsIgnoreCase("MasterControl")) {
			return (RemoteDB.DB) (getInstance());
		}

		/**
		 * 获取VPN Topo Corba引用
		 */
		if (m_CorbaName.equalsIgnoreCase("VpnTopo")) {
			return (RemoteDB.DB) (getInstance());
		}
		/**
		 * 获取Net Topo Corba引用
		 */
		if (m_CorbaName.equalsIgnoreCase("NetTopo")) {
			return (RemoteDB.WebTopoManager) (getInstance());
		}
//		/**
//		 * 获取Pmee Corba引用（性能相关）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("Pmee")) {
//			return PMEEManagerHelper.narrow(getInstance());
//		}
//		/**
//		 * 获取FluxManage Corba引用（流量相关）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("FluxManage")) {
//			return FluxDeviceConfHelper.narrow(getInstance());
//		}
//		/**
//		 * 获取AdslCheck Corba引用（在线测试、现场安装相关）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("AdslCheck")) {
//			return Config_ModifiedHelper.narrow(getInstance());
//		}
//		/**
//		 * 获取TroubleLocation Corba引用（故障定位）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("TroubleLocation")) {
//			return Trouble_LocationHelper.narrow(getInstance());
//		}
//		/**
//		 * 获取Simulate Corba引用（业务模拟测试）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("Simulate")) {
//			return I_ConfigModifiedHelper.narrow(getInstance());
//		}
//		/**
//		 * 获取SnmpGather Corba引用（实时采集测试）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("SnmpGather")) {
//			return Performance.PerformanceManagerHelper.narrow(getInstance());
//		}
//		/**
//		 * 获取TopoServer Corba引用（拓扑发现测试）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("TopoServer")) {
//			return I_Topo_Object.I_ObjectManagerHelper.narrow(getInstance());
//		}
//		/**
//		 * 获取HostMan Corba引用（主机管理测试）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("HostMan")) {
//			return I_Host_Object.I_ObjectManagerHelper.narrow(getInstance());
//		}
//
//		/**
//		 * 获取IpCheck Corba引用（IP测试）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("IpCheck")) {
//			return I_Ip_Check.I_ObjectManagerHelper.narrow(getInstance());
//		}
//
//		/**
//		 * 获取Scheduler Corba引用（综合调度测试）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("Scheduler")) {
//			return EventCorrelation.EventSchedulerHelper.narrow(getInstance());
//		}
//
//		/**
//		 * 获取AdslCheck Corba引用（在线测试）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("AdslCheck")) {
//			return I_Config_Modified.Config_ModifiedHelper
//					.narrow(getInstance());
//		}
//		/**
//		 * 获取ArpCheck Corba引用（Arp测试）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("ArpCheck")) {
//			return User_Arp_Check.I_LanUserArpCheckHelper.narrow(getInstance());
//		}
//		/**
//		 * 获取SnmpPing Corba引用（SnmpPing测试）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("SnmpPing")) {
//			return I_Snmp_Ping.I_ConfigModifiedHelper.narrow(getInstance());
//		}
//
//		/**
//		 * 获取HwLsCheck Corba引用（HwLsCheck测试）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("HwLsCheck")) {
//			return I_Hw52_Check.I_Hw52CheckHelper.narrow(getInstance());
//		}
//
//		/**
//		 * 获取AlarmFilter Corba引用（AlarmFilter测试）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("AlarmFilter")) {
//			return AlarmFilter.O_AlarmFilterHelper.narrow(getInstance());
//		}
//
//		/**
//		 * 获取ForwardAlarm Corba引用（ForwardAlarm测试）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("ForwardAlarm")) {
//			return FowardAlarm.O_FowardAlarmHelper.narrow(getInstance());
//		}
//
//		/**
//		 * 获取EventCorrelation Corba引用（EventCorrelation测试）
//		 */
//		if (m_CorbaName.equalsIgnoreCase("EventCorrelation")) {
//			return EventSchedulerHelper.narrow(getInstance());
//		}
//			
//		
//		if (m_CorbaName.equalsIgnoreCase("TrapProbe")) {
//			return O_TrapProbeHelper.narrow(getInstance());
//		}
		
		if (m_CorbaName.equalsIgnoreCase("EventCorrelation")) {
	    	return O_AlarmFilterHelper.narrow(getInstance());
	    }
		 
		return null;
	}

	/**
	 * 获得采集进程子进程数
	 * 
	 * @return void
	 */
	private void getChildProcessNum() {
		pSQL.setSQL(LOAD_CHILDPROCESS_NUM);

		pSQL.setString(1, m_GatherId);
		pSQL.setString(2, m_ProcessName);
//		m_logger.debug(pSQL.getSQL());
		field = DataSetBean.getRecord(pSQL.getSQL());

		// 若在进程配置表中不存在，则判断为Java进程调用，另作处理
		if (field == null) {
			this.m_ProcessNum = null;
		} else {
			this.m_ProcessNum = String.valueOf(field.get("process_number"));
		}

		field = null;
	}

	/**
	 * 根据进程名和采集点编号获得Corba服务引用名
	 * 
	 */
	private void getLocalPoaName() {
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			LOAD_POANAME_ALL = "select gather_id, process_name, location, para_item, para_context from tab_process_config";
		}
		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(LOAD_POANAME_ALL);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(LOAD_POANAME_ALL);
		field = cursor.getNext();
		String m_Exchange = null;

		while (field != null) {
			// 不包含
			if (!m_ProcessGatherLocationMap.containsKey(field.get("gather_id")
					+ ";" + field.get("process_name") + ";"
					+ field.get("location"))) {
				if (String.valueOf(field.get("para_item")).equalsIgnoreCase(
						"LocalName")) {
					m_ProcessGatherLocationMap.put(field.get("gather_id") + ";"
							+ field.get("process_name") + ";"
							+ field.get("location"), field.get("para_context")
							+ ";");
				}
				if (String.valueOf(field.get("para_item")).equalsIgnoreCase(
						"LocalPoaName")) {
					m_ProcessGatherLocationMap.put(field.get("gather_id") + ";"
							+ field.get("process_name") + ";"
							+ field.get("location"), ";"
							+ field.get("para_context"));
				}
			} else {
				m_Exchange = String.valueOf(m_ProcessGatherLocationMap
						.get(field.get("gather_id") + ";"
								+ field.get("process_name") + ";"
								+ field.get("location")));

				if (String.valueOf(field.get("para_item")).equalsIgnoreCase(
						"LocalName")) {
					m_ProcessGatherLocationMap.put(field.get("gather_id") + ";"
							+ field.get("process_name") + ";"
							+ field.get("location"), field.get("para_context")
							+ m_Exchange);
				}
				if (String.valueOf(field.get("para_item")).equalsIgnoreCase(
						"LocalPoaName")) {
					m_ProcessGatherLocationMap.put(field.get("gather_id") + ";"
							+ field.get("process_name") + ";"
							+ field.get("location"), m_Exchange
							+ field.get("para_context"));
				}
			}

			field = cursor.getNext();
		}
	}

	/**
	 * 获得所有Corba引用参数
	 * 
	 * @return void
	 */
	private void getIorMap() {
		m_IorMap = null;
		m_IorMap = new HashMap();
		PrepareSQL psql = new PrepareSQL(LOAD_CORBA_IORS);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(LOAD_CORBA_IORS);
		field = cursor.getNext();

		while (field != null) {
			m_IorMap.put(field.get("object_name") + ";"
					+ field.get("object_poa"), field.get("ior"));

			field = cursor.getNext();
		}

		// clear
		field = null;
		cursor = null;
	}

	/**
	 * 重新绑定Java corba对象
	 * 
	 * @return boolean
	 */
	private boolean reBindJavaCorba() {
		// 刷新IOR
		this.getIorMap();
		this.m_Ior = String.valueOf(m_IorMap.get(WEBTOPO_OBJECT_NAME + ";"
				+ WEBTOPO_OBJECT_POA));

		boolean result = true;
		org.omg.CORBA.Object db_object = null;
		// org.omg.CORBA.Object webtopo_object = null;
		// org.omg.CORBA.Object webvpntopo_object = null;

		try {
			// m_logger.debug(m_Ior);
			ORB orb = ORB.init((String[]) null, null);
			org.omg.CORBA.Object objRef = orb.string_to_object(m_Ior);
			db_object = RemoteDB.DBHelper.narrow(objRef);

			// m_logger.debug(db_object);
			// m_logger.debug("reBindJavaCorba() = " + m_GatherId + ";"
			// + m_ProcessName + ";" + m_Location);
			// m_CorbaMap.remove(m_GatherId + ";" + m_ProcessName + ";" +
			// m_Location);
			m_CorbaMap.put(m_GatherId + ";" + m_ProcessName + ";" + m_Location,
					db_object);
		} catch (Exception e) {
			e.printStackTrace();
			m_logger.error("获取Web java Corba接口失败：" + e.getMessage());

			return false;
		}

		return result;
	}

	/**
	 * 初始化Corba引用对象
	 * 
	 * @return boolean
	 */
	private boolean reBind() {
		boolean result = true;

		try {
			ORB orb = ORB.init((String[]) null, null);
			org.omg.CORBA.Object objRef = orb.string_to_object(m_Ior);
			ncRef = NamingContextHelper.narrow(objRef);
			path[0].id = this.m_LocalPoaName;
			path[1].id = this.m_LocalName;
			object = ncRef.resolve(path);
			m_logger.debug("object: "+object);
//			m_logger.debug(object);
			m_CorbaMap.put(m_GatherId + ";" + m_ProcessName + ";" + m_Location,
					object);
		} catch (Exception e) {
			e.printStackTrace();
			m_logger.error("获取Web Corba接口失败：" + e.getMessage());
			return false;
		}

		return result;
	}

	/**
	 * 刷新Corba对象引用
	 * 
	 * @return boolean
	 */
	public boolean refreshCorba(String languageType) {
		return languageType.equalsIgnoreCase("java") ? reBindJavaCorba()
				: reBind();
	}
	
	/**
	 * 刷新Corba对象引用
	 * 
	 * @return boolean
	 */
	public boolean refreshCorba() {
		return reBind();
	}
}
