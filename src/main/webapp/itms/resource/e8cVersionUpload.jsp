<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../timelater.jsp"%>
<jsp:useBean id="fileManage" scope="request" class="com.linkage.litms.filemanage.FileManage"/>
<%
    int fileType = 4;
    Cursor cursor = (Cursor)fileManage.getCursor(fileType);
    Map fields = cursor.getNext();
%>
<html>
<link href="<s:url value="/css3/css_s.css"/>" rel="stylesheet"
	type="text/css" />
<meta http-equiv="x-ua-compatible" content="IE=7" >
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/slide.js"/>"></script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<title>e8-c�淶�汾�ϴ�</title>
<script language="JavaScript">
var operType = <%=request.getParameter("operType")%>;
//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
 			dyniframe[i].style.display="block"
 			//����û����������NetScape
 			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
  				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
  			//����û����������IE
 			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
  				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
			 }
		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
		tempobj.style.display="block"
		}
	}
}

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 
//У���ļ������Ƿ����
function checkFillName(inputVal){
	var uploadFileName = inputVal.value;
	var url = "<s:url value='/itms/resource/E8cVersionQuery!checkFillName.action'/>";
	$.post(url,{
		uploadFileName:uploadFileName
	},function(ajax){
		if(ajax == "-1"){
			alert("���ļ����Ѵ���,��������д!");
			$("#uploadButton").css("backgroundColor","#c6c6c6");
			document.getElementById("uploadButton").disabled = true;
			$("#cancelButton").css("backgroundColor","#0066ff");
		}else{
			$("#uploadButton").css("backgroundColor","#0066ff");
			document.getElementById("uploadButton").disabled = false;
			$("#cancelButton").css("backgroundColor","#c6c6c6");
		}
	});
}
//У������汾�Ƿ����ϴ�
function checkSoftVersion(inputVal){
	var devicetype_id = inputVal.value;
	var url = "<s:url value='/itms/resource/E8cVersionQuery!checkSoftVersion.action'/>";
	$.post(url,{
		devicetype_id:devicetype_id
	},function(ajax){
		if(ajax == "-1"){
			alert("�ð汾���ϴ�,������ѡ��!");
			$("#uploadButton").css("backgroundColor","#c6c6c6");
			document.getElementById("uploadButton").disabled = true;
			$("#cancelButton").css("backgroundColor","#0066ff");
		}else{
			$("#uploadButton").css("backgroundColor","#0066ff");
			document.getElementById("uploadButton").disabled = false;
			$("#cancelButton").css("backgroundColor","#c6c6c6");
		}
	});
}
//���ݳ��������豸�ͺ�
function getDeviceModel(oselect){
	var url = "<s:url value='/itms/resource/E8cVersionQuery!getDevModelSelectList.action'/>";
	var vendor_id = oselect.value;
	$.post(url,{
		vendor_id:vendor_id
	},function(ajax){
		parseMessageModel(ajax);
	});
	
	//������ѯ�豸�ͺ�
	function parseMessageModel(ajax){
		var devModelObj = $("#selectDevModel");
		var str = '';
		devModelObj.html("");
		str = "<option value=\"-1\" selected>==��ѡ���ͺ�==</option>";
		str += ajax;
		devModelObj.append(str);
	}
}
//�����豸�ͺ���������汾
function getsoftVersion(){
	var device_model = $.trim($("select[@name='device_model']").val());
	var url = "<s:url value='/itms/resource/E8cVersionQuery!getsoftVersionSelectList.action'/>";
	$.post(url,{
		device_model:device_model
	},function(ajax){
		parseMessageModel(ajax);
	});
	
	//������ѯ�豸�ͺ�
	function parseMessageModel(ajax){
		var softVersionObj = $("#selectSoftVersion");
		var str = '';
		softVersionObj.html("");
		str = "<option value=\"-1\" selected>==��ѡ���ͺ�==</option>";
		str += ajax;
		softVersionObj.append(str);
	}
}
function showChild(param){
	if(param == "fileserver"){
		var serValue = document.frm.fileserver.value;
		if(serValue == -1){
			document.frm.serUser.value="";
			document.frm.serPass.value="";
		}else{
			var arrServer = serValue.split("|");
			document.frm.serUser.value=arrServer[3];
			document.frm.serPass.value=arrServer[4];
		}		
	}
	if(param == 'vendor_name'){
		queryModel(param);
	}
	
	if(param == 'device_model_2'){
		queryVersion();
	}
	if(param == 'device_model'){
		queryHardware();
	}
}
function doUpload(){
	document.getElementById("it_ok").style.display = "";
	document.getElementById("it_ok").innerHTML = "�����ϴ�,���Ե�..";
	$("#uploadButton").css("backgroundColor","#c6c6c6");
	var _fullFileName = $("input[@name='fullFileName']");
	var vendor_name = $("#vendor_name option:selected").text();
	var device_model = $("#selectDevModel option:selected").text();
	var softVersion = $("#selectSoftVersion option:selected").text();
	var _fileserver = $("select[@name='fileserver']"); 
	var serUser = $.trim($("input[@name='serUser']").val());
	var serpass = $.trim($("input[@name='serpass']").val());
	var _file1 = $("input[@name='file1']");
	var uploadName =  _fullFileName.val() + "." + _file1.val().substring(_file1.val().lastIndexOf('.') + 1);
	$("input[@name='uploadName']").val(uploadName);
	if("" == _fullFileName.val()){
		alert("����д�ļ�����!");
		_fullFileName.focus();
		return;
	}
	if("" == vendor_name){
		alert("��ѡ����!");
		$("#vendor_name").focus();
		return;
	}
	var respqce = new RegExp(" ","g");
	var replus = /\+/g;
	strDeviceModel = device_model.replace(respqce,'AGELINK').replace(replus,'PLUSLINK') + ",";
	if(strDeviceModel==''){
		alert("��ѡ���ͺţ�");
		$("#device_model").focus();
		return;
	}
	strDeviceModel += "LINKAGE";
	if("" == softVersion){
		alert("��ѡ������汾!");
		$("#softVersion").focus();
		return;
	}
	if("-1" == _fileserver.val()){
		alert("��ѡ��洢·��!");
		_fileserver.focus();
		return;
	}
	if("" == _file1.val()){
		alert("��ѡ���ļ�!");
		_file1.focus();
		return;
	}
	var url = _fileserver.val();
	var urlParameter = url + "/doUpload.jsp?refresh="+Math.random() + "&path=FILE/SOFT"+"&fileRename=" + _fullFileName.val() + "." + _file1.val().substring(_file1.val().lastIndexOf('.') + 1) 
		+"&dir_id=4&seruser=&serpass=&file_status=1&softwareversion"+ softVersion.replace(respqce,'AGELINK').replace(replus,'PLUSLINK')
		+"&device_model="+strDeviceModel+"&hardwareversion=&tablename=tab_software_file&cityList=&serviceList=";
	$("input[@name='urlParameter']").val("");
	document.getElementById("urlParameter").value = urlParameter;
	document.frm.submit();
	return;
}
function cancel(obj){
	obj.reset();
	document.getElementById("it_ok").style.display="none";
	$("#uploadButton").css("backgroundColor","#0066ff");
	$("#cancelButton").css("backgroundColor","#c0c0c0");
	document.getElementById("uploadButton").disabled = false;
}
</script>
</head>
<body>
	<form id="frm" name="frm" action ="<s:url value='/itms/resource/E8cVersionQuery!uploadLocalFile.action'/>" method="post" enctype ="multipart/form-data" target="dataForm">
		<div class="it_main">
			<h1 class="it_webtt">�汾�ļ����</h1>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="import_table">
				<tr>
					<td class="tit">
						<span class="red_x">*</span>&nbsp;&nbsp;���ƣ�
					</td>
					<td>
						<input class="imp_ipt" type="text" name="fullFileName" onblur="checkFillName(this);"/>
						<span class="prompt">���Ʋ��ܰ������ģ��ո�������ַ�,����ʹ������汾�����ƣ�</span>
					</td>
				</tr>
				<tr>
				    <td class="tit">
				    	<span class="red_x">*</span>&nbsp;&nbsp;�豸���̣�
				    </td>
				    <td id="vendor_name">
				    	<s:select list="vendorList" name="vendor_id" headerKey="-1" 
							headerValue="��ѡ����" listKey="vendor_id" listValue="vendorName" 
							value="vendor_id" onchange="getDeviceModel(this);" theme="simple" cssClass="gj_select"/>
				   	</td>
				</tr>
				 <tr>
				    <td class="tit">
				    	<span class="red_x">*</span>&nbsp;&nbsp;�豸�ͺţ�
				    </td>
				    <td>
				    	<select id="selectDevModel" name="device_model" class="gj_select" onchange="getsoftVersion();">
							<option value="-1">==��ѡ����==</option>
						</select>
					</td>
				 </tr>
				 <tr>
				    <td class="tit">
				    	<span class="red_x">*</span>&nbsp;&nbsp;����汾��
				    </td>
				    <td>
				    	<select id="selectSoftVersion" name="devicetype_id" class="gj_select" onblur="checkSoftVersion(this);">
				      		<option value="-1">==��ѡ���豸�ͺ�==</option>
				    	</select>
				    </td>
				 </tr>
				 <tr>
				    <td class="tit">
				    	<span class="red_x">*</span>&nbsp;&nbsp;�洢·����
				    </td>
				    <td>
				    	<select  name="fileserver" class="bk" onchange="showChild('fileserver')">
				    		<option value="-1">--��ѡ�������--</option>
							<option value="192.168.208.102">192.168.208.102</option>
							<!-- <option value="http://192.168.2.4:7000/FileServer">192.168.2.4</option> -->
							<!-- <option value="http://192.168.2.20:30779/FileServer">192.168.2.20</option> -->
						</select>
				    </td>
				</tr>
				<tr bgcolor="#FFFFFF" style="display:none">
					<td class=column align="right" >�û���</td>
					<td >
						<input type="text" name="serUser" class="bk" value="">			
					</td>
					<td class=column align="right" >���� </td>	
					<td >
						<input type="password" name="serPass" class="bk" value="">
					</td>								
				</tr>
				<tr>
				    <td class="tit">
				    	<span class="red_x">*</span>&nbsp;&nbsp;ѡ��·����
				    </td>
				    <td>
				    	<input style="background-color: white;width: 300px;height: 26px;" class="imp_ipt" type="file" name="file1" class="btn_view" value="���" />
				    	<input type="hidden" name="upload" value="">
				    	<input type="hidden" name="uploadName" value="">
				    	<span class="prompt" id="prompt"></span>
				    </td>
				</tr>
				<tr>
				    <td></td>
				    <td style="padding-top:10px;">
				    	<input type="hidden" id="urlParameter" name="urlParameter" value=""/>
				    	<a href="javascript:doUpload();" id="uploadButton" class="putin">�ϴ�</a>&nbsp;&nbsp;&nbsp;&nbsp;
				    	<a href="javascript:cancel(document.frm);" id="cancelButton" class="cancel">�����ϴ�</a>
				    </td>
				 </tr>
			</table>
		</div>
	</form>
	<div id="it_ok" class="it_stips it_ok" style="display: none;"></div>
	<div id="it_stips" class="it_stips" style="display: none;"></div>
	<div>
		<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
		scrolling="no" width="100%" src=""></iframe>
	</div>
</body>
</html>