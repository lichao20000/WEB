<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����������������</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
	
<script type="text/javascript">
	$(function() {
		parent.closeMsgDlg();
		parent.dyniframesize();
	});
	
	function ToExcel(){
		parent.ToExcel();
	}
	
</script>	
</head>
<body>
<table class="listtable">
	<caption>
		������������������ϸ�б�
	</caption>
	<thead>
		<tr>
			<th>����</th>
			<th>������</th>
			<th>LOID</th>
			<th>�豸���к�</th>
			<th>���͹⹦��</th>
			<th>���չ⹦��</th>
			<th>���յ�ַ</th>
			<th>OLT����</th>
			<th>OLTIP</th>
			<th>PON�˿�</th>
			<th>ONTID</th>
			<th>���ִ���</th>
			<th>ƽ���ӳ�</th>
			<th>ʱ�ӳ��ִ���</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
	<s:if test="netWorkList!=null">
			<s:if test="netWorkList.size()>0">
				<s:iterator value="netWorkList">
				<tr>
					<td>
						<s:property value="area_name" />
					</td>
					<td>
						<s:property value="subarea_name" />
					</td>
					<td>
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="device_serialnumber" />
					</td>
					<td>
						<s:property value="tx_power" />
					</td>
					<td>
						<s:property value="rx_power" />
					</td>
					<td>
						<s:property value="linkaddress" />
					</td>
					<td>
						<s:property value="olt_name" />
					</td>
					<td>
						<s:property value="olt_ip" />
					</td>
					<td>
						<s:property value="pon_id" />
					</td>
					<td>
						<s:property value="ont_id" />
					</td>
					<td>
						<s:property value="count_num" />
					</td>
					<td>
						<s:property value="avg_delay" />
					</td>
					
					<td>
						<s:property value="loss_pp" />
					</td>
					
					<td>
						<s:property value="time" />
					</td>
					
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
				<tr>
					<td colspan=15>û��������������������Ϣ</td>
				</tr>
		</s:else>
	</s:if>
		<s:else>
			<tr>
				<td colspan=15>
					ϵͳû����ص��豸��Ϣ!
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan=15>
				<span style="float: right;"> 
				<lk:pages url="/ids/NetWorkQualityAnalyseInfo!netWorkQualityAnalyseInfo.action" styleClass="" showType="" isGoTo="true" changeNum="true" /> </span>
			</td>
		</tr>
		<tr>
			<td colspan="15" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>
