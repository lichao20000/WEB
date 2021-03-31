<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ɽ����ͨRMSƽ̨�����й����ɹ���ͳ��</title>
<link rel="stylesheet" href="<s:url value='/css3/c_table.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css3/global.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/tab.css'/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
$(function(){
	parent.dyniframesize();
});
$(function(){
	$("button[@name='button']",parent.document).attr("disabled", false);
	$("#trData",parent.document).css("display","none");
	$("#QueryData",parent.document).css("display","none");
});

function queryDataForExcel(starttime,endtime){
	var page = "<s:url value='/itms/resource/stbSheetSuccRate!queryDataForExcel.action'/>?"
		+"starttime="+starttime
		+ "&endtime=" + endtime;
	document.all("childFrm").src=page;
}
</script>
</head>
<body>
	<table class="listtable">
		<caption>
			ͳ�ƽ��
		</caption>
		<thead>
			<tr>
				<th>���� </th>
				<th>FTTH�ɹ���</th>
				<th>FTTH����</th>
				<th>FTTH�ɹ���</th>
				<th>FTTB�ɹ���</th>
				<th>FTTB����</th>
				<th>FTTB�ɹ��� </th>
				<th>LAN�ɹ���</th>
				<th>LAN����</th>
				<th>LAN�ɹ��� </th>
				<th>HGW�ɹ���</th>
				<th>HGW����</th>
				<th>HGW�ɹ��� </th>
				<th>�ܹ����ɹ���</th>
				<th>�ܹ�������</th>
				<th>�ܹ����ɹ���</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="data.size()>0">
				<s:iterator value="data" var="map1">
					<tr>
						<td align="center">
							<s:property value="cityName" />
						</td>
						<td align="center">
							<s:property value="FTTHSuccNum" />
						</td>
						<td align="center">
							<s:property value="FTTHTotalNum" />
						</td>
						<td align="center">
							<s:property value="FTTHSuccRate" />
						</td>
						<td align="center">
							<s:property value="FTTBSuccNum" />
						</td>
						<td align="center">
							<s:property value="FTTBTotalNum" />
						</td>
						<td align="center">
							<s:property value="FTTBSuccRate" />
						</td>
						<td align="center">
							<s:property value="LANSuccNum" />
						</td>
						<td align="center">
							<s:property value="LANTotalNum" />
						</td>
						<td align="center">
							<s:property value="LANSuccRate" />
						</td>
						<td align="center">
							<s:property value="HGWSuccNum" />
						</td>
						<td align="center">
							<s:property value="HGWTotalNum" />
						</td>
						<td align="center">
							<s:property value="HGWSuccRate" />
						</td>
						<td align="center">
							<s:property value="totalSuccNum" />
						</td>
						<td align="center">
							<s:property value="totalNum" />
						</td>
						<td align="center">
							<s:property value="totalSuccRate" />
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tfoot>
					<tr>
						<td align="left">
							û�������Ϣ
						</td>
					</tr>
				</tfoot>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan='16'>
					<a href="javascript:queryDataForExcel('<s:property value="starttime"/>','<s:property value="endtime"/>')">
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�' style='cursor: hand' />
					</a>
				</td>
			</tr>
		</tfoot>
		<tr STYLE="display: none">
			<td colspan="16">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</table>
</body>
</html>
