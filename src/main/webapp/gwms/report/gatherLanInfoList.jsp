<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">

function geNoMatchList(cityId){
    var startOpenDate=window.parent.$("input[@name='startOpenDate']").val();
    var endOpenDate=window.parent.$("input[@name='endOpenDate']").val();
	var strpage="<s:url value='/gwms/report/lanGatherInfo!queryDetail.action'/>?cityId="
	                    + cityId+"&startOpenDate="+startOpenDate+"&endOpenDate="+endOpenDate;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

</script>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center" style="margin-top: 50px" >
		<tr>
			<th class=column1 height="25" align="center"><strong>lan��Ϣ�ɼ�ͳ�ƽ���</strong>
			</th>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center" bgcolor="#999999"  style="overflow-y: auto" >
					<tr bgcolor="#FFFFFF">
						<th>����</th>
						<th>GE������</th>
						<th>GE��������</th>
						<th>GE�ڰ���ռ��</th>
						<th>lan3��������</th>
                        <th>lan4��������</th>
                        <th>GE�ڲ�ƥ����ϸ�嵥</th>
					</tr>
					<s:iterator value="lanInfoList">
						<tr bgcolor="#FFFFFF">
							<td width="14%">
								<s:property value="city_name" />
							</td>
							<td width="14%">
								<s:property value="geDevNum" />
							</td>
							<td width="14%">
								<s:property value="geUseNum" />
							</td>
							<td width="14%">
								<s:property value="geNoUsePer" />
							</td>
							<td width="14%">
								<s:property value="lan3Num" />
							</td>
							<td width="14%">
                            	<s:property value="lan4Num" />
                            </td>
                            <td width="16%">
                                <IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="��ϸ��Ϣ"
                                    onclick="geNoMatchList('<s:property value="city_id"/>')"
                                    style="cursor: hand">
                            </td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
