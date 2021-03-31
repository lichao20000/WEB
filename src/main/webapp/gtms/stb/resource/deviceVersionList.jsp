<%@page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<style type="text/css">
td {
	white-space: nowrap;
	overflow: hidden;
}
</style>

<SCRIPT LANGUAGE="JavaScript">

	/** 1����ͥ����	2����ҵ����	4��������  */
	var gw_type = "<s:property value='gw_type'/>";
	var type = 1;

	function vendorChange(){
		var vendorId = $("select[@name='vendorId']").val();
		var url = "<s:url value='/gtms/stb/resource/deviceVersion!getDeviceModel.action'/>";
		if(vendorId!="-1"){
			$("div[@id='deviceModel']").html("");
			$.post(url,{
				vendorId:vendorId
			},function(ajax){
				if(ajax!=""){
					var lineData = ajax.split("#");
					var	checkboxtxt="";
					if(typeof(lineData)&&typeof(lineData.length)){
						checkboxtxt="<table>";
						for(var i=0;i<lineData.length;i++){
							var oneElement = lineData[i].split("$");
							var xValue = oneElement[0];
							var xText = oneElement[1];
							//$("select[@name='versionType']").val(versionType);
							if(i%8==0){
								checkboxtxt=checkboxtxt+"<tr style='border:0px'>";
							}
							checkboxtxt=checkboxtxt+"<td style='border:0px'><input type='checkbox' name='deviceModelId' value='"+xValue+"'>"+xText+"</td>";
							if( (i!=0 && (i+1)%8==0) || (i==lineData.length-1)){
								checkboxtxt=checkboxtxt+"</tr>";
							}
						}
						checkboxtxt=checkboxtxt+"</table>";
						$("div[@id='deviceModel']").append(checkboxtxt);
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


	function getVersionPath(versionPath){
		var url = "<s:url value='/gtms/stb/resource/deviceVersion!getVersionPathList.action'/>";
		$.post(url,{},function(ajax){
			$("select[@name='versionPath']").html("");
			if(ajax!=""){
				var lineData = ajax.split("#");
				$("select[@name='versionPath']").append("<option value='-1' >"+lineData[0]+"</option>");
				for(var i=1; i<lineData.length; i++){
					var option = "<option value='"+lineData[i]+"' >"+lineData[i]+"</option>";
					$("select[@name='versionPath']").append(option);
				}
			}
		});

	}

	function addeditVersion(isAdd,id,vendorId,softwareversion,versionDesc,versionPath,versionType,deviceModelIds,specialPath,dcnPath){
		$("table[@id='addedit']").show();
		<s:if test='%{isUploadFile=="1"}'>
			getVersionPath();
			$("input[@name='newfileName']").val("");
			document.frames("uploadFrm").document.location="FileUpload.jsp";
		</s:if>
		<s:else>
			$("input[@name='versionPath']").val(versionPath);
		</s:else>
		$("input[@name='isAdd']").val(isAdd);
		$("input[@name='pathId']").val(id);
		$("input[@name='softwareversion']").val(softwareversion);
		$("input[@name='versionDesc']").val(versionDesc);
		$("select[@name='vendorId']").val(vendorId);
		$("select[@name='versionType']").val(versionType);

		if('' != specialPath){
			$("input[@name='specialPath']").val(specialPath);
		}
		if('' != dcnPath){
			$("input[@name='dcnPath']").val(dcnPath);
		}
		vendorChange();
		setTimeout('deviceModelIdchecked("'+deviceModelIds+'")', 1000);

    	//$("input[@name='versionPath']").attr("disabled","none");
	}

	function deviceModelIdchecked(deviceModelIds){
		var deviceModelId = deviceModelIds.split(",");
    	for(var i = 0;i<deviceModelId.length;i++){
	        $("input[@name='deviceModelId'][@value='"+deviceModelId[i]+"']").attr("checked",true);
    	}
	}

	function ExecMod(){
		var isAdd = $("input[@name='isAdd']").val();
		var pathId = $("input[@name='pathId']").val();
		var versionDesc = $("input[@name='versionDesc']").val();
		var vendorId = $("select[@name='vendorId']").val();
		var versionPath= "";
		<s:if test='%{isUploadFile=="1"}'>
			versionPath = $("select[@name='versionPath']").val();
		</s:if>
		<s:else>
			versionPath = $("input[@name='versionPath']").val();
		</s:else>
		var softwareversion = $("input[@name='softwareversion']").val();

		var dcnPath = $("input[@name='dcnPath']").val();
		var specialPath = $("input[@name='specialPath']").val();

		if(softwareversion==""){
			alert("������汾����!");
			$("input[@name='softwareversion']").focus();
			return;
		}
		if(versionDesc==""){
			alert("������汾����!");
			$("input[@name='versionDesc']").focus();
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
		var newfileName = $("input[@name='newfileName']").val();
		if (newfileName){
			var len = versionPath.length;
			var last = versionPath.charAt(len-1);
			if("/"==last){
				versionPath = versionPath+newfileName;
			}else{
				versionPath = versionPath+"/"+newfileName;
			}
		}
		var deviceModelId = "";
		$("input[@name='deviceModelId'][@checked]").each(function(){
	    	deviceModelId = deviceModelId + $(this).val()+",";
	    });
	    if(deviceModelId==""){
			alert("��ѡ�������ͺ�!");
			return;
		}

	    if(dcnPath==""){
			alert("�汾�ļ�DCN·��������Ϊ��!");
			$("input[@name='dcnPath']").focus();
			return;
		}

		var versionType = $("select[@name='versionType']").val();

	    var url = "<s:url value='/gtms/stb/resource/deviceVersion!addedit.action'/>";
	    $.post(url,{
			isAdd:isAdd,
			versionDesc:encodeURIComponent(versionDesc),
			vendorId:vendorId,
			versionPath:versionPath,
			deviceModelId:deviceModelId,
			versionType:versionType,
			softwareversion:encodeURIComponent(softwareversion),
			pathId:pathId,
			dcnPath:dcnPath,
			specialPath:specialPath,
			gwType:type
		},function(ajax){
	    	var s = ajax.split(",");
	    	if(s[0]=="1"){
	    		alert(s[1]);
	    		var gwtype = s[2];
	    		var strpage = "<s:url value='/gtms/stb/resource/deviceVersion.action'/>";
	    			window.location.href=strpage+"?gwType="+gwtype;
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else{
	    		alert("���/�༭ʧ�ܣ�");
	    	}
	    });
	}

	function deleteVersion(pathId){
		if(!confirm("���Ҫɾ��������¼��")){
			return false;
		}
		var url = "<s:url value='/gtms/stb/resource/deviceVersion!deleteVersion.action'/>";
	    $.post(url,{
			pathId:pathId,
			gwType:type
		},function(ajax){
	    	var s = ajax.split(",");
if  (s[0]=="2"){
	alert(s[1]);
}
else  {
	if(s[0]=="1"){
		alert(s[1]);
		var gwtype = s[2];
		var strpage = "<s:url value='/gtms/stb/resource/deviceVersion.action'/>";
		window.location.href=strpage+"?gwType="+gwtype;
	}else if(s[0]=="0"){
		alert(s[1]);
	}else{
		alert("ɾ��ʧ�ܣ�");
	}
}
});
	}
	function query()
	{
		var vendor = $("select[name=queryVendorId]").val();
		var softW = $("input[name=querySoftwareversion]").val();
		window.location.href="<s:url value='/gtms/stb/resource/deviceVersion.action'/>?queryVendorId=" + vendor + "&querySoftwareversion=" + softW + "&isUploadFile=<s:property value='isUploadFile'/>";
	}

	function initQuery()
	{
		$("input[name=querySoftwareversion]").attr("value",'<s:property value='querySoftwareversion'/>');
	}
</SCRIPT>

</head>
<body onload="initQuery()">
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24> ����ǰ��λ�ã��汾�ļ�·������
		</TD>
	</TR>
</TABLE>

<br>
<table width="98%" class="querytable" align="center">
	<tr>
		<td class="title_2" align="center">����</td>
		<td width="30%">
			<s:select list="vendorList" name="queryVendorId" headerKey="-1" headerValue="ȫ��"
				listKey="vendor_id" listValue="vendor_add" value="queryVendorId" cssClass="bk"
				theme="simple">
			</s:select>
		</td>
		<td class="title_2" align="center">�汾����</td>
		<td width="30%">
			<input type="text" name="querySoftwareversion" maxlength="20" />
		</td>
		<td class="title_2" align="center">
			<input type="button" style="cursor:pointer" value="��ѯ" onclick="query()"/>
		</td>
	</tr>
</table>
<br>

<table class="listtable" width="98%" align="center">
	<thead>
		<tr>
			<ms:inArea areaCode="hn_lt" notInMode="false">
				<th align="center" width="10%">����</th>
				<th align="center" width="10%">�����ͺ�</th>
				<th align="center" width="10%">�汾����</th>
				<th align="center" width="10%">�汾����</th>
				<th align="center" width="10%">�汾����</th>
				<th align="center" width="15%">�汾�ļ�·��</th>
				<th align="center" width="15%">�汾�ļ�ר��·��</th>
				<th align="center" width="15%">�汾�ļ�DCN·��</th>
				<th align="center" width="5%">����</th>
			</ms:inArea>
			<ms:inArea areaCode="hn_lt" notInMode="true">
				<th align="center" width="10%">����</th>
				<th align="center" width="20%">�����ͺ�</th>
				<th align="center" width="20%">�汾����</th>
				<th align="center" width="10%">�汾����</th>
				<th align="center" width="10%">�汾����</th>
				<th align="center" width="20%">�汾�ļ�·��</th>
				<th align="center" width="10%">����</th>
			</ms:inArea>
		</tr>
	</thead>
	<s:set var="gwType" value='<s:property value="gwType" />' />
	<tbody>
		<s:iterator value="versionList">
			<tr bgcolor="#FFFFFF">
				<td><s:property value="vendor_add" /></td>
				<td><s:property value="device_model" /></td>
				<td><s:property value="version_type" /></td>
				<td><s:property value="softwareversion" /></td>
				<td><s:property value="version_desc" /></td>

				<ms:inArea areaCode="hn_lt" notInMode="false">
					<td><s:property value="version_path" /></td>
					<td><s:property value="special_path" /></td>
					<td><s:property value="dcn_path" /></td>
				</ms:inArea>
				<ms:inArea areaCode="hn_lt" notInMode="true">
					<td>
						<s:property value="version_path" />
					</td>
				</ms:inArea>

				<td align="center" width="20%">
					<s:if test='accoid==acc_oid||areaId=="1"'>
						<ms:inArea areaCode="hn_lt" notInMode="false">
							<label
								onclick="javascript:addeditVersion('0','<s:property value='id'/>',
																		'<s:property value='vendor_id'/>',
																		'<s:property value='softwareversion'/>',
																		'<s:property value='version_desc'/>',
																		'<s:property value='version_path'/>',
																		'<s:property value='version_type'/>',
																		'<s:property value='device_model_id'/>',
																		'<s:property value='special_path' />',
																		'<s:property value='dcn_path' />');">
								<IMG src="<s:url value="/images/edit.gif"/>" border='0' alt='�༭' style='cursor: hand'>
							</label>
						</ms:inArea>
						<ms:inArea areaCode="hn_lt" notInMode="true">
							<label
								onclick="javascript:addeditVersion('0','<s:property value='id'/>',
																		'<s:property value='vendor_id'/>',
																		'<s:property value='softwareversion'/>',
																		'<s:property value='version_desc'/>',
																		'<s:property value='version_path'/>',
																		'<s:property value='version_type'/>',
																		'<s:property value='device_model_id'/>',
																		'','');">
								<IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='�༭' style='cursor: hand'>
							</label>
						</ms:inArea>
					</s:if>
					<s:if test='isAdmin=="1"'>
						<label onclick="javascript:deleteVersion(<s:property value='id'/>);">
							<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0' ALT='ɾ��' style='cursor: hand'>
						</label>
					</s:if>
				</td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<ms:inArea areaCode="hn_lt" notInMode="false">
			<tr bgcolor="#FFFFFF">
				<td colspan="9" align="right">
					<lk:pages url="/gtms/stb/resource/deviceVersion.action" styleClass="" showType=""
						isGoTo="true" changeNum="true" />
				</td>
			</tr>
		<!--
			<tr>
				<td colspan="9" align="left">
				<button onclick="javascript:addeditVersion('1','','-1','','','','','','');">
				��Ӽ�¼</button>
				</td>
			</tr>
		 -->
		</ms:inArea>
		<ms:inArea areaCode="hn_lt" notInMode="true">
			<tr bgcolor="#FFFFFF">
				<td colspan="7" align="right">
					<lk:pages url="/gtms/stb/resource/deviceVersion.action" styleClass="" showType=""
						isGoTo="true" changeNum="true" />
				</td>
			</tr>
			<tr>
				<td colspan="7" align="left">
					<input type="button" value="��Ӽ�¼" style="cursor:pointer" onclick="javascript:addeditVersion('1','','-1','','','','','','');"/>

				</td>
			</tr>
		</ms:inArea>
	</tfoot>
</table>
<br>
<br>

<table class="querytable" width="98%" align="center" style="display: none" id="addedit">
	<tr>
		<td class="title_1" colspan="2">
			<ms:inArea areaCode="hn_lt" notInMode="true">���/</ms:inArea>�༭��
			<input type="hidden" name="isAdd" value="">
			<input type="hidden" name="pathId" value="">
		</td>
	</tr>
	<TR>
		<TD class="title_2" align="center" width="15%">�汾����</TD>
		<TD width="85%">
			<input type="text" name="softwareversion" id="softwareversion" class="bk" value="" size="40" maxlength="20">
			<font color="red">*����С��20</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�汾����</TD>
		<TD width="85%">
			<input type="text" name="versionDesc" id="versionDesc" class="bk" value="" size="40" maxlength="20">
			<font color="red">*����С��20</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">����</TD>
		<TD width="85%">
			<s:select list="vendorList" name="vendorId"
				headerKey="-1" headerValue="��ѡ����" listKey="vendor_id"
				listValue="vendor_add" value="vendorId" cssClass="bk"
				onchange="vendorChange()" theme="simple"></s:select>
			<font color="red">*</font>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�����ͺ�</TD>
		<TD width="85%">
		<div id="deviceModel">��ѡ����</div>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�汾����</TD>
		<TD width="85%"><select name="versionType" id="versionType"
			class="bk">
			<option value="0">��ͨ�汾</option>
		</select></TD>
	</TR>
	<s:if test='%{isUploadFile=="1"}'>
		<TR>
			<TD class="title_2" align="center" width="15%">�汾�ļ�·��</TD>
			<TD width="85%">
				<select name="versionPath" id="versionPath" class="bk"></select>
			</TD>
		</TR>
		<TR>
			<TD class="title_2" align="center" width="15%">�ļ�����</TD>
			<TD width="85%">
				<input type="text" name="newfileName"
					id="newfileName" class="bk" value="" size="40" maxlength="20">
			</TD>
		</TR>
		<TR>
			<TD class="title_2" align="center" width="15%"><font color="red">*</font>&nbsp;ѡ���ļ�</TD>
			<TD width="85%">
				<iframe id="uploadFrm" name="uploadFrm" FRAMEBORDER=0 SCROLLING=NO src="FileUpload.jsp"  height="40" width=600>
				</iframe>
			</TD>
		</TR>
	</s:if>
	<s:else>
		<TR>
			<TD class="title_2" align="center" width="15%">�汾�ļ�·��</TD>
			<TD width="85%">
				<input type="text" name="versionPath" id="versionPath" class="bk" value="" size="40">
				<font color="red">*</font>
			</TD>
		</TR>
	</s:else>

	<ms:inArea areaCode="hn_lt" notInMode="false">
		<TR>
			<TD class="title_2" align="center" width="15%">�汾�ļ�ר��·��</TD>
			<TD width="85%"><input type="text" name="specialPath"
				id="specialPath" class="bk" value="" size="40">
			</TD>
		</TR>
		<TR>
			<TD class="title_2" align="center" width="15%">�汾�ļ�DCN·��</TD>
			<TD width="85%"><input type="text" name="dcnPath"
				id="dcnPath" class="bk" value="" size="40"> <font color="red">*</font>
			</TD>
		</TR>
	</ms:inArea>
	<tr>
		<td colspan="2" class="foot" align="right">
			<div class="right">
				<button onclick="ExecMod()">�ύ</button>
			</div>
		</td>
	</tr>
</table>
<%--
<table class="querytable" width="98%" align="center"
	style="display: none" id="addedit1">
	<tr>
		<td class="title_1" colspan="2">���/�༭�� <input type="hidden"
			name="isAdd" value=""> <input type="hidden" name="pathId"
			value=""></td>
	</tr>
	<TR>
		<TD class="title_2" align="center" width="15%">�汾����</TD>
		<TD width="85%"><input type="text" name="softwareversion"
			id="softwareversion" class="bk" value="" size="40" maxlength="20">
		<font color="red">*����С��20</font></TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�汾����</TD>
		<TD width="85%"><input type="text" name="versionDesc"
			id="versionDesc" class="bk" value="" size="40" maxlength="20">
		<font color="red">*����С��20</font></TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">����</TD>
		<TD width="85%"><s:select list="vendorList" name="vendorId"
			headerKey="-1" headerValue="��ѡ����" listKey="vendor_id"
			listValue="vendor_add" value="vendorId" cssClass="bk"
			onchange="vendorChange()" theme="simple"></s:select> <font
			color="red">*</font></TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�����ͺ�</TD>
		<TD width="85%">
		<div id="deviceModel">��ѡ����</div>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�汾����</TD>
		<TD width="85%"><select name="versionType" id="versionType"
			class="bk">
			<option value="1">����˹�汾</option>
			<option value="2">������˹�汾</option>
			<option value="0">��ͨ�汾</option>
			<option value="3">�����ð汾</option>
		</select></TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">�汾�ļ�·��</TD>
		<TD width="85%">
			<select name="versionPath" id="versionPath" class="bk">
			</select>
			</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%"><font color="red">*</font>&nbsp;ѡ���ļ�</TD>
		<TD width="85%">
						<iframe id="uploadFrm" name="uploadFrm" FRAMEBORDER=0 SCROLLING=NO src="FileUpload.jsp"  height="25" width=600>
						</iframe>
		</TD>
	</TR>
	<tr>
		<td colspan="2" class="foot" align="right">
		<div class="right">
		<button onclick="ExecMod()">�ύ</button>
		</div>
		</td>
	</tr>
</table>
	 --%>
</body>
