package com.linkage.litms.webtopo.snmpgather;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * <p>
 * Title:用来读取设备系统信息
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author smf
 * @version 1.0
 */
public class ReadDeviceInfo extends DeviceCommonData implements
	InterfaceReadInfo {

    public ReadDeviceInfo() {
	this.type = 1;
    }

    public ArrayList getDeviceInfo() {
	ArrayList mydata = new ArrayList();
	SnmpDataGather sdg = new SnmpDataGather();
	sdg.setDevice(this.device_id);
	String[][] m_strOID = { { "1.3.6.1.2.1.1.1", "系统描述信息" },
		{ "1.3.6.1.2.1.1.2", "系统sysObjectID" },
		{ "1.3.6.1.2.1.47.1.1.1.1.9", "硬件版本" },
		{ "1.3.6.1.2.1.47.1.1.1.1.10", "软件版本" },
		{ "1.3.6.1.2.1.47.1.1.1.1.13", "设备型号" },
		{ "1.3.6.1.2.1.1.3", "系统活动的时间" }, { "1.3.6.1.2.1.1.4", "联系人" },
		{ "1.3.6.1.2.1.1.5", "系统名字" }, { "1.3.6.1.2.1.1.64", "所在位置" },
		{ "1.3.6.1.2.1.4.20.1.1", "设备的ip地址" } };

	ArrayList list = new ArrayList();
	HashMap map = new HashMap();
	for (int i = 0; i < m_strOID.length; i++) {
	    list.add(m_strOID[i][0]);
	    map.put(m_strOID[i][0], m_strOID[i][1]);
	}
	sdg.setOidData(list);
	HashMap dataMap = sdg.getDataList();

	// 处理过程
	Performance.Data[] DataList = null;
	String[] str = null;
	for (int i = 0; i < list.size(); i++) {
	    str = new String[2];
	    str[0] = (String) map.get((String) list.get(i));
	    DataList = (Performance.Data[]) dataMap.get((String) list.get(i));
	    if (DataList != null) {
		if (DataList.length == 1) {
		    str[1] = DataList[0].dataStr;
		} else if (DataList.length == 0) {
		    str[1] = "";
		} else {
		    if ("硬件版本".equals(str[0]) || "软件版本".equals(str[0]) || "设备型号".equals(str[0])) {
			str[1] = DataList[0].dataStr;
		    } else {
			str[1] = "";
			for (int j = 0; j < DataList.length; j++) {
			    if (!DataList[j].dataStr.equals(""))
				str[1] += DataList[j].dataStr + "<br>";
			    else
				str[1] += "";
			}
		    }
		}
	    } else {
		str[1] = "";
	    }
	    mydata.add(str);
	}
	return mydata;

    }

}