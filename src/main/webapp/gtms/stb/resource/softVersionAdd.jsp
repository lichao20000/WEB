<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>


<SCRIPT LANGUAGE="JavaScript">
	//�����ͺ�
	function vendorChange()
	{
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/resource/softVersion!getDeviceModel.action'/>";
		if(vendorId!="-1"){
			$("div[@id='deviceModel']").html("");
			$.post(url,{
				vendorId:vendorId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					if(typeof(lineData)&&typeof(lineData.length)){
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							checkboxtxt = "<input type='checkbox' name='deviceModelId' value='"
												+xValue+"'>"+xText+"  ";
							$("div[@id='deviceModel']").append(checkboxtxt);
						}
					}else{
						$("div[@id='deviceModel']").append("�ó���û�ж�Ӧ�ͺţ�");
					}
				}else{
					$("div[@id='deviceModel']").append("�ó���û�ж�Ӧ�ͺţ�");
				}
			});
		}else{
			$("div[@id='deviceModel']").html("��ѡ����");
		}
	}
	
	//�༭�������汾
	function ExecMod()
	{
		var isAdd = $("input[@name='isAdd']").val();
		var versionDesc = $("input[@name='versionDesc']").val();
		var vendorId = $("select[@name='vendorId']").val();
		var versionPath= $("input[@name='versionPath']").val();
		var versionName = $("input[@name='versionName']").val();
		var fileSize = $("input[@name='fileSize']").val();
		var MD5 = $("input[@name='MD5']").val();
		var epg_version = $("input[@name='epg_version']").val();
		var net_type = $("select[@name='net_type']").val();
		
		if(versionName==""){
			alert("������汾����!");
			$("input[@name='versionName']").focus();
			return;
		}
		if(vendorId=="-1"){
			alert("��ѡ���̣�");
			return;
		}
		if(versionPath==""){
			alert("������汾�ļ�·��!");
			$("input[@name='versionPath']").focus();
			return;
		}
		if(fileSize==""){
			alert("������汾�ļ���С!");
			$("input[@name='fileSize']").focus();
			return;
		}
		if(MD5==""){
			alert("������MD5����!");
			$("input[@name='MD5']").focus();
			return;
		}
		if(epg_version==""){
			alert("������EPG�汾����!");
			$("input[@name='epg_version']").focus();
			return;
		}
		
		var deviceModelId = "";
		$("input[@name='deviceModelId'][@checked]").each(function(){ 
	    	deviceModelId = deviceModelId + $(this).val()+",";
	    });
	    if(deviceModelId=="" || deviceModelId==null){
			alert("��ѡ�������ͺ�!");
			return;
		}
	    
	    var url = "<s:url value='/gtms/stb/resource/softVersion!addedit.action'/>";
	    $.post(url,{
			isAdd:isAdd,
			versionDesc:versionDesc,
			vendorId:vendorId,
			versionPath:versionPath,
			deviceModelId:deviceModelId,
			versionName:versionName,
			fileSize:fileSize,
			MD5:MD5,
			epg_version:epg_version,
			net_type:net_type
		},function(ajax){
	    	var s = ajax.split(",");
	    	alert(s[1]);
	    	if("1"==s[0])
	    	{
	    		$("input[@name='isAdd']").val("1");
	    		$("input[@name='id']").val("");
	    		$("input[@name='versionDesc']").val("");
	    		$("select[@name='vendorId']").val("-1");
	    		$("input[@name='versionPath']").val("");
	    		$("input[@name='versionName']").val("");
	    		$("input[@name='fileSize']").val("");
	    		$("input[@name='MD5']").val("");
	    		$("input[@name='epg_version']").val("");
	    	}
	    });
	}
</SCRIPT>

</head>
<body>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24> 
			����ǰ��λ�ã���������汾�ļ�·������
		</TD>
	</TR>
</TABLE>

<table class="querytable" width="98%" align="center" id="addedit">
	<tr>
		<td class="title_1" colspan="2">
			��������汾�ļ�·��
			<input type="hidden" name="isAdd" value="1"> 
		</td>
	</tr>
	<TR>
		<TD class="title_2" align="center" width="15%">�汾����</TD>
		<TD width="85%">
			<input type="text" name="versionName" id="versionName" class="bk" value="" size="40" maxlength="40">
			<font color="red">*����С��40</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�汾����</TD>
		<TD width="85%">
			<input type="text" name="versionDesc" id="versionDesc" class="bk" value="" size="40" maxlength="80">
			<font color="red">*����С��80</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">����</TD>
		<TD width="85%">
			<s:select list="vendorList" name="vendorId"
				headerKey="-1" headerValue="��ѡ����" listKey="vendor_id"
				listValue="vendor_add" value="vendorId" cssClass="bk"
				onchange="vendorChange()" theme="simple">
			</s:select> <font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�����ͺ�</TD>
		<TD width="85%">
			<div id="deviceModel">��ѡ����</div>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�汾�ļ�·��</TD>
		<TD width="85%">
			<input type="text" name="versionPath" id="versionPath" class="bk" value="" size="40"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�ļ���С</TD>
		<TD width="85%">
			<input type="text" name="fileSize" id="fileSize" class="bk" value="" size="40"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">MD5</TD>
		<TD width="85%">
			<input type="text" name="MD5" id="MD5" class="bk" value="" size="40"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">EPG�汾</TD>
		<TD width="85%">
			<input type="text" name="epg_version" id="epg_version" class="bk" value="" size="16"> 
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">������������</TD>
		<TD width="85%">
			<select name="net_type" class="bk" >
				<option value="unknown_net" selected>δ  ֪</option>
				<option value="public_net">��  ��</option>
				<option value="private_net">ר  ��</option>
			</select>
		</TD>
	</TR>
	<tr>
		<td colspan="2" class="foot" align="right">
			<div class="right">
				<button onclick="javascript:ExecMod();">�ύ</button>
			</div>
		</td>
	</tr>
</table>
</body>