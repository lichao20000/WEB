package com.linkage.litms.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.obj.StrategyOBJ;

/**
 * 
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Aug 26, 2009
 * @see
 * @since 1.0
 */
@SuppressWarnings( { "unused", "unchecked" })
public class DoBusinessThread {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(DoBusinessThread.class);

	/**
	 * 单态实例
	 */
	private static DoBusinessThread instance = null;
	/**
	 * 单态实例锁
	 */
	private static Object lock = new Object();

	/** the sleep-time of Schedule thread */
	private final int ScheduleSleepTime = 10000;

	/** the min number */
	private final int ScheduleMinNum = 0;

	/** the temp DoBusinessObject */
	private ArrayList<String> tmpList = null;

	/** device_id + , + service_id */
	private Map<String, String> deviceInfoMap = new HashMap<String, String>();

	private String[] deviceIdArr = null;

	private String[] serviceIdArr = null;

	public static DoBusinessThread getInstance() {
		synchronized (lock) {
			if (null == instance) {
				instance = new DoBusinessThread();
			}
		}

		return instance;
	}

	private DoBusinessThread() {
		Thread ODBThread = new Thread(new OperateDB());
		ODBThread.start();
	}

	/**
	 * get sql list,and remove it.
	 */
	public ArrayList<String> GetScheduleSQLList(int length) {
		ArrayList<String> tmpList = null;
		String[] sqlArr = null;

		if (length <= 0) {
			return null;
		}
		tmpList = new ArrayList<String>();
		for (int j = 0; j < length; j++) {
			// 取出一个对象，生成SQL
			sqlArr = getSQLs(LipossGlobals.DoBusinessObjList.get(0));

			tmpList.add(sqlArr[0]);
			tmpList.add(sqlArr[1]);

			LipossGlobals.DoBusinessObjList.remove(0);
		}

		return tmpList;
	}

	/**
	 * 从DoBusinessObject对象中取得数据，生成SQL
	 * 
	 * @param dbo
	 * @return
	 */
	private String[] getSQLs(DoBusinessObject dbo) {
		long id = StrategyOBJ.createStrategyId();
		String device_id = dbo.getDevice_id();
		String gather_id = dbo.getGather_id();
		String oui = dbo.getOui();
		String device_serialnumber = dbo.getDevice_serialnumber();
		String vpiid = dbo.getVpiid();
		String vciid = dbo.getVciid();
		String username = dbo.getUsername();
		String passwd = dbo.getPasswd();
		String service_id = dbo.getService_id();
		String wan_type = dbo.getWan_type();
		String ipaddress = dbo.getIpaddress();
		String ipmask = dbo.getIpmask();
		String gateway = dbo.getGateway();
		String adsl_ser = dbo.getAdsl_ser();
		String bind_port = dbo.getBind_port();

		String[] SQLs = new String[2];

		Date currentDate = new Date();
		// 设备最近更新时间 获取当前时间
		long l_curupdatetime = currentDate.getTime() / 1000;

		// List<String> sqlList = new ArrayList<String>();

		/**
		 * 路由开户的参数顺序是PVC,username,password,绑定端口
		 */
		String sheet_para = "PVC:" + vpiid + "/" + vciid;
		if ("1008".equals(service_id)) {
			// 路由(参数顺序是PVC,username,password,绑定端口)
			sheet_para += "|||" + username + "|||" + passwd;
		} else if ("1010".equals(service_id)) {
			// 静态IP(参数顺序是PVC,ipaddress,ipmask,gateway,adsl_ser,绑定端口)
			sheet_para += "|||" + ipaddress + "|||" + ipmask + "|||" + gateway
					+ "|||" + adsl_ser;
		}

		// 绑定端口
		if (null == bind_port || "".equals(bind_port)) {
			sheet_para += "|||InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1,InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3,InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4";
		} else {
			sheet_para += bind_port;
		}

		SQLs[0] = "insert into gw_serv_strategy(id,status,acc_oid,"
				+ "time,type,gather_id,device_id,oui,device_serialnumber,username,sheet_para,service_id) "
				+ "values (" + id + ",0,1," + l_curupdatetime + ",0,'"
				+ gather_id + "','" + device_id + "','" + oui + "','"
				+ device_serialnumber + "','" + username + "','" + sheet_para
				+ "'," + service_id + ")";
		PrepareSQL psql = new PrepareSQL(SQLs[0]);
    	psql.getSQL();
		// sqlList.add(tempSQL);

		SQLs[1] = "update tab_hgwcustomer set wan_type= " + wan_type
				+ " where username='" + username + "'";
		psql = new PrepareSQL(SQLs[1]);
    	psql.getSQL();
		// 一旦的数据，及往deviceInfoMap中放值

		deviceInfoMap.put(device_id, service_id);

		// sqlList.add(tempSQL);

		return SQLs;
	}

