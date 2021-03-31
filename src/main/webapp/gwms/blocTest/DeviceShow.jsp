<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="/timelater.jsp"%>
<%@ include file="../head.jsp"%>

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ page import="com.linkage.litms.common.util.*"%>
<%@ page import="com.linkage.module.gwms.blocTest.act.QueryEgwcustServInfoACT"%>

<%@ page import="java.util.*"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="EGWUserInfoAct" scope="request" class="com.linkage.litms.resource.EGWUserInfoAct" />
<jsp:useBean id="FileSevice" scope="request" class="com.linkage.litms.resource.FileSevice" />
<%
	Cursor cursor = null;
	Map fields = null;
	request.setCharacterEncoding("GBK");
	String device_id = request.getParameter("device_id");
	
	// ��ȡ�����з�ʽ
	QueryEgwcustServInfoACT qesiACT2 = new QueryEgwcustServInfoACT();
	Cursor cursor2 = qesiACT2.getAccessType(device_id);  
	Map fields2 = cursor2.getNext();
	String access_type = fields2.get("access_type")==null?"":(String)fields2.get("access_type");
	if("1".equals(access_type)){
		access_type = "DSL";
	}else if("2".equals(access_type)){
		access_type = "Ethernet";
	}else if("3".equals(access_type)){
		access_type = "EPON";
	}else if("4".equals(access_type)){
		access_type = "GPON";
	}
	
	//String gw_type = request.getParameter("gw_type");
	//if (null == gw_type || "".equals(gw_type)) {
	//	gw_type = "1";
	//}
	//�û���Ϣ
	String username = "";
	String service_name = "";
	// ��ȡ�����豸��Ϣ
	fields = DeviceAct.getSingleDeviceInfo(request);
	String device_serialnumber = "";//�豸���к�
	String device_name = "";//�豸����
	String maxenvelopes = "";//������Envelopes
	//String port = "";//�˿�
	//String path = "";//��ַ
	//String retrycount = "";//���Դ���
	String device_model = "";//�豸�ͺ�
	String devicetype_id = "";
	String cpe_mac = "";//�豸MAC��ַ
	String loopback_ip = "";
	//	2009/03/04 ��ѧ��ע�ͣ�ԭ���ж��ն�����״̬�Լ��豸����ʱ��任�����ڴ��豸״̬���ѯ
	//	String cpe_currentupdatetime = "";//�豸�������ʱ��
	String last_time = "";
	String software_version = ""; //����汾
	String handware_version = ""; //Ӳ���汾
	String spec_version = ""; //Ӳ���汾
	//	2009/03/04 ��ѧ��ע�ͣ�ԭ���ж��ն�����״̬�Լ��豸����ʱ��任�����ڴ��豸״̬���ѯ
	//	String cpe_currentstatus = "";//�豸��ǰע��״̬
	String online_status = "";
	String online_status_1 = "";
	String cpe_operationinfo = "";//�豸��������ʷ��Ϣ
	String oui = "";//����oui
	String manufacturer = "";
	//String oui_name = "";
	String device_area_id = "";
	String device_status = "";
	String city_id = "";
	String complete_time = "";
	String device_type = "";
	//�ͻ�id
	String customer_id = "";
	//�����ַ
	String device_url = "";
	String gw_type = "";
	String gw_type_name = "";
	//����״̬
	String status = "";
	//����ID
	String vendor_id = "";
	if (fields != null)
	{
		oui = (String) fields.get("oui");
		vendor_id = (String) fields.get("vendor_id");
		device_serialnumber = (String) fields.get("device_serialnumber");
		cpe_mac = (String) fields.get("cpe_mac");
		loopback_ip = (String) fields.get("loopback_ip");
		//		2009/03/04 ��ѧ��ע�ͣ�ԭ���ж��ն�����״̬�Լ��豸����ʱ��任�����ڴ��豸״̬���ѯ
		//		cpe_currentupdatetime = (String) fields.get("cpe_currentupdatetime");
		last_time = (String) fields.get("last_time");
		complete_time = (String) fields.get("complete_time");
		//		2009/03/04 ��ѧ��ע�ͣ�ԭ���ж��ն�����״̬�Լ��豸����ʱ��任�����ڴ��豸״̬���ѯ
		//		cpe_currentstatus = (String) fields.get("cpe_currentstatus");
		online_status = (String) fields.get("online_status");
		online_status_1 = online_status;
		//	cpe_operationinfo = (String)fields.get("cpe_operationinfo");
		device_name = (String) fields.get("device_name");
		devicetype_id = (String) fields.get("devicetype_id");
		//oui = oui_name = (String)fields.get("oui");
		oui = (String) fields.get("oui");
		device_area_id = (String) fields.get("area_id");
		device_status = (String) fields.get("device_status");
		maxenvelopes = (String) fields.get("maxenvelopes");
		gw_type = (String) fields.get("gw_type");
		if (gw_type.equals("0"))
		{
			gw_type_name = "��ͨ�����豸";
		}
		else if (gw_type.equals("1"))
		{
			gw_type_name = "��ͥ�����豸";
		}
		else if (gw_type.equals("4"))
		{
			gw_type_name = "��ȫ�����豸";
		}
		else if (gw_type.equals("5"))
		{
			gw_type_name = "���������";
		}
		else
		{
			gw_type_name = "��ҵ�����豸";
		}
		city_id = (String) fields.get("city_id");
		device_type = (String) fields.get("device_type");
		customer_id = (String) fields.get("customer_id");
		device_url = (String) fields.get("device_url");
		//2009/03/04 ��ѧ��ע�ͣ�ԭ���ж��ն�����״̬�Լ��豸����ʱ��任�����ڴ��豸״̬���ѯ
		//�ж�����״̬
		//		String max_time = (String) fields.get("max_time");
		//		if (max_time != null) {
		//	Date date = new Date();
		//	long nowtime = date.getTime();
		//	if ((nowtime - Long.parseLong(max_time) * 1000) > 5 * 60 * 1000) {
		//		status = "<font color='red'>�豸����</font>";
		//	} else {
		//		status = "<font color='green'>�豸����</font>";
		//	}
		//		} else {
		//	status = "<font color='red'>�豸����</font>";
		//		}
		//		if (cpe_currentstatus != null && "1".equals(cpe_currentstatus)) {
		//	cpe_currentstatus = "<font color='green'>�豸����</font>";
		//		} else {
		//	cpe_currentstatus = "<font color='red'>�豸����</font>";
		//		}
		//		if (!LipossGlobals.getLipossProperty("InstArea.ShortName").equals("gd_dx"))
		//	status = cpe_currentstatus;
		//	}
		//��cpe_currentupdatetimeת����ʱ��
		//	if (cpe_currentupdatetime != null && !cpe_currentupdatetime.equals("")) {
		//		DateTimeUtil dateTimeUtil = new DateTimeUtil(
		//		Long.parseLong(cpe_currentupdatetime) * 1000);
		//		cpe_currentupdatetime = dateTimeUtil.getLongDate();
		//		dateTimeUtil = null;
	}
	//online_status������״̬ת��
	if (null != online_status && "1".equals(online_status))
	{
		online_status = "<font color='green'>�豸����</font>";
	}
	else
	{
		online_status = "<font color='red'>�豸����</font>";
	}
	//��last_timeת����ʱ��
	if (null != last_time && !("").equals(last_time))
	{
		DateTimeUtil dateTimeUtil = new DateTimeUtil(
		Long.parseLong(last_time) * 1000);
		last_time = dateTimeUtil.getLongDate();
		dateTimeUtil = null;
	}
	//��complete_timeת����ʱ��
	if (complete_time != null && !complete_time.equals(""))
	{
		DateTimeUtil dateTimeUtil = new DateTimeUtil(Long
		.parseLong(complete_time) * 1000);
		complete_time = dateTimeUtil.getLongDate();
		dateTimeUtil = null;
	}
	//��ȡ�豸����Ӧ��Ӳ���汾/����汾
	fields = DeviceAct.getDeviceModelVersion(vendor_id, devicetype_id);
	if (fields != null)
	{
		software_version = (String) fields.get("softwareversion");
		handware_version = (String) fields.get("hardwareversion");
		spec_version = (String) fields.get("specversion");
		manufacturer = (String) fields.get("vendor_name");
		device_model = (String) fields.get("device_model");
	}
	cpe_operationinfo = cpe_operationinfo == null ? "��" : cpe_operationinfo;
	//�豸������Ϣ
	String device_area_name = "";//�豸��������
	String device_support_code = "";//�豸��֧�ֵ�ҵ�����
	String device_current_code = "";//�豸��ǰ��ͨ��ҵ�����
	String device_hard_code = "";//�豸Ӳ����֧�ֵ�ҵ�����
	device_support_code = device_hard_code = DeviceAct
			.getSupportService(devicetype_id);
	//device_current_code = DeviceAct.getSupportServiceByDevice(device_id);//�豸��ǰ��ͨ��ҵ�����
	Map area_Map = DeviceAct.getAreaIdMapName();
	device_area_name = (String) area_Map.get(device_area_id);
	cursor = DeviceAct.getDeviceModelTemplate(oui, devicetype_id);
	fields = cursor.getNext();
	String deviceModelTemplate = "";
	StringBuffer sb = new StringBuffer();
	//�����ַ�����ʽ
	sb.append("");
	while (fields != null)
	{
		sb.append("" + fields.get("template_name") + "\n");
		fields = cursor.getNext();
	}
	deviceModelTemplate = sb.toString();
	sb = null;
	//��ȡITMS������ʷ��Ϣ
	//cpe_operationinfo = DeviceAct.getHistoryOperation(device_id);
