
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������ر����ѯ</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
$(function()
{
	gwShare_change("city","-1");
	gwShare_change_select("vendor","-1");
});
function gwShare_change(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value="/gtms/stb/resource/stbReport!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
				$("select[@name='cityId']").html("<option value='-1'>==����ѡ������==</option>");
			});
			break;
		case "cityid":
			var url = "<s:url value='/gtms/stb/resource/stbReport!getCityNext.action'/>";
			var cityId = $("select[@name='gwShare_cityId']").val();
			if("-1"==cityId){
				$("select[@name='gwShare_cityId']").html("<option value='-1'>==����ѡ������==</option>");
				break;
			}
			$.post(url,{
				cityId:cityId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='citynext']"),selectvalue);
				$("select[@name='cityId']").html("<option value='-1'>==����ѡ������==</option>");
			});
			break;
		default:
			alert("δ֪��ѯѡ�");
			break;
	}	
}
function gwShare_change_select(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value="/gtms/stb/resource/stbReport!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/gtms/stb/resource/stbReport!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gtms/stb/resource/stbReport!getDeviceModel.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue);
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gtms/stb/resource/stbReport!getDevicetype.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
			});
			break;	
		default:
			alert("δ֪��ѯѡ�");
			break;
	}	
}

/*------------------------------------------------------------------------------
//������:		deviceSelect_parseMessage
//����  :	ajax 
            	������XXX$XXX#XXX$XXX
            field
            	��Ҫ���ص�jquery����
//����  :	����ajax���ز���
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
//������ѯ�豸�ͺŷ���ֵ�ķ���
function gwShare_parseMessage(ajax,field,selectvalue){
	var flag = true;
	if(""==ajax){
		return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");
	option = "<option value='-1' selected>==��ѡ��==</option>";
	field.append(option);
	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue){
			flag = false;
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		}else{
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		}
		try{
			field.append(option);
		}catch(e){
			alert("�豸�ͺż���ʧ�ܣ�");
		}
	}
	if(flag){
		field.attr("value","-1");
	}
}
function gwShare_queryDevice()
{
    var startIp = $("select[@name='startIp']").val();
    var endIp = $("select[@name='startIp']").val();
   if(startIp!=""&&startIp!=null)
	   {
    if (!checkip(startIp)) {
		alert("��������ȷ����ʼIP��ַ��");
		return false;
	}
	   }
   if(endIp!=""&&endIp!=null){
    if (!checkip(endIp)) {
		alert("��������ȷ�Ľ���IP��ַ��");
		return false;
	}}

    document.gwShare_selectForm.submit();
}
function checkip(str) {
	var pattern = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
	return pattern.test(str);
}
/*------------------------------------------------------------------------------
//������:		trim
//����  :	str �������ַ���
//����  :	���ݴ���Ĳ�������ȥ�����ҵĿո�
//����ֵ:		trim��str��
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"];

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes";

function dyniframesize() 
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
   			dyniframe[i].style.display="block";
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
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
  		tempobj.style.display="block";
		}
	}
}

$(function(){
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
});

</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<body>
<form name="gwShare_selectForm" id="gwShare_selectForm" action="<s:url value="/gtms/stb/resource/stbReport!query.action"/>" target="dataForm">
<table width="100%" align="center" class="querytable">
			<tr>
				<td><img height=20 src="<s:url value='/images/bite_2.gif'/>"
					width=24> ����ǰ��λ�ã���������ر����ѯ</td>
			</tr>
</table>
<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="4" class="title_1" id="gwShare_thTitle">��������ر����ѯ</td></tr>
	<TR id="gwShare_tr21" >
		<TD align="right" class="title_2" width="15%" >��    ��</TD>
		<TD align="left" width="35%">
			<select name="gwShare_cityId" class="bk" onchange="gwShare_change('cityid','-1')">
				<option value="-1">==��ѡ��==</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%" >�¼���   ��</TD>
		<TD align="left" width="35%">
			<select name="citynext" class="bk" >
				<option value="-1">����ѡ������</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr22" >
		<TD align="right" class="title_2" width="15%">��    ��</TD>
		<TD width="35%">
			<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
				<option value="-1">==��ѡ��==</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%">�豸�ͺ�</TD>
		<TD align="left" width="35%">
			<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
				<option value="-1">����ѡ����</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr22" >
		<TD align="right" class="title_2" width="15%">�豸�汾</TD>
		<TD width="35%">
			<select name="gwShare_devicetypeId" class="bk" onchange="">
				<option value="-1">����ѡ���豸�ͺ�</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%">����״̬</TD>
		<TD width="35%">
			<select name="gwShare_onlineStatus" class="bk">
				<option value="-1">==��ѡ��==</option>
				<option value="0">����</option>
				<option value="1">����</option>
			</select>
		</TD>	
	</TR>
	<TR id="gwShare_tr24"  >
		<TD align="right" class="title_2" width="15%">��ʼIP</TD>
		<TD width="35%">
			<input type="text" name="startIp" value="" size="25" maxlength="40" class="bk"/>
		</TD>
		<TD align="right" class="title_2" width="15%">����IP</TD>
		<TD width="35%">
			<input type="text" name="endIp" value="" size="25" maxlength="40" class="bk"/>
		</TD>
	</TR>
	<tr >
		<td colspan="4" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:gwShare_queryDevice();" 
				name="gwShare_queryButton" > �� ѯ </button>
			</div>
		</td>
	</tr>
</TABLE>
		<br>
		<div>
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
</form>
</body>
</html>