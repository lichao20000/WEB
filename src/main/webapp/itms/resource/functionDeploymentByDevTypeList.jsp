<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�������ܲ��𱨱��豸�ͺ����</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>�������ܲ��𱨱��豸�ͺ���� </caption>
	<thead>
		<tr>
			<th width="50%">�豸�ͺ�</th>
			<th width="50%">�ѿ�ͨ�ն���</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deployList!=null">
			<s:if test="deployList.size()>0">
				<s:iterator value="deployList">
						<tr>
							<td width="50%" >
							
								<s:property value="modelType" />
							</td>
							<td width="50%">
								<a href="javascript:openModel('<s:property value="model_id"/>');">
									<s:property value="deploy_total" />
								</a>
							</td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=2>ϵͳ��û�в�ѯ����Ҫ����Ϣ!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=2>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="2" align="right"><IMG SRC="/itms/images/excel.gif" 
				BORDER=0  ALT="�����б�" style="cursor: hand" onclick="javaScript:ToExcel();">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>