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
	
	function stbDeviceDetail(deviceId) {
		var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryZeroDeviceDetail.action'/>?deviceId="
				+ deviceId + "&gw_type=4";
		window.open(url, "",
						"left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}
	
	function deviceDetail(device_id) {
		var gw_type = "";
		var strpage = "<s:url value='/Resource/DeviceShow.jsp'/>?device_id="
				+ device_id;
		window.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}

</script>
</head>
<body>
	<input type="hidden" id="bind_total" value="<s:property value="total"/>" />
	<table class="listtable" id="listTable">
	<caption>��������Դ��¼</caption>
	<thead>
		<tr>
			<th align="center">LOID</th>
			<th align="center">�豸���к�</th>
			<th align="center">MAC</th>
			
			<th align="center">������ҵ���˺�</th>
			<th align="center">���������к�</th>
			<th align="center">������MAC</th>
			
			<th align="center">�ϱ�ʱ��</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devSourceList!=null">
			<s:if test="devSourceList.size()>0">
				<s:iterator value="devSourceList">
						<tr>
							<td class=column1 align="center"><s:property value="loid"/></td>
							<td class=column1 align="center">
							  <a href="javascript:deviceDetail('<s:property value="devId" />')"><s:property value="devSn"/></a>
							</td>
							<td class=column1 align="center"><s:property value="mac"/></td>
							
							<td class=column1 align="center"><s:property value="servAccount"/></td>
							<td class=column1 align="center">
							<a href="javascript:stbDeviceDetail('<s:property value="stbDevId" />')">
							<s:property value="stbDevSn"/>
							</a>
							</td>
							<td class=column1 align="center"><s:property value="stbmac"/></td>
							
							<td class=column1 align="center"><s:property value="updatetime"/></td>
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
			<tr>
				<td colspan="7" align="right"><lk:pages
						url="/gtms/stb/resource/stbSource!qryStbSource.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>

		<%-- 	<tr>
				<td colspan="14" align="right">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel()">(������ҹ��22:00֮�����!)</td>
			</tr> --%>
		</tfoot>
	</table>
</body>
</html>