package com.linkage.litms.webtopo.snmpgather;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.vipms.flux.FluxPortInfo;
import com.linkage.litms.vipms.flux.PortJudgeAttr;

public class ReadFluxConfigPortInfo extends DeviceCommonData implements
				InterfaceReadInfo
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ReadFluxConfigPortInfo.class);
	private ArrayList oidList = null;

	private String serial = null;

	private int type = 1;

	public ReadFluxConfigPortInfo()
	{

	}

	// 这是要采集的基本性能的oid
	public void setOidList(ArrayList oidList)
	{
		this.oidList = oidList;
	}

	// 设置设备型号
	public void setSerial(String serial)
	{
		this.serial = serial;
	}

	// 设置来源（网络拓扑图、VPN拓扑图）
	public void setType(int _type)
	{
		this.type = _type;
	}

	// 获取从设备实时采集性能的值
	public ArrayList getDeviceInfo()
	{
		HashMap valueMap = new HashMap();
		ArrayList retList = new ArrayList();

		SnmpDataGather sdg = null;
		sdg = new SnmpDataGather();
		sdg.setDevice(this.device_id);

		// 首先做特殊判断,对HiX5300
		if (serial.compareTo("") == 0)
		{

		}
		// 针对MA5100做特殊判断
		else if (serial.compareTo("") == 0)
		{

		}
		// 针对ut900做特殊处理
		else if (serial.compareTo("") == 0)
		{

		}
		else
		{
			logger.debug("wp_ReadFluxConfigPortInfo_getDeviceInfo:diaoyong");
			int oidLen = oidList.size();
			logger.debug("oidList len is:" + oidList.size());
			PortJudgeAttr pja = null;
			// 针对里面每一个进行处理
			ArrayList list = new ArrayList();
			HashMap dataMap = null;
			Performance.Data[] DataList = null;
			FluxPortInfo fpi = null;
			// begin modified by w5221 20071012 有些端口根据端口ip
			// OID采到的端口，但是根据端口索引就采不到了
			for (int i = 0; i < oidLen; i++)
			{
				list.clear();
				pja = (PortJudgeAttr) oidList.get(i);
				list.add(pja.getValue());
				sdg.setOidData(list);
				dataMap = sdg.getDataList();
				//logger.debug("wp_dataLength:" + dataMap.size());

				// 没有采集到数据的情况
				if (dataMap.size() == 0)
				{
					continue;
				}
				
				 //处理过程
				 DataList = (Performance.Data[]) dataMap.get((String) list.get(0));
				 for(int j=0;j<DataList.length;j++)
				 {
					 if(pja.getName().equals("ifindex"))
					 {
						 fpi=new FluxPortInfo();
						 fpi.setIfindex(DataList[j].index);
						 valueMap.put(DataList[j].index,fpi);
					 }
					 else if(!pja.getName().equals("ifportip"))
					 {
						 //只处理索引OID采到的数据
						 if(null!=valueMap.get(DataList[j].index))
						 {
							 fpi = (FluxPortInfo)valueMap.get(DataList[j].index);
							 if(pja.getName().equals("ifdescr"))
							 {
								 fpi.setIfdescr(DataList[j].dataStr);
							 }
							 else if(pja.getName().equals("ifname"))
							 {
								 fpi.setIfname(DataList[j].dataStr);
							 }
							 else if(pja.getName().equals("ifnamedefined"))
							 {
								 fpi.setIfnamedefined(DataList[j].dataStr);
							 }
						 }
					 }
					 else if(pja.getName().equals("ifportip"))
					 {
						 //只处理索引OID采到的数据
						 if(null!=valueMap.get(DataList[j].dataStr))
						 {
							 fpi=(FluxPortInfo)valueMap.get(DataList[j].dataStr);
							 fpi.setIfportip(DataList[j].index);
						 }
					 }
				 }		 

			}
			// for(int i=0;i<oidLen;i++)
			// {
			// list.clear();
			// pja=(PortJudgeAttr)oidList.get(i);
			// //如果是端口索引的话，不进行处理
			// if(pja.getName().compareTo("ifindex")==0)
			// {
			// continue;
			// }
			// list.add(pja.getValue());
			// sdg.setOidData(list);
			// dataMap = sdg.getDataList();
			// logger.debug("wp_dataLength:"+dataMap.size());
			//        
			// //没有采集到数据的情况
			// if(dataMap.size()==0)
			// {
			// continue;
			// }
			//        
			// 
			// //处理过程
			// DataList = (Data[]) dataMap.get((String) list.get(0));
			//        
			//
			// //logger.debug(DataList.length);
			//
			// //如果是端口ip的话，需要做特殊处理
			// if(pja.getName().compareTo("ifportip")!=0)
			// {
			// for(int j=0;j<DataList.length;j++)
			// {
			// if(valueMap.get(DataList[j].index)==null)
			// {
			// fpi=new FluxPortInfo();
			// fpi.setIfindex(DataList[j].index);
			// }
			// else
			// {
			// fpi=(FluxPortInfo)valueMap.get(DataList[j].index);
			// }
			//
			// if(pja.getName().compareTo("ifdescr")==0)
			// {
			// fpi.setIfdescr(DataList[j].dataStr);
			// }
			// else if(pja.getName().compareTo("ifname")==0)
			// {
			// fpi.setIfname(DataList[j].dataStr);
			// }
			// else if(pja.getName().compareTo("ifnamedefined")==0)
			// {
			// fpi.setIfnamedefined(DataList[j].dataStr);
			// }
			// valueMap.put(DataList[j].index,fpi);
			// }
			// }
			// else
			// {
			// for(int j=0;j<DataList.length;j++)
			// {
			// if(DataList[j].dataStr==null ||
			// DataList[j].dataStr.indexOf("-")==0)
			// {
			// continue;
			// }
			// if(valueMap.get(DataList[j].dataStr)==null)
			// {
			// fpi=new FluxPortInfo();
			// fpi.setIfindex(DataList[j].dataStr);
			// }
			// else
			// {
			// fpi=(FluxPortInfo) valueMap.get(DataList[j].dataStr);
			// }
			// fpi.setIfportip(DataList[j].index);
			// valueMap.put(DataList[j].dataStr,fpi);
			// }
			// }
			//        
			// }
			// end modified by w5221 20071012 有些端口根据端口ip OID采到的端口，但是根据端口索引就采不到了
			retList.addAll(valueMap.values());

		}

		return retList;
	}
}