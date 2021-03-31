<%@ include file="../../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">

<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">

//����excel��ѯ
function queryDataForExcel(reportType,city_id,timeStart,bookparam){
	
	var url = "gwms/report/deviceException!getDayReport.action";	
	
	url = url + "?reportType="+reportType+"&cityId="+city_id+"&longData="+timeStart+"&bookparam="+bookparam;
	
	url = url+"&isReport=excel"
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

//����pdf��ѯ
function queryDataForPdf(reportType,city_id,timeStart,bookparam){
	
	var url = "gwms/report/deviceException!getDayReport.action";	
	
	url = url + "?reportType="+reportType+"&cityId="+city_id+"&longData="+timeStart+"&bookparam="+bookparam;
	
	url = url+"&isReport=pdf"
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

function queryDataForPrint(reportType,city_id,timeStart,bookparam){
	
	var url = "gwms/report/deviceException!getDayReport.action";	
	
	url = url + "?reportType="+reportType+"&cityId="+city_id+"&longData="+timeStart+"&bookparam="+bookparam;
	url = url+"&isReport=print";
	
	window.open(url,"","left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	
}

</script>

</head>

<body>
	<table border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#000000">
					<tr>
						<s:if test='"none".equals(displayusername)'>
						</s:if>
						<s:else>
							<th>�û��ʺ�</th>
						</s:else>
						<s:if test='"none".equals(displaytime)'>
						</s:if>
						<s:else>
							<th>ʱ��</th>
						</s:else>
						<s:if test='"none".equals(displaycity_name)'>
						</s:if>
						<s:else>
							<th>����</th>
						</s:else>
						<s:if test='"none".equals(displayoui)'>
						</s:if>
						<s:else>
							<th>����</th>
						</s:else>
						<s:if test='"none".equals(displaydevice_serialnumber)'>
						</s:if>
						<s:else>
							<th>�豸���к�</th>
						</s:else>
					</tr>
					<s:iterator value="exceptinList">
						<tr bgcolor="#FFFFFF">
							<s:if test='"none".equals(displayusername)'>
							</s:if>
							<s:else>
								<td class=column1><s:property value="username"/></td>
							</s:else>
							<s:if test='"none".equals(displaytime)'>
							</s:if>
							<s:else>
								<td class=column1><s:property value="time"/></td>
							</s:else>
							<s:if test='"none".equals(displaycity_name)'>
							</s:if>
							<s:else>
								<td class=column1><s:property value="city_name"/></td>
							</s:else>
							<s:if test='"none".equals(displayoui)'>
							</s:if>
							<s:else>
								<td class=column1><s:property value="oui"/></td>
							</s:else>
							<s:if test='"none".equals(displaydevice_serialnumber)'>
							</s:if>
							<s:else>
								<td class=column1><s:property value="device_serialnumber"/></td>
							</s:else>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#000000">
					<tr bgcolor="#FFFFFF">
						<td class=column1 align="left" width="100">
						 	<a href="javascript:queryDataForPrint('<s:property value="reportType"/>','<s:property value="cityId"/>','<s:property value="longData"/>','<s:property value="bookparam"/>');">
					 			<img src="../../images/print.gif"  border="0" width="16" height="16"></img>
						 	</a>
						 	&nbsp;
						 	<a href="javascript:queryDataForExcel('<s:property value="reportType"/>','<s:property value="cityId"/>','<s:property value="longData"/>','<s:property value="bookparam"/>');">
					 			<img src="../../images/excel.gif"  border="0" width="16" height="16"></img>
						 	</a>
						 	&nbsp;
							<a href="javascript:queryDataForPdf('<s:property value="reportType"/>','<s:property value="cityId"/>','<s:property value="longData"/>','<s:property value="bookparam"/>');">
					 			<img src="../../images/pdf.gif"  border="0" width="16" height="16"></img>
						 	</a>
						</td>
						<td class=column1 align="right">
							<strong>
								<s:if test='"1".equals(reportType)'>
									ͳ�����ڣ�<s:property value="hourDataEnd"/>
								</s:if>
								<s:if test='"2".equals(reportType)'>
									ͳ�����ڣ�<s:property value="dayDataEnd"/>
								</s:if>
								<s:if test='"3".equals(reportType)'>
									ͳ�ƽ�ֹʱ�䣺<s:property value="weekDataEnd"/>
								</s:if>
								<s:if test='"4".equals(reportType)'>
									ͳ�ƽ�ֹʱ�䣺<s:property value="monthDataEnd"/>
								</s:if>
							</strong>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="5" align="right">
				<lk:pages url="/gwms/report/deviceException!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
	</table>
</body>
<form name="frm2"></form>
</html>