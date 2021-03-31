<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>¹Ø±Õ¿í´øDHCP½ÚµãµÄ²ßÂÔ²éÑ¯</title>
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/inmp/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/inmp/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});
</script>
</head>
<body>
	<table class="listtable">
			<thead>
			<tr>
				<th>
					ÒµÎñÃû³Æ
				</th>
				<th>
					¶¨ÖÆÊ±¼ä
				</th>
				<th>
					Ö´ÐÐÊ±¼ä
				</th>
				<th>
					²ßÂÔ×´Ì¬
				</th>
				<th>
					²ßÂÔ½á¹û
				</th>
				<th>
					½á¹ûÃèÊö
				</th>
			</tr>
		</thead>
		<tbody style="background-color:#FFFFFF;height: 25 ">
			<s:if test="list.size()>0">
				<s:iterator value="list">
					<tr style="vertical-align:middle; text-align:center;">
						<td align="center">
							<s:property value="service_id"  />
						</td>
						<td align="center">
							<s:property value="time" />
						</td>
						<td align="center">
							<s:property value="start_time" /> 
						</td>
						<td align="center">
							<s:property value="status" /> 
						</td>
						<td align="center">
							<s:property value="result_id" /> 
						</td>
						<td align="center">
							<s:property value="result_desc" /> 
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6 >
						<font color="red">Ã»ÓÐÂú×ãÌõ¼þµÄÊý¾Ý£¡</font>
					</td>
				</tr>
			</s:else>
		</tbody>
		<tr>
			<td colspan="6" align="right">
				<lk:pages url="/inmp/config/netByDHCPStop!queryNetByDHCPList.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
	</table>
</body>
</html>