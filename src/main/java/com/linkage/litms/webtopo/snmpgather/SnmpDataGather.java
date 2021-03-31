package com.linkage.litms.webtopo.snmpgather;

import java.util.ArrayList;
import java.util.HashMap;

import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterface;

public class SnmpDataGather
{
	private String indexOid = "";
	private ArrayList oidList = null;
	private String device_id = "";
	// private DataManager manager = null;
	private ArrayList snmpData = null;
	public ArrayList getSnmpData()
	{
		return this.snmpData;
	}
	public SnmpDataGather()
	{
	}
	// 该方法是用于设置首先获取属性索引的ｏｉｄ
	public void setIndexData(String oid)
	{
		this.indexOid = oid;
	}
	// 设置要获取性能数据的ｏｉｄ的数组
	public void setOidData(ArrayList oidList)
	{
		this.oidList = oidList;
	}
	// 设置设备编码
	public void setDevice(String device_id)
	{
		this.device_id = device_id;
	}
	// 返回数组：如果是光获取性能数据，则返回性能数据对应的Ｈａｓｈ
	// 如果是返回索引的数组，则返回性能-索引的ｈａｓｈ，并能获取到一个关于性能数据的二维数组
	public HashMap getDataList()
	{
		HashMap dataMap = new HashMap();
		org.omg.CORBA.IntHolder dataType = new org.omg.CORBA.IntHolder();
		// 第一种情况
		if (indexOid.trim().length() == 0)
		{
			String oid = "";
			Performance.Data[] DataList = null;
			for (int i = 0; i < oidList.size(); i++)
			{
				oid = (String) oidList.get(i);
				dataMap.put(oid, new Performance.Data[0]);
				// begin modified by w5221 山东要求web直接与snmpGather通讯
				DataList = SnmpGatherInterface.GetInstance().GetDataListIMDFull(dataType,
						this.device_id, "", oid);
				// end modified by w5221 山东要求web直接与snmpGather通讯
				// 如果返回的值是整型，则将整型的值转换成str
				if (dataType.value == 1)
				{
					for (int j = 0; j < DataList.length; j++)
					{
						DataList[j].dataStr = String.valueOf(DataList[j].dataDou);
					}
				}
				dataMap.put(oid, DataList);
			}
		}
		// 第二种情况
		else
		{
			//logger.debug("进入+++++++++++++++++++++++++");
			// 首先获取端口索引
			Performance.Data[] DataList = null;
			Performance.Data[] datalist = null;
			for (int j = 0; j < oidList.size(); j++)
			{
				dataMap.put(oidList.get(j), String.valueOf(j + 1));
			}
			// begin modified by w5221 山东要求web直接与snmpGather通讯			
			DataList = SnmpGatherInterface.GetInstance().GetDataListIMDFull(dataType,
					this.device_id, "", indexOid);
			// end modified by w5221 山东要求web直接与snmpGather通讯
			String[] indexOids = new String[DataList.length];
			// 对端口索引进行处理，形成端口的数组
			if (dataType.value == 1)
			{
				for (int i = 0; i < DataList.length; i++)
				{
					indexOids[i] = String.valueOf(DataList[i].dataDou);
				}
			}
			else
			{
				for (int i = 0; i < DataList.length; i++)
				{
					indexOids[i] = DataList[i].index;
				}
			}
			// 初始化要返回的数组
			snmpData = new ArrayList();
			String[] strs = null;
			for (int i = 0; i < indexOids.length; i++)
			{
				strs = new String[oidList.size() + 1];
				// 初始化端口索引
				strs[0] = indexOids[i];
				// 获取每个性能的同一端口的数据
				String[] tmp_oids = new String[oidList.size()];
				for (int j = 0; j < oidList.size(); j++)
				{
					tmp_oids[j] = (String) oidList.get(j) + "." + DataList[i].index;
				}
				// begin modified by w5221 山东要求web直接与snmpGather通讯				
				datalist = SnmpGatherInterface.GetInstance().GetDataArrayAllIMDFull(
						device_id, "", tmp_oids, tmp_oids.length);
				// end modified by w5221 山东要求web直接与snmpGather通讯
				// 第一列是端口索引，其它列一次是各项数据，如果没有的话，会初始化为“０”
				for (int testi = 0; testi < datalist.length; testi++)
				{					
					strs[testi + 1] = datalist[testi].dataStr;
				}
				snmpData.add(strs);
			}
		}
		
		return dataMap;
	}
	
	
}