<%@page import="com.linkage.module.gwms.dao.tabquery.CityDAO"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ page
	import="com.linkage.litms.common.database.*,java.util.*,com.linkage.litms.common.util.*"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="EGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.EGWUserInfoAct" />
<jsp:useBean id="FileSevice" scope="request"
	class="com.linkage.litms.resource.FileSevice" />
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
	
<%
	Cursor cursor = null;
	Map fields = null;
	request.setCharacterEncoding("GBK");
	String device_id = request.getParameter("device_id");
	String gw_type = request.getParameter("gw_type");
	if (null == gw_type || "".equals(gw_type)) {
		gw_type = "1";
	}
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
	String is_normal = ""; //�Ƿ�淶
	String is_check = ""; //�Ƿ����
	String spec_version = ""; //Ӳ���汾
	//	2009/03/04 ��ѧ��ע�ͣ�ԭ���ж��ն�����״̬�Լ��豸����ʱ��任�����ڴ��豸״̬���ѯ
	//	String cpe_currentstatus = "";//�豸��ǰע��״̬
	String online_status = "";
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
	// String gw_type = "";
	String gw_type_name = "";
	//����״̬
	String status = "";
	//����ID
	String vendor_id = "";
	//�ն˹��
	String spec_name = "";
	//�Ƿ�֧��awifi��ͨ
	String is_awifi = "��";
	//�ӱ����ӹ�è��ʶ
	String gigabit_port="";
	String gigabitport="";
	// ���������ն�֧������
	String gbbroadband="";
	// ����������������
	String accessStyle="";
	
	//�½��������з�ʽ�Ͱ汾����,�Ƿ�����ʲ�
	String access_type = "";
	String deviceTypeName = "";
	String isTelDev = "";

	String device_version_type="";

	//�½����ӱ����ն�״̬ 0 Ϊ���ϣ���Ϊ����
	String scrapStatus="����";
	
	if (fields != null)
	{
		oui = (String) fields.get("oui");
		vendor_id = (String) fields.get("vendor_id");
		device_serialnumber = (String) fields.get("device_serialnumber");
		cpe_mac = (String) fields.get("cpe_mac");
		
		spec_name = (String)fields.get("spec_name");
		if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
		gigabit_port=(String)fields.get("gigabit_port");
		if(gigabit_port.equals("1"))
		{
			gigabitport="��";
		}else
		{
			gigabitport="��";
		}
		}
		
		if("gs_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
			device_version_type = (String)fields.get("device_version_type");
			if("1".equals(device_version_type)){
				device_version_type= "E8-C";
			}else if ("2".equals(device_version_type)){
				device_version_type= "PON�ں�";
			}else if ("3".equals(device_version_type)){
				device_version_type= "10GPON";
			}else if ("4".equals(device_version_type)){
				device_version_type= "��������";
			}else if ("5".equals(device_version_type)){
				device_version_type= "��������1.0";
			}else if ("6".equals(device_version_type)){
				device_version_type= "��������2.0";
			}else if ("7".equals(device_version_type)){
				device_version_type= "��������3.0";
			}else{
				device_version_type="";
			}
			
			
		}

        if("ah_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
			device_version_type = (String)fields.get("deviceVersionType");
		}
		
		if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
			boolean flg = DeviceAct.IsHtMegaBytes(device_id);
			gbbroadband=(String)fields.get("gbbroadband");
			
			if(gbbroadband.equals("1"))
			{
				gbbroadband="ǧ��";
			}
			// 20200512 ����������������
			else if(gbbroadband.equals("3"))
			{
				gbbroadband="����";
			}
			else if(flg == true)
			{
				gbbroadband="ǧ��";
			}
			else
			{
				gbbroadband="����";
			}

			if (null != fields.get("device_version_type")) {
			    if ("5".equals(fields.get("device_version_type").toString())) {
					accessStyle = "XGPON";
				}
				else if ("4".equals(fields.get("device_version_type").toString())) {
					accessStyle = "10GEPON";
				}
			}
			if ("".equals(accessStyle) && null != fields.get("access_style_relay_id")) {
				if ("3".equals(fields.get("access_style_relay_id").toString())) {
					accessStyle = "EPON";
				}
				else if ("4".equals(fields.get("access_style_relay_id").toString())) {
					accessStyle = "GPON";
				}
			}
			if ("".equals(accessStyle) && null != fields.get("access_style_id")) {
				if ("3".equals(fields.get("access_style_id").toString())) {
					accessStyle = "EPON";
				}
				else if ("4".equals(fields.get("access_style_id").toString())) {
					accessStyle = "GPON";
				}
			}

		}
		loopback_ip = (String) fields.get("loopback_ip");
		//		2009/03/04 ��ѧ��ע�ͣ�ԭ���ж��ն�����״̬�Լ��豸����ʱ��任�����ڴ��豸״̬���ѯ
		//		cpe_currentupdatetime = (String) fields.get("cpe_currentupdatetime");
		last_time = (String) fields.get("last_time");
		complete_time = (String) fields.get("complete_time");
		//		2009/03/04 ��ѧ��ע�ͣ�ԭ���ж��ն�����״̬�Լ��豸����ʱ��任�����ڴ��豸״̬���ѯ
		//		cpe_currentstatus = (String) fields.get("cpe_currentstatus");
		online_status = (String) fields.get("online_status");
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
		if (null != fields.get("is_awifi") && "1".equals((String) fields.get("is_awifi")))
		{
			is_awifi = "��";
		}
		if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
			String access_type_id = (String)fields.get("device_version_type");
			if("1".equals(access_type_id)){
				deviceTypeName = "";  //Ӧ����Ϊ�ջ�ΪE8C��ʱ����ʾΪ��
			}else if("2".equals(access_type_id)){
				deviceTypeName = "��������1.0";
			}else if("3".equals(access_type_id)){
				deviceTypeName = "��������2.0";
			}else if("4".equals(access_type_id)){
				deviceTypeName = "�ں�����";
			}else if("5".equals(access_type_id)){
				deviceTypeName = "��������3.0";
			}else{
				deviceTypeName = "";
			}
			access_type = (String)fields.get("type_name");
			System.out.print("deviceTypeName:"+deviceTypeName+",access_type:"+access_type);
			
			String telDev = (String)fields.get("is_tel_dev");
			if("1".equals(telDev)){
				isTelDev = "����"; 
			}else if("2".equals(telDev)){
				isTelDev = "�ǵ���";
			}

			String isScrap  =  (String)fields.get("isscrap") ;
			String devStatus = (String)fields.get("dev_id") ;
			if("".equals(isScrap) && "".equals(devStatus)){
				scrapStatus ="����";
			}


		}
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
		online_status = "<font color='green'>���������� </font>";
	}
	else
	{
		online_status = "<font color='red'>������������</font>";
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
	// ɽ����ͨ������
	if("sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) &&  gw_type.equals("4"))
	{
		fields = DeviceAct.getStbDeviceModelVersion(vendor_id, devicetype_id);
	}
	else 
	{
		fields = DeviceAct.getDeviceModelVersion(vendor_id, devicetype_id);
	}
	if (fields != null)
	{
		software_version = (String) fields.get("softwareversion");
		handware_version = (String) fields.get("hardwareversion");
		spec_version = (String) fields.get("specversion");
		manufacturer = (String) fields.get("vendor_name");
		device_model = (String) fields.get("device_model");
		is_normal = (String) fields.get("is_normal");
		is_check = (String) fields.get("is_check");
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
		String sql = "";
		// ɽ����ͨ������
		if("sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) &&  gw_type.equals("4"))
		{
			sql = "select device_serialnumber,x_com_username,x_com_passwd,cpe_username,cpe_passwd,"
				+ "acs_username,acs_passwd from stb_tab_gw_device where device_id ='" + device_id + "'";
		}else
		{
			sql = "select device_serialnumber,x_com_username,x_com_passwd,cpe_username,cpe_passwd,acs_username,acs_passwd from tab_gw_device where device_id ='"
				+ device_id + "'";
		}
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
	Map cityMap = CityDAO.getCityIdCityNameMap();
	String city = "";
	if (null != cityMap.get(city_id))
	{
		city = cityMap.get(city_id).toString();
	}
%>
<%
	/*
	*  added  by zhangsb 2012��6��11��  �½����� --�豸��ϸ��Ϣչʾҳ��������������汾��Ϣ start
	*/
	String stragyStatusCode = "";
		String stragyStatusName = "";
		String resultId = "";
		String result = "";
		String sheetParam = "";
		String newDeviceTypeId = "";
		String oldDeviceTypeId = "";
		String newDeviceType = "";
		String oldDeviceType = "";
		Map fields2 = null;
		Map fields3 = null;
	if(LipossGlobals.isXJDX()){
		
		fields2 = DeviceAct.getCurrentSoftUpStrategy(request);
		fields3 = DeviceAct.getCurrentSoftUpStrategyXML(request);
		
		Map<String,String> softMap = DeviceAct.getSoftwareKV();
		
	 	if(null != fields2){
	 		stragyStatusCode = (String)fields2.get("status");
	 		resultId = (String)fields2.get("resultId");
	 		if(null != fields2.get("newDeviceTypeId")){
	 			newDeviceType = softMap.get((String)fields2.get("newDeviceTypeId"));
	 			if(null==newDeviceType)
	 			     newDeviceType="";
	 		}
	 		if (null != fields2.get("oldDeviceTypeId")){
	 			oldDeviceType = softMap.get((String)fields2.get("oldDeviceTypeId"));
	 			if(null==oldDeviceType)
	 				oldDeviceType ="";
	 		}
	 		//����ִ�е�״̬
	 		if(null != stragyStatusCode){
		 		if("0".equals(stragyStatusCode)){
		 			stragyStatusName = "�ȴ�ִ��";
		 		}else if("1".equals(stragyStatusCode)){
		 			stragyStatusName = "Ԥ��PVC";
		 		}else if("2".equals(stragyStatusCode)){
		 			stragyStatusName = "Ԥ���󶨶˿�";
		 		}else if("3".equals(stragyStatusCode)){
		 			stragyStatusName = "Ԥ������";
		 		}else if("4".equals(stragyStatusCode)){
		 			stragyStatusName = "ҵ���·�";
		 		}else if("100".equals(stragyStatusCode)){
		 			stragyStatusName = "ִ�����";
		 		}
	 		}else{
	 			stragyStatusName = "";
	 		}
	 		if(null != resultId){
		 		//����ִ�еĽ��
		 		if("1".equals(resultId)){
		 			result = "�ɹ�" ;
		 		}else if("0".equals(resultId) || "2".equals(resultId)){
		 			result = "�м�״̬";
		 		}else if ("3".equals(resultId)){
		 			result = "�豸�޷�����" ;
		 		}else if ("4".equals(resultId)){
		 			result = "��ʾ���豸δ����iTV���ߣ����޷�������������" ;
		 		}else {
		 			result = "ʧ��";
		 		}
	 		}else {
	 			result = "";
	 		}
	 	}
	 	if(null != fields3){
	 		if(null != fields3.get("sheet_para")){
	 			sheetParam = (String)fields3.get("sheet_para");
	 		}else{
	 			sheetParam = "δ��ȡ��XML��Ϣ";
	 		}
	 	}else{
	 		sheetParam = "δ��ȡ��XML��Ϣ";
	 	}
 	}
 	 /*
	*  added  by zhangsb 2012��6��11��  �½����� --�豸��ϸ��Ϣչʾҳ��������������汾��Ϣ    end
	*/
 %>
<%@ include file="../head.jsp"%>
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/commFunction.js"/></script>
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
	
	$(function(){
		<%-- ��ҳ����غ������ǰ�û����豸����ҳ���������ʾȨ�ޣ����¼�������������־ --%>
		<ms:hasAuth authCode="ShowDevPwd">
			superAuthLog('ShowDevPwd',
					'�鿴[<%=device_serialnumber%>]�豸�ĵ���ά������[<%=x_com_passwd%>]');
		</ms:hasAuth>
	});

	function updatePage() {
     	if (request.readyState == 4) {
        if (request.status == 200) {
        	document.all.operation_info.innerHTML = request.responseText;
        } else
            alert("status is " + request.status);
     	}
   }

///////////////////////////////��������/////////////////////////////////////////
	//�����û�������ajax
   function send_request_PW(url) {
     request.open("GET", url, true);
     request.onreadystatechange = updatePagePW;
     request.send(null);
   }

	function updatePagePW() {
     	if (request.readyState == 4) {
        if (request.status == 200) {
        	document.all.compw_span_new.innerHTML = "<font color='blue'>"+request.responseText + "</font>";
        	document.all.compw_span_new.value = request.responseText;
        } else
            alert("status is " + request.status);
     	}
   }
    
	function showOperation() {
		<%--
		var obj = document.frm;
		//alert("showOperationInfo.jsp?device_id="+<%=device_id%>);
		//alert("showOperationInfo.jsp?device_id=\""+<%=device_id%>+"\"");--%>
		send_request("showOperationInfo.jsp?device_id="+<%=device_id%>+"&tt="+new Date().getTime());
	}

	function toggleOperation(){
		var btnHTML = $("#his_check").html();
		if(btnHTML == "�鿴"){
			$("#his_check").html("�ر�");
			send_request("showOperationInfo.jsp?device_id="+<%=device_id%>+"&tt="+new Date().getTime());
		}else{
			$("#his_check").html("�鿴");
			document.all.operation_info.innerHTML = "";
		}
		
	}
	function generateComPw() {

		//$.ajax({
        //    type: "GET", 
        //    url: "GenerateComPw.jsp", 
        //    data: "tt="+new Date().getTime(),
        //    success:
        //            function(data) {
        //                    $("#compw_span_new").html("<font color='blue'>"+data+"</font>");

        //                    alert(data.trim());
        //                    $("input[@name=compw_span_hid]").value = data.trim();
        //                    alert($("input[@name=compw_span_hid]").value);
        //            },
        //    erro:
        //            function(xmlR,msg,other){
        //                   $("#compw_span_new").html("");
        //            }
        //});
	    
		send_request_PW("GenerateComPw.jsp?gw_type="+<%=gw_type%>+"&tt="+new Date().getTime());
	}

///////////////////////////////��������/////////////////////////////////////////
	//�����û�������ajax
    function send_request_setPW(url) {
        request.open("GET", url, true);
        request.onreadystatechange = updatePageSetPW;
        request.send(null);
    }

	function updatePageSetPW() {
     	if (request.readyState == 4) {
	        if (request.status == 200) {
	        	closeMsgDlg();
	        	//alert(request.responseText.trim());
				if (request.responseText.trim() == "-2") {
					alert("���ݳ����������");
					document.all.compw_span_new.innerHTML = "";
				} else if (request.responseText.trim() == "-1") {
					alert("��������ʧ��,��ȷ���豸����������");
					document.all.compw_span_new.innerHTML = "";
				} else if(request.responseText.trim() == "0") {
					alert("�������óɹ��������ݿ����ʧ�ܣ�����ϵ����Ա��");
				} else if(request.responseText.trim() == "1") {
					alert("�������óɹ���");
					document.all.compw_span.innerHTML = document.all.compw_span_new.value.trim();
		        	document.all.compw_span_new.innerHTML = "";
				} else {
					alert("δ֪����");
					document.all.compw_span_new.innerHTML = "";
				}
				inspireBtns();
	        } else {
	        	closeMsgDlg();
	            alert("����(status is " + request.status + ")");
	            inspireBtns();
	        }
     	}
    }

	function setComPw() {
		//alert($("input[@name=compw_span_hid]").val());
		var obj = document.all.compw_span_new;
		if (obj.value == undefined || obj.innerHTML == "") {
			alert(" ���� �����ɡ� ���룡 ");
		} else {
			showMsgDlg();
			send_request_setPW("SetComPw.jsp?device_id="+<%=device_id%>+"&pwValue="+obj.value.trim()+"&tt="+new Date().getTime());
			disableBtns();
		}
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
		document.all.onlineStatus.innerHTML = "<font color='blue'>���ڻ�ȡ�豸��ƽ̨����״̬</font>";
		send_request_OS("../paramConfig/testConnectionSubmit.jsp?device_id="+<%=device_id%>+"&tt="+new Date().getTime());
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
	        	document.all.onlineStatus.innerHTML = "<font color='green'>����������(ʵʱ)</font>";
	        	document.all.onlineStatus.value = onlineStatus;
        	}else{
        		document.all.onlineStatus.innerHTML = "<font color='red'>������������(ʵʱ)</font>";
	        	document.all.onlineStatus.value = onlineStatus;
        	}
        } else
            alert("status is " + request.status);
     	}
   }
 
   
  //�м�������� 
function add(){
	$("input[@name='des']").val("add device");
	var url = "<s:url value='/itms/midware/midWare!add.action'/>";
	operate(url);
}

function update(){
	$("input[@name='des']").val("change device info");
	var url = "<s:url value='/itms/midware/midWare!update.action'/>";
	operate(url);
}

function del(){
	$("input[@name='des']").val("delete device");
	var url = "<s:url value='/itms/midware/midWare!delete.action'/>";
	operate(url);
}

function midareOpen(){
	var deviceId = $.trim($("input[@name='deviceId']").val());
	var url = "<s:url value='/itms/midware/businessOpen!midMareDevOpen.action'/>";
	$.post(url,{
		deviceId:deviceId
	},function(ajax){
	    alert(ajax);
	});
}

//�м������  
function operate(url){
	var cityId = $.trim($("input[@name='cityId']").val());
	var deviceId = $.trim($("input[@name='deviceId']").val());
	var oui = $.trim($("input[@name='oui']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var deviceModel = $.trim($("input[@name='deviceModel']").val());
	var adNumber = $.trim($("input[@name='adNumber']").val());
	var status = $.trim($("input[@name='status']").val());
	var des = $.trim($("input[@name='des']").val());
	var area = $.trim($("input[@name='area']").val());
	var group = $.trim($("input[@name='group']").val());
	var phone = $.trim($("input[@name='phone']").val());
	$.post(url,{
		cityId:cityId,
		deviceId:deviceId,
		oui:oui,
		deviceSn:deviceSn,
		deviceModel:deviceModel,
		adNumber:adNumber,
		status:status,
		des:des,
		area:area,
		group:group,
		phone:phone
	},function(ajax){	
	    alert(ajax);
	});
}

//�м��ҵ��ͨ
function business_open(){	
	
	var cityId = $.trim($("input[@name='cityId']").val());
	var deviceId = $.trim($("input[@name='deviceId']").val());
	var oui = $.trim($("input[@name='oui']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var deviceModel = $.trim($("input[@name='deviceModel']").val());
	var adNumber = $.trim($("input[@name='adNumber']").val());
	var status = $.trim($("input[@name='status']").val());
	var des = $.trim($("input[@name='des']").val());
	var area = $.trim($("input[@name='area']").val());
	var group = $.trim($("input[@name='group']").val());
	var phone = $.trim($("input[@name='phone']").val());
	var strpage="<s:url value='/itms/midware/businessOpen.action'/>?"
		+ "&cityId="+cityId
		+ "&deviceModel="+deviceModel
		+ "&deviceId=" + deviceId
		+ "&oui=" + oui
		+ "&deviceSn=" + deviceSn
		+ "&account=" + adNumber
		+ "&username=" + adNumber;
	window.open(strpage,"","left=100,top=150,width=850,height=350,resizable=yes,scrollbars=yes");
}
 
 function GoContent(user_id,gw_type){
 if(gw_type=="2"){
 	var strpage="EGWUserRelatedInfo.jsp?user_id=" + user_id;
 }else{
	var strpage="HGWUserRelatedInfo.jsp?user_id=" + user_id;
 }
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
/**
 *add by zhangsb3 2012��6��11�� start  �½�����
 */	
function getDeviceStrategyXML(){
	$("#tr_sheet_param").show();
}
function showSoftUpHistoryInfo() {
	var obj = document.frm;
	var url="showSoftUpHistoryInfo.jsp";
	$.post(url,{
		device_id:<%=device_id%>
	},function(ajax){	
	    $("#softUpHistory").html(ajax);
	});
}
/**
 *add by zhangsb3 2012��6��11�� end  �½�����
 */	
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
					<img src="../images/cursor_hourglas.gif" border="0" WIDTH="30"
						HEIGHT="30">
				</td>
				<td>
					&nbsp;&nbsp;
				</td>
				<td valign="middle">
					<span id=txtLoading style="font-size: 12px; font-family: ����">�����������룬���Եȡ�</span>
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
								<TD><%=software_version%>
								<%
								if (null != LipossGlobals.getLipossProperty("InstArea.ShortName")
										&& "jx_dx".equals(LipossGlobals
												.getLipossProperty("InstArea.ShortName")) )
								{
									 if ("1".equals(is_check)){ // ԭ����  if ("1".equals(is_normal)){ // chenxj6-20161025
									 %> 
									<font color="green">(����汾)</font>
								<%} else {%>
									<font color="red">(������汾)</font>
								<%}}%>
								</TD>
								<TD class=column align="right">
									�豸����
								</TD>
								<TD><%=device_type%></TD>
							</TR>
						 <%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
						 <TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									���з�ʽ
								</TD>
								<TD width=140><%=access_type %></TD>
								<TD class=column align="right">
									�汾����
								</TD>
								<TD><%=deviceTypeName %></TD>
						 </TR>
						 <%} %>
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
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									�ն˹��
								</TD>
								<TD><%=spec_name%></TD>
								<ms:inArea areaCode="sd_lt" notInMode="false">
								<TD class=column align="right">
								</TD>
								<TD>
								</TD>
								</ms:inArea>
								<ms:inArea areaCode="sd_lt" notInMode="true">
								<TD class=column align="right">
									��ַ��ʽ
								</TD>
								<TD>
									<%
										String ipType = null;
										//�õ��û���Ϣ
										cursor = DeviceAct.getCustomerOfDev(device_id, gw_type);
										fields = cursor.getNext();
										if (fields == null){
											ipType = "";
										}else{
											// ֻѭ��һ�Σ��Ͳ���while��
											if (fields != null){
												String user_id = (String) fields.get("user_id");
												ipType = HGWUserInfoAct.getTabNetServParamByUserId(user_id);
												
											}
										}
									%>
									<%=ipType %>
								</TD>
								</ms:inArea>
							</TR>
							<%if("gs_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) || "ah_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
							<TR bgcolor="#FFFFFF" height="20">
							<TD class=column align="right">
									�豸�汾����
								</TD>
								<TD><%=device_version_type%></TD>
								<TD class=column align="right"></TD><TD></TD>
							</TR>
							<%} %>
							<%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
							<TR bgcolor="#FFFFFF" height="20">
							<TD class=column align="right">
									�ն��ʲ�
								</TD>
								<TD><%=isTelDev%></TD>
								<TD class=column align="right">״̬</TD>
								<TD>
									<%=scrapStatus%>
								</TD>
							</TR>
							<%} %>
							<%if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
							<TR bgcolor="#FFFFFF" height="20">
							<TD class=column align="right">
									�Ƿ�֧�ֹ�è��ʶ
								</TD>
								<TD><%=gigabitport%></TD>
								<TD class=column align="right"></TD><TD></TD>
							</TR>
							<%} %>
							
							<%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									�ն�֧������
								</TD>
								<TD><%=gbbroadband%></TD>

								<TD class=column align="right">
									��������
								</TD>
								<TD><%=accessStyle%></TD>
							</TR>
							<%} %>
							
							<%
								if (null != LipossGlobals.getLipossProperty("InstArea.ShortName")
										&& "js_dx".equals(LipossGlobals
												.getLipossProperty("InstArea.ShortName"))
										&& "1".equals(gw_type))
								{
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									�Ƿ�֧��awifi��ͨ
								</TD>
								<TD colspan= 3><%=is_awifi%></TD>
							</TR>
							<%
							}
							%>
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
							<%if(!"3".equals(LipossGlobals.getLipossProperty("ClusterMode.mode"))){ %>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									�豸��ƽ̨��������״̬
								</TD>
								<TD colspan="3" width="30%">
									<span id="onlineStatus"><%=online_status%></span>&nbsp;&nbsp;
									<input name="onlineStatusGet" type="button" value="��⽻��״̬"
										class="btn_g" onclick="getOnlineStatus()">
								</TD>
							</TR>
							<%} %>
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
							<%--				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right" nowrap>�����֧�ֵ�ҵ�����</TD>
					<TD><textarea width=100% cols=40 rows=4 readonly><%=device_support_code%></textarea></TD>
					<TD class=column align="right">��ǰ��ͨ��ҵ�����</TD>
					<TD><textarea width=100% cols=40 rows=4 readonly><%=device_current_code%></textarea></TD>
				</TR>
		
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right">�豸����ģ��</TD>
					<TD ><textarea cols=40 rows=4 readonly><%=deviceModelTemplate%></textarea></TD>
				<TD class=column align="right" nowrap>Ӳ����֧�ֵ�ҵ�����</TD>
					<TD><textarea width=100% cols=40 rows=4 readonly><%=device_hard_code%></textarea></TD>		
				</TR>
--%>
                            <%  if(!LipossGlobals.isXJDX()){%>
                            <!-- �½����Ų���������� -->
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" valign="top" colspan=4>
									<%
										if ("1".equals(gw_type))
										{
									%>
									<%
											if (LipossGlobals.inArea("sd_lt") || LipossGlobals.inArea("sx_lt") || LipossGlobals.inArea("nx_lt"))
											{
									%>
									RMS������ʷ��Ϣ
									<%
											}
											else
											{
									%>
									ITMS������ʷ��Ϣ
									<%										
											}
									 %>
									<%
										}
										else
										{
									%>
									BBMS������ʷ��Ϣ
									<%
									}
									%>
									
									<%if(!LipossGlobals.inArea("nx_lt")){ %>
									��
									<a href="javascript:showOperation()"
										style="color: 808080; font-size: 9pt;">�鿴</a>��
									<%}else{ %>
									��
									<a href="javascript:toggleOperation()"
										style="color: 808080; font-size: 9pt;" id='his_check'>�鿴</a>��
									<%} %>
								</TD>

							</TR>
							<%} %>
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
							<ms:hasAuth authCode="ShowDevPwd">
							<TR bgcolor="#FFFFFF" height="20">
								<%if (LipossGlobals.inArea("sd_lt") || LipossGlobals.inArea("sx_lt") || LipossGlobals.inArea("jl_lt") || LipossGlobals.inArea("ah_lt")) { %>
								<TD class=column align="right">
								��ͨά���˺�
								</TD>
								<%} else if(LipossGlobals.inArea("nx_lt")){ %>
								<TD class=column align="right">
								��������Ա�˺�
								</TD>
								<%}else{ %>
								<TD class=column align="right">
								����ά���˺�
								</TD>
								<%} %>
								<TD><%=x_com_username%></TD>
								<%if (LipossGlobals.inArea("sd_lt") || LipossGlobals.inArea("sx_lt") || LipossGlobals.inArea("jl_lt") || LipossGlobals.inArea("ah_lt")) { %>
								<TD class=column align="right">
								��ͨά������
								</TD>
								<%} else if(LipossGlobals.inArea("nx_lt")){ %>
								<TD class=column align="right">
								��������Ա����
								</TD>
								<%}else{ %>
								<TD class=column align="right">
								����ά������
								</TD>
								<%} %>
								<TD>
									<span id="compw_span"><%=x_com_passwd%></span>&nbsp;
									<span id="compw_span_new"></span>&nbsp;
									<!-- <input name="compw_span_hid" value="" type="hidden"/> -->
									<input name="compwGen" type="button" value="����" class="btn_g"
										onclick="generateComPw()">
									<input name="compwSet" type="button" value="����" class="btn_g"
										onclick="setComPw()">
								</TD>
							</TR>
							</ms:hasAuth>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan=4>
									�û�������Ϣ
								</TD>
							</TR>
							<%
								//�õ��û���Ϣ
								cursor = DeviceAct.getCustomerOfDev(device_id, gw_type);
								fields = cursor.getNext();
								if (fields == null)
								{
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD align="center" colspan=4>
									�豸�����û�.
								</TD>
							</TR>
							<%}else{
								while (fields != null)
								{
									username = (String) fields.get("username");
									service_name = (String) fields.get("service_name");
									String user_id = (String) fields.get("user_id");
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
							<%}%>
								<%  if(LipossGlobals.getMidWare()){%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan=4>
									�м���豸����
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan="4">
								<input type="hidden" name="status" value="1">
								<input type="hidden" name="area" value="">
								<input type="hidden" name="group" value="">
								<input type="hidden" name="phone" value="">
								<input type="hidden" name="des" value="">
								<input type="button" value="�� ��" class="btn_g"
										onclick="add();">
										&nbsp;&nbsp;&nbsp;
								<input type="button" value="�� ��" class="btn_g"
										onclick="update();">
										&nbsp;&nbsp;&nbsp;
								<input type="button" value="ɾ ��" class="btn_g"
										onclick="del();">
										&nbsp;&nbsp;&nbsp;
								<input type="button" value="�����м��" class="btn_g"
										onclick="midareOpen();">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" value="ҵ�����" class="btn_g"
										onclick="business_open();">
								</TD>
							</TR>
							<% }%>
							
							<%}
								if (!"1".equals(gw_type))
								{
									//�õ��û���Ϣ
									String customer_name;
									String customer_pwd;
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
										fields = cursor.getNext();
							%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									�ͻ�����
								</TD>
								<TD><%=customer_name%></TD>
								<TD class=column align="right">
									�ͻ�����
								</TD>
								<TD><%=customer_pwd%></TD>
							</TR>
							
							<%
								}
								}
							%>
							<% if(LipossGlobals.isXJDX()){%>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" colspan=4>
									��ǰ�豸�������������Ϣ  <input type="button" value="�鿴����XML" class="btn_g"
										onclick="getDeviceStrategyXML();">
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									�豸�ϰ汾
								</TD>
								<TD><%=oldDeviceType%></TD>
								<TD class=column align="right">
									�豸Ŀ��汾
								</TD>
								<TD><%=newDeviceType%></TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="right">
									����ִ��״̬
								</TD>
								<TD><%=stragyStatusName%></TD>
								<TD class=column align="right">
									ִ�н��
								</TD>
								<TD><%=result%></TD>
							</TR>
							<TR id ="tr_sheet_param" bgcolor="#FFFFFF" height="20" style="display: none">
								<TD class=column width="15%" align='right'>
									���Բ�����Ϣ
								</TD>
								<TD width="85%" colspan="3" >
									<%=sheetParam %>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" height="20">
								<TD class=column align="center" valign="top" colspan=4>
									�豸�汾�仯��ʷ��¼
									��
									<a href="javascript:showSoftUpHistoryInfo()"
										style="color: 808080; font-size: 9pt;">�鿴</a>��
								</TD>
							</TR>
							<TR>
								<TD class=column align="center" colspan=4>
									<div id="softUpHistory" style="width: 100%; height: 100%; z-index: 1; top: 100%;">
									</div>
								</TD>
							</TR>
							<%} %>
							<TR>
								<TD colspan="4" align="center" class=foot>
									<INPUT TYPE="submit" value=" �� �� " class=jianbian
										onclick="javascript:window.close();">
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