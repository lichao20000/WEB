package com.linkage.litms.common.corba.interfacecontrol;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.common.util.Log;



/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class PmeeInterface extends BaseInterface
{
	private static Logger log = LoggerFactory.getLogger(PmeeInterface.class);
	
	private static String fileName = "corba.log";

	static protected PmeeInterface Instance = null;

	private PmeeInterface()
	{
		SetProcessName("Pmee");
		InitProcessList();
	}

	static public PmeeInterface GetInstance()
	{
		if (Instance == null)
		{
			Instance = new PmeeInterface();
		}
		return Instance;
	}

	/**
	 * 功能：从后台取得pmee采集到的性能数据给客户端。
	 * 
	 * @param dataType
	 * @param ip
	 * @param name
	 * @return
	 */
	public PMEE.Data[] GetDataList(String expression_id, String device_id,
					org.omg.CORBA.StringHolder collecttime)
	{
		log.debug("begin GetDataList from web expression_id:"+expression_id+"    device_id:"+device_id+"   "+new DateTimeUtil().getLongDate());
		collecttime.value = "0";
		PMEE.Data[] pmeeData;
		GatherProcess process = null;
		HashMap deviceInfo = getDeviceInfo(device_id);
		if (deviceInfo == null)
			return new PMEE.Data[0];

		process = SearchProcessByObject(deviceInfo);
		if (process == null)
		{
			return new PMEE.Data[0];
		}
		if (process.Manager == null)
		{
			process.InitManager();
		}
        //设备IP
		//String device_ip = (String) deviceInfo.get("loopback_ip");
		if (process.Manager == null)
		{
			Log.writeLog("获取" + device_id + "性能时，采集点:"
							+ (String) deviceInfo.get("gather_id")
							+ "的性能后台为null，无法获取性能.", fileName);
			return new PMEE.Data[0];
		}
		try
		{			
			//调用后台接口全部用device_id取代原来的device_ip 20090104 gongsj
			log.debug(" begin GetDataList from PMEE expression_id:"+expression_id+"    device_id:"+device_id+"    "+new DateTimeUtil().getLongDate());
			pmeeData = ((PMEE.PMEEManager) process.Manager).GetDataList(
							expression_id, device_id, collecttime);
			log.debug(" end GetDataList from PMEE expression_id:"+expression_id+"    device_id:"+device_id+"    size:"+pmeeData.length+"     "+new DateTimeUtil().getLongDate());

		}
		catch (Exception e)
		{
			e.printStackTrace();			
			process.Manager = null;
			
			//重绑
			if (process.Manager == null)
			{
				process.InitManager();
			}
			
			try
			{			
				
				log.debug(" begin GetDataList from PMEE expression_id:"+expression_id+"    device_id:"+device_id+"    "+new DateTimeUtil().getLongDate());
				pmeeData = ((PMEE.PMEEManager) process.Manager).GetDataList(
								expression_id, device_id, collecttime);
				log.debug(" end GetDataList from PMEE expression_id:"+expression_id+"    device_id:"+device_id+"    size:"+pmeeData.length+"     "+new DateTimeUtil().getLongDate());

			}
			catch (Exception e1)
			{
				log.error("GetDataList fail expression_id:"+expression_id+"    device_id:"+device_id);
				process.Manager = null;
				pmeeData = new PMEE.Data[0];
			}
			
			
		}
		log.debug("begin GetDataList from web expression_id:"+expression_id+"    device_id:"+device_id+"   size:"+pmeeData.length+"   "+new DateTimeUtil().getLongDate());
		return pmeeData;

	}

	public void ReadAll()
	{
		for (int i = 0; i < ProcessList.size(); i++)
		{
			GatherProcess process = (GatherProcess) ProcessList.get(i);
			if (process.ProcessCount == 1 || process.ProcessCount == 0)
			{
				if (process.Manager == null)
					process.InitManager();
				try
				{
					((PMEE.PMEEManager) process.Manager).ReadAll();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					process.Manager = null;
				}
			}
			else
			{
				ArrayList subProcessList = process.GetProcessList();
				if (subProcessList == null)
					return;
				for (int k = 0; k < subProcessList.size(); k++)
				{
					GatherProcess subProcess = (GatherProcess) subProcessList
									.get(i);
					if (subProcess.Manager == null)
						subProcess.InitManager();
					try
					{
						((PMEE.PMEEManager) subProcess.Manager).ReadAll();
					}
					catch (Exception e)
					{
						e.printStackTrace();
						subProcess.Manager = null;
					}
				}
			}
		}
	}

	public int StartGatherData(String expression_id, String[] IDList)
	{		
		if (IDList.length == 0)
			return -1;
		for (int i = 0; i < IDList.length; i++)
		{
			HashMap deviceInfo = getDeviceInfo(IDList[i]);
			if (deviceInfo == null)
				continue;
			GatherProcess process = SearchProcessByObject(deviceInfo);
			if (process == null)
				continue;
			if (process.Manager == null)
			{
				process.InitManager();
			}
			try
			{
				
				//调用后台接口全部用device_id取代原来的device_ip 20090104 gongsj
				
//				String[] IpList = new String[1];
//				IpList[0] = (String) deviceInfo.get("loopback_ip");
				String[] IdList = new String[1];
				IdList[0] = IDList[i];
				
				((PMEE.PMEEManager) process.Manager).StartGatherData(expression_id, IdList);				
			}
			catch (Exception e)
			{				
				e.printStackTrace();
				process.Manager = null;
			}
		}
		return 0;

	}

	public int StopGatherData(String expression_id, String[] IDList)
	{
		if (IDList.length == 0)
			return -1;
		for (int i = 0; i < IDList.length; i++)
		{
			HashMap deviceInfo = getDeviceInfo(IDList[i]);
			if (deviceInfo == null)
				continue;
			GatherProcess process = SearchProcessByObject(deviceInfo);
			if (process == null)
				continue;
			if (process.Manager == null)
			{
				process.InitManager();
			}
			try
			{
				//String[] IpList = new String[1];
				//IpList[0] = (String) deviceInfo.get("loopback_ip");
				//((PMEE.PMEEManager) process.Manager).StopGatherData(
				//				expression_id, IpList);
				
				//调用后台接口全部用device_id取代原来的device_ip 20090104 gongsj
				String[] IdList = new String[1];
				IdList[0] = IDList[i];
				((PMEE.PMEEManager) process.Manager).StopGatherData(expression_id, IdList);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				process.Manager = null;
			}
		}
		return 0;

	}

	public int readDevices(String[] devices)
	{
		log.debug("begin readDevices "+new DateTimeUtil().getLongDate());
		if (devices.length == 0)
			return -1;
		for (int i = 0; i < devices.length; i++)
		{
			HashMap deviceInfo = getDeviceInfo(devices[i]);
			if (deviceInfo == null)
				continue;
			GatherProcess process = SearchProcessByObject(deviceInfo);
			if (process == null)
				continue;
			if (process.Manager == null)
			{
				process.InitManager();
			}
			try
			{
				//调用后台接口全部用device_id取代原来的device_ip 20090104 gongsj
				//String[] IpList = new String[1];
				//IpList[0] = (String)deviceInfo.get("loopback_ip");
				
				String[] IdList = new String[1];
				IdList[0] = devices[i];
				
				log.debug("begin readDevices to pmee  one device_id:"+IdList[0]+new DateTimeUtil().getLongDate());
				((PMEE.PMEEManager) process.Manager).ReadDevices(IdList);
				log.debug("end readDevices to pmee one device_id:"+IdList[0]+new DateTimeUtil().getLongDate());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				process.Manager = null;
				
				//重绑
				if (process.Manager == null)
				{
					process.InitManager();
				}
				try
				{
					//String[] IpList = new String[1];
					//IpList[0] = (String)deviceInfo.get("loopback_ip");
					
					//调用后台接口全部用device_id取代原来的device_ip 20090104 gongsj
					String[] IdList = new String[1];
					IdList[0] = devices[i];
					
					log.debug("begin readDevices to pmee  two device_id:"+IdList[0]+new DateTimeUtil().getLongDate());
					((PMEE.PMEEManager) process.Manager).ReadDevices(IdList);
					log.debug("end readDevices to pmee two device_id:"+IdList[0]+new DateTimeUtil().getLongDate());
				}
				catch (Exception e1)
				{
					log.error("readDevices device_ip:"+(String)deviceInfo.get("loopback_ip"));
					process.Manager = null;
					
				}
				
			}
		}
		log.debug("end readDevices "+new DateTimeUtil().getLongDate());
		return 0;
	}

	/**
	 * 批量获取设备性能指标
	 * 
	 * @param DevExpMap
	 * @return
	 */
	// public RemoteDB.PmeeBatchData[] GetBatchDataList(DeviceAndExpressionMap[]
	// DevExpMap) {

	// length:"+DevExpMap.length);
	//	  	
	// RemoteDB.PmeeBatchData[] batchData;
	// ArrayList arryBatchData = new ArrayList();
	// arryBatchData.clear();

	//	  	
	// //以gather_id为key 存放每个gather_id对应的设备对象列表
	// HashMap gatherProcess = new HashMap();
	// gatherProcess.clear();
	//	  	
	
	// try {
	//		
	// String objectID = "";
	// for (int i = 0; i < DevExpMap.length; i++) {
	// objectID = DevExpMap[i].device_id;
	
	// NetworkDevice obj = TopFrame.NCDataSource
	// .getNetworkDeviceByDeviceId_WithLock(objectID);
	//			
	// if (obj == null) {
	// continue;
	// }
	// GatherProcess process = SearchProcessByObject(obj);
	// if (process == null) {
	// continue;
	// }
	//				
	// if (process.Manager == null) {
	// process.InitManager();
	// }
	//			
	// //参数类型转换
	// PMEE.DeviceAndExpressionMap _DEM = new PMEE.DeviceAndExpressionMap();
	// //???
	// _DEM.device_ip = obj.ip;
	// _DEM.expression_list = new String[DevExpMap[i].expression_list.length];
	// for (int j = 0; j < DevExpMap[i].expression_list.length; j++) {
	// _DEM.expression_list[j] = DevExpMap[i].expression_list[j];
	// 设备"+objectID+"性能编号"+j+"："+DevExpMap[i].expression_list[j]);
	// }
	//			
	// if(!gatherProcess.containsKey(process.GatherId)) {
	// ArrayList devArray = new ArrayList();
	// devArray.clear();
	// //把process放在第一位
	// devArray.add(process);
	// devArray.add(_DEM);
	// gatherProcess.put(process.GatherId, devArray);
	// }else {//把设备追加到列表后
	// ((ArrayList)gatherProcess.get(process.GatherId)).add(_DEM);
	// 向Map中key值为;"+process.GatherId+"的设备列表追加设备："+_DEM.device_ip);
	// process = null;
	// }
	//			
	// }
	//		
	// }catch(Exception e){
	// e.printStackTrace();
	//			
	// }
	//
	//		
	// try {
	// Iterator it = gatherProcess.entrySet().iterator();
	// while(it.hasNext()) {
	// Map.Entry entry = (Map.Entry)it.next();
	// ArrayList temArray = (ArrayList)entry.getValue();
	//				
	// GatherProcess temProcess = (GatherProcess)temArray.get(0);
	// temArray.remove(0);
	// temArray.trimToSize();
	//				
	// PMEE.DeviceAndExpressionMap[] _DEM = new
	// PMEE.DeviceAndExpressionMap[temArray.size()];
	// temArray.toArray(_DEM);
	//				
	// 采集点"+temProcess.GatherId+"需获取数据的设备长度为："+_DEM.length);
	//				
	// PMEE.BatchData[] pmeeData = null;
	//				
	// try {
	// pmeeData = ((PMEE.PMEEManager)
	// temProcess.Manager).GetBatchDataList(_DEM);
	// }catch(Exception e) {
	// e.printStackTrace();
	// temProcess.Manager = null;
	// }
	//				
	// if(pmeeData == null)
	// continue;
	//				
	// for (int i = 0; i < pmeeData.length; i++) {
	// if(pmeeData[i]==null)
	// continue;
	// RemoteDB.PmeeBatchData _PBD = new RemoteDB.PmeeBatchData();
	// _PBD.expression_id = pmeeData[i].expression_id;
	// _PBD.device_id = pmeeData[i].device_id;
	// _PBD.device_ip = pmeeData[i].device_ip;
	// _PBD.collecttime = pmeeData[i].collecttime;
	// _PBD.data_list = new RemoteDB.PmeeData[pmeeData[i].data_list.length];
	//					
	// 获取设备"+_PBD.device_id+"/"+_PBD.device_ip+"/"+_PBD.collecttime+"/"+_PBD.expression_id+"的数据为："+_PBD.data_list.length);
	//					
	// for (int j = 0; j < pmeeData[i].data_list.length; j++) {
	// _PBD.data_list[j] = new RemoteDB.PmeeData();
	// _PBD.data_list[j].index = pmeeData[i].data_list[j].index;
	// _PBD.data_list[j].desc = pmeeData[i].data_list[j].desc;
	// _PBD.data_list[j].value = pmeeData[i].data_list[j].value;
	// "+j+"#"+_PBD.data_list[j].index+"#"+_PBD.data_list[j].desc+"#"+_PBD.data_list[j].value);
	// }
	//					
	// arryBatchData.add(_PBD);
	// }
	// }
	//
	// gatherProcess = null;
	//			
	// arryBatchData.trimToSize();
	// batchData = new RemoteDB.PmeeBatchData[arryBatchData.size()];
	// arryBatchData.toArray(batchData);
	//			
	// return batchData;
	// } catch (Exception e) {
	// e.printStackTrace();
	// batchData = new RemoteDB.PmeeBatchData[0];
	// }
	//		
	// return batchData;
	// }
}