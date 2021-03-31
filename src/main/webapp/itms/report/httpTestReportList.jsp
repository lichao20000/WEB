<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������Դ�б�</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<% 
request.setCharacterEncoding("GBK");

String gw_type = request.getParameter("gw_type");
%>
<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	
	function detail(cityId,type) {
		parent.detail(cityId,type);
	}
	
	function ToExcel(){
		parent.toExcel();
	}

</script>
</head>
<body>
	<input type="hidden" id="bind_total" value="<s:property value="total"/>" />
	<table class="listtable" id="listTable">
	<caption>��ͨ��ͥ�û����ٳɹ���</caption>
	<thead>
		<tr>
			<th align="center">����</th>
			<th align="center">�����</th>
			<th align="center">���</th>
			<th align="center">�ܼ�</th>
			<th align="center">�����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="httpTestList!=null">
			<s:if test="httpTestList.size()>0">
				<s:iterator value="httpTestList">
						<tr>
							<td align="center"><s:property value="cityName"/></td>
							<td align="center">
							  <a href="javascript:detail('<s:property value="cityId" />','noReached')"><s:property value="noReached"/></a>
							</td>
							<td align="center">
							  <a href="javascript:detail('<s:property value="cityId" />','reached')"><s:property value="reached"/></a>
							</td>
							<td align="center">
							  <a href="javascript:detail('<s:property value="cityId" />','total')"><s:property value="total"/></a>
							</td>
							<td align="center"><s:property value="reachedRate"/></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=7>û����ز�ѯ��Ϣ��Ϣ</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7>û����ز�ѯ��Ϣ</td>
			</tr>
		</s:else>
	</tbody>

		<tfoot>
			<%-- <tr>
				<td colspan="7" align="right"><lk:pages
						url="/itms/report/httpTest!qryHttpTestList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr> --%>
		 <tr>
				<td colspan="14" align="right">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="javascript:ToExcel()"></td>
			</tr> 
		</tfoot>
	</table>
</body>
</html>