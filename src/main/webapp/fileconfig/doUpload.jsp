<%--
Desc		: Communciate with WEB:upload file.
Linkage Communication Technology Co., Ltd
Copyright 2005-2007. All right reserved.
Author		: Alex.Yan (yanhj@lianchuang.com)
Version     : 1.1.0011 2007-7-17
modify record
------------------------------------------------------
desc		: file server.
author		: Alex.Yan (yanhj@lianchuang.com)
version		: V2.0.0000,2007-11-24
--%>

<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.linkage.commons.db.DBOperation"%>
<%@page import="com.linkage.commons.db.DBUtil"%>
<%@page import="com.linkage.commons.fileupload.File"%>
<%@page import="com.linkage.commons.fileupload.Upload"%>
<%@page import="com.linkage.litms.fileserver.dao.UploadDAO"%>
<%
	request.setCharacterEncoding("GBK");

	org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this
			.getClass());

	Upload fupload = new Upload();
	fupload.initialize(pageContext);
	fupload.setMaxFileSize(1024 * 1024 * 100);
	fupload.upload();

	String strPath = request.getParameter("path");
	String strRename = request.getParameter("fileRename");
	String dir_id = request.getParameter("dir_id");
	String serUser = request.getParameter("seruser");
	String serPass = request.getParameter("serpass");
	String device_model_id = request.getParameter("device_model_id");
	String devicetype_id = request.getParameter("devicetype_id");
	//--zhaixf
	String device_model = request.getParameter("device_model");
	String vendor_name = request.getParameter("vendor_name");
	String hardwareversion = request.getParameter("hardwareversion");
	String softwareversion = request.getParameter("softwareversion");
	//-----//
	String remark = request.getParameter("remark");
	String file_status = request.getParameter("file_status");
	String _tableName = request.getParameter("tablename");
	String cityList = request.getParameter("cityList");

	String area_id = request.getParameter("area_id");
	String serviceList = request.getParameter("serviceList");

	String msg = "";
	//boolean nohas = false;
	boolean isSuccess = false;
	int file_size = 0;

	String sql = "";
	Map fields = null;
	if (("tab_software_file").equals(_tableName)) {
		fields = UploadDAO.getSoftFile(strRename);
	} else {
		fields = UploadDAO.getConfFile(strRename);
	}

	int icode = Integer.parseInt((String) fields.get("num"));
	if (icode > 0) {
		msg = "文件名重复，请重新上传！";
		fields = null;
		icode = 0;
	} else {
		try {
			int count = 0;
			for (int i = 0; i < fupload.getFiles().getCount(); i++) {
				File file = fupload.getFiles().getFile(i);
				if (!file.isMissing()) {
					file_size = file.getSize();
					file.saveAs("/" + strPath + "/" + strRename, 1);
					log.debug("SaveFile:" + "/" + strPath + "/"
							+ strRename);
					count++;
				}
			}

			msg = count + " 个文件上传成功！";
			isSuccess = true;
		} catch (Exception e) {
			msg = "上传文件失败,请重新上传！";
		} finally {
			fields = null;
		}
		icode = 0;
	}

	if (isSuccess) {
		if (_tableName.equals("tab_software_file")) {
			long file_id = UploadDAO.getSoftFileMaxId();
			
			//表结构变更，tab_devicetype表中device_model->device_model_id
			String strSQL = "select device_model,device_model_id from gw_device_model a, tab_vendor b"
					+ " where a.vendor_id=b.vendor_id "
					+ " and b.vendor_name='" + vendor_name + "'";
			Map<String,String> devModelMap = UploadDAO.getMapFromList(
					DBOperation.getRecords(strSQL),"device_model","device_model_id");
//			if(devModelMap == null){
//				devModelMap = new HashMap<String,String>();
//			}
			//System.out.println("devModelMap:" + devModelMap);
			
			String[] arryDevModel = device_model
					.replace("AGELINK", " ").split(",");
			String[] arryHardware = hardwareversion.replace("AGELINK",
					" ").split(",");
			int iDevModelLen = arryDevModel.length;
			int iHardwareLen = arryHardware.length;
			
			//System.out.println("arryDevModel:" + device_model);
			//System.out.println("arryHardware:" + hardwareversion);
			
			String devicetypeId = "";
			long maxDevicetypeId = 0L;
			int ouiNum = 0;
			ArrayList<String> sqlList = new ArrayList<String>();
			ArrayList<String> sqlfileList = new ArrayList<String>();
			Map<String,String> tm = null;
			Set<String> devicetypeIdSet = null;
			ArrayList<HashMap<String,String>> tlist = null;
			ArrayList<HashMap<String,String>> vendorlist = null;

			if (DBUtil.GetDB() == 1){
				strSQL = "select vendor_id || '|' || device_model_id || '|' || hardwareversion || '|' || softwareversion as dtype,devicetype_id from tab_devicetype_info";
			}else{
				strSQL = "select vendor_id + '|' + device_model_id + '|' + hardwareversion + '|' + softwareversion as dtype, devicetype_id from tab_devicetype_info";
			}
			
			Map<String,String> devicetypeMap = UploadDAO.getMapFromList(
					DBOperation.getRecords(strSQL),"dtype","devicetype_id");
//			if(devicetypeMap == null){
//				devicetypeMap = new HashMap<String,String>();
//			}
			//System.out.println(devicetypeMap);
			
			if (DBUtil.GetDB() == 1){
				strSQL = "select vendor_id || '|' || device_model_id || '|' || hardwareversion as dmodel, 1 from tab_devicetype_info";
			}else{
				strSQL = "select vendor_id + '|' + device_model_id + '|' + hardwareversion as dmodel, 1 from tab_devicetype_info";
			}
			
			Set<String> devicetypeSet = null;
			if((tm = UploadDAO.getMapFromList(DBOperation.getRecords(strSQL),"dmodel","1")) != null){
				devicetypeSet = tm.keySet();
			}else{
				devicetypeSet = new HashSet<String>();
			}
			//System.out.println(devicetypeSet);
			/**
			strSQL = "select vendor_id +'|'+device_model_id vmodel,device_model_id from gw_device_model b";
			Map<String, String> deviceModelIdMap = UploadDAO.getMapFromList(
					DBOperation.getRecords(strSQL),"vmodel","device_model_id")));
//			if(deviceModelIdMap == null){
//				deviceModelIdMap = new HashMap<String,String>();
//			}
			**/
			
			strSQL = "select devicetype_id,1 yi from tab_software_file where softwarefile_isexist=1";

			if((tm = UploadDAO.getMapFromList(DBOperation.getRecords(strSQL),"devicetype_id","yi")) != null){
				devicetypeIdSet = tm.keySet();
			}else{
				devicetypeIdSet = new HashSet<String>();
			}
			//System.out.println(devicetypeIdSet);
			
			strSQL = "select vendor_id from tab_vendor where vendor_name='"
					+ vendor_name + "'";

			if((tlist = DBOperation.getRecords(strSQL)) != null){
				vendorlist = tlist;
			}else{
				vendorlist = new ArrayList<HashMap<String,String>>();
			}
			//System.out.println(vendorlist);
			ouiNum = vendorlist.size();

			maxDevicetypeId = DBOperation.getMaxId("devicetype_id",
					"tab_devicetype_info");
			//在入tab_devicetype_info表的过程中，其他模块同时也入了该表
			maxDevicetypeId = maxDevicetypeId + 3;
			//不考虑最后的LINKAGE
			iDevModelLen--;
			iHardwareLen--;
			Map<String, String> vendorMap = new HashMap<String, String>();
			String tmpOUI = "";
			String tmpDevModelId = null;
			for (int k = 0; k < ouiNum; k++) {
				vendorMap = vendorlist.get(k);
				tmpOUI = String.valueOf(vendorMap.get("vendor_id"));
				for (int i = 0; i < iDevModelLen; i++) {
					tmpDevModelId = String.valueOf(devModelMap.get(arryDevModel[i]));
					//tmpDevModelId = String.valueOf(arryDevModel[i]);
					for (int j = 0; j < iHardwareLen; j++) {
						System.out.println("i,j,k:" + tmpOUI + "|"+ tmpDevModelId + "|"+ arryHardware[j]);
						if (devicetypeSet.contains(tmpOUI + "|"
								+ tmpDevModelId + "|"
								+ arryHardware[j])) {
							devicetypeId = (String) devicetypeMap
									.get(tmpOUI + "|" + tmpDevModelId
											+ "|" + arryHardware[j]
											+ "|" + softwareversion);
							if (devicetypeId == null
									|| "".equals(devicetypeId)) {
								sqlList
										.add("insert into tab_devicetype_info "
												+ "(devicetype_id,vendor_id,device_model_id,hardwareversion,softwareversion)"
												+ " values ("
												+ (++maxDevicetypeId)
												+ ",'"
												+ tmpOUI
												+ "','"
												+ tmpDevModelId
												+ "','"
												+ arryHardware[j]
												+ "','"
												+ softwareversion
												+ "')");

								sqlfileList
										.add("insert into tab_software_file(softwarefile_id,softwarefile_name,"
												+ "softwarefile_description,softwarefile_size,dir_id,softwarefile_status,"
												+ "softwarefile_isexist,devicetype_id,citylist,servicelist,device_model_id) values("
												+ (++file_id)
												+ ","
												+ "'"
												+ strRename
												+ "',"
												+ "'"
												+ remark
												+ "',"
												+ file_size
												+ ","
												+ dir_id
												+ ","
												+ file_status
												+ ","
												+ "1"
												+ ","
												+ maxDevicetypeId
												+ ",'"
												+ cityList
												+ "','"
												+ serviceList
												+ "','"
												+ tmpDevModelId
												+ "')");
							} else {
								if (!devicetypeIdSet
										.contains(devicetypeId)) {
									sqlfileList
											.add("insert into tab_software_file(softwarefile_id,softwarefile_name,"
													+ "softwarefile_description,softwarefile_size,dir_id,softwarefile_status,"
													+ "softwarefile_isexist,devicetype_id,citylist,servicelist,device_model_id) values("
													+ (++file_id)
													+ ","
													+ "'"
													+ strRename
													+ "',"
													+ "'"
													+ remark
													+ "',"
													+ file_size
													+ ","
													+ dir_id
													+ ","
													+ file_status
													+ ","
													+ "1"
													+ ","
													+ devicetypeId
													+ ",'"
													+ cityList
													+ "','"
													+ serviceList
													+ "','"
													+ tmpDevModelId
													+ "')");
								} else {
									System.out.println("厂商:"
											+ vendor_name + "(oui:"
											+ tmpOUI + ") 型号："
											+ tmpDevModelId
											+ " 硬件版本："
											+ arryHardware[j]
											+ " 目标软件版本："
											+ softwareversion
											+ " 的软件升级文件已经存在");
									
								}
							}
						}
					}
				}
			}
			if (sqlList != null && sqlList.size() > 0) {
				icode = DBOperation.executeUpdate(sqlList);
				if (icode > 0) {
					if(null != sqlfileList && sqlfileList.size() > 0){
						icode = DBOperation.executeUpdate(sqlfileList);
						if (icode > 0) {
							msg += " 文件入库成功！";
						} else {
							msg += " 文件入库失败！";
						}
					}else{
						msg += "该厂商该版本的文件已经存在";
						msg += " 文件入库失败！";
					}
				} else {
					msg += " 设备类型入库失败！";
				}
			} else {
				if(null != sqlfileList && sqlfileList.size() > 0){
					icode = DBOperation.executeUpdate(sqlfileList);
					if (icode > 0) {
						msg += " 文件入库成功！";
					} else {
						msg += " 文件入库失败！";
					}
				}else{
					msg += "该厂商的该版本文件已经存在！";
					msg += " 文件入库失败！";
				}
			}

		} else {
			icode = UploadDAO.addConfFile(UploadDAO.getConfFileMaxId(),
					strRename, remark, file_size, dir_id, file_status,
					devicetype_id, area_id);
			if (icode > 0) {
				msg += " 文件入库成功！";
			} else {
				msg += " 文件入库失败！";
			}
		}
	}
%>

<HTML>
	<HEAD>
		<style>
BODY {
	BACKGROUND-COLOR: #F4F4FF;
	COLOR: #000000;
	FONT-FAMILY: "宋体", "Arial";
	FONT-SIZE: 12px;
	MARGIN: 0px
}
</style>
	</HEAD>

	<BODY BGCOLOR="white">
		<SPAN id="idMsg"><%=msg%></SPAN>
	</BODY>

</HTML>
