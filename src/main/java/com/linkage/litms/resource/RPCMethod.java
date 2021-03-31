package com.linkage.litms.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;

import ACS.DevRpc;
import ACS.Rpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.acs.soap.io.XML;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.GetRPCMethods;
import com.linkage.litms.acs.soap.service.GetRPCMethodsResponse;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;


/**
 * 
 * <P>Linkage Communication Technology Co., Ltd<P>
 * <P>Copyright 2005-2007. All right reserved.<P>
 * @version 1.0.0 2007-6-16
 * @author Linkage
 * Modify Record:
 * 2007-06-16  Alex.Yan (yanhj@lianchuang.com)
 *             RemoteDB ACS.   
 */
public class RPCMethod {
	public Map GetRPCMethods(HttpServletRequest request) {

		String device_id = request.getParameter("device_id");

		DevRpc[] devRPCArr = new DevRpc[1];
		String strSQL1 = "select *  from tab_gw_device where device_id='"+ device_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL1 = "select oui, device_serialnumber, gw_type from tab_gw_device where device_id='"+ device_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(strSQL1);
		psql.getSQL();
		Cursor cursor1 = DataSetBean.getCursor(strSQL1);
		Map fields1 = cursor1.getNext();
		String out = (String) fields1.get("oui");
		String SerialNumber = (String) fields1.get("device_serialnumber");
		// 终端类型
		String gw_type = (String)fields1.get("gw_type");
		Map rpcmethodMap = new HashMap();
		rpcmethodMap.put("oui", out);
		rpcmethodMap.put("SerialNumber", SerialNumber);
		
		String[] stringArr = new String[1];
		GetRPCMethods getRPCMethods = new GetRPCMethods();
		stringArr[0] = getRPCMethods.toRPC();

		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "GetRPCMethods";
		rpcArr[0].rpcValue = getRPCMethods.toRPC();
		devRPCArr[0].rpcArr = rpcArr;
		// corba
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr,Global.RpcCmd_Type);

		if ((devRPCRep != null) && devRPCRep.size()!=0 && devRPCRep.get(0)!= null) {
			if (devRPCRep.get(0).getStat()!= 1){
				rpcmethodMap.put("rpcArr", null);
			}
			else{
				SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(devRPCRep.get(0).getRpcList().get(0).getValue()));
		        if (soapOBJ == null) {
		        	return null;
		        }
				GetRPCMethodsResponse getRPCMethodsResponse = XmlToRpc.GetRPCMethodsResponse(soapOBJ.getRpcElement());
				rpcmethodMap.put("rpcArr", getRPCMethodsResponse.getMethodList());
			}
		}
		else{
			rpcmethodMap.put("rpcArr", null);
		}
		
		return rpcmethodMap;
	}
}