%>
<%
	 /*   
	 *    add by benyp
	 */
	Cursor cursor1 = new Cursor();
	Map fields1 = null;
	if (device_id != null)
	{
		String sql = "select device_serialnumber,x_com_username,x_com_passwd,cpe_username,cpe_passwd,acs_username,acs_passwd from tab_gw_device where device_id ='"
		+ device_id + "'";
		cursor1 = DataSetBean.getCursor(sql);
	}
	String x_com_username = "";
	String x_com_passwd = "";
	String cpe_username = "";
	String cpe_passwd = "";
	String acs_username = "";
	String acs_passwd = "";
	fields1 = cursor1.getNext();
	while (fields1 != null)
	{
		x_com_username = (String) fields1.get("x_com_username");
		x_com_passwd = (String) fields1.get("x_com_passwd");
		cpe_username = (String) fields1.get("cpe_username");
		cpe_passwd = (String) fields1.get("cpe_passwd");
		acs_username = (String) fields1.get("acs_username");
		acs_passwd = (String) fields1.get("acs_passwd");
		fields1 = cursor1.getNext();
	}
	//��ȡcityMap
	//String city_id_ = "";
	//String city_name = "";
	//String city_sql = "select city_id, city_name from tab_city";
	//Cursor city_cursor = new Cursor();
	//city_cursor = DataSetBean.getCursor(city_sql);
	//Map cityFiled = city_cursor.getNext();
	//HashMap cityMap = new HashMap();
	//while (cityFiled != null)
	//{
	//	city_id_ = cityFiled.get("city_id").toString();
	//	city_name = cityFiled.get("city_name").toString();
	//	cityMap.put(city_id_, city_name);
	//	cityFiled = city_cursor.getNext();
	//}
	Map cityMap = CityDAO.getCityIdCityNameMap();
	String city = "";
	if (null != cityMap.get(city_id))
	{
		city = cityMap.get(city_id).toString();
	}
