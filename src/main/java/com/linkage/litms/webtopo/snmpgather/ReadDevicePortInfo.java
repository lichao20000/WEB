package com.linkage.litms.webtopo.snmpgather;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.Data;

public class ReadDevicePortInfo extends DeviceCommonData implements
        InterfaceReadInfo {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ReadDevicePortInfo.class);
    private int type = 0;

    public ReadDevicePortInfo() {

    }

    public void setValueType(int type) {
        this.type = type;

    }

    public ArrayList getDeviceInfo() {
        ArrayList mydata = new ArrayList();
        ;
        //SnmpDataGather sdg = new SnmpDataGather(account, passwd);
        SnmpDataGather sdg = new SnmpDataGather();
        sdg.setDevice(this.device_id);
        String[][] m_strOID = { { "1.3.6.1.2.1.2.2.1.1", "端口索引" },
                { "1.3.6.1.2.1.2.2.1.2", "端口描述" },
                { "1.3.6.1.2.1.31.1.1.1.1", "端口名字" },
                { "1.3.6.1.2.1.4.20.1.2", "端口IP" },
                { "1.3.6.1.2.1.31.1.1.1.18", "端口别名" } };

        ArrayList list = new ArrayList();
        HashMap map = new HashMap();

        list.add(m_strOID[type][0]);
        map.put(m_strOID[type][0], m_strOID[type][1]);

        sdg.setOidData(list);
        HashMap dataMap = sdg.getDataList();

        // 处理过程
        Data[] DataList = null;
        DataList = (Data[]) dataMap.get((String) list.get(0));
        switch (type) {
        case 3: {
            for (int i = 0; i < DataList.length; i++) {
                mydata.add(DataList[i].index);
            }
            break;
        }
        case 0:
        case 1:
        case 2:
        case 4: {
            for (int i = 0; i < DataList.length; i++) {
                mydata.add(DataList[i].dataStr);
            }
            break;
        }
        }
        return mydata;
    }

    /**
     * 通过snmp获取端口数据
     * 
     * @author Hemc
     * @date 2006-12-8
     * @return 返回ArrayList（List中以数组存放端口信息）
     */
    public ArrayList getDevicePortInfo() {
        String[][] m_strOID = { { "1.3.6.1.2.1.2.2.1.1", "索引" },
                { "1.3.6.1.2.1.2.2.1.2", "端口描述" },
                { "1.3.6.1.2.1.31.1.1.1.1", "端口名字" },
                { "1.3.6.1.2.1.31.1.1.1.18", "端口别名" } };
        ArrayList list = new ArrayList();
        HashMap map = new HashMap();
        // 增加了端口IP列
        // String[] strs=new String[m_strOID.length+1];
        for (int i = 0; i < m_strOID.length; i++) {
            if (i > 0) {
                list.add(m_strOID[i][0]);
            }
            map.put(m_strOID[i][0], m_strOID[i][1]);
        }
        //SnmpDataGather sdg = new SnmpDataGather(account, passwd);
        SnmpDataGather sdg = new SnmpDataGather();
        sdg.setDevice(this.device_id);
        sdg.setIndexData("1.3.6.1.2.1.2.2.1.1");
        sdg.setOidData(list);
        // 获取到ｏｉｄ和列之间的ｈａｓｈＭａｐ
        sdg.getDataList();
        ArrayList snmpdata = sdg.getSnmpData();
        logger.debug("base info is over.......");

        // 增加获取端口的信息
        list.clear();
        list.add("1.3.6.1.2.1.4.20.1.2");
        sdg.setIndexData("");
        sdg.setOidData(list);
        HashMap dataMap = sdg.getDataList();
        Data[] DataList = null;
        DataList = (Data[]) dataMap.get((String) list.get(0));
        HashMap portipMap = new HashMap();
        //端口IP需要特殊处理
        for (int i = 0; i < DataList.length; i++) {
            if (!portipMap.containsKey(DataList[i].dataStr)) {
                portipMap.put(DataList[i].dataStr, DataList[i].index);
            }
        }
        // 将端口IP放入列表
        ArrayList retdata = new ArrayList();
        String[] retStr = null;

        String[] temp_strs = null;
        String portip = "";
        for (int i = 0; i < snmpdata.size(); i++) {
            retStr = new String[m_strOID.length + 1];
            temp_strs = (String[]) snmpdata.get(i);
            int j = 0;
            for (j = 0; j < temp_strs.length; j++) {
                retStr[j] = temp_strs[j];
            }
            portip = (portipMap.get(temp_strs[0]) != null) ? (String) portipMap
                    .get(temp_strs[0]) : "";
            retStr[j] = portip;
            retdata.add(retStr);
        }
        if (snmpdata != null){
            logger.debug("Hemc:" + snmpdata.size());
            snmpdata.clear();
        }
        snmpdata = null;
        
        return retdata;
    }
}