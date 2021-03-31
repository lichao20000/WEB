package com.linkage.module.itms.service.bio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.service.dao.ManuallyConfigurePortBusinessDAO;

public class ManuallyConfigurePortBusinessBIO {
	private ManuallyConfigurePortBusinessDAO dao;

	public int qyeryLoid(String loid) {
		return dao.qyeryLoid(loid);
	}

	public ManuallyConfigurePortBusinessDAO getDao() {
		return dao;
	}

	public void setDao(ManuallyConfigurePortBusinessDAO dao) {
		this.dao = dao;
	}

	public int wlanNum(String loid) {
		return dao.wlanNum(loid);
	}

	public int lanNum(String loid) {
		return dao.lanNum(loid);
	}

	public List<Map> internetList(String loid) {
		return dao.internetList(loid);
	}

	public List<Map> itvList(String loid) {
		return dao.itvList(loid);
	}

	public List<Map> tianyiList(String loid) {
		return dao.tianyiList(loid);
	}

	public int update(String id, String loid, String val, int wlanNum,
			int lanNum, String userid, String username) {
		Map<String, String> map = new HashMap<String, String>();
		int lan_Num = 0;
		for (int i = 0; i < lanNum; i++) {
			map.put("LAN" + (i + 1),
					"InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig."
							+ (i + 1));
		}
		for (int i = 0; i < wlanNum; i++) {
			map.put("WLAN" + (i + 1),
					"InternetGatewayDevice.LANDevice.1.WLANConfiguration."
							+ (i + 1));
		}
		String tempVal = "";
		if (!StringUtil.IsEmpty(val)) {
			String[] str = val.split("@");
			StringBuffer temp = new StringBuffer();
			for (int i = 0; i < str.length; i++) {
				if (map.get(str[i]) != null) {
					temp.append(map.get(str[i]) + ",");
				}
			}
			tempVal = temp.substring(0, temp.length() - 1);

			String[] bind_port = tempVal.split(",");
			for (int i = 0; i < bind_port.length; i++) {
				if (bind_port[i].length() == 62) {
					lan_Num++;
				}
			}
		} else {
			tempVal = "";
		}
		return dao.update(id, loid, tempVal, lan_Num, userid, username);
	}

	public List<HashMap<String, String>> getInternet(String deviceId) {
		return dao.getInternet(deviceId);
	}

	public List<HashMap<String, String>> getIPTV(String deviceId) {
		return dao.getIPTV(deviceId);
	}

	public List<HashMap<String, String>> getTianyi(String deviceId) {
		return dao.getTianyi(deviceId);
	}
	
	public String getUserName(String tempUserid,
			String vlId) {
		return dao.getUserName(tempUserid,vlId);
	}
	
	public int realwlanNum(String loid) {
		return dao.realwlanNum(loid);
	}

	public int reallanNum(String loid) {
		return dao.reallanNum(loid);
	}


}
