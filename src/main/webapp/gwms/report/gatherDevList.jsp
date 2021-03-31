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
	var strpage="<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
	window.open(strpage,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

</script>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center"  >
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center" bgcolor="#999999"  style="overflow-y: auto" >
					<tr bgcolor="#FFFFFF">
						<th>属地</th>
						<th>绑定终端数</th>
						<th>采集终端的数量</th>
						<th>采集终端的占比</th>
						<th>使用wifi终端量</th>
                        <th>使用GE口终端量</th>
                        <th>同时使用wifi和GE口终端量</th>
					</tr>
					<s:iterator value="taskList">
						<tr bgcolor="#FFFFFF">
							<td width="14%">
								<s:if test="''==city">
								其他
								</s:if>
								<s:else>
								<s:property value="city" />
								</s:else>
							</td>
							<td width="14%">
								<s:property value="sum" />
							</td>
							<td width="14%">
								<s:property value="sum1" />
							</td>
							<td width="14%">
								<s:property value="devper" />%
							</td>
							<td width="14%">
								<s:property value="sum2" />
							</td>
							<td width="14%">
                            	<s:property value="sum3" />
                            </td>
                            <td width="16%">
								<s:property value="sum4" />
                            </td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