%>

<script type="text/javascript" src="../../Js/jquery.js"></script>
<script>
<!-- 
var request = false;
   try {
     request = new XMLHttpRequest();
   } catch (trymicrosoft) {
     try {
       request = new ActiveXObject("Msxml2.XMLHTTP");
       
     } catch (othermicrosoft) {
       try {
         request = new ActiveXObject("Microsoft.XMLHTTP");
       } catch (failed) {
         request = false;
       }  
     }
   }
   if (!request)
     alert("Error initializing XMLHttpRequest!");
     
	//�����û�������ajax
   function send_request(url) {
     request.open("GET", url, true);
     request.onreadystatechange = updatePage;
     request.send(null);
   }

	function updatePage() {
     	if (request.readyState == 4) {
        if (request.status == 200) {
        	document.all.operation_info.innerHTML = request.responseText;
        } else
            alert("status is " + request.status);
     	}
   }

   
   function openCust(param){
		window.open("<s:url value='/bbms/CustomerInfo!detailInfo.action'/>?customer_id="+param,"","left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
	}
    

	function showMsgDlg(){
		w = document.body.clientWidth;
		h = document.body.clientHeight;

		l = (w-250)/2;
		t = (h-60)/2;
		PendingMessage.style.left = l;
		PendingMessage.style.top  = t;
		PendingMessage.style.display="";
	}
	function closeMsgDlg(){
		PendingMessage.style.display="none";
	}

	function disableBtns() {
		document.all.compwGen.disabled = true;
		document.all.compwSet.disabled = true;
	}

	function inspireBtns() {
		document.all.compwGen.disabled = false;
		document.all.compwSet.disabled = false;
	}
	
	function getOnlineStatus() {
		document.all.onlineStatus.innerHTML = "<font color='blue'>���ڻ�ȡ�豸����״̬</font>";
		send_request_OS("../../paramConfig/testConnectionSubmit.jsp?device_id="+<%=device_id%>+"&tt="+new Date().getTime());
	}
	
	function send_request_OS(url) {
     request.open("GET", url, true);
     request.onreadystatechange = updatePageOS;
     request.send(null);
   }

	function updatePageOS() {
     	if (request.readyState == 4) {
        if (request.status == 200) {
        	var onlineStatus = request.responseText;
        	onlineStatus = onlineStatus.replace(/(^\s*)|(\s*$)/g, ""); 
        	//alert(onlineStatus);
        	if(onlineStatus=="���豸���ӳɹ���"){
	        	document.all.onlineStatus.innerHTML = "<font color='green'>�豸����(ʵʱ)</font>";
	        	document.all.onlineStatus.value = onlineStatus;
        	}else{
        		document.all.onlineStatus.innerHTML = "<font color='red'>�豸����(ʵʱ)</font>";
	        	document.all.onlineStatus.value = onlineStatus;
        	}
        } else
            alert("status is " + request.status);
     	}
   }
 
   





 function GoContent(user_id,gw_type){
 	var strpage="EGWUserRelatedInfo.jsp?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
//-->
</script>
<style type="text/css">
<!--
.btn_g {
	border: 1px solid #999999
}
//
-->
</style>

<div id="PendingMessage"
	style="position: absolute; z-index: 3; top: 240px; left: 250px; width: 250; height: 60; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
	<center>
		<table border="0">
			<tr>
				<td valign="middle">
					<img src="../../images/cursor_hourglas.gif" border="0" WIDTH="30"
						HEIGHT="30">
				</td>
				<td>
					&nbsp;&nbsp;
				</td>
				<td valign="middle">
					<span id="txtLoading" style="font-size: 12px; font-family: ����">�����������룬���Եȡ�</span>
				</td>
			</tr>
		</table>
	</center>
</div>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD>
			<TABLE width="99%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD bgcolor=#999999>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
								<TH colspan="4" align="center">
									�豸��
									<%=device_serialnumber%>
									����ϸ��Ϣ
								</TH>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan=4>
									�豸������Ϣ
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right" width="15%">
									�豸ID
								</TD>
								<TD width="25%"><%=device_id%>
								<input type="hidden" name="deviceId" value="<%=device_id%>">
								</TD>
								<TD class=column align="right">
									�豸�ͺ�
								</TD>
								<TD width="40%"><%=device_model%>
								<input type="hidden" name="deviceModel" value="<%=device_model%>">
								</TD>
							</TR>

							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									�豸����(OUI)
								</TD>
								<TD><%=manufacturer%>(<%=oui%>)
								<input type="hidden" name="oui" value="<%=oui%>">
								</TD>
								<TD class=column align="right">
									���к�
								</TD>
								<TD><%=device_serialnumber%>
								<input type="hidden" name="deviceSn" value="<%=device_serialnumber%>">
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									Ӳ���汾
								</TD>
								<TD><%=handware_version%></TD>
								<TD class=column align="right">
									�ر�汾
								</TD>
								<TD><%=spec_version%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									����汾
								</TD>
								<TD><%=software_version%></TD>
								<TD class=column align="right">
									�豸����
								</TD>
								<TD><%=device_type%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									������
								</TD>
								<TD width=140><%=maxenvelopes%></TD>
								<TD class=column align="right">
									MAC ��ַ
								</TD>
								<TD><%=cpe_mac%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									�豸���
								</TD>
								<TD width=140><%=device_id%></TD>
								<TD class=column align="right">
									������
								</TD>
								<TD><%=device_area_name%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									�豸��ID
								</TD>
								<TD><%=gw_type%></TD>
								<TD class=column align="right">
									�豸������
								</TD>
								<TD><%=gw_type_name%></TD>
							</TR>
							<%
								if ("2".equals(gw_type))
								{
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									��ҵ����
								</TD>
								<TD><%=EGWUserInfoAct.getCustomerName(customer_id)%></TD>
								<TD class=column align="right">
									�����ַ
								</TD>
								<TD>
									<a href="#" onclick="window.open('<%=device_url%>')"><%=device_url%></a>
								</TD>
							</TR>
							<%
							}
							%>
							<%
								List devServiceStatus = DeviceAct.getServiceStatByDevice(device_id);
								//List bindCustomerStatus = DeviceAct.getBindCustomerStatusByDeviceId(oui,device_serialnumber);
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan=4>
									�豸ҵ����Ϣ
								</TD>
							</TR>

							<%
							/**
								String outStr = null;
								if (bindCustomerStatus != null && bindCustomerStatus.size() > 0)
								{
									int len = bindCustomerStatus.size();
									int pause = 0;
									int stop = 0;
									for (int i = 0; i < len; i++)
									{
										String user_state = (String) bindCustomerStatus.get(i);
										if ("2".equals(user_state))
										{
									pause++;
										}
										if ("3".equals(user_state))
										{
									stop++;
										}
									}
									if (pause == len)
									{
										outStr = "��ͣ";
									}
									if (stop == len)
									{
										outStr = "ͣ��";
									}
								}
								
								if (outStr == null) {
									if (cpe_currentstatus.equals("1")) {
										outStr = "����";
									} else if (cpe_currentstatus.equals("0")) {
										outStr = "������";
									} else {
										outStr = "δ֪";
									}
								}*/
							%>


							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									��ǰ��ͨҵ��
								</TD>
								<TD><%=devServiceStatus == null || devServiceStatus.size() == 0 ? "<��>"
							: "" + devServiceStatus.size() + "��ҵ��"%>
								</TD>
								<TD class=column align="right">
									�Ƿ񼤻�
								</TD>
								<TD><%=devServiceStatus == null || devServiceStatus.size() == 0 ? "<��>"
							: ""%>
								</TD>
							</TR>
							<%
								if (devServiceStatus != null)
								{
									if (devServiceStatus.size() > 0)
									{
										int len = devServiceStatus.size();
										for (int i = 0; i < len; i++)
										{
									String[] devStatusData = (String[]) devServiceStatus.get(i);
									String t = devStatusData[1];
									if (t != null && !"".equals(t.trim()))
									{
									}
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right"></TD>
								<TD><%=devStatusData[2]%></TD>
								<TD class=column align="right"></TD>
								<TD><%="1".equals(devStatusData[1]) ? "�Ѽ���" : "0".equals(devStatusData[1])?"δ����":"����ʧ��"%></TD>
							</TR>
							<%
									}
									}
								}
							%>

							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan=4>
									�豸��̬��Ϣ
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									�豸����״̬
								</TD>
								<TD colspan="3" width="30%">
									<span id="onlineStatus"><%=online_status%></span>&nbsp;&nbsp;
									<input name="onlineStatusGet" type="button" value="�������״̬"
										class="btn_g" onclick="getOnlineStatus()">
								</TD>
							</TR>

							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									�豸����
									<input type="hidden" name="cityId" value="<%=city_id%>">
								</TD>
								<TD><%=city%></TD>
								<TD class=column align="right">
									ע��ϵͳʱ��
								</TD>
								<TD><%=complete_time%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									IP��ַ
								</TD>
								<TD><%=loopback_ip%></TD>
								<TD class=column align="right">
									�������ʱ��
								</TD>
								<TD><%=last_time%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">

								<TD class=column align="right">
									�豸��ǰע��״̬
								</TD>
								<TD>
									<%
											if (device_status.equals("1"))
											out.println("��ȷ��");
										else if (device_status.equals("0"))
											out.println("δȷ��");
										else
											out.println("��ɾ��");
									%>
								</TD>
								<TD class=column align="right">
									�豸����ģ��
								</TD>
								<TD>
									<textarea cols=40 rows=4 readonly><%=deviceModelTemplate%></textarea>
								</TD>
							</TR>

							<TR>
								<TD class=column align="center" colspan=4>

									<div id="div_device"
										style="width: 100%; height: 100%; z-index: 1; top: 100%;">
										<span id="operation_info"></span>
									</div>
								</TD>

							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan="4">
									��ǰ���ò���
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									CPE�û���
								</TD>
								<TD><%=cpe_username%></TD>
								<TD class=column align="right">
									CPE����
								</TD>
								<TD><%=cpe_passwd%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									ACS�û���
								</TD>
								<TD><%=acs_username%></TD>
								<TD class=column align="right">
									ACS����
								</TD>
								<TD><%=acs_passwd%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									����ά���˺�
								</TD>
								<TD><%=x_com_username%></TD>
								<TD class=column align="right">
									����ά������
								</TD>
								<TD>
									<span id="compw_span"><%=x_com_passwd%></span>&nbsp;
									<span id="compw_span_new"></span>&nbsp;
									<!-- <input name="compw_span_hid" value="" type="hidden"/> -->
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan=4>
									�û�������Ϣ
								</TD>
							</TR>
							<%
								//�õ��û���Ϣ
								cursor = DeviceAct.getCustomerOfDev(device_id, gw_type);
								fields = cursor.getNext();
								if (fields == null){
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD align="center" colspan=4>
									�豸�����û�.
								</TD>
							</TR>
							<%
								}else{
								String user_id = "";
								while (fields != null){
									username = (String) fields.get("username");
									service_name = (String) fields.get("serv_type_name");
									user_id = (String) fields.get("user_id");
									fields = cursor.getNext();
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									�û��ʺ�
								</TD>
								<TD>
								<a href="javascript:" onclick="GoContent('<%=user_id%>','<%=gw_type%>')"><%=username%></a>
								<input type="hidden" name="adNumber" value="<%=username%>">
								</TD>
								<TD class=column align="right">
									ҵ������
								</TD>
								<TD><%=service_name%></TD>
							</TR>
							<%
							 	}
							 	QueryEgwcustServInfoACT qesiACT = new QueryEgwcustServInfoACT();
							 	cursor = qesiACT.getEgwcustServInfo(user_id);  //��ȡ PPPoE�˺š�PVC�š�VLAN�š��ӿ�״̬
								fields = cursor.getNext();
								if(fields != null){
									String PPPoENo = (String) fields.get("username");   //PPPoE�˺�
									
									
									String vpiid = fields.get("vpiid")==null?"":(String)fields.get("vpiid");
									String vciid = fields.get("vciid")==null?"":(String)fields.get("vciid");
									String PVC = vpiid+"/"+vciid; // PVC��
									
									String PVC_VLAN = fields.get("vlanid")==null? "":(String)fields.get("vlanid");  // VLAN��
									
									//·����������PVC���Ž���VLANID
									if("DSL".equals(access_type))
									{
										PVC_VLAN = PVC;
									}
									
									String satusStr = "";
									if (null != online_status_1 && "1".equals(online_status_1)){
										satusStr = "UP";
									}else{
										satusStr = "DOWN";
									}
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">PPPoE�˺�</TD>
								<TD><%=PPPoENo%></TD>
								<TD class=column align="right">�ӿ�״̬</TD>
								<TD><%=satusStr%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">PVC/VLAN</TD>
								<TD><%=PVC_VLAN%></TD>
								<TD class=column align="right"></TD>
								<TD></TD>
							</TR>
							<%
								}
							}
								if (!"1".equals(gw_type))
								{
									//�õ��û���Ϣ
									String customer_name;
									String customer_pwd;
									String customer_address;
									String mobile;
									String linkphone;
									String phoneStr;
									cursor = DeviceAct.getCustomerOfDevBBMS(device_id);
									fields = cursor.getNext();
									
									if (fields == null)
									{
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD align="center" colspan=4>
									�豸���޿ͻ���Ϣ.
								</TD>
							</TR>
							<%
									}
									while (fields != null)
									{
										customer_name = (String) fields.get("customer_name");
										customer_pwd = (String) fields.get("customer_pwd");
										customer_address = fields.get("customer_address") ==null?"":(String)fields.get("customer_address");
										mobile = fields.get("mobile") == null?"":(String)fields.get("mobile");
										linkphone = fields.get("linkphone")==null?"":(String)fields.get("linkphone");
										if(!"".equals(mobile)){
											phoneStr = mobile;
										}else{
											phoneStr = linkphone;
										}
										fields = cursor.getNext();
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									�ͻ�����
								</TD>
								<TD>
									<a href="javascript:openCust('<%=customer_id%>')">
									<%=customer_name%>
									</a>
								</TD>
								<TD class=column align="right">
									�ͻ�����
								</TD>
								<TD><%=customer_pwd%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">��ϵ�绰</TD>
								<TD><%=phoneStr%></TD>
								<TD class=column align="right">װ����ַ</TD>
								<TD><%=customer_address%></TD>
							</TR>
							
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">���뷽ʽ</TD>
								<TD><%=access_type%></TD>
								<TD class=column align="right"></TD>
								<TD></TD>
							</TR>
							<%
								}
								}
							%>
							<TR>
								<TD colspan="4" align="center" class=foot>
									<!--<INPUT TYPE="submit" value=" �� �� " class=jianbian
										onclick="javascript:window.close();">-->
								</TD>
							</TR>

						</TABLE>


					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<div id="setPWDIV"></div>
		</TD>

	</TR>
</TABLE>
<%
	cursor = null;
	if (fields != null)
	{
		fields.clear();
	}
	fields = null;
	DeviceAct = null;
%>
<%@ include file="../foot.jsp"%>