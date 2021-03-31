<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * 异常设备的处理或者处理信息查询
 * @author 王志猛(工号) tel：1234567890123
 * @version 1.0
 * @since 2008-6-11 下午05:21:28
 *
 * 版权：南京联创科技 网管科技部
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Insert title here</title>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css/liulu.css"/>" type="text/css">
<style type="text/css">
.table,.table tr,.table td ,.table th{
	border-style: solid;
	border-width: 1px;
	border-color: black;
	border-collapse: collapse;
	border-spacing: 0px;
}

.table {
	width: 100%;
}
</style>
<SCRIPT type="text/javascript">
function doExpDev()
{
	var tx= $("textarea:first");
	if(tx.val()==""||tx.val()==null)
	{
		alert("请填写处理信息");
		return;
	}
	$.post("<s:url value="/bbms/ExceptionDevice!doExpDev.action"/>",{device_id:'<s:property value="device_id"/>',doinfo:$.cc("textarea:first"),exception_time:<s:property value="exception_time"/>},function(data){
		if(data=="success")
		{
			alert("处理成功.");
			opener.frm.submit();
			window.close();
		}
		else
		{
			alert("处理失败，请重新保存.");
		}
	});
}
</SCRIPT>
</head>
<body>
<s:if test="viewStaus==3">
<table style="width:100%" class="table">
<tr><th>请填入处理描述信息</th></tr>
<tr><td>
<textarea cols="40" rows="5" style="width:100%" name="doinfo"></textarea>
</td></tr>
<tr><td align="right"><button  class="jianbian" onclick=" doExpDev();">保存</button></td></tr>
</table>
</s:if>
<s:else>
<table class="table" style="width:100%">
<tr><th colspan="4">异常设备<s:property value="viewInfo.device_info"/>的确认信息</th></tr>
<tr><td class="column">异常原因</td><td><s:property value="viewInfo.exceptionType"/></td><td class="column">处理结果</td><td><s:property value="viewInfo.dealResult"/></td></tr>
<tr><td class="column">处理账号</td><td><s:property value="viewInfo.acc_loginname"/></td><td class="column">处理日期</td><td><s:date name="viewInfo.deal_time"/></td></tr>
<tr><td class="column">处理信息</td><td colspan="3"><s:property value="viewInfo.result_desc"/></td></tr>
<tr><td colspan="4" align="right"><button onclick="window.close();" class="jianbian">关闭</button></td></tr>
</table>
</s:else>
</body>
</html>