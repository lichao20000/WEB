<%@page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ȫʡIPTV�û��ն˷ֲ�����Ծ��������</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
		parent.isShowButton(true);
		parent.isCountDesc("");
	});
	
	function toExcel()
	{
		var is_exp=$("input[@name='is_exp']").val();
		if(is_exp==0){
			$("input[@name='is_exp']").val("1");
			parent.query('exp',$("input[@name='queryTime']").val());
		}else{
			alert("�������ڵ����У������ظ�������");
		}
	}
</script>
</head>

<body>
<input type='hidden' name="is_exp" value="0" />
<input type='hidden' name="queryTime" value="<s:property value='queryTime' />" />
<table>
<tr>
	<td>
		<table class="listtable">
			<caption>ȫʡIPTV�û��ն˷ֲ�����Ծ�����</caption>
			<thead>
				<tr>
					<th>��λ</th>
					<th>�ն�����</th>
					<th>����4K������</th>
					<th>���з�4K������</th>
					<th>�Ѳ���̽��</th>
					<th>̽�벿����</th>
					<th>��Ծ��</th>
					<th>��Ծ��</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="data!=null && data.size()>0">
					<s:iterator value="data">
						<tr>
							<td><s:property value="city_name" /></td>
							<td><s:property value="all_num" /></td>
							<td><s:property value="Y_4K_num" /></td>
							<td><s:property value="N_4K_num" /></td>
							<td><s:property value="is_probe_num" /></td>
							<td><s:property value="probe_rate" /></td>
							<td><s:property value="month_active_num" /></td>
							<td><s:property value="month_active_rate" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=8>û��ͳ�Ƶ����ݣ�</td>
					</tr>
				</s:else>
			</tbody>
			<s:if test="data!=null && data.size()>0">
			<tfoot>
				<tr>
					<td colspan="8">
						<img src="<s:url value="/images/excel.gif"/>" border='0' alt='�����б�'
							style='cursor: hand' onclick="toExcel()">
					</td>
				</tr>
			</tfoot>
			</s:if>
			<tr STYLE="display: none">
				<td colspan="8">
					<iframe id="childFrm" src=""></iframe>
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>
</body>
</html>