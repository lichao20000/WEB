<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%request.setCharacterEncoding("GBK");%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> Test FileUpload </TITLE>
<script type="text/javascript" src="<s:url value="../Js/CheckForm.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<%
String area = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>

<SCRIPT LANGUAGE="JavaScript">

var area = '<%=area%>'

function IsNull(strValue,strMsg){
	if(Trim(strValue).length>0) return true;
	else{
		alert(strMsg+'����Ϊ��');
		return false;
	}
}
function Trim(strValue){
	var v = strValue;
	var i = 0;
	while(i<v.length){
	  if(v.substring(i,i+1)!=' '){
		v = v.substring(i,v.length) 
		break;
	  }
	  i = i + 1;
	  if(i==v.length){
        v="";
      }
	}

	i = v.length;
	while(i>0){
	  if(v.substring(i-1,i)!=' '){
	    v = v.substring(0,i);
		break;
	  }	
	  i = i - 1;
	}

	return v;
}
function doUpload(){
	var vendorName = parent.document.all("vendor_name");
	var softwareversion = parent.document.all("softwareversion");

    if(!softwareversion || softwareversion.value == -1 || softwareversion.value == ''){
        alert("��ѡ������汾��");
        softwareversion.select();
        return false;
    }

	//var deviceModel = parent.document.getElementsByName("deviceModel");
	var deviceModel = '';
	if(area == 'sx_lt'){
		deviceModel = parent.document.all("device_model_2");
	}else{
		deviceModel = parent.document.all("device_model");
	}
	//var hardwareversion = parent.document.getElementsByName("hardwareversion");
	var hardwareversion = parent.document.getElementById("hardwareversion").value;
	
	var hardwareversionArr = hardwareversion.split(";");
	
	var strDeviceModel = '';
	var strHardwareversion = '';
	if(!IsNull(parent.document.frm.filename.value,"�ļ���")){
		parent.document.frm.filename.focus();
		parent.document.frm.filename.select();
		return false;
	}
	if(parent.document.frm.fileserver.value == -1){
		alert("��ѡ��洢·����");
		parent.document.frm.serUser.focus();
		parent.document.frm.serUser.select();
		return false;
	}
	if(vendorName.value == -1){
		alert("��ѡ���̣�");
		vendorName.select();
		return false;
	}
	if(deviceModel.value == -1){
		alert("��ѡ���ͺţ�");
		vendorName.select();
		return false;
	}
	//if(!deviceModel || (deviceModel.length <=0)){
	//	alert("��ѡ���ͺţ�");
	//	deviceModel.select();
	//	return false;
	//}
	
	if(!hardwareversionArr || (hardwareversionArr.length <=0)){
		alert("û�з���������Ӳ���汾��");
		return false;
	}
	
	var respqce = new RegExp(" ","g");
	var replus = /\+/g;
	//for (var i=0;i<deviceModel.length;i++){
	//	if(deviceModel[i].checked){
	//		//�ո�
	//		strDeviceModel += deviceModel[i].value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + ",";
	//	}
	//}
	strDeviceModel = deviceModel.value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + ",";
	if(strDeviceModel==''){
		alert("��ѡ���ͺţ�");
		return false;
	}
	strDeviceModel += "LINKAGE";
	//for (var i=0;i<hardwareversion.length;i++){
	//	if(hardwareversion[i].checked){
	//		//�ո�
	//		strHardwareversion += hardwareversion[i].value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + ",";
	//	}
	//}
	for (var i=0;i<hardwareversionArr.length;i++){
		// �ո� �����⴦��
		strHardwareversion += hardwareversionArr[i].replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + ",";
	}
	if(strHardwareversion==''){
		alert("û�з���������Ӳ���汾��");
		return false;
	}
	strHardwareversion += "LINKAGE";
	//alert(strDeviceModel);
	//alert(strHardwareversion);
	/**
	modified by zhaixf
	if(parent.document.frm.device_model_id.value == -1){
		alert("��ѡ���豸�ͺţ�");
		parent.document.frm.device_model_id.focus();
		parent.document.frm.device_model_id.select();
		return false;
	}
	if(parent.document.frm.devicetype_id.value == -1){
		alert("��ѡ������汾��");
		parent.document.frm.devicetype_id.focus();
		parent.document.frm.devicetype_id.select();
		return false;
	}
	**/

//modified by yanhj.2007-7-25
//	if(!IsNull(parent.document.frm.serUser.value,"�û���")){
//		parent.document.frm.serUser.focus();
//		parent.document.frm.serUser.select();
//		return false;
//	}
//	if(!IsNull(parent.document.frm.serPass.value,"����")){
//		parent.document.frm.serPass.focus();
//		parent.document.frm.serPass.select();
//		return false;
//	}
//modified end.2007-7-25
	if(!IsNull(document.frm.file1.value,"�ϴ��ļ�")){
		document.frm.file1.focus();
		document.frm.file1.select();
		return false;
	}
	for(var i=0;i< parent.document.frm.inORout.length ;i++){
		if(parent.document.frm.inORout[i].checked){
			inORout = parent.document.frm.inORout[i].value;
			break;
		}
	}
	
	var server = parent.document.frm.fileserver.value;
	var serList = new Array();  
	serList = server.split("|");
	if (inORout == 0) {
		//����URL
		var url = serList[0];
	} else {
		//����URL
		var url = serList[5];
	}
	var path = serList[1];
	var dir_id = serList[2];
	var remark =  parent.document.frm.remark.value;
	
	var filetype = 0;
	if(area != 'sx_lt')
	{
		for(var i=0;i< parent.document.frm.filestatus.length;i++)
		{
			if(parent.document.frm.filestatus[i].checked)
			{
				filetype=parent.document.frm.filestatus[i].value;
				break;
			}
		}
	}
	
	
	// ɽ����ͨ������״̬��Ĭ��Ϊ�����
	if(area == 'sx_lt'){
		filetype = 1;
	}
	
	var citys="";
	var services="";
	for (var i=0;i<parent.document.frm.elements.length;i++)
   	{
		var e = parent.document.frm.elements[i];
		if (e.name == 'city' && e.checked==true)
		{
			citys=citys+","+parent.document.frm.elements[i].value;
		}
		if (e.name == 'service' && e.checked==true)
		{
			services=services+","+parent.document.frm.elements[i].value;
		}
   	}
    citys = citys + ",";
    services = services + ",";
	
	idWait.innerHTML = "�����ϴ�......";
	
	/**
	document.frm.action = url + "/doUpload.jsp?refresh="+Math.random() + "&path="+path+"&fileRename=" + encodeURIComponent(parent.document.frm.filename.value)
							+"&dir_id="+dir_id+"&seruser=" + parent.document.frm.serUser.value +"&serpass="+parent.document.frm.serPass.value
						    +"&remark=" +remark + "&file_status=" + filetype + "&devicetype_id=" + parent.document.frm.devicetype_id.value 
						    + "&device_model_id=" + parent.document.frm.device_model_id.value 
						    + "&tablename=tab_software_file&cityList=" + citys + "&serviceList=" + services;
	**/
	
	//add by zhangcong 2011-11-25
	var uploadType = <%= LipossGlobals.getLipossProperty("uploadFileTransitByWEB")%>
	//alert(uploadType);
	
	var fileRename = encodeURIComponent(parent.document.frm.filename.value);
	
	var urlParameter = url + "/doUpload.jsp?refresh="+Math.random() + "&path="+path+"&fileRename=" + fileRename
		+"&dir_id="+dir_id+"&seruser=" + parent.document.frm.serUser.value +"&serpass="+parent.document.frm.serPass.value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK')
		+"&remark=" +remark + "&file_status=" + filetype + "&softwareversion=" + softwareversion.value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') 
		+ "&device_model=" + strDeviceModel + "&vendor_name=" + vendorName.value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + "&hardwareversion=" + strHardwareversion
		+ "&tablename=tab_software_file&cityList=" + citys + "&serviceList=" + services;

    if(area == 'jl_dx'){
        var softwareversion_jldx = softwareversion.value.substring(0, softwareversion.value.indexOf("("));
        var hardwareversion_jldx = softwareversion.value.substring(softwareversion.value.indexOf("(") + 1, softwareversion.value.indexOf(")"));

        // WebRoot/doUpload.jsp:191  �����
        var str_hardwareversion_jldx = hardwareversion_jldx.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + "," + "LINKAGE";

        urlParameter = url + "/doUpload.jsp?refresh="+Math.random() + "&path="+path+"&fileRename=" + fileRename
            +"&dir_id="+dir_id+"&seruser=" + parent.document.frm.serUser.value +"&serpass="+parent.document.frm.serPass.value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK')
            +"&remark=" +remark + "&file_status=" + filetype + "&softwareversion=" + softwareversion_jldx.replace(respqce,'AGELINK').replace(replus,'PLUSLINK')
            + "&device_model=" + strDeviceModel + "&vendor_name=" + vendorName.value.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + "&hardwareversion=" + str_hardwareversion_jldx
            + "&tablename=tab_software_file&cityList=" + citys + "&serviceList=" + services;
    }

	if(uploadType == '1')
	{
		//�ϴ��ļ�ͨ��WEB��ת
		$("input[@name='urlParameter']").val("");
		document.getElementById("urlParameter").value = urlParameter;
		//alert(document.getElementById("urlParameter").value);
		//$("input[@name='urlParameter']").append(urlParameter);
		//alert(urlParameter);
		
		var fileIsExist = false;
		
		// 2020/04/24 ɽ����ͨ ��У��汾�ļ��Ƿ���� ���ϴ�
		if(area == "sx_lt")
		{
			idWait.innerHTML = "����У��汾�ļ��Ƿ���ڡ�";
			
			var checkUrl = "<s:url value='/gwms/resource/software!checkUploadFile.action'/>";
			document.frm.upload.disabled = true;
			
			$.ajax({
				type:"Post",
				url: checkUrl,
				data: {urlParameter: urlParameter},
				dataType:'json',
				async: false,
				success:function(data){
					console.info("data",data);
					if(data.code > 0){
						fileIsExist = true;
						alert(data.message);
						idWait.innerHTML = data.message;
						
						if(data.code == 1){
							parent.frm.filename.focus();
						}
						
					}else{
						idWait.innerHTML = "�����ϴ�......";
					}
					document.frm.upload.disabled = false;
				},
				error:function(e){
					document.frm.upload.disabled = false;
					alert("�������쳣");
					console.info("e",e);
				}
			});
		}
		
		// ����汾�ļ��Ѵ��� �������ϴ�
		if(fileIsExist){
			return;
		}
		
		document.frm.action = "<s:url value='/gwms/resource/software!uploadLocalFile.action'/>";
		
	}else
	{
		//��ֱַ��
		document.frm.action = urlParameter;
		
	}
	//�ύ��
	//alert(document.frm.action);
	document.frm.submit();
	document.frm.upload.disabled = true;
	return;
	//add by zhangcong 2011-11-25
	
	//////////================���ô��룬�ݴ� 2011-11-25 zhangcong@
	var requestURL = "<s:url value='/gwms/resource/software!checkUploadLocalFile.action'/>";
	//AJAX�ж��ϴ��ļ��Ƿ�Ϸ�
	$.post(requestURL,{
		strRename:fileRename
	},function(ajax){
		//alert(ajax);
		
		//����ļ��Ѿ����ڣ�����ֹ
		if(ajax.indexOf("ERROR:") > -1)
		{
			alert(ajax.substring(ajax.indexOf(":") + 1));
			idWait.innerHTML = ajax.substring(ajax.indexOf(":") + 1);
			return;
		}
		
		document.frm.action = "<s:url value='/gwms/resource/software!uploadLocalFile.action'/>";
		
		//alert(document.frm.action);
		document.frm.submit();
		document.frm.upload.disabled = true;
	});
	//alert("action end!");
	return;
}
</SCRIPT>
<style>
BODY {
	BACKGROUND-COLOR: #F4F4FF; COLOR: #000000; FONT-FAMILY: "����","Arial"; FONT-SIZE: 12px; MARGIN: 0px
}
</style>
</HEAD>

<BODY>
<FORM NAME="frm" METHOD=POST action="" ENCTYPE="multipart/form-data">
<input type="hidden" id="urlParameter" name="urlParameter" value=""/>
<INPUT TYPE="file" NAME="file1" class="bk">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<INPUT TYPE="button" name="upload" value=" �� �� " onclick="doUpload()" class=jianbian>&nbsp;&nbsp;
<SPAN id="idWait"></SPAN>
</FORM>
</BODY>
</HTML>
