<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����ע��״̬����ͳ����Ϣ</title>

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
	function appearCount(device_sn){
		parent.appearCount(device_sn);
	}
	
</script>	
	
</head>
<body>
<table class="listtable" id="listTable">
	<thead>
		<tr>
			<th>�豸���к�</th>
			<th>LOID</th>
			<th>װ����ַ</th>
			<th>ƽ��ʱ��</th>
			<th>���ִ���</th>
			<th>������</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="netWorkQualityList!=null">
			<s:if test="netWorkQualityList.size()>0">
				<s:iterator value="netWorkQualityList">
						<tr>
							<td>
								<s:property value="device_serialnumber" />
							</td>
							<td>
								<s:property value="loid" />
							</td>
							<td>
								<s:property value="address" />
							</td>
							<td>
								<s:property value="avg_delay" />
							</td>
							<td>
								<a href="javascript:appearCount('<s:property value="device_serialnumber"/>')">
									<s:property value="appear_count" />
								</a>
							</td>
							<td>
								<s:property value="loss_pp" />
							</td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>û��������������Ϣ</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan=6>
				<span style="float: right;"> <lk:pages
						url="/ids/NetWorkQualityTest!netWorkQualityInfo.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
			</td>
		</tr>
		<tr>
			<td colspan="6" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>