<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���ؼ���б�</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
var getId = "<s:property value='groupId'/>"; 
	$(function(){
		debugger;
		if (getId) {
			$("#groupId").attr("disabled", "true");
		}
	});
	
	function cancel() {
		window.close();
	}
	
	function saveData() {
		var pWindow = window.dialogArguments;
		var groupId = $("#groupId").val();
		var groupName = $("#groupName").val();
		var remark = $("#remark").val();
		if (!groupId) {
			alert("�û�����ID����Ϊ�գ�");
			return;
		}
		if (!groupName) {
			alert("�û��������Ʋ���Ϊ�գ�");
			return;
		}
		var url = "<s:url value='/gtms/stb/resource/customerGroupACT!addData.action'/>";
		if (getId) {
			url = "<s:url value='/gtms/stb/resource/customerGroupACT!editData.action'/>";
		}
		$.post(url, {
			groupId : groupId, 
			groupName : groupName, 	// 1 open 0 close
			remark : remark
         },function(ajax){
				if("1"==ajax){
					alert("�����ɹ�");
					cancel();
					if(pWindow != null){  
						pWindow.queryData();  
					}
				}else if ("0"==ajax){
					alert("����ʧ��");
				}else{
					alert(ajax);
				}
          });
	}
</SCRIPT>
</head>
<body>
<table border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<tr>
		<td>
			<TABLE width="98%" class="querytable" align="center">
			<tr>
				<th colspan="2" id="thTitle" class="title_1">�û��������͹���</th>
			</tr>

			<TR>
				<TD width="20%" class="title_2">�û�����ID��</TD>
				<TD><input type="text" name="groupId" id="groupId" value="<s:property value="groupId"/>" size="20" maxlength="40" class="bk" /></TD>
			</TR>
			<TR>
				<TD width="20%" class="title_2">�û��������ƣ�</TD>
				<TD><input type="text" name="groupName" id="groupName" value="<s:property value="groupName"/>" size="20" maxlength="40" class="bk" /></TD>
			</TR>
			<TR>
				<TD width="20%" class="title_2">��ע��</TD>
				<TD><input type="text" name="remark" id="remark" value="<s:property value="remark"/>" size="50" class="bk" /></TD>
			</TR>
			<tr align="right">
				<td colspan="2" class="foot" align="right">
					<div class="right">
						<input type="button" onclick="saveData()" align="right" class=jianbian
							value=" ����" />
						<input type="button" onclick="cancel()" align="right" class=jianbian
							value=" ȡ��" />
					</div>
				</td>
			</tr>
		</TABLE>
		</td>
	</tr>
</table>
</body>