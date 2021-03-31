/**
 * 移植TopoShow设备路由信息
 */
package com.linkage.litms.webtopo.snmpgather;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Performance.Data;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterface;
import com.linkage.litms.common.database.DataSetBean;

/**
 * @author Administrator
 * 
 */
public class DeviceRouteInfo
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(DeviceRouteInfo.class);
	// OID的字符串
	private String[][] m_strOID = { { "1.3.6.1.2.1.4.21.1.1", "ipRouteDest" },
			{ "1.3.6.1.2.1.4.21.1.2", "ipRouteIfIndex" },
			{ "1.3.6.1.2.1.4.21.1.3", "ipRouteMetric1" },
			{ "1.3.6.1.2.1.4.21.1.4", "ipRouteMetric2" },
			{ "1.3.6.1.2.1.4.21.1.5", "ipRouteMetric3" },
			{ "1.3.6.1.2.1.4.21.1.6", "ipRouteMetric4" },
			{ "1.3.6.1.2.1.4.21.1.7", "ipRouteNextHop" },
			{ "1.3.6.1.2.1.4.21.1.8", "ipRouteType" },
			{ "1.3.6.1.2.1.4.21.1.9", "ipRouteProto" },
			{ "1.3.6.1.2.1.4.21.1.10", "ipRouteAge" },
			{ "1.3.6.1.2.1.4.21.1.11", "ipRouteMask" },
			{ "1.3.6.1.2.1.4.21.1.12", "ipRouteMetric5" },
			{ "1.3.6.1.2.1.4.21.1.13", "ipRouteInfo" } };
	// private RemoteDB.DataManager manager = null;
	private String device_id = null;
	private String readcom = null;
	private String loopback_ip = null;
	private String[][] m_strDatas = null; // 采集数据的数组(String类型)
	public DeviceRouteInfo(String device_id, String account, String passwd)
	{
		this.device_id = device_id;
		 String sql = "select loopback_ip from tab_gw_device where device_id='" + device_id + "'";
		 PrepareSQL psql = new PrepareSQL(sql);
		 psql.getSQL();
		 Map field = DataSetBean.getRecord(sql);
		 if(field != null){
		 //this.readcom = String.valueOf(field.get("snmp_ro_community"));
		 loopback_ip = String.valueOf(field.get("loopback_ip"));
		 }
		// logger.debug("设备：readcom=" + this.readcom + "ip=" +
		// this.loopback_ip );
		// DataManagerControl DataManagerControl = new DataManagerControl();
		// manager = DataManagerControl.getDataManager(account,passwd);
		// logger.debug("获取manager=" + manager);
		// DataManagerControl = null;
	}
	/**
	 * 获取列
	 * 
	 * @return
	 */
	public String[] getColumnOID()
	{
		String[] column = new String[m_strOID.length];
		for (int i = 0; i < m_strOID.length; i++)
		{
			column[i] = m_strOID[i][1];
		}
		return column;
	}
	public String getDeviceIP()
	{
		return this.loopback_ip;
	}
	// 由数据库读取采集数据
	public String[][] fnGetData()
	{
		logger.debug("fnGetData＝＝＝＝＝＝＝＝＝＝＝＝");
		// 条件不具备，则退出。
		// if(device_id == null || readcom == null || manager == null){
		if (device_id == null)
		{
			logger.debug("return null");
			return null;
		}
		String m_strDeviceOID = null;
		Data[] DataList = null;
		try
		{
			m_strDatas = new String[0][0];
			// -----------------------------------------------------------------
			// 调用Corba接口，接收采集值
			// -----------------------------------------------------------------
			// 根据ip、expression取得后台轮询得到的最新数值数据列表
			org.omg.CORBA.IntHolder dataType = new org.omg.CORBA.IntHolder();
			// 先读取读取第1个OID的数据，并获取端口数
			m_strDeviceOID = m_strOID[0][0];
//			DataList = manager.GetDataListIMD(dataType, this.device_id, readcom,
//					m_strDeviceOID);
			DataList =SnmpGatherInterface.GetInstance().GetDataListIMDFull(dataType, this.device_id, readcom,m_strDeviceOID);
			// logger.debug(DataList);
			if (DataList == null || DataList.length == 0)
			{
				return null;
			}
			// 分配数组m_strDatas的空间
			m_strDatas = new String[DataList.length + 2][m_strOID.length];
			for (int i = 0; i < m_strDatas.length; i++)
			{
				for (int j = 0; j < m_strDatas[0].length; j++)
				{
					m_strDatas[i][j] = "";
				}
			}
			logger.debug(" 开始采集");
			// 赋第1次的采集值
			for (int j = 0; j < DataList.length; j++)
			{
				// 类型值 1-数字 2-字符
				if (dataType.value == 1)
				{
					m_strDatas[j][0] = String.valueOf(DataList[j].dataDou);
					// 值
				}
				else if (dataType.value == 2)
				{
					m_strDatas[j][0] = DataList[j].dataStr.trim();
				}
				else
				{
					m_strDatas[j][0] = "";
				}
			}
			// -------------------------------------------------------------
			// 根据数组m_strOID的记录，循环获取其余OID的数据
			for (int i = 1; i < m_strOID.length; i++)
			{
				m_strDeviceOID = m_strOID[i][0];
//				DataList = manager.GetDataListIMD(dataType, device_id, readcom,
//						m_strDeviceOID);
				DataList=SnmpGatherInterface.GetInstance().GetDataListIMDFull(dataType, device_id, readcom,m_strDeviceOID);
				// logger.debug(DataList);
				// 如果源数组为空，则设本次采集值为0
				if (DataList != null && DataList.length > 0)
				{
					// 赋本次采集值
					for (int j = 0; j < DataList.length; j++)
					{
						// 类型值 1-数字 2-字符
						if (dataType.value == 1)
						{
							m_strDatas[j][i] = String.valueOf(DataList[j].dataDou);
							// 值
						}
						else if (dataType.value == 2)
						{
							m_strDatas[j][i] = DataList[j].dataStr.trim();
						}
						else
						{
							m_strDatas[j][i] = "";
						}
						if (j == m_strDatas.length)
							break;
					}
				}
				// -------------------------------------------------------------
				logger.debug("采集中..." + i);
			} // 数组循环
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			logger.warn("Exception" + ex);
		}
		return m_strDatas;
	}
}
