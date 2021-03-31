package com.linkage.litms.webtopo.snmpgather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Performance.Data;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterface;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

public class ReadDeviceStatus
    extends DeviceCommonData
    implements InterfaceReadInfo {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ReadDeviceStatus.class);
  public ReadDeviceStatus() {
    this.type=2;

  } 

  public ArrayList getDeviceInfo() {
    String[][] m_strOID = {
        {
        "1.3.6.1.2.1.2.2.1.1", "索引"}
        , {
        "1.3.6.1.2.1.2.2.1.2", "端口描述"}
        , {
        "1.3.6.1.2.1.2.2.1.3", "端口类型"}
        , {
        "1.3.6.1.2.1.2.2.1.4", "最大传输单元"}
        , {
        "1.3.6.1.2.1.2.2.1.5", "端口速度"}
        , {
        "1.3.6.1.2.1.2.2.1.6", "物理地址"}
        , {
        "1.3.6.1.2.1.2.2.1.7", "管理状态"}
        , {
        "1.3.6.1.2.1.2.2.1.8", "运行状态"}
        ,{
        "1.3.6.1.2.1.31.1.1.1.1","端口名字"}
        ,{
        "1.3.6.1.2.1.31.1.1.1.18","端口别名"
        }
    };
    ArrayList list = new ArrayList(); 
    HashMap map = new HashMap();
    //增加了端口IP列
    String[] strs=new String[m_strOID.length+3];

    for (int i = 0; i < m_strOID.length; i++) {
      if (i > 0) {
        list.add(m_strOID[i][0]);
      }
      map.put(m_strOID[i][0], m_strOID[i][1]);
      strs[i]= m_strOID[i][1];
    }
    strs[m_strOID.length]="端口IP";
    strs[m_strOID.length+1]="子网掩码";
    strs[m_strOID.length+2]="端口描述";

    //SnmpDataGather sdg = new SnmpDataGather(account, passwd);
    SnmpDataGather sdg = new SnmpDataGather();
    sdg.setDevice(this.device_id);
    sdg.setIndexData("1.3.6.1.2.1.2.2.1.1");
    sdg.setOidData(list);
    //获取到ｏｉｄ和列之间的ｈａｓｈＭａｐ
    sdg.getDataList();
    ArrayList snmpdata = sdg.getSnmpData();
    logger.debug("base info is over.......");

    //增加获取端口的信息
    list.clear();
    
    //获取IP信息
    list.add("1.3.6.1.2.1.4.20.1.2");
    sdg.setIndexData("");
    
    sdg.setOidData(list);
   
    HashMap dataMap = sdg.getDataList();
    Performance.Data[] DataList = null;
    DataList = ( Performance.Data[]) dataMap.get( (String) list.get(0));
    HashMap portipMap=new HashMap();
    HashMap portNetMaskMap = new HashMap();    
   
    for(int i=0;i<DataList.length;i++)
    {
      if(!portipMap.containsKey(DataList[i].dataStr))
      {
    	  //key:端口索引，value：端口IP
    	  portipMap.put(DataList[i].dataStr,DataList[i].index);    	  
      }
    }
    
    
    //采集子网掩码 
    list.clear();
    list.add("1.3.6.1.2.1.4.20.1.3");
    sdg.setIndexData("");    
    sdg.setOidData(list);
    dataMap = sdg.getDataList();
    DataList=( Performance.Data[]) dataMap.get( (String) list.get(0));
    for(int i=0;i<DataList.length;i++)
    {
//    	logger.debug("begin:"+DataList[i].index+"  "+DataList[i].dataStr);
    	if(!"".equals(DataList[i].dataStr)&&!"NODATA".equalsIgnoreCase(DataList[i].dataStr)
    			&&!"ERROR".equalsIgnoreCase(DataList[i].dataStr))
    	{
    		portNetMaskMap.put(DataList[i].index,DataList[i].dataStr);
    	}
    } 
    
    //采集设备端口数
    String oid="1.3.6.1.2.1.2.1.0";
    String portNum="0";
    Data portNumData=SnmpGatherInterface.GetInstance().getDataIMDFull(this.device_id,oid);
    if(!"".equalsIgnoreCase(portNumData.dataStr)&&!"NODATA".equalsIgnoreCase(portNumData.dataStr)
    		&&!"ERROR".equalsIgnoreCase(portNumData.dataStr))
    {
    	if(oid.equalsIgnoreCase(portNumData.index))
    	{
    		portNum=portNumData.dataStr;
    	}
    }    
    
    //将端口IP放入列表
    ArrayList retdata=new ArrayList();
    String[] retStr=null;

    //将端口的信息初始化清楚
    HashMap portMap=new HashMap();
    portMap.put("1","Up");
    portMap.put("2","Down");
    portMap.put("3","Testing");
    //端口描述名称
    Map<String, String> portNameMap = new HashMap<String, String>();
    String tempSql = "select port_name, port_desc from tab_port_name_map where device_model_id=(select device_model_id from tab_gw_device where device_id='"+device_id+"')";
    PrepareSQL psql = new PrepareSQL(tempSql);
	psql.getSQL();
    Cursor cursor = DataSetBean.getCursor(tempSql);
    Map tempMap = cursor.getNext();
    while (null != tempMap) {
    	portNameMap.put((String)tempMap.get("port_name"), (String)tempMap.get("port_desc"));
    	tempMap = cursor.getNext();
    }
    //端口类型
    Map<String, String> portTypeMap = new HashMap<String, String>();
    portTypeMap.put("1", "other");
    portTypeMap.put("2", "regular1822");
    portTypeMap.put("3", "hdh1822");
    portTypeMap.put("4", "ddn-x25");
    portTypeMap.put("5", "rfc877-x25");
    portTypeMap.put("6", "ethernet-csmacd");
    portTypeMap.put("7", "iso88023-csmacd");
    portTypeMap.put("8", "iso88024-tokenBus");
    portTypeMap.put("9", "iso88025-tokenRing");
    portTypeMap.put("10", "iso88026-man");
    portTypeMap.put("11", "starLan");
    portTypeMap.put("12", "proteon-10Mbit");
    portTypeMap.put("13", "proteon-80Mbit");
    portTypeMap.put("14", "hyperchannel");
    portTypeMap.put("15", "fddi");
    portTypeMap.put("16", "lapb");
    portTypeMap.put("17", "sdlc");
    portTypeMap.put("18", "ds1");
    portTypeMap.put("19", "e1");
    portTypeMap.put("20", "basicISDN");
    portTypeMap.put("21", "primaryISDN");
    portTypeMap.put("22", "propPointToPointSerial");
    portTypeMap.put("23", "ppp");
    portTypeMap.put("24", "softwareLoopback");
    portTypeMap.put("25", "eon");
    portTypeMap.put("26", "ethernet-3Mbit");
    portTypeMap.put("27", "nsip");
    portTypeMap.put("28", "slip");
    portTypeMap.put("29", "ultra");
    portTypeMap.put("30", "ds3");
    portTypeMap.put("31", "sip");
    portTypeMap.put("32", "frame-relay");
    
    String[] temp_strs=null;
    String portip="";
    String portName = "";
    for(int i=0;i<snmpdata.size();i++)
    {
      retStr=new String[m_strOID.length+3];
      temp_strs=(String[]) snmpdata.get(i);
      //端口描述
      //temp_strs[1]=portNameMap.get(temp_strs[1])==null?temp_strs[1]:portNameMap.get(temp_strs[1]);
      //端口类型
      temp_strs[2]=portTypeMap.get(temp_strs[2])==null?temp_strs[2]:portTypeMap.get(temp_strs[2]);
      temp_strs[6]=portMap.get(temp_strs[6])==null?"":(String)portMap.get(temp_strs[6]);
      temp_strs[7]=portMap.get(temp_strs[7])==null?"":(String)portMap.get(temp_strs[7]);
      //temp_strs[13]=portNameMap.get(temp_strs[1])==null?temp_strs[1]:portNameMap.get(temp_strs[1]);
      int j=0;
      for(j=0;j<temp_strs.length;j++)
      {
        retStr[j]=temp_strs[j];
      }
      portip=(portipMap.get(temp_strs[0])!=null)?(String)portipMap.get(temp_strs[0]):"";
      retStr[j]=portip;
      
      //有端口IP的情况下
      if(!"".equals(retStr[j]))
      {
    	  retStr[j+1]= (portNetMaskMap.get(retStr[j])!=null)?(String)portNetMaskMap.get(retStr[j]):"";
      }
      else
      {
    	  retStr[j+1]="";
      }
      portName = portNameMap.get(temp_strs[1])==null?temp_strs[1]:portNameMap.get(temp_strs[1]);
      retStr[j+2]= portName;
      
//      logger.debug("end:"+retStr[j]+"   "+retStr[j+1]);
      retdata.add(retStr);
 
    }
    //snmpdata.clear();
    ArrayList returnData=new ArrayList();
    returnData.add(strs);
    returnData.addAll(retdata);
//    logger.debug("port_size:"+snmpdata.size());
//    logger.debug("size:"+returnData.size());
    
    //returnData.add(portNum);
    return returnData;
  }
 
  /**
   * 获得ADSL上行的数据
   * @return
   */
  public ArrayList getDeviceInfo_adsl() {
	    String[][] m_strOID = {
	        {
	        "1.3.6.1.2.1.2.2.1.1", "ADSL上行实际速率"}
	        , {
	        "1.3.6.1.2.1.2.2.1.2", "ADSL下行实际速率"}
	        , {
	        "1.3.6.1.2.1.2.2.1.3", "PVC状态"}
	        , {
	        "1.3.6.1.2.1.2.2.1.4", "是否在线(PPPoE)"}
	        , {
	        "1.3.6.1.2.1.2.2.1.5", "账号(PPPoE)"}
	    };
	    ArrayList list = new ArrayList(); 
	    HashMap map = new HashMap();
	    //增加了端口IP列
	    String[] strs=new String[m_strOID.length];
	    logger.debug("strs-------------:" + strs);
	    
	    for (int i = 0; i < m_strOID.length; i++) {
	      if (i > 0) {
	        list.add(m_strOID[i][0]);
	      }
	      map.put(m_strOID[i][0], m_strOID[i][1]);
	      strs[i]= m_strOID[i][1];
	      logger.debug("strs["+i+"]-------------:" + strs[i]);
	    }
	    SnmpDataGather sdg = new SnmpDataGather();
	    sdg.setDevice(this.device_id);
	    sdg.setIndexData("1.3.6.1.2.1.2.2.1.1");
	    sdg.setOidData(list);
	    //获取到oid和列之间的HashMap
	    sdg.getDataList();
	    ArrayList snmpdata = sdg.getSnmpData();
	    //logger.debug("base info is over-------------:" + snmpdata);
	   
	    //增加获取端口的信息
	    list.clear();
	    
	    //将端口IP放入列表
	    ArrayList retdata=new ArrayList();
	    String[] retStr=null;

	    //将端口的信息初始化清楚
	    HashMap portMap=new HashMap();
	    portMap.put("1","Up");
	    portMap.put("2","Down");
	    portMap.put("3","Testing");
	   
	    //端口类型
	    Map<String, String> portTypeMap = new HashMap<String, String>();
	    portTypeMap.put("1", "other");
	    portTypeMap.put("2", "regular1822");
	    portTypeMap.put("3", "hdh1822");
	    
	    
	    String[] temp_strs=null;
	    for(int i=0;i<snmpdata.size();i++)
	    {
	      retStr=new String[m_strOID.length];
	      logger.debug("retStr-------------:" + retStr);
	      
	      temp_strs=(String[]) snmpdata.get(i);
	      //端口类型
	      temp_strs[2]=portTypeMap.get(temp_strs[2])==null?temp_strs[2]:portTypeMap.get(temp_strs[2]);
	      int j=0;
	      for(j=0;j<temp_strs.length;j++)
	      {
	        retStr[j]=temp_strs[j];
	        logger.debug("temp_strs["+j+"]-------------:" + temp_strs[j]);
	      }
	      
//	      logger.debug("end:"+retStr[j]+"   "+retStr[j+1]);
	      retdata.add(retStr);
	 
	    }
	    //snmpdata.clear();
	    ArrayList returnData=new ArrayList();
	    returnData.add(strs);
	    returnData.addAll(retdata);
//	    logger.debug("port_size:"+snmpdata.size());
//	    logger.debug("size:"+returnData.size());
	    
	    //returnData.add(portNum);
	    return returnData;
	  }
  
  /**
   * 获得PPPoE的账号和是否在线的信息
   * @return
   */
  public ArrayList getDeviceInfo_PPPoE() {
	    String[][] m_strOID = {
	        {
	        "1.3.6.1.2.1.2.2.1.1", "IP地址"}
	        , {
	        "1.3.6.1.2.1.2.2.1.2", "子网掩码"}
	        , {
	        "1.3.6.1.2.1.2.2.1.3", "默认网关"}
	        , {
	        "1.3.6.1.2.1.2.2.1.4", "DNS"}
	        , {
	        "1.3.6.1.2.1.2.2.1.5", "DHCP服务状态"}
	    };
	    ArrayList list = new ArrayList(); 
	    HashMap map = new HashMap();
	    //增加了端口IP列
	    String[] strs=new String[m_strOID.length];

	    for (int i = 0; i < m_strOID.length; i++) {
	      if (i > 0) {
	        list.add(m_strOID[i][0]);
	      }
	      map.put(m_strOID[i][0], m_strOID[i][1]);
	      strs[i]= m_strOID[i][1];
	    }
	    SnmpDataGather sdg = new SnmpDataGather();
	    sdg.setDevice(this.device_id);
	    sdg.setIndexData("1.3.6.1.2.1.2.2.1.1");
	    sdg.setOidData(list);
	    //获取到oid和列之间的HashMap
	    sdg.getDataList();
	    ArrayList snmpdata = sdg.getSnmpData();

	    //增加获取端口的信息
	    list.clear();
	    
	    //将端口IP放入列表
	    ArrayList retdata=new ArrayList();
	    String[] retStr=null;

	    //将端口的信息初始化清楚
	    HashMap portMap=new HashMap();
	    portMap.put("1","Up");
	    portMap.put("2","Down");
	    portMap.put("3","Testing");
	   
	    //端口类型
	    Map<String, String> portTypeMap = new HashMap<String, String>();
	    portTypeMap.put("1", "other");
	    portTypeMap.put("2", "regular1822");
	    portTypeMap.put("3", "hdh1822");
	    
	    
	    String[] temp_strs=null;
	    for(int i=0;i<snmpdata.size();i++)
	    {
	      retStr=new String[m_strOID.length];
	      temp_strs=(String[]) snmpdata.get(i);
	      //端口类型
	      temp_strs[2]=portTypeMap.get(temp_strs[2])==null?temp_strs[2]:portTypeMap.get(temp_strs[2]);
	      int j=0;
	      for(j=0;j<temp_strs.length;j++)
	      {
	        retStr[j]=temp_strs[j];
	      }
	      
//	      logger.debug("end:"+retStr[j]+"   "+retStr[j+1]);
	      retdata.add(retStr);
	 
	    }
	    //snmpdata.clear();
	    ArrayList returnData=new ArrayList();
	    returnData.add(strs);
	    returnData.addAll(retdata);
//	    logger.debug("port_size:"+snmpdata.size());
//	    logger.debug("size:"+returnData.size());
	    
	    //returnData.add(portNum);
	    return returnData;
	  }
}