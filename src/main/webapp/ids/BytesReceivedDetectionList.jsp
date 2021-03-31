<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<%
	/**
	 *  Ԥ��Ԥ�޸澯��Ϣ
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2014-02-18
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
$(function() {
	$("#trData",parent.document).hide();
	$("#btn",parent.document).attr('disabled',false);
	parent.dyniframesize();
});

function ListToExcel(deviceSerialnumber,starttime,endtime,queryTimeType,loid){
	var page = "<s:url value='/ids/bytesReceivedDetection!queryLanAndPonDataExcel.action'/>?"
		+ "deviceSerialnumber="+deviceSerialnumber
		+ "&starttime="+starttime
		+ "&endtime=" + endtime
		+ "&queryTimeType="+queryTimeType
		+ "&loid="+loid;
	document.all("childFrm").src=page;
}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<thead>
			<tr>
				<th width="7%">loid</th>
				<th width="7%">�豸���к�</th>
				<th width="7%">LAN1������ͳ��(M)</th>
				<th width="7%">LAN1��ƽ������(KB/s)</th>
				<th width="7%">LAN2������ͳ��(M)</th>
				<th width="7%">LAN2��ƽ������(KB/s)</th>
				<th width="7%">LAN3������ͳ��(M)</th>
				<th width="7%">LAN3��ƽ������(KB/s)</th>
				<th width="7%">LAN4������ͳ��(M)</th>
				<th width="7%">LAN4��ƽ������(KB/s)</th>
				<th width="7%">PON������ͳ��(M)</th>
				<th width="7%">�ϱ�ʱ��</th>
				<th width="7%">���ʱ��</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="list!=null">
				<s:if test="list.size()>0">
					<s:iterator value="list">
						<tr>
							<td align="center"><s:property value="loid" /></td>
							<td align="center"><s:property value="device_serialnumber" /></td>
							<td align="center"><s:property value="bytes1" /></td>
							<td align="center"><s:property value="bytespert1" /></td>
							<td align="center"><s:property value="bytes2" /></td>
							<td align="center"><s:property value="bytespert2" /></td>
							<td align="center"><s:property value="bytes3" /></td>
							<td align="center"><s:property value="bytespert3" /></td>
							<td align="center"><s:property value="bytes4" /></td>
							<td align="center"><s:property value="bytespert4" /></td>
							<td align="center"><s:property value="ponbytes" /></td>
							<td align="center"><s:property value="upload_time" /></td>
							<td align="center"><s:property value="add_time" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=13>ϵͳû��ƥ�䵽��Ӧ��Ϣ!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=13>ϵͳû��ƥ�䵽��Ӧ��Ϣ!</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="13" align="right">
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand;float: left'
						onclick="ListToExcel('<s:property value="deviceSerialnumber"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="queryTimeType"/>','<s:property value="loid"/>')">
						
						</td>
			</tr>
<tr STYLE="display: none">
			<td colspan="8"><iframe id="childFrm" src=""></iframe></td>
	</tr>
		</tfoot>
	</table>
</body>
</html>