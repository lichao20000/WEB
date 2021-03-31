
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript">
	$(function(){
		
	});
	function addeditVersion(isAdd,id,vendorId,softwareversion,versionDesc,versionPath,versionType){
		$('#versionBtn').attr("disabled",false);
		$("table[@id='addedit']").show();
		$("input[@name='isAdd']").val(isAdd);
		$("input[@name='pathId']").val(id);
		$("input[@name='softwareversion']").val(softwareversion);
		$("input[@name='versionDesc']").val(versionDesc);
		$("select[@name='vendorId']").val(vendorId);
		$("select[@name='versionType']").val(versionType);
    	$("input[@name='versionPath']").val(versionPath);
	}
	
	function deviceModelIdchecked(deviceModelIds){
		var deviceModelId = deviceModelIds.split(",");
    	for(i = 0;i<deviceModelId.length;i++){
	        $("input[@name='deviceModelId'][@value='"+deviceModelId[i]+"']").attr("checked",true);
    	}
	}
	function vendorChange(){
		$('#versionBtn').attr("disabled",false);
		var vendorId = $("select[@name='vendorId']").val();
		var versionType = $("select[@name='versionType']").val();
		var url = "<s:url value='/gtms/stb/resource/deviceVersion!isStsExsit.action'/>";
		if(vendorId!="-1"){
			$.post(url,{
				vendorId:vendorId,
				versionType:versionType
			},function(ajax){
				if(ajax!=""){
					var result = ajax.split("#");
					if(result[0]=="0"){
						alert(result[1]);
						$('#versionBtn').attr("disabled",true);
					}
				}
			});
		}
	}
	function ExecMod(){
		var isAdd = $("input[@name='isAdd']").val();
		var pathId = $("input[@name='pathId']").val();
		var versionDesc = $("input[@name='versionDesc']").val();
		var vendorId = $("select[@name='vendorId']").val();
		var versionPath = $("input[@name='versionPath']").val();
		var softwareversion = $("input[@name='softwareversion']").val();
		var versionType = $("select[@name='versionType']").val();
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
		if(versionType=="-1"){
			alert("��ѡ��汾���ͣ�");
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
	    var url = "<s:url value='/gtms/stb/resource/deviceVersion!addeditSts.action'/>";
	    $.post(url,{
			isAdd:isAdd,
			versionType:versionType,
			versionDesc:encodeURIComponent(versionDesc),
			vendorId:vendorId,
			versionPath:versionPath,
			softwareversion:encodeURIComponent(softwareversion),
			pathId:pathId
		},function(ajax){
	    	var s = ajax.split(",");
	    	if(s[0]=="1"){
	    		alert(s[1]);
	    		var strpage = "<s:url value='/gtms/stb/resource/deviceVersion!queryStsVersion.action'/>";
	    		window.location.href=strpage;
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
			pathId:pathId
		},function(ajax){
	    	var s = ajax.split(",");
	    	if(s[0]=="1"){
	    		alert(s[1]);
	    		var strpage = "<s:url value='/gtms/stb/resource/deviceVersion.action'/>";
	    		window.location.href=strpage;
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else{
	    		alert("ɾ��ʧ�ܣ�");
	    	}
	    });
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
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					����ǰ��λ�ã�һ������汾�ļ�·������
				</TD>
			</TR>
		</TABLE>
		<br>
		<table class="listtable" width="98%" align="center">
			<thead>
				<tr>
					<th align="center" width="10%">
						����
					</th>
					<th align="center" width="25%">
						�汾����
					</th>
					<th align="center" width="15%">
						�汾����
					</th>
					<th align="center" width="15%">
						�汾����
					</th>
					<th align="center" width="25%">
						�汾�ļ�·��
					</th>
					<th align="center" width="10%">
						����
					</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="stsVersionList">
					<tr bgcolor="#FFFFFF">
						<td>
							<s:property value="vendor_add" />
						</td>
						<td>
							<s:property value="version_type" />
						</td>
						<td>
							<s:property value="softwareversion" />
						</td>
						<td>
							<s:property value="version_desc" />
						</td>
						<td>
							<s:property value="version_path" />
						</td>
						<td align="center">
							<s:if test='accoid==acc_oid||areaId=="1"'>
								<label
									onclick="javascript:addeditVersion('0','<s:property value='id'/>','<s:property value='vendor_id'/>','<s:property value='softwareversion'/>','<s:property value='version_desc'/>','<s:property value='version_path'/>','<s:property value='version_type'/>');">
									<IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0'
										ALT='�༭' style='cursor: hand'>
								</label>
								<!-- 
								<label
									onclick="javascript:deleteVersion(<s:property value='id'/>);">
									<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0'
										ALT='ɾ��' style='cursor: hand'>
								</label> -->
							</s:if>
						</td>
					</tr>
				</s:iterator>
			</tbody>
			<tfoot>
				<tr bgcolor="#FFFFFF">
					<td colspan="6" align="right">
						<lk:pages url="/gtms/stb/resource/deviceVersion.action" styleClass=""
							showType="" isGoTo="true" changeNum="true" />
					</td>
				</tr>
				<tr>
					<td colspan="6" align="left">
						<button onclick="addeditVersion('1','','-1','','','','-1');">
							��Ӽ�¼
						</button>
					</td>
				</tr>

			</tfoot>
		</table>
		<br>
		<table class="querytable" width="98%" align="center"
			style="display: none" id="addedit">
			<tr>
				<td class="title_1" colspan="2">
					���/�༭��
					<input type="hidden" name="isAdd" value="">
					<input type="hidden" name="pathId" value="">
				</td>
			</tr>
			<TR>
				<TD class="title_2" align="center" width="15%">
					�汾����
				</TD>
				<TD width="85%">
					<input type="text" name="softwareversion" id="softwareversion"
						class="bk" value="" size="40" maxlength="20">
					<font color="red">*����С��20</font>
				</TD>
			</TR>
			<TR>
				<TD class="title_2" align="center" width="15%">
					�汾����
				</TD>
				<TD width="85%">
					<input type="text" name="versionDesc" id="versionDesc" class="bk"
						value="" size="40" maxlength="20">
					<font color="red">*����С��20</font>
				</TD>
			</TR>
			<TR>
				<TD class="title_2" align="center" width="15%">
					�汾����
				</TD>
				<TD width="85%">
					<s:select list="versionTypeList" name="versionType" headerKey="-1"
						headerValue="��ѡ��汾����" listKey="type_id" listValue="type_name"
						value="versionType" cssClass="bk" onchange="vendorChange()" 
						theme="simple"></s:select>
					<font color="red">*</font>
				</TD>
			</TR>
			<TR>
				<TD class="title_2" align="center" width="15%">
					����
				</TD>
				<TD width="85%">
					<s:select list="vendorList" name="vendorId" headerKey="-1"
						headerValue="��ѡ����" listKey="vendor_id" listValue="vendor_add"
						value="vendorId" cssClass="bk" onchange="vendorChange()"
						theme="simple"></s:select>
					<font color="red">*</font>
				</TD>
			</TR>
			<TR>
				<TD class="title_2" align="center" width="15%">
					�汾�ļ�·��
				</TD>
				<TD width="85%">
					<input type="text" name="versionPath" id="versionPath" class="bk"
						value="" size="40">
					<font color="red">*</font>
				</TD>
			</TR>
			<tr>
				<td colspan="2" class="foot" align="right">
					<div class="right">
						<button id="versionBtn" onclick="ExecMod()">
							�ύ
						</button>
					</div>
				</td>
			</tr>
		</table>
	</body>