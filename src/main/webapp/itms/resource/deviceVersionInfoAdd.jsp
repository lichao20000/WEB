<%--
Author		: Alex.Yan (yanhj@lianchuang.com)
Date		: 2007-11-28
Desc		: TR069: devicetype_list, add;
			  SNMP:	 Ddevicetype list.
--%>

<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<SCRIPT LANGUAGE="JavaScript">

function Init(){
	// ��ʼ������
	
	gwShare_queryChange("2");
	
	// ��ͨ��ʽ�ύ
	/**
	var form = document.getElementById("mainForm");
	 setValue();
	form.action = "<s:url value="/itms/resource/deviceTypeInfo!queryList.action"/>";
	//form.target = "dataForm";
	form.submit();
	**/
}

$(function(){
	Init();
});

</SCRIPT>

</head>
<%@ include file="/toolbar.jsp"%>
<%@ include file="./DeviceType_Info_util.jsp"%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" enctype="multipart/form-data"
			target="dataForm">
		<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">�豸�汾�����</div>
				</td>
				<td><img src="/itms/images/attention_2.gif" width="15"
					height="12">�豸�汾�����</td>
			</tr>
		</table>
		<!-- �߼���ѯpart -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center">
					<tr>
						<th colspan="4" id="gwShare_thTitle">�豸�汾���</th>
					</tr>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">�豸����</TD>
						<TD align="left" width="35%"><select name="vendor" class="bk"
							onchange="gwShare_change_select('deviceModel','-1')">
						</select></TD>
						<TD align="right" class=column width="15%">�豸�ͺ�</TD>
						<TD width="35%"><select name="device_model" class="bk">
							<option value="-1">==��ѡ����==</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">Ӳ���汾</TD>
						<TD align="left" width="35%"><INPUT TYPE="text"
							NAME="hard_version" maxlength=30 class=bk size=20>&nbsp;<font
							color="#FF0000">*</font></TD>
						<TD align="right" class=column width="15%">����汾</TD>
						<TD width="35%" nowrap><INPUT TYPE="text" NAME="soft_version"
							maxlength=30 class=bk size=20>&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">�ر�汾</TD>
						<TD align="left" width="35%">
							<INPUT TYPE="text"
							NAME="spec_version" maxlength=30 class=bk size=20>&nbsp;<font
							color="#FF0000">*</font></TD>
						<TD align="right" class=column width="15%">�ļ��ϴ�</TD>
						<TD width="35%" nowrap>
						<!--  
						<input type="file" name="file_path" id="file_path"
						 size="40" />&nbsp;<font color='red'>*</font>
						--> 
						 <input type="file"  name="file_path" id="file_path"   size="35" />
						</TD>
					</TR>

					<tr bgcolor="#FFFFFF">
						<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button"
							class=jianbian name="gwShare_reButto" value=" �� �� "
							onclick="javascript:save();" />
						<input type="button"
							class=jianbian name="gwShare_reButto" value=" �� �� "
							onclick="javascript:queryReset();" />
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</FORM>
		<input type='hidden' name="gw_type" value="<s:property value="gw_type" />" />
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			<iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe>
		</TD>
	</TR>
</TABLE>
</body>
<%@ include file="/foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">

// ����
function queryReset(){
	reset();
}

function  reset(){
	
	    document.mainForm.vendor.value="-1";
	    document.mainForm.device_model.value="-1";
		document.mainForm.hard_version.value="";
		document.mainForm.soft_version.value="";
		document.mainForm.spec_version.value="";
		document.mainForm.file_path.value = "";
		$("input[@name='file_path']").val("");
		//document.mainForm.start_time.value="";
		//document.mainForm.end_time.value="";
}

// ����
function save()
{
	trimAll();
	
	var vendor = $("select[@name='vendor']").val();
	var device_model = $("select[@name='device_model']").val();
	var spec_version = $("input[@name='spec_version']").val();
	var hard_version = $("input[@name='hard_version']").val();
	var soft_version = $("input[@name='soft_version']").val();
	var file_path = $("input[@name='file_path']").val();
	
	// ����У��
	if($("select[@name='vendor']").val() == "-1")
	{
		alert("��ѡ���̣�");
		$("select[@name='vendor']").focus();
		return;
	}
	if($("select[@name='device_model']").val() == "-1")
	{
		alert("��ѡ���ͺţ�");
		$("select[@name='device_model']").focus();
		return;
	}
	
	if($("input[@name='hard_version']").val() == "")
	{
		alert("������Ӳ���汾��");
		$("input[@name='hard_version']").focus();
		return;
	}
	
	if($("input[@name='soft_version']").val() == "")
	{
		alert("����������汾��");
		$("input[@name='soft_version']").focus();
		return;
	}
	
	if($("input[@name='spec_version']").val() == "")
	{
		alert("�������ر�汾��");
		$("input[@name='spec_version']").focus();
		return;
	}
	
	/** �ļ��ϴ����Ǳ����
	if($("input[@name='file_path']").val() == "")
	{
		alert("����ѡ��汾�ļ������ϴ���");
		$("input[@name='file_path']").select();
		$("input[@name='file_path']").focus();
		return;
	}**/
	
	var url = "<s:url value="/itms/resource/deviceVersionManageACT!addDeviceVersion.action"/>";
	
	document.mainForm.action = url;
	document.mainForm.submit();
}

//�����豸�ͺ�
function change_model(type,selectvalue){
	switch (type){
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendor_add']").val();
			if("-1"==vendorId){
				$("select[@name='device_model_add']").html("<option value='-1'>==����ѡ���豸����==</option>");
				//$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				//$("select[@name='device_model']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				gwShare_parseMessage(ajax,$("select[@name='device_model_add']"),selectvalue);
			});
			break;
		default:
			alert("δ֪��ѯѡ�");
			break;
	}	
}

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

/** ���߷��� **/
/*LTrim(string):ȥ����ߵĿո�*/
function LTrim(str){
    var whitespace = new String("�� \t\n\r");
    var s = new String(str);   

    if (whitespace.indexOf(s.charAt(0)) != -1){
        var j=0, i = s.length;
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
            j++;
        }
        s = s.substring(j, i);
    }
    return s;
}
/*RTrim(string):ȥ���ұߵĿո�*/
function RTrim(str){
    var whitespace = new String("�� \t\n\r");
    var s = new String(str);
 
    if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
        var i = s.length - 1;
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
            i--;
        }
        s = s.substring(0, i+1);
    }
    return s;
}
/*Trim(string):ȥ���ַ������ߵĿո�*/
function trim(str){
    return RTrim(LTrim(str)).toString();
}


//ȫ��trim
function trimAll()
{
	var inputs = document.getElementsByTagName("input");
	for(var i=0;i<inputs.length;i++)
	{
		var input = inputs[i];
		if(/text/gi.test(input.type))
		{
			input.value = trim(input.value);
		}
	}
}

</SCRIPT>
</html>