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
	
	function deleteData(groupId) {
		if(!confirm("��ȷʵҪɾ����")){
			return false;
		}
		var url = "<s:url value='/gtms/stb/resource/customerGroupACT!deleteData.action'/>";
		$.post(url, {
			groupId : groupId
         },function(ajax){
				if("1"==ajax){
					alert("�����ɹ�");
					parent.queryData();
				}else if ("0"==ajax){
					alert("����ʧ��");
				}else{
					alert(ajax);
				}
          });
	}
	
	function edit(id) {
		parent.editData(id);
	}
</SCRIPT>
</head>
<body>
<table border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<tr>
		<td>
			<table class="listtable" width="100%" align="center">
				<thead>
					<tr>
					<th align="center">�û�����ID</th>
					<th align="center">�û���������</th>
					<th align="center">��ע</th>
					<th align="center">����</th>
				</thead>
				<tbody>
					<s:iterator value="dataList">
						<tr bgcolor="#FFFFFF" align="center">
						    <td><s:property value="group_id"/></td>
							<td><s:property value="group_name"/></td>
							<td><s:property value="remark"/></td>
							<td align="center" class=column1>
								<a href="#" onclick="edit('<s:property value="group_id"/>')">�༭</a>
								<a href="#" onclick="deleteData('<s:property value="group_id"/>')">ɾ��</a>
							</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</td>
	</tr>
</table>
</body>