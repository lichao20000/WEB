<%--
FileName	: selectDevice.jsp
Date		: 2009��2��2��
Desc		: ѡ���豸.
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/inmp/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

//��ѯ�豸�ķ�ʽת��
function deviceSelect_ShowDialog(param)
{
	
	//�����豸�汾����ѯ
	if(param==0)
	{
		this.tr11.style.display="";
		this.tr12.style.display="";
		this.tr13.style.display="";
		this.tr14.style.display="";
		this.tr15.style.display="none";  // VOIP�绰����
		this.tr16.style.display="none";  // iTV�ʺ�
		this.tr17.style.display="none";  // ������ʺ�
		this.tr21.style.display="none";
		this.tr31.style.display="none";
		this.tr41.style.display="none";
		this.tr42.style.display="none";
	}
	
	//�����û�����ѯ
	if(param==1)
	{
		this.tr11.style.display="none";
		this.tr12.style.display="none";
		this.tr13.style.display="none";
		this.tr14.style.display="none";
		this.tr15.style.display="none";  // VOIP�绰����
		this.tr16.style.display="none";  // iTV�ʺ�
		this.tr17.style.display="none";  // ������ʺ�
		this.tr21.style.display="";
		this.tr31.style.display="none";
		this.tr41.style.display="none";
		this.tr42.style.display="none";
	}
	
	//�����豸���к�����ѯ
	if(param==2)
	{
		this.tr11.style.display="none";
		this.tr12.style.display="none";
		this.tr13.style.display="none";
		this.tr14.style.display="none";
		this.tr15.style.display="none";  // VOIP�绰����
		this.tr16.style.display="none";  // iTV�ʺ�
		this.tr17.style.display="none";  // ������ʺ�
		this.tr21.style.display="none";
		this.tr31.style.display="";
		this.tr41.style.display="none";
		this.tr42.style.display="none";
	}
	
	//�����ļ���������ѯ����ѯ
	if(param==3)
	{
		this.tr11.style.display="none";
		this.tr12.style.display="none";
		this.tr13.style.display="none";
		this.tr14.style.display="none";
		this.tr15.style.display="none";  // VOIP�绰����
		this.tr16.style.display="none";  // iTV�ʺ�
		this.tr17.style.display="none";  // ������ʺ�
		this.tr21.style.display="none";
		this.tr31.style.display="none";
		this.tr41.style.display="";
		this.tr42.style.display="";
	}
	
	//����VOIP�绰��������ѯ����ѯ  add by zhangchy 2012-02-21 
	if(param==4)
	{
		this.tr11.style.display="none";
		this.tr12.style.display="none";
		this.tr13.style.display="none";
		this.tr14.style.display="none";
		this.tr15.style.display="";      // VOIP�绰����
		this.tr16.style.display="none";  // iTV�ʺ�
		this.tr17.style.display="none";  // ������ʺ�
		this.tr21.style.display="none";
		this.tr31.style.display="none";
		this.tr41.style.display="none";
		this.tr42.style.display="none";
	}
	
	//����iTV�ʺ�����ѯ����ѯ  add by zhangchy 2012-02-29
	if(param==5)
	{
		this.tr11.style.display="none";
		this.tr12.style.display="none";
		this.tr13.style.display="none";
		this.tr14.style.display="none";
		this.tr15.style.display="none";  // VOIP�绰����
		this.tr16.style.display="";      // iTV�ʺ�
		this.tr17.style.display="none";  // ������ʺ�
		this.tr21.style.display="none";
		this.tr31.style.display="none";
		this.tr41.style.display="none";
		this.tr42.style.display="none";
	}
	
	//���ݿ�����ʺ�����ѯ����ѯ add by zhangchy 2012-02-29
	if(param==6)
	{
		this.tr11.style.display="none";
		this.tr12.style.display="none";
		this.tr13.style.display="none";
		this.tr14.style.display="none";
		this.tr15.style.display="none";  // VOIP�绰����
		this.tr16.style.display="none";  // iTV�ʺ�
		this.tr17.style.display="";      // ������ʺ�
		this.tr21.style.display="none";
		this.tr31.style.display="none";
		this.tr41.style.display="none";
		this.tr42.style.display="none";
	}
}