	/**
	 * 把Map中的device_id, service_id放到两个数组中
	 */
	private void convertDeviceInfo() {
		if (null != deviceInfoMap && deviceInfoMap.size() != 0) {
			Set<String> set = deviceInfoMap.keySet();
			deviceIdArr = new String[set.size()];
			serviceIdArr = new String[set.size()];

			Iterator<String> it = set.iterator();

			for (int i = 0; it.hasNext(); i++) {
				deviceIdArr[i] = it.next();

				serviceIdArr[i] = deviceInfoMap.get(deviceIdArr[i]);
			}
		}
	}

	/**
	 * getDeviceIdArr
	 * 
	 * @return
	 */
	private String[] getDeviceIdArr() {
		for (String dev_id : deviceIdArr) {
			logger.debug("[{}] 通知预处理", dev_id);
		}
		if (deviceIdArr != null) {
			return deviceIdArr;
		}
		return null;
	}

	/**
	 * getServiceIdArr
	 * 
	 * @return
	 */
	private String[] getServiceIdArr() {
		for (String ser_id : serviceIdArr) {

			logger.warn("通知预处理的:{}", ser_id);
		}

		if (serviceIdArr != null) {
			return serviceIdArr;
		}
		return null;
	}

	/**
	 * 开始调用预读模块
	 * 
	 * @param invokeStruct
	 */
	private void invokePreProcess() {
		String[] device_id = getDeviceIdArr();
		String[] service_id = getServiceIdArr();

		if (null == device_id || null == service_id) {

			logger.warn("通知预处理模块时，未获取到device_id和service_id");
			return;
		}

		String ior = getPreProcessIOR();

		// PreProcess.OneToOne[] invokeStruct = getOTOStruct(device_id,
		// service_id);
		// if (null != ior) {
		// String[] args = null;
		// ORB orb = ORB.init(args, null);
		// org.omg.CORBA.Object objRef = orb.string_to_object(ior);
		// PPManager ppm = PPManagerHelper.narrow(objRef);
		// ppm.processOOBatch(invokeStruct);
		// orb = ORB.init(args, null);
		// }
	}

	/**
	 * 通过device_id数组和service_id数组，得到一个PreProcess.OneToOne[]
	 * 
	 * @param device_id
	 * @param service_id
	 * @return
	 */
	public PreProcess.OneToOne[] getOTOStruct(String[] device_id,
			String[] service_id) {
		PreProcess.OneToOne[] oto = new PreProcess.OneToOne[device_id.length];
		for (int i = 0; i < oto.length; i++) {
			oto[i] = new PreProcess.OneToOne();
			oto[i].deviceId = device_id[i];
			oto[i].serviceId = service_id[i];
		}
		return oto;
	}

	/**
	 * 获得预读模块的IOR
	 * 
	 * @return ior
	 */
	private String getPreProcessIOR() {
		String ior = null;
		String iorSQL = "select ior from tab_ior where object_name='PreProcess' and object_poa='PreProcess_Poa'";
		PrepareSQL psql = new PrepareSQL(iorSQL);
    	psql.getSQL();
		Map iorMap = DataSetBean.getRecord(iorSQL);

		if (iorMap != null) {
			ior = (String) iorMap.get("ior");
			return ior;
		} else {
			return ior;
		}
	}

	/**
	 * 清理所有的容器及数组
	 */
	private void clearAllContainers() {
		deviceInfoMap.clear();
		deviceIdArr = null;
		serviceIdArr = null;
	}

	/**
	 * 操作数据库线程
	 * 
	 * @author Administrator
	 * 
	 */
	class OperateDB implements Runnable {
		@Override
		public void run() {

			int length = 0;
			while (true) {
				length = LipossGlobals.DoBusinessObjList.size();

				if (length > ScheduleMinNum) {

					tmpList = GetScheduleSQLList(length);

					DataSetBean.doBatch(tmpList);
					tmpList.clear();

					// 把Map中的device_id, service_id放到两个数组中
					convertDeviceInfo();

					// 调用预处理模块
					// 暂时注释掉了(20090318)
					// invokePreProcess();

					// 清理所有的容器及数组
					clearAllContainers();
				}
				try {
					Thread.sleep(ScheduleSleepTime);
				} catch (InterruptedException e) {
				}
			}

		}
	}
}
