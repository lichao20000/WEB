package com.linkage.litms.webtopo;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.litms.webtopo.common.DeviceCommonOperation;

public class Dev_pollInit {
	HttpSession session = null;
	Object[] dev_Object = null;
	ArrayList list_serial = null;
	HashMap map_devList = null;
	HashMap map_TypeName = null;
	
	public Dev_pollInit(HttpServletRequest request) {
		session = request.getSession();
		DeviceCommonOperation userDevModal = new DeviceCommonOperation(session);
		dev_Object = userDevModal.getUserDevModal();
		list_serial = (ArrayList)dev_Object[0];
		map_devList = (HashMap)dev_Object[1];
		map_TypeName = userDevModal.getTypeNameMap();
	}
	
	public ArrayList get_DevSerialList() {
		
		return list_serial;
	}
	
	public HashMap get_DevList() {
		return map_devList;
	}
	
	public HashMap getTypeNameMap() {
		return map_TypeName;
	}
}
