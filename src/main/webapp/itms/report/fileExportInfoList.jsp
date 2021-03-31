<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ include file="../../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������������ͳ����ͼ</title>
<%
	/**
		 * ������������ͳ����ͼ
		 * 
		 * @author guankai
		 * @version 1.0
		 * @since 2020-04-20
		 * @category
		 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});
	
	
	function downloadFile(status,fileName) {
	
			if("δ���"== status){
				alert("�ļ�δ���ɣ�");
				return;
			}else{
				$("input[@name='filename']").val(fileName);
				var url = "<s:url value='/itms/report/fileExportReport!download.action'/>";
				document.getElementById("mainForm").action = url;
				document.getElementById("mainForm").submit();
				document.getElementById("mainForm").reset();
			}
	
	}
</script>

</head>

<body>
<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm" enctype="multipart/form-data">
		<input type="hidden" name="filename" value="">
</FORM>
<table class="listtable">
	<caption>
		ͳ�ƽ��
	</caption>
	<thead>
		<tr>
			<th>����������</th>
			<th>����ʱ��</th>
			<th>�ļ�����ʱ��</th>
			<th>˵��</th>			
			<th>�ļ�����״̬</th>
			<th>�����ļ���</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="data">
			<tr bgcolor="#FFFFFF">
				<td class=column1><s:property value="fileexportuser"/></td>
				<td class=column1><s:property value="fileexporttime"/></td>
				<td class=column1><s:property value="filefinishtime"/></td>
				<td class=column1><s:property value="fileexportdesc"/></td>
				<td class=column1><s:property value="status"/></td>
				<td class=column1><a href="javascript:;" onclick="downloadFile('<s:property value="status"/>','<s:property value="filename"/>')"><s:property value="filename"/></a></td>
				<td class=column1><s:property value="cityname"/></td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="7" align="right">
				<lk:pages url="/itms/report/fileExportReport!getFileExportInfo.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
				<%--<ms:simplePages url="/itms/report/fileExportReport!getFileExportInfo.action" />--%>
			</td>
		</tr>
	</tfoot>
</body>
</html>