//���̡��ͺż���
function deviceSelect_change_select(type){
	
	var city_id = $("select[@name='city_id']").val();
	var vendor_id = $("select[@name='vendor_id']").val();
	var device_model_id = $("select[@name='device_model_id']").val();
	var devicetype_id = $("select[@name='devicetype_id']").val();
	
	//���ݳ��̲�ѯ�豸�ͺţ��豸�汾����
	if("vendor"==type){

		var url = "selectDeviceTag!getDeviceModel.action";
		$.post(url,{
			vendor_id:vendor_id
		},function(ajax){
			deviceSelect_parseMessageVendor(ajax);
		});
	}
	
	//�����ͺŲ�ѯ�豸�汾
	if("model"==type){
		var url = "selectDeviceTag!getVersion.action";
		$.post(url,{
			vendor_id:vendor_id,
			device_model_id:device_model_id
		},function(ajax){
			deviceSelect_parseMessageModel(ajax);
		});
		
	}
	
}

//������ѯ�豸�ͺŷ���ֵ�ķ���
function deviceSelect_parseMessageVendor(ajax){
	
	var select_model = $("select[@name='device_model_id']");
	var select_DeviceType = $("select[@name='devicetype_id']");
	select_model.html("");
	select_DeviceType.html("");
	
	var option = "<option value='-1'>==��ѡ��==</option>";
	try{
		select_model.append(option);
	}catch(e){
		alert("�豸�ͺż���ʧ�ܣ�");
		return false;
	}
	
	var option = "<option value='-1'>==����ѡ���豸�ͺ�==</option>";
	try{
		select_DeviceType.append(option);
	}catch(e){
		alert("�豸�ͺż���ʧ�ܣ�");
		return false;
	}
	
	if(""==ajax){
		select_DeviceType.attr("value","-1");
		return;
	}
	
	var lineModel = ajax.split("#");
	
	if(!typeof(lineModel) || !typeof(lineModel.length)){
		return false;
	}
	
	for(var i=0;i<lineModel.length;i++){
		var oneElement = lineModel[i].split(",");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		//����ÿ��value��text��ǵ�ֵ����һ��option����
		option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		try{
			select_model.append(option);
		}catch(e){
			alert("�豸�ͺż���ʧ�ܣ�");
		}
	}
	
	select_model.attr("value","-1");
}

//�����豸�ͺŲ�ѯ�豸�汾
function deviceSelect_parseMessageModel(ajax){

	var select_DeviceType = $("select[@name='devicetype_id']");
	select_DeviceType.html("");
	
	var option = "<option value='-1'>==��ѡ��==</option>";
	try{
		select_DeviceType.append(option);
	}catch(e){
		alert("�豸�汾����ʧ�ܣ�");
		return false;
	}

	if(""==ajax){
		select_DeviceType.attr("value","-1");
		return;
	}
	
	var lineDeviceType = ajax.split("#");
	
	if(!typeof(lineDeviceType) || !typeof(lineDeviceType.length)){
		return false;
		
	}
	
	for(var i=0;i<lineDeviceType.length;i++){
		var oneElement = lineDeviceType[i].split(",");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		//����ÿ��value��text��ǵ�ֵ����һ��option����
		option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		try{
			select_DeviceType.append(option);
		}catch(e){
			alert("�豸�汾����ʧ�ܣ�");
		}
	}
	select_DeviceType.attr("value","-1");
}

//�����û��˺Ų�ѯ
function deviceSelect_relateDeviceByUsername(){
	
	var select_type = "1";
	
	var hguser = $("input[@name='hguser']").val();
	var selectType = $("input[@name='selectType']").val();
	var jsFunctionName = $("input[@name='jsFunctionName']").val();
	var jsFunctionNameBySn = $("input[@name='jsFunctionNameBySn']").val();

	if(""==hguser || null==hguser){
		alert("�������������û��˺�");
		$("input[@name='hguser']")[0].focus();
		return false;
	}

	var url = "selectDeviceTag!getDeviceByUsername.action";
	$.post(url,{
		select_type:select_type,
		hguser:hguser,
		selectType:selectType,
		jsFunctionName:jsFunctionName,
		jsFunctionNameBySn:jsFunctionNameBySn,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
}


/**
 * ����VOIP�绰�����ѯ
 */
function deviceSelect_relateDeviceByVOIPNum(){
	
	var select_type = "3";
	
	var voipPara = $("input[@name='voipPara']").val();
	var selectType = $("input[@name='selectType']").val();
	var jsFunctionName = $("input[@name='jsFunctionName']").val();
	var jsFunctionNameBySn = $("input[@name='jsFunctionNameBySn']").val();

	if(""==voipPara || null==voipPara){
		alert("������VOIP�绰���룡");
		$("input[@name='voipPara']")[0].focus();
		return false;
	}

	var url = "selectDeviceTag!getDeviceByVoipTelNum.action";
	$.post(url,{
		select_type:select_type,
		voipPara:voipPara,
		selectType:selectType,
		jsFunctionName:jsFunctionName,
		jsFunctionNameBySn:jsFunctionNameBySn,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
}



/**
 * ��iTV
 * ���󵥣�JSDX_ITMS-REQ-20120222-LUHJ-001
 */
function deviceSelect_relateDeviceByItvUserName(){
	
	var select_type = "5";
	
	var iTVUserName = $("input[@name='iTVUserName']").val();
	var selectType = $("input[@name='selectType']").val();
	var jsFunctionName = $("input[@name='jsFunctionName']").val();
	var jsFunctionNameBySn = $("input[@name='jsFunctionNameBySn']").val();

	if(""==iTVUserName || null==iTVUserName){
		alert("������iTV�ʺţ�");
		$("input[@name='iTVUserName']")[0].focus();
		return false;
	}

	var url = "selectDeviceTag!getDeviceByITVUserName.action";
	$.post(url,{
		select_type:select_type,
		itvUserName:iTVUserName,
		selectType:selectType,
		jsFunctionName:jsFunctionName,
		jsFunctionNameBySn:jsFunctionNameBySn,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
}



/**
 * ��������ʺ�
 * ���󵥣�JSDX_ITMS-REQ-20120222-LUHJ-001
 */
function deviceSelect_relateDeviceByWideNet(){
	
	var select_type = "6";
	
	var wideNetPara = $("input[@name='wideNetPara']").val();
	var selectType = $("input[@name='selectType']").val();
	var jsFunctionName = $("input[@name='jsFunctionName']").val();
	var jsFunctionNameBySn = $("input[@name='jsFunctionNameBySn']").val();

	if(""==wideNetPara || null==wideNetPara){
		alert("���������ʺţ�");
		$("input[@name='wideNetPara']")[0].focus();
		return false;
	}

	var url = "selectDeviceTag!getDeviceByWideAccount.action";
	$.post(url,{
		select_type:select_type,
		wideNetPara:wideNetPara,
		selectType:selectType,
		jsFunctionName:jsFunctionName,
		jsFunctionNameBySn:jsFunctionNameBySn,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
}

//�����豸���кź�IP��ѯ
function deviceSelect_relateDeviceBySerialno(){
	
	var select_type = "2";
	
	var selectType = $("input[@name='selectType']").val();
	var jsFunctionName = $("input[@name='jsFunctionName']").val();
	var device_serialnumber = $("input[@name='device_serialnumber']").val();
	var loopback_ip = $("input[@name='loopback_ip_']").val();
	var jsFunctionNameBySn = $("input[@name='jsFunctionNameBySn']").val();
	
	if(device_serialnumber.length>0 && device_serialnumber.length<6){
		alert("������������λ�豸���кţ�");
		$("input[@name='device_serialnumber']").focus();
		return false;
	}
	
	if(""==device_serialnumber || null==device_serialnumber){
		if(""==loopback_ip || null==loopback_ip){
			alert("����������һ���ѯ������");
			$("input[@name='device_serialnumber']").focus();
			return false;
		}
	}else{
		device_serialnumber = $.trim(device_serialnumber);
	}

	var url = "selectDeviceTag!getDeviceBySerialno.action";
	$.post(url,{
		select_type:select_type,
		device_serialnumber:device_serialnumber,
		loopback_ip:loopback_ip,
		selectType:selectType,
		jsFunctionName:jsFunctionName,
		jsFunctionNameBySn:jsFunctionNameBySn,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
}

//�߼���ѯ���������ء����̵Ȳ�ѯ
function deviceSelect_relateDeviceBySenior(){

	var select_type = "0";
	
	if(!deviceSelect_filterselected()){
		alert("����д��ز�ѯ������");
		return false;
	}	
	
	var city_id = $("select[@name='city_id']").val();
	var office_id = $("select[@name='office_id']").val();
	var vendor_id = $("select[@name='vendor_id']").val();
	var device_model_id = $("select[@name='device_model_id']").val();
	var devicetype_id = $("select[@name='devicetype_id']").val();
	var loopback_ip = $("input[@name='loopback_ip']").val();
	var online_status = $("select[@name='online_status']").val();
	var selectType = $("input[@name='selectType']").val();
	var jsFunctionName = $("input[@name='jsFunctionName']").val();
	var jsFunctionNameBySn = $("input[@name='jsFunctionNameBySn']").val();
	var listControl = $("input[@name='listControl']").val();

	var url = "selectDeviceTag!getDeviceByCity.action";
	$.post(url,{
		city_id:city_id,
		office_id:office_id,
		select_type:select_type,
		vendor_id:vendor_id,
		device_model_id:device_model_id,
		devicetype_id:devicetype_id,
		loopback_ip:loopback_ip,
		online_status:online_status,
		selectType:selectType,
		jsFunctionName:jsFunctionName,
		jsFunctionNameBySn:jsFunctionNameBySn,
		listControl:listControl,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
}

//�ж���û���豸��ѡ��
//��:����true
//��:����false
function deviceSelectedCheck(){
	
	var oSelect = document.all("device_id");
	
	if(oSelect !=null ) {
		for(var i=0; i<oSelect.length; i++) {
			if(oSelect[i].checked) {
				return true;
			}
		}
		if(oSelect.checked){
			return true;
		}
		return false;
	}else{
		return false;
	}
}

//ȫѡ��ť
function deviceSelect_selectAll(elmID){
	
	t_obj = document.all(elmID);
	if(!t_obj) return;
	obj = event.srcElement;
	if(obj.checked){
		if(typeof(t_obj) == "object" ) {
			if(typeof(t_obj.length) != "undefined") {
				for(var i=0; i<t_obj.length; i++){
					t_obj[i].checked = true;
				}
			} else {
				t_obj.checked = true;
			}
		}
	}else{
		if(typeof(t_obj) == "object" ) {
			if(typeof(t_obj.length) != "undefined") {
				for(var i=0; i<t_obj.length; i++){
					t_obj[i].checked = false;
				}
			} else {
				t_obj.checked = false;
			}
		}
	}
}

//�ڸ߼���ѯʱ���ж��Ƿ����еĲ�ѯ������û��ѯ
function deviceSelect_filterselected(){
	var city_id = $("select[@name='city_id']").val();
	if(""!=city_id && null!=city_id && "-1"!=city_id){
		return true;
	}
	var vendor_id = $("select[@name='vendor_id']").val();
	if(""!=vendor_id && null!=vendor_id && "-1"!=vendor_id){
		return true;
	}
	var loopback_ip = $("input[@name='loopback_ip']").val();
	if(""!=loopback_ip && null!=loopback_ip){
		return true;
	}
	var online_status = $("select[@name='online_status']").val();
	if(""!=online_status && null!=online_status && "-1"!=online_status){
		return true;
	}
	return false;
}

//����URL
var deviceSelect_url = "selectDeviceTag!reload.action";

var selectType='<%=request.getParameter("selectType")%>';
var jsFunctionName='<%=request.getParameter("jsFunctionName")%>';
var jsFunctionNameBySn='<%=request.getParameter("jsFunctionNameBySn")%>';
var maxFileNum='<%=request.getParameter("maxFileNum")%>';

var byVoipTelNum = '<%=request.getParameter("byVoipTelNum")%>';  //  ��VOIP�绰�����ѯ add by zhangchy 2012-02-21
var byItv = '<%=request.getParameter("byItv")%>';                //  ��iTV�ʺŲ�ѯ add by zhangchy 2012-02-29
var byWideNet = '<%=request.getParameter("byWideNet")%>';        //  ��������ʺŲ�ѯ add by zhangchy 2012-02-29
var byDeviceno='<%=request.getParameter("byDeviceno")%>';
var buUsername='<%=request.getParameter("buUsername")%>';
var byCity='<%=request.getParameter("byCity")%>';
var byImport='<%=request.getParameter("byImport")%>';
var listControl='<%=request.getParameter("listControl")%>';
var div_device_height = '<%=request.getParameter("div_device_height")%>';
var gw_type = '<%=request.getParameter("gw_type")%>';
//alert(gw_type+'js');
//��Ϊ�״ν������
$.post(deviceSelect_url,{
	selectType:selectType,
	jsFunctionName:jsFunctionName,
	jsFunctionNameBySn:jsFunctionNameBySn,
	maxFileNum:maxFileNum,
	byDeviceno:byDeviceno,
	buUsername:buUsername,
	byVoipTelNum:byVoipTelNum,  // add by zhangchy 2012-02-21
	byCity:byCity,
	byImport:byImport,
	listControl:listControl
},function(ajax){
	
	$("div[@id='selectDevice']").html("");
	$("div[@id='selectDevice']").append(ajax);

	if("null"!=div_device_height && ""!=div_device_height){
		$("div[@id='div_device']").height(div_device_height);
	}
	
});


</SCRIPT